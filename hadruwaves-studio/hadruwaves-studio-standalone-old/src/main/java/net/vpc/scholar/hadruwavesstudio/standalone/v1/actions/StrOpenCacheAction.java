package net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions;

import java.io.File;
import java.io.IOException;
import net.vpc.lib.pheromone.application.swing.JOptionPane2;

import net.thevpc.scholar.hadruwavesstudio.standalone.v1.TMWLabApplication;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.MomProjectEditor;
import net.thevpc.scholar.hadrumaths.io.HFile;
import net.thevpc.scholar.hadruwaves.mom.MomCache;

/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public class StrOpenCacheAction extends StructureAction {

    public StrOpenCacheAction(MomProjectEditor editor) {
        super(editor, "StrOpenCacheAction", false);
    }

    public void execute(RunningProjectThread thread) throws Exception {
    }

    @Override
    protected void terminateProcess(RunningProjectThread thread) {
        try {
            MomCache c = new MomCache(thread.getHelper(true).getCurrentCache(false));
            if (c != null) {
                HFile file = c.getFolder();
                if (file != null) {
                    try {
                        TMWLabApplication.openInShell(new File(file.getNativeLocalFile()), null);
                        return;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            JOptionPane2.showErrorDialog(getEditor(), "Impossible d'ouvrir le dossier");
        } catch (Exception e) {
            JOptionPane2.showErrorDialog(e);
        }
    }
}
