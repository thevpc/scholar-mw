package net.thevpc.scholar.hadruwaves.mom.mpie;

import net.thevpc.nuts.Nuts;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.cache.CacheMode;
import net.thevpc.scholar.hadrumaths.geom.*;
import net.thevpc.scholar.hadrumaths.meshalgo.triconsdes.MeshTriangulationOptions;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.RWG;
import net.thevpc.scholar.hadruwaves.Material;
import net.thevpc.scholar.hadruwaves.WallBorders;
import net.thevpc.scholar.hadruwaves.mom.*;
import net.thevpc.scholar.hadruwaves.mom.sources.planar.CstPlanarSource;
import net.thevpc.scholar.hadruwaves.mom.sources.planar.DefaultPlanarSources;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A 3.1 mm strip on 1.6 mm FR4 (the array deck's 50-ohm line), fed by a delta gap, open at both ends.
 * The RWG current is integrated across the strip at many cuts and fitted with forward/backward
 * waves: this gives beta (hence epsEff) and the effective open ends; the gap impedance of the two
 * open stubs in series then gives Z0. References: Kirschning-Jansen dispersive epsEff and
 * Hammerstad-Jensen Z0, coded here independently.
 */
public class MpieLayeredLineTest {

    static final double ER = 4.4, H = 1.6e-3, WF = 3.1e-3, F = 6.5e9;
    // long side +y, short side -y, gap near y = 0. Lengths put beta*d near pi/4 + n pi on both sides
    // (d = gap-to-effective-open-end), so cot(beta d) ~ 1 and the Z0 extraction is well conditioned.
    static final double LA = 39.5e-3, LB1 = 14.5e-3, LB2 = 9.5e-3;

    @BeforeAll
    public static void init() {
        Nuts.require();
    }

    // ------------------------------------------------------------------ closed-form references
    static double epsEff0HJ(double u, double er) {
        double a = 1 + Math.log((Math.pow(u, 4) + Math.pow(u / 52, 2)) / (Math.pow(u, 4) + 0.432)) / 49
                + Math.log(1 + Math.pow(u / 18.1, 3)) / 18.7;
        double b = 0.564 * Math.pow((er - 0.9) / (er + 3), 0.053);
        return (er + 1) / 2 + (er - 1) / 2 * Math.pow(1 + 10 / u, -a * b);
    }

    static double z0HJ(double u, double er) {
        double eta0 = 376.730313668;
        double fu = 6 + (2 * Math.PI - 6) * Math.exp(-Math.pow(30.666 / u, 0.7528));
        double z01 = eta0 / (2 * Math.PI) * Math.log(fu / u + Math.sqrt(1 + 4 / (u * u)));
        return z01 / Math.sqrt(epsEff0HJ(u, er));
    }

    /** Kirschning-Jansen 1982, fn = f[GHz] * h[mm]. */
    static double epsEffKJ(double u, double er, double fGHz, double hmm) {
        double fn = fGHz * hmm;
        double e0 = epsEff0HJ(u, er);
        double p1 = 0.27488 + (0.6315 + 0.525 / Math.pow(1 + 0.0157 * fn, 20)) * u - 0.065683 * Math.exp(-8.7513 * u);
        double p2 = 0.33622 * (1 - Math.exp(-0.03442 * er));
        double p3 = 0.0363 * Math.exp(-4.6 * u) * (1 - Math.exp(-Math.pow(fn / 38.7, 4.97)));
        double p4 = 1 + 2.751 * (1 - Math.exp(-Math.pow(er / 15.916, 8)));
        double p = p1 * p2 * Math.pow((0.1844 + p3 * p4) * fn, 1.5763);
        return er - (er - e0) / (1 + p);
    }

    @Test
    public void closedFormReferencesSanity() {
        // textbook values: w/h = 1.94 on epsr 4.4 is the classic 50-ohm FR4 line
        double u = WF / H;
        assertEquals(3.34, epsEff0HJ(u, ER), 0.02);
        assertEquals(50.0, z0HJ(u, ER), 1.5);
        double kj = epsEffKJ(u, ER, F / 1e9, H * 1e3);
        assertTrue(kj > epsEff0HJ(u, ER) && kj < ER, "dispersion raises epsEff, below epsr");
        // independent dispersion model (Getsinger 1973): fp = Z0/(2 mu0 h), G = 0.6 + 0.009 Z0
        double z0 = z0HJ(u, ER);
        double fp = z0 / (2 * 4e-7 * Math.PI * H);
        double g = 0.6 + 0.009 * z0;
        double getsinger = ER - (ER - epsEff0HJ(u, ER)) / (1 + g * Math.pow(F / fp, 2));
        assertEquals(getsinger, kj, 0.03 * kj, "Kirschning-Jansen vs Getsinger");
    }

    // ------------------------------------------------------------------ the MoM experiment
    static final class Result {
        double beta, epsEff, fitResidual, cotSum, yGap, yeLong, yeShort;
        Complex zin;
    }

    private static HGeometry box(double x, double y, double w, double h) {
        return new DefaultHPolygon(HPoint.create(x, y), HPoint.create(x + w, y), HPoint.create(x + w, y + h), HPoint.create(x, y + h));
    }

    /** Current through the cut y = c: sum_n X_n Int_cut f_n,y dx, exact for RWG (f_y constant along x). */
    static Complex currentAt(double c, RWG[] rwgs, ComplexMatrix x) {
        Complex sum = Complex.ZERO;
        for (int n = 0; n < rwgs.length; n++) {
            RWG r = rwgs[n];
            double s = 0;
            HTriangle[] ts = {r.tr1, r.tr2};
            for (int k = 0; k < 2; k++) {
                HTriangle t = ts[k];
                double len = cutLength(t, c);
                if (len == 0) {
                    continue;
                }
                double sign = k == 0 ? 1 : -1;
                // f = sign * (l / 2A) (r - v), v = free vertex p1()
                s += sign * r.edgeLength / (2 * t.area()) * (c - t.p1().y) * len;
            }
            if (s != 0) {
                sum = sum.plus(x.get(n, 0).mul(s));
            }
        }
        return sum;
    }

    static double cutLength(HTriangle t, double c) {
        HPoint[] p = {t.p1(), t.p2(), t.p3()};
        double xmin = Double.POSITIVE_INFINITY, xmax = Double.NEGATIVE_INFINITY;
        int hits = 0;
        for (int i = 0; i < 3; i++) {
            HPoint a = p[i], b = p[(i + 1) % 3];
            if ((a.y - c) * (b.y - c) < 0) {
                double xx = a.x + (c - a.y) * (b.x - a.x) / (b.y - a.y);
                xmin = Math.min(xmin, xx);
                xmax = Math.max(xmax, xx);
                hits++;
            }
        }
        return hits == 2 ? xmax - xmin : 0;
    }

    /** least squares I(y) = a e^{-j beta y} + b e^{+j beta y}; returns {a, b, relative residual}. */
    static Object[] fitWaves(double beta, double[] ys, Complex[] is) {
        Complex[][] m = new Complex[ys.length][2];
        Complex[][] rhs = new Complex[ys.length][1];
        for (int i = 0; i < ys.length; i++) {
            m[i][0] = Complex.of(Math.cos(beta * ys[i]), -Math.sin(beta * ys[i]));
            m[i][1] = Complex.of(Math.cos(beta * ys[i]), Math.sin(beta * ys[i]));
            rhs[i][0] = is[i];
        }
        ComplexMatrix a = Maths.matrix(m);
        ComplexMatrix ah = a.transposeHermitian();
        ComplexMatrix sol = ah.mul(a).solve(ah.mul(Maths.matrix(rhs)));
        double res = 0, nrm = 0;
        for (int i = 0; i < ys.length; i++) {
            Complex fit = m[i][0].mul(sol.get(0, 0)).plus(m[i][1].mul(sol.get(1, 0)));
            res += fit.minus(is[i]).absdblsqr();
            nrm += is[i].absdblsqr();
        }
        return new Object[]{sol.get(0, 0), sol.get(1, 0), Math.sqrt(res / nrm)};
    }

    /** position of the open end (current zero) implied by the wave amplitudes, nearest to yGuess. */
    static double openEnd(double beta, Complex a, Complex b, double yGuess) {
        // I(ye) = 0  <=>  e^{2j beta ye} = -a/b
        double phase = Complex.ZERO.minus(a).div(b).arg().toDouble();
        double ye = phase / (2 * beta);
        double period = Math.PI / beta;
        return ye + Math.rint((yGuess - ye) / period) * period;
    }

    static Result run(boolean layered, double LB) {
        Maths.Config.setCacheEnabled(false);
        Maths.Config.setPersistenceCacheMode(CacheMode.DISABLED);
        HGeometry geom = box(-WF / 2, -LB, WF, LB).addGeometry(box(-WF / 2, 0, WF, LA));
        TestFunctions tf = TestFunctionsFactory.createRWG(geom, new MeshTriangulationOptions().setMaxEdgeLength(1.6e-3).setAdaptive(false));
        MomStructure mom = new MomStructure();
        mom.setProjectType(ProjectType.PLANAR_STRUCTURE);
        mom.setDomain(Domain.ofBounds(-30e-3, 30e-3, -30e-3, 50e-3));
        mom.setBorders(WallBorders.EEEE);
        mom.setFirstBoxSpace(BoxSpace.shortCircuit(Material.substrate("FR4", ER, 0.0), H));
        mom.setSecondBoxSpace(BoxSpace.matchedLoad(Material.VACUUM));
        mom.setCircuitType(CircuitType.SERIAL);
        mom.setSolverType(MomSolverType.SPATIAL_MPIE);
        mom.setFrequency(F);
        mom.setTestFunctions(tf);
        mom.getHintsManager().setHint("charW", WF); // legacy kernel: epsEff of this line (its best case)
        if (layered) {
            mom.getHintsManager().setHint("mpieGreen", "layered");
        }
        // the mesher does not put a node row at y = 0: put the gap on the row of edges nearest to 0
        // (a row = at least 3 shared-edge midpoints with the same y)
        java.util.Map<Long, Integer> rows = new java.util.TreeMap<>();
        for (DoubleToVector f : tf.toArray()) {
            HPoint m = RWGDeltaGapBMatrix.tryUnwrapRWG(f).getSharedEdgeMidpoint();
            if (Math.abs(m.y) < 1.5e-3) {
                rows.merge(Math.round(m.y * 1e7), 1, Integer::sum);
            }
        }
        double y0 = Double.NaN;
        for (java.util.Map.Entry<Long, Integer> e : rows.entrySet()) {
            double y = e.getKey() / 1e7;
            if (e.getValue() >= 3 && (Double.isNaN(y0) || Math.abs(y) < Math.abs(y0))) {
                y0 = y;
            }
        }
        assertFalse(Double.isNaN(y0), "no edge row near y=0");
        final double yGap = y0;
        Domain gap = Domain.ofPoints(-WF, yGap - 2e-6, WF, yGap + 2e-6);
        mom.setSources(new DefaultPlanarSources(CstPlanarSource.ofVoltage(1.0, gap, Axis.Y, Complex.of(50))));

        ComplexMatrix A = mom.matrixA().evalMatrix();
        ComplexMatrix B = mom.matrixB().evalMatrix();
        int portEdges = 0;
        for (int i = 0; i < B.getRowCount(); i++) {
            if (!B.get(i, 0).isZero()) {
                portEdges++;
            }
        }
        assertTrue(portEdges >= 2, "gap must cut the strip, found " + portEdges + " port edges");
        ComplexMatrix X = A.solve(B);
        DoubleToVector[] fs = tf.toArray();
        RWG[] rwgs = new RWG[fs.length];
        for (int i = 0; i < fs.length; i++) {
            rwgs[i] = RWGDeltaGapBMatrix.tryUnwrapRWG(fs[i]);
        }
        Result r = new Result();
        r.zin = B.transposeHermitian().mul(X).get(0, 0).inv();

        // long side: cuts from 5 mm after the gap to 5 mm before the end, off the mesh nodes
        int nc = 40;
        double[] ys = new double[nc];
        Complex[] is = new Complex[nc];
        for (int i = 0; i < nc; i++) {
            ys[i] = 8e-3 + (LA - 16e-3) * (i + 0.37) / nc;
            is[i] = currentAt(ys[i], rwgs, X);
        }
        double k0 = 2 * Math.PI * F / GroundedSlabGreen.C0;
        double best = 0, bestRes = Double.POSITIVE_INFINITY;
        for (double b = 1.3 * k0; b <= 2.1 * k0; b += 0.0005 * k0) {
            double res = (Double) fitWaves(b, ys, is)[2];
            if (res < bestRes) {
                bestRes = res;
                best = b;
            }
        }
        // golden-section refinement
        double lo = best - 0.0005 * k0, hi = best + 0.0005 * k0;
        for (int it = 0; it < 60; it++) {
            double m1 = hi - 0.618 * (hi - lo), m2 = lo + 0.618 * (hi - lo);
            if ((Double) fitWaves(m1, ys, is)[2] < (Double) fitWaves(m2, ys, is)[2]) {
                hi = m2;
            } else {
                lo = m1;
            }
        }
        r.beta = 0.5 * (lo + hi);
        Object[] fl = fitWaves(r.beta, ys, is);
        if (Boolean.getBoolean("dumpCurrent")) {
            for (double y = -LB + 0.25e-3; y < LA; y += 0.5e-3) {
                Complex c = currentAt(y, rwgs, X);
                Complex fit = ((Complex) fl[0]).mul(Complex.of(Math.cos(r.beta * y), -Math.sin(r.beta * y)))
                        .plus(((Complex) fl[1]).mul(Complex.of(Math.cos(r.beta * y), Math.sin(r.beta * y))));
                System.out.printf("I y=%7.3f |I|=%.5e arg=%7.2f  |fit|=%.5e%n", y * 1e3, c.absdbl(), Math.toDegrees(c.arg().toDouble()), fit.absdbl());
            }
        }
        r.fitResidual = (Double) fl[2];
        r.epsEff = Math.pow(r.beta / k0, 2);
        double yeLong = openEnd(r.beta, (Complex) fl[0], (Complex) fl[1], LA);
        // short side with the same beta
        int ns = 12;
        double[] ys2 = new double[ns];
        Complex[] is2 = new Complex[ns];
        for (int i = 0; i < ns; i++) {
            ys2[i] = -LB + 3e-3 + (LB - 6e-3) * (i + 0.37) / ns;
            is2[i] = currentAt(ys2[i], rwgs, X);
        }
        Object[] fs2 = fitWaves(r.beta, ys2, is2);
        double yeShort = openEnd(r.beta, (Complex) fs2[0], (Complex) fs2[1], -LB);
        // gap sees two open stubs in series: Zline = -j Z0 (cot(beta d1) + cot(beta d2))
        r.cotSum = 1 / Math.tan(r.beta * (yeLong - yGap)) + 1 / Math.tan(r.beta * (yGap - yeShort));
        r.yGap = yGap;
        r.yeLong = yeLong;
        r.yeShort = yeShort;
        System.out.printf("gap at y=%.3f mm, %d unknowns, %d port edges%n", yGap * 1e3, fs.length, portEdges);
        System.out.printf("%s kernel LB=%.1f mm: beta/k0=%.5f epsEff=%.4f fitRes=%.2e openEnds=(%.3f, %.3f) mm (dL=%.3f, %.3f) Zin=%s cotSum=%.3f%n",
                layered ? "layered" : "legacy", LB * 1e3, r.beta / k0, r.epsEff, r.fitResidual, yeShort * 1e3, yeLong * 1e3,
                (yeLong - LA) * 1e3, (-LB - yeShort) * 1e3, r.zin, r.cotSum);
        return r;
    }

    @Test
    public void fiftyOhmLine() {
        double u = WF / H;
        double kj = epsEffKJ(u, ER, F / 1e9, H * 1e3);
        double z0 = z0HJ(u, ER);
        System.out.printf("references: epsEff(KJ, %.1f GHz)=%.4f, epsEff0(HJ)=%.4f, Z0(HJ)=%.2f%n", F / 1e9, kj, epsEff0HJ(u, ER), z0);
        double fp = z0 / (2 * 4e-7 * Math.PI * H);
        double getsinger = ER - (ER - epsEff0HJ(u, ER)) / (1 + (0.6 + 0.009 * z0) * Math.pow(F / fp, 2));
        System.out.printf("           epsEff(Getsinger)=%.4f%n", getsinger);
        double[] legacy = lineParameters(false);
        double[] layered = lineParameters(true);
        System.out.printf("legacy : epsEff=%.4f Z0=%.2f Cgap=%.1f fF%nlayered: epsEff=%.4f Z0=%.2f Cgap=%.1f fF%n",
                legacy[0], legacy[1], legacy[2] * 1e15, layered[0], layered[1], layered[2] * 1e15);
        // the two closed-form dispersion models differ by ~1.7% here; require agreement with both
        assertEquals(kj, layered[0], 0.02 * kj, "layered epsEff vs Kirschning-Jansen");
        assertEquals(getsinger, layered[0], 0.02 * getsinger, "layered epsEff vs Getsinger");
        assertEquals(z0, layered[1], 0.05 * z0, "layered Z0 vs Hammerstad-Jensen");
        assertTrue(layered[2] > 0, "gap capacitance must be positive");
    }

    /**
     * The least-squares port de-embedding (default with the layered kernel): a gap 1.5 mm from one end
     * of a 40 mm open line. Referenced to the gap plane, S11 is the open end's reflection carried back
     * along the line: |S11| close to 1 (only radiation and surface-wave loss), phase -2 beta d_eff, with
     * d_eff the distance to the effective open end measured independently from the current fit.
     */
    @Test
    public void lsqDeembeddingOfAnOpenLine() {
        Result r = run(true, 1.5e-3); // builds and solves the same structure, gives beta and the open end
        Maths.Config.setCacheEnabled(false);
        HGeometry geom = box(-WF / 2, -1.5e-3, WF, 1.5e-3).addGeometry(box(-WF / 2, 0, WF, LA));
        TestFunctions tf = TestFunctionsFactory.createRWG(geom, new MeshTriangulationOptions().setMaxEdgeLength(1.6e-3).setAdaptive(false));
        MomStructure mom = new MomStructure();
        mom.setProjectType(ProjectType.PLANAR_STRUCTURE);
        mom.setDomain(Domain.ofBounds(-30e-3, 30e-3, -30e-3, 50e-3));
        mom.setBorders(WallBorders.EEEE);
        mom.setFirstBoxSpace(BoxSpace.shortCircuit(Material.substrate("FR4", ER, 0.0), H));
        mom.setSecondBoxSpace(BoxSpace.matchedLoad(Material.VACUUM));
        mom.setCircuitType(CircuitType.SERIAL);
        mom.setSolverType(MomSolverType.SPATIAL_MPIE);
        mom.setFrequency(F);
        mom.setTestFunctions(tf);
        mom.getHintsManager().setHint("mpieGreen", "layered");
        mom.setSources(new DefaultPlanarSources(CstPlanarSource.ofVoltage(1.0,
                Domain.ofPoints(-WF, r.yGap - 2e-6, WF, r.yGap + 2e-6), Axis.Y, Complex.of(50))));
        Complex zin = mom.inputImpedance().evalComplex();
        Complex g = zin.minus(Complex.of(50)).div(zin.plus(Complex.of(50)));
        double expPhase = -2 * r.beta * (r.yeLong - r.yGap);
        double dphi = g.arg().toDouble() - expPhase;
        dphi = Math.atan2(Math.sin(dphi), Math.cos(dphi));
        System.out.printf("LSQ de-embedding: S11=%s |S11|=%.4f, phase error vs -2 beta d_eff: %.2f deg%n", g, g.absdbl(), Math.toDegrees(dphi));
        assertTrue(g.absdbl() > 0.9 && g.absdbl() <= 1.0, "open line: |S11| in (0.9, 1], got " + g.absdbl());
        assertEquals(0, dphi, Math.toRadians(5), "S11 phase");
    }

    /**
     * Two runs with different short-stub lengths. A delta gap adds a shunt gap capacitance C (mesh
     * dependent), so Yin_i = j w C + j / (Z0 S_i), S_i = cot(beta d1) + cot(beta d2). The difference of
     * the two admittances cancels C: Z0 = j (1/S1 - 1/S2) / (Y1 - Y2).
     *
     * @return {epsEff (mean), Z0, C}
     */
    static double[] lineParameters(boolean layered) {
        Result r1 = run(layered, LB1);
        Result r2 = run(layered, LB2);
        // a few % of the strip current is not the quasi-TEM standing wave: the TM0 surface wave launched
        // at the gap and the open ends rides on the strip (beta_sw ~ k0, beta_line ~ 1.88 k0)
        if (layered) {
            assertTrue(r1.fitResidual < 0.10 && r2.fitResidual < 0.10, "the current must be mostly a standing wave");
            assertTrue(r1.zin.getReal() >= 0 && r2.zin.getReal() >= 0, "passive: Re Zin >= 0");
        }
        Complex y1 = r1.zin.inv(), y2 = r2.zin.inv();
        Complex num = Complex.of(0, 1 / r1.cotSum - 1 / r2.cotSum);
        Complex z0 = num.div(y1.minus(y2));
        double w = 2 * Math.PI * F;
        Complex jwc = y1.minus(Complex.of(0, 1 / (z0.getReal() * r1.cotSum)));
        System.out.printf("%s: Z0 = %s (imag part should be ~0)%n", layered ? "layered" : "legacy", z0);
        return new double[]{0.5 * (r1.epsEff + r2.epsEff), z0.getReal(), jwc.getImag() / w};
    }
}
