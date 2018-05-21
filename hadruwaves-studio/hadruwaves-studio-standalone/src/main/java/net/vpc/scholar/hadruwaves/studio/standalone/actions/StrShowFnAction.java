package net.vpc.scholar.hadruwaves.studio.standalone.actions;

import net.vpc.lib.pheromone.ariana.util.Resources;
import net.vpc.scholar.hadruwaves.studio.standalone.editors.MomProjectEditor;
import net.vpc.scholar.hadrumaths.plot.ComplexAsDouble;
import net.vpc.scholar.hadruwaves.WallBorders;
import net.vpc.scholar.hadruwaves.mom.ModeFunctions;
import net.vpc.scholar.hadruwaves.mom.project.common.RunAction;
import net.vpc.scholar.hadruwaves.mom.util.MomStrHelper;

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
                ModeFunctions base = jxy.getStructure().getModeFunctions();
                
                
                WallBorders b = base.getBorders();
                String desc="=>E(x=" + b.getExDescription()+",y="+b.getEyDescription()+") ; J(x="+b.getJxDescription()+",y="+b.getJxDescription()+")";
                plotter().title(getTitle()+" "+desc).domain(jxy.getStructure().getDomain()).plot(jxy.getStructure().getModeFunctions().arr());
                
//                FunctionsXYPlotPanel panel = new FunctionsXYPlotPanel(getTitle()+" "+desc, jxy.getStructure().getDomain(), ComplexAsDouble.ABS, base.fn());

//                etRenderer().getWindowManager().addWindow(getKey(),
//                getTitle(),
//                Resources.loadImageIcon("/net/vpc/research/tmwlab/resources/images/Graph.gif"),
//                panel);
                return null;
            }
        });
        thread.getRunAction().go();
    }
}
