package net.vpc.scholar.hadruwaves.studio.standalone.v2;

import net.vpc.common.prpbind.PropertyEvent;
import net.vpc.common.prpbind.PropertyListener;
import net.vpc.common.prpbind.WritablePList;
import net.vpc.common.swings.app.*;
import net.vpc.scholar.hadruwaves.project.HWProject;
import net.vpc.scholar.hadruwaves.project.HWSolution;
import net.vpc.scholar.hadruwaves.project.HWSolutionProcessor;
import net.vpc.scholar.hadruwaves.project.configuration.HWSConfigurationRun;
import net.vpc.scholar.hadruwaves.studio.standalone.v2.win.*;
import org.noos.xing.mydoggy.Content;
import org.noos.xing.mydoggy.ContentManager;
import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.logging.Level;

public class HadruwavesStudioV2 {
    private Application application;
    private HWSolutionProcessor processor = new HWSolutionProcessor();
    private HWSContentView wsContentComponent;
    private HWSMessageTools wsMessages;
    private HWSConsoleTool wsConsole;
    private HWSLogTool wsLog;
    private HWSLocksTool wsLocks;
    private HWSTasksTool wsTasks;
    private HWSProjectSolverTool wsSolver;
    private HWSProjectConfigurationResultsTool wsResults;
    private HWSCacheExplorerTool wsCache;
    private HWSLibraryExplorerTool wsLibraries;
    private HWSFileExplorerTool wsFiles;
    private HWSProjectUnitsTool wsUnits;
    private HWSProjectHistoryTool wsHistory;
    private HWSProjectParametersTool wsParameters;
    private HWSProjectConfigurationTool wsConfs;
    //    private HWSProjectNavigationTreeTool wsProjectNavTree;
    private HWSSolutionExplorerTool wsSolution;

    public Application getApplication() {
        return application;
    }

    public HWSolution activeSolution() {
        return proc().solution().get();
    }

    public HWSConfigurationRun activeProjectConfiguration() {
        HWProject s = activeProject();
        return s == null ? null : s.configurations().activeConfiguration().get();
    }

    public HWProject activeProject() {
        HWSolution s = activeSolution();
        return s == null ? null : s.activeProject().get();
    }

    public HWSolutionProcessor proc() {
        return processor;
    }

