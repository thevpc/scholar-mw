package net.vpc.scholar.hadruwaves.studio.standalone.v1.actions;

import net.vpc.scholar.hadruwaves.studio.standalone.v1.editors.MomProjectEditor;
import net.vpc.scholar.hadruwaves.studio.standalone.v1.MatrixDialog;
import net.vpc.scholar.hadruwaves.studio.standalone.v1.buildactions.BuildZinAction;
import net.vpc.scholar.hadrumaths.ComplexMatrix;


/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public class StrZinAction extends StructureAction {
    public StrZinAction(MomProjectEditor editor) {
        super(editor, "StrZinAction",true);
    }

    public void execute(RunningProjectThread thread) throws Exception {
        ComplexMatrix zin = new BuildZinAction(thread.getHelper(true)).go0();
        thread.getProperties().put("zin", zin);
    }

    protected void terminateProcess(RunningProjectThread thread) {
        ComplexMatrix zin = (ComplexMatrix) thread.getProperties().get("zin");
        MatrixDialog.showMatrix(getResources().get("displayZin.title", getEditor().getProjectName()),zin);
    }
}
