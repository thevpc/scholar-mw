package net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions;

import java.io.File;

import javax.swing.JFileChooser;

import net.thevpc.common.io.FileUtils;
import net.vpc.lib.pheromone.application.swing.JFileChooser2;

import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.MomProjectEditor;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.TMWLabApplication;


/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public class StrSaveAction extends StructureAction {
    public StrSaveAction(MomProjectEditor editor) {
        super(editor, "StrSaveAction",false);
    }

    protected void configure(RunningProjectThread thread) {
        if (getEditor().getConfigFile() == null) {
            JFileChooser2 jFileChooser = new JFileChooser2();
            jFileChooser.addChoosableFileFilter("structure");
            if (getEditor().getWorkDir() != null) {
                jFileChooser.setCurrentDirectory(getEditor().getWorkDir());
            }
            if (getEditor().getConfigFile() != null) {
                jFileChooser.setSelectedFile(getEditor().getConfigFile());
            }
            if (JFileChooser.APPROVE_OPTION != jFileChooser.showSaveDialog(getEditor())) {
                thread.setStopped(true);
                return;
            }
            thread.getProperties().put("SelectedFile", jFileChooser.getSelectedFile());
        } else {
            thread.getProperties().put("SelectedFile", getEditor().getConfigFile());
        }
    }

    public void execute(RunningProjectThread thread) throws Exception {
        File selectFile = (File) thread.getProperties().get("SelectedFile");
        String ext = FileUtils.getFileExtension(selectFile);
        if(ext.length()==0){
            selectFile=new File(selectFile.getParentFile(),selectFile.getName()+".str");
        }
        getEditor().saveStruct(selectFile);
        TMWLabApplication a = (TMWLabApplication) getApplication();
        a.getRecentFilesMenu().addFile(selectFile);
    }
}
