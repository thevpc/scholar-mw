package net.thevpc.scholar.hadruwaves.mom.str.mpie;

import net.thevpc.nuts.text.NMsg;
import net.thevpc.scholar.hadrumaths.Axis;
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
import net.thevpc.scholar.hadruwaves.mom.sources.PlanarSource;
import net.thevpc.scholar.hadruwaves.mom.sources.PlanarSources;
import net.thevpc.scholar.hadruwaves.mom.sources.planar.CstPlanarSource;
import net.thevpc.scholar.hadrumaths.Domain;

import java.util.*;

/**
 * Standing-wave transmission line de-embedding for RWG Method of Moments.
 * Extracts forward (I+) and backward (I-) traveling waves along uniform
 * feedlines to remove the delta-gap electrostatic near-field singularity
 * and open-ended stub reactances.
 */
public class RWGTransmissionLineDeembedder {

    private static class CutCandidate {
        int index;
        double coord;
        double tMin;
        double tMax;
        double weight;
        double gamma;
    }

    private static class CutGroup {
        double coord;
        double coverage;
        List<CutCandidate> selected;

        public CutGroup(double coord, double coverage, List<CutCandidate> selected) {
            this.coord = coord;
            this.coverage = coverage;
            this.selected = selected;
        }
    }

    public static ComplexMatrix tryDeembed(MomStructure str, ComplexMatrix A, ComplexMatrix B, ComplexMatrix X) {
        if (str == null || B == null || X == null) {
            return null;
        }
        if ("none".equals(deembedMode(str))) {
            return null; // caller uses the raw delta-gap impedance
        }
        int portCount = B.getColumnCount();
        if (portCount == 0) {
            return null;
        }
        DoubleToVector[] tfs = str.testFunctions().toArray();
        if (tfs == null || tfs.length == 0) {
            return null;
        }

        // Substrate parameters
        double h0 = 1.6e-3;
        double epsr = 4.4;
        if (str.getSecondBoxSpace() != null && str.getSecondBoxSpace().getMaterial() != null
                && str.getSecondBoxSpace().getMaterial().permittivity() > 1.0) {
            if (str.getSecondBoxSpace().getWidth() > 0 && !Double.isInfinite(str.getSecondBoxSpace().getWidth())) {
                h0 = str.getSecondBoxSpace().getWidth();
            }
            Material mat = str.getSecondBoxSpace().getMaterial();
            epsr = mat.permittivity();
        } else if (str.getFirstBoxSpace() != null && str.getFirstBoxSpace().getMaterial() != null
                && str.getFirstBoxSpace().getMaterial().permittivity() > 1.0) {
            if (str.getFirstBoxSpace().getWidth() > 0 && !Double.isInfinite(str.getFirstBoxSpace().getWidth())) {
                h0 = str.getFirstBoxSpace().getWidth();
            }
            Material mat = str.getFirstBoxSpace().getMaterial();
            epsr = mat.permittivity();
        }

        Complex[][] Zin = new Complex[portCount][portCount];
        for (int i = 0; i < portCount; i++) {
            for (int j = 0; j < portCount; j++) {
                Zin[i][j] = Complex.ZERO;
            }
        }

        PlanarSources ps = (PlanarSources) str.getSources();
        PlanarSource[] sources = (ps != null) ? ps.getPlanarSources() : null;

        boolean anySuccess = false;
        for (int p = 0; p < portCount; p++) {
            Complex Z0 = Complex.of(50.0);
            if (sources != null && p < sources.length && sources[p].getCharacteristicImpedance() != null) {
                Z0 = sources[p].getCharacteristicImpedance();
            }

            Complex zDeembed = deembedPort(p, str, tfs, B, X, h0, epsr, Z0);
            if (zDeembed != null && !zDeembed.isNaN() && !Double.isInfinite(zDeembed.getReal())) {
                Zin[p][p] = zDeembed;
                anySuccess = true;
            }
        }

        if (!anySuccess) {
            return null;
        }

        // Fill remaining diagonal
        for (int p = 0; p < portCount; p++) {
            if (Zin[p][p].isZero()) {
                Complex rawY = Complex.ZERO;
                for (int i = 0; i < B.getRowCount(); i++) {
                    rawY = rawY.plus(B.get(i, p).conj().mul(X.get(i, p)));
                }
                if (!rawY.isZero()) {
                    Zin[p][p] = rawY.inv();
                } else {
                    Zin[p][p] = Complex.of(50.0);
                }
            }
        }
        return Maths.matrix(Zin);
    }

