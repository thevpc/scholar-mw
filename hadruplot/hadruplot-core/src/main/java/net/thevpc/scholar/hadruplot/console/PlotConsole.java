package net.thevpc.scholar.hadruplot.console;

import net.thevpc.common.swing.win.WindowInfo;
import net.thevpc.common.swing.win.WindowInfoListener;
import net.thevpc.common.swing.win.WindowPath;
import net.thevpc.scholar.hadruplot.console.extension.PlotConsoleCacheSupport;
import net.thevpc.scholar.hadruplot.console.extension.PlotConsoleFileSupport;
import net.thevpc.scholar.hadruplot.console.extension.PlotConsoleTool;
import net.thevpc.scholar.hadruplot.extension.PlotWindowManagerProvider;
import net.thevpc.common.io.FileUtils;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.*;
import net.thevpc.common.swing.file.ExtensionFileChooserFilter;
import net.thevpc.common.swing.frame.JInternalFrameHelper;
import net.thevpc.common.swing.text.LogAreaComponent;
import net.thevpc.common.swing.SwingUtilities3;
import net.thevpc.scholar.hadruplot.*;
import net.thevpc.scholar.hadruplot.console.params.ParamSet;
import net.thevpc.scholar.hadruplot.console.yaxis.PlotAxis;
import net.thevpc.scholar.hadruplot.filetypes.PlotFileTypeJpeg;
import net.thevpc.scholar.hadruplot.filetypes.PlotFileTypePng;

