package net.vpc.scholar.hadruwaves.studio.standalone.v1.actions;

import net.vpc.scholar.hadruwaves.studio.standalone.v1.editors.PlotConfigData;
import net.vpc.scholar.hadruwaves.studio.standalone.v1.editors.PlotConfigEditor;
import net.vpc.scholar.hadruwaves.studio.standalone.v1.editors.MomProjectEditor;
import net.vpc.scholar.hadruwaves.studio.standalone.v1.buildactions.BuildS11SerieAction;
import net.vpc.scholar.hadrumaths.Complex;

/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public class StrPlotS11Action extends StructureAction {
    public StrPlotS11Action(MomProjectEditor editor) {
        super(editor, "StrPlotS11Action", false);
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

        BuildS11SerieAction runAction = new BuildS11SerieAction(thread.getHelper(true), plotConfig.getIteratorName(), freqs,0,0);
        thread.setRunAction(runAction);

        showPlotComplex(freqs, (Complex[]) thread.getRunAction().go());
    }
}
