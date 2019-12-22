package net.vpc.scholar.hadruwaves.studio.standalone.actions;

import net.vpc.scholar.hadruwaves.studio.standalone.editors.PlotConfigData;
import net.vpc.scholar.hadruwaves.studio.standalone.editors.PlotConfigEditor;
import net.vpc.scholar.hadruwaves.studio.standalone.editors.MomProjectEditor;
import net.vpc.scholar.hadruwaves.studio.standalone.buildactions.BuildConvengenceJCoeffAction;
import net.vpc.scholar.hadrumaths.Complex;

/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public class StrPlotConvengenceJCoeffAction extends StructureAction {
    public StrPlotConvengenceJCoeffAction(MomProjectEditor editor) {
        super(editor, "StrPlotConvengenceJCoeffAction", false);
    }

    protected void configure(RunningProjectThread thread) {
        PlotConfigEditor configPanel = new PlotConfigEditor(thread.getMomProjectEditor());
        configPanel.setVisibleIterPanel(true);
        configPanel.setVisibleRelativeErrPanel(true);
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

        BuildConvengenceJCoeffAction runAction = new BuildConvengenceJCoeffAction(
                thread.getHelper(true),
                plotConfig.iterName,
                plotConfig.iterMin,
                plotConfig.iterMax,
                plotConfig.iterCount,
                plotConfig.relativeErr
        );
        thread.setRunAction(runAction);
        Double[] d_err=(Double[]) thread.getRunAction().go();
        Complex[] c_err=new Complex[d_err.length];
        double[] allIterations=runAction.getIterations();
        double[] iterations=new double[d_err.length];
        for (int i = 0; i < d_err.length; i++) {
            c_err[i]=Complex.valueOf(d_err[i]);
            iterations[i]=allIterations[i+1];
        }
        showPlotComplex(iterations,c_err);
    }
}
