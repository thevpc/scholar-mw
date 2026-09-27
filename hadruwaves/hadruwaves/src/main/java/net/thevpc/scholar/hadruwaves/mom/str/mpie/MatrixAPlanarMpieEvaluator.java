package net.thevpc.scholar.hadruwaves.mom.str.mpie;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.geom.HPoint;
import net.thevpc.scholar.hadrumaths.geom.HTriangle;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.RWG;
import net.thevpc.scholar.hadruwaves.Material;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.RWGDeltaGapBMatrix;
import net.thevpc.scholar.hadruwaves.mom.mpie.MpiePotentialHelper;
import net.thevpc.scholar.hadruwaves.mom.str.MatrixAEvaluator;

import java.util.stream.IntStream;

/**
 * Computes the MoM interaction matrix A for planar microstrip geometries using
 * Spatial-Domain Mixed Potential Integral Equation (MPIE).
 */
public class MatrixAPlanarMpieEvaluator implements MatrixAEvaluator {
    public static final MatrixAPlanarMpieEvaluator INSTANCE = new MatrixAPlanarMpieEvaluator();

    @Override
    public ComplexMatrix evaluate(MomStructure str, ProgressMonitor monitor) {
        DoubleToVector[] g = str.testFunctions().toArray();
        int N = g.length;
        if (N == 0) {
            return Maths.zerosMatrix(0, 0);
        }

        double freq = str.getFrequency();
        double omega = 2.0 * Math.PI * freq;

        // Substrate parameters
        double h0 = 0.0016; // default 1.6mm
        double epsr = 1.0;
        double tand = 0.0;
        if (str.getFirstBoxSpace() != null) {
            if (str.getFirstBoxSpace().getWidth() > 0) {
                h0 = str.getFirstBoxSpace().getWidth();
            }
            Material mat = str.getFirstBoxSpace().getMaterial();
            if (mat != null) {
                epsr = mat.permittivity();
                tand = mat.lossTangent();
            }
        }

        // Microstrip effective permittivity (Wheeler/Hammerstad quasi-TEM formulation for microstrip on grounded substrate):
        double charW = 2.0 * h0; // default characteristic conductor width
        Object hintCharW = str.getHintsManager() != null ? str.getHintsManager().getHint("charW", str.getHintsManager().getHint("characteristicWidth")) : null;
        Object hintEpsEff = str.getHintsManager() != null ? str.getHintsManager().getHint("epsEff", str.getHintsManager().getHint("effectivePermittivity")) : null;
        String charWProp = System.getProperty("hadruwaves.mom.mpie.charW");
        String epsEffProp = System.getProperty("hadruwaves.mom.mpie.epsEff");

        if (hintCharW instanceof Number) {
            charW = ((Number) hintCharW).doubleValue();
        } else if (charWProp != null && !charWProp.isEmpty()) {
            charW = Double.parseDouble(charWProp);
        } else if (N > 0) {
            // Auto-detect characteristic width from conductor geometry:
            // Exterior boundary edges occur once across all triangles; interior edges occur twice.
            // Characteristic width (hydraulic diameter) = 4 * TotalArea / Perimeter
            java.util.Set<HTriangle> uniqueTriangles = new java.util.HashSet<>();
            for (int i = 0; i < N; i++) {
                RWG r = RWGDeltaGapBMatrix.tryUnwrapRWG(g[i]);
                if (r != null) {
                    uniqueTriangles.add(r.tr1);
                    uniqueTriangles.add(r.tr2);
                }
            }

            double totalArea = 0;
            java.util.Map<String, Double> edgeLengths = new java.util.HashMap<>();
            java.util.Map<String, Integer> edgeCounts = new java.util.HashMap<>();
            for (HTriangle tri : uniqueTriangles) {
                totalArea += tri.area();
                HPoint[] pts = {tri.p1(), tri.p2(), tri.p3()};
                for (int e = 0; e < 3; e++) {
                    HPoint pA = pts[e];
                    HPoint pB = pts[(e + 1) % 3];
                    long xa = Math.round(pA.x * 1e6);
                    long ya = Math.round(pA.y * 1e6);
                    long xb = Math.round(pB.x * 1e6);
                    long yb = Math.round(pB.y * 1e6);
                    String key = (xa < xb || (xa == xb && ya <= yb)) ? (xa + "_" + ya + ":" + xb + "_" + yb) : (xb + "_" + yb + ":" + xa + "_" + ya);
                    edgeCounts.put(key, edgeCounts.getOrDefault(key, 0) + 1);
                    if (!edgeLengths.containsKey(key)) {
                        edgeLengths.put(key, pA.distance(pB));
                    }
                }
            }

            double perimeter = 0;
            for (java.util.Map.Entry<String, Integer> entry : edgeCounts.entrySet()) {
                if (entry.getValue() == 1) {
                    perimeter += edgeLengths.get(entry.getKey());
                }
            }

            if (perimeter > 0 && totalArea > 0) {
                charW = 4.0 * totalArea / perimeter;
            }
        }
        double epsEff = epsr > 1.0 ? ((epsr + 1.0) / 2.0 + (epsr - 1.0) / 2.0 / Math.sqrt(1.0 + 12.0 * (h0 / charW))) : 1.0;
        if (hintEpsEff instanceof Number) {
            epsEff = ((Number) hintEpsEff).doubleValue();
        } else if (epsEffProp != null && !epsEffProp.isEmpty()) {
            epsEff = Double.parseDouble(epsEffProp);
        }
        str.log().log(net.thevpc.nuts.text.NMsg.ofC("MPIE Evaluator: charW=%.4f mm, epsEff=%.4f, f=%.3f GHz", charW * 1000, epsEff, freq / 1e9));
        double k0 = omega / Maths.C;
        final double k = k0 * Math.sqrt(epsEff);
        final double h = h0;

        // MPIE prefactors:
        // Z_mn = j*omega*mu0 * <fm, An> + 1/(j*omega*eps0*epsC) * <div fm, Phin>
        // Complex permittivity with substrate loss tangent:
        Complex epsC = tand > 0 ? Complex.of(epsEff, -epsEff * tand) : Complex.of(epsEff, 0);
        final Complex cA = Complex.I.mul(omega * Maths.U0);
        final Complex cV = Complex.I.mul(omega * Maths.EPS0).mul(epsC).inv();

        final Complex Zs = str.getSerialZs() != null ? str.getSerialZs().impedanceValue() : Complex.ZERO;

        RWG[] rwgs = new RWG[N];
        for (int i = 0; i < N; i++) {
            rwgs[i] = RWGDeltaGapBMatrix.tryUnwrapRWG(g[i]);
            if (rwgs[i] == null) {
                throw new IllegalArgumentException("Spatial MPIE solver currently requires RWG basis functions. Function " + i + " is " + g[i]);
            }
        }

        Complex[][] A = new Complex[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                A[i][j] = Complex.ZERO;
            }
        }

