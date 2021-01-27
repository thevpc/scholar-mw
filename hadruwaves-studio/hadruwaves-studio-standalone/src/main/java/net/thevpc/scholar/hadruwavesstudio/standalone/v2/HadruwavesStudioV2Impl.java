package net.thevpc.scholar.hadruwavesstudio.standalone.v2;

import net.thevpc.echo.*;
import net.thevpc.echo.swing.Applications;
import net.thevpc.echo.swing.actions.RedoAction;
import net.thevpc.echo.swing.actions.UndoAction;
import net.thevpc.echo.swing.core.swing.JFrameAppWindow;
import net.thevpc.common.i18n.I18nResourceBundle;
import net.thevpc.common.iconset.ResourcesIconSet;
import net.thevpc.common.msg.Message;
import net.thevpc.common.msg.StringMessage;
import net.thevpc.common.props.FileObject;
import net.thevpc.common.props.PropertyEvent;
import net.thevpc.common.props.PropertyListener;
import net.thevpc.common.props.WritablePList;
import net.thevpc.common.strings.StringUtils;
import net.thevpc.scholar.hadruwaves.SolverBuildResult;
import net.thevpc.scholar.hadruwaves.project.*;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwaves.solvers.HWSolver;
import net.thevpc.scholar.hadruwaves.solvers.HWSolverTemplate;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.actions.global.*;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.components.*;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.HWSUIPropsTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.cache.HWSCacheExplorerTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.console.HWSConsoleTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.components.HWProjectSourceFile;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.files.HWSFileExplorerTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.history.HWSProjectHistoryTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.library.HWSLibraryExplorerTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.locks.HWSLocksTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.logs.HWSLogTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.messages.HWSMessageTools;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.params.HWSProjectParametersTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.HWSProjectPropertiesTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.HWSProjectResultsTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.tasks.HWSTasksTool;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.view3d.HWS3DView;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.view3d.actions.ResetCameraAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.view3d.actions.ZoomInAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.view3d.actions.ZoomOutAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.HWAppIconResolver;
import net.thevpc.swings.plaf.UIPlafManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.thevpc.echo.swing.mydoggy.MyDoggyAppDockingWorkspace;
import net.thevpc.echo.swing.swingx.AppSwingxConfigurator;

public class HadruwavesStudioV2Impl implements HadruwavesStudio {

    private Application application;
    private HWSolutionProcessor processor = new HWSolutionProcessor();
    private AppEditorThemes editorThemes = new AppEditorThemes();
    private HWSMessageTools wsMessages;
    private HWSConsoleTool wsConsole;
    private HWSLogTool wsLog;
    private HWSLocksTool wsLocks;
    private HWSTasksTool wsTasks;
    private HWSProjectResultsTool wsResults;
    private HWSCacheExplorerTool wsCache;
    private HWSLibraryExplorerTool wsLibraries;
    private HWSFileExplorerTool wsFiles;
    private HWSProjectHistoryTool wsHistory;
    private HWSProjectParametersTool wsParameters;
    private HWSProjectPropertiesTool wsProperties;
    private HWSSolutionExplorerTool wsSolutionExplorer;
    private HWS3DView ws3DView;
    private ProjectBuildProducer projectBuildMessages = new ProjectBuildProducer(this);
    private AppSwingxConfigurator swingx = new AppSwingxConfigurator();
    //    private HWS2DView ws2DViewws2DView;
    private SourceEditorCharTableListener sourceEditorCharTableListener = new SourceEditorCharTableListener();

    public static void main(String[] args) {
        HadruwavesStudioV2Impl b = new HadruwavesStudioV2Impl();
        b.start();
//        TMWLabApplication.main(args);
    }

    @Override
    public Application app() {
        return application;
    }

    @Override
    public HWSolutionProcessor proc() {
        return processor;
    }

    @Override
    public HWS3DView get3DView() {
        return ws3DView;
    }

    @Override
    public void saveFileObjectAs2(FileObject o) {
        String oldFile = o.filePath().get();
        if (oldFile != null) {
            File f = new File(oldFile).getParentFile();
            if (f != null) {
                app().currentWorkingDirectory().set(f.getPath());
            }
        }
        if (o instanceof HWProject) {
            HWProject p = new DefaultHWProject();
            p.load(((HWProject) o).toTsonElement());
            o = p;
        }
        o.filePath().set(null);
        saveFileObject(o);
    }

