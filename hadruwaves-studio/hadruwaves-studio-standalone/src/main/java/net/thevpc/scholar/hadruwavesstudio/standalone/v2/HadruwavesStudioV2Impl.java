package net.thevpc.scholar.hadruwavesstudio.standalone.v2;

import net.thevpc.common.i18n.I18nResourceBundle;
import net.thevpc.common.msg.Message;
import net.thevpc.common.msg.StringMessage;
import net.thevpc.common.props.*;
import net.thevpc.common.strings.StringUtils;
import net.thevpc.echo.*;
import net.thevpc.common.props.Path;
import net.thevpc.echo.ScrollPane;
import net.thevpc.echo.TextArea;
import net.thevpc.echo.api.AppMessageProducer;
import net.thevpc.echo.api.components.AppComponent;
import net.thevpc.echo.api.components.AppDock;
import net.thevpc.echo.api.components.AppWindow;
import net.thevpc.echo.constraints.Anchor;
import net.thevpc.echo.impl.Applications;
import net.thevpc.echo.api.AppContainerChildren;
import net.thevpc.echo.CheckBox;
import net.thevpc.echo.impl.DefaultApplication;
import net.thevpc.echo.swing.peers.SwingFramePeer;
import net.thevpc.echo.swing.swingx.AppSwingxConfigurator;
import net.thevpc.scholar.hadruwaves.SolverBuildResult;
import net.thevpc.scholar.hadruwaves.project.*;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwaves.solvers.HWSolver;
import net.thevpc.scholar.hadruwaves.solvers.HWSolverTemplate;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.actions.global.*;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.components.*;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.extra.RedoAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.extra.UndoAction;
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
import net.thevpc.swing.plaf.UIPlafManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HadruwavesStudioV2Impl implements HadruwavesStudio {

    private Application application;
    private HWSolutionProcessor processor = new HWSolutionProcessor("processor");
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
        return (Component) app().mainFrame().get().peer().toolkitComponent();
    }

    public AppDock content() {
        return (AppDock) application.mainFrame().get().content().get();
    }

    @Override
    public void openSourceFile(HWProjectSourceFile s) {
        if (HWTextEditor.isSupportedFile(s.file)) {
            AppDock ws = content();
            AppWindow content = (AppWindow) ws.children().get("SourceEditor");
            SourceFileEditor sfe = null;
            if (content == null) {
                sfe = new SourceFileEditor(this);
                content = ws.addWindow("SourceEditor", application.toolkit().createComponent(sfe), Anchor.CENTER);
                content.title().set(s.file.getName());
                content.closable().set(false);
            } else {
                sfe = (SourceFileEditor) content.component().get();
            }
            content.title().set(s.file.getName());
            content.icon().set(app().iconSets().iconForFile(s.file, false, false).get());
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

    @Override
    public AppEditorThemes editorThemes() {
        return editorThemes;
    }

    private Application start() {
//        ApplicationMenu
        proc().onChange(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                if (event.property().propertyName().equals("activeProject")) {
                    application.messages().update();
                }
            }
        });
        proc().demoSolution();

        final Application app = new DefaultApplication();
        UIPlafManager.INSTANCE.apply("FlatLight");
        app.iconSets().resolver().set(new HWAppIconResolver());
        app.i18n().bundles().add(new I18nResourceBundle("net.thevpc.scholar.hadruwavesstudio.standalone.v2.messages.HadruwavesStudio"));
        application = app;

        app.start();

        app.iconSets().add().name("Material-Neron").path("/net/thevpc/scholar/hadruwavesstudio/standalone/v2/images/material-mono").build();
        app.iconSets().add().name("Material-Neron").path("/net/thevpc/scholar/hadruwavesstudio/standalone/v2/images/material-mono").build();
        app.iconSets().add().name("Icon8-Simple").path("/net/thevpc/scholar/hadruwavesstudio/standalone/v2/images/icons8-color").build();
        app.iconSets().add().name("Icon8-Simple").path("/net/thevpc/scholar/hadruwavesstudio/standalone/v2/images/icons8-color").build();
        app.iconSets().id().set("Icon8-Simple-16");
//        for (Class cls : ClassPathUtils.resolveContextClasses()) {
//
//        }
        prepareWorkspace();
        prepareMenu();
        app.mainFrame().get().title().set("Hadruwaves Studio - v2.0");
        app.iconSets().icon("App").bindTarget(app.mainFrame().get().icon());
        SwingUtilities.invokeLater(() -> ((SwingFramePeer) app.mainFrame().get()).getFrame().setSize(1024, 800));
        swingx.confirmExit().set(() -> confirmSaveCurrent());
        swingx.configure(app);
        app.state().onChange(e -> {
            AppState a = e.newValue();
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
        AppContainerChildren<AppComponent> tools = application.components();
        tools.addFolder("/mainFrame/menuBar/File");
        tools.addAction().bind(new SaveAction(application, this)).path("/mainFrame/menuBar/File/Save"/*, "/mainFrame/toolBar/Default/Save"*/).tool();
        tools.addAction().bind(new SaveAllAction(application, this)).path("/mainFrame/menuBar/File/SaveAll", "/mainFrame/toolBar/Default/SaveAll").tool();
        tools.addAction().bind(new SaveAsAction(application, this)).path("/mainFrame/menuBar/File/SaveAs"/*, "/mainFrame/toolBar/Default/SaveAs"*/).tool();
        tools.addAction().bind(new NewSolutionAction(application, this)).path("/mainFrame/menuBar/File/New/NewSolution", "/mainFrame/toolBar/Default/NewSolution").tool();
        tools.addAction().bind(new NewProjectAction(application, this)).path("/mainFrame/menuBar/File/New/NewProject"/*, "/mainFrame/toolBar/Default/NewProject"*/).tool();
        tools.addAction().bind(new OpenAction(application, this)).path("/mainFrame/menuBar/File/Open", "/mainFrame/toolBar/Default/Open").tool();

        tools.addFolder("/mainFrame/menuBar/File/LoadRecent");

        tools.addAction().path("/mainFrame/menuBar/File/LoadRecent/Solution1").tool();
        tools.addAction().path("/mainFrame/menuBar/File/LoadRecent/Solution2").tool();
        tools.addSeparator("/mainFrame/menuBar/File/LoadRecent/Separator1");
        tools.addAction().path("/mainFrame/menuBar/File/LoadRecent/Project1").tool();
        tools.addAction().path("/mainFrame/menuBar/File/LoadRecent/Project2").tool();
        tools.addSeparator("/mainFrame/menuBar/File/LoadRecent/Separator1");
        tools.addAction().path("/mainFrame/menuBar/File/LoadRecent/Clear").tool();

        tools.addSeparator("/mainFrame/menuBar/File/Separator1");
//        model.addAction("/mainFrame/menuBar/File/ClearProjectCache");
//        model.addAction("/mainFrame/menuBar/File/ClearAllCache");
        tools.addSeparator("/mainFrame/menuBar/File/Separator2");
        tools.addAction().bind(new SettingsAction(application, this)).path("/mainFrame/menuBar/File/Settings", "/mainFrame/toolBar/Default/Settings").tool();
        tools.addSeparator("/mainFrame/menuBar/File/Separator3");
        tools.addAction().bind(new ExitAction(application, this)).path("/mainFrame/menuBar/File/Exit").tool();

    }

    protected void prepareMenu_Edit() {
        AppContainerChildren<AppComponent> tools = application.components();
        tools.addFolder("/mainFrame/menuBar/Edit");
        tools.addSeparator("/mainFrame/toolBar/Default/Separator1");
        tools.addAction().bind(new CopyAction(application)).path("/mainFrame/menuBar/Edit/Copy", "/mainFrame/toolBar/Default/Copy").tool();
        tools.addAction().bind(new CutAction(application)).path("/mainFrame/menuBar/Edit/Cut", "/mainFrame/toolBar/Default/Cut").tool();
        tools.addAction().bind(new PasteAction(application)).path("/mainFrame/menuBar/Edit/Paste", "/mainFrame/toolBar/Default/Paste").tool();

        tools.addAction().bind(new UndoAction(application)).path("/mainFrame/menuBar/Edit/Undo", "/mainFrame/toolBar/Default/Undo").tool();
        tools.addAction().bind(new RedoAction(application)).path("/mainFrame/menuBar/Edit/Redo", "/mainFrame/toolBar/Default/Redo").tool();

        tools.addSeparator("/mainFrame/toolBar/Default/Separator2");
        tools.addCustom().renderer(
                () -> {
                    return CharactersTableComponent.createComponents(sourceEditorCharTableListener,
                            CharactersTableComponent.Family.GREEK_LOWER, CharactersTableComponent.Family.GREEK_UPPER)
                            .get(0);
                }
        ).path("/mainFrame/toolBar/Default/GREEK").tool();
        tools.addCustom().renderer(
                () -> {
                    return CharactersTableComponent.createComponents(sourceEditorCharTableListener,
                            CharactersTableComponent.Family.OPERATORS)
                            .get(0);
                }
        ).path("/mainFrame/toolBar/Default/OPERATORS").tool();
        tools.addCustom().renderer(
                () -> {
                    return CharactersTableComponent.createComponents(sourceEditorCharTableListener,
                            CharactersTableComponent.Family.SYMBOLS)
                            .get(0);
                }
        ).path("/mainFrame/toolBar/Default/SYMBOLS").tool();
        tools.addCustom().renderer(
                () -> {
                    return CharactersTableComponent.createComponents(sourceEditorCharTableListener,
                            CharactersTableComponent.Family.CONSTANTS)
                            .get(0);
                }
        ).path("/mainFrame/toolBar/Default/CONSTANTS");
        tools.addCustom().renderer(
                () -> {
                    return CharactersTableComponent.createComponents(sourceEditorCharTableListener,
                            CharactersTableComponent.Family.TRIGO)
                            .get(0);
                }
        ).path("/mainFrame/toolBar/Default/TRIGO").tool();

        //        model.addAction("/mainFrame/menuBar/Edit/Macros/StartMacroRecording");
//        model.addAction("/mainFrame/menuBar/Edit/Macros/StopMacroRecording");
//        model.addAction("/mainFrame/menuBar/Edit/Macros/RunMacro");
    }

    protected void prepareMenu_View() {
        Applications.Helper.addViewToolActions(application);
        AppContainerChildren<AppComponent> tools = application.components();
        Applications.Helper.addViewPlafActions(application);
        tools.addFolder(Path.of("/mainFrame/menuBar/View/Editor Themes"));
        for (HWTextEditorTheme theme : editorThemes.editorThemes().values()) {
            tools.addAction().bind(new EditorThemeAction(this, theme.getId())).path("/mainFrame/menuBar/View/Editor Themes/" + theme.getName()).tool();
        }
        tools.addSeparator(Path.of("/mainFrame/menuBar/View/Editor Themes/Separator1"));
        CheckBox t = new CheckBox("usePlaf", application);
        t.selected().bind(editorThemes.usePlaf());
        app().components().add(t, Path.of("/mainFrame/menuBar/View/Editor Themes/Maximize L&F"));

        Applications.Helper.addViewIconActions(application);
        Applications.Helper.addViewAppearanceActions(application);

        tools.addSeparator(Path.of("/mainFrame/toolBar/Default/Separator3"));
        tools.addAction().bind(new ZoomInAction(application, "ZoomIn", this)).path("/mainFrame/menuBar/View/3DView/ZoomIn", "/mainFrame/toolBar/Default/ZoomIn").tool();
        tools.addAction().bind(new ZoomOutAction(application, "ZoomOut", this)).path("/mainFrame/menuBar/View/3DView/ZoomIn", "/mainFrame/toolBar/Default/ZoomOut").tool();
        tools.addAction().bind(new ResetCameraAction(application, "ResetCamera", this)).path("/mainFrame/menuBar/View/3DView/ResetCamera", "/mainFrame/toolBar/Default/ResetCamera").tool();

        t = new CheckBox("usePlaf", application);
        t.selected().bind(editorThemes.usePlaf());
        app().components().add(t, Path.of("/mainFrame/menuBar/View/Editor Themes/Maximize L&F"));

        t = new CheckBox("boxVisible", application);
        t.selected().bind(ws3DView.getPreferences().boxVisible());
        app().components().addMulti(t, Path.of("/mainFrame/menuBar/View/3DView/AxisVisible"), Path.of("/mainFrame/toolBar/Default/BoxVisible"));

        t = new CheckBox("axisVisible", application);
        t.selected().bind(ws3DView.getPreferences().axisVisible());
        app().components().addMulti(t, Path.of("/mainFrame/menuBar/View/3DView/AxisVisible"), Path.of("/mainFrame/toolBar/Default/AxisVisible"));

        t = new CheckBox("gridXYVisible", application);
        t.selected().bind(ws3DView.getPreferences().gridXYVisible());
        app().components().addMulti(t, Path.of("/mainFrame/menuBar/View/3DView/GridXYVisible"), Path.of("/mainFrame/toolBar/Default/GridXYVisible"));

        t = new CheckBox("perspectiveEnabled", application);
        t.selected().bind(ws3DView.getPreferences().perspectiveEnabled());
        app().components().addMulti(t, Path.of("/mainFrame/menuBar/View/3DView/PerspectiveEnabled"), Path.of("/mainFrame/toolBar/Default/PerspectiveEnabled"));

        t = new CheckBox("steroscopyEnabled", application);
        t.selected().bind(ws3DView.getPreferences().steroscopyEnabled());
        app().components().addMulti(t, Path.of("/mainFrame/menuBar/View/3DView/StereoscopeEnabled"), Path.of("/mainFrame/toolBar/Default/StereoscopeEnabled"));
    }

    protected void prepareMenu() {
        prepareMenu_File();
        prepareMenu_Edit();
        prepareMenu_View();

    }

    private void prepareWorkspace() {
        AppWorkspace ws = content();
        ws.addWindow("Solution", (wsSolutionExplorer = new HWSSolutionExplorerTool(this)), Anchor.LEFT);
        ws.addWindow("Parameters", (wsParameters = new HWSProjectParametersTool(this)), Anchor.BOTTOM);
        ws.addWindow("UI Dev", (new HWSUIPropsTool(this)), Anchor.BOTTOM);
        ws.addWindow("Properties", (wsProperties = new HWSProjectPropertiesTool(this)), Anchor.RIGHT);
        ws.addWindow("History", (wsHistory = new HWSProjectHistoryTool(this)), Anchor.LEFT);
        ws.addWindow("Files", (wsFiles = new HWSFileExplorerTool(this)), Anchor.LEFT);
        ws.addWindow("Library", (wsLibraries = new HWSLibraryExplorerTool(this)), Anchor.LEFT);
        ws.addWindow("Cache", (wsCache = new HWSCacheExplorerTool(this)), Anchor.RIGHT);
        ws.addWindow("Results", (wsResults = new HWSProjectResultsTool(this)), Anchor.RIGHT);
        ws.addWindow("Tasks", (wsTasks = new HWSTasksTool(this)), Anchor.BOTTOM);
        ws.addWindow("Locks", (wsLocks = new HWSLocksTool(this)), Anchor.BOTTOM);
        ws.addWindow("Log", (wsLog = new HWSLogTool(this)), Anchor.BOTTOM);
        ws.addWindow("Console", (wsConsole = new HWSConsoleTool(this)), Anchor.BOTTOM);
        ws.addWindow("Messages", (wsMessages = new HWSMessageTools(this)), Anchor.BOTTOM);
        ws.addWindow("3DView", (ws3DView = new HWS3DView(this)), Anchor.CENTER);
        ws.addWindow("JTextArea", (new ScrollPane(new TextArea(application))), Anchor.CENTER);
    }

    private JFileChooser createJFileChooser() {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileView(new HWFileView(this));
        return jfc;
    }

    public static class ProjectBuildProducer implements AppMessageProducer {

        private List<Message> messages = new ArrayList<Message>();
        private HadruwavesStudioV2Impl studio;

        public ProjectBuildProducer(HadruwavesStudioV2Impl studio) {
            this.studio = studio;
        }

        @Override
        public void produceMessages(Application application, WritableList<Message> messages) {
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

    private class SourceEditorCharTableListener implements CharTableListener {

        @Override
        public void onChar(CharEvent event) {
            AppWorkspace ws = content();
            AppWindow content = ws.children().get("SourceEditor");
            if (content != null) {
                if (content.model().active().get()) {
                    SourceFileEditor sfe = (SourceFileEditor) content.model().component().get();
                    if (sfe != null) {
                        sfe.getTextEditor().insertOrReplaceSelection(event.getCharCommand().getText());
                    }
                }
            }
        }
    }
}
