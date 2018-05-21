package net.vpc.scholar.hadruwaves.studio.standalone.actions;

import net.vpc.scholar.hadruwaves.studio.standalone.MatrixDialog;
import net.vpc.scholar.hadruwaves.studio.standalone.buildactions.BuildCapacityAction;
import net.vpc.scholar.hadruwaves.studio.standalone.editors.MomProjectEditor;
import net.vpc.scholar.hadrumaths.Matrix;


/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public class StrCapacityAction extends StructureAction {
    public StrCapacityAction(MomProjectEditor editor) {
        super(editor, "StrCapacityAction", true);
    }

    public void execute(RunningProjectThread thread) throws Exception {
        Matrix capa = new BuildCapacityAction(thread.getHelper(true)).go0();
        thread.getProperties().put("capacity", capa);
    }

    protected void terminateProcess(RunningProjectThread thread) {
        Matrix capa = (Matrix) thread.getProperties().get("capacity");
        MatrixDialog.showMatrix(getResources().get("displayCapacity.title", getEditor().getProjectName()), capa);
    }
}