    public Application start() {
        proc().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                System.out.println(event);
                if (event.getProperty().getPropertyName().equals("activeProject")) {
                    HWProject p = event.getNewValue();
                    if (p == null) {
                        application.updateMessages();
                    }
                }
            }
        });
        proc().demoSolution();

        final Application app = Applications.Apps.Default();
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        application = app;
//        for (Class cls : ClassPathUtils.resolveContextClasses()) {
//
//        }
        prepareMenu();
        app.builder().mainWindowBuilder().get().workspaceFactory().set(
                Applications.Workspaces.Default(createWorkspace())
        );
        app.messageProducers().add(new AppMessageProducer() {
            @Override
            public void produceMessages(Application application, WritablePList<AppMessage> messages) {
                messages.add(new StringAppMessage(Level.WARNING, "No Project is open..."));
            }
        });
        app.start();
        app.mainWindow().title().set("Hadruwaves Studio - v2.0");
        return app;
    }

    public void prepareMenu() {
        application.tools().addFolder("/mainWindow/menuBar/File");
        application.tools().addAction("/mainWindow/menuBar/File/SaveProject");
        application.tools().addAction("/mainWindow/menuBar/File/SaveSolution");
        application.tools().addAction("/mainWindow/menuBar/File/SaveProjectAs");
        application.tools().addAction("/mainWindow/menuBar/File/SaveSolutionAs");
        application.tools().addAction("/mainWindow/menuBar/File/NewSolution");
        application.tools().addAction("/mainWindow/menuBar/File/NewProject");
        application.tools().addAction("/mainWindow/menuBar/File/LoadProject");
        application.tools().addFolder("/mainWindow/menuBar/File/LoadRecent");

        application.tools().addAction("/mainWindow/menuBar/File/LoadRecent/Solution1");
        application.tools().addAction("/mainWindow/menuBar/File/LoadRecent/Solution2");
        application.tools().addSeparator("/mainWindow/menuBar/File/LoadRecent/Separator1");
        application.tools().addAction("/mainWindow/menuBar/File/LoadRecent/Project1");
        application.tools().addAction("/mainWindow/menuBar/File/LoadRecent/Project2");
        application.tools().addSeparator("/mainWindow/menuBar/File/LoadRecent/Separator1");
        application.tools().addAction("/mainWindow/menuBar/File/LoadRecent/Clear");


        application.tools().addSeparator("/mainWindow/menuBar/File/Separator1");
        application.tools().addAction("/mainWindow/menuBar/File/ClearProjectCache");
        application.tools().addAction("/mainWindow/menuBar/File/ClearAllCache");
        application.tools().addSeparator("/mainWindow/menuBar/File/Separator2");
        application.tools().addAction("/mainWindow/menuBar/File/Settings");
        application.tools().addSeparator("/mainWindow/menuBar/File/Separator3");
        application.tools().addAction("/mainWindow/menuBar/File/Exit");
        application.tools().addFolder("/mainWindow/menuBar/Edit");
        application.tools().addAction("/mainWindow/menuBar/Edit/Copy");
        application.tools().addAction("/mainWindow/menuBar/Edit/Cut");
        application.tools().addAction("/mainWindow/menuBar/Edit/Paste");
        application.tools().addAction("/mainWindow/menuBar/Edit/Undo");
        application.tools().addAction("/mainWindow/menuBar/Edit/Redo");
        application.tools().addFolder("/mainWindow/menuBar/Edit/Macros");
        application.tools().addAction("/mainWindow/menuBar/Edit/Macros/StartMacroRecording");
        application.tools().addAction("/mainWindow/menuBar/Edit/Macros/StopMacroRecording");
        application.tools().addAction("/mainWindow/menuBar/Edit/Macros/RunMacro");

        application.tools().addFolder("/mainWindow/menuBar/View");
        application.tools().addFolder("/mainWindow/menuBar/View/ToolWindows");
        application.tools().addCheck("/mainWindow/menuBar/View/ToolWindows/Projects");
        application.tools().addCheck("/mainWindow/menuBar/View/ToolWindows/Errors");
        application.tools().addCheck("/mainWindow/menuBar/View/ToolWindows/Messages");

        application.tools().addRadio("/mainWindow/menuBar/View/Appearance/Normal").tool().group().set("AppearanceMode");
        application.tools().addRadio("/mainWindow/menuBar/View/Appearance/FullScreen").tool().group().set("AppearanceMode");
        application.tools().addCheck("/mainWindow/menuBar/View/Appearance/Toolbar");
        application.tools().addCheck("/mainWindow/menuBar/View/Appearance/StatusBar");

//        app.tools().addFolder("/mainWindow/menuBar/Window");

        application.tools().addFolder("/mainWindow/toolBar/Help");
        application.tools().addAction("/mainWindow/toolBar/Help/About");
        application.tools().addAction("/mainWindow/toolBar/3D/1");
        application.tools().addAction("/mainWindow/toolBar/3D/2");
        application.tools().addAction("/mainWindow/toolBar/3D/3");
        application.tools().addAction("/mainWindow/toolBar/3D/4");
        application.tools().addAction("/mainWindow/toolBar/3D/5");
        application.tools().addAction("/mainWindow/toolBar/3D/6");
        application.tools().addAction("/mainWindow/toolBar/3D/7");
        application.tools().addAction("/mainWindow/toolBar/3D/8");
        application.tools().addAction("/mainWindow/toolBar/3D/9");
        application.tools().addAction("/mainWindow/toolBar/3D/10");
        application.tools().addAction("/mainWindow/toolBar/3D/11");
    }

    private void addAction(String path, String title, String imageName, ActionListener listener) {
        AppToolComponent<AppToolAction> c = application.tools().addAction(path);
        c.tool().title().set(title);
        c.tool().smallIcon().set(HWUtils.iconFor(imageName));
        c.tool().action().set(listener);
    }

    private JComponent createWorkspace() {
        MyDoggyToolWindowManager toolWindowManager = new MyDoggyToolWindowManager();
        wsSolution = new HWSSolutionExplorerTool(this);
        toolWindowManager.registerToolWindow(
                "Solution",        // Tool Window identifier
                "",    // Tool Window Title
                null,//icon,           // Tool Window Icon
                wsSolution,      // Tool Window Component
                ToolWindowAnchor.LEFT // Tool Window Anchor
        );
//        wsProjectNavTree = new HWSProjectNavigationTreeTool(this);
//        toolWindowManager.registerToolWindow(
//                "Navigation Tree",        // Tool Window identifier
//                "",    // Tool Window Title
//                null,//icon,           // Tool Window Icon
//                wsProjectNavTree,      // Tool Window Component
//                ToolWindowAnchor.LEFT // Tool Window Anchor
//        );
        wsConfs = new HWSProjectConfigurationTool(this);
        toolWindowManager.registerToolWindow(
                "Configuration",        // Tool Window identifier
                "",    // Tool Window Title
                null,//icon,           // Tool Window Icon
                wsConfs,      // Tool Window Component
                ToolWindowAnchor.RIGHT // Tool Window Anchor
        );


        wsParameters = new HWSProjectParametersTool(this);
        toolWindowManager.registerToolWindow(
                "Parameters",        // Tool Window identifier
                "",    // Tool Window Title
                null,//icon,           // Tool Window Icon
                wsParameters,      // Tool Window Component
                ToolWindowAnchor.BOTTOM // Tool Window Anchor
        );

        wsHistory = new HWSProjectHistoryTool(this);
        toolWindowManager.registerToolWindow(
                "History",        // Tool Window identifier
                "",    // Tool Window Title
                null,//icon,           // Tool Window Icon
                wsHistory,      // Tool Window Component
                ToolWindowAnchor.LEFT // Tool Window Anchor
        );

        wsUnits = new HWSProjectUnitsTool(this);
        toolWindowManager.registerToolWindow(
                "Units",        // Tool Window identifier
                "",    // Tool Window Title
                null,//icon,           // Tool Window Icon
                wsUnits,      // Tool Window Component
                ToolWindowAnchor.LEFT // Tool Window Anchor
        );

        wsFiles = new HWSFileExplorerTool(this);
        toolWindowManager.registerToolWindow(
                "Files",        // Tool Window identifier
                "",    // Tool Window Title
                null,//icon,           // Tool Window Icon
                wsFiles,      // Tool Window Component
                ToolWindowAnchor.LEFT // Tool Window Anchor
        );

        wsLibraries = new HWSLibraryExplorerTool(this);
        toolWindowManager.registerToolWindow(
                "Library",        // Tool Window identifier
                "",    // Tool Window Title
                null,//icon,           // Tool Window Icon
                wsLibraries,      // Tool Window Component
                ToolWindowAnchor.LEFT // Tool Window Anchor
        );

        wsCache = new HWSCacheExplorerTool(this);
        toolWindowManager.registerToolWindow(
                "Cache",        // Tool Window identifier
                "",    // Tool Window Title
                null,//icon,           // Tool Window Icon
                wsCache,      // Tool Window Component
                ToolWindowAnchor.RIGHT // Tool Window Anchor
        );

        wsResults = new HWSProjectConfigurationResultsTool(this);
        ToolWindow resultsWindow = toolWindowManager.registerToolWindow(
                "Results",        // Tool Window identifier
                "",    // Tool Window Title
                null,//icon,           // Tool Window Icon
                wsResults,      // Tool Window Component
                ToolWindowAnchor.RIGHT // Tool Window Anchor
        );
        wsSolver = new HWSProjectSolverTool(this);
        ToolWindow solver = toolWindowManager.registerToolWindow(
                "Solver",        // Tool Window identifier
                "",    // Tool Window Title
                null,//icon,           // Tool Window Icon
                wsSolver,      // Tool Window Component
                ToolWindowAnchor.RIGHT // Tool Window Anchor
        );

        wsTasks = new HWSTasksTool(this);
        ToolWindow tasks = toolWindowManager.registerToolWindow(
                "Tasks",        // Tool Window identifier
                "",    // Tool Window Title
                null,//icon,           // Tool Window Icon
                wsTasks,      // Tool Window Component
                ToolWindowAnchor.BOTTOM // Tool Window Anchor
        );
        wsLocks = new HWSLocksTool(this);
        ToolWindow locks = toolWindowManager.registerToolWindow(
                "Locks",        // Tool Window identifier
                "",    // Tool Window Title
                null,//icon,           // Tool Window Icon
                wsLocks,      // Tool Window Component
                ToolWindowAnchor.BOTTOM // Tool Window Anchor
        );

        wsLog = new HWSLogTool(this);
        toolWindowManager.registerToolWindow(
                "Log",        // Tool Window identifier
                "",    // Tool Window Title
                null,//icon,           // Tool Window Icon
                wsLog,      // Tool Window Component
                ToolWindowAnchor.BOTTOM // Tool Window Anchor
        );

        wsConsole = new HWSConsoleTool(this);
        toolWindowManager.registerToolWindow(
                "Console",        // Tool Window identifier
                "",    // Tool Window Title
                null,//icon,           // Tool Window Icon
                wsConsole,      // Tool Window Component
                ToolWindowAnchor.BOTTOM // Tool Window Anchor
        );

        wsMessages = new HWSMessageTools(this);
        toolWindowManager.registerToolWindow(
                "Messages",        // Tool Window identifier
                "",    // Tool Window Title
                null,//icon,           // Tool Window Icon
                wsMessages,      // Tool Window Component
                ToolWindowAnchor.BOTTOM // Tool Window Anchor
        );

        ContentManager contentManager = toolWindowManager.getContentManager();
        contentManager.setEnabled(true);
        wsContentComponent = new HWSContentView(this);
        Content mainContent = contentManager.addContent("MainContent",   // Content Key
                "MainContent",  // Content title
                null,     // Content Item
                wsContentComponent, // Content component
                "Result" // Content tooltip text
        );
        mainContent.setEnabled(true);
        for (ToolWindow window : toolWindowManager.getToolWindows()) {
            window.setAvailable(true);
        }
        return toolWindowManager;

    }
}
