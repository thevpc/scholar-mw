package net.vpc.scholar.hadruwaves.studio.standalone.actions;

import net.vpc.scholar.hadruwaves.studio.standalone.editors.MomProjectEditor;
import net.vpc.scholar.hadruwaves.studio.standalone.MatrixDialog;
import net.vpc.scholar.hadruwaves.studio.standalone.buildactions.BuildZinAction;
import net.vpc.scholar.hadrumaths.Matrix;


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
        Matrix zin = new BuildZinAction(thread.getHelper(true)).go0();
        thread.getProperties().put("zin", zin);
    }

    protected void terminateProcess(RunningProjectThread thread) {
        Matrix zin = (Matrix) thread.getProperties().get("zin");
        MatrixDialog.showMatrix(getResources().get("displayZin.title", getEditor().getProjectName()),zin);
    }
}
