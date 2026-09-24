package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.geom.HPoint;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.RWG;
import net.thevpc.scholar.hadruwaves.mom.sources.PlanarSource;
import net.thevpc.scholar.hadruwaves.mom.sources.planar.CstPlanarSource;

import java.util.logging.Logger;

/**
 * Builds the MoM excitation matrix B using delta-gap port excitation for RWG
 * basis functions.
 *
 * <p>For each basis function f[n]:
 * <ul>
 *   <li>If f[n] wraps an {@link RWG} and its shared-edge midpoint lies within
 *       the source domain -&gt; B[n] = V0 * gamma_n (delta-gap projection)</li>
 *   <li>Otherwise -&gt; B[n] = integral(f[n] . E_inc) via standard spatial path</li>
 * </ul>
 *
 * <p>This handles mixed test-function sets (e.g. ListTestFunctions containing
 * both GpRWG and sinusoidal functions) transparently, with no API change for the user.
 *
 * @see RWG#deltaGapGamma(Axis)
 * @see RWG#getSharedEdgeMidpoint()
 */
public class RWGDeltaGapBMatrix {

    private static final Logger LOG = Logger.getLogger(RWGDeltaGapBMatrix.class.getName());

    /**
     * Tries to extract the {@link RWG} component from a {@link DoubleToVector}.
     *
     * <p>Inspects the expression tree of X and Y components recursively to find
     * any underlying {@link RWG} instance.
     *
     * @return the first RWG found, or {@code null} if not an RWG function
     */
    public static RWG tryUnwrapRWG(DoubleToVector f) {
        if (f == null) return null;
        RWG r = tryUnwrapRWGExpr(f.getX());
        if (r != null) return r;
        return tryUnwrapRWGExpr(f.getY());
    }

    private static RWG tryUnwrapRWGExpr(Expr expr) {
        if (expr == null) return null;
        if (expr instanceof RWG) return (RWG) expr;
        for (Expr child : expr.getChildren()) {
            RWG found = tryUnwrapRWGExpr(child);
            if (found != null) return found;
        }
        return null;
    }

    /**
     * Checks whether the given structure uses any RWG basis functions.
     *
     * @param str the MoM structure
     * @return true if RWG functions are present
     */
    public static boolean hasRWG(MomStructure str) {
        if (str == null) return false;
        try {
            if (str.testFunctions() instanceof net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpRWG) {
                return true;
            }
            for (DoubleToVector f : str.testFunctions().toArray()) {
                if (tryUnwrapRWG(f) != null) {
                    return true;
                }
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * Builds the N x 1 excitation matrix B for a single source.
     */
    public static ComplexMatrix buildB(MomStructure momStructure, CstPlanarSource source, ProgressMonitor monitor) {
        return buildB(momStructure, new PlanarSource[]{source}, monitor);
    }

    /**
     * Builds the N x P excitation matrix B for multiple planar sources (ports).
     *
     * <p>For each port p and each RWG basis function f[n]:
     * If f[n] has its shared-edge midpoint inside port p's source domain,
     * uses delta-gap: B[n, p] = V0_p * gamma_n.
     * Otherwise computes spatial scalar product or sets 0.
     *
     * @param momStructure the MoM problem
     * @param sources      the array of planar sources (P ports)
     * @param monitor      progress monitor
     * @return ComplexMatrix of size N x P
     */
    public static ComplexMatrix buildB(MomStructure momStructure, PlanarSource[] sources, ProgressMonitor monitor) {
        DoubleToVector[] tfs = momStructure.testFunctions().toArray();
        int N = tfs.length;
        int P = sources.length;
        Complex[][] b = new Complex[N][P];

        for (int p = 0; p < P; p++) {
            PlanarSource src = sources[p];
            if (src instanceof CstPlanarSource) {
                CstPlanarSource csrc = (CstPlanarSource) src;
                Domain sourceDomain = csrc.getGeometryList().getDomain();
                Axis polarization = csrc.getPolarization() != null ? csrc.getPolarization() : Axis.Y;

                // Reconstruct V0 from stored field strength x gap dimension
                double V0;
                if (polarization == Axis.X) {
                    V0 = csrc.getXvalue() * sourceDomain.xwidth();
                } else {
                    V0 = csrc.getYvalue() * sourceDomain.ywidth();
                }

                DoubleToVector srcFn = null;
                int portEdgeCount = 0;

                for (int n = 0; n < N; n++) {
                    RWG rwg = tryUnwrapRWG(tfs[n]);
                    if (rwg != null) {
                        HPoint mid = rwg.getSharedEdgeMidpoint();
                        if (sourceDomain.contains(mid.x, mid.y)) {
                            double gamma = rwg.deltaGapGamma(polarization);
                            b[n][p] = Complex.of(V0 * gamma);
                            portEdgeCount++;
                        } else {
                            b[n][p] = Complex.ZERO;
                        }
                        continue;
                    }
                    if (srcFn == null) {
                        srcFn = csrc.getFunction();
                    }
                    b[n][p] = Maths.scalarProduct(tfs[n], srcFn).toComplex();
                }

                if (portEdgeCount == 0) {
                    LOG.warning("RWGDeltaGapBMatrix: port " + p + ": no port edges found inside source domain " + sourceDomain);
                } else {
                    LOG.info("RWGDeltaGapBMatrix: port " + p + ": " + portEdgeCount + " port edges found. V0=" + V0);
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
}
