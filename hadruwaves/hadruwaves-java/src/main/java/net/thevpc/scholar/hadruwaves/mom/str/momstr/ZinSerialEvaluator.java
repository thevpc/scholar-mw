package net.thevpc.scholar.hadruwaves.mom.str.momstr;

import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadruwaves.mom.ModeFunctions;
import net.thevpc.scholar.hadruwaves.str.ZinEvaluator;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.scholar.hadruwaves.mom.ProjectType;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 août 2007 09:00:36
 */
public class ZinSerialEvaluator implements ZinEvaluator {
    public static final ZinSerialEvaluator INSTANCE=new ZinSerialEvaluator();
    public ComplexMatrix evaluate(MomStructure str, ProgressMonitor monitor) {
        //Z= inv(Bt.inv(A).B)
        ProgressMonitor[] mons = ProgressMonitors.split(monitor, new double[]{1, 4});
        ComplexMatrix B_ = str.matrixB().monitor(mons[0]).evalMatrix();
        ComplexMatrix A_ = str.matrixA().monitor(mons[1]).evalMatrix();
        ComplexMatrix ZinPaire=null;
        ComplexMatrix cMatrix = null;
        ComplexMatrix aInv=null;
        try {
            aInv = A_.inv(str.getInvStrategy(), str.getCondStrategy(), str.getNormStrategy());
            //should use conjugate transpose
//            cMatrix = B_.transpose().multiply(aInv).multiply(B_); // Bt.inv(A).B
            cMatrix = B_.transposeHermitian().mul(aInv).mul(B_); // Bt.inv(A).B
            //la division
            ZinPaire = cMatrix.inv();
            if (str.getHintsManager().isHintRegularZnOperator()) {
                ModeFunctions fn = str.modeFunctions();
                for (ModeInfo fnIndexes : fn.getPropagatingModes()) {
                    ZinPaire = ZinPaire.sub(fnIndexes.impedance.impedanceValue());
                }
            }
        } catch (Exception e) {
            str.getLog().error("Error Zin : " + e);
            if(aInv==null){
                str.wdebug("resolveZin : matrix A is singular ", e, A_);
            }else if(ZinPaire==null){
                str.wdebug("resolveZin : matrix Y is singular ", e, cMatrix);
            }
            return Maths.NaNMatrix(B_.getColumnCount());
        }
        boolean useZParity =str.getProjectType()== ProjectType.WAVE_GUIDE;
        //TODO pourquoi paire ?
        ComplexMatrix cMatrix1 = useZParity ?ZinPaire.div(2):ZinPaire;
//        System.out.println("["+str.getName()+ "] Zin = " + cMatrix1);
//        System.out.println(str.getClass().getSimpleName()+":w="+str.getWidth()+":f="+str.getFrequency()+":l="+str.getLambda()+":w/l="+(str.getWidth()/str.getLambda()) +":zin"+ cMatrix1);
        return cMatrix1;
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
