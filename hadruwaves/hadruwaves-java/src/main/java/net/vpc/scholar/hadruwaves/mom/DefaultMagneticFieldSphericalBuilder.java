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
import net.vpc.scholar.hadruwaves.builders.AbstractMagneticFieldSphericalBuilder;
import net.vpc.scholar.hadruwaves.str.MWStructure;

import static net.vpc.scholar.hadrumaths.Maths.*;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public class DefaultMagneticFieldSphericalBuilder extends AbstractMagneticFieldSphericalBuilder {


    public DefaultMagneticFieldSphericalBuilder(MWStructure momStructure, ApertureType apertureType) {
        super(momStructure,apertureType);
    }


    public Vector<ComplexMatrix> evalMatrix(double[] theta, double[] phi, final double r) {
        return new StrSubCacheSupport<Vector<ComplexMatrix>>(getStructure(), "magnetic-field", CacheKey.obj("spherical-evalMatrix"
                , "theta", theta, "phi", phi, "r", r
                , "apertureType", getApertureType()
        ), getMonitor()) {
            @Override
            public Vector<ComplexMatrix> eval(ObjectCache momCache) {
                return invokeMonitoredAction(getMonitor(), getClass().getSimpleName(), new MonitoredAction<Vector<ComplexMatrix>>() {
                    @Override
                    public Vector<ComplexMatrix> process(final ProgressMonitor monitor, String messagePrefix) throws Exception {
                        switch (getApertureType()) {
                            case PEC:
                                return evaluatePEC(theta, phi, r, getMonitor());
                        }
                        throw new IllegalArgumentException("Unsupported");
                    }
                });
            }
        }.evalCached();
    }

    @Override
    public Vector<ComplexMatrix> evalMatrix(double[] theta, double phi, double[] r) {
        return new StrSubCacheSupport<Vector<ComplexMatrix>>(getStructure(), "magnetic-field", CacheKey.obj("spherical-evalMatrix"
                , "theta", theta, "phi", phi, "r", r
                , "apertureType", getApertureType()
        ), getMonitor()) {
            @Override
            public Vector<ComplexMatrix> eval(ObjectCache momCache) {
                return invokeMonitoredAction(getMonitor(), getClass().getSimpleName(), new MonitoredAction<Vector<ComplexMatrix>>() {
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
        return new StrSubCacheSupport<Vector<ComplexMatrix>>(getStructure(), "magnetic-field", CacheKey.obj("spherical-evalMatrix"
                , "theta", theta, "phi", phi, "r", r
                , "apertureType", getApertureType()
        ), getMonitor()) {
            @Override
            public Vector<ComplexMatrix> eval(ObjectCache momCache) {
                return invokeMonitoredAction(getMonitor(), getClass().getSimpleName(), new MonitoredAction<Vector<ComplexMatrix>>() {
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
        throw new IllegalArgumentException("Not Supported yet");
    }


}
