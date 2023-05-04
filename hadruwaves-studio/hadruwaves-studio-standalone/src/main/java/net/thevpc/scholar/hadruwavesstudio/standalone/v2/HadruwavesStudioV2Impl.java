package net.thevpc.scholar.hadruwavesstudio.standalone.v2;

import net.thevpc.common.i18n.I18nResourceBundle;
import net.thevpc.common.i18n.Str;
import net.thevpc.common.msg.Message;
import net.thevpc.common.msg.StringMessage;
import net.thevpc.common.props.*;
import net.thevpc.common.strings.StringUtils;
import net.thevpc.echo.*;
import net.thevpc.common.props.Path;
import net.thevpc.echo.Button;
import net.thevpc.echo.Dimension;
import net.thevpc.echo.Frame;
import net.thevpc.echo.ScrollPane;
import net.thevpc.echo.TextArea;
import net.thevpc.echo.api.AppMessageProducer;
import net.thevpc.echo.api.components.AppComponent;
import net.thevpc.echo.api.components.AppDock;
import net.thevpc.echo.api.components.AppFrame;
import net.thevpc.echo.api.components.AppWindow;
import net.thevpc.echo.constraints.Anchor;
import net.thevpc.echo.constraints.Pos;
import net.thevpc.echo.iconset.IconConfig;
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
    public ObservableValue<String> iconSet() {
        return app().mainFrame().get().iconSet();
    }

    @Override
    public ObservableValue<IconConfig> iconConfig() {
        return app().mainFrame().get().iconConfig();
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
                AppComponent cc = application.toolkit().createComponent(sfe);
                cc.anchor().set(Anchor.CENTER);
                cc.title().set(Str.of("SourceEditor"));
                ws.children().add(cc);
                content = (AppWindow) cc;
                content.title().set(Str.of(s.file.getName()));
                content.closable().set(false);
            } else {
                sfe = (SourceFileEditor) content.component().get();
            }
            content.title().set(Str.of(s.file.getName()));
            content.icon().set(app().iconSets().iconForFile(s.file, false, false,null));
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
        app.mainFrame().get().title().set(Str.of("Hadruwaves Studio - v2.0"));
        app.iconSets().icon("App").bindTarget(app.mainFrame().get().icon());
        SwingUtilities.invokeLater(() -> app.mainFrame().get().prefSize().set(new Dimension(1024, 800)));
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
        tools.addFolder(Path.of("/mainFrame/menuBar/File"));
        tools.add(new Button("SaveAction",new SaveAction(application, this),app()),Path.of("/mainFrame/menuBar/File/Save"/*, "/mainFrame/toolBar/Default/Save"*/));
        tools.add(new Button("SaveAllAction",new SaveAllAction(application, this),app()),Path.of("/mainFrame/menuBar/File/SaveAll"));
        tools.add(new Button("SaveAllAction",new SaveAllAction(application, this),app()),Path.of("/mainFrame/toolBar/Default/SaveAll"));
        tools.add(new Button("SaveAsAction",new SaveAsAction(application, this),app()),Path.of("/mainFrame/menuBar/File/SaveAs"/*, "/mainFrame/toolBar/Default/SaveAs"*/));
        tools.add(new Button("NewSolutionAction",new NewSolutionAction(application, this),app()),Path.of("/mainFrame/menuBar/File/New/NewSolution"));
        tools.add(new Button("NewSolutionAction",new NewSolutionAction(application, this),app()),Path.of("/mainFrame/toolBar/Default/NewSolution"));
        tools.add(new Button("NewProjectAction",new NewProjectAction(application, this),app()),Path.of("/mainFrame/menuBar/File/New/NewProject"/*, "/mainFrame/toolBar/Default/NewProject"*/));
        tools.add(new Button("OpenAction",new OpenAction(application, this),app()),Path.of("/mainFrame/menuBar/File/Open"));
        tools.add(new Button("OpenAction",new OpenAction(application, this),app()),Path.of("/mainFrame/toolBar/Default/Open"));

        tools.addFolder(Path.of("/mainFrame/menuBar/File/LoadRecent"));

        tools.add(new Button("Solution1",Str.of("Solution1"), app()),Path.of("/mainFrame/menuBar/File/LoadRecent/Solution1"));
        tools.add(new Button("Solution2",Str.of("Solution2"), app()),Path.of("/mainFrame/menuBar/File/LoadRecent/Solution2"));
        tools.addSeparator(Path.of("/mainFrame/menuBar/File/LoadRecent/Separator1"));
        tools.add(new Button("Solution1",Str.of("Project1"), app()),Path.of("/mainFrame/menuBar/File/LoadRecent/Project1"));
        tools.add(new Button("Solution1",Str.of("Project2"), app()),Path.of("/mainFrame/menuBar/File/LoadRecent/Project2"));
        tools.addSeparator(Path.of("/mainFrame/menuBar/File/LoadRecent/Separator1"));
        tools.add(new Button("Clear",Str.of("Clear"), app()),Path.of("/mainFrame/menuBar/File/LoadRecent/Clear"));

        tools.addSeparator(Path.of("/mainFrame/menuBar/File/Separator1"));
