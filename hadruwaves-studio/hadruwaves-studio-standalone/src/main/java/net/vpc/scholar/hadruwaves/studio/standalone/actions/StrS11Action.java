package net.vpc.scholar.hadruwaves.studio.standalone.actions;

import net.vpc.scholar.hadruwaves.studio.standalone.MatrixDialog;
import net.vpc.scholar.hadruwaves.studio.standalone.buildactions.BuildS11Action;
import net.vpc.scholar.hadruwaves.studio.standalone.editors.MomProjectEditor;
import net.vpc.scholar.hadruwaves.studio.standalone.editors.PlotConfigData;
import net.vpc.scholar.hadruwaves.studio.standalone.editors.PlotConfigEditor;
import net.vpc.scholar.hadrumaths.ComplexMatrix;


/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public class StrS11Action extends StructureAction {
    public StrS11Action(MomProjectEditor editor) {
        super(editor, "StrS11Action", true);
    }

    protected void configure(RunningProjectThread thread) {
        PlotConfigEditor configPanel = new PlotConfigEditor(thread.getMomProjectEditor());
        configPanel.setVisibleZ0Panel(true);
        PlotConfigData config = configPanel.showDialog(getEditor(), getTitle(),
                getEditor().getProject());
        if (config == null) {
            thread.setStopped(true);
            return;
        }
        thread.getProperties().put("config", config);
    }

    public void execute(RunningProjectThread thread) throws Exception {
        PlotConfigData params = (PlotConfigData) thread.getProperties().get("config");

        ComplexMatrix s11 = new BuildS11Action(thread.getHelper(true)).go0();
        MatrixDialog.showMatrix(thread.getResources().get("displayS11.title", getEditor().getProjectName()), s11);
    }
}
