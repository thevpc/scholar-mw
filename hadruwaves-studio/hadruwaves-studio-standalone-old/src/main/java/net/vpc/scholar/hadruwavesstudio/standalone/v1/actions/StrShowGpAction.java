package net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions;

import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.MomProjectEditor;
import net.thevpc.scholar.hadruwaves.mom.project.common.RunAction;
import net.thevpc.scholar.hadruwaves.mom.util.MomStrHelper;

/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public class StrShowGpAction extends StructureAction {
    public StrShowGpAction(MomProjectEditor editor) {
        super(editor, "StrShowGpAction",false);
    }

    public void execute(final RunningProjectThread thread) throws Exception {
        
        thread.setRunAction(new RunAction() {
            @Override
            protected Object run() {
                MomStrHelper jxy = thread.getHelper(true);
                plotter().title(getTitle()).domain(jxy.getStructure().getDomain()).plot(jxy.getStructure().testFunctions().arr());
                return null;
            }
        });
        thread.getRunAction().go();
    }
}
