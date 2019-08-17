package net.vpc.scholar.hadruwaves.studio.standalone.actions;

import net.vpc.scholar.hadruwaves.studio.standalone.editors.MomProjectEditor;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.Vector;

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
        Vector j = thread.getHelper(true).computeTestcoeff(ProgressMonitorFactory.none()).getColumn(0);
        Complex[] y = new Complex[j.size()];
        double[] x = Maths.dsteps(1, y.length, 1.0);
        for (int i = 0; i < y.length; i++) {
            y[i] = j.get(i);
        }
        showPlotComplex(x, y);
    }
}
