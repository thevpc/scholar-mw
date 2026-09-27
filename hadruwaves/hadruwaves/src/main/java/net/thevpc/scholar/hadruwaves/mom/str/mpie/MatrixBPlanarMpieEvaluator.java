package net.thevpc.scholar.hadruwaves.mom.str.mpie;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.geom.HPoint;
import net.thevpc.scholar.hadrumaths.geom.HTriangle;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.RWG;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.RWGDeltaGapBMatrix;
import net.thevpc.scholar.hadruwaves.mom.sources.PlanarSource;
import net.thevpc.scholar.hadruwaves.mom.sources.PlanarSources;
import net.thevpc.scholar.hadruwaves.mom.sources.planar.CstPlanarSource;
import net.thevpc.scholar.hadruwaves.mom.str.MatrixBEvaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
                                c.longCoord = (polarization == Axis.X) ? mid.x : mid.y;
                                HTriangle t1 = rwg.getTriangle1();
                                double pA = (polarization == Axis.X) ? t1.p2().y : t1.p2().x;
                                double pB = (polarization == Axis.X) ? t1.p3().y : t1.p3().x;
                                c.tMin = Math.min(pA, pB);
                                c.tMax = Math.max(pA, pB);
                                c.weight = c.tMax - c.tMin;
                                candidates.add(c);
                            }
                        }
                    } else {
                        DoubleToVector srcFn = csrc.getFunction();
                        b[n][p] = Maths.scalarProduct(tfs[n], srcFn).toComplex();
                    }
                }

                if (!candidates.isEmpty()) {
                    for (Candidate c : candidates) {
                        str.log().log(net.thevpc.nuts.text.NMsg.ofC("  [MPIE CANDIDATE] i=%d, gamma=%.4e, mid=(%.3f, %.3f)mm, span=[%.3f, %.3f]mm",
                                c.index, c.gamma, c.mid.x * 1000, c.mid.y * 1000, c.tMin * 1000, c.tMax * 1000));
                    }
                    // Find the cut that provides maximum width coverage across the port line
                    double bestCoverage = 0;
                    double bestCoord = candidates.get(0).longCoord;
                    List<Candidate> bestSelected = Collections.emptyList();
                    // Strict cluster tolerance so all port edges share the same transverse cut coordinate
                    double coordClusterTol = 1e-4; // 0.1mm cluster tolerance

                    for (Candidate c : candidates) {
                        List<Candidate> cluster = new ArrayList<>();
                        for (Candidate o : candidates) {
                            if (Math.abs(o.longCoord - c.longCoord) <= coordClusterTol) {
                                cluster.add(o);
                            }
                        }
                        List<Candidate> selected = selectBestNonOverlappingCover(cluster);
                        double cov = 0;
                        for (Candidate s : selected) {
                            cov += s.weight;
                        }
                        if (cov > bestCoverage) {
                            bestCoverage = cov;
                            bestCoord = c.longCoord;
                            bestSelected = selected;
                        }
                    }

                    str.log().log(net.thevpc.nuts.text.NMsg.ofC("  [MPIE SELECTION] bestCoord=%.3fmm, bestCoverage=%.4e, selectedCount=%d",
                            bestCoord * 1000, bestCoverage, bestSelected.size()));

                    for (Candidate s : bestSelected) {
                        b[s.index][p] = Complex.of(V0 * s.gamma);
                        str.log().log(net.thevpc.nuts.text.NMsg.ofC("    -> SELECTED i=%d, span=[%.3f, %.3f]mm, gamma=%.4e, B=%s",
                                s.index, s.tMin * 1000, s.tMax * 1000, s.gamma, b[s.index][p]));
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

    private static class Candidate {
        int index;
        RWG rwg;
        HPoint mid;
        double gamma;
        double longCoord;
        double tMin;
        double tMax;
        double weight;
    }

    private static List<Candidate> selectBestNonOverlappingCover(List<Candidate> cluster) {
        if (cluster.isEmpty()) {
            return Collections.emptyList();
        }
        // Sort by tMax ascending, then tMin ascending
        List<Candidate> sorted = new ArrayList<>(cluster);
        sorted.sort(Comparator.comparingDouble((Candidate c) -> c.tMax).thenComparingDouble(c -> c.tMin));

        int m = sorted.size();
        double[] opt = new double[m];
        int[] prev = new int[m];

        for (int i = 0; i < m; i++) {
            Candidate curr = sorted.get(i);
            int p = -1;
            for (int j = i - 1; j >= 0; j--) {
                Candidate candJ = sorted.get(j);
                double overlap = Math.max(0, candJ.tMax - curr.tMin);
                if (overlap <= 0.05 * Math.min(candJ.weight, curr.weight) + 1e-6) {
                    p = j;
                    break;
                }
            }
            prev[i] = p;
            double takeWeight = curr.weight + (p >= 0 ? opt[p] : 0.0);
            double skipWeight = (i > 0 ? opt[i - 1] : 0.0);
            opt[i] = Math.max(takeWeight, skipWeight);
        }

        // Backtrack to find selected candidates
        List<Candidate> selected = new ArrayList<>();
        int currIdx = m - 1;
        while (currIdx >= 0) {
            double takeWeight = sorted.get(currIdx).weight + (prev[currIdx] >= 0 ? opt[prev[currIdx]] : 0.0);
            double skipWeight = (currIdx > 0 ? opt[currIdx - 1] : 0.0);
            if (takeWeight >= skipWeight) {
                selected.add(sorted.get(currIdx));
                currIdx = prev[currIdx];
            } else {
                currIdx--;
            }
        }
        Collections.reverse(selected);
        return selected;
    }

    @Override
    public NElement toElement() {
        return NElement.ofObjectBuilder(getClass().getSimpleName()).build();
    }
}
