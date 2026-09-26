package net.thevpc.scholar.hadruwaves.mom.str.mpie;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.geom.HPoint;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.RWG;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.RWGDeltaGapBMatrix;
import net.thevpc.scholar.hadruwaves.mom.sources.PlanarSource;
import net.thevpc.scholar.hadruwaves.mom.sources.PlanarSources;
import net.thevpc.scholar.hadruwaves.mom.sources.planar.CstPlanarSource;
import net.thevpc.scholar.hadruwaves.mom.str.MatrixBEvaluator;

import java.util.ArrayList;
import java.util.List;

/**
 * Computes the MoM excitation matrix B for planar microstrip geometries using
 * delta-gap port excitation across RWG basis functions.
 */
public class MatrixBPlanarMpieEvaluator implements MatrixBEvaluator {
    public static final MatrixBPlanarMpieEvaluator INSTANCE = new MatrixBPlanarMpieEvaluator();

    @Override
    public ComplexMatrix evaluate(MomStructure str, ProgressMonitor monitor) {
        PlanarSources planarSources = (PlanarSources) str.getSources();
        if (planarSources == null) {
            throw new IllegalArgumentException("Missing Planar Sources");
        }
        PlanarSource[] sources = planarSources.getPlanarSources();
        if (sources.length == 0) {
            throw new IllegalArgumentException("No sources defined");
        }

        DoubleToVector[] tfs = str.testFunctions().toArray();
        int N = tfs.length;
        int P = sources.length;
        Complex[][] b = new Complex[N][P];

        for (int p = 0; p < P; p++) {
            PlanarSource src = sources[p];
            if (src instanceof CstPlanarSource) {
                CstPlanarSource csrc = (CstPlanarSource) src;
                Domain sourceDomain = csrc.getGeometryList().getDomain();
                Axis polarization = csrc.getPolarization() != null ? csrc.getPolarization() : Axis.Y;

                double V0;
                if (polarization == Axis.X) {
                    V0 = csrc.getXvalue() * sourceDomain.xwidth();
                } else {
                    V0 = csrc.getYvalue() * sourceDomain.ywidth();
                }

                class Candidate {
                    int index;
                    RWG rwg;
                    HPoint mid;
                    double gamma;
                    double coord;
                }

                List<Candidate> candidates = new ArrayList<>();
                for (int n = 0; n < N; n++) {
                    b[n][p] = Complex.ZERO;
                    RWG rwg = RWGDeltaGapBMatrix.tryUnwrapRWG(tfs[n]);
                    if (rwg != null) {
                        HPoint mid = rwg.getSharedEdgeMidpoint();
                        if (sourceDomain.contains(mid.x, mid.y)) {
                            double gamma = rwg.deltaGapGamma(polarization);
                            if (Math.abs(gamma) > 1e-12) {
                                Candidate c = new Candidate();
                                c.index = n;
                                c.rwg = rwg;
                                c.mid = mid;
                                c.gamma = gamma;
                                c.coord = (polarization == Axis.X) ? mid.x : mid.y;
                                candidates.add(c);
                            }
                        }
                    } else {
                        DoubleToVector srcFn = csrc.getFunction();
                        b[n][p] = Maths.scalarProduct(tfs[n], srcFn).toComplex();
                    }
                }

                if (!candidates.isEmpty()) {
                    // Find the cut that provides maximum width coverage across the port line
                    double bestCoverage = 0;
                    double bestCoord = candidates.get(0).coord;
                    double dominantSign = 1.0;
                    double coordClusterTol = 0.5e-3; // 0.5mm cluster tolerance

                    for (Candidate c : candidates) {
                        double covPos = 0;
                        double covNeg = 0;
                        for (Candidate o : candidates) {
                            if (Math.abs(o.coord - c.coord) <= coordClusterTol) {
                                if (o.gamma > 0) covPos += o.gamma;
                                else covNeg += -o.gamma;
                            }
                        }
                        if (covPos > bestCoverage) {
                            bestCoverage = covPos;
                            bestCoord = c.coord;
                            dominantSign = 1.0;
                        }
                        if (covNeg > bestCoverage) {
                            bestCoverage = covNeg;
                            bestCoord = c.coord;
                            dominantSign = -1.0;
                        }
                    }

                    for (Candidate c : candidates) {
                        if (Math.abs(c.coord - bestCoord) <= coordClusterTol && (c.gamma * dominantSign > 0)) {
                            b[c.index][p] = Complex.of(V0 * c.gamma);
                        }
                    }
                }
            } else {
                DoubleToVector srcFn = src.getFunction();
                for (int n = 0; n < N; n++) {
                    b[n][p] = Maths.scalarProduct(tfs[n], srcFn).toComplex();
                }
            }
        }

        return Maths.matrix(b);
    }

    @Override
    public NElement toElement() {
        return NElement.ofObjectBuilder(getClass().getSimpleName()).build();
    }
}