    @Override
    public void saveFileObjectAs(FileObject fileObject) {
        JFileChooser jfc = createJFileChooser();
        String name = fileObject.name().get();
        String s = fileObject.fileTypeTitle();
        jfc.setDialogTitle("Save " + (s == null ? "" : (s + " ")) + name);
        String old = fileObject.filePath().get();
        File dir = null;
        String suffix = fileObject.defaultFileSuffix();
        if (StringUtils.isBlank(old)) {
            File parentFile = app().currentWorkingDirectory().get() == null ? null : new File(app().currentWorkingDirectory().get());
            if (parentFile == null) {
                parentFile = new File(System.getProperty("user.home"));
            }
            jfc.setCurrentDirectory(parentFile);
            jfc.setSelectedFile(new File(parentFile, name + "." + suffix));
        } else if (!StringUtils.isBlank(old)) {
            jfc.setSelectedFile(new File(old));
        }
        int d = jfc.showSaveDialog(appComponent());
        if (d == JFileChooser.APPROVE_OPTION) {
            File f = jfc.getSelectedFile();
            if (f == null) {
                throw new IllegalArgumentException("Save Cancelled");
            }
            if (!f.getName().endsWith("." + suffix)) //proc().openSolution(new HWSolution());
            {
                f = new File(f.getParentFile(), f.getName() + "." + suffix);
                fileObject.filePath().set(f.getPath());
            }
            app().currentWorkingDirectory().set(f.getParentFile().getPath());
            fileObject.filePath().set(f.getPath());
            fileObject.save();
            fileObject.modified().set(false);
            if (fileObject instanceof HWProject) {
                solutionExplorer().reload();
            }
            if (fileObject instanceof HWSolution) {
                solutionExplorer().reload();
            }
        }
    }

    @Override
    public void saveFileObject(FileObject fileObject) {
        String s = fileObject.filePath().get();
        if (s == null || s.length() == 0) {
            saveFileObjectAs(fileObject);
            return;
        }
        fileObject.save();
        if (fileObject instanceof HWProject) {
            solutionExplorer().reload();
        }
        if (fileObject instanceof HWSolution) {
            solutionExplorer().reload();
        }
    }

    @Override
    public void openSettings() {

    }

    @Override
    public void closeSolution() {

    }

    @Override
    public void open() {
        JFileChooser jfc = createJFileChooser();
        int d = jfc.showOpenDialog(appComponent());
        if (d == JFileChooser.APPROVE_OPTION) {
            File f = jfc.getSelectedFile();
            if (f.getName().endsWith(".hws.tson")) {
                closeSolution();
                HWSolution s = new DefaultHWSolution();
                s.filePath().set(f.getPath());
                s.load();
                proc().openSolution(s);
            } else if (f.getName().endsWith(".hwp.tson")) {
                HWProject s = new DefaultHWProject();
                s.filePath().set(f.getPath());
                s.load();
                //what if a solution folder is selected?

                proc().solution().get().add(s, "/");

            } else {
                throw new IllegalArgumentException("Unsupported file");
            }
        }
    }

    @Override
    public boolean confirmSaveCurrent() {
        HWSolution s = proc().activeSolutionValue();
        java.util.List<FileObject> saveMe = new ArrayList<>();
        if (s != null) {
            for (HWProject f : s.findModifiedProjects()) {
                saveMe.add(f);
            }
            if (s.modified().get()) {
                saveMe.add(s);
            }
        }
        if (saveMe.size() > 0) {
            int i = JOptionPane.showConfirmDialog(
                    appComponent(), "The Solution is modified. Do you wand to Save changes?", "Save?", JOptionPane.YES_NO_CANCEL_OPTION);
            switch (i) {
                case JOptionPane.YES_OPTION:
                    for (FileObject object : saveMe) {
                        saveFileObject(object);
                    }
                    return true;
                case JOptionPane.NO_OPTION:
                    return true;
                case JOptionPane.CANCEL_OPTION:
                    return false;
                default:
                    return false;
            }
        }
        return true;

    }

