package net.vpc.scholar.hadruwaves.studio.standalone.v1.actions;

import javax.swing.JOptionPane;

import net.vpc.scholar.hadruwaves.studio.standalone.v1.editors.MomProjectEditor;
import net.vpc.scholar.hadruwaves.studio.standalone.v1.buildactions.BuildJcoeffAction;
import net.vpc.scholar.hadruwaves.mom.util.MomStrHelper;

/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public class StrBuildAction extends StructureAction {
    public StrBuildAction(MomProjectEditor editor) {
        super(editor, "StrBuildAction",false);
    }

    public void execute(RunningProjectThread thread) throws Exception {
        MomStrHelper jxy = thread.getHelper(true);
        thread.setRunAction(new BuildJcoeffAction(jxy));
        thread.getRunAction().go();
        JOptionPane.showMessageDialog(getEditor(), "Build completed successfully");
    }
}
