package net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions;

import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.PlotConfigData;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.PlotConfigEditor;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.MomProjectEditor;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.buildactions.BuildDurationJCoeffAction;
import net.thevpc.scholar.hadrumaths.Complex;

/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public class StrPlotDurationJCoeffAction extends StructureAction {
    public StrPlotDurationJCoeffAction(MomProjectEditor editor) {
        super(editor, "StrPlotDurationJCoeffAction", false);
    }

    protected void configure(RunningProjectThread thread) {
        PlotConfigEditor configPanel = new PlotConfigEditor(thread.getMomProjectEditor());
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

        BuildDurationJCoeffAction runAction = new BuildDurationJCoeffAction(
                thread.getHelper(true),
                plotConfig.iterName,
                plotConfig.iterMin,
                plotConfig.iterMax,
                plotConfig.iterCount
        );
        thread.setRunAction(runAction);
        Long[] durations=(Long[]) thread.getRunAction().go();
        Complex[] durations_c=new Complex[durations.length];
        for (int i = 0; i < durations_c.length; i++) {
            durations_c[i]=Complex.of(durations[i]);
        }
        showPlotComplex(runAction.getIterations(),durations_c);
    }
}