//        model.addAction("/mainFrame/menuBar/File/ClearProjectCache");
//        model.addAction("/mainFrame/menuBar/File/ClearAllCache");
        tools.addSeparator(Path.of("/mainFrame/menuBar/File/Separator2"));
        tools.add(new Button("SettingsAction",new SettingsAction(application, this),app()),Path.of("/mainFrame/menuBar/File/Settings"));
        tools.add(new Button("SettingsAction",new SettingsAction(application, this),app()),Path.of("/mainFrame/toolBar/Default/Settings"));
        tools.addSeparator(Path.of("/mainFrame/menuBar/File/Separator3"));
        tools.add(new Button("ExitAction",new ExitAction(application, this),app()),Path.of("/mainFrame/menuBar/File/Exit"));
    }

    protected void prepareMenu_Edit() {
        AppContainerChildren<AppComponent> tools = application.components();
        tools.addFolder(Path.of("/mainFrame/menuBar/Edit"));
        tools.addSeparator(Path.of("/mainFrame/toolBar/Default/Separator1"));
        tools.add(new Button("CopyAction",new CopyAction(application),app()),Path.of("/mainFrame/menuBar/Edit/Copy", "/mainFrame/toolBar/Default/Copy"));
        tools.add(new Button("CutAction",new CutAction(application),app()),Path.of("/mainFrame/menuBar/Edit/Cut", "/mainFrame/toolBar/Default/Cut"));
        tools.add(new Button("PasteAction",new PasteAction(application),app()),Path.of("/mainFrame/menuBar/Edit/Paste", "/mainFrame/toolBar/Default/Paste"));

        tools.add(new Button("UndoAction",new UndoAction(application),app()),Path.of("/mainFrame/menuBar/Edit/Undo", "/mainFrame/toolBar/Default/Undo"));
        tools.add(new Button("RedoAction",new RedoAction(application),app()),Path.of("/mainFrame/menuBar/Edit/Redo", "/mainFrame/toolBar/Default/Redo"));

        tools.addSeparator(Path.of("/mainFrame/toolBar/Default/Separator2"));
        tools.add(new UserControl("GREEK_LOWER",
                    CharactersTableComponent.createComponents(sourceEditorCharTableListener,
                            CharactersTableComponent.Family.GREEK_LOWER, CharactersTableComponent.Family.GREEK_UPPER)
                            .get(0),app()),Path.of("/mainFrame/toolBar/Default/GREEK"));
        tools.add(new UserControl("OPERATORS",
                    CharactersTableComponent.createComponents(sourceEditorCharTableListener,
                            CharactersTableComponent.Family.OPERATORS)
                            .get(0)
                ,app()),Path.of("/mainFrame/toolBar/Default/OPERATORS"));
        tools.add(new UserControl("CONSTANTS",
                    CharactersTableComponent.createComponents(sourceEditorCharTableListener,
                            CharactersTableComponent.Family.SYMBOLS)
                            .get(0)
                ,app()),Path.of("/mainFrame/toolBar/Default/SYMBOLS"));
        tools.add(new UserControl("CONSTANTS",
                    CharactersTableComponent.createComponents(sourceEditorCharTableListener,
                            CharactersTableComponent.Family.CONSTANTS)
                            .get(0),app()
        ),Path.of("/mainFrame/toolBar/Default/CONSTANTS"));
        tools.add(new UserControl("Trigo",CharactersTableComponent.createComponents(sourceEditorCharTableListener,
                            CharactersTableComponent.Family.TRIGO)
                            .get(0)
                ,app()),Path.of("/mainFrame/toolBar/Default/TRIGO"));

        //        model.addAction("/mainFrame/menuBar/Edit/Macros/StartMacroRecording");
//        model.addAction("/mainFrame/menuBar/Edit/Macros/StopMacroRecording");
//        model.addAction("/mainFrame/menuBar/Edit/Macros/RunMacro");
    }

    protected void prepareMenu_View() {
        Frame appFrame = (Frame) this.app().mainFrame().get();
        appFrame.defaultMenus().addViewToolActions();
        AppContainerChildren<AppComponent> tools = application.components();
        appFrame.defaultMenus().addViewPlafActions();
        tools.addFolder(Path.of("/mainFrame/menuBar/View/Editor Themes"));
        for (HWTextEditorTheme theme : editorThemes.editorThemes().values()) {
            tools.add(new Button("EditorThemeAction",new EditorThemeAction(this, theme.getId()),app()),Path.of("/mainFrame/menuBar/View/Editor Themes/" + theme.getName()));
        }
        tools.addSeparator(Path.of("/mainFrame/menuBar/View/Editor Themes/Separator1"));
        CheckBox t = new CheckBox("usePlaf", application);
        t.selected().bind(editorThemes.usePlaf());
        app().components().add(t, Path.of("/mainFrame/menuBar/View/Editor Themes/Maximize L&F"));

        appFrame.defaultMenus().addViewIconActions();
        appFrame.defaultMenus().addViewAppearanceActions();

        tools.addSeparator(Path.of("/mainFrame/toolBar/Default/Separator3"));
        tools.add(new Button("new ZoomInAction",new ZoomInAction(application, "ZoomIn", this),app()),Path.of("/mainFrame/menuBar/View/3DView/ZoomIn"));
        tools.add(new Button("new ZoomInAction",new ZoomInAction(application, "ZoomIn", this),app()),Path.of("/mainFrame/toolBar/Default/ZoomIn"));
        tools.add(new Button("ZoomOutAction",new ZoomOutAction(application, "ZoomOut", this),app()),Path.of("/mainFrame/menuBar/View/3DView/ZoomIn"));
        tools.add(new Button("ZoomOutAction",new ZoomOutAction(application, "ZoomOut", this),app()),Path.of("/mainFrame/toolBar/Default/ZoomOut"));
        tools.add(new Button("ResetCameraAction",new ResetCameraAction(application, "ResetCamera", this),app()),Path.of("/mainFrame/menuBar/View/3DView/ResetCamera"));
        tools.add(new Button("ResetCameraAction",new ResetCameraAction(application, "ResetCamera", this),app()),Path.of("/mainFrame/toolBar/Default/ResetCamera"));

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

    private void prepareWorkspaceWindow(String name,Object toolkitComponent,Anchor anchor) {
        AppDock ws = content();
        AppComponent uc =null;
        if(toolkitComponent instanceof AppComponent){
            uc=(AppComponent) toolkitComponent;
        }else {
            uc = new UserControl(
                    name, toolkitComponent, app()
            );
        }
        uc.anchor().set(anchor);
        uc.title().set(Str.of(name));
        ws.children().add(uc);
    }
    private void prepareWorkspace() {
        Frame frame = new Frame("Frame", app());
        application.mainFrame().set(frame);
        frame.content().set(new DockPane("DockPane",app()));
        AppDock ws = content();
        prepareWorkspaceWindow("Solution", (wsSolutionExplorer = new HWSSolutionExplorerTool(this)), Anchor.LEFT);
        prepareWorkspaceWindow("Parameters", (wsParameters = new HWSProjectParametersTool(this)), Anchor.BOTTOM);
        prepareWorkspaceWindow("UI Dev", (new HWSUIPropsTool(this)), Anchor.BOTTOM);
        prepareWorkspaceWindow("Properties", (wsProperties = new HWSProjectPropertiesTool(this)), Anchor.RIGHT);
        prepareWorkspaceWindow("History", (wsHistory = new HWSProjectHistoryTool(this)), Anchor.LEFT);
        prepareWorkspaceWindow("Files", (wsFiles = new HWSFileExplorerTool(this)), Anchor.LEFT);
        prepareWorkspaceWindow("Library", (wsLibraries = new HWSLibraryExplorerTool(this)), Anchor.LEFT);
        prepareWorkspaceWindow("Cache", (wsCache = new HWSCacheExplorerTool(this)), Anchor.RIGHT);
        prepareWorkspaceWindow("Results", (wsResults = new HWSProjectResultsTool(this)), Anchor.RIGHT);
        prepareWorkspaceWindow("Tasks", (wsTasks = new HWSTasksTool(this)), Anchor.BOTTOM);
        prepareWorkspaceWindow("Locks", (wsLocks = new HWSLocksTool(this)), Anchor.BOTTOM);
        prepareWorkspaceWindow("Log", (wsLog = new HWSLogTool(this)), Anchor.BOTTOM);
        prepareWorkspaceWindow("Console", (wsConsole = new HWSConsoleTool(this)), Anchor.BOTTOM);
        prepareWorkspaceWindow("Messages", (wsMessages = new HWSMessageTools(this)), Anchor.BOTTOM);
        prepareWorkspaceWindow("3DView", (ws3DView = new HWS3DView(this)), Anchor.CENTER);
        prepareWorkspaceWindow("JTextArea", (new ScrollPane(new TextArea(application))), Anchor.CENTER);
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
            AppDock ws = content();
            AppWindow content = (AppWindow) ws.children().get("SourceEditor");
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
