package net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions;

import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.MomProjectEditor;
import net.thevpc.scholar.hadruwaves.WallBorders;
import net.thevpc.scholar.hadruwaves.mom.ModeFunctions;
import net.thevpc.scholar.hadruwaves.mom.project.common.RunAction;
import net.thevpc.scholar.hadruwaves.mom.util.MomStrHelper;

/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:20:01
 */
public class StrShowFnAction extends StructureAction {
    public StrShowFnAction(MomProjectEditor editor) {
        super(editor, "StrShowFnAction",false);
    }

    public void execute(final RunningProjectThread thread) throws Exception {
        
        thread.setRunAction(new RunAction() {
            @Override
            protected Object run() {
                MomStrHelper jxy = thread.getHelper(true);
                ModeFunctions base = jxy.getStructure().modeFunctions();
                
                
                WallBorders b = jxy.getStructure().getBorders();
                String desc="=>E(x=" + b.getExDescription()+",y="+b.getEyDescription()+") ; J(x="+b.getJxDescription()+",y="+b.getJxDescription()+")";
                plotter().title(getTitle()+" "+desc).domain(jxy.getStructure().getDomain()).plot(jxy.getStructure().modeFunctions().arr());
                
//                FunctionsXYPlotPanel panel = new FunctionsXYPlotPanel(getTitle()+" "+desc, jxy.getStructure().getDomain(), PlotDoubleConverter.ABS, base.fn());

//                etRenderer().getWindowManager().addWindow(getKey(),
//                getTitle(),
//                Resources.loadImageIcon("/net/thevpc/scholar/hadruwavesstudio/standalone/v1/images/Graph.gif"),
//                panel);
                return null;
            }
        });
        thread.getRunAction().go();
    }
}
