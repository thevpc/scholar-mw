package net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import net.vpc.lib.pheromone.ariana.util.Resources;

import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.MomProjectEditor;
import net.thevpc.scholar.hadruwaves.mom.project.common.RunAction;
import net.thevpc.scholar.hadruwaves.mom.util.MomStrHelper;

/**
 * Created by IntelliJ IDEA.
 * User: taha
 * Date: 7 juil. 2003
 * Time: 14:00:39
 * 
 */
public class StrDumpAction extends StructureAction {

    public StrDumpAction(MomProjectEditor editor) {
        super(editor, "StrDumpAction", false);
    }

    public void execute(RunningProjectThread thread) throws Exception {
        final MomStrHelper jxy = thread.getHelper(true);
        thread.setRunAction(new RunAction() {

            @Override
            protected Object run() {
                JTextArea a = new JTextArea(jxy.dump());
                JScrollPane sp = new JScrollPane(a);
                getApplicationRenderer().getWindowManager().addWindow("Structure",
                        "Structure Dump",
                        Resources.loadImageIcon("/net/thevpc/scholar/hadruwavesstudio/standalone/v1/images/Structure.gif"),
                        sp);
                return null;
            }
        });
        thread.getRunAction().go();
    }

}
