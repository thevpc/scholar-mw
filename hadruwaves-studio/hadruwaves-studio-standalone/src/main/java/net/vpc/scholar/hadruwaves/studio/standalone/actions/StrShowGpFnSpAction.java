package net.vpc.scholar.hadruwaves.studio.standalone.actions;

import net.vpc.scholar.hadruwaves.studio.standalone.editors.MomProjectEditor;
import net.vpc.scholar.hadruwaves.mom.project.common.RunAction;
import net.vpc.scholar.hadruwaves.mom.util.MomStrHelper;

/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public class StrShowGpFnSpAction extends StructureAction {
    public StrShowGpFnSpAction(MomProjectEditor editor) {
        super(editor, "StrShowGpFnSpAction",false);
    }

    public void execute(final RunningProjectThread thread) throws Exception {
        
        thread.setRunAction(new RunAction() {
            @Override
            protected Object run() {
                MomStrHelper jxy = thread.getHelper(true);
                plotter().title(getTitle()).plot(jxy.getStructure().getTestSourceScalarProducts(this));
                return null;
            }
        });
        thread.getRunAction().go();
    }
}
