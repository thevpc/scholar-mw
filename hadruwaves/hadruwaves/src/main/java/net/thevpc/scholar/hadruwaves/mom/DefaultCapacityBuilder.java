package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadruwaves.str.MWStructure;
import net.thevpc.scholar.hadruwaves.builders.AbstractCapacityBuilder;

import static net.thevpc.scholar.hadrumaths.Complex.I;
import static net.thevpc.scholar.hadruwaves.Physics.omega;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class DefaultCapacityBuilder extends AbstractCapacityBuilder {


    public DefaultCapacityBuilder(MWStructure momStructure) {
        super(momStructure);
    }

    public ComplexMatrix evalMatrixImpl(ProgressMonitor evalMonitor) {
        MomStructure momStructure = (MomStructure)getStructure();
        ProgressMonitor[] mons = evalMonitor.split(.9, .1);
        ComplexMatrix z = momStructure.inputImpedance().monitor(mons[0]).evalMatrix();
        Complex[][] cc = z.getArrayCopy();
        double o = omega(momStructure.getFrequency());
        for (int i = 0; i < cc.length; i++) {
            Complex[] complexes = cc[i];
            for (int j = 0; j < complexes.length; j++) {
                Complex zz = complexes[j];
                cc[i][j] = Maths.inv(zz.mul(I).mul(o));
            }
            mons[1].setProgress(i,cc.length);
        }
        return Maths.matrix(cc);
    }



}
