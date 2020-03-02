package net.vpc.scholar.hadruwaves.studio.standalone.v1.actions;

import net.vpc.scholar.hadruwaves.studio.standalone.v1.MatrixDialog;
import net.vpc.scholar.hadruwaves.studio.standalone.v1.buildactions.BuildCapacityAction;
import net.vpc.scholar.hadruwaves.studio.standalone.v1.editors.MomProjectEditor;
import net.vpc.scholar.hadrumaths.ComplexMatrix;


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
        ComplexMatrix capa = new BuildCapacityAction(thread.getHelper(true)).go0();
        thread.getProperties().put("capacity", capa);
    }

    protected void terminateProcess(RunningProjectThread thread) {
        ComplexMatrix capa = (ComplexMatrix) thread.getProperties().get("capacity");
        MatrixDialog.showMatrix(getResources().get("displayCapacity.title", getEditor().getProjectName()), capa);
    }
}
