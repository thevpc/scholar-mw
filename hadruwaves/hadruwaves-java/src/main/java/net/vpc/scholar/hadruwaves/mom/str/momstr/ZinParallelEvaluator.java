package net.vpc.scholar.hadruwaves.mom.str.momstr;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.str.ZinEvaluator;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadrumaths.ComplexMatrix;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 août 2007 09:00:36
 */
public class ZinParallelEvaluator implements ZinEvaluator {
    public static final ZinParallelEvaluator INSTANCE=new ZinParallelEvaluator();
    @Override
    public ComplexMatrix evaluate(MomStructure str, ProgressMonitor monitor) {
        ProgressMonitor[] mons= ProgressMonitorFactory.split(monitor, new double[]{1, 4});
        ComplexMatrix B_ = str.matrixB().monitor(mons[0]).computeMatrix();
        ComplexMatrix A_ = str.matrixA().monitor(mons[1]).computeMatrix();
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
        return getClass().getName();
    }

    @Override
    public String dump() {
        return getClass().getName();
    }
}
