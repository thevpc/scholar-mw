/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.studio.standalone;

import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JMenu;
import javax.swing.KeyStroke;
import net.vpc.common.log.FileLog;
import net.vpc.common.log.Log;
import net.vpc.common.swings.FileEvent;
import net.vpc.common.swings.FileSelectedListener;
import net.vpc.common.swings.RecentFilesMenu;

import net.vpc.lib.pheromone.application.Application;
import net.vpc.lib.pheromone.application.ApplicationLoader;
import net.vpc.lib.pheromone.application.DefaultApplicationComponentsConfigurator;
import net.vpc.lib.pheromone.application.actions.ApplicationActionManager;
import net.vpc.lib.pheromone.application.actions.ApplicationInformationsAction;
import net.vpc.lib.pheromone.application.actions.CalculatorAction;
import net.vpc.lib.pheromone.application.actions.GotoWebSiteAction;
import net.vpc.lib.pheromone.application.actions.LookAndFeelConfiguratorAction;
import net.vpc.lib.pheromone.application.actions.MemoryViewAction;
import net.vpc.lib.pheromone.application.log.config.ActivityLoggerAction;
import net.vpc.lib.pheromone.application.log.config.ConsolePanelLog;
import net.vpc.lib.pheromone.application.log.config.FileLogConfigModule;
import net.vpc.lib.pheromone.application.swing.*;
import net.vpc.lib.pheromone.application.toolbar.ClockToolbarItemProvider;
import net.vpc.lib.pheromone.application.toolbar.MemoryUsageToolbarItemProvider;
import net.vpc.lib.pheromone.ariana.util.Resources;
import net.vpc.scholar.hadruwaves.studio.standalone.actions.GAboutAction;
import net.vpc.scholar.hadruwaves.studio.standalone.actions.LoadStructureAction;
import net.vpc.scholar.hadruwaves.studio.standalone.actions.NewStructureAction;

/**
 *
 * @author vpc
 */
public class TMWLabApplicationConfigurator extends DefaultApplicationComponentsConfigurator {

    public TMWLabApplicationConfigurator(Application application) {
        super(application);
    }

   

    @Override
    public void configureFileManager(FileManager manager) {
        super.configureFileManager(manager);
        Resources resources = getApplication().getResources();
        FileTypeManager fileTypeManager = manager.getFileTypeManager();
        fileTypeManager.registerFileType(new FileType("structure", resources.get("strFileType"), new String[]{"str"}, "java:OpenStructureAction"));
        fileTypeManager.registerFileType(new FileType("matrix", resources.get("matrixFileType"), new String[]{"matrix"}, "java:OpenMatrixAction"));
        fileTypeManager.registerFileType(new FileType("matlab", resources.get("matlabFileType"), new String[]{"m"}, "java:OpenMatrixAction"));
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
    public void configureCustomLog() {
        super.configureCustomLog();
        //unregister starter
        Log.unregisterAllLoggers();
        Application app = getApplication();
        Resources rsr = app.getResources();
        FileManager fm = app.getFileManager();
        FileLog debugger = new FileLog(Log.DEBUG_LOGGER, rsr.get("Log.DEBUG_LOGGER.title"), rsr.get("Log.DEBUG_LOGGER.desc"), "[$d][$h][$F:$l] $m", Log.ALL_ARRAY, fm.getFile("log/debug<DATE>.log").getPath(), 5);
        debugger.loadConfig();
        Log.registerLogger(debugger);

        ApplicationLoader.moduleLoaded("DEBUG_LOGGER loaded successfully.");
//        Log.unsetDefaultLogger();
    }
}
