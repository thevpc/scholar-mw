package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public class DefaultPoyntingVectorSphericalBuilder extends AbstractPoyntingVectorSphericalBuilder {
    public DefaultPoyntingVectorSphericalBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public ComplexMatrix evalModuleMatrix(double[] theta, double[] phi, double r) {
        ComplexMatrix Etot = getStructure().electricField().spherical().monitor(getMonitor()).evalModuleMatrix(theta, phi, r);
        return Etot.sqr().mul(r * r / 2).div(Maths.sqrt(Maths.U0 / Maths.EPS0));
    }

    @Override
    public ComplexMatrix evalModuleMatrix(double[] theta, double phi, double[] r) {
        ComplexMatrix Etot = getStructure().electricField().spherical().monitor(getMonitor()).evalModuleMatrix(theta, phi, r);
        Etot = Etot.copy().sqr().div(2 * Maths.sqrt(Maths.U0 / Maths.EPS0));
        for (int i = 0; i < Etot.getRowCount(); i++) {
            for (int j = 0; j < Etot.getColumnCount(); j++) {
                double r0 = r[j];
                Etot.mul(r0 * r0);
            }
        }
        return Etot;
    }

    @Override
    public ComplexMatrix evalModuleMatrix(double theta, double[] phi, double[] r) {
        ComplexMatrix Etot = getStructure().electricField().spherical().monitor(getMonitor()).evalModuleMatrix(theta, phi, r);
        Etot = Etot.copy().sqr().div(2 * Maths.sqrt(Maths.U0 / Maths.EPS0));
        for (int i = 0; i < Etot.getRowCount(); i++) {
            for (int j = 0; j < Etot.getColumnCount(); j++) {
                double r0 = r[j];
                Etot.mul(r0 * r0);
            }
        }
        return Etot;
    }
}
