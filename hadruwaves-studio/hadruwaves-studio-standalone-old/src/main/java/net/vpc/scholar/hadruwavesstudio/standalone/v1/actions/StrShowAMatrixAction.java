package net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions;

import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.MomProjectEditor;
import net.thevpc.scholar.hadruwaves.mom.project.common.RunAction;
import net.thevpc.scholar.hadruwaves.mom.util.MomStrHelper;

/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public class StrShowAMatrixAction extends StructureAction {
    public StrShowAMatrixAction(MomProjectEditor editor) {
        super(editor, "StrShowAMatrixAction",false);
    }

    public void execute(final RunningProjectThread thread) throws Exception {
        
        thread.setRunAction(new RunAction() {
            @Override
            protected Object run() {
                MomStrHelper jxy = thread.getHelper(true);
                plotter().title(getTitle()).asMatrix().plot(jxy.evalAMatrix(this));

//                getApplicationRenderer().getWindowManager().addWindow(getKey(),
//                getTitle(),
//                Resources.loadImageIcon("/net/thevpc/scholar/hadruwavesstudio/standalone/v1/images/Graph.gif"),
//                Plot.plotMatrix("AMatrix", jxy.computeAMatrix(ProgressMonitors.none()),PlotType.SURFACE));
                return null;
            }
        });
        thread.getRunAction().go();
    }
}
