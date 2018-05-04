package net.vpc.scholar.hadrumaths.plot.console;

import net.vpc.scholar.hadrumaths.Chronometer;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Plot;
import net.vpc.scholar.hadrumaths.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.cache.CacheAware;
import net.vpc.scholar.hadrumaths.plot.PlotBuilder;
import net.vpc.scholar.hadrumaths.plot.PlotComponent;
import net.vpc.scholar.hadrumaths.plot.console.params.ParamSet;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.PlotAxis;
import net.vpc.scholar.hadrumaths.util.*;
import net.vpc.scholar.hadrumaths.util.swingext.ExtensionFileChooserFilter;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class PlotConsole implements PlotComponentDisplayer {
    //    private JFrame currentFrame;
    public static int debugFramesCount = 0;
    public static String PLOT_CONSOLE_FILE_EXTENSION = "plotconsole";
    public static String PLOT_CONSOLE_FILE_DESC = "Java Plot Console";
    public static SimpleDateFormat FILE_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmss");
    public static ExtensionFileChooserFilter CHOOSER_FILTER = new ExtensionFileChooserFilter(PLOT_CONSOLE_FILE_EXTENSION, PLOT_CONSOLE_FILE_DESC);
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
    private final Object autoSaving = new Object();
    boolean disposing;
    private long startTime = 0;
    //    private Plot currentCourbePlot;
    //    private StringBuffer xtitle = new StringBuffer();
    //    private StringBuffer ytitle = new StringBuffer();
    private String frameTitle = "NO_NAME";
    //    private String windowTitle;
//    private ArrayList<ParamSet> params = new ArrayList<ParamSet>();
    //    private final Collection<ConsoleAction> actions = new Vector<ConsoleAction>();
//    private AbstractStructure2D direct;
    //    private AbstractStructure2D modele;
    //    private Hashtable<String, JInternalFrame> frames;
    //    private Hashtable<String, Plot> plots;
    //    private Hashtable<String, JCardPanel> cards;
    private Chronometer globalChronometer;
    private int globalProgressIndex;
    private JTextArea logger;
    private JInternalFrame logFrame;
    private TLog log = new ConsoleLogger(this);
    private PlotConsoleFrame plotConsoleFrame;
    private CloseOption closeOption = CloseOption.EXIT;
    private TaskMonitor taskMonitor;
    private JInternalFrame taskMonitorFrame;
//    private PlotAxis currentY = null;
    private LockMonitor lockMonitor;
    private JInternalFrame lockMonitorFrame;
    private boolean readOnly = false;
    private String autoSavingFilePattern;
    private File currentAutoSavingFile;
    private ObjectOutputStream autoSavingFileOutputStream;
    private boolean autoSave = false;
    private long progressPeriod = 2000;
    private File currentDirectory = null;
    private ProgressMonitorThread progressMonitorThread;
    private boolean cacheByIteration = false;
    private HFile cachePrefix = null;
    private PlotConsoleWindowManager windowManager = null;
    private List<PlotConsoleFileSupport> fileSupportList = new ArrayList<>();
    private List<PlotConsoleMenuItem> menus = new ArrayList<>();

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
        silentSetFrameTitle(title);
        setAutoSavingFilePattern("{classname}-{date}");
    }

    public PlotConsole start() {
        if (startTime == 0) {
            getLog().trace("Start Project " + getFrameTitle());
            getTaskMonitor();
            getLog();
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
        if (taskMonitor != null) {
            taskMonitor.killAll();
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

    public JInternalFrame getTaskMonitorFrame() {
        getTaskMonitor();
        return taskMonitorFrame;
    }

    public JInternalFrame getLockMonitorFrame() {
        getLockMonitor();
        return lockMonitorFrame;
    }

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
//        run(new PlotData(windowTitle, direct, modele, axisList, parameters));
//    }

    public EnhancedProgressMonitor logger(String title) {
        EnhancedProgressMonitor logger = ProgressMonitorFactory.logger(5 * Maths.SECOND);
        getTaskMonitor().addTask(logger, title, null);
        return logger;
    }

    public PlotDataBuilder createPlot() {
        return new PlotDataBuilder(this);
    }

    public PlotDataBuilder parametrizedPlotter() {
        return new PlotDataBuilder(this);
    }

    public PlotBuilder plotter() {
        return new PlotBuilder().windowManager(windowManager)
                /*.display(false).plotBuilderListener(new PlotBuilderListener() {
            @Override
            public void onPlot(PlotComponent component, PlotBuilder builder) {
                display(component);
            }
        })*/;
    }

    /**
     * <pre>
     *  new PlotData()
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
            getPlotConsoleFrame().setGlobalInfo(plotData.getWindowTitle(), maxIterations);
            getPlotConsoleFrame().setGlobalProgress(0, -1);
            globalChronometer = new Chronometer();
            globalChronometer.start();
//        ProgressMonitor monitor = new ProgressMonitor(null, "Progression", null, 0, maxIterations);
            globalProgressIndex = 0;
            boolean firstShow = true;
            if (params0.length == 0) {
                ComputeTitle serieTitle = new ComputeTitle(false);
                for (ParamSet param : params0) {
                    if (disposing) {
                        return;
                    }
                    serieTitle.add(param);
                }

                getPlotConsoleFrame().setGlobalProgress(++globalProgressIndex, globalChronometer.getTime());
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
                        getTaskMonitor().addTask(new PlotThread(
                                yParam.clone(),
                                plotData.clone(),
                                serieTitle,
                                (direct == null ? null : direct.clone()),
                                (plotData.getStructure2() == null ? null : plotData.getStructure2().clone()),
                                consoleAxise,
                                this
                        ));
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

                            getPlotConsoleFrame().setGlobalProgress(++globalProgressIndex, globalChronometer.getTime());
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
                                        if (cachePrefix != null) {
                                            if (cacheByIteration) {
                                                if (directClone instanceof CacheAware) {
                                                    ((CacheAware) directClone).getCacheConfig().setCacheBaseFolder(new HFile(cachePrefix, "/Direct/" + serieTitle.toString()));
                                                }
                                            } else {
                                                if (directClone instanceof CacheAware) {
                                                    ((CacheAware) directClone).getCacheConfig().setCacheBaseFolder(new HFile(cachePrefix, "/Direct"));
                                                }
                                            }
                                        }
                                    }
                                    ConsoleAwareObject modelClone = (plotData.getStructure2() == null ? null : plotData.getStructure2().clone());
                                    if (modelClone != null) {
                                        if (cachePrefix != null) {
                                            if (cacheByIteration) {
                                                if (modelClone instanceof CacheAware) {
                                                    ((CacheAware) modelClone).getCacheConfig().setCacheBaseFolder(new HFile(cachePrefix, "/Direct/" + serieTitle.toString()));
                                                }
                                            } else {
                                                if (modelClone instanceof CacheAware) {
                                                    ((CacheAware) modelClone).getCacheConfig().setCacheBaseFolder(new HFile(cachePrefix, "/Direct"));
                                                }
                                            }
                                        }
                                    }
                                    getTaskMonitor().addTask(new PlotThread(
                                            yParam.clone(),
                                            plotData.clone(),
                                            serieTitle,
                                            directClone,
                                            modelClone,
                                            consoleAxise,
                                            this
                                    ));
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
            globalChronometer.stop();
            getLog().trace("End Execution [" + globalChronometer + "]");
            ticMonitor();
        } finally {
            globalChronometer = null;
        }
    }

    public synchronized PlotConsoleFrame show() {
        return getPlotConsoleFrame();
    }

    public synchronized PlotConsoleFrame getPlotConsoleFrame() {
        if (plotConsoleFrame == null) {
            plotConsoleFrame = new PlotConsoleFrame(this, getFrameTitle());
            plotConsoleFrame.prepare();
            plotConsoleFrame.setVisible(true);
            if (closeOption == null) {
                closeOption = CloseOption.EXIT;
            }
            switch (closeOption) {
                case EXIT: {
                    plotConsoleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    break;
                }
                case CLOSE: {
                    plotConsoleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    break;
                }
                case NOTHING: {
                    plotConsoleFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    break;
                }
            }
        }
        return plotConsoleFrame;
    }

    public void setCloseOption(CloseOption closeOption) {
        this.closeOption = (closeOption == null) ? CloseOption.EXIT : closeOption;
        if (plotConsoleFrame != null) {
            switch (closeOption) {
                case EXIT: {
                    plotConsoleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    break;
                }
                case CLOSE: {
                    plotConsoleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    break;
                }
                case NOTHING: {
                    plotConsoleFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    break;
                }
            }
        }
    }

    public TaskMonitor getTaskMonitor() {
        if (taskMonitor == null) {
            taskMonitor = new TaskMonitor(this);
            JInternalFrame f = new JInternalFrame("Task Monitor", true, true, true, true);
            f.add(new JScrollPane(taskMonitor));
            f.setPreferredSize(new Dimension(600, 300));
            f.pack();
            f.setVisible(true);
            try {
                f.setIcon(false);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }
            taskMonitor.setFrame(f);
            getPlotConsoleFrame().addToolsFrame(taskMonitorFrame=f);
        }
        return taskMonitor;
    }

    public LockMonitor getLockMonitor() {
        if (lockMonitor == null) {
            lockMonitor = new LockMonitor(this);
            JInternalFrame f = new JInternalFrame("Locks Monitor", true, true, true, true);
            f.add(new JScrollPane(lockMonitor));
            f.setPreferredSize(new Dimension(600, 300));
            f.pack();
            f.setVisible(true);
            try {
                f.setIcon(false);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }
            lockMonitor.setFrame(f);
            getPlotConsoleFrame().addToolsFrame(lockMonitorFrame=f);
        }
        return lockMonitor;
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
        SwingUtils.invokeLater(new Runnable() {
            public void run() {
                action.execute(PlotConsole.this);
            }
        });
        return this;
    }

    public PlotConsole loadFiles(File[] files) throws IOException {
        boolean first=true;
        String ext=null;
        for (File file : files) {
            String e = IOUtils.getFileExtension(file).toLowerCase();
            if(first){
                first=false;
                ext=e;
            }else{
                if(e!=null && e.length()>0  && e.equals(ext)){
                    //ok
                }else{
                    ext=null;
                    break;
                }
            }
        }
        if(ext==null){
            for (File file : files) {
                loadFile(file);
            }
        }else{
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
                    if(ext.equals(plotConsoleFileSupport.getFileExtension())){
                        return run(plotConsoleFileSupport.createLoadAction(files));
                    }
                }
                throw new IOException("Unsupported File Extension " + ext);
            }
        }
        return this;
    }

    public PlotConsole loadFile(File file) throws IOException {
        String e = IOUtils.getFileExtension(file).toLowerCase();
        if (e.equals(PLOT_CONSOLE_FILE_EXTENSION)) {
            loadConsole(file);
        } else if (Plot.acceptFileByExtension(file)) {
            run(new ConsoleActionPlotFile(file));
        } else {
            for (PlotConsoleFileSupport plotConsoleFileSupport : fileSupportList) {
                if(e.equals(plotConsoleFileSupport.getFileExtension())){
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


    public TLog getLog() {
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

    public JTextArea getLogArea() {
        if (logger == null) {
            logger = new JTextArea();
            logger.setFont(new Font("Monospaced", 0, 12));
            logger.setEditable(false);

            JInternalFrame f = new JInternalFrame("Log", true, false, true, true);
            f.add(new JScrollPane(logger));
            f.setPreferredSize(new Dimension(400, 300));
            f.pack();
            f.setVisible(true);
            try {
                f.setIcon(false);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }
            getPlotConsoleFrame().addToolsFrame(logFrame=f);
        }
        return logger;
    }
    public void silentLogLn(final String msg) {

        SwingUtils.invokeLater(new Runnable() {
            public void run() {
                getLogArea().append(msg + "\n");
            }
        });
        System.out.println("[LOG] " + msg);
    }


    public long getStartTime() {
        return startTime;
    }

    public boolean isDisposing() {
        return disposing;
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

    public boolean isCacheByIteration() {
        return cacheByIteration;
    }

    public void setCacheByIteration(boolean cacheByIteration) {
        this.cacheByIteration = cacheByIteration;
    }

    public PlotConsole updateCacheByIteration(boolean cacheByIteration) {
        setCacheByIteration(cacheByIteration);
        return this;
    }

    public HFile getCachePrefix() {
        return cachePrefix;
    }

    public void setCachePrefix(HFile cachePrefix) {
        this.cachePrefix = cachePrefix;
    }

    public PlotConsole updateCachePrefix(HFile cachePrefix) {
        setCachePrefix(cachePrefix);
        return this;
    }

    public PlotConsoleWindowManager getWindowManager() {
        return windowManager;
    }

    @Override
    public void display(PlotComponent plotComponent) {
        run(new ConsoleActionPlotComponent(plotComponent,plotComponent==null?null:plotComponent.getLayoutConstraints()));
    }

    public void display(PlotComponent plotComponent,String path) {
        run(new ConsoleActionPlotComponent(plotComponent,path));
    }

    protected void displayImpl(PlotComponent component,String path) {
        JComponent plotComponent = component.toComponent();
        if (plotComponent != null) {

            List<String> pathList = StringUtils.split(path,"/");
            WindowPath preferredPath = new WindowPath(pathList.size()==0?"NoName":pathList.get(0));
            if(pathList.size()>0){
                pathList.remove(0);
            }
            String plotGroup = component.getPlotTitle();
            if (plotGroup == null) {
                plotGroup = "";
            }
            if (plotGroup.isEmpty()) {
                plotGroup = "Plot";
            }
            ConsoleWindow window = getPlotConsoleFrame().getWindow(preferredPath);
            window.addChild(StringUtils.toPath(pathList,"/"),component.toComponent());
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

    public PlotConsoleFileSupport[] getPlotConsoleFileSupports(){
        return fileSupportList.toArray(new PlotConsoleFileSupport[fileSupportList.size()]);
    }

    public PlotConsole addFileSupport(PlotConsoleFileSupport fileSupport){
        fileSupportList.add(fileSupport);
        return this;
    }

    public PlotConsole removeFileSupport(PlotConsoleFileSupport fileSupport){
        fileSupportList.remove(fileSupport);
        return this;
    }

    public PlotConsoleMenuItem[] getMenus(){
        return menus.toArray(new PlotConsoleMenuItem[menus.size()]);
    }

    public PlotConsole addMenu(PlotConsoleMenuItem fileSupport){
        menus.add(fileSupport);
        return this;
    }

    public PlotConsole removeMenu(PlotConsoleMenuItem fileSupport){
        menus.remove(fileSupport);
        return this;
    }

    public JInternalFrame getLogFrame() {
        getLogArea();
        return logFrame;
    }
}
