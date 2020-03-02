package net.vpc.scholar.hadruwaves.mom;

import net.vpc.common.mon.MonitoredAction;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.cache.CacheKey;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.symbolic.double2double.Linear;
import net.vpc.scholar.hadruwaves.Physics;
import net.vpc.scholar.hadruwaves.str.FarFieldEvaluator;
import net.vpc.scholar.hadruwaves.str.MWStructure;

import static net.vpc.scholar.hadrumaths.Maths.*;

public class FarFieldEvaluatorPEC implements FarFieldEvaluator {
    public static final FarFieldEvaluator INSTANCE = new FarFieldEvaluatorPEC();

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.function(getClass().getSimpleName()).build();
    }

    @Override
    public Vector<ComplexMatrix> evaluate(final MWStructure str, final double[] thetaArr, final double[] phiArr, final double r, ProgressMonitor monitor) {
        return invokeMonitoredAction(monitor, getClass().getSimpleName(), new MonitoredAction<Vector<ComplexMatrix>>() {
            @Override
            public Vector<ComplexMatrix> process(final ProgressMonitor monitor, String messagePrefix) throws Exception {
                final MomStructure structure = (MomStructure) str;
                final double k0 = Physics.K0(structure.getFrequency());
                final Complex C1 = (î.mul(-k0).mul(exp(î.mul(-k0 * r)))).div(4.0 * Math.PI * r);
                final ComplexMatrix SPM = structure.getTestModeScalarProducts();
                final ComplexMatrix Xj = structure.matrixX().evalMatrix();
                Complex[][] EthetaMatrix = new Complex[thetaArr.length][phiArr.length];
                Complex[][] EphiMatrix = new Complex[thetaArr.length][phiArr.length];
                final String monText = getClass().getSimpleName();
                for (int i = 0; i < thetaArr.length; i++) {
                    for (int j = 0; j < phiArr.length; j++) {
                        monitor.setProgress(i, j,thetaArr.length,phiArr.length,monText);
                        final int finalI = i;
                        final int finalJ = j;
                        System.out.println("TRY EXEC="+monitor.getProgressValue()+" :: "+monText+" "+CharactersTable.THETA+"="+thetaArr[finalI]+" "+CharactersTable.PHI+"="+phiArr[finalJ]);
                        Complex[] rr= new StrSubCacheSupport<Complex[]>(structure, "far-field-angle",
                                CacheKey.obj("computeFarFieldThetaPhiElement","theta",thetaArr[i],"phi", phiArr[i],"r",r)
                                ,monitor) {

                            @Override
                            public Complex[] eval(ObjectCache momCache) {
                                monitor.setProgress(finalI, finalJ,thetaArr.length,phiArr.length,monText);
                                System.out.println("EXEC="+monitor.getProgressValue()+" :: "+monText+" "+CharactersTable.THETA+"="+thetaArr[finalI]+" "+CharactersTable.PHI+"="+phiArr[finalJ]);
                                double sin_theta = sin(thetaArr[finalI]);
                                double cos_theta = cos(thetaArr[finalI]);
                                double sin_phi = sin(phiArr[finalJ]);
                                double cos_phi = cos(phiArr[finalJ]);
                                double ttheta = k0 * sin_theta;
                                Linear ftheta = FunctionFactory.segment(ttheta * cos_phi, ttheta * sin_phi, 0, structure.getDomain());
                                Expr rtheta = add(cos(ftheta), sin(ftheta).mul(î));

                                double tphi = k0 * sin_phi;
                                Linear fphi = FunctionFactory.segment(tphi * cos_phi, tphi * sin_phi, 0, structure.getDomain());
                                Expr rphi = add(cos(fphi), sin(fphi).mul(î));

                                int mm = 0;

                                MutableComplex fxtheta = new MutableComplex();
                                MutableComplex fytheta = new MutableComplex();
                                MutableComplex fxphi = new MutableComplex();
                                MutableComplex fyphi = new MutableComplex();
                                Complex[] zmns = structure.getModesImpedances();
                                DoubleToVector[] fn = structure.fn();
                                while (mm < fn.length) {
                                    DoubleToVector fmn = fn[mm];
                                    ComplexVector gp_fmn = SPM.getColumn(mm);
                                    Expr Ee = fmn.mul(gp_fmn.scalarProduct(Xj).mul(zmns[mm]));
                                    DoubleToVector Eev = Ee.toDV();
                                    fxtheta.add(integrate(Eev.getY().mul(rtheta)));
                                    fytheta.add(integrate(Eev.getX().mul(rtheta)));
                                    fxphi.add(integrate(Eev.getY().mul(rphi)));
                                    fyphi.add(integrate(Eev.getX().mul(rphi)));
                                    mm += 1;
                                    //println("mm="+mm)
                                }

                                fxtheta.mul(2.0);
                                fytheta.mul(-2.0);
                                fxphi.mul(-2.0);
                                fyphi.mul(2.0);

                                Complex px_theta = fxtheta.toComplex();
                                Complex py_theta = fytheta.toComplex();
                                Complex Etheta = (py_theta.mul(cos_phi).sub(px_theta.mul(sin_phi))).mul(C1); // Etheta

                                Complex px_phi = fxphi.toComplex();
                                Complex py_phi = fyphi.toComplex();
                                Complex Ephi = (px_phi.mul(cos_theta * cos_phi).add(py_phi.mul(cos_theta * sin_phi))).mul(C1); // Etheta
                                return new Complex[]{Etheta,Ephi};
                            }
                        }.evalCached();

                        EthetaMatrix[i][j] = rr[0];
                        EphiMatrix[i][j] = rr[1];
                    }
                }
                Vector<ComplexMatrix> tMatrices = Maths.columnVector(
                        $MATRIX,
                        matrix(EthetaMatrix),
                                matrix(EphiMatrix)
                );
                return (Vector) tMatrices;
            }
        });
    }

