package net.vpc.scholar.hadruwaves.studio.standalone.actions;

import net.vpc.scholar.hadruwaves.studio.standalone.editors.MomProjectEditor;
import net.vpc.scholar.hadruwaves.studio.standalone.editors.PlotConfigData;
import net.vpc.scholar.hadruwaves.studio.standalone.buildactions.BuildSourceBaseAction;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadruwaves.mom.util.MomStrHelper;

/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public class StrPlotSourceAction extends AbstractStrPlotXYAction {
    public StrPlotSourceAction(MomProjectEditor editor){
        super(editor, "StrPlotSourceAction");
    }
    public VDiscrete[] getSeries(RunningProjectThread thread, double[] x, double[] y,double[] z) {
        PlotConfigData plotConfig = (PlotConfigData) thread.getProperties().get("config");
        MomStrHelper jxy = thread.getHelper(true);
        thread.setRunAction(new BuildSourceBaseAction(jxy,plotConfig, x, y));
        return (VDiscrete[]) thread.getRunAction().go();
    }
}
