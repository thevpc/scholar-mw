package net.vpc.scholar.hadruwaves.mom.str;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 * Created by vpc on 5/30/14.
 */
public class DefaultMatrixUnknownEvaluator implements MatrixUnknownEvaluator {
    public static final DefaultMatrixUnknownEvaluator INSTANCE=new DefaultMatrixUnknownEvaluator();
    @Override
    public Matrix evaluate(MomStructure str, ProgressMonitor monitor) {
        ProgressMonitor[] mons = ProgressMonitorFactory.split(monitor, new double[]{1, 4}, new boolean[]{true, true});
        Matrix B_ = str.matrixB().monitor(mons[0]).computeMatrix();
        Matrix A_ = str.matrixA().monitor(mons[1]).computeMatrix();

        Matrix Testcoeff;

        try {
            Testcoeff = A_.solve(B_);
        } catch (Exception e) {
            str.getLog().error("Error DefaultMatrixUnknownEvaluator : " + e);
//            getLog().error("A=" + A_);
//            getLog().error("B=" + B_);
            str.wdebug("DefaultMatrixUnknownEvaluator", e);
            return Maths.NaNMatrix(B_.getRowCount(), B_.getColumnCount());
//                    new CMatrix(new Complex[][]{{Complex.CNaN}});
//            throw new RuntimeException(e);
        }
        return Testcoeff;
    }

    @Override
    public String dump() {
        return getClass().getName();
    }
}