import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class PlotConsole implements PlotComponentDisplayer, PlotManager, ProgressMonitorFactory, PlotWindowManagerProvider {

    public static int debugFramesCount = 0;
    public static String PLOT_CONSOLE_FILE_EXTENSION = "plotconsole";
    public static String PLOT_CONSOLE_FILE_DESC = "Java Plot Console";
    public static SimpleDateFormat FILE_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmss");
    public static ExtensionFileChooserFilter CHOOSER_FILTER = new ExtensionFileChooserFilter(PLOT_CONSOLE_FILE_EXTENSION, PLOT_CONSOLE_FILE_DESC);
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");

    private final Object autoSaving = new Object();
    boolean disposing;
    private long startTime = 0;
    private String frameTitle = "NO_NAME";
    private LogAreaComponent logger;
    //    private JInternalFrame logFrame;
    private ConsoleLogger log = new DefaultConsoleLogger(this);
    private PlotConsoleFrame plotConsoleFrame;
    private CloseOption closeOption = CloseOption.EXIT;
    private TaskMonitorManagerComponent taskMonitorComponent;
    private List<PlotConsoleTool> tools = new ArrayList<>();
    private boolean readOnly = false;
    private String autoSavingFilePattern;
    private File currentAutoSavingFile;
    private ObjectOutputStream autoSavingFileOutputStream;
    private boolean autoSave = false;
    private long progressPeriod = 2000;
    private File currentDirectory = null;
    private ProgressMonitorThread progressMonitorThread;
    private List<PlotConsoleCacheSupport> plotConsoleCacheSupports = new ArrayList<>();
    private PlotConsoleWindowManager windowManager = null;
    private List<PlotConsoleFileSupport> fileSupportList = new ArrayList<>();
    private List<PlotConsoleMenuItem> menus = new ArrayList<>();
    private TaskMonitorManager taskMonitor;

    public PlotConsole() {
        this(new File("."), false);
    }

    public PlotConsole(boolean autoSave) {
        this(new File("."), autoSave);
    }

    public PlotConsole(File folder, boolean autoSave) {
        this(folder);
        setAutoSave(autoSave);
    }

    public PlotConsole(File folder) {
        this(null, folder);
        silentSetFrameTitle(getClass().getSimpleName());
    }

    public PlotConsole(String title, File folder) {
        this.currentDirectory = folder;
        this.windowManager = new PlotConsoleWindowManager(this);

        ServiceLoader<PlotConsoleCacheSupport> sl = ServiceLoader.load(PlotConsoleCacheSupport.class);
        for (PlotConsoleCacheSupport sup : sl) {
            plotConsoleCacheSupports.add(sup);
        }
        //
        silentSetFrameTitle(title);
        setAutoSavingFilePattern("{classname}-{date}");
    }

    public PlotConsole start() {
        if (startTime == 0) {
            getTaskMonitor();
            getLog().trace("Start Project " + getFrameTitle());
            startTime = System.currentTimeMillis();
            disposing = false;
            readOnly = false;

            progressMonitorThread = new ProgressMonitorThread(this, progressPeriod);

            progressMonitorThread.start();
        }
        return this;
    }

    public void dispose() {

        disposing = true;
        if (taskMonitorComponent != null) {
            getTaskMonitor().terminateAll();
        }
        try {
            if (progressMonitorThread != null) {
                progressMonitorThread.softStop();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        disposeAutoSave();
        progressMonitorThread = null;

    }

    //    public JInternalFrameHelper getTaskMonitorFrame() {
//        getTaskMonitor();
//        return new JInternalFrameHelper(taskMonitorFrame);
//    }
//
//    public JInternalFrameHelper getLockMonitorFrame() {
//        getLockMonitor();
//        return new JInternalFrameHelper(lockMonitorFrame);
//    }
    protected void disposeAutoSave() {
        synchronized (autoSaving) {
            try {
                if (autoSavingFileOutputStream != null) {
                    autoSavingFileOutputStream.close();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                autoSavingFileOutputStream = null;
                currentAutoSavingFile = null;
            }
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        dispose();
    }

    //    /**
//     * @param obj        used to get Title name
//     * @param direct     first Structure instance
//     * @param modele     xmax Structure instance
//     * @param axisList   X and Y axis
//     * @param parameters parameters (Y_* classes)
//     */
//    public void run(Object obj, ConsoleAwareObject direct, ConsoleAwareObject modele, ConsoleAxisList axisList, ParamSet... parameters) {
//        run(obj.getClass().getSimpleName(), direct, modele, axisList, parameters);
//    }
//
//    public void run(String windowTitle, ConsoleAwareObject direct, ConsoleAwareObject modele, ConsoleAxisList axisList, ParamSet... parameters) {
//        run(new PlotValue(windowTitle, direct, modele, axisList, parameters));
//    }
    public ProgressMonitor logger(String title) {
        ProgressMonitor logger = ProgressMonitors.logger(5 * 1000);
        getTaskMonitor().addTask(logger, title, null);
        return logger;
    }

    public net.thevpc.scholar.hadruplot.console.PlotDataBuilder createPlot() {
        return new net.thevpc.scholar.hadruplot.console.PlotDataBuilder(this);
    }

    public net.thevpc.scholar.hadruplot.console.PlotDataBuilder parametrizedPlotter() {
        return new PlotDataBuilder(this);
    }

    public PlotConsole setDefaultPlotManager() {
        Plot.Config.setManager(this);
        return this;
    }

    public PlotConsole setGlobal() {
        setDefaultPlotManager();
        setDefaultWindowManager();
        return this;
    }

    public PlotConsole setDefaultWindowManager() {
        Plot.setDefaultWindowManager(this);
        return this;
    }

    public PlotBuilder Plot() {
        return newPlot();
    }

    public PlotBuilder newPlot() {
        return new PlotBuilder()
                //                .cd("/" + Plot.Config.getDefaultWindowTitle())
                .windowManager(windowManager) /*.display(false).plotBuilderListener(new PlotBuilderListener() {
            @Override
            public void onPlot(PlotComponent component, PlotBuilder builder) {
                display(component);
            }
        })*/;
    }

    public void addPlotConsoleListener(WindowInfoListener listener) {
        getPlotConsoleFrame().addPlotConsoleListener(listener);
    }

    public void removePlotConsoleListener(WindowInfoListener listener) {
        getPlotConsoleFrame().removePlotConsoleListener(listener);
    }

    /**
     * <pre>
     *  new PlotValue()
     *  .setStructure(mySource)
     *  .setX(MomConsoleFactory.params.xy(3000, 100))
     *
     *
     *  //.addY(PlotAxisFactory.current2D(Axis.X))
     *  //   .addY(PlotAxisFactory.testField3D(Axis.X))
     *  .addY(MomConsoleFactory.axis.current3D())
     *  .addY(MomConsoleFactory.axis.testFunctions())
     *  .addY(MomConsoleFactory.axis.electricField3D())
     * </pre>
     *
     * @param data
     */
    public void run(PlotData data) {
        start();
        PlotData plotData = data.clone();
        try {
            getLog().trace("Start Execution : ");
            if (plotData.getStructure() == null && plotData.getStructure2() == null) {
                getLog().error("MomStructure is Missing. please call setStructure/setStructure2 methods.");
                return;
            }
            ConsoleAwareObject direct = plotData.getStructure() != null ? plotData.getStructure() : null;
            if (direct == null) {
                throw new IllegalArgumentException("Missing reference object");
//                MomStructure str = new MomStructure();
//                str.getPersistenceCache().setRootFolder(getCurrentDirectory());
//                direct = str;
            }
            direct.setLog(getLog());
            getLog().trace("Reference Data (1) : ");
            getLog().trace("-----------------");
            getLog().trace(direct.dump());
            if (plotData.getStructure2() != null) {
                plotData.getStructure2().setLog(getLog());
                getLog().trace("Second Data (2) : ");
                getLog().trace("-----------------");
                getLog().trace(plotData.getStructure2().dump());
            } else {
                getLog().trace("Second Data (2) : ");
                getLog().trace("-----------------");
                getLog().trace("<NOT SPECIFIED>");
            }
            int maxIterations = 1;
            ParamSet[] params0 = plotData.getParams();
            for (ParamSet param : params0) {
                maxIterations *= param.getSize();
                param.reset();
                param.next();
                getLog().trace("Setting Global ParamSet : " + param.getTitle());
                param.setParameter(direct);
                if (plotData.getStructure2() != null) {
                    param.setParameter(plotData.getStructure2());
                }
                param.reset();
            }
            for (int i = 0; i < params0.length / 2; i++) {
                ParamSet x = params0[i];
                params0[i] = params0[params0.length - i - 1];
                params0[params0.length - i - 1] = x;
            }
            int level = params0.length - 1;
            getPlotConsoleFrame().setGlobalInfo(plotData.getWindowTitle() + " [" + maxIterations + "]");
            boolean firstShow = true;
            if (params0.length == 0) {
                ComputeTitle serieTitle = new ComputeTitle(false);
                for (ParamSet param : params0) {
                    if (disposing) {
                        return;
                    }
                    serieTitle.add(param);
                }

                for (ConsoleAxis consoleAxise : plotData.getAxisList()) {
                    if (disposing) {
                        return;
                    }
                    if (consoleAxise.getX() != null) {
                        getLog().trace("Setting X : " + consoleAxise.getX().getTitle());
                    }
                    for (PlotAxis yParam : consoleAxise.getY()) {
                        if (disposing) {
                            return;
                        }
                        getLog().trace("Start Running : " + yParam.getName(serieTitle));
                        getLog().trace("Setting Y : " + yParam);
                        PlotThread plotThread = new PlotThread(
                                yParam.clone(),
                                plotData.clone(),
                                serieTitle,
                                (direct == null ? null : direct.clone()),
                                (plotData.getStructure2() == null ? null : plotData.getStructure2().clone()),
                                consoleAxise,
                                this
                        );
                        getTaskMonitor().addTask(plotThread, plotThread.getWindowTitle(), plotThread.getSerieTitle().toString());
                        plotThread.start();
                        if (disposing) {
                            return;
                        }
                        getTaskMonitor().waitForTask();
                        ticMonitor();
                    }
                }
                if (firstShow) {
                    firstShow = false;
                }
            } else {
                while (level < params0.length) {
                    if (disposing) {
                        return;
                    }
                    if (!params0[level].hasNext()) {
                        level++;
                    } else {
                        params0[level].next();
                        getLog().trace("Setting ParamSet : " + params0[level].getTitle());
                        params0[level].setParameter(direct);
                        if (plotData.getStructure2() != null) {
                            params0[level].setParameter(plotData.getStructure2());
                        }
                        if (level > 0) {
                            level--;
                            params0[level].reset();
                            //                    logln("Reset ParamSet : " + params0[level]);
                        } else {
                            ComputeTitle serieTitle = new ComputeTitle(false);
                            for (ParamSet param : params0) {
                                if (disposing) {
                                    return;
                                }
                                serieTitle.add(param);
                            }

                            for (ConsoleAxis consoleAxise : plotData.getAxisList()) {
                                if (disposing) {
                                    return;
                                }
                                if (consoleAxise.getX() == null) {
                                    getLog().trace("No X to Set!");
                                } else {
                                    getLog().trace("Setting X : " + consoleAxise.getX().getTitle());
                                }
                                for (PlotAxis yParam : consoleAxise.getY()) {
                                    if (disposing) {
                                        return;
                                    }
                                    getLog().trace("Start Running : " + yParam.getName(serieTitle));
                                    getLog().trace("Setting Y : " + yParam);
                                    ConsoleAwareObject directClone = (direct == null ? null : direct.clone());
                                    if (directClone != null) {
                                        for (PlotConsoleCacheSupport plotConsoleCacheSupport : plotConsoleCacheSupports) {
                                            plotConsoleCacheSupport.prepareObject(directClone, "Direct", serieTitle.toString());
                                        }
                                    }
                                    ConsoleAwareObject modelClone = (plotData.getStructure2() == null ? null : plotData.getStructure2().clone());
                                    if (modelClone != null) {
                                        for (PlotConsoleCacheSupport plotConsoleCacheSupport : plotConsoleCacheSupports) {
                                            plotConsoleCacheSupport.prepareObject(modelClone, "Model", serieTitle.toString());
                                        }
                                    }
                                    PlotThread plotThread = new PlotThread(
                                            yParam.clone(),
                                            plotData.clone(),
                                            serieTitle,
                                            directClone,
                                            modelClone,
                                            consoleAxise,
                                            this
                                    );
                                    getTaskMonitor().addTask(plotThread, plotThread.getWindowTitle(), plotThread.getSerieTitle().toString());
                                    plotThread.start();
                                    if (disposing) {
                                        return;
                                    }
                                    getTaskMonitor().waitForTask();
                                    ticMonitor();
                                }
                            }
                            if (firstShow) {
                                firstShow = false;
                            }
                        }
                    }
                }
            }
            ticMonitor();
        } finally {
        }
    }

    public synchronized PlotConsoleFrame show() {
        return getPlotConsoleFrame();
    }

    public synchronized PlotConsoleFrame getPlotConsoleFrame() {
        if (plotConsoleFrame == null) {
//            plotConsoleFrame = new DefaultPlotConsoleFrame(this, getFrameTitle());
            plotConsoleFrame = new DefaultPlotConsoleFrame2(this, getFrameTitle());
            plotConsoleFrame.setDefaultCloseOperation(closeOption);
            ServiceLoader<PlotConsoleTool> sl = ServiceLoader.load(PlotConsoleTool.class);
            for (PlotConsoleTool plotConsoleTool : sl) {
                addTool(plotConsoleTool);
            }
        }
        return plotConsoleFrame;
    }

    public void addTool(PlotConsoleTool tool) {
        WindowInfo info = new WindowInfo().setTitle(tool.getTitle())
                .setResizable(true).setClosable(true)
                .setMaximizable(true).setIconifiable(true)
                .setComponent(new JScrollPane(tool.toComponent()))
                .setFrameIcon(
                        SwingUtilities3.getScaledIcon(tool.getIconURL(),
                                16, 16
                        )
                );
//            JInternalFrame f = new JInternalFrame("Locks Monitor", true, true, true, true);
//            f.add(new JScrollPane(lockMonitor));
//            f.setPreferredSize(new Dimension(600, 300));
//            f.pack();
//            f.setVisible(true);
//            try {
//                f.setIcon(false);
//            } catch (PropertyVetoException e) {
//                e.printStackTrace();
//            }
        tool.onInstall(this, getPlotConsoleFrame().addToolsFrame(info));
    }

    public void setCloseOption(CloseOption closeOption) {
        this.closeOption = (closeOption == null) ? CloseOption.EXIT : closeOption;
        if (plotConsoleFrame != null) {
            plotConsoleFrame.setDefaultCloseOperation(closeOption);
        }
    }

    public TaskMonitorManager taskMonitor() {
        return getTaskMonitor();
    }

    public TaskMonitorManager getTaskMonitor() {
        if (taskMonitorComponent == null) {
            if (taskMonitor == null) {
                taskMonitor = new DefaultTaskMonitorManager();
            }
            taskMonitorComponent = new TaskMonitorManagerComponent(taskMonitor);
            JScrollPane comp = new JScrollPane(taskMonitorComponent);
            WindowInfo info = new WindowInfo().setTitle("TaskMonitor Monitor").setResizable(true).setClosable(true).setMaximizable(true).setIconifiable(true)
                    .setFrameIcon(SwingUtilities3.getScaledIcon(
                            PlotConsole.class.getResource("Tasks.png"),
                            16, 16
                    ))
                    .setComponent(comp);

            //JInternalFrame f = new JInternalFrame("TaskMonitor Monitor", true, true, true, true);
            //f.add(comp);
            //f.setPreferredSize(new Dimension(600, 300));
            //f.pack();
//            f.setVisible(true);
//            try {
//                f.setIcon(false);
//            } catch (PropertyVetoException e) {
//                e.printStackTrace();
//            }
            taskMonitorComponent.setFrame(getPlotConsoleFrame().addToolsFrame(info));
            taskMonitorComponent.getFrame().setClosed(true);
        }
        return taskMonitor;
    }

    protected LogAreaComponent getLogArea() {
        if (logger == null) {
            logger = new LogAreaComponent();
            WindowInfo info = new WindowInfo().setTitle("Log").setResizable(true).setClosable(true).setMaximizable(true).setIconifiable(true)
                    .setComponent(new JScrollPane(logger.toComponent()))
                    .setFrameIcon(
                            SwingUtilities3.getScaledIcon(
                                    PlotConsole.class.getResource("Log.png"),
                                    16, 16
                            )
                    );
//            JInternalFrame f = new JInternalFrame("Log", true, false, true, true);
//            f.add(new JScrollPane(logger.toComponent()));
//            f.setPreferredSize(new Dimension(400, 300));
//            f.pack();
//            f.setVisible(true);
//            try {
//                f.setIcon(false);
//            } catch (PropertyVetoException e) {
//                e.printStackTrace();
//            }
            JInternalFrame f = getPlotConsoleFrame().addToolsFrame(info);
            JInternalFrameHelper h = new JInternalFrameHelper(f);
            h.setClosed(true);
        }
        return logger;
    }

    public PlotConsole run(final ConsoleAction action) {
        if (disposing) {
            System.out.println("action after dispose ... " + action);
            throw new ConsoleDisposingException("action after dispose ... " + action);
        }
        if (!readOnly && isAutoSave()) {
            synchronized (autoSaving) {
                try {
                    if (autoSavingFileOutputStream == null) {
                        autoSavingFileOutputStream = new ObjectOutputStream(new FileOutputStream(getCurrentAutoSavingFile()));
                    }
                    autoSavingFileOutputStream.writeObject(action);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        SwingUtilities3.invokeLater(new Runnable() {
            public void run() {
                action.execute(PlotConsole.this);
            }
        });
        return this;
    }

    public PlotConsole loadFiles(File[] files) throws IOException {
        boolean first = true;
        String ext = null;
        for (File file : files) {
            String e = FileUtils.getFileExtension(file).toLowerCase();
            if (first) {
                first = false;
                ext = e;
            } else {
                if (e != null && e.length() > 0 && e.equals(ext)) {
                    //ok
                } else {
                    ext = null;
                    break;
                }
            }
        }
        if (ext == null) {
            for (File file : files) {
                loadFile(file);
            }
        } else {
            if (ext.equals(PLOT_CONSOLE_FILE_EXTENSION)) {
                for (File file : files) {
                    loadFile(file);
                }
            } else if (Plot.acceptExtension(ext)) {
                for (File file : files) {
                    loadFile(file);
                }
            } else {
                for (PlotConsoleFileSupport plotConsoleFileSupport : fileSupportList) {
                    if (ext.equals(plotConsoleFileSupport.getFileExtension())) {
                        ConsoleAction a = null;
                        try {
                            a = plotConsoleFileSupport.createLoadAction(files);
                        } catch (UnsupportedOperationException ex) {
                            //ignore
                        }
                        if (a != null) {
                            return run(a);
                        }
                    }
                }
                throw new IOException("Unsupported File Extension " + ext);
            }
        }
        return this;
    }

    public PlotConsole loadFile(File file) throws IOException {
        String e = FileUtils.getFileExtension(file).toLowerCase();
        if (e.equals(PLOT_CONSOLE_FILE_EXTENSION)) {
            loadConsole(file);
        } else if (Plot.acceptFileByExtension(file)) {
            run(new ConsoleActionPlotFile(file));
        } else {
            for (PlotConsoleFileSupport plotConsoleFileSupport : fileSupportList) {
                if (e.equals(plotConsoleFileSupport.getFileExtension())) {
                    return run(plotConsoleFileSupport.createLoadAction(file));
                }
            }
            throw new IOException("Unsupported File Extension " + e);
        }
        return this;
    }

    public PlotConsole loadConsole(File file) throws IOException {
        silentSetFrameTitle(file.getName());
        setCurrentDirectory(file.getParentFile());
        ObjectInputStream oos = null;
        try {
            oos = new ObjectInputStream(new FileInputStream(file));
            PlotConsole con = startTime == 0 ? this : new PlotConsole(file.getParentFile());
            con.currentAutoSavingFile = file;
            con.readOnly = true;
            Object obj;
            while ((obj = oos.readObject()) != null) {
                if (obj instanceof Collection) {
                    Collection<ConsoleAction> actions = (Collection<ConsoleAction>) obj;
                    for (ConsoleAction action : actions) {
                        con.run(action);//backword compatibility
                    }
                } else {
                    con.run((ConsoleAction) obj);//backword compatibility
                }
            }
        } catch (EOFException e) {
            //ok end reached
        } catch (IOException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            if (oos != null) {
                oos.close();
            }
        }
        return this;
    }

    public PlotConsole setWindowTitle(String windowTitle) {
        return run(new WindowTitleConsoleAction(windowTitle));
    }

    public ConsoleLogger getLog() {
        return log;
    }

    public void ticMonitor() {
        getTaskMonitor().ticMonitor();
//        SwingUtilities.invokeLater(
//                new Runnable() {
//                    public void run() {
//                        if (globalChronometer != null) {
//                            getPlotConsoleFrame().setGlobalProgress(globalProgressIndex, globalChronometer.getTime());
//                        }
//                    }
//                }
//        );
    }

    public long getProgressPeriod() {
        return progressPeriod;
    }

    public PlotConsole setProgressPeriod(long progressPeriod) {
        this.progressPeriod = progressPeriod;
        return this;
    }

    public String getAutoSavingFilePattern() {
        return autoSavingFilePattern;
    }

    public PlotConsole setAutoSavingFilePattern(String autoSavingFilePattern) {
        if (this.autoSavingFilePattern != null && !this.autoSavingFilePattern.equals(autoSavingFilePattern)) {
            disposeAutoSave();
        }
        this.autoSavingFilePattern = autoSavingFilePattern;
        return this;
    }

    public File getCurrentAutoSavingFile() {
        if (currentAutoSavingFile == null) {
            String file = this.autoSavingFilePattern;
            if (file == null) {
                file = (getClass().getSimpleName() + "-" + sdf.format(new Date()));
            }
            if (!file.toLowerCase().endsWith("." + PlotConsole.PLOT_CONSOLE_FILE_EXTENSION)) {
                file = file + "." + PlotConsole.PLOT_CONSOLE_FILE_EXTENSION;
            }
            Map<String, String> props = new HashMap<String, String>();
            props.put("classname", getClass().getSimpleName());
            props.put("title", getFrameTitle());
            props.put("date", sdf.format(new Date()));
            for (Map.Entry<String, String> entry : props.entrySet()) {
                String rid = "\\{" + entry.getKey() + "\\}";
                String id = "{" + entry.getKey() + "}";
                if (file.contains(id)) {
                    file = file.replaceAll(rid, entry.getValue());
                }
            }
            currentAutoSavingFile = new File(getCurrentDirectory(), file);
        }
        return currentAutoSavingFile;
    }

    public File getCurrentDirectory() {
        return currentDirectory;
    }

    public PlotConsole setCurrentDirectory(File currentDirectory) {
        this.currentDirectory = currentDirectory;
        return this;
    }

    public void silentSetWindowTitle(String windowTitle) {
        //this.windowTitle = windowTitle;
    }

    public void silentLogLn(final String msg) {
        getLogArea().append(msg);
        //System.out.println("[LOG] " + msg);
    }

    public long getStartTime() {
        return startTime;
    }

    public boolean isDisposing() {
        return disposing;
    }

    public PlotConsole setDisposing(boolean disposing) {
        this.disposing = disposing;
        return this;
    }

    public String getFrameTitle() {
        return "Hadhrumaths Console :: " + frameTitle;
    }

    public void silentSetFrameTitle(String frameTitle) {
        if (frameTitle == null) {
            frameTitle = getClass().getSimpleName();
        }
        this.frameTitle = frameTitle;
        if (plotConsoleFrame != null) {
            plotConsoleFrame.setTitle(getFrameTitle());
        }
    }

    public boolean isAutoSave() {
        return autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    public PlotConsole updateAutoSave(boolean autoSavingEnabled) {
        setAutoSave(autoSavingEnabled);
        return this;
    }

    public PlotConsole updateCloseOption(CloseOption closeOption) {
        setCloseOption(closeOption);
        return this;
    }

    @Override
    public PlotWindowManager getPlotWindowManager() {
        return getWindowManager();
    }

    public PlotConsoleWindowManager getWindowManager() {
        return windowManager;
    }

    @Override
    public void display(PlotComponent plotComponent) {
        run(new ConsoleActionPlotComponent(plotComponent,
                plotComponent == null ? null
                        : PlotPath.of(plotComponent.getLayoutConstraints()
                        )
        ));
    }

    public void display(PlotComponent plotComponent, PlotPath path) {
        run(new ConsoleActionPlotComponent(plotComponent, path));
    }

    protected void displayImpl(PlotComponent component, PlotPath path) {
        JComponent plotComponent = component.toComponent();
        if (path == null) {
            path = PlotPath.ROOT;
        }
        if (plotComponent != null) {

            WindowPath preferredPath = new WindowPath(path.isRoot() ? "NoName" : path.get(0));
            if (!path.isRoot()) {
                path = path.removeFirst();
            }
            String plotGroup = component.getPlotTitle();
            if (plotGroup == null) {
                plotGroup = "";
            }
            if (plotGroup.isEmpty()) {
                plotGroup = "Plot";
            }
            ConsoleWindow window = getPlotConsoleFrame().getWindow(preferredPath);
            window.addChild(path.removeFirst(), component.toComponent());
//            JComponent jcomponent = window.getComponent();
//            if (jcomponent == null) {
//                jcomponent = new JListCardPanel();
//                window.setComponent(jcomponent);
//            }
//            JListCardPanel list = (JListCardPanel) jcomponent;
//            JComponent pageComponent = list.getPageComponent(plotGroup);
//            if (pageComponent == null) {
//                pageComponent = plotComponent;
//                window.addChild(plotGroup, pageComponent);
//            }
        }
    }

    public PlotConsoleFileSupport[] getPlotConsoleFileSupports() {
        return fileSupportList.toArray(new PlotConsoleFileSupport[0]);
    }

    public PlotConsole addFileSupport(PlotConsoleFileSupport fileSupport) {
        fileSupportList.add(fileSupport);
        return this;
    }

    public PlotConsole removeFileSupport(PlotConsoleFileSupport fileSupport) {
        fileSupportList.remove(fileSupport);
        return this;
    }

    public PlotConsoleMenuItem[] getMenus() {
        return menus.toArray(new PlotConsoleMenuItem[0]);
    }

    public PlotConsole addMenu(PlotConsoleMenuItem fileSupport) {
        menus.add(fileSupport);
        return this;
    }

    public PlotConsole removeMenu(PlotConsoleMenuItem fileSupport) {
        menus.remove(fileSupport);
        return this;
    }

    @Override
    public PlotBuilder createPlotBuilder() {
        return newPlot();
    }

    @Override
    public ProgressMonitor createMonitor(String name, String description) {
        return taskMonitor().createMonitor(name, description);
    }

    //    public JInternalFrameHelper getLogFrame() {
//        getLogArea();
//        return new JInternalFrameHelper(logFrame);
//    }
}