    private static Complex deembedPort(int p, MomStructure str, DoubleToVector[] tfs,
                                       ComplexMatrix B, ComplexMatrix X,
                                       double h0, double epsr, Complex Z0) {
        int N = B.getRowCount();
        List<Integer> portIndices = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            if (!B.get(i, p).isZero()) {
                portIndices.add(i);
            }
        }
        if (portIndices.isEmpty()) {
            return null;
        }

        Axis polarization = Axis.Y;
        if (str.getSources() instanceof PlanarSources) {
            PlanarSource[] srcs = ((PlanarSources) str.getSources()).getPlanarSources();
            if (srcs != null && p < srcs.length && srcs[p] instanceof CstPlanarSource) {
                CstPlanarSource csrc = (CstPlanarSource) srcs[p];
                if (csrc.getPolarization() != null) {
                    polarization = csrc.getPolarization();
                } else if (csrc.getGeometryList() != null) {
                    Domain d = csrc.getGeometryList().getDomain();
                    if (d != null) {
                        polarization = (d.xwidth() >= d.ywidth()) ? Axis.Y : Axis.X;
                    }
                }
            }
        } else {
            RWG firstRwg = RWGDeltaGapBMatrix.tryUnwrapRWG(tfs[portIndices.get(0)]);
            if (firstRwg != null) {
                HTriangle t1 = firstRwg.getTriangle1();
                double dEdgeX = Math.abs(t1.p3().x - t1.p2().x);
                double dEdgeY = Math.abs(t1.p3().y - t1.p2().y);
                polarization = (dEdgeX >= dEdgeY) ? Axis.Y : Axis.X;
            }
        }
        boolean polY = (polarization == Axis.Y);

        double portCoord = 0;
        double minTrans = Double.POSITIVE_INFINITY;
        double maxTrans = Double.NEGATIVE_INFINITY;
        int validPortCount = 0;
        for (int idx : portIndices) {
            RWG r = RWGDeltaGapBMatrix.tryUnwrapRWG(tfs[idx]);
            if (r != null) {
                HPoint mid = r.getSharedEdgeMidpoint();
                portCoord += polY ? mid.y : mid.x;
                validPortCount++;
                HTriangle tr = r.getTriangle1();
                if (polY) {
                    minTrans = Math.min(minTrans, Math.min(tr.p2().x, tr.p3().x));
                    maxTrans = Math.max(maxTrans, Math.max(tr.p2().x, tr.p3().x));
                } else {
                    minTrans = Math.min(minTrans, Math.min(tr.p2().y, tr.p3().y));
                    maxTrans = Math.max(maxTrans, Math.max(tr.p2().y, tr.p3().y));
                }
            }
        }
        if (validPortCount == 0) {
            return null;
        }
        portCoord /= validPortCount;
        double wf = maxTrans - minTrans;
        if (wf <= 0) {
            return null;
        }

        int countPos = 0;
        int countNeg = 0;
        for (int i = 0; i < N; i++) {
            RWG r = RWGDeltaGapBMatrix.tryUnwrapRWG(tfs[i]);
            if (r != null) {
                HPoint mid = r.getSharedEdgeMidpoint();
                double tCoord = polY ? mid.x : mid.y;
                double lCoord = polY ? mid.y : mid.x;
                if (tCoord >= minTrans - 0.5 * wf && tCoord <= maxTrans + 0.5 * wf) {
                    if (lCoord > portCoord + 1e-4) countPos++;
                    else if (lCoord < portCoord - 1e-4) countNeg++;
                }
            }
        }
        double dir = (countPos >= countNeg) ? 1.0 : -1.0;

        if ("lsq".equals(deembedMode(str))) {
            Complex z = deembedPortLsq(str, tfs, X, p, portCoord, dir, minTrans, maxTrans, polY, h0, epsr, Z0);
            if (z != null) {
                return z;
            }
            str.log().log(NMsg.ofC("[DEEMBED LSQ] failed, falling back to the two-plane de-embedding").asWarning());
        }

