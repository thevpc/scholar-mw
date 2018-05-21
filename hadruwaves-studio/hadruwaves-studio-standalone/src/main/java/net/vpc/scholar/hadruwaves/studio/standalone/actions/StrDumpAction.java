package net.vpc.scholar.hadruwaves.studio.standalone.actions;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import net.vpc.lib.pheromone.ariana.util.Resources;

import net.vpc.scholar.hadruwaves.studio.standalone.editors.MomProjectEditor;
import net.vpc.scholar.hadruwaves.mom.project.common.RunAction;
import net.vpc.scholar.hadruwaves.mom.util.MomStrHelper;

/**
 * Created by IntelliJ IDEA.
 * User: taha
 * Date: 7 juil. 2003
 * Time: 14:00:39
 * To change this template use Options | File Templates.
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
                JTextArea a = new JTextArea(jxy.getDump());
                JScrollPane sp = new JScrollPane(a);
                getApplicationRenderer().getWindowManager().addWindow("Structure",
                        "Structure Dump",
                        Resources.loadImageIcon("/net/vpc/research/tmwlab/resources/images/Structure.gif"),
                        sp);
                return null;
            }
        });
        thread.getRunAction().go();
    }

}
