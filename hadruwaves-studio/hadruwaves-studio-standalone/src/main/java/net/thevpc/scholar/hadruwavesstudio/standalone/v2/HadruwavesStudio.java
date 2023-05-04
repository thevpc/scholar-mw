package net.thevpc.scholar.hadruwavesstudio.standalone.v2;

import net.thevpc.echo.api.components.AppDock;
import net.thevpc.echo.iconset.IconSetAware;
import net.thevpc.scholar.hadruwaves.SolverBuildResult;
import net.thevpc.common.props.FileObject;
import net.thevpc.scholar.hadruwaves.project.HWSolutionProcessor;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwaves.solvers.HWSolver;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.components.AppEditorThemes;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.components.HWProjectSourceFile;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.view3d.HWS3DView;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.HWSProjectPropertiesTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.HWSProjectResultsTool;

import java.awt.*;
import net.thevpc.echo.Application;

public interface HadruwavesStudio extends IconSetAware {

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

    AppDock content();

    void openSourceFile(HWProjectSourceFile s);

    void setBuildResult(SolverBuildResult buildResult);
    
    HWSolver buildSolver(HWConfigurationRun configuration);

    AppEditorThemes editorThemes();
}
