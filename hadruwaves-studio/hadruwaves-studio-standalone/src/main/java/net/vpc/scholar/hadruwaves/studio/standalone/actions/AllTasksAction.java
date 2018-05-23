package net.vpc.scholar.hadruwaves.studio.standalone.actions;

import net.vpc.scholar.hadruwaves.studio.standalone.TMWLabApplication;
import net.vpc.scholar.hadruwaves.studio.standalone.editors.MomProjectEditor;
import net.vpc.scholar.hadruwaves.studio.standalone.editors.PlotConfigData;
import net.vpc.scholar.hadruwaves.studio.standalone.editors.PlotConfigEditor;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.common.util.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadruwaves.mom.project.MomProject;
import net.vpc.scholar.hadruwaves.mom.project.common.RunAction;
import net.vpc.scholar.hadruwaves.studio.standalone.buildactions.BuildEBaseAction;
import net.vpc.scholar.hadruwaves.studio.standalone.buildactions.BuildJBaseAction;
import net.vpc.scholar.hadruwaves.studio.standalone.buildactions.BuildJTestingAction;
import net.vpc.scholar.hadruwaves.studio.standalone.buildactions.BuildS11SerieAction;
import net.vpc.scholar.hadruwaves.studio.standalone.buildactions.BuildSourceBaseAction;
import net.vpc.scholar.hadruwaves.studio.standalone.buildactions.BuildZinSerieAction;

/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public class AllTasksAction extends StructureAction {
    private static final long serialVersionUID = 11111111118L;

    public AllTasksAction(MomProjectEditor editor) {
        super(editor, "AllTasksAction", false);
    }

    @Override
    protected void configure(RunningProjectThread thread) {
        PlotConfigEditor configPanel = new PlotConfigEditor(thread.getMomProjectEditor());
        configPanel.setVisibleAxisPanel(true);
        configPanel.setVisibleLeadingPanel(true);
        configPanel.setVisibleXPanel(true);
        configPanel.setVisibleYPanel(true);

        PlotConfigData config = configPanel.showDialog(getEditor(), getTitle(),
                getEditor().getProject());
        if (config == null) {
            thread.setStopped(true);
        }
        thread.getProperties().put("config", config);
    }

    public void execute(RunningProjectThread thread) throws Exception {
        PlotConfigData plotConfig = (PlotConfigData) thread.getProperties().get("config");
        double[] x = plotConfig.getX();
        double[] y = plotConfig.getY();
        double[] z = plotConfig.getZ();
        double[] f = plotConfig.getIteratorValues();

        MomProject structure = null;
        structure = getEditor().getProject();

        // BUILD
        setTitle("Calcul des Coeff de J (" + getEditor().getProjectName() + ")");
        //Jxy jxy = new Jxy(structure);
        //    jxy.reloadCoeffs ();


        // Jcoeffs
        setTitle("Coefficients de J (" + getEditor().getProjectName() + ")");
        Matrix j = thread.getHelper(true).computeTestcoeff(ProgressMonitorFactory.none());
        Complex[] c = j.getColumn(0).toArray();
        double[] indices = Maths.dsteps(1, c.length, 1.0);
        showPlotComplex(indices, c);
        RunAction runAction = null;
        setTitle("J sur les fonctions de Base (" + getEditor().getProjectName() + ")");
        runAction = new BuildJBaseAction(thread.getHelper(true), plotConfig, x, y);
        thread.setRunAction(runAction);
        showPlotCCubeFunctionVector3D((VDiscrete) runAction.go());


        setTitle("J sur les fonctions de Test (" + getEditor().getProjectName() + ")");
        runAction = new BuildJTestingAction(thread.getHelper(true), plotConfig, x, y);
        thread.setRunAction(runAction);
        showPlotCCubeFunctionVector3D((VDiscrete) runAction.go());


        setTitle("E sur les fonctions de Base (" + getEditor().getProjectName() + ")");
        runAction = new BuildEBaseAction(thread.getHelper(true), plotConfig, x, y, z);
        thread.setRunAction(runAction);
        showPlotCCubeFunctionVector3D((VDiscrete) runAction.go());

        setTitle("Source sur les fonctions de Base (" + getEditor().getProjectName() + ")");
        runAction = new BuildSourceBaseAction(thread.getHelper(true), plotConfig, x, y);
        thread.setRunAction(runAction);
        showPlotComplex(x, y, (Complex[][]) runAction.go());

        setTitle("Variantion de Zin en fonction de la frequence (" + getEditor().getProjectName() + ")");
        runAction = new BuildZinSerieAction(thread.getHelper(true), plotConfig.getIteratorName(), f, 0, 0);
        thread.setRunAction(runAction);
        showPlotComplex(f, (Complex[]) runAction.go());

        setTitle("Variantion de S11 en fonction de la frequence (" + getEditor().getProjectName() + ")");
        runAction = new BuildS11SerieAction(thread.getHelper(true), plotConfig.getIteratorName(), f, 0, 0);
        //runAction=new BuildS11SerieAction(thread.getHelper(true),plotConfig.z0,f,0,0);
        thread.setRunAction(runAction);
        showPlotComplex(f, (Complex[]) runAction.go());
    }
}
