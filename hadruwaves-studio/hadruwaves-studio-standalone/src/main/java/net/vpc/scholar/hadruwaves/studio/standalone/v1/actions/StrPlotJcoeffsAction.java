package net.vpc.scholar.hadruwaves.studio.standalone.v1.actions;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadruwaves.studio.standalone.v1.editors.MomProjectEditor;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.common.mon.ProgressMonitors;
import net.vpc.scholar.hadrumaths.ComplexVector;

/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public class StrPlotJcoeffsAction extends StructureAction {
    public StrPlotJcoeffsAction(MomProjectEditor editor) {
        super(editor, "StrPlotJcoeffsAction",true);
    }

    public void execute(RunningProjectThread thread) throws Exception {
        ComplexVector j = thread.getHelper(true).evalXMatrix(ProgressMonitors.none()).getColumn(0);
        Complex[] y = new Complex[j.size()];
        double[] x = Maths.dsteps(1, y.length, 1.0);
        for (int i = 0; i < y.length; i++) {
            y[i] = j.get(i);
        }
        showPlotComplex(x, y);
    }
}
