package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.geom.HPoint;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.RWG;
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
     * Builds the N x 1 excitation matrix B.
     *
     * <p>For RWG functions whose shared-edge midpoint is inside the source domain,
     * uses delta-gap: B[n] = V0 * gamma_n.
     * For all other functions, computes the scalar product with the spatial E_inc field.
     *
     * @param momStructure the MoM problem
     * @param source       the planar source (provides voltage, domain, polarization)
     * @param monitor      progress monitor
     * @return ComplexMatrix of size N x 1
     */
    public static ComplexMatrix buildB(MomStructure momStructure, CstPlanarSource source, ProgressMonitor monitor) {
        DoubleToVector[] tfs = momStructure.testFunctions().toArray();
        int N = tfs.length;

        Domain sourceDomain = source.getGeometryList().getDomain();
        Axis polarization = source.getPolarization() != null ? source.getPolarization() : Axis.Y;

        // Reconstruct V0 from stored field strength x gap dimension
        double V0;
        if (polarization == Axis.X) {
            V0 = source.getXvalue() * sourceDomain.xwidth();
        } else {
            V0 = source.getYvalue() * sourceDomain.ywidth();
        }

        // Lazy: only compute source function once if fallback is needed
        DoubleToVector srcFn = null;

        Complex[][] b = new Complex[N][1];
        int portEdgeCount = 0;

        for (int n = 0; n < N; n++) {
            RWG rwg = tryUnwrapRWG(tfs[n]);
            if (rwg != null) {
                HPoint mid = rwg.getSharedEdgeMidpoint();
                if (sourceDomain.contains(mid.x, mid.y)) {
                    double gamma = rwg.deltaGapGamma(polarization);
                    b[n][0] = Complex.of(V0 * gamma);
                    portEdgeCount++;
                } else {
                    b[n][0] = Complex.ZERO;
                }
                continue;
            }
            // Fallback: standard spatial scalar product for non-RWG functions (e.g. mixed sinusoidal)
            if (srcFn == null) {
                srcFn = source.getFunction();
            }
            b[n][0] = Maths.scalarProduct(tfs[n], srcFn).toComplex();
        }

        // Diagnostics
        if (portEdgeCount == 0) {
            LOG.warning("RWGDeltaGapBMatrix: no port edges found inside source domain " + sourceDomain
                    + ". The mesh may not have an edge within the feed region. "
                    + "All entries computed via spatial integration (check mesh density).");
        } else if (portEdgeCount == 1) {
            LOG.info("RWGDeltaGapBMatrix: 1 port edge found -> standard single delta-gap. V0=" + V0);
        } else {
            LOG.info("RWGDeltaGapBMatrix: " + portEdgeCount + " port edges found."
                    + " Distributed delta-gap across all matching edges. V0=" + V0);
        }

        return Maths.matrix(b);
    }
}
