package net.vpc.scholar.hadruwaves.project;

import net.vpc.common.mon.DefaultTaskMonitorManager;
import net.vpc.common.mon.TaskMonitorManager;
import net.vpc.common.prpbind.*;
import net.vpc.common.prpbind.impl.PropertyListenersImpl;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadruwaves.Material;
import net.vpc.scholar.hadruwaves.mom.BoxSpaceFactory;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.SourceFactory;
import net.vpc.scholar.hadruwaves.mom.TestFunctionsFactory;
import net.vpc.scholar.hadruwaves.project.configuration.HWSConfigurationRun;
import net.vpc.scholar.hadruwaves.project.parameter.HWSParameterValue;
import net.vpc.scholar.hadruwaves.str.MicroStripCalc;

import static net.vpc.scholar.hadrumaths.Maths.*;

public class HWSolutionProcessor implements WithListeners{
    private WritablePList<HWFile> recentFiles = Props.of("recentFiles").listOf(HWFile.class);
    private WritablePValue<HWSolution> solution = Props.of("solution").valueOf(HWSolution.class, null);
    private TaskMonitorManager taskMonitorManager = new DefaultTaskMonitorManager();
    private PropertyListenersImpl listeners = new PropertyListenersImpl(this);

//    private WritablePValue<HWSConfigurationRun> conf = Props.of("configuration").valueOf(HWSConfigurationRun.class, null);

//    private PropertyListener projectUpdatedListener = new PropertyListener() {
//        @Override
//        public void propertyUpdated(PropertyEvent event) {
//            projectUpdated();
//        }
//    };

    public HWSolutionProcessor() {
        listeners.addDelegate(recentFiles);
        listeners.addDelegate(solution);

//        solution.listeners().add(new PropertyListener() {
//            @Override
//            public void propertyUpdated(PropertyEvent event) {
//                HWSolution o = event.getOldValue();
//                HWSolution n = event.getNewValue();
//                if(o!=null){
//                    o.listeners().removeIf(null);
//                }
//                if(n!=null){
//                    o.listeners().add(new PropertyListener() {
//                        @Override
//                        public void propertyUpdated(PropertyEvent event) {
//                            listeners.addDelegate(o);
//                        }
//                    });
//                }
//            }
//        });
//        project.listeners().add(new PropertyListener() {
//            @Override
//            public void propertyUpdated(PropertyEvent event) {
//                HWProject oldValue = event.getOldValue();
//                HWProject newValue = event.getNewValue();
//                if (oldValue != newValue) {
//                    if (oldValue != null) {
//                        oldValue.listeners().remove(projectUpdatedListener);
//                    }
//                    if (newValue != null) {
//                        newValue.listeners().add(projectUpdatedListener);
//                    }
//                }
//                conf.set(null);
//                projectUpdated();
//            }
//        });

    }

    @Override
    public PropertyListeners listeners() {
        return listeners;
    }

    protected void projectUpdated() {

    }

    protected void solutionUpdated() {

    }

    public TaskMonitorManager getTaskMonitorManager() {
        return taskMonitorManager;
    }

    public void reloadRecentFile() {

    }

    public void openFile(HWFile file) {

    }

    public void openSolution(HWSolution solution) {
        this.solution.set(solution);
    }

    public void openProject(HWProject project, String projectPath) {
        solution.get().add(project, projectPath);
    }

    public void closeSolution() {

    }

    public void newSolution() {
        HWSolution s = new HWSolution();
        s.name().set("New Solution");
        openSolution(s);
    }

    public void demoSolution() {
        HWSolution s = new HWSolution();
        s.name().set("Demo Solution");
        s.add(createDemoProject("Project 1"), "/main");
        s.add(createDemoProject("Project 2"), "/main");
        s.add(createDemoProject("Project 3"), "/test");
        s.add(createDemoProject("Project 4"), "/test");
        openSolution(s);
        s.name().set("New Demo Name");
        WritablePNamedNode<HWSolutionElement> e = s.findElement("/main/Project 1");
        e.get().name().set("Project 11");
    }

    private HWProject createDemoProject(String name) {
        HWProject p = new HWProject();
        p.name().set(name);

        p.configurations().add(new HWSConfigurationRun("Configurations " + name), "/");
        p.configurations().add(new HWSConfigurationRun("Configuration 1"), "/Default");
        p.configurations().add(new HWSConfigurationRun("Configuration 2"), "/Default");

        p.parameters().add(new HWSParameterValue("a",  UnitTypes.Dimension.mm), "/Geometry");
        p.parameters().add(new HWSParameterValue("b",  UnitTypes.Dimension.mm), "/Geometry");
        p.parameters().add(new HWSParameterValue("w",  UnitTypes.Dimension.mm), "/Geometry");
        p.parameters().add(new HWSParameterValue("h",  UnitTypes.Dimension.mm), "/Geometry");
        p.parameters().add(new HWSParameterValue("f",  UnitTypes.Frequency.GHz), "/Other");
        p.parameters().add(new HWSParameterValue("d",  UnitTypes.Number.Double), "/Other");
        p.parameters().add(new HWSParameterValue("i",  UnitTypes.Number.Int), "/Other");


        double f = 3 * GHZ;
        double lambda = 3 * GHZ;
        MicroStripCalc c = MicroStripCalc.calculate(f, 2.2, 1 * MM);
        double fl = lambda / 10;
        double fw = lambda / 40;
        double sl = lambda / 40;
        double sw = lambda / 40;
        double dw = fw + c.width + lambda;
        double dl = fl + c.length + lambda;
        MomStructure s = MomStructure.EEEE(Domain.ofBounds(0, dl, -dw / 2, dw / 2), c.freq, 1000,
                BoxSpaceFactory.shortCircuit(Material.substrate(c.epsr), c.height),
                BoxSpaceFactory.matchedLoad(Material.VACUUM)
        );
        s.addSource(SourceFactory.createPlanarSource(1, complex(50), Axis.X, Domain.ofBounds(0, sl, -sw / 2, sw / 2)));
        s.setTestFunctions(
                TestFunctionsFactory
                        .addGeometry(Domain.ofBounds(0, fl, -fw / 2, fw / 2))
                        .addGeometry(Domain.ofBounds(fl, fl + c.length, -c.width, c.width / 2))
                        .buildSicoCoco());
        p.scene().set(s.createScene());
        return p;
    }

    public PList<HWFile> getRecentFile() {
        return recentFiles.readOnly();
    }

    public WritablePList<HWFile> getRecentFiles() {
        return recentFiles;
    }

    public PValue<HWSolution> solution() {
        return solution;
    }

//    public WritablePValue<HWProject> project() {
//        return project;
//    }
//
//    public WritablePValue<HWSConfigurationRun> conf() {
//        return conf;
//    }

}
