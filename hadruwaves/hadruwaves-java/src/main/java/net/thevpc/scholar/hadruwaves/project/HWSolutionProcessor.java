package net.thevpc.scholar.hadruwaves.project;

import net.thevpc.scholar.hadrumaths.units.LengthUnit;
import net.thevpc.scholar.hadrumaths.units.FrequencyUnit;
import net.thevpc.scholar.hadrumaths.units.UnitType;
import net.thevpc.common.mon.DefaultTaskMonitorManager;
import net.thevpc.common.mon.TaskMonitorManager;
import net.thevpc.common.props.*;
import net.thevpc.common.props.impl.PropertyListenersImpl;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadruwaves.Material;
import net.thevpc.scholar.hadruwaves.mom.*;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwaves.str.MicroStripCalc;

import static net.thevpc.scholar.hadrumaths.Maths.*;
import net.thevpc.scholar.hadruwaves.project.templates.MicrostripProjectTemplate;

public class HWSolutionProcessor implements WithListeners {

    private WritablePList<HWFile> recentFiles = Props.of("recentFiles").listOf(HWFile.class);
    private WritablePValue<HWSolution> solution = Props.of("solution").valueOf(HWSolution.class, null);
    private TaskMonitorManager taskMonitorManager = new DefaultTaskMonitorManager();
    private PropertyListenersImpl listeners = new PropertyListenersImpl(this);
    private WritablePValue<HWProject> selectedProject = Props.of("selectedProject").valueOf(HWProject.class, null);
    private WritablePValue<HWConfigurationRun> selectedConfiguration = Props.of("selectedConfiguration").valueOf(HWConfigurationRun.class, null);

    public HWSolutionProcessor() {
        listeners.addDelegate(recentFiles);
        listeners.addDelegate(solution);
        solution.listeners().add(new SolutionTracker(this));
    }

    public WritablePValue<HWProject> selectedProject() {
        return selectedProject;
    }

    public WritablePValue<HWConfigurationRun> selectedConfiguration() {
        return selectedConfiguration;
    }

    public HWSolution activeSolutionValue() {
        return solution().get();
    }

    public HWConfigurationRun activeConfigValue() {
        HWProject s = activeProjectValue();
        return s == null ? null : s.configurations().activeConfiguration().get();
    }

    public HWProject activeProjectValue() {
        HWSolution s = activeSolutionValue();
        return s == null ? null : s.activeProject().get();
    }

    @Override
    public PropertyListeners listeners() {
        return listeners;
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

    public void newSolution() {
        HWSolution s = new DefaultHWSolution();
        s.name().set("New Solution");
        openSolution(s);
    }

    public void demoSolution() {
        HWSolution s = new DefaultHWSolution();
        s.name().set("Demo Solution");
        s.add(createDemoProject("Project 1"), "/main");
//        s.add(createDemoProject("Project 2"), "/test");
        openSolution(s);
        s.name().set("New Demo Name");
        HWSolutionElement e = s.findElement("/main/Project 1");
        e.name().set("Project 11");
    }

    private HWProject createDemoProject(String name) {
        MicrostripProjectTemplate t=new MicrostripProjectTemplate();
        HWProject p = t.createProject();
        p.name().set(name);
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

    private static class SolutionTracker extends SourcePropertyListener {

        public SolutionTracker(Object o) {
            super(o);
        }

        @Override
        public void propertyUpdated(PropertyEvent event) {
            System.out.println("Hello " + event.getAction() + " :: " + event.getPath());
        }
    }

}