    public HWSSolutionExplorerTool solutionExplorer() {
        return wsSolutionExplorer;
    }

    public HWSProjectPropertiesTool props() {
        return wsProperties;
    }

    @Override
    public HWSProjectResultsTool results() {
        return wsResults;
    }

    @Override
    public Component appComponent() {
        return (Component) app().mainWindow().get().component();
    }

    public AppDockingWorkspace workspace() {
        return (AppDockingWorkspace) application.mainWindow().get().workspace().get();
    }


    @Override
    public void openSourceFile(HWProjectSourceFile s) {
        if (HWTextEditor.isSupportedFile(s.file)) {
            AppDockingWorkspace ws = workspace();
            AppContentWindow content = ws.getContent("SourceEditor");
            SourceFileEditor sfe = null;
            if (content == null) {
                sfe = new SourceFileEditor(this);
                content = ws.addContent("SourceEditor", s.file.getName(), null, sfe);
                content.closable().set(false);
            } else {
                sfe = (SourceFileEditor) content.component().get();
            }
            content.title().set(s.file.getName());
            content.icon().set(app().iconSet().iconIdForFile(s.file,false,false));
            sfe.loadFile(s);
            content.active().set(true);
        } else {
            JOptionPane.showMessageDialog(appComponent(), "Unsupported file type");
        }
    }

    @Override
    public void setBuildResult(SolverBuildResult buildResult) {
        buildMessages().setMessages(buildResult.getMessages().stream().map(x -> new StringMessage(x.getLevel(), x.getMessage()))
                .collect(Collectors.toList())
        );
        app().messages().update();
    }

    @Override
    public HWSolver buildSolver(HWConfigurationRun configuration) {
        HWSolverTemplate solverTemplate = configuration.solver().get();
        HWSolver solver = solverTemplate.eval(configuration, proc().getTaskMonitorManager());
        setBuildResult(solver.buildResult());
        solver.buildResult().requireNoError();
        return solver;
    }

    private Application start() {
//        ApplicationMenu
        proc().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                if (event.getProperty().name().equals("activeProject")) {
                    application.messages().update();
                }
            }
        });
        proc().demoSolution();

        final Application app = Applications.Apps.Default();
        UIPlafManager.INSTANCE.apply("FlatLight");
        app.iconSet().resolver().set(new HWAppIconResolver());
        app.i18n().bundles().add(new I18nResourceBundle("net.thevpc.scholar.hadruwavesstudio.standalone.v2.messages.HadruwavesStudio"));
        application = app;
        app.builder().mainWindowBuilder().get().workspaceFactory().set(MyDoggyAppDockingWorkspace.factory());

        app.start();

        app.iconSets().add(new ResourcesIconSet("Material-Neron-16", 16, 16, "/net/thevpc/scholar/hadruwavesstudio/standalone/v2/images/material-mono", "png", getClass().getClassLoader()));
        app.iconSets().add(new ResourcesIconSet("Material-Neron-32", 32, 32, "/net/thevpc/scholar/hadruwavesstudio/standalone/v2/images/material-mono", "png", getClass().getClassLoader()));
        app.iconSets().add(new ResourcesIconSet("Icon8-Simple-16", 16, 16, "/net/thevpc/scholar/hadruwavesstudio/standalone/v2/images/icons8-color", "png", getClass().getClassLoader()));
        app.iconSets().add(new ResourcesIconSet("Icon8-Simple-32", 32, 32, "/net/thevpc/scholar/hadruwavesstudio/standalone/v2/images/icons8-color", "png", getClass().getClassLoader()));
        app.iconSet().id().set("Icon8-Simple-16");
