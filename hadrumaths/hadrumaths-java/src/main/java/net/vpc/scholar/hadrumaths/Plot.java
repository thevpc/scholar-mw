package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.plot.*;
import net.vpc.scholar.hadrumaths.plot.console.PlotComponentDisplayer;
import net.vpc.scholar.hadrumaths.plot.console.PlotConsole;
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductCache;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.util.*;
import net.vpc.scholar.hadrumaths.util.swingext.GridBagLayout2;
import org.jfree.chart.ChartPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Plot {
    /**
     * Java Figure
     */
    public static final String JFIG_FILE_EXTENSION = "jfig";
    /**
     * Java Figure
     */
    public static final String JFIGDATA_FILE_EXTENSION = "jfigdata";
    /**
     * Java Serializable Figure
     */
    public static final String JFIGOBJ_FILE_EXTENSION = "jfigobj";
    /**
     * Java Figure Bundle
     */
    public static final String JFB_FILE_EXTENSION = "jfb";

    private static PlotWindowManager defaultWindowManager = PlotWindowManagerFactory.create();
    private static SimpleDateFormat UNIVERSAL_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static Map<String, PlotComponent> cachedPlotComponent = new LinkedHashMap<>();

    public static PlotContainer create(String container) {
        return getDefaultWindowManager().add(container);
    }

    public static PlotWindowManager getDefaultWindowManager() {
        return defaultWindowManager;
    }

    public static void setDefaultWindowManager(PlotWindowManager windowManager) {
        Plot.defaultWindowManager = windowManager;
    }

    public static PlotContainer addPlotContainer(String name) {
        return getDefaultWindowManager().add(name);
    }

    public static PlotBuilder builder() {
        return new PlotBuilder();
    }

    public static PlotBuilder domain(Domain domain) {
        return builder().domain(domain);
    }

    public static PlotBuilder windowManager(PlotWindowManager windowManager) {
        return builder().windowManager(windowManager);
    }

    public static PlotBuilder xname(String xname) {
        return builder().xname(xname);
    }

    public static PlotBuilder yname(String yname) {
        return builder().yname(yname);
    }

    public static PlotBuilder samples(int xsamples, int ysamples) {
        return builder().samples(xsamples, ysamples);
    }

    public static PlotBuilder samples(int xsamples, int ysamples, int zsample) {
        return builder().samples(xsamples, ysamples, zsample);
    }

    public static PlotBuilder xsamples(double[] values) {
        return builder().xsamples(values);
    }

    public static PlotBuilder ysamples(double[] values) {
        return builder().ysamples(values);
    }

    public static PlotBuilder zsamples(double[] values) {
        return builder().zsamples(values);
    }

    public static PlotBuilder xsamples(int value) {
        return builder().xsamples(value);
    }

    public static PlotBuilder title(String title) {
        return builder().title(title);
    }

    public static PlotBuilder titles(String... titles) {
        return builder().titles(titles);
    }

    public static PlotBuilder nodisplay() {
        return builder().nodisplay();
    }

    public static PlotBuilder display(PlotContainer parent) {
        return builder().display(parent);
    }

    public static PlotBuilder update(PlotComponent component) {
        return builder().update(component);
    }

    public static PlotBuilder update(String component) {
        return builder().update(component);
    }

    public static PlotBuilder display() {
        return builder().display();
    }

    public static PlotBuilder display(boolean display) {
        return builder().display(display);
    }

    public static PlotBuilder converter(ComplexAsDouble converter) {
        return builder().converter(converter);
    }

    public static PlotBuilder asHeatMap() {
        return builder().asHeatMap();
    }

    public static PlotBuilder asMatrix() {
        return builder().asMatrix();
    }

    public static PlotBuilder asMesh() {
        return builder().asMesh();
    }

    public static PlotBuilder asTable() {
        return builder().asTable();
    }

    public static PlotBuilder asCurve() {
        return builder().asCurve();
    }

    public static PlotBuilder asPolar() {
        return builder().asPolar();
    }

    public static PlotBuilder asAbs() {
        return builder().asAbs();
    }

    public static PlotBuilder asImag() {
        return builder().asImag();
    }

    public static PlotBuilder asReal() {
        return builder().asReal();
    }

    public static PlotBuilder asDB() {
        return builder().asDB();
    }

    public static PlotBuilder asDB2() {
        return builder().asDB2();
    }

    public static PlotBuilder asArg() {
        return builder().asArg();
    }

    public static PlotBuilder plotType(PlotType plotType) {
        return builder().plotType(plotType);
    }

    public static PlotComponent plot(Object... obj) {
        return builder().plot(obj);
    }

    public static PlotBuilder ysamples(int value) {
        return builder().ysamples(value);
    }

    public static PlotBuilder zsamples(int value) {
        return builder().zsamples(value);
    }

    public static PlotConsole console() {
        PlotConsole plotConsole = new PlotConsole();
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    plotConsole.show();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return plotConsole;
    }

    public static PlotConsole console(boolean autoSave) {
        return new PlotConsole(autoSave);
    }

    public static PlotConsole console(File folder, boolean autoSave) {
        return new PlotConsole(folder, autoSave);
    }

    public static PlotConsole console(File folder) {
        return new PlotConsole(folder);
    }

    public static PlotConsole console(String title, File folder) {
        return new PlotConsole(title, folder);
    }

    public static void plot(File selectedFile, PlotComponentDisplayer context) throws IOException {
        PlotComponent plot = loadPlot(selectedFile);
//                            frame.getContentPane().setLayout(new BorderLayout());
//                            openFrames++;
//                            frame.addWindowListener(wa);
//                            frame.setTitle(selectedFile.getPath());

        if (context == null) {
            plot.display();
        } else {
            context.display(plot);
        }
    }

    public static PlotComponent loadPlot(File file) throws IOException {

        String e = IOUtils.getFileExtension(file).toLowerCase();
        if (!
                (
                        e.equals(ObjectCache.CACHE_OBJECT_FILE_EXTENSION)
                                || e.equals(JFIG_FILE_EXTENSION)
                                || e.equals(JFIGDATA_FILE_EXTENSION)
                                || e.equals(JFIGOBJ_FILE_EXTENSION)
                                || e.equals(JFB_FILE_EXTENSION)
                )
                ) {
            throw new IOException("Unsupported file extension " + e);
        }
        PlotComponent plot = null;
        if (e.equals(ObjectCache.CACHE_OBJECT_FILE_EXTENSION)) {
            Object o = null;
            try {
                o = IOUtils.loadZippedObject(file.getPath());
            } catch (ClassNotFoundException ee) {
                throw new IOException(ee);
            }
            if (o instanceof Complex) {
                o = Maths.matrix(new Complex[][]{{(Complex) o}});
            } else if (o instanceof Matrix) {
                //let it
            } else if (o instanceof DMatrix) {
                //let it
            } else if (o instanceof Number) {
                o = Maths.matrix(new Complex[][]{{Complex.valueOf(((Number) o).doubleValue())}});
            } else if (o instanceof VDiscrete) {
                o = new VDiscrete[]{(VDiscrete) o};
            } else if (o instanceof Discrete) {
                o = new VDiscrete[]{new VDiscrete((Discrete) o)};
            }
            if (o != null) {
                if (o instanceof Matrix) {
                    plot = Plot.title(file.getName()).asHeatMap().plot((Matrix) o);
                } else if (o instanceof VDiscrete[]) {
                    plot = Plot.create(
                            new VDiscretePlotModel().setVdiscretes((VDiscrete[]) o)
                                    .setTitle(file.getName()), Plot.getDefaultWindowManager());
                } else if (o instanceof DoubleToVector[]) {
                    plot = Plot.nodisplay().title(file.getName()).plot((DoubleToVector[]) o);
                } else if (o instanceof DDx[]) {
                    plot = Plot.nodisplay().title(file.getName()).plot((DDx[]) o);
                } else if (o instanceof DoubleToDouble[]) {
                    plot = Plot.nodisplay().title(file.getName()).plot((DoubleToDouble[]) o);
                } else if (o instanceof DoubleToComplex[]) {
                    plot = Plot.title(file.getName()).asAbs().plot((DoubleToComplex[]) o);
                } else {
                    throw new IllegalArgumentException("Unsupported Type " + o.getClass());
                }
            } else {
                throw new IllegalArgumentException("Unsupported Type " + file);
            }
        } else {
            plot = create(loadPlotModel(file), Plot.getDefaultWindowManager());
        }
//                            frame.getContentPane().setLayout(new BorderLayout());
//                            openFrames++;
//                            frame.addWindowListener(wa);
//                            frame.setTitle(selectedFile.getPath());

        return plot;
    }

    public static PlotPanel create(PlotModel model, PlotWindowManager windowManager) {
        if (model == null) {
            throw new NullPointerException("Null Model");
        }
        if (model instanceof VDiscretePlotModel) {
            return new VDiscretePlotPanel((VDiscretePlotModel) model, windowManager);
        }
        if (model instanceof ExpressionsPlotModel) {
            return new ExpressionsPlotPanel((ExpressionsPlotModel) model, windowManager);
        }
        if (model instanceof ValuesPlotModel) {
            return new ValuesPlotPanel((ValuesPlotModel) model, windowManager);
        }
        throw new IllegalArgumentException("Unsupported Model " + model);
    }

    private static ValuesPlotModel loadDataBundle(File file) throws IOException {
        ZipInputStream in = null;
        ZipEntry ze = null;
//        File ff = null;
        try {
            in = new ZipInputStream(new FileInputStream(file));
            while ((ze = in.getNextEntry()) != null) {
                if ("data.figdata".equals(ze.getName())) {
//                    ff = File.createTempFile("_p_", ".jfig");
//                    FileOutputStream fos = null;
//                    try {
//                        fos = new FileOutputStream(ff);
//                        byte[] buffer = new byte[1024];
//                        int count = 0;
//                        while ((count = in.read(buffer)) > 0) {
//                            fos.write(buffer, 0, count);
//                        }
//                    } finally {
//                        if (fos != null) {
//                            fos.close();
//                        }
//                    }
                    return loadDataJfig(in);
                }
            }
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                //
            }
//            if (ff != null) {
//                ff.delete();
//            }
        }
        throw new UnsupportedOperationException("Unknown file format");
    }

    public static ValuesPlotModel loadDataJfig(File file) throws IOException {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
            return loadDataJfig(fileReader);
        } finally {
            if (fileReader != null) {
                fileReader.close();
            }
        }
    }

    public static ValuesPlotModel loadDataJfig(InputStream reader) throws IOException {
        InputStreamReader streamReader = null;
        try {
            streamReader = new InputStreamReader(reader);
            return loadDataJfig(streamReader);
        } finally {
            if (streamReader != null) {
                streamReader.close();
            }
        }
    }

    public static ValuesPlotModel loadDataJfig(Reader reader) throws IOException {
        ValuesPlotModel m = new ValuesPlotModel();
        BufferedReader br = null;
        try {
            br = (reader instanceof BufferedReader) ? ((BufferedReader) reader) : new BufferedReader(reader);
            String line = null;
            ArrayList<String> ytitlesList = new ArrayList<String>();
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0 || line.startsWith("#")) {
                    continue;
                }
                if (line.startsWith("x =")) {
                    StringBuilder sb = new StringBuilder(line.substring(3).trim()).append("\n");
                    if (sb.toString().trim().length() > 0 && !line.endsWith("]")) {
                        while ((line = br.readLine()) != null) {
                            sb.append(line).append("\n");
                            if (line.endsWith("]")) {
                                break;
                            }
                        }
                    }
                    m.setX(sb.toString().trim().length() == 0 ? null : ArrayUtils.getReal(Maths.matrix(sb.toString()).getArray()));
                } else if (line.startsWith("y =")) {
                    StringBuilder sb = new StringBuilder(line.substring(3).trim()).append("\n");
                    if (sb.toString().trim().length() > 0 && !line.endsWith("]")) {
                        while ((line = br.readLine()) != null) {
                            sb.append(line).append("\n");
                            if (line.endsWith("]")) {
                                break;
                            }
                        }
                    }
                    m.setY(sb.toString().trim().length() == 0 ? null : ArrayUtils.absdbl(Maths.matrix(sb.toString()).getArray()));
                } else if (line.startsWith("z =")) {
                    StringBuilder sb = new StringBuilder(line.substring(3).trim()).append("\n");
                    if (sb.toString().trim().length() > 0 && !line.endsWith("]")) {
                        while ((line = br.readLine()) != null) {
                            sb.append(line).append("\n");
                            if (line.endsWith("]")) {
                                break;
                            }
                        }
                    }

                    m.setZ(sb.toString().trim().length() == 0 ? null : Maths.matrix(sb.toString()).getArray());
                }
                if (line != null) {
                    if (line.startsWith("title =")) {
                        m.setTitle(line.substring("title".length() + 2));
                    }
                    if (line.startsWith("name =")) {
                        m.setTitle(line.substring("name".length() + 2));
                    }
                    if (line.startsWith("xlabel =")) {
                        m.setxTitle(line.substring("xlabel".length() + 2));
                    }

                    if (line.startsWith("ylabel =")) {
                        m.setyTitle(line.substring("ylabel".length() + 2));
                    }

                    if (line.startsWith("plotType =")) {
                        m.setPlotType(PlotType.valueOf(line.substring("plotType".length() + 2)));
                    }

                    if (line.startsWith("zDoubleFunction =")) {
                        m.setZDoubleFunction(ComplexAsDouble.valueOf(line.substring("zDoubleFunction".length() + 2)));
                    }

                    if (line.startsWith("ytitle =")) {
                        ytitlesList.add(line.substring("ytitle".length() + 2));
                    }
                }
            }
            m.setYtitles(ytitlesList.toArray(new String[ytitlesList.size()]));
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return m;
    }

    public static void savePlot(File file, String preferredExtension, PlotModelProvider plotProvider) throws IOException {
        if (StringUtils.isEmpty(preferredExtension)) {
            preferredExtension = IOUtils.getFileExtension(file).toLowerCase();
        } else {
            preferredExtension = preferredExtension.trim().toLowerCase();
            if (StringUtils.isEmpty(preferredExtension)) {
                preferredExtension = JFIGOBJ_FILE_EXTENSION;
            }
        }
        if (!file.getName().toLowerCase().endsWith("." + preferredExtension)) {
            file = new File(file.getPath() + "." + preferredExtension);
        }
        if (preferredExtension.equals("xls") || preferredExtension.equals("csv")) {
            saveDataCSV(file, plotProvider);
        } else if (preferredExtension.equals(JFIGDATA_FILE_EXTENSION) || preferredExtension.equals(JFIG_FILE_EXTENSION)) {
            saveDataFIGDATA(file, plotProvider);
        } else if (preferredExtension.equals("m")) {
            saveDataMATLB(file, plotProvider);
        } else if (preferredExtension.equals("png")) {
            saveImagePNG(file, plotProvider);
        } else if (preferredExtension.equals("jpeg")) {
            saveImageJPEG(file, plotProvider);
        } else if (preferredExtension.equals(JFB_FILE_EXTENSION)) {
            saveDataBundle(file, plotProvider);
        } else if (preferredExtension.equals(JFIGOBJ_FILE_EXTENSION)) {
            saveDataFIGOBJ(file, plotProvider);
        } else {
            saveDataBundle(file, plotProvider);
        }
    }

    private static void saveDataBundle(File file, PlotModelProvider plotProvider) throws IOException {
        File tempFolder = File.createTempFile("_p_", "plot");
        tempFolder.delete();
        tempFolder.mkdirs();
        File file1 = new File(tempFolder, "data.figdata");
        File file2 = new File(tempFolder, "data.csv");
        File file3 = new File(tempFolder, "data.png");
        File[] files = new File[]{file1, file2, file3};
        saveDataFIGDATA(file1, plotProvider);
        saveDataCSV(file2, plotProvider);
        saveImagePNG(file3, plotProvider);
        // These are the files to include in the ZIP file
        String[] filenames = new String[files.length];
        for (int i = 0; i < filenames.length; i++) {
            filenames[i] = files[i].getName();
        }

        byte[] buf = new byte[1024];

        // Create the ZIP file
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(file));

            // Compress the files
            for (int i = 0; i < filenames.length; i++) {
                FileInputStream in = null;
                try {
                    in = new FileInputStream(files[i]);

                    // Add ZIP entry to output stream.
                    out.putNextEntry(new ZipEntry(filenames[i]));

                    // Transfer bytes from the file to the ZIP file
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }

                    // Complete the entry
                    out.closeEntry();
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
                files[i].delete();
            }
        } finally {
            if (out != null) {
                out.close();
            }
        }
        tempFolder.delete();

        // Complete the ZIP file
        out.close();

    }

    public static BufferedImage getImage(PlotModelProvider plotProvider) {
        Component c = plotProvider.getComponent();
        int graphWidth = c.getSize().width;
        int graphHeight = c.getSize().width;
        if (graphWidth > 0 && graphHeight > 0) {
            BufferedImage bi = new BufferedImage(graphWidth, graphHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bi.createGraphics();
            c.paint(g2d);
            return bi;
        } else {
            BufferedImage bi = new BufferedImage(600, 500, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bi.createGraphics();
            c.paint(g2d);
            return bi;
//            return null;
        }
    }

    private static void saveImagePNG(File file, PlotModelProvider plotProvider) throws IOException {
        BufferedImage img = getImage(plotProvider);
        if (img != null) {
            ImageIO.write(img, "JPG", file);
        }
//        if (USER_JFREECHART) {
//            if (mainComponent instanceof ChartPanel) {
//                ChartPanel cp = (ChartPanel) mainComponent;
//                ChartUtilities.saveChartAsPNG(file, cp.getChart(), getWidth(), getHeight());
//            }
//        } else {
//        }
    }

    private static void saveImageJPEG(File file, PlotModelProvider plotProvider) throws IOException {
        BufferedImage img = getImage(plotProvider);
        if (img != null) {
            ImageIO.write(img, "PNG", file);
        }
//        if (USER_JFREECHART) {
//            if (mainComponent instanceof ChartPanel) {
//                ChartPanel cp = (ChartPanel) mainComponent;
//                ChartUtilities.saveChartAsJPEG(file, cp.getChart(), getWidth(), getHeight());
//            }
//        } else {
//        }
    }

    private static void saveDataFIGOBJ(File file, PlotModelProvider plotProvider) throws IOException {
        IOUtils.saveZippedObject(file.getPath(), plotProvider.getModel());
    }

    private static void saveDataFIGDATA(File file, PlotModelProvider plotProvider) throws IOException {
        ValuesPlotModel model = (ValuesPlotModel) plotProvider.getModel();
        String commentChar = "#";
        String endLine = "";
        // matlab file
        if (file.getName().toLowerCase().endsWith(".m")) {
            commentChar = "%";
            endLine = ";";
        }
        PrintStream printStream = null;
        try {
            printStream = new PrintStream(new FileOutputStream(file), true);
            String lineSep = System.getProperty("line.separator");
            printStream.println(commentChar + " Plot data");
            printStream.println(commentChar + " Last modified " + UNIVERSAL_DATE_TIME_FORMAT.format(new Date()));
            printStream.println(commentChar + " " + model.getTitle());
            printStream.println();
            printStream.println();
            printStream.println(((model.getX() == null || model.getX().length == 0 || model.getX()[0] == null) ? ("x =" + lineSep) : (new DMatrix(model.getX()).toString(commentChar, "x"))) + endLine);
            printStream.println();
            printStream.println(((model.getY() == null || model.getY().length == 0 || model.getY()[0] == null) ? ("y =" + lineSep) : (new DMatrix(model.getY()).toString(commentChar, "y"))) + endLine);
            printStream.println();
            printStream.println(((model.getZ() == null || model.getZ().length == 0 || model.getZ()[0] == null) ? ("z =" + lineSep) : (Maths.matrix(model.getZ()).format(commentChar, "z"))) + endLine);
            printStream.println("title =" + (model.getTitle() == null ? "" : model.getTitle()) + endLine);
            printStream.println("xlabel =" + (model.getXtitle() == null ? "" : model.getXtitle()) + endLine);
            printStream.println("ylabel =" + (model.getYtitle() == null ? "" : model.getYtitle()) + endLine);
            printStream.println("zlabel =" + (model.getZtitle() == null ? "" : model.getZtitle()) + endLine);
            printStream.println("plotType =" + model.getPlotType().toString() + endLine);
            printStream.println("zDoubleFunction =" + model.getZDoubleFunction().toString() + endLine);
            String[] ytitles = model.getYtitles();
            if (ytitles != null) {
                for (int i = 0; i < ytitles.length; i++) {
                    printStream.println("ytitle =" + (model.getYtitles()[i] == null ? "" : model.getYtitles()[i]) + endLine);
                }
            }
        } finally {
            if (printStream != null) {
                try {
                    printStream.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private static void saveDataMATLB(File file, PlotModelProvider plotProvider) throws IOException {
        ValuesPlotModel model = (ValuesPlotModel) plotProvider.getModel();
        String commentChar = "%";
        String endLine = ";";
        PrintStream printStream = null;
        try {
            printStream = new PrintStream(new FileOutputStream(file), true);
            String lineSep = System.getProperty("line.separator");
            printStream.println(commentChar + " Plot data");
            printStream.println(commentChar + " Last modified " + UNIVERSAL_DATE_TIME_FORMAT.format(new Date()));
            printStream.println(commentChar + " " + model.getTitle());
            printStream.println();
            printStream.println();
            printStream.println(((model.getX() == null || model.getX().length == 0 || model.getX()[0] == null) ? ("x =" + lineSep) : (new DMatrix(model.getX()).toString(commentChar, "x"))) + endLine);
            printStream.println();
            printStream.println(((model.getY() == null || model.getY().length == 0 || model.getY()[0] == null) ? ("y =" + lineSep) : (new DMatrix(model.getY()).toString(commentChar, "y"))) + endLine);
            printStream.println();
            printStream.println(((model.getZ() == null || model.getZ().length == 0 || model.getZ()[0] == null) ? ("z =" + lineSep) : (Maths.matrix(model.getZ()).format(commentChar, "z"))) + endLine);
//            printStream.println("title =" + (model.getTitle() == null ? "" : model.getTitle()) + endLine);
//            printStream.println("xlabel =" + (model.getXtitle() == null ? "" : model.getXtitle()) + endLine);
//            printStream.println("ylabel =" + (model.getYtitle() == null ? "" : model.getYtitle()) + endLine);
//            printStream.println("zlabel =" + (model.getZtitle() == null ? "" : model.getZtitle()) + endLine);
//            printStream.println("plotType =" + model.getPlotType().toString() + endLine);
//            printStream.println("zDoubleFunction =" + model.getZDoubleFunction().toString() + endLine);
//            String[] ytitles = model.getYtitles();
//            if (ytitles != null) {
//                for (int i = 0; i < ytitles.length; i++) {
//                    printStream.println("ytitle =" + (model.getYtitles()[i] == null ? "" : model.getYtitles()[i]) + endLine);
//                }
//            }
        } finally {
            if (printStream != null) {
                try {
                    printStream.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public PlotBuilder plotBuilderListener(PlotBuilderListener listener){
        return builder().addPlotBuilderListener(listener);
    }



    private static void saveDataCSV(File file, PlotModelProvider plotProvider) throws IOException {
        String endLine = System.getProperty("line.separator");
        String endCell = "\t";
        PrintStream printStream = null;
        try {
            ValuesPlotModel model = (ValuesPlotModel) plotProvider.getModel();
            printStream = new PrintStream(new FileOutputStream(file), true);
            boolean newLine = true;
            if (model.getX() != null && model.getX().length > 0) {
                for (int i = 0; i < model.getX().length; i++) {
                    if (!newLine) {
                        printStream.print(endCell);
                    } else {
                        newLine = false;
                    }
                    printStream.print("x" + (i + 1));
                }
            }
            if (model.getY() != null && model.getY().length > 0) {
                for (int i = 0; i < model.getY().length; i++) {
                    if (!newLine) {
                        printStream.print(endCell);
                    } else {
                        newLine = false;
                    }
                    printStream.print("y" + (i + 1));
                }
            }
            if (model.getZ() != null && model.getZ().length > 0) {
                for (int i = 0; i < model.getZ().length; i++) {
                    if (!newLine) {
                        printStream.print(endCell);
                    } else {
                        newLine = false;
                    }
                    printStream.print("z" + (i + 1));
                    printStream.print(endCell + "|z" + (i + 1) + "|");
                    printStream.print(endCell + "real(z" + (i + 1) + ")");
                    printStream.print(endCell + "imag(z" + (i + 1) + ")");
                }
            }
            if (model.getX() != null && model.getX().length > 0 && model.getX()[0] != null) {
                for (int j = 0; j < model.getX()[0].length; j++) {
                    printStream.print(endLine);
                    newLine = true;
                    if (model.getX() != null && model.getX().length > 0) {
                        for (int i = 0; i < model.getX().length; i++) {
                            if (!newLine) {
                                printStream.print(endCell);
                            } else {
                                newLine = false;
                            }
                            printStream.print(model.getX()[i][j]);
                        }
                    }
                    if (model.getY() != null && model.getY().length > 0) {
                        for (int i = 0; i < model.getY().length; i++) {
                            if (model.getY()[i]!=null && j < model.getY()[i].length) {
                                if (!newLine) {
                                    printStream.print(endCell);
                                } else {
                                    newLine = false;
                                }
                                printStream.print(model.getY()[i][j]);
                            }
                        }
                    }
                    if (model.getZ() != null && model.getZ().length > 0) {
                        for (int i = 0; i < model.getZ().length; i++) {
                            if (!newLine) {
                                printStream.print(endCell);
                            } else {
                                newLine = false;
                            }
                            final Complex[] zzi = model.getZ()[i];
                            if (j < zzi.length) {
                                printStream.print(zzi[j]);
                                printStream.print(endCell + model.getZ()[i][j].abs());
                                printStream.print(endCell + model.getZ()[i][j].getReal());
                                printStream.print(endCell + model.getZ()[i][j].getImag());
                            } else {
                                printStream.print("");
                                printStream.print(endCell);
                                printStream.print(endCell);
                                printStream.print(endCell);
                            }
                        }
                    }
                }
            }
        } finally {
            if (printStream != null) {
                try {
                    printStream.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static JPopupMenu buildJPopupMenu(PlotComponentPanel mainComponent, final PlotModelProvider modelProvider) {
        JPopupMenu popupMenu = mainComponent.getPopupMenu();
        if(popupMenu!=null){
            return buildJPopupMenu(popupMenu,modelProvider);
        }
        return null;
    }

    public static JPopupMenu buildJPopupMenu(JComponent mainComponent, final PlotModelProvider modelProvider) {
        JPopupMenu componentPopupMenu = null;
        if (mainComponent instanceof PlotComponentPanel) {
            componentPopupMenu = ((PlotComponentPanel) mainComponent).getPopupMenu();
        }
        if (componentPopupMenu == null) {
            if (mainComponent instanceof ChartPanel) {
                componentPopupMenu = ((ChartPanel) mainComponent).getPopupMenu();
            }
        }
        if (componentPopupMenu == null) {
            componentPopupMenu = mainComponent.getComponentPopupMenu();
        }
        if (componentPopupMenu == null) {
            componentPopupMenu = new JPopupMenu();
            mainComponent.setComponentPopupMenu(componentPopupMenu);
        } else {
            componentPopupMenu.addSeparator();
        }
        return buildJPopupMenu(mainComponent,modelProvider);
    }

    public static JPopupMenu buildJPopupMenu(JPopupMenu componentPopupMenu, final PlotModelProvider modelProvider) {
        componentPopupMenu.addSeparator();
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        boolean enableViewMenu = true;
        DoubleABSAction absAction = new DoubleABSAction(modelProvider);
        DoubleREALAction realAction = new DoubleREALAction(modelProvider);
        DoubleIMAGAction imagAction = new DoubleIMAGAction(modelProvider);
        DoubleDBAction dbAction = new DoubleDBAction(modelProvider);
        DoubleDB2Action db2Action = new DoubleDB2Action(modelProvider);
        DoubleArgAction argAction = new DoubleArgAction(modelProvider);
        ComplexAction complexAction = new ComplexAction(modelProvider);

        JMenuItem selectY = new JMenuItem("Configure Series");
        selectY.addActionListener(new ActionListener() {


            public void actionPerformed(ActionEvent e) {
                configureSeries(modelProvider);
            }
        });
        JMenuItem extProperties = new JMenuItem("Extra Properties");
        JMenu functionsMenu = new JMenu("Function");
        JMenu viewMenu = null;
        if (enableViewMenu) {
            viewMenu = new JMenu("Display");
        }
        componentPopupMenu.add(functionsMenu);
        if (enableViewMenu) {
            componentPopupMenu.add(viewMenu);
        }
        componentPopupMenu.add(extProperties);
        componentPopupMenu.add(selectY);

        ButtonGroup g;
        JCheckBoxMenuItem f;

        g = new ButtonGroup();

        f = new JCheckBoxMenuItem(absAction);
        f.setSelected(modelProvider.getModel().getZDoubleFunction() == ComplexAsDouble.ABS);
        g.add(f);
        functionsMenu.add(f);

        f = new JCheckBoxMenuItem(realAction);
        f.setSelected(modelProvider.getModel().getZDoubleFunction() == ComplexAsDouble.REAL);
        g.add(f);
        functionsMenu.add(f);

        f = new JCheckBoxMenuItem(imagAction);
        f.setSelected(modelProvider.getModel().getZDoubleFunction() == ComplexAsDouble.IMG);
        g.add(f);
        functionsMenu.add(f);

        f = new JCheckBoxMenuItem(dbAction);
        f.setSelected(modelProvider.getModel().getZDoubleFunction() == ComplexAsDouble.DB);
        g.add(f);
        functionsMenu.add(f);

        f = new JCheckBoxMenuItem(db2Action);
        f.setSelected(modelProvider.getModel().getZDoubleFunction() == ComplexAsDouble.DB2);
        g.add(f);
        functionsMenu.add(f);

        f = new JCheckBoxMenuItem(argAction);
        f.setSelected(modelProvider.getModel().getZDoubleFunction() == ComplexAsDouble.ARG);
        g.add(f);
        functionsMenu.add(f);

        f = new JCheckBoxMenuItem(complexAction);
        f.setSelected(modelProvider.getModel().getZDoubleFunction() == ComplexAsDouble.COMPLEX);
        g.add(f);
        functionsMenu.add(f);

        if (enableViewMenu) {

            g = new ButtonGroup();

            f = new JCheckBoxMenuItem(new PlotCourbeAction(modelProvider));
            PlotType type = modelProvider.getModel().getPlotType();
            f.setSelected(type == PlotType.CURVE);
            g.add(f);
            viewMenu.add(f);

            f = new JCheckBoxMenuItem(new PlotHeatMapAction(modelProvider));
            f.setSelected(type == PlotType.HEATMAP);
            g.add(f);
            viewMenu.add(f);

            f = new JCheckBoxMenuItem(new PlotMeshAction(modelProvider));
            f.setSelected(type == PlotType.MESH);
            g.add(f);
            viewMenu.add(f);

            f = new JCheckBoxMenuItem(new PlotMatrixAction(modelProvider));
            f.setSelected(type == PlotType.MATRIX);
            g.add(f);
            viewMenu.add(f);

            f = new JCheckBoxMenuItem(new PlotPolarAction(modelProvider));
            f.setSelected(type == PlotType.POLAR);
            g.add(f);
            viewMenu.add(f);

            f = new JCheckBoxMenuItem(new PlotTableAction(modelProvider));
            f.setSelected(type == PlotType.TABLE);
            g.add(f);
            viewMenu.add(f);
        }
        extProperties.addActionListener(new ActionListener() {


            public void actionPerformed(ActionEvent e) {
                Complex[][] z1 = modelProvider.getModel().getZ();
                if (z1 != null) {
                    Matrix c = Maths.matrix(z1);
                    double n1 = Double.NaN;
                    double n2 = Double.NaN;
                    double n3 = Double.NaN;
                    double n4 = Double.NaN;
                    double cd1 = Double.NaN;
                    double cd2 = Double.NaN;
                    double cd3 = Double.NaN;
                    Complex d1 = Complex.NaN;
                    int m1 = c.getRowCount();
                    int m2 = c.getColumnCount();
                    try {
                        n1 = c.norm1();
                    } catch (Exception e1) {
                        //
                    }
                    try {
                        n2 = c.norm2();
                    } catch (Exception e1) {
                        //
                    }
                    try {
                        n3 = c.norm3();
                    } catch (Exception e1) {
                        //
                    }
                    try {
                        n4 = c.normInf();
                    } catch (Exception e1) {
                        //
                    }
                    try {
                        cd1 = c.cond();
                    } catch (Exception e1) {
                        //
                    }
                    try {
                        cd2 = c.cond2();
                    } catch (Exception e1) {
                        //
                    }
                    try {
                        cd3 = c.condHadamard();
                    } catch (Exception e1) {
                        //
                    }
                    try {
                        d1 = c.det();
                    } catch (Exception e1) {
                        //
                    }
                    JPanel p = new JPanel(new GridBagLayout2().addLine("[<N1L][<+=N1]").addLine("[<N2L][<+=N2]").addLine("[<N3L][<+=N3]").addLine("[<N4L][<+=N4]").addLine("[<C1L][<+=C1]").addLine("[<C2L][<+=C2]").addLine("[<C3L][<+=C3]").addLine("[<D1L][<+=D1]").addLine("[<M1L][<+=M1]").addLine("[<M2L][<+=M2]").setInsets(".*", new Insets(3, 3, 3, 3)));
                    p.add(new JLabel("Norm1"), "N1L");
                    p.add(new JLabel(String.valueOf(n1)), "N1");
                    p.add(new JLabel("Norm2"), "N2L");
                    p.add(new JLabel(String.valueOf(n2)), "N2");
                    p.add(new JLabel("Norm3"), "N3L");
                    p.add(new JLabel(String.valueOf(n3)), "N3");
                    p.add(new JLabel("NormInf"), "N4L");
                    p.add(new JLabel(String.valueOf(n4)), "N4");
                    p.add(new JLabel("Cond"), "C1L");
                    p.add(new JLabel(String.valueOf(cd1)), "C1");
                    p.add(new JLabel("Cond2"), "C2L");
                    p.add(new JLabel(String.valueOf(cd2)), "C2");
                    p.add(new JLabel("CondH"), "C3L");
                    p.add(new JLabel(String.valueOf(cd3)), "C3");
                    p.add(new JLabel("Det"), "D1L");
                    p.add(new JLabel(String.valueOf(d1)), "D1");
                    p.add(new JLabel("Rows"), "M1L");
                    p.add(new JLabel(String.valueOf(m1)), "M1");
                    p.add(new JLabel("Cols"), "M2L");
                    p.add(new JLabel(String.valueOf(m2)), "M2");
                    JOptionPane.showMessageDialog(null, p);
                }
            }
        });
        componentPopupMenu.addSeparator();
        componentPopupMenu.add(new SaveJFigAction(modelProvider));
        componentPopupMenu.add(new LoadPlotAction());
        return componentPopupMenu;
    }

    public static void configureSeries(final PlotModelProvider modelProvider) {
        ValuesPlotModel model = modelProvider.getModel();
        String[] ytitles = model.getYtitles();
        if ((ytitles == null || ytitles.length == 0) && model.getZ().length > 0) {
            ytitles = new String[model.getZ().length];
            for (int i = 0; i < ytitles.length; i++) {
                ytitles[i] = String.valueOf(i + 1);
            }
        }
        if (ytitles != null && ytitles.length > 0) {
            JTabbedPane jTabbedPane = new JTabbedPane();
            JPanel general = new JPanel(
                    new GridBagLayout2()
                            .addLine("[<showLegend     :: ]")
                            .addLine("[<alternateColor :: ]")
                            .addLine("[<alternateNode  :: ]")
                            .addLine("[<alternateLine  :: ]")
                            .addLine("[<defaultLineTypeLabel][defaultLineType][zeroForDefaultsLabel1]")
                            .addLine("[<defaultNodeTypeLabel][defaultNodeType][zeroForDefaultsLabel2]")
                            .setInsets(".*", new Insets(3, 3, 3, 3))
            );
            Boolean showLegendValue = (Boolean) model.getProperty("showLegend");
            final JCheckBox showLegendCheckBox = new JCheckBox("Show Legend");
            final JCheckBox alternateColorCheckBox = new JCheckBox("Alternate Color", (Boolean) model.getProperty("alternateColor", true));
            final JCheckBox alternateLineCheckBox = new JCheckBox("Alternate Line Type", (Boolean) model.getProperty("alternateLine", false));
            final JCheckBox alternateNodeCheckBox = new JCheckBox("Alternate Node Type", (Boolean) model.getProperty("alternateNode", false));
            showLegendCheckBox.setSelected(showLegendValue == null || showLegendValue);
            final JSpinner defaultLineType = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
            final JSpinner defaultNodeType = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
            final JLabel defaultLineTypeLabel = new JLabel("Default Line Type");
            final JLabel defaultNodeTypeLabel = new JLabel("Default Node Type");

            defaultLineType.setEnabled(!alternateLineCheckBox.isSelected());
            defaultNodeType.setEnabled(!alternateNodeCheckBox.isSelected());
            ItemListener itemListener = new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    defaultLineType.setEnabled(!alternateLineCheckBox.isSelected());
                    defaultLineTypeLabel.setEnabled(!alternateLineCheckBox.isSelected());
                    defaultNodeType.setEnabled(!alternateNodeCheckBox.isSelected());
                    defaultNodeTypeLabel.setEnabled(!alternateNodeCheckBox.isSelected());
                }
            };
            alternateLineCheckBox.addItemListener(itemListener);
            alternateNodeCheckBox.addItemListener(itemListener);
            alternateColorCheckBox.addItemListener(itemListener);

            defaultLineType.setValue(model.getProperty("defaultLineType", 0));
            defaultNodeType.setValue(model.getProperty("defaultNodeType", 0));
            general.add(showLegendCheckBox, "showLegend");
            general.add(defaultLineTypeLabel, "defaultLineTypeLabel");
            general.add(defaultNodeTypeLabel, "defaultNodeTypeLabel");
            general.add(defaultLineType, "defaultLineType");
            general.add(defaultNodeType, "defaultNodeType");
            general.add(alternateColorCheckBox, "alternateColor");
            general.add(alternateNodeCheckBox, "alternateNode");
            general.add(alternateLineCheckBox, "alternateLine");
            JLabel zeroForDefaultsLabel1 = new JLabel("<html><font size=-3 color=gray>(let zero for defaults)</font>");
            JLabel zeroForDefaultsLabel2 = new JLabel("<html><font size=-3 color=gray>(let zero for defaults)</font>");
            general.add(zeroForDefaultsLabel1, "zeroForDefaultsLabel1");
            general.add(zeroForDefaultsLabel2, "zeroForDefaultsLabel2");
            ModelSeriesItem[] lines = new ModelSeriesItem[ytitles.length];
            for (int i = 0; i < ytitles.length; i++) {
                lines[i] = new ModelSeriesItem();
                lines[i].index = i;
                lines[i].title = ytitles[i];
                lines[i].color = null;
                lines[i].visible = model.getYVisible(i);
                lines[i].lineType = (Integer) model.getProperty(i, "lineType", 0);
                lines[i].nodeType = (Integer) model.getProperty(i, "nodeType", 0);
                lines[i].xmultiplier = ((Number) model.getProperty(i, "xmultiplier", 1.0)).doubleValue();
                lines[i].ymultiplier = ((Number) model.getProperty(i, "ymultiplier", 1.0)).doubleValue();
            }
            jTabbedPane.addTab("General", general);
            ;

            jTabbedPane.addTab("Series", JTableHelper.prepareIndexedTable(new ModelSeriesModel(lines)).getPane());
            jTabbedPane.addTab("Data", JTableHelper.prepareIndexedTable(new ValuesPlotTableModel(modelProvider)).getPane());
            if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null, jTabbedPane, "Configure Series...", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE)) {
                model.setProperty("showLegend", showLegendCheckBox.isSelected());
                model.setProperty("defaultNodeType", defaultNodeType.getValue());
                model.setProperty("defaultLineType", defaultLineType.getValue());
                model.setProperty("alternateColor", alternateColorCheckBox.isSelected());
                model.setProperty("alternateNode", alternateNodeCheckBox.isSelected());
                model.setProperty("alternateLine", alternateLineCheckBox.isSelected());
                for (int i = 0; i < ytitles.length; i++) {
                    model.setYVisible(i, lines[i].visible);
                    model.setYVisible(i, lines[i].visible);
                    model.setProperty(i, "lineType", lines[i].lineType);
                    model.setProperty(i, "nodeType", lines[i].nodeType);
                    model.setProperty(i, "xmultiplier", lines[i].xmultiplier);
                    model.setProperty(i, "ymultiplier", lines[i].ymultiplier);
                }
                model.modelUpdated();
            }
        }
    }

    public static PlotModel loadPlotModel(File file) throws IOException {
        String e = IOUtils.getFileExtension(file).toLowerCase();
        if (e.equals(JFIGOBJ_FILE_EXTENSION)) {
            try {
                return (PlotModel) IOUtils.loadZippedObject(file.getPath());
            } catch (ClassNotFoundException ee) {
                throw new IOException(ee);
            }
        } else if (e.equals(JFIG_FILE_EXTENSION) || e.equals(JFIGDATA_FILE_EXTENSION)) {
            return loadDataJfig(file);
        } else if (e.equals(JFB_FILE_EXTENSION)) {
            return loadDataBundle(file);
        } else {
            throw new UnsupportedOperationException("Unsupported loading for file " + file);
        }
    }

    public static PlotBuilder samples(Samples samples) {
        return builder().samples(samples);
    }

    public static PlotBuilder xformat(DoubleFormatter format) {
        return builder().xformat(format);
    }

    public static PlotComponent getCachedPlotComponent(String name) {
        return cachedPlotComponent.get(name);
    }

    public static PlotComponent setCachedPlotComponent(String name, PlotComponent component) {
        if (component == null) {
            cachedPlotComponent.remove(name);
        } else {
            cachedPlotComponent.put(name, component);
        }
        return component;
    }

    public static class Config {
        private static String defaultWindowTitle = "Hadrumaths Plot";
        private static ClassMap<Converter> objectConverters = new ClassMap<Converter>(Object.class, Converter.class);

        public static String getDefaultWindowTitle() {
            return defaultWindowTitle;
        }

        public static void setDefaultWindowTitle(String defaultWindowTitle) {
            Config.defaultWindowTitle = defaultWindowTitle;
        }

        public static Object convert(Object object) {
            if (object == null) {
                return null;
            }
            Converter converter = objectConverters.get(object.getClass());
            if (converter != null) {
                return converter.convert(object);
            }
            return object;
        }

        public static void registerConverter(Class cls, Converter converter) {
            objectConverters.put(cls, converter);
        }

        public static void unregisterConverter(Class cls) {
            objectConverters.remove(cls);
        }
    }

    private static class ModelSeriesItem {
        private int index;
        private String title;
        private boolean visible;
        private int nodeType;
        private int lineType;
        private double xmultiplier;
        private double ymultiplier;
        private Color color;
    }

    private static class DoubleABSAction extends ValuesModelAction implements Serializable {

        public DoubleABSAction(PlotModelProvider modelProvider) {
            super("Abs", modelProvider);
        }


        public void actionPerformed(ActionEvent e) {
            getModel().setZDoubleFunction(ComplexAsDouble.ABS);
        }
    }

    private static class DoubleREALAction extends ValuesModelAction implements Serializable {

        public DoubleREALAction(PlotModelProvider modelProvider) {
            super("Real", modelProvider);
        }


        public void actionPerformed(ActionEvent e) {
            getModel().setZDoubleFunction(ComplexAsDouble.REAL);
        }
    }

    private static class DoubleIMAGAction extends ValuesModelAction implements Serializable {

        public DoubleIMAGAction(PlotModelProvider modelProvider) {
            super("Imag", modelProvider);
        }


        public void actionPerformed(ActionEvent e) {
            getModel().setZDoubleFunction(ComplexAsDouble.IMG);
        }
    }

    private static class DoubleDBAction extends ValuesModelAction implements Serializable {

        public DoubleDBAction(PlotModelProvider modelProvider) {
            super("DB", modelProvider);
        }


        public void actionPerformed(ActionEvent e) {
            getModel().setZDoubleFunction(ComplexAsDouble.DB);
        }
    }

    private static class DoubleDB2Action extends ValuesModelAction implements Serializable {

        public DoubleDB2Action(PlotModelProvider modelProvider) {
            super("DB2", modelProvider);
        }


        public void actionPerformed(ActionEvent e) {
            getModel().setZDoubleFunction(ComplexAsDouble.DB2);
        }
    }

    private static class DoubleArgAction extends ValuesModelAction implements Serializable {

        public DoubleArgAction(PlotModelProvider modelProvider) {
            super("Arg", modelProvider);
        }


        public void actionPerformed(ActionEvent e) {
            getModel().setZDoubleFunction(ComplexAsDouble.ARG);
        }
    }

    private static class ComplexAction extends ValuesModelAction implements Serializable {

        public ComplexAction(PlotModelProvider modelProvider) {
            super("Complex", modelProvider);
        }


        public void actionPerformed(ActionEvent e) {
            getModel().setZDoubleFunction(ComplexAsDouble.COMPLEX);
        }
    }

    private static class PlotCourbeAction extends ValuesModelAction implements Serializable {

        public PlotCourbeAction(PlotModelProvider modelProvider) {
            super("Curves", modelProvider);
        }


        public void actionPerformed(ActionEvent e) {
            getModel().setPlotType(PlotType.CURVE);
        }
    }

    private static class PlotHeatMapAction extends ValuesModelAction implements Serializable {

        public PlotHeatMapAction(PlotModelProvider modelProvider) {
            super("Heat Map", modelProvider);
        }


        public void actionPerformed(ActionEvent e) {
            getModel().setPlotType(PlotType.HEATMAP);
        }
    }

    private static class PlotTableAction extends ValuesModelAction implements Serializable {

        public PlotTableAction(PlotModelProvider modelProvider) {
            super("Table", modelProvider);
        }


        public void actionPerformed(ActionEvent e) {
            getModel().setPlotType(PlotType.TABLE);
        }
    }

    private static abstract class ModelAction extends AbstractPlotAction implements Serializable {
        PlotModelProvider modelProvider;

        private ModelAction(String name, PlotModelProvider modelProvider) {
            super(name);
            this.modelProvider = modelProvider;
        }

        public PlotModelProvider getModelProvider() {
            return modelProvider;
        }

        public PlotModel getModel() {
            return getModelProvider().getModel();
        }
    }

    private static abstract class ValuesModelAction extends ModelAction {
        public ValuesModelAction(String name, PlotModelProvider modelProvider) {
            super(name, modelProvider);
        }

        public ValuesPlotModel getModel() {
            return getModelProvider().getModel();
        }
    }

    private static class PlotMeshAction extends ValuesModelAction {
        public PlotMeshAction(PlotModelProvider modelProvider) {
            super("Mesh 3D", modelProvider);
        }


        public void actionPerformed(ActionEvent e) {
            getModel().setPlotType(PlotType.MESH);
        }
    }

    private static class PlotMatrixAction extends ValuesModelAction {
        public PlotMatrixAction(PlotModelProvider modelProvider) {
            super("Matrix", modelProvider);
        }


        public void actionPerformed(ActionEvent e) {
            getModel().setPlotType(PlotType.MATRIX);
        }
    }

    private static class PlotPolarAction extends ValuesModelAction {
        public PlotPolarAction(PlotModelProvider modelProvider) {
            super("Polar", modelProvider);
        }


        public void actionPerformed(ActionEvent e) {
            getModel().setPlotType(PlotType.POLAR);
        }
    }

    private static class ModelSeriesModel implements TableModel {
        private ModelSeriesItem[] lines;

        private ModelSeriesModel(ModelSeriesItem[] lines) {
            this.lines = lines;
        }

        public int getRowCount() {
            return lines.length;
        }

        public ModelSeriesItem[] getLines() {
            return lines;
        }

        public int getColumnCount() {
            return 8;
        }

        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0: {
                    return "Index";
                }
                case 1: {
                    return "Title";
                }
                case 2: {
                    return "NodeType";
                }
                case 3: {
                    return "LineType";
                }
                case 4: {
                    return "Color";
                }
                case 5: {
                    return "xfactor";
                }
                case 6: {
                    return "yfactor";
                }
                case 7: {
                    return "Visible";
                }
            }
            return null;
        }

        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0: {
                    //return "Index";
                    return Integer.class;
                }
                case 1: {
                    //return "Title";
                    return String.class;
                }
                case 2: {
                    //return "NodeType";
                    return Integer.class;
                }
                case 3: {
                    //return "LineType";
                    return Integer.class;
                }
                case 4: {
                    //return "Color";
                    return Color.class;
                }
                case 5: {
                    //return "xmultiplier";
                    return Double.class;
                }
                case 6: {
                    //return "ymultiplier";
                    return Double.class;
                }
                case 7: {
                    //return "Visible";
                    return Boolean.class;
                }
            }
            return null;
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0: {
                    //return "Index";
                    return false;
                }
                case 1: {
                    //return "Title";
                    return true;
                }
                case 2: {
                    //return "NodeType";
                    return true;
                }
                case 3: {
                    //return "LineType";
                    return true;
                }
                case 4: {
                    //return "Color";
                    return true;
                }
                case 5: {
                    //return "xmultiplier";
                    return true;
                }
                case 6: {
                    //return "ymultiplier";
                    return true;
                }
                case 7: {
                    //return "Visible";
                    return true;
                }
            }
            return false;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0: {
                    //return "Index";
                    return lines[rowIndex].index;
                }
                case 1: {
                    //return "Title";
                    return lines[rowIndex].title;
                }
                case 2: {
                    //return "NodeType";
                    return lines[rowIndex].nodeType;
                }
                case 3: {
                    //return "LineType";
                    return lines[rowIndex].lineType;
                }
                case 4: {
                    //return "Color";
                    return lines[rowIndex].color;
                }
                case 5: {
                    //return "xmultiplier";
                    return lines[rowIndex].xmultiplier;
                }
                case 6: {
                    //return "ymultiplier";
                    return lines[rowIndex].ymultiplier;
                }
                case 7: {
                    //return "Visible";
                    return lines[rowIndex].visible;
                }
            }
            return null;
        }

        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0: {
                    //return "Index";
                    lines[rowIndex].index = (Integer) aValue;
                    break;
                }
                case 1: {
                    //return "Title";
                    lines[rowIndex].title = (String) aValue;
                    break;
                }
                case 2: {
                    //return "NodeType";
                    lines[rowIndex].nodeType = (Integer) aValue;
                    break;
                }
                case 3: {
                    //return "LineType";
                    lines[rowIndex].lineType = (Integer) aValue;
                    break;
                }
                case 4: {
                    //return "Color";
                    lines[rowIndex].color = (Color) aValue;
                    break;
                }
                case 5: {
                    //return "xmultiplier";
                    lines[rowIndex].ymultiplier = ((Number) aValue).doubleValue();
                    break;
                }
                case 6: {
                    //return "ymultiplier";
                    lines[rowIndex].ymultiplier = ((Number) aValue).doubleValue();
                    break;
                }
                case 7: {
                    //return "Visible";
                    lines[rowIndex].visible = (Boolean) aValue;
                    break;
                }
            }
        }

        public void addTableModelListener(TableModelListener l) {
            //
        }

        public void removeTableModelListener(TableModelListener l) {
            //
        }
    }

}
