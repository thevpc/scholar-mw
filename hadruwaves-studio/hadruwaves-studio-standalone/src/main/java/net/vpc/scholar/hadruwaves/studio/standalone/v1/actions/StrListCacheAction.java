package net.vpc.scholar.hadruwaves.studio.standalone.v1.actions;

import java.util.Collection;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import net.vpc.lib.pheromone.application.swing.JOptionPane2;
import net.vpc.scholar.hadruwaves.studio.standalone.v1.editors.BuildCacheList;
import net.vpc.scholar.hadruwaves.studio.standalone.v1.editors.MomProjectEditor;
import net.vpc.scholar.hadruwaves.mom.MomCache;


/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public class StrListCacheAction extends StructureAction {
    public StrListCacheAction(MomProjectEditor editor) {
        super(editor, "StrListCacheAction",false);
    }

    public void execute(RunningProjectThread thread) throws Exception {
    }

    protected void terminateProcess(RunningProjectThread thread) {
        try {
            Collection<MomCache> allCaches=thread.getHelper(true).getAllCaches();
            BuildCacheList l = new BuildCacheList(allCaches.toArray(new MomCache[allCaches.size()]));
            JScrollPane p = new JScrollPane(l);
            JOptionPane.showMessageDialog(getEditor(), p, "Cache", JOptionPane.INFORMATION_MESSAGE, null);
        } catch (Exception e) {
            JOptionPane2.showErrorDialog(e);
        }
    }

}
