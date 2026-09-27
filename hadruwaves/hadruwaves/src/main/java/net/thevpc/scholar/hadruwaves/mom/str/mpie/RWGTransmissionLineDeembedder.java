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