//        for (Class cls : ClassPathUtils.resolveContextClasses()) {
//
//        }
        prepareWorkspace();
        prepareMenu();
        app.mainWindow().get().title().set("Hadruwaves Studio - v2.0");
        app.iconSet().icon("App").bind(app.mainWindow().get().icon());
        SwingUtilities.invokeLater(() -> ((JFrameAppWindow) app.mainWindow().get()).getFrame().setSize(1024, 800));
        swingx.confirmExit().set(() -> confirmSaveCurrent());
        swingx.configure(app);
        app.state().listeners().add(e -> {
            AppState a = e.getNewValue();
            if (a == AppState.CLOSED) {
                System.out.println("closed");
                System.exit(0);
            }
        });
        app.messages().producers().add(projectBuildMessages);
        return app;
    }

    public ProjectBuildProducer buildMessages() {
        return projectBuildMessages;
    }

    protected void prepareMenu_File() {
        AppTools tools = application.tools();
        tools.addFolder("/mainWindow/menuBar/File");
        tools.addAction(new SaveAction(application, this), "/mainWindow/menuBar/File/Save"/*, "/mainWindow/toolBar/Default/Save"*/);
        tools.addAction(new SaveAllAction(application, this), "/mainWindow/menuBar/File/SaveAll", "/mainWindow/toolBar/Default/SaveAll");
        tools.addAction(new SaveAsAction(application, this), "/mainWindow/menuBar/File/SaveAs"/*, "/mainWindow/toolBar/Default/SaveAs"*/);
        tools.addAction(new NewSolutionAction(application, this), "/mainWindow/menuBar/File/New/NewSolution", "/mainWindow/toolBar/Default/NewSolution");
        tools.addAction(new NewProjectAction(application, this), "/mainWindow/menuBar/File/New/NewProject"/*, "/mainWindow/toolBar/Default/NewProject"*/);
        tools.addAction(new OpenAction(application, this), "/mainWindow/menuBar/File/Open", "/mainWindow/toolBar/Default/Open");

        tools.addFolder("/mainWindow/menuBar/File/LoadRecent");

        tools.addAction("/mainWindow/menuBar/File/LoadRecent/Solution1");
        tools.addAction("/mainWindow/menuBar/File/LoadRecent/Solution2");
        tools.addSeparator("/mainWindow/menuBar/File/LoadRecent/Separator1");
        tools.addAction("/mainWindow/menuBar/File/LoadRecent/Project1");
        tools.addAction("/mainWindow/menuBar/File/LoadRecent/Project2");
        tools.addSeparator("/mainWindow/menuBar/File/LoadRecent/Separator1");
        tools.addAction("/mainWindow/menuBar/File/LoadRecent/Clear");

        tools.addSeparator("/mainWindow/menuBar/File/Separator1");
//        tools.addAction("/mainWindow/menuBar/File/ClearProjectCache");
//        tools.addAction("/mainWindow/menuBar/File/ClearAllCache");
        tools.addSeparator("/mainWindow/menuBar/File/Separator2");
        tools.addAction(new SettingsAction(application, this), "/mainWindow/menuBar/File/Settings", "/mainWindow/toolBar/Default/Settings");
        tools.addSeparator("/mainWindow/menuBar/File/Separator3");
        tools.addAction(new ExitAction(application, this), "/mainWindow/menuBar/File/Exit");


    }

    protected void prepareMenu_Edit() {
        AppTools tools = application.tools();
        tools.addFolder("/mainWindow/menuBar/Edit");
        tools.addSeparator("/mainWindow/toolBar/Default/Separator1");
        tools.addAction(new CopyAction(application), "/mainWindow/menuBar/Edit/Copy", "/mainWindow/toolBar/Default/Copy");
        tools.addAction(new CutAction(application), "/mainWindow/menuBar/Edit/Cut", "/mainWindow/toolBar/Default/Cut");
        tools.addAction(new PasteAction(application), "/mainWindow/menuBar/Edit/Paste", "/mainWindow/toolBar/Default/Paste");

        tools.addAction(new UndoAction(application), "/mainWindow/menuBar/Edit/Undo", "/mainWindow/toolBar/Default/Undo");
        tools.addAction(new RedoAction(application), "/mainWindow/menuBar/Edit/Redo", "/mainWindow/toolBar/Default/Redo");

        tools.addSeparator("/mainWindow/toolBar/Default/Separator2");
        tools.addTool(AppToolComponent.of(new CustomAppTool("GREEK"), "/mainWindow/toolBar/Default/GREEK", 0,
                context -> {
                    return CharactersTableComponent.createComponents(sourceEditorCharTableListener,
                            CharactersTableComponent.Family.GREEK_LOWER, CharactersTableComponent.Family.GREEK_UPPER)
                            .get(0);
                }
        ));
        tools.addTool(AppToolComponent.of(new CustomAppTool("OPERATORS"), "/mainWindow/toolBar/Default/OPERATORS", 0,
                context -> {
                    return CharactersTableComponent.createComponents(sourceEditorCharTableListener,
                            CharactersTableComponent.Family.OPERATORS)
                            .get(0);
                }
        ));
        tools.addTool(AppToolComponent.of(new CustomAppTool("SYMBOLS"), "/mainWindow/toolBar/Default/SYMBOLS", 0,
                context -> {
                    return CharactersTableComponent.createComponents(sourceEditorCharTableListener,
                            CharactersTableComponent.Family.SYMBOLS)
                            .get(0);
                }
        ));
        tools.addTool(AppToolComponent.of(new CustomAppTool("CONSTANTS"), "/mainWindow/toolBar/Default/CONSTANTS", 0,
                context -> {
                    return CharactersTableComponent.createComponents(sourceEditorCharTableListener,
                            CharactersTableComponent.Family.CONSTANTS)
                            .get(0);
                }
        ));
        tools.addTool(AppToolComponent.of(new CustomAppTool("TRIGO"), "/mainWindow/toolBar/Default/TRIGO", 0,
                context -> {
                    return CharactersTableComponent.createComponents(sourceEditorCharTableListener,
                            CharactersTableComponent.Family.TRIGO)
                            .get(0);
                }
        ));

        //        tools.addAction("/mainWindow/menuBar/Edit/Macros/StartMacroRecording");
//        tools.addAction("/mainWindow/menuBar/Edit/Macros/StopMacroRecording");
//        tools.addAction("/mainWindow/menuBar/Edit/Macros/RunMacro");
    }

    @Override
    public AppEditorThemes editorThemes() {
        return editorThemes;
    }

    protected void prepareMenu_View() {
        Applications.Helper.addViewToolActions(application);
        AppTools tools = application.tools();
        Applications.Helper.addViewPlafActions(application);
        tools.addFolder("/mainWindow/menuBar/View/Editor Themes");
        for (HWTextEditorTheme theme : editorThemes.editorThemes().values()) {
            tools.addAction(new EditorThemeAction(this, theme.getId()),"/mainWindow/menuBar/View/Editor Themes/" + theme.getName());
        }
        tools.addSeparator("/mainWindow/menuBar/View/Editor Themes/Separator1");
        tools.addCheck("MaximizeLaF", editorThemes.usePlaf(), "/mainWindow/menuBar/View/Editor Themes/Maximize L&F");

        Applications.Helper.addViewIconActions(application);
        Applications.Helper.addViewAppearanceActions(application);

        tools.addSeparator("/mainWindow/toolBar/Default/Separator3");
        tools.addAction(new ZoomInAction(application, "ZoomIn", this), "/mainWindow/menuBar/View/3DView/ZoomIn", "/mainWindow/toolBar/Default/ZoomIn");
        tools.addAction(new ZoomOutAction(application, "ZoomOut", this), "/mainWindow/menuBar/View/3DView/ZoomIn", "/mainWindow/toolBar/Default/ZoomOut");
        tools.addAction(new ResetCameraAction(application, "ResetCamera", this), "/mainWindow/menuBar/View/3DView/ResetCamera", "/mainWindow/toolBar/Default/ResetCamera");
        tools.addCheck("BoxVisible", ws3DView.getPreferences().boxVisible(), "/mainWindow/menuBar/View/3DView/AxisVisible", "/mainWindow/toolBar/Default/BoxVisible");
        tools.addCheck("AxisVisible", ws3DView.getPreferences().axisVisible(), "/mainWindow/menuBar/View/3DView/AxisVisible", "/mainWindow/toolBar/Default/AxisVisible");
        tools.addCheck("GridXYVisible", ws3DView.getPreferences().gridXYVisible(), "/mainWindow/menuBar/View/3DView/GridXYVisible", "/mainWindow/toolBar/Default/GridXYVisible");
        tools.addCheck("PerspectiveEnabled", ws3DView.getPreferences().perspectiveEnabled(), "/mainWindow/menuBar/View/3DView/PerspectiveEnabled", "/mainWindow/toolBar/Default/PerspectiveEnabled");
        tools.addCheck("StereoscopeEnabled", ws3DView.getPreferences().steroscopyEnabled(), "/mainWindow/menuBar/View/3DView/StereoscopeEnabled", "/mainWindow/toolBar/Default/StereoscopeEnabled");
    }

    protected void prepareMenu() {
        prepareMenu_File();
        prepareMenu_Edit();
        prepareMenu_View();

    }

    private void prepareWorkspace() {
        AppDockingWorkspace ws = workspace();
        ws.addTool("Solution", "", null, wsSolutionExplorer = new HWSSolutionExplorerTool(this), AppToolWindowAnchor.LEFT);
        ws.addTool("Parameters", "", null, wsParameters = new HWSProjectParametersTool(this), AppToolWindowAnchor.BOTTOM);
        ws.addTool("UI Dev", "", null, new HWSUIPropsTool(this), AppToolWindowAnchor.BOTTOM);
        ws.addTool("Properties", "", null, wsProperties = new HWSProjectPropertiesTool(this), AppToolWindowAnchor.RIGHT);
        ws.addTool("History", "", null, wsHistory = new HWSProjectHistoryTool(this), AppToolWindowAnchor.LEFT);
        ws.addTool("Files", "", null, wsFiles = new HWSFileExplorerTool(this), AppToolWindowAnchor.LEFT);
        ws.addTool("Library", "", null, wsLibraries = new HWSLibraryExplorerTool(this), AppToolWindowAnchor.LEFT);
        ws.addTool("Cache", "", null, wsCache = new HWSCacheExplorerTool(this), AppToolWindowAnchor.RIGHT);
        ws.addTool("Results", "", null, wsResults = new HWSProjectResultsTool(this), AppToolWindowAnchor.RIGHT);
        ws.addTool("Tasks", "", null, wsTasks = new HWSTasksTool(this), AppToolWindowAnchor.BOTTOM);
        ws.addTool("Locks", "", null, wsLocks = new HWSLocksTool(this), AppToolWindowAnchor.BOTTOM);
        ws.addTool("Log", "", null, wsLog = new HWSLogTool(this), AppToolWindowAnchor.BOTTOM);
        ws.addTool("Console", "", null, wsConsole = new HWSConsoleTool(this), AppToolWindowAnchor.BOTTOM);
        ws.addTool("Messages", "", null, wsMessages = new HWSMessageTools(this), AppToolWindowAnchor.BOTTOM);
        ws.addContent("3DView", "3D View", null, ws3DView = new HWS3DView(this));
        ws.addContent("JTextArea", "JTextArea", null, new JScrollPane(new JTextArea()));
    }

    public static class ProjectBuildProducer implements AppMessageProducer {

        private List<Message> messages = new ArrayList<Message>();
        private HadruwavesStudioV2Impl studio;

        public ProjectBuildProducer(HadruwavesStudioV2Impl studio) {
            this.studio = studio;
        }

        @Override
        public void produceMessages(Application application, WritablePList<Message> messages) {
            for (Message message : this.messages) {
                messages.add(message);
            }
        }

        public void setMessages(List<Message> other) {
            messages.clear();
            if (other != null) {
                for (Message m : other) {
                    if (m != null) {
                        messages.add(m);
                    }
                }
            }
            studio.app().messages().update();
        }
    }

    private JFileChooser createJFileChooser(){
        JFileChooser jfc=new JFileChooser();
        jfc.setFileView(new HWFileView(this));
        return jfc;
    }

    private class SourceEditorCharTableListener implements CharTableListener {
        @Override
        public void onChar(CharEvent event) {
            AppDockingWorkspace ws = workspace();
            AppContentWindow content = ws.getContent("SourceEditor");
            if (content != null) {
                if (content.active().get()) {
                    SourceFileEditor sfe = (SourceFileEditor) content.component().get();
                    if (sfe != null) {
                        sfe.getTextEditor().insertOrReplaceSelection(event.getCharCommand().getText());
                    }
                }
            }
        }
    }
}
