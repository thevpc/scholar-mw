package net.vpc.scholar.hadruwaves.mom.console.yaxis;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.PlotAxisCustom;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.util.EnhancedProgressMonitor;
import net.vpc.scholar.hadrumaths.util.MonitoredAction;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpAdaptiveMesh;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleAction;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;
import net.vpc.scholar.hadruwaves.console.PolygonPlotConsoleAction;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

import java.util.Iterator;
import java.util.ArrayList;

public class PlotStructure extends PlotAxisCustom implements Cloneable {
    public PlotStructure() {
        super("Structure");
    }
    public PlotStructure(YType type) {
        super("Structure", type);
    }

    public Iterator<ConsoleAction> createConsoleActionIterator(ConsoleActionParams p){
        return Maths.invokeMonitoredAction(this, getName(), new MonitoredAction<Iterator<ConsoleAction>>() {
            @Override
            public Iterator<ConsoleAction> process(EnhancedProgressMonitor monitor, String messagePrefix) throws Exception {
                ArrayList<ConsoleAction> all=new ArrayList<ConsoleAction>();
                MomStructure str = (MomStructure) p.getStructure();
                if(containsType(YType.REFERENCE) && str !=null){
                    String st = getName(YType.REFERENCE);
                    if (st.length() > 0 && p.getSerieTitle().toString().length()>0) {
                        st += " : ";
                    }
                    st += p.getSerieTitle();
                    net.vpc.scholar.hadruwaves.mom.TestFunctions gp = str.getTestFunctions();
                    if(gp instanceof GpAdaptiveMesh){
                        GpAdaptiveMesh gpa=(GpAdaptiveMesh) gp;
                        all.add(new PolygonPlotConsoleAction(st+"["+ str.getCircuitType()+"]", gpa.getPolygons(str.getCircuitType()).clone(),gpa.getMeshAlgo().clone(), gpa.getPattern(),str.getDomain(), p.getPreferredPath()));
//                all.add(new PolygonPlotConsoleAction(st+"[IF_PARALLEL]", gpa.getPolygons(SchemaType.PARALLEL).clone(),gpa.getMeshAlgo().clone(),preferredPath));
//            }else if(gp instanceof GpAdaptativeMultiMesh){
//                GpAdaptativeMultiMesh gpa=(GpAdaptativeMultiMesh) gp;
//                all.add(new PolygonPlotConsoleAction(st+"["+p.getStructure().getCircuitType()+"]", gpa.getPolygons(p.getStructure().getCircuitType()).clone(),gpa.getMeshAlgo().clone(), gpa.getPattern(), p.getPreferredPath()));
                    }
                }
                MomStructure str2 = (MomStructure) p.getStructure2();
                if(containsType(YType.MODELED) && str2 !=null){
                    String st = getName(YType.REFERENCE);
                    if (st.length() > 0 && p.getSerieTitle().toString().length()>0) {
                        st += " : ";
                    }
                    st += p.getSerieTitle();
                    st += "-[Model]";
                    net.vpc.scholar.hadruwaves.mom.TestFunctions gp = str2.getTestFunctions();
                    if(gp instanceof GpAdaptiveMesh){
                        GpAdaptiveMesh gpa=(GpAdaptiveMesh) gp;
                        all.add(new PolygonPlotConsoleAction(
                                st+"["+ str2.getCircuitType()+"]",
                                gpa.getPolygons(str2.getCircuitType()).clone(),
                                gpa.getMeshAlgo().clone(),
                                gpa.getPattern(),
                                str2.getDomain(),
                                p.getPreferredPath())
                        );
//                all.add(new PolygonPlotConsoleAction(st+"[IF_PARALLEL]", gpa.getPolygons(SchemaType.PARALLEL).clone(),gpa.getMeshAlgo().clone(),preferredPath));
                    }
                }
                return all.iterator();
            }
        });
    }

}