        ProgressMonitor prog = ProgressMonitors.incremental(monitor, N);

        // Parallel loop over row i
        IntStream.range(0, N).parallel().forEach(i -> {
            RWG rwgi = rwgs[i];
            HTriangle t1_i = rwgi.tr1;
            HTriangle t2_i = rwgi.tr2;
            HPoint v1_i = t1_i.p1();
            HPoint v2_i = t2_i.p1();
            double a1_i = t1_i.area();
            double a2_i = t2_i.area();
            double li = rwgi.edgeLength;

            for (int j = i; j < N; j++) {
                RWG rwgj = rwgs[j];
                HTriangle t1_j = rwgj.tr1;
                HTriangle t2_j = rwgj.tr2;
                HPoint v1_j = t1_j.p1();
                HPoint v2_j = t2_j.p1();
                double a1_j = t1_j.area();
                double a2_j = t2_j.area();
                double lj = rwgj.edgeLength;

                Complex zij_V = Complex.ZERO;
                Complex zij_A = Complex.ZERO;

                // 4 triangle pair combinations: (T1_i, T1_j), (T1_i, T2_j), (T2_i, T1_j), (T2_i, T2_j)
                // Combination (1, 1): sign = +1 * +1 = +1
                MpiePotentialHelper.InteractionResult r11 = MpiePotentialHelper.computeTriangleInteraction(
                        t1_i, v1_i, t1_j, v1_j, k, k0, h
                );
                zij_V = zij_V.plus(r11.scalarIntegral.mul((+1.0 * li / a1_i) * (+1.0 * lj / a1_j)));
                zij_A = zij_A.plus(r11.vectorIntegral.mul((+1.0 * li / (2.0 * a1_i)) * (+1.0 * lj / (2.0 * a1_j))));

                // Combination (1, 2): sign = +1 * -1 = -1
                MpiePotentialHelper.InteractionResult r12 = MpiePotentialHelper.computeTriangleInteraction(
                        t1_i, v1_i, t2_j, v2_j, k, k0, h
                );
                zij_V = zij_V.plus(r12.scalarIntegral.mul((+1.0 * li / a1_i) * (-1.0 * lj / a2_j)));
                zij_A = zij_A.plus(r12.vectorIntegral.mul((+1.0 * li / (2.0 * a1_i)) * (-1.0 * lj / (2.0 * a2_j))));

                // Combination (2, 1): sign = -1 * +1 = -1
                MpiePotentialHelper.InteractionResult r21 = MpiePotentialHelper.computeTriangleInteraction(
                        t2_i, v2_i, t1_j, v1_j, k, k0, h
                );
                zij_V = zij_V.plus(r21.scalarIntegral.mul((-1.0 * li / a2_i) * (+1.0 * lj / a1_j)));
                zij_A = zij_A.plus(r21.vectorIntegral.mul((-1.0 * li / (2.0 * a2_i)) * (+1.0 * lj / (2.0 * a1_j))));

                // Combination (2, 2): sign = -1 * -1 = +1
                MpiePotentialHelper.InteractionResult r22 = MpiePotentialHelper.computeTriangleInteraction(
                        t2_i, v2_i, t2_j, v2_j, k, k0, h
                );
                zij_V = zij_V.plus(r22.scalarIntegral.mul((-1.0 * li / a2_i) * (-1.0 * lj / a2_j)));
                zij_A = zij_A.plus(r22.vectorIntegral.mul((-1.0 * li / (2.0 * a2_i)) * (-1.0 * lj / (2.0 * a2_j))));

                Complex zij = zij_A.mul(cA).plus(zij_V.mul(cV));

                if (i == j && !Zs.isZero()) {
                    // Add surface resistance
                    double normSq1 = (li / (2.0 * a1_i)) * (li / (2.0 * a1_i)) * triangleVertexNormSq(t1_i);
                    double normSq2 = (li / (2.0 * a2_i)) * (li / (2.0 * a2_i)) * triangleVertexNormSq(t2_i);
                    zij = zij.plus(Zs.mul(normSq1 + normSq2));
                }

                A[i][j] = zij;
                A[j][i] = zij;
            }
            prog.inc();
        });

        return Maths.matrix(A);
    }

    private static double triangleVertexNormSq(HTriangle t) {
        double dx2 = t.p2().x - t.p1().x;
        double dy2 = t.p2().y - t.p1().y;
        double dx3 = t.p3().x - t.p1().x;
        double dy3 = t.p3().y - t.p1().y;
        double l2Sq = dx2 * dx2 + dy2 * dy2;
        double l3Sq = dx3 * dx3 + dy3 * dy3;
        double dot23 = dx2 * dx3 + dy2 * dy3;
        return (t.area() / 6.0) * (l2Sq + l3Sq + dot23);
    }

    @Override
    public NElement toElement() {
        return NElement.ofObjectBuilder(getClass().getSimpleName()).build();
    }
}
