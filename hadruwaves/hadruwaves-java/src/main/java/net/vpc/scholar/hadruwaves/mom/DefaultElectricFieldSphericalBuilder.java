package net.vpc.scholar.hadruwaves.mom;

import net.vpc.common.mon.MonitoredAction;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.cache.CacheKey;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.double2complex.CDiscrete;
import net.vpc.scholar.hadrumaths.symbolic.double2double.Linear;
import net.vpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;
import net.vpc.scholar.hadruwaves.ApertureType;
import net.vpc.scholar.hadruwaves.Physics;
import net.vpc.scholar.hadruwaves.builders.AbstractElectricFieldSphericalBuilder;
import net.vpc.scholar.hadruwaves.str.MWStructure;

import static net.vpc.scholar.hadrumaths.Maths.*;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public class DefaultElectricFieldSphericalBuilder extends AbstractElectricFieldSphericalBuilder {


    public DefaultElectricFieldSphericalBuilder(MWStructure momStructure, ElectricFieldPart part,ApertureType apertureType) {
        super(momStructure,part,apertureType);
    }


    public Vector<ComplexMatrix> evalMatrix(double[] theta, double[] phi, final double r) {
        return new StrSubCacheSupport<Vector<ComplexMatrix>>(getStructure(), "electric-field", CacheKey.obj("spherical-evalMatrix"
                , "theta", theta, "phi", phi, "r", r
                , "apertureType", getApertureType(), "fieldPart", getElectricFieldPart()
        ), getMonitor()) {
            @Override
            public Vector<ComplexMatrix> eval(ObjectCache momCache, ProgressMonitor cacheMonitor) {
                return invokeMonitoredAction(cacheMonitor, getClass().getSimpleName(), new MonitoredAction<Vector<ComplexMatrix>>() {
                    @Override
                    public Vector<ComplexMatrix> process(final ProgressMonitor monitor, String messagePrefix) throws Exception {
                        switch (getApertureType()) {
                            case PEC:
                                return evaluatePEC(theta, phi, r, monitor);
                        }
                        throw new IllegalArgumentException("Unsupported");
                    }
                });
            }
        }.evalCached();
    }

    @Override
    public Vector<ComplexMatrix> evalMatrix(double[] theta, double phi, double[] r) {
        return new StrSubCacheSupport<Vector<ComplexMatrix>>(getStructure(), "electric-field", CacheKey.obj("spherical-evalMatrix"
                , "theta", theta, "phi", phi, "r", r
                , "apertureType", getApertureType(), "fieldPart", getElectricFieldPart()
        ), getMonitor()) {
            @Override
            public Vector<ComplexMatrix> eval(ObjectCache momCache, ProgressMonitor cacheMonitor) {
                return invokeMonitoredAction(cacheMonitor, getClass().getSimpleName(), new MonitoredAction<Vector<ComplexMatrix>>() {
                    @Override
                    public Vector<ComplexMatrix> process(final ProgressMonitor monitor, String messagePrefix) throws Exception {
                        return evalMatrixImpl(theta, phi, r);
                    }
                });
            }
        }.evalCached();
    }
    @Override
    public Vector<ComplexMatrix> evalMatrix(double theta, double[] phi, double[] r) {
        return new StrSubCacheSupport<Vector<ComplexMatrix>>(getStructure(), "electric-field", CacheKey.obj("spherical-evalMatrix"
                , "theta", theta, "phi", phi, "r", r
                , "apertureType", getApertureType(), "fieldPart", getElectricFieldPart()
        ), getMonitor()) {
            @Override
            public Vector<ComplexMatrix> eval(ObjectCache momCache, ProgressMonitor cacheMonitor) {
                return invokeMonitoredAction(cacheMonitor, getClass().getSimpleName(), new MonitoredAction<Vector<ComplexMatrix>>() {
                    @Override
                    public Vector<ComplexMatrix> process(final ProgressMonitor monitor, String messagePrefix) throws Exception {
                        return evalMatrixImpl(theta, phi, r);
                    }
                });
            }
        }.evalCached();
    }

    public Vector<ComplexMatrix> evalMatrixImpl(double[] theta, double phi, double[] r) {
        Complex[][] EthetaMatrix = new Complex[theta.length][r.length];
        Complex[][] EphiMatrix = new Complex[theta.length][r.length];
        Complex[][] ErMatrix = new Complex[theta.length][r.length];
        for (int i = 0; i < r.length; i++) {
            Vector<ComplexMatrix> t = evalMatrix(theta, new double[]{phi}, r[i]);
            for (int j = 0; j < 3; j++) {
                Complex[][] to = j == 0 ? EthetaMatrix : j == 1 ? EphiMatrix : ErMatrix;
                ComplexMatrix a = t.get(j);
                for (int l = 0; l < a.getRowCount(); l++) {
                    to[l][j] = a.get(l, 0);
                }
            }
        }
        Vector<ComplexMatrix> tMatrices = new ArrayVector<ComplexMatrix>($MATRIX,false,3);
        tMatrices.append(matrix(EthetaMatrix));
        tMatrices.append(matrix(EphiMatrix));
        tMatrices.append(matrix(ErMatrix));
        return tMatrices;

    }

    public Vector<ComplexMatrix> evalMatrixImpl(double theta, double[] phi, double[] r) {
        Complex[][] EthetaMatrix = new Complex[phi.length][r.length];
        Complex[][] EphiMatrix = new Complex[phi.length][r.length];
        Complex[][] ErMatrix = new Complex[phi.length][r.length];
        for (int i = 0; i < r.length; i++) {
            Vector<ComplexMatrix> t = evalMatrix(new double[]{theta}, phi, r[i]);
            for (int j = 0; j < 3; j++) {
                Complex[][] to = j == 0 ? EthetaMatrix : j == 1 ? EphiMatrix : ErMatrix;
                ComplexMatrix a = t.get(j);
                for (int l = 0; l < a.getColumnCount(); l++) {
                    to[l][j] = a.get(0, l);
                }
            }
        }

        Vector<ComplexMatrix> tMatrices = new ArrayVector<ComplexMatrix>($MATRIX,false,3);
        tMatrices.append(matrix(EthetaMatrix));
        tMatrices.append(matrix(EphiMatrix));
        tMatrices.append(matrix(ErMatrix));
        return tMatrices;
    }

    @Override
    public Vector<VDiscrete> evalVDiscrete(double[] theta, double[] phi, double[] r) {
        Complex[][][] EthetaMatrix = new Complex[theta.length][phi.length][r.length];
        Complex[][][] EphiMatrix = new Complex[theta.length][phi.length][r.length];
        Complex[][][] ErMatrix = new Complex[theta.length][phi.length][r.length];

        for (int i = 0; i < r.length; i++) {
            Vector<ComplexMatrix> t = evalMatrix(theta, phi, r[i]);
            for (int j = 0; j < 3; j++) {
                Complex[][][] to = j == 0 ? EthetaMatrix : j == 1 ? EphiMatrix : ErMatrix;
                ComplexMatrix a = t.get(j);
                for (int k = 0; k < a.getRowCount(); k++) {
                    for (int l = 0; l < a.getColumnCount(); l++) {
                        to[k][l][i] = a.get(k, l);
                    }
                }
            }
        }
        Domain d = Domain.ofBounds(theta[0], theta[theta.length - 1], phi[0], phi[phi.length - 1], r[0], r[r.length - 1]);

        Vector<VDiscrete> tMatrices = new ArrayVector<VDiscrete>($VDISCRETE,false,3);
        tMatrices.append(new VDiscrete(new CDiscrete(EthetaMatrix, d)));
        tMatrices.append(new VDiscrete(new CDiscrete(EphiMatrix, d)));
        tMatrices.append(new VDiscrete(new CDiscrete(ErMatrix, d)));
        return tMatrices;
    }

    public Vector<ComplexMatrix> evaluatePEC(final double[] theta, final double[] phi, final double r, ProgressMonitor monitor) {
        final MomStructure structure = getStructure();
        final double k0 = Physics.K0(structure.getFrequency());
        final Complex C1 = (î.mul(-k0).mul(exp(î.mul(-k0 * r)))).div(4.0 * Math.PI * r);
        final ComplexMatrix SPM = structure.getTestModeScalarProducts();
        final ComplexMatrix Xj = structure.matrixX().evalMatrix();
        Complex[][] EthetaMatrix = new Complex[theta.length][phi.length];
        Complex[][] EphiMatrix = new Complex[theta.length][phi.length];
        Complex[][] ErMatrix = ArrayUtils.fillMatrix(new Complex[theta.length][phi.length], Complex.ZERO);
        final String monText = getClass().getSimpleName();
        for (int i = 0; i < theta.length; i++) {
            for (int j = 0; j < phi.length; j++) {
                final int curr_i = i;
                final int curr_j = j;
                final double theta_i = theta[curr_i];
                final double phi_j = phi[curr_j];
                System.out.println("TRY EXEC=" + monitor.getProgress() + " :: " + monText + " " + CharactersTable.THETA + "=" + theta_i + " " + CharactersTable.PHI + "=" + phi_j);
                Complex[] rr = new StrSubCacheSupport<Complex[]>(structure, "spherical-evalMatrix-single",
                        CacheKey.obj("spherical-evalMatrix-single", "theta", theta[i], "phi", phi[j], "r", r,
                                 "apertureType", getApertureType(), "fieldPart", getElectricFieldPart())
                        , monitor.translate(i, theta.length, j, phi.length)) {

                    @Override
                    public Complex[] eval(ObjectCache momCache, ProgressMonitor cacheMonitor) {
                        System.out.println("EXEC=" + monitor.getProgress() + " :: " + monText + " " + CharactersTable.THETA + "=" + theta_i + " " + CharactersTable.PHI + "=" + phi_j);
                        double sin_theta = sin(theta_i);
                        double cos_theta = cos(theta_i);
                        double sin_phi = sin(phi_j);
                        double cos_phi = cos(phi_j);
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
                            fxtheta.add(integrate(Eev.Y().mul(rtheta)));
                            fytheta.add(integrate(Eev.X().mul(rtheta)));
                            fxphi.add(integrate(Eev.Y().mul(rphi)));
                            fyphi.add(integrate(Eev.X().mul(rphi)));
                            mm += 1;
                            //println("mm="+mm)
                        }

//                        fxtheta.mul(2.0);
//                        fytheta.mul(-2.0);
//                        fxphi.mul(-2.0);
//                        fyphi.mul(2.0);

//                        fxtheta.mul(1.0);
                        fytheta.mul(-1.0);
                        fxphi.mul(-1.0);
//                        fyphi.mul(1.0);

                        Complex px_theta = fxtheta.toComplex();
                        Complex py_theta = fytheta.toComplex();
                        Complex Etheta = (py_theta.mul(cos_phi).minus(px_theta.mul(sin_phi))).mul(C1); // Etheta

                        Complex px_phi = fxphi.toComplex();
                        Complex py_phi = fyphi.toComplex();
                        Complex Ephi = (px_phi.mul(cos_theta * cos_phi).plus(py_phi.mul(cos_theta * sin_phi))).mul(C1); // Etheta
                        return new Complex[]{Etheta, Ephi};
                    }
                }.evalCached();

                EthetaMatrix[i][j] = rr[0];
                EphiMatrix[i][j] = rr[1];
            }
        }
        Vector<ComplexMatrix> tMatrices = new ArrayVector<ComplexMatrix>($MATRIX,false,3);
        tMatrices.append(matrix(EthetaMatrix));
        tMatrices.append(matrix(EphiMatrix));
        tMatrices.append(matrix(ErMatrix));
        return tMatrices;
    }


}
