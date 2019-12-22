package net.vpc.scholar.hadruwaves.studio.standalone.actions;

import net.vpc.scholar.hadruwaves.studio.standalone.editors.PlotConfigData;
import net.vpc.scholar.hadruwaves.studio.standalone.editors.PlotConfigEditor;
import net.vpc.scholar.hadruwaves.studio.standalone.editors.MomProjectEditor;
import net.vpc.scholar.hadruwaves.studio.standalone.buildactions.BuildZinSerieAction;
import net.vpc.scholar.hadrumaths.Complex;

/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public class StrPlotZinAction extends StructureAction {
    public StrPlotZinAction(MomProjectEditor editor) {
        super(editor, "StrPlotZinAction", false);
    }

    @Override
    protected void configure(RunningProjectThread thread) {
        PlotConfigEditor configPanel = new PlotConfigEditor(thread.getMomProjectEditor());
        configPanel.setVisibleZ0Panel(true);
        configPanel.setVisibleIterPanel(true);
        PlotConfigData config = configPanel.showDialog(getEditor(), getTitle(),
                getEditor().getProject());
        if (config == null) {
            thread.setStopped(true);
            return;
        }
        config.recompile();
        thread.getProperties().put("config", config);
    }

    public void execute(RunningProjectThread thread) throws Exception {
        PlotConfigData plotConfig = (PlotConfigData) thread.getProperties().get("config");
        int index1 = thread.getHelper(true).getStructureConfig().evaluateInt(plotConfig.index1)-1;
        int index2 = thread.getHelper(true).getStructureConfig().evaluateInt(plotConfig.index2)-1;

        double[] freqs = plotConfig.getIteratorValues();
        BuildZinSerieAction runAction = new BuildZinSerieAction(thread.getHelper(true), plotConfig.getIteratorName(), freqs,index1,index2);
        thread.setRunAction(runAction);

        showPlotComplex(freqs, (Complex[]) thread.getRunAction().go());
    }
}
