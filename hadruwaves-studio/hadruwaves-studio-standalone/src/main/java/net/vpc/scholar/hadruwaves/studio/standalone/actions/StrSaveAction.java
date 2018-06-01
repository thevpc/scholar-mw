package net.vpc.scholar.hadruwaves.studio.standalone.actions;

import java.io.File;

import javax.swing.JFileChooser;
import net.vpc.common.prs.util.IOUtils;
import net.vpc.lib.pheromone.application.swing.JFileChooser2;

import net.vpc.scholar.hadruwaves.studio.standalone.editors.MomProjectEditor;
import net.vpc.scholar.hadruwaves.studio.standalone.TMWLabApplication;


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
        String ext = IOUtils.getFileExtension(selectFile);
        if(ext.length()==0){
            selectFile=new File(selectFile.getParentFile(),selectFile.getName()+".str");
        }
        getEditor().saveStruct(selectFile);
        TMWLabApplication a = (TMWLabApplication) getApplication();
        a.getRecentFilesMenu().addFile(selectFile);
    }
}