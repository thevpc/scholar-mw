package net.vpc.scholar.hadruwaves.studio.standalone.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import net.vpc.lib.pheromone.application.ApplicationRenderer;
import net.vpc.lib.pheromone.application.swing.JFileChooser2;
import net.vpc.lib.pheromone.ariana.util.Resources;

import net.vpc.scholar.hadruwaves.studio.standalone.TMWLabApplication;
import net.vpc.scholar.hadruwaves.studio.standalone.editors.MomProjectEditor;


/**
 * Created by IntelliJ IDEA.
 * User: taha
 * Date: 7 juil. 2003
 * Time: 14:00:39
 * To change this template use Options | File Templates.
 */
public class LoadStructureAction extends TmwlabAction {
    File lastLoadedFile = null;
    public LoadStructureAction(ApplicationRenderer application) {
        super(application, "LoadStructureAction", application.getResources());
    }

    public void applicationActionPerformed(ActionEvent e) {
        JFileChooser2 jFileChooser = new JFileChooser2(getApplication().getFileManager().getFileTypeManager());
        jFileChooser.addChoosableFileFilter("structure");
        jFileChooser.setSelectedFile(lastLoadedFile == null ? lastLoadedFile : lastLoadedFile.getAbsoluteFile());
        int ret = jFileChooser.showOpenDialog(null);
        jFileChooser.setMultiSelectionEnabled(true);
        if (ret == JFileChooser.APPROVE_OPTION) {
            load(jFileChooser.getSelectedFile());
//            File[] files = jFileChooser.getSelectedFiles();
//            for (File file : files) {
//                load(file);
//            }
        }
    }

    public void load(File file) {
        try {
            TMWLabApplication application = (TMWLabApplication)getApplication();
            application.getRecentFilesMenu().addFile(file);
            getApplicationRenderer().getWindowManager().addWindow("Structure",
                    "Structure",
                    Resources.loadImageIcon("/net/vpc/research/tmwlab/resources/images/Structure.gif"),
                    new MomProjectEditor(getApplicationRenderer(),file));
            lastLoadedFile = file;
        } catch (Throwable e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(null, e1.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
