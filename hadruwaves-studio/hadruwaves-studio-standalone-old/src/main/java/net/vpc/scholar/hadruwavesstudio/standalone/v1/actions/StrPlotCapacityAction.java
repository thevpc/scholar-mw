package net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions;

import net.thevpc.scholar.hadruwavesstudio.standalone.v1.buildactions.BuildCapacitySerieAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.PlotConfigData;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.PlotConfigEditor;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.MomProjectEditor;
import net.thevpc.scholar.hadrumaths.Complex;

/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public class StrPlotCapacityAction extends StructureAction {
    public StrPlotCapacityAction(MomProjectEditor editor) {
        super(editor, "StrPlotCapacityAction", false);
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
        double[] freqs = plotConfig.getIteratorValues();
        int index1 = thread.getHelper(true).getStructureConfig().evaluateInt(plotConfig.index1)-1;
        int index2 = thread.getHelper(true).getStructureConfig().evaluateInt(plotConfig.index2)-1;

        BuildCapacitySerieAction runAction = new BuildCapacitySerieAction(thread.getHelper(true), plotConfig.getIteratorName(), freqs,index1,index2);
        thread.setRunAction(runAction);

        showPlotComplex(freqs, (Complex[]) thread.getRunAction().go());
    }
}
