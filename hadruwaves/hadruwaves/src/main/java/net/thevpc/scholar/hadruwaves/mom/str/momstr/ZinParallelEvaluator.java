package net.thevpc.scholar.hadruwaves.mom.str.momstr;

import net.thevpc.common.mon.ProgressMonitors;

import net.thevpc.nuts.elem.NElement;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadruwaves.str.ZinEvaluator;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 août 2007 09:00:36
 */
public class ZinParallelEvaluator implements ZinEvaluator {
    public static final ZinParallelEvaluator INSTANCE=new ZinParallelEvaluator();
    @Override
    public ComplexMatrix evaluate(MomStructure str, ProgressMonitor monitor) {
        ProgressMonitor[] mons= ProgressMonitors.split(monitor, new double[]{1, 4});
        ComplexMatrix B_ = str.matrixB().monitor(mons[0]).evalMatrix();
        ComplexMatrix A_ = str.matrixA().monitor(mons[1]).evalMatrix();
        ComplexMatrix ZinCond;
        try {
            ComplexMatrix aInv = A_.inv(str.getInvStrategy(),str.getCondStrategy(),str.getNormStrategy());
            //should use conjugate transpoe
            ComplexMatrix ZinPaire=B_.transposeHermitian().mul(aInv).mul(B_);
            ZinCond = ZinPaire.div(2);
        } catch (Exception e) {
            str.getLog().error("Error Zin : " + e);
            str.wdebug("resolveZin", e);
            return Maths.NaNMatrix(1);
        }
        return ZinCond;
    }

    @Override
    public String toString() {
        return dump();
    }

    @Override
    public NElement toElement() {
        return NElement.ofUplet(getClass().getSimpleName());
    }
}
