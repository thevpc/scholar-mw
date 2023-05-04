package net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions;

import java.io.File;

import javax.swing.JFileChooser;
import net.vpc.lib.pheromone.application.swing.JFileChooser2;
import net.vpc.lib.pheromone.application.swing.JOptionPane2;

import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.MomProjectEditor;


/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public class StrLoadJCoeffsAction extends StructureAction {
    public StrLoadJCoeffsAction(MomProjectEditor editor) {
        super(editor, "StrLoadJCoeffsAction", false);
    }

    public void execute(RunningProjectThread thread) throws Exception {
    }

    protected void terminateProcess(RunningProjectThread thread) {
        try {
            JFileChooser2 jFileChooser = new JFileChooser2(getEditor().getWorkDir(),thread.getApplication().getFileManager().getFileTypeManager());
            jFileChooser.addChoosableFileFilter("matrix");
            jFileChooser.setSelectedFile(new File(getEditor().getWorkDir(), "J.matrix"));
            if (JFileChooser.APPROVE_OPTION == jFileChooser.showOpenDialog(getEditor())) {
//                getEditor().getJxy().setJcoeffs(new CVector(jFileChooser.getSelectedFile()));
            }
        } catch (Exception e) {
            JOptionPane2.showErrorDialog(e);
        }
    }

}
