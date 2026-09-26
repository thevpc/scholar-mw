package net.thevpc.scholar.hadruwaves.mom.str.momstr;

import net.thevpc.common.mon.ProgressMonitors;

import net.thevpc.nuts.elem.NElement;

import net.thevpc.nuts.text.NMsg;
import net.thevpc.scholar.hadrumaths.Complex;
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
    public static final ZinSerialEvaluator INSTANCE = new ZinSerialEvaluator();

    public ComplexMatrix evaluate(MomStructure str, ProgressMonitor monitor) {
        //Z= inv(Bt.inv(A).B)
        ProgressMonitor[] mons = ProgressMonitors.split(monitor, 1, 4);
        ComplexMatrix B_ = str.matrixB().monitor(mons[0]).evalMatrix();
        ComplexMatrix A_ = str.matrixA().monitor(mons[1]).evalMatrix();
        ComplexMatrix ZinPaire = null;
        ComplexMatrix cMatrix = null;
        try {
            net.thevpc.scholar.hadrumaths.InverseStrategy strategy = str.getInvStrategy();
            if (strategy == net.thevpc.scholar.hadrumaths.InverseStrategy.DEFAULT
                    && net.thevpc.scholar.hadruwaves.mom.RWGDeltaGapBMatrix.hasRWG(str)
                    && str.getSolverType() != net.thevpc.scholar.hadruwaves.mom.MomSolverType.SPATIAL_MPIE) {
                strategy = net.thevpc.scholar.hadrumaths.InverseStrategy.REGULARIZED;
            }
            if (strategy == net.thevpc.scholar.hadrumaths.InverseStrategy.REGULARIZED) {
                int n = A_.getRowCount();
                ComplexMatrix Ah = A_.transposeHermitian();
                ComplexMatrix AhB = Ah.mul(B_);
                ComplexMatrix AhA = Ah.mul(A_);
                double maxDiag = 0;
                for (int i = 0; i < n; i++) {
                    maxDiag = Math.max(maxDiag, AhA.get(i, i).absDouble());
                }
                ComplexMatrix regI = Maths.identityMatrix(n).mul(Complex.of(1e-5 * maxDiag));
                ComplexMatrix H = AhA.add(regI);
                ComplexMatrix X = H.solve(AhB);
                cMatrix = B_.transposeHermitian().mul(X);
            } else {
                try {
                    ComplexMatrix X = A_.solve(B_);
                    cMatrix = B_.transposeHermitian().mul(X);
                } catch (Exception ex) {
                    str.log().log(NMsg.ofC("Matrix A solve failed (%s), falling back to REGULARIZED: %s", ex.getMessage(), ex).asWarning());
                    int n = A_.getRowCount();
                    ComplexMatrix Ah = A_.transposeHermitian();
                    ComplexMatrix AhB = Ah.mul(B_);
                    ComplexMatrix AhA = Ah.mul(A_);
                    double maxDiag = 0;
                    for (int i = 0; i < n; i++) {
                        maxDiag = Math.max(maxDiag, AhA.get(i, i).absDouble());
                    }
                    ComplexMatrix regI = Maths.identityMatrix(n).mul(Complex.of(1e-5 * maxDiag));
                    ComplexMatrix H = AhA.add(regI);
                    ComplexMatrix X = H.solve(AhB);
                    cMatrix = B_.transposeHermitian().mul(X);
                }
            }

            //la division
            ZinPaire = cMatrix.inv();
            if (str.getHintsManager().isHintRegularZnOperator()) {
                ModeFunctions fn = str.modeFunctions();
                for (ModeInfo fnIndexes : fn.getPropagatingModes()) {
                    ZinPaire = ZinPaire.sub(fnIndexes.impedance.impedanceValue());
                }
            }
        } catch (Exception e) {
            str.log().log(NMsg.ofC("Error Zin : " + e).asError(e));
            if (cMatrix == null) {
                str.wdebug("resolveZin : matrix A is singular ", e, A_);
            } else if (ZinPaire == null) {
                str.wdebug("resolveZin : matrix Y is singular ", e, cMatrix);
            }
            return Maths.NaNMatrix(B_.getColumnCount());
        }
        boolean useZParity = str.getProjectType() == ProjectType.WAVE_GUIDE;
        //TODO pourquoi paire ?
        ComplexMatrix cMatrix1 = useZParity ? ZinPaire.div(2) : ZinPaire;
        return cMatrix1;
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
