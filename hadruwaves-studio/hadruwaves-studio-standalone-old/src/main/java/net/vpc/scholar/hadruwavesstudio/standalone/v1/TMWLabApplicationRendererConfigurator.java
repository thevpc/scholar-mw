/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v1;

import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JMenu;
import javax.swing.KeyStroke;
import net.thevpc.common.log.FileLog;
import net.thevpc.common.log.Log;
import net.thevpc.common.swing.FileEvent;
import net.thevpc.common.swing.FileSelectedListener;
import net.thevpc.common.swing.RecentFilesMenu;

import net.vpc.lib.pheromone.application.Application;
import net.vpc.lib.pheromone.application.ApplicationLoader;
import net.vpc.lib.pheromone.application.ApplicationRenderer;
import net.vpc.lib.pheromone.application.DefaultApplicationRendererComponentsConfigurator;
import net.vpc.lib.pheromone.application.actions.ApplicationActionManager;
import net.vpc.lib.pheromone.application.actions.ApplicationInformationsAction;
import net.vpc.lib.pheromone.application.actions.CalculatorAction;
import net.vpc.lib.pheromone.application.actions.GotoWebSiteAction;
import net.vpc.lib.pheromone.application.actions.LookAndFeelConfiguratorAction;
import net.vpc.lib.pheromone.application.actions.MemoryViewAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.TMWHelpAction;
import net.vpc.lib.pheromone.application.log.config.ActivityLoggerAction;
import net.vpc.lib.pheromone.application.log.config.ConsolePanelLog;
import net.vpc.lib.pheromone.application.log.config.FileLogConfigModule;
import net.vpc.lib.pheromone.application.log.config.LogConfigModuleManager;
import net.vpc.lib.pheromone.application.swing.*;
import net.vpc.lib.pheromone.application.toolbar.ClockToolbarItemProvider;
import net.vpc.lib.pheromone.application.toolbar.MemoryUsageToolbarItemProvider;
import net.vpc.lib.pheromone.ariana.util.Resources;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.GAboutAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.LoadStructureAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions.NewStructureAction;

/**
 *
 * @author vpc
 */
public class TMWLabApplicationRendererConfigurator extends DefaultApplicationRendererComponentsConfigurator {

    public TMWLabApplicationRendererConfigurator(ApplicationRenderer application) {
        super(application);
    }

    @Override
    public void configureActionManager(ApplicationActionManager actionManager) {
        super.configureActionManager(actionManager);
        actionManager.registerAction(new GAboutAction(getApplicationRenderer()), "Group.Misc", false, true);
//                actionManager.registerAction(createHtmlHelpAction("help/helpPM.html"), "Group.Misc", false, true);
        actionManager.registerAction(new GotoWebSiteAction(getApplicationRenderer(), "GotoEnitWebSiteAction", "http://www.enit.rnu.tn"), "Group.Misc", false, true);
        actionManager.registerAction(new LookAndFeelConfiguratorAction(getApplicationRenderer()), "Group.Misc", false, true);
        actionManager.registerAction(new ApplicationInformationsAction(getApplicationRenderer()), "Group.Misc", false, true);
        actionManager.registerAction(new MemoryViewAction(getApplicationRenderer()), "Group.Tools", false, true);
        actionManager.registerAction(new CalculatorAction(getApplicationRenderer()), "Group.Tools", false, true);
        actionManager.registerAction(new NewStructureAction(getApplicationRenderer()), "Group.Tools", false, true);
        actionManager.registerAction(new LoadStructureAction(getApplicationRenderer()), "Group.Tools", false, true);
        actionManager.registerAction(new ActivityLoggerAction(getApplicationRenderer(), (ConsolePanelLog) Log.getLogger(Log.CONSOLE_LOGGER)), "Group.Tools", false, true);
        actionManager.registerAction(((FileLogConfigModule) getApplicationRenderer().getLogConfigModuleManager().getLogConfigModule(Log.DEBUG_LOGGER)).getAction(), "Group.Tools", false, true);
        actionManager.registerAction(new TMWHelpAction(getApplicationRenderer()), "Group.Tools", false, true);
                
    }

    @Override
    public void configureStatusbar(JToolBar2 toolbar) {
        super.configureStatusbar(toolbar);
        toolbar.setDefaultActions(new String[]{
                    JToolBar2.MAX_SEPARATOR,
                    JToolBar2.SEPARATOR,
                    ClockToolbarItemProvider.TOOLBAR_ITEM_CLOCK,
                    JToolBar2.SEPARATOR,
                    MemoryUsageToolbarItemProvider.TOOLBAR_ITEM_MEMORY,
                    JToolBar2.SEPARATOR
                });
    }

    @Override
    public void configureToolbar(JToolBar2 toolbar) {
        super.configureToolbar(toolbar);
        toolbar.setDefaultActions(new String[]{
                    ApplicationActionManager.QUIT_ACTION,
                    JToolBar2.SEPARATOR,
                    CalculatorAction.ACTION,
                    JToolBar2.SEPARATOR,
                    "ExternalHtmlAction"
                });
    }