//    public Complex[] electricFieldThetaPhi(double r, double theta, double phi) {
//        double k0 = Physics.K0(structure.getFrequency());
//        Complex C1 = (î.mul(-k0).mul(Maths.exp(î.mul(-k0 * r)))).div(4.0 * Math.PI * r);
//        ComplexMatrix SPM = structure.getTestModeScalarProducts();
//
//        double ttheta = k0 * sin(theta) ;
//        Linear ftheta = FunctionFactory.segment(ttheta * cos(phi), ttheta * sin(phi), 0, structure.getDomain());
//        Expr rtheta = Plus.of(new Cos(ftheta),new Sin(ftheta).mul(î));
//
//        double tphi = k0 * sin(theta) ;
//        Linear fphi = FunctionFactory.segment( tphi * cos(phi), tphi * sin(phi), 0, structure.getDomain());
//        Expr rphi = Plus.of(new Cos(fphi),new Sin(fphi).mul(î));
//
//        int mm = 0;
//        Matrix Xj = structure.matrixX().computeMatrix();
//
//        MutableComplex fxtheta = new MutableComplex();
//        MutableComplex fytheta = new MutableComplex();
//        MutableComplex fxphi = new MutableComplex();
//        MutableComplex fyphi = new MutableComplex();
//        Complex[] zmns = structure.getModesImpedances();
//        DoubleToVector[] fn = structure.fn();
//        while (mm < fn.length) {
//            DoubleToVector fmn = fn[mm];
//            ComplexVector gp_fmn = SPM.getColumn(mm);
//            Expr Ee = fmn.mul(gp_fmn.scalarProduct(Xj).mul(zmns[mm]));
//            DoubleToVector Eev = Ee.toDV();
//            fxtheta.add(integrate(Eev.getY().mul(rtheta)));
//            fytheta.add(integrate(Eev.getX().mul(rtheta)));
//            fxphi.add(integrate(Eev.getY().mul(rphi)));
//            fyphi.add(integrate(Eev.getX().mul(rphi)));
//            mm += 1;
//            //println("mm="+mm)
//        }
//
//        fxtheta.mul(2.0);
//        fytheta.mul(-2.0);
//        fxphi.mul(-2.0);
//        fyphi.mul(2.0);
//
//        Complex px_theta=fxtheta.toComplex();
//        Complex py_theta = fytheta.toComplex();
//        Complex Etheta=(py_theta.mul(cos(phi)).sub(px_theta.mul(sin(phi)))).mul(C1); // Etheta
//
//        Complex px_phi=fxphi.toComplex();
//        Complex py_phi= fyphi.toComplex();
//        Complex Ephi=(px_phi.mul(cos(theta)*cos(phi)).add(py_phi.mul(cos(theta)*sin(phi)))).mul(C1); // Etheta
//        return new Complex[]{Etheta,Ephi};
//    }

}
