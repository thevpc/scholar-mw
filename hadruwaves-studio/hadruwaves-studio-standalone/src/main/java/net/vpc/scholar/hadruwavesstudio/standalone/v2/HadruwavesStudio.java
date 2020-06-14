package net.vpc.scholar.hadruwavesstudio.standalone.v2;

import net.vpc.common.app.AppDockingWorkspace;
import net.vpc.common.app.Application;
import net.vpc.scholar.hadruwaves.SolverBuildResult;
import net.vpc.common.props.FileObject;
import net.vpc.scholar.hadruwaves.project.HWProject;
import net.vpc.scholar.hadruwaves.project.HWSolutionProcessor;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwaves.solvers.HWSolver;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.components.AppEditorThemes;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.components.HWProjectSourceFile;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.view3d.HWS3DView;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.props.HWSProjectPropertiesTool;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.HWSProjectResultsTool;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public interface HadruwavesStudio {

    Application app();

    HWSolutionProcessor proc();

    HWS3DView get3DView();

    void saveFileObjectAs2(FileObject fileObj);

    void saveFileObjectAs(FileObject fileObj);

    void saveFileObject(FileObject solution);

    void openSettings();

    void closeSolution();

    void open();

    boolean confirmSaveCurrent();

    HWSSolutionExplorerTool solutionExplorer();

    HWSProjectPropertiesTool props();

    HWSProjectResultsTool results();

    Component appComponent();

    AppDockingWorkspace workspace();

    void openSourceFile(HWProjectSourceFile s);

    void setBuildResult(SolverBuildResult buildResult);
    
    HWSolver buildSolver(HWConfigurationRun configuration);

    AppEditorThemes editorThemes();
}