    @Override
    public void configureMenubar(JMenuBar2 menurar) {
        super.configureMenubar(menurar);
        TMWLabApplication app = (TMWLabApplication) getApplication();
        JMenu generalMenu = new JMenu(app.getResources().get("FileMenu"));
        menurar.add(generalMenu);
        generalMenu.setMnemonic(KeyEvent.VK_G);
        final ApplicationActionManager actionManager = getApplicationRenderer().getActionManager();
        generalMenu.add(actionManager.getAction("NewStructureAction"));
        generalMenu.add(actionManager.getAction("LoadStructureAction"));
        generalMenu.addSeparator();


        RecentFilesMenu recentFilesMenu = new RecentFilesMenu();
        app.setRecentFilesMenu(recentFilesMenu);
        
        //todo taha
        //recentFilesMenu.setForgotBadFilesEnabled(true);
        //recentFilesMenu.setRelativePathEnabled(true);
        recentFilesMenu.addFileSelectedListener(new FileSelectedListener() {

            public void fileSelected(FileEvent evt) {
                LoadStructureAction loadStructureAction = (LoadStructureAction) actionManager.getAction("LoadStructureAction");
                loadStructureAction.load((File) evt.getFile());
            }
        });
        generalMenu.add(recentFilesMenu);
        generalMenu.addSeparator();
        generalMenu.add(actionManager.getAction(ApplicationActionManager.QUIT_ACTION)).setAccelerator(KeyStroke.getKeyStroke(115, 8));
        JMenu paramsMenu = new JMenu(app.getResources().get("ParamsMenu"));
        paramsMenu.setMnemonic(KeyEvent.VK_C);
        menurar.add(paramsMenu);
        paramsMenu.add(actionManager.getAction("ConfigurationAction")).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK));
        paramsMenu.addSeparator();
        paramsMenu.add(actionManager.getAction("LookAndFeelConfiguratorAction")).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK));
        paramsMenu.add(actionManager.getAction("MemoryViewAction")).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK));
        paramsMenu.addSeparator();
        paramsMenu.add(actionManager.getAction("ActivityLoggerAction")).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK));

        menurar.add(getApplicationRenderer().getUIHelper().configureWindowManagerMenu(null));
        JMenu helpMenu = new JMenu("?");
        helpMenu.setMnemonic(KeyEvent.VK_EXCLAMATION_MARK);
        menurar.add(helpMenu);
        helpMenu.add(actionManager.getAction(ApplicationActionManager.HELP_ACTION)).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        helpMenu.add(actionManager.getAction("GotoEnitWebSiteAction")).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, KeyEvent.SHIFT_MASK));
        helpMenu.addSeparator();
        helpMenu.add(actionManager.getAction("ApplicationInformationsAction")).setAccelerator(KeyStroke.getKeyStroke(73, 3));
        helpMenu.add(actionManager.getAction(ApplicationActionManager.ABOUT_ACTION));
    }

    @Override
    public void configureStartupLog() {
        super.configureStartupLog();
        Log.unsetDefaultLogger();
        Application app = getApplication();
        Resources rsr = app.getResources();
        FileLog starter = new FileLog(Log.STARUP_LOGGER, rsr.get("Log.STARTUP_LOGGER.title"), rsr.get("Log.STARTUP_LOGGER.desc"), "[$d][$h][$F:$l] $m", Log.ALL_ARRAY, new File(app.getInstallationInfo().getInstallationDir(), "log/debug<DATE>.log").getPath(), 5);
        Log.registerLogger(starter);
    }

    @Override
    public void configureLogConfigModuleManager(LogConfigModuleManager configModuleManager) {
        super.configureLogConfigModuleManager(configModuleManager); //To change body of generated methods, choose Tools | Templates.
    
    //unregister starter
//        Log.unregisterAllLoggers();
//        Application app = getApplication();
        FileManager fm = getApplication().getFileManager();
//        FileLog debugger = new FileLog(Log.DEBUG_LOGGER, rsr.get("Log.DEBUG_LOGGER.title"), rsr.get("Log.DEBUG_LOGGER.desc"), "[$d][$h][$F:$l] $m", Log.ALL_ARRAY, fm.getFile("log/debug<DATE>.log").getPath(), 5);
//        debugger.loadConfig();
//        Log.registerLogger(debugger);

        Resources rsr = getApplication().getResources();
        ConsolePanelLog console = new ConsolePanelLog(getApplicationRenderer(), Log.CONSOLE_LOGGER, rsr.get("Log.CONSOLE_LOGGER.title"), rsr.get("Log.CONSOLE_LOGGER.desc"), "$d $m", new String[]{
                    Log.TRACE
                }, new ConsolePanel(getApplication().getConfigurationManager().getSharedConfigurationProvider()), fm.getFile("log/trace<SEQ>.log").getPath(), 1);
        console.loadConfig();
        Log.registerLogger(console);
        ApplicationLoader.moduleLoaded("CONSOLE_LOGGER loaded successfully.");
//        Log.unsetDefaultLogger();
        Log.getLogger(Log.CONSOLE_LOGGER).addAcceptedMessageTypeGroup(Log.CLIENT_TRACE_MSG_GROUP);
    }
    
//    @Override
//    public void configureCustomLog() {
//        super.configureCustomLog();
//        
//    }
}
