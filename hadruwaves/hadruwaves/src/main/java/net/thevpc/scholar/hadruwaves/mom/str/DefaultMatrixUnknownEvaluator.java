package net.thevpc.scholar.hadruwaves.mom.str;


import net.thevpc.nuts.elem.NElement;

import net.thevpc.nuts.text.NMsg;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

/**
 * Created by vpc on 5/30/14.
 */
public class DefaultMatrixUnknownEvaluator implements MatrixUnknownEvaluator {
    public static final DefaultMatrixUnknownEvaluator INSTANCE=new DefaultMatrixUnknownEvaluator();
    @Override
    public ComplexMatrix evaluate(MomStructure str, ProgressMonitor monitor) {
        ProgressMonitor[] mons = ProgressMonitors.split(monitor, new double[]{1, 4});
        ComplexMatrix B_ = str.matrixB().monitor(mons[0]).evalMatrix();
        ComplexMatrix A_ = str.matrixA().monitor(mons[1]).evalMatrix();

        ComplexMatrix Testcoeff;

        try {
            net.thevpc.scholar.hadrumaths.InverseStrategy strategy = str.getInvStrategy();
            if (strategy == net.thevpc.scholar.hadrumaths.InverseStrategy.DEFAULT
                    && net.thevpc.scholar.hadruwaves.mom.RWGDeltaGapBMatrix.hasRWG(str)) {
                strategy = net.thevpc.scholar.hadrumaths.InverseStrategy.REGULARIZED;
            }
            if (strategy == net.thevpc.scholar.hadrumaths.InverseStrategy.REGULARIZED) {
                Testcoeff = A_.invRegularized().mul(B_);
            } else {
                try {
                    Testcoeff = A_.solve(B_);
                } catch (Exception ex) {
                    str.log().log(NMsg.ofC("Matrix A solve failed (%s), falling back to REGULARIZED: %s", ex.getMessage(), ex).asWarning());
                    Testcoeff = A_.invRegularized().mul(B_);
                }
            }
        } catch (Exception e) {
            str.log().log(NMsg.ofC("Error DefaultMatrixUnknownEvaluator : " + e).asError(e));
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
    public String toString() {
        return dump();
    }

    @Override
    public NElement toElement() {
        return NElement.ofObjectBuilder(getClass().getSimpleName()).build();
    }
}
