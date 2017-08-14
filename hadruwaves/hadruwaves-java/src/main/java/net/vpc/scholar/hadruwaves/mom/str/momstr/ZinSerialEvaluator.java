package net.vpc.scholar.hadruwaves.mom.str.momstr;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.util.ProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.ModeFunctions;
import net.vpc.scholar.hadruwaves.str.ZinEvaluator;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadruwaves.mom.ProjectType;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 août 2007 09:00:36
 */
public class ZinSerialEvaluator implements ZinEvaluator {
    public static final ZinSerialEvaluator INSTANCE=new ZinSerialEvaluator();
    public Matrix evaluate(MomStructure str, ProgressMonitor monitor) {
        //Z= inv(Bt.inv(A).B)
        ProgressMonitor[] mons = ProgressMonitorFactory.split(monitor, new double[]{1, 4});
        Matrix B_ = str.matrixB().monitor(mons[0]).computeMatrix();
        Matrix A_ = str.matrixA().monitor(mons[1]).computeMatrix();
        Matrix ZinPaire=null;
        Matrix cMatrix = null;
        Matrix aInv=null;
        try {
            aInv = A_.inv(str.getInvStrategy(), str.getCondStrategy(), str.getNormStrategy());
            //should use conjugate transpose
//            cMatrix = B_.transpose().multiply(aInv).multiply(B_); // Bt.inv(A).B
            cMatrix = B_.transposeHermitian().mul(aInv).mul(B_); // Bt.inv(A).B
            //la division
            ZinPaire = cMatrix.inv();
            if (str.getHintsManager().isHintRegularZnOperator()) {
                ModeFunctions fn = str.getModeFunctions();
                for (ModeInfo fnIndexes : fn.getPropagatingModes()) {
                    ZinPaire = ZinPaire.sub(fnIndexes.impedance);
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
        Matrix cMatrix1 = useZParity ?ZinPaire.div(2):ZinPaire;
//        System.out.println("["+str.getName()+ "] Zin = " + cMatrix1);
//        System.out.println(str.getClass().getSimpleName()+":w="+str.getWidth()+":f="+str.getFrequency()+":l="+str.getLambda()+":w/l="+(str.getWidth()/str.getLambda()) +":zin"+ cMatrix1);
        return cMatrix1;
    }
    @Override
    public String toString() {
        return getClass().getName();
    }

    public String dump() {
        return getClass().getName();
    }
}
