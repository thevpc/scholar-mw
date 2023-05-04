package net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions;

import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.MomProjectEditor;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.PlotConfigData;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.PlotConfigEditor;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.scholar.hadruwaves.mom.project.MomProject;
import net.thevpc.scholar.hadruwaves.mom.project.common.RunAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.buildactions.BuildEBaseAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.buildactions.BuildJBaseAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.buildactions.BuildJTestingAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.buildactions.BuildS11SerieAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.buildactions.BuildSourceBaseAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.buildactions.BuildZinSerieAction;

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
        ComplexMatrix j = thread.getHelper(true).evalXMatrix(ProgressMonitors.none());
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
