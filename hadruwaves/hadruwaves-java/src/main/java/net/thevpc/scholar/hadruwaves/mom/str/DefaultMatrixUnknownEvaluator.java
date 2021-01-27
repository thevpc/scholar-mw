package net.thevpc.scholar.hadruwaves.mom.str;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
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
    public String toString() {
        return dump();
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.function(getClass().getSimpleName()).build();
    }
}
