package net.vpc.scholar.hadruwaves.studio.standalone.actions;

import java.io.File;

import javax.swing.JFileChooser;
import net.vpc.lib.pheromone.application.swing.JFileChooser2;

import net.vpc.scholar.hadruwaves.studio.standalone.editors.MomProjectEditor;
import net.vpc.common.mon.ProgressMonitorFactory;


/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public class StrSaveJCoeffsAction extends StructureAction {
    public StrSaveJCoeffsAction(MomProjectEditor editor) {
        super(editor, "StrSaveJCoeffsAction",true);
    }

    public void execute(RunningProjectThread thread) throws Exception {
        JFileChooser2 jFileChooser = new JFileChooser2(getEditor().getWorkDir(),getApplication().getFileManager().getFileTypeManager());
        jFileChooser.addChoosableFileFilter("matrix");
        jFileChooser.setSelectedFile(new File(getEditor().getWorkDir(), "J.matrix"));
        if (JFileChooser.APPROVE_OPTION == jFileChooser.showSaveDialog(getEditor())) {
            thread.getHelper(true).computeTestcoeff(ProgressMonitorFactory.none()).store(jFileChooser.getSelectedFile());
        }
    }
}