        // Effective permittivity (Hammerstad/Wheeler)
        double u = wf / h0;
        double epsEff = (epsr + 1.0) / 2.0 + (epsr - 1.0) / 2.0 / Math.sqrt(1.0 + 12.0 / u);
        double f = str.getFrequency();
        double k0 = 2.0 * Math.PI * f / Maths.C;
        double beta = k0 * Math.sqrt(epsEff);
        double lambdaG = 2.0 * Math.PI / beta;

        // Evanescent near-field threshold: sample at least 0.4 * wf away from the delta gap
        double minDist = 0.40 * wf;
        double maxDist = 2.0 * wf;

        List<CutCandidate> candidates = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            RWG r = RWGDeltaGapBMatrix.tryUnwrapRWG(tfs[i]);
            if (r != null) {
                HPoint mid = r.getSharedEdgeMidpoint();
                double tCoord = polY ? mid.x : mid.y;
                double lCoord = polY ? mid.y : mid.x;
                if (tCoord >= minTrans - 1e-4 && tCoord <= maxTrans + 1e-4) {
                    double dist = (lCoord - portCoord) * dir;
                    if (dist >= minDist - 1e-4 && dist <= maxDist + 1e-4) {
                        double gam = r.deltaGapGamma(polY ? Axis.Y : Axis.X);
                        if (Math.abs(gam) > 1e-12) {
                            CutCandidate cc = new CutCandidate();
                            cc.index = i;
                            cc.coord = lCoord;
                            cc.gamma = gam;
                            HTriangle tr = r.getTriangle1();
                            double pA = polY ? tr.p2().x : tr.p2().y;
                            double pB = polY ? tr.p3().x : tr.p3().y;
                            cc.tMin = Math.min(pA, pB);
                            cc.tMax = Math.max(pA, pB);
                            cc.weight = cc.tMax - cc.tMin;
                            candidates.add(cc);
                        }
                    }
                }
            }
        }

        // Cluster candidates into transverse cuts
        double clusterTol = 1.5e-4; // 0.15mm tolerance
        List<CutGroup> groups = new ArrayList<>();
        List<CutCandidate> remaining = new ArrayList<>(candidates);
        while (!remaining.isEmpty()) {
            CutCandidate seed = remaining.get(0);
            List<CutCandidate> cluster = new ArrayList<>();
            for (CutCandidate c : remaining) {
                if (Math.abs(c.coord - seed.coord) <= clusterTol) {
                    cluster.add(c);
                }
            }
            remaining.removeAll(cluster);

            List<CutCandidate> selected = selectBestNonOverlappingCover(cluster);
            double cov = 0;
            double avgCoord = 0;
            for (CutCandidate sc : selected) {
                cov += sc.weight;
                avgCoord += sc.coord;
            }
            if (!selected.isEmpty()) {
                avgCoord /= selected.size();
                if (cov >= 0.7 * wf) {
                    groups.add(new CutGroup(avgCoord, cov, selected));
                }
            }
        }

        final double fPortCoord = portCoord;
        final double fDir = dir;
        groups.sort(Comparator.comparingDouble(g -> (g.coord - fPortCoord) * fDir));

        double y1, y2;
        Complex I1, I2;
        double minCutSep = 0.35 * wf; // require at least ~1mm separation between reference planes
        if (groups.size() >= 2 && Math.abs(groups.get(1).coord - groups.get(0).coord) >= minCutSep) {
            CutGroup g1 = groups.get(0);
            CutGroup g2 = groups.get(1);
            y1 = g1.coord;
            y2 = g2.coord;
            I1 = Complex.ZERO;
            for (CutCandidate cc : g1.selected) {
                I1 = I1.plus(X.get(cc.index, p).mul(cc.gamma));
            }
            I2 = Complex.ZERO;
            for (CutCandidate cc : g2.selected) {
                I2 = I2.plus(X.get(cc.index, p).mul(cc.gamma));
            }
        } else if (groups.size() >= 1) {
            CutGroup g1 = groups.get(0);
            y1 = g1.coord;
            I1 = Complex.ZERO;
            for (CutCandidate cc : g1.selected) {
                I1 = I1.plus(X.get(cc.index, p).mul(cc.gamma));
            }
            y2 = y1 + dir * minCutSep;
            I2 = computeCurrentAtCoord(y2, minTrans, maxTrans, tfs, X, p, polY);
        } else {
            double d1 = Math.max(minDist, 0.05 * lambdaG);
            double d2 = d1 + minCutSep;
            y1 = portCoord + dir * d1;
            y2 = portCoord + dir * d2;
            I1 = computeCurrentAtCoord(y1, minTrans, maxTrans, tfs, X, p, polY);
            I2 = computeCurrentAtCoord(y2, minTrans, maxTrans, tfs, X, p, polY);
        }

        if (I1.isZero() && I2.isZero()) {
            str.log().log(NMsg.ofC("[DEEMBED FAIL] Zero currents I1=%s, I2=%s at y1=%.3fmm, y2=%.3fmm", I1, I2, y1 * 1000, y2 * 1000));
            return null;
        }

        double s1 = (y1 - portCoord) * dir;
        double s2 = (y2 - portCoord) * dir;

        Complex e1p = Complex.of(Math.cos(-beta * s1), Math.sin(-beta * s1));
        Complex e1m = Complex.of(Math.cos(beta * s1), Math.sin(beta * s1));
        Complex e2p = Complex.of(Math.cos(-beta * s2), Math.sin(-beta * s2));
        Complex e2m = Complex.of(Math.cos(beta * s2), Math.sin(beta * s2));

        Complex det = e1p.mul(e2m).minus(e1m.mul(e2p));
        if (det.absDouble() < 1e-12) {
            str.log().log(NMsg.ofC("[DEEMBED FAIL] Singular det=%s", det));
            return null;
        }

        Complex Iplus = I1.mul(e2m).minus(I2.mul(e1m)).div(det);
        Complex Iminus = e1p.mul(I2).minus(e2p.mul(I1)).div(det);

        if (Iplus.isZero()) {
            str.log().log(NMsg.ofC("[DEEMBED FAIL] Zero Iplus"));
            return null;
        }

        Complex gammaV = Iminus.div(Iplus).mul(-1.0);

        if (gammaV.absDouble() > 0.999) {
            gammaV = gammaV.div(gammaV.absDouble()).mul(0.999);
        }

        Complex ZinDeembed = Z0.mul(Complex.ONE.plus(gammaV).div(Complex.ONE.minus(gammaV)));
        double s11db = 20 * Math.log10(gammaV.absDouble());
        str.log().log(NMsg.ofC("[DEEMBED SUCCESS] f=%.3fGHz: y1=%.3fmm, y2=%.3fmm, |I+|=%.3e, |I-|=%.3e, S11=%.2f dB, Zin=%s",
                f / 1e9, y1 * 1000, y2 * 1000, Iplus.absDouble(), Iminus.absDouble(), s11db, ZinDeembed));
        return ZinDeembed;
    }

    private static List<CutCandidate> selectBestNonOverlappingCover(List<CutCandidate> candidates) {
        if (candidates.isEmpty()) return Collections.emptyList();
        List<CutCandidate> sorted = new ArrayList<>(candidates);
        sorted.sort(Comparator.comparingDouble(c -> c.tMax));

        int m = sorted.size();
        double[] dp = new double[m];
        int[] parent = new int[m];
        Arrays.fill(parent, -1);

        for (int i = 0; i < m; i++) {
            dp[i] = sorted.get(i).weight;
            for (int j = 0; j < i; j++) {
                if (sorted.get(j).tMax <= sorted.get(i).tMin + 1e-6) {
                    if (dp[j] + sorted.get(i).weight > dp[i]) {
                        dp[i] = dp[j] + sorted.get(i).weight;
                        parent[i] = j;
                    }
                }
            }
        }

        int bestEnd = 0;
        double maxWeight = 0;
        for (int i = 0; i < m; i++) {
            if (dp[i] > maxWeight) {
                maxWeight = dp[i];
                bestEnd = i;
            }
        }

        List<CutCandidate> selected = new ArrayList<>();
        int curr = bestEnd;
        while (curr >= 0) {
            selected.add(sorted.get(curr));
            curr = parent[curr];
        }
        Collections.reverse(selected);
        return selected;
    }

    /**
     * "legacy" (two planes, Hammerstad beta), "lsq" (least-squares wave fit, beta from the solution)
     * or "none". Hint "deembed"; default lsq with the layered kernel, legacy otherwise.
     */
    static String deembedMode(MomStructure str) {
        Object h = str.getHintsManager() != null ? str.getHintsManager().getHint("deembed") : null;
        if (h != null && !String.valueOf(h).trim().isEmpty()) {
            return String.valueOf(h).trim().toLowerCase();
        }
        Object g = str.getHintsManager() != null ? str.getHintsManager().getHint("mpieGreen") : null;
        return g != null && "layered".equalsIgnoreCase(String.valueOf(g).trim()) ? "lsq" : "legacy";
    }

    /** total conductor width crossed by the cut at longitudinal coordinate c, within [tLo, tHi]. */
    private static double conductorWidthAt(double c, List<HTriangle> tris, double tLo, double tHi, boolean polY) {
        double w = 0;
        for (HTriangle t : tris) {
            List<Double> xs = new ArrayList<>();
            intersectEdge(t.p1(), t.p2(), c, polY, xs);
            intersectEdge(t.p2(), t.p3(), c, polY, xs);
            intersectEdge(t.p3(), t.p1(), c, polY, xs);
            if (xs.size() < 2) {
                continue;
            }
            double a = Math.max(Collections.min(xs), tLo), b = Math.min(Collections.max(xs), tHi);
            if (b > a) {
                w += b - a;
            }
        }
        return w;
    }

    /**
     * Least-squares de-embedding: the port line current, sampled on many cuts over the uniform stretch
     * of the feed line (2 wf clear of the gap and of the first discontinuity), is fitted with
     * I(s) = a e^{-gamma s} + b e^{+gamma s}, gamma = alpha + j beta found by minimising the residual
     * (beta, alpha are the solver's own, not a closed form). The voltage reflection coefficient at the
     * gap plane is -b/a; it is normalised to the line itself (like an MSL port), and returned as the
     * impedance Z0 (1+G)/(1-G) so that S11 = G with the port impedance Z0.
     */
    private static Complex deembedPortLsq(MomStructure str, DoubleToVector[] tfs, ComplexMatrix X, int p,
                                          double portCoord, double dir, double minTrans, double maxTrans,
                                          boolean polY, double h0, double epsr, Complex Z0) {
        double wf = maxTrans - minTrans;
        double k0 = 2.0 * Math.PI * str.getFrequency() / Maths.C;
        // unique triangles
        Map<String, HTriangle> uniq = new LinkedHashMap<>();
        for (DoubleToVector f : tfs) {
            RWG r = RWGDeltaGapBMatrix.tryUnwrapRWG(f);
            if (r == null) {
                continue;
            }
            for (HTriangle tr : new HTriangle[]{r.getTriangle1(), r.getTriangle2()}) {
                HPoint c = tr.getBarycenter();
                uniq.putIfAbsent(Math.round(c.x * 1e9) + ":" + Math.round(c.y * 1e9), tr);
            }
        }
        List<HTriangle> tris = new ArrayList<>(uniq.values());
        double tLo = minTrans - wf, tHi = maxTrans + wf;
        // walk along the line until its width changes by more than 15% (junction, bend, step or end)
        double step = wf / 8, sEnd = 0;
        for (double s = 0.5 * wf; s < 2.0; s += step) {
            double w = conductorWidthAt(portCoord + dir * s, tris, tLo, tHi, polY);
            if (w < 0.85 * wf || w > 1.15 * wf) {
                break;
            }
            sEnd = s;
        }
        double s0 = 2 * wf, s1 = sEnd - 2 * wf;
        double betaGuess = k0 * Math.sqrt((epsr + 1) / 2);
        if (s1 - s0 < 0.25 * 2 * Math.PI / betaGuess) {
            str.log().log(NMsg.ofC("[DEEMBED LSQ] uniform feed line too short: %.2f mm usable (from %.2f to %.2f mm), need lambda_g/4 = %.2f mm; lengthen the port line",
                    (s1 - s0) * 1e3, s0 * 1e3, s1 * 1e3, 0.25 * 2 * Math.PI / betaGuess * 1e3).asWarning());
            return null;
        }
        int nc = 32;
        double[] ss = new double[nc];
        Complex[] is = new Complex[nc];
        for (int i = 0; i < nc; i++) {
            ss[i] = s0 + (s1 - s0) * (i + 0.37) / nc;
            is[i] = computeCurrentAtCoord(portCoord + dir * ss[i], minTrans, maxTrans, tfs, X, p, polY);
        }
        // The TM0 surface wave launched at the gap and at the discontinuities rides along the strip; on
        // thick substrates it biases a 2-wave fit badly (it then needs a negative alpha). Fit it too, with
        // its propagation constant from the grounded-slab pole (lossy).
        Complex swGamma = null;
        if (surfaceWaveTerms(str)) {
            double tand = 0;
            if (str.getFirstBoxSpace() != null && str.getFirstBoxSpace().getMaterial() != null) {
                tand = str.getFirstBoxSpace().getMaterial().lossTangent();
            }
            net.thevpc.scholar.hadruwaves.mom.mpie.GroundedSlabGreen.Cx kp =
                    new net.thevpc.scholar.hadruwaves.mom.mpie.GroundedSlabGreen(str.getFrequency(), epsr, tand, h0).tm0Pole();
            swGamma = Complex.of(-kp.im, kp.re); // j kp
        }
        // 2-wave fit always; the 4-wave one when asked ("true") or, in "auto", kept only when it at least
        // halves the residual (thick substrates: 4-21x on the 3.2 mm patch; thin: 1.2-1.6x on 1.6 mm, where
        // the extra freedom only makes the line constants wander)
        double[] ab2 = searchGamma(null, k0, epsr, ss, is);
        Complex sw = null;
        double alpha = ab2[0], beta = ab2[1];
        String swMode = surfaceWaveMode(str);
        if (swGamma != null && !"false".equals(swMode)) {
            double[] ab4 = searchGamma(swGamma, k0, epsr, ss, is);
            double r2 = (Double) fitWaves(ab2[0], ab2[1], null, ss, is)[2];
            double r4 = (Double) fitWaves(ab4[0], ab4[1], swGamma, ss, is)[2];
            if ("true".equals(swMode) || r4 < 0.5 * r2) {
                sw = swGamma;
                alpha = ab4[0];
                beta = ab4[1];
            }
            str.log().log(NMsg.ofC("[DEEMBED LSQ] residual 2 waves %.2e, 4 waves %.2e (port-surface-wave=%s): using %s",
                    r2, r4, swMode, sw == null ? "2 waves" : "4 waves"));
        }
        Object[] fit = fitWaves(alpha, beta, sw, ss, is);
        Complex a = (Complex) fit[0], b = (Complex) fit[1];
        double res = (Double) fit[2];
        Complex gamma = b.div(a).neg();
        Complex zin = Z0.mul(Complex.ONE.plus(gamma)).div(Complex.ONE.minus(gamma));
        str.log().log(NMsg.ofC("[DEEMBED LSQ] f=%.3fGHz: window %.2f..%.2f mm from the gap (line uniform to %.2f mm), %s, beta/k0=%.4f (epsEff %.4f), alpha=%.3f Np/m, fit residual %.2e, S11=%.2f dB, Zin=%s",
                str.getFrequency() / 1e9, s0 * 1e3, s1 * 1e3, sEnd * 1e3, sw == null ? "2 waves" : "4 waves (line + TM0 surface wave)",
                beta / k0, Math.pow(beta / k0, 2), alpha, res, 20 * Math.log10(gamma.absdbl()), zin));
        if (alpha < 0) {
            str.log().log(NMsg.ofC("[DEEMBED LSQ] negative line attenuation %.3f Np/m: the fit is absorbing a wave it does not model", alpha).asWarning());
        }
        if (res > 0.15) {
            str.log().log(NMsg.ofC("[DEEMBED LSQ] poor fit (residual %.2e): the port line current is not a clean two-wave field", res).asWarning());
        }
        return zin;
    }

    /** "port-surface-wave": auto (default), true or false. Hint "deembedSurfaceWave". */
    static String surfaceWaveMode(MomStructure str) {
        Object h = str.getHintsManager() != null ? str.getHintsManager().getHint("deembedSurfaceWave") : null;
        String s = h == null ? "auto" : String.valueOf(h).trim().toLowerCase();
        return s.equals("true") || s.equals("false") ? s : "auto";
    }

    /** the TM0 surface wave exists only in the layered kernel's model; "false" disables it. */
    static boolean surfaceWaveTerms(MomStructure str) {
        if ("false".equals(surfaceWaveMode(str))) {
            return false;
        }
        Object g = str.getHintsManager() != null ? str.getHintsManager().getHint("mpieGreen") : null;
        return g != null && "layered".equalsIgnoreCase(String.valueOf(g).trim());
    }

    /** beta by grid + golden, then alpha (may come out negative: reported), then beta again. */
    private static double[] searchGamma(Complex sw, double k0, double epsr, double[] ss, Complex[] is) {
        double best = k0, bestRes = Double.POSITIVE_INFINITY;
        for (double b = k0; b <= Math.sqrt(epsr) * k0; b += 0.002 * k0) {
            double r = (Double) fitWaves(0, b, sw, ss, is)[2];
            if (r < bestRes) {
                bestRes = r;
                best = b;
            }
        }
        double beta = golden(x -> (Double) fitWaves(0, x, sw, ss, is)[2], best - 0.002 * k0, best + 0.002 * k0);
        final double b1 = beta;
        double alpha = golden(x -> (Double) fitWaves(x, b1, sw, ss, is)[2], -0.05 * beta, 0.1 * beta);
        final double a1 = alpha;
        beta = golden(x -> (Double) fitWaves(a1, x, sw, ss, is)[2], beta - 0.002 * k0, beta + 0.002 * k0);
        return new double[]{alpha, beta};
    }

    /**
     * least squares I(s) = a e^{-g s} + b e^{g s} [+ c e^{-sw s} + d e^{sw s}], g = alpha + j beta;
     * returns {a, b, relative residual}.
     */
    private static Object[] fitWaves(double alpha, double beta, Complex sw, double[] ss, Complex[] is) {
        if (sw == null) {
            return fitWaves(alpha, beta, ss, is);
        }
        int n = ss.length;
        Complex g = Complex.of(alpha, beta);
        Complex[][] a = new Complex[n][4];
        Complex[][] rhs = new Complex[n][1];
        for (int i = 0; i < n; i++) {
            a[i][0] = g.mul(-ss[i]).exp();
            a[i][1] = g.mul(ss[i]).exp();
            a[i][2] = sw.mul(-ss[i]).exp();
            a[i][3] = sw.mul(ss[i]).exp();
            rhs[i][0] = is[i];
        }
        ComplexMatrix am = Maths.matrix(a);
        ComplexMatrix ah = am.transposeHermitian();
        ComplexMatrix x = ah.mul(am).solve(ah.mul(Maths.matrix(rhs)));
        double res = 0, nrm = 0;
        for (int i = 0; i < n; i++) {
            Complex f = Complex.ZERO;
            for (int k = 0; k < 4; k++) {
                f = f.plus(a[i][k].mul(x.get(k, 0)));
            }
            res += f.minus(is[i]).absdblsqr();
            nrm += is[i].absdblsqr();
        }
        return new Object[]{x.get(0, 0), x.get(1, 0), Math.sqrt(res / Math.max(nrm, 1e-300))};
    }

    /** least squares I(s) = a e^{-g s} + b e^{g s}, g = alpha + j beta; returns {a, b, relative residual}. */
    private static Object[] fitWaves(double alpha, double beta, double[] ss, Complex[] is) {
        // normal equations of the 2-column system, solved in closed form
        Complex s11 = Complex.ZERO, s12 = Complex.ZERO, s22 = Complex.ZERO, r1 = Complex.ZERO, r2 = Complex.ZERO;
        Complex[] e1 = new Complex[ss.length], e2 = new Complex[ss.length];
        for (int i = 0; i < ss.length; i++) {
            double m = Math.exp(-alpha * ss[i]);
            e1[i] = Complex.of(m * Math.cos(beta * ss[i]), -m * Math.sin(beta * ss[i]));
            e2[i] = Complex.of(Math.cos(beta * ss[i]) / m, Math.sin(beta * ss[i]) / m);
            s11 = s11.plus(e1[i].conj().mul(e1[i]));
            s12 = s12.plus(e1[i].conj().mul(e2[i]));
            s22 = s22.plus(e2[i].conj().mul(e2[i]));
            r1 = r1.plus(e1[i].conj().mul(is[i]));
            r2 = r2.plus(e2[i].conj().mul(is[i]));
        }
        Complex s21 = s12.conj();
        Complex det = s11.mul(s22).minus(s12.mul(s21));
        Complex a = r1.mul(s22).minus(s12.mul(r2)).div(det);
        Complex b = s11.mul(r2).minus(s21.mul(r1)).div(det);
        double res = 0, nrm = 0;
        for (int i = 0; i < ss.length; i++) {
            res += e1[i].mul(a).plus(e2[i].mul(b)).minus(is[i]).absdblsqr();
            nrm += is[i].absdblsqr();
        }
        return new Object[]{a, b, Math.sqrt(res / Math.max(nrm, 1e-300))};
    }

    private static double golden(java.util.function.DoubleUnaryOperator f, double lo, double hi) {
        for (int it = 0; it < 80; it++) {
            double m1 = hi - 0.6180339887 * (hi - lo), m2 = lo + 0.6180339887 * (hi - lo);
            if (f.applyAsDouble(m1) < f.applyAsDouble(m2)) {
                hi = m2;
            } else {
                lo = m1;
            }
        }
        return 0.5 * (lo + hi);
    }

    private static Complex computeCurrentAtCoord(double coord, double minTrans, double maxTrans,
                                                 DoubleToVector[] tfs, ComplexMatrix X, int p, boolean polY) {
        Complex I = Complex.ZERO;
        for (int i = 0; i < tfs.length; i++) {
            Complex xVal = X.get(i, p);
            if (xVal.isZero()) continue;
            RWG rwg = RWGDeltaGapBMatrix.tryUnwrapRWG(tfs[i]);
            if (rwg == null) continue;
            HTriangle t1 = rwg.getTriangle1();
            HTriangle t2 = rwg.getTriangle2();
            I = I.plus(xVal.mul(integrateTriangleCut(t1, rwg.edgeLength, coord, minTrans, maxTrans, polY, +1)));
            I = I.plus(xVal.mul(integrateTriangleCut(t2, rwg.edgeLength, coord, minTrans, maxTrans, polY, -1)));
        }
        return I;
    }

    private static double integrateTriangleCut(HTriangle t, double edgeLen, double coord,
                                              double minTrans, double maxTrans, boolean polY, int sign) {
        double a = t.area();
        if (a <= 0) return 0;
        HPoint p1 = t.p1(), p2 = t.p2(), p3 = t.p3();
        double c1 = polY ? p1.y : p1.x;
        double c2 = polY ? p2.y : p2.x;
        double c3 = polY ? p3.y : p3.x;
        double minC = Math.min(c1, Math.min(c2, c3));
        double maxC = Math.max(c1, Math.max(c2, c3));
        if (coord < minC || coord > maxC) return 0;

        List<Double> transIntersections = new ArrayList<>();
        intersectEdge(p1, p2, coord, polY, transIntersections);
        intersectEdge(p2, p3, coord, polY, transIntersections);
        intersectEdge(p3, p1, coord, polY, transIntersections);

        if (transIntersections.size() < 2) return 0;
        Collections.sort(transIntersections);
        double xA = Math.max(transIntersections.get(0), minTrans);
        double xB = Math.min(transIntersections.get(transIntersections.size() - 1), maxTrans);
        if (xB <= xA) return 0;
        double dx = xB - xA;

        double vFreeCoord = polY ? p1.y : p1.x;
        double fnLong = sign * (edgeLen / (2.0 * a)) * (coord - vFreeCoord);
        return fnLong * dx;
    }

    private static void intersectEdge(HPoint va, HPoint vb, double coord, boolean polY, List<Double> out) {
        double ca = polY ? va.y : va.x;
        double cb = polY ? vb.y : vb.x;
        if ((ca - coord) * (cb - coord) <= 0 && Math.abs(ca - cb) > 1e-12) {
            double frac = (coord - ca) / (cb - ca);
            double ta = polY ? va.x : va.y;
            double tb = polY ? vb.x : vb.y;
            out.add(ta + frac * (tb - ta));
        }
    }
}
