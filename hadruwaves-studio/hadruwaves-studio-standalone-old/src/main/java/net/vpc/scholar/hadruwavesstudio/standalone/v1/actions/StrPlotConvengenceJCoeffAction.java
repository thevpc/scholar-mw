package net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions;

import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.PlotConfigData;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.PlotConfigEditor;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.MomProjectEditor;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.buildactions.BuildConvengenceJCoeffAction;
import net.thevpc.scholar.hadrumaths.Complex;

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
            c_err[i]=Complex.of(d_err[i]);
            iterations[i]=allIterations[i+1];
        }
        showPlotComplex(iterations,c_err);
    }
}
