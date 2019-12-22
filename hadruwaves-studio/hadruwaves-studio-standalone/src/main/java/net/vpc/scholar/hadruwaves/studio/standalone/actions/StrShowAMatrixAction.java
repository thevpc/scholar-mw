package net.vpc.scholar.hadruwaves.studio.standalone.actions;

import net.vpc.scholar.hadruwaves.studio.standalone.editors.MomProjectEditor;
import net.vpc.scholar.hadruwaves.mom.project.common.RunAction;
import net.vpc.scholar.hadruwaves.mom.util.MomStrHelper;

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
                plotter().title(getTitle()).asMatrix().plot(jxy.computeAMatrix(this));

//                getApplicationRenderer().getWindowManager().addWindow(getKey(),
//                getTitle(),
//                Resources.loadImageIcon("/net/vpc/research/tmwlab/resources/images/Graph.gif"),
//                Plot.plotMatrix("AMatrix", jxy.computeAMatrix(ProgressMonitorFactory.none()),PlotType.SURFACE));
                return null;
            }
        });
        thread.getRunAction().go();
    }
}
