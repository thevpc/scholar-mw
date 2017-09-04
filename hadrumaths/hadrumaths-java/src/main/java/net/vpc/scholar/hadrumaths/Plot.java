package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.plot.*;
import net.vpc.scholar.hadrumaths.plot.console.PlotComponentDisplayer;
import net.vpc.scholar.hadrumaths.plot.console.PlotConsole;
import net.vpc.scholar.hadrumaths.util.*;
import net.vpc.scholar.hadrumaths.util.swingext.ColorChooserEditor;
import net.vpc.scholar.hadrumaths.util.swingext.GridBagLayout2;
import org.jfree.chart.ChartPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class Plot {
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

    private Plot() {
    }

    private static PlotWindowManager defaultWindowManager = PlotWindowManagerFactory.createScatteredFrames();

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
        return new PlotBuilder().cd("/"+Config.getDefaultWindowTitle());
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

    public static PlotBuilder asBar() {
        return builder().asBar();
    }

    public static PlotBuilder asRing() {
        return builder().asRing();
    }

    public static PlotBuilder asBubble() {
        return builder().asBubble();
    }

    public static PlotBuilder asArea() {
        return builder().asArea();
    }

    public static PlotBuilder asPie() {
        return builder().asPie();
    }

    public static PlotBuilder asPolar() {
        return builder().asPolar();
    }

    public static PlotBuilder asField() {
        return builder().asField();
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
            SwingUtils.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    plotConsole.show();
                }
            });
        } catch (Exception e) {
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

    public static boolean acceptFileExtension(File file) throws IOException {
        String e = IOUtils.getFileExtension(file).toLowerCase();
        if (!
                (
                        e.equals(ObjectCache.CACHE_OBJECT_FILE_EXTENSION)
                                || e.equals(JFIG_FILE_EXTENSION)
                                || e.equals(JFIGDATA_FILE_EXTENSION)
                                || e.equals(JFIGOBJ_FILE_EXTENSION)
                                || e.equals(JFB_FILE_EXTENSION)
                                || e.equals("m")
                )
                ) {
            return false;
        }
        return true;
    }

    public static PlotComponent loadPlot(File file) throws IOException {
        return create(loadPlotModel(file), Plot.getDefaultWindowManager());
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
        if (model instanceof PlotModelList) {
            return new PlotModelListPanel((PlotModelList) model, windowManager);
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
            printStream.println(commentChar + " Last modified " + Maths.UNIVERSAL_DATE_TIME_FORMAT.format(new Date()));
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
            printStream.println(commentChar + " Last modified " + Maths.UNIVERSAL_DATE_TIME_FORMAT.format(new Date()));
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
                            if (model.getY()[i] != null && j < model.getY()[i].length) {
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
        if (popupMenu != null) {
            return buildJPopupMenu(popupMenu, modelProvider);
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
        return buildJPopupMenu(mainComponent, modelProvider);
    }

    public static JPopupMenu buildJPopupMenu(JPopupMenu componentPopupMenu, final PlotModelProvider modelProvider) {
        componentPopupMenu.addSeparator();
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        boolean enableViewMenu = true;

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
        final PlotModel amodel = modelProvider.getModel();
        if (amodel instanceof ValuesPlotModel) {
            ValuesPlotModel model = (ValuesPlotModel) amodel;
            g = new ButtonGroup();
            for (ComplexAsDouble complexAsDouble : ComplexAsDouble.values()) {
                f = new JCheckBoxMenuItem(new DoubleTypeAction(modelProvider, StringUtils.toCapitalized(complexAsDouble.name()), complexAsDouble));
                f.setSelected((model).getZDoubleFunction() == ComplexAsDouble.ABS);
                g.add(f);
                functionsMenu.add(f);
            }

            if (enableViewMenu) {

                g = new ButtonGroup();
                for (PlotType plotType : PlotType.values()) {
                    if (!plotType.equals(PlotType.ALL) && !plotType.equals(PlotType.AUTO)) {
                        f = new JCheckBoxMenuItem(new PlotTypeAction(modelProvider, StringUtils.toCapitalized(plotType.name()), plotType));
                        PlotType type = (model).getPlotType();
                        f.setSelected(type == PlotType.CURVE);
                        g.add(f);
                        viewMenu.add(f);
                    }
                }
            }
            extProperties.addActionListener(new ActionListener() {


                public void actionPerformed(ActionEvent e) {
                    Complex[][] z1 = (model).getZ();
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
        }
        componentPopupMenu.addSeparator();
        componentPopupMenu.add(new SaveJFigAction(modelProvider));
        componentPopupMenu.add(new LoadPlotAction());
        return componentPopupMenu;
    }

    public static void configureSeries(final PlotModelProvider modelProvider) {
        PlotModel amodel = modelProvider.getModel();
        if (!(amodel instanceof ValuesPlotModel)) {
            return;
        }
        ValuesPlotModel model = (ValuesPlotModel) amodel;
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
                            .addLine("[<showLegend     ][<nodeLabelCheckBox  ][<threeDCheckBox  ]")
                            .addLine("[<alternateColor ][<alternateNode  ][<alternateLine  ]")
                            .addLine("[<lineStepTypeLabel][lineStepTypeCombo -]")
                            .addLine("[<defaultMaxLegendLabel][defaultMaxLegendText -]")
                            .addLine("[<polarOffsetLabel][polarOffsetText -][<clockwiseCheckBox  ]")
                            .addLine("[<defaultLineTypeLabel][defaultLineType - ][< zeroForDefaultsLabel1]")
                            .addLine("[<defaultNodeTypeLabel][defaultNodeType - ][< zeroForDefaultsLabel2]")
                            .setInsets(".*", new Insets(3, 3, 3, 3))
            );
            PlotConfig config = (PlotConfig) model.getProperty("config", null);
            if (config == null) {
                config = new PlotConfig();
                model.setProperty("config", config);
            }
            final JCheckBox showLegendCheckBox = new JCheckBox("Show Legend", config.showLegend == null ? true : config.showLegend);
            final JCheckBox clockwiseCheckBox = new JCheckBox("Clock Wise (polar)", config.clockwise == null ? true : config.clockwise);
            final JCheckBox threeDCheckBox = new JCheckBox("3D", config.threeD == null ? false : config.threeD);
            final JCheckBox alternateColorCheckBox = new JCheckBox("Alternate Color", config.alternateColor == null ? true : config.alternateColor);
            final JCheckBox alternateLineCheckBox = new JCheckBox("Alternate Line Type", config.alternateLine == null ? false : config.alternateLine);
            final JCheckBox alternateNodeCheckBox = new JCheckBox("Alternate Node Type", config.alternateNode == null ? false : config.alternateNode);
            final JCheckBox nodeLabelCheckBox = new JCheckBox("Show Labels", config.nodeLabel == null ? false : config.nodeLabel);
            final JSpinner defaultLineType = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
            final JSpinner defaultNodeType = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
            final JLabel defaultLineTypeLabel = new JLabel("Default Line Type");
            final JLabel defaultNodeTypeLabel = new JLabel("Default Node Type");
            final JLabel polarOffsetLabel = new JLabel("Polar Offset");
            final JLabel defaultMaxLegendLabel = new JLabel("Max Legend");
            final JLabel lineStepTypeLabel = new JLabel("Interpolation");
            final JTextField defaultMaxLegendText = new JTextField(String.valueOf(config.maxLegendCount == null ? Plot.Config.getMaxLegendCount() : config.maxLegendCount));
            final JTextField polarOffsetText = new JTextField(String.valueOf(config.polarAngleOffset == null ? 0 : config.polarAngleOffset));
            final JComboBox lineStepTypeCombo = new JComboBox(new Vector(Arrays.asList(PlotConfigLineStepType.values())));
            lineStepTypeCombo.setSelectedItem(config.lineStepType == null ? PlotConfigLineStepType.DEFAULT : config.lineStepType);
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

            defaultLineType.setValue(config.lineType == null ? 0 : config.lineType);
            defaultNodeType.setValue(config.nodeType == null ? 0 : config.nodeType);
            general.add(showLegendCheckBox, "showLegend");
            general.add(defaultLineTypeLabel, "defaultLineTypeLabel");
            general.add(defaultNodeTypeLabel, "defaultNodeTypeLabel");
            general.add(defaultLineType, "defaultLineType");
            general.add(defaultNodeType, "defaultNodeType");
            general.add(alternateColorCheckBox, "alternateColor");
            general.add(alternateNodeCheckBox, "alternateNode");
            general.add(nodeLabelCheckBox, "nodeLabelCheckBox");
            general.add(alternateLineCheckBox, "alternateLine");
            general.add(defaultMaxLegendLabel, "defaultMaxLegendLabel");
            general.add(defaultMaxLegendText, "defaultMaxLegendText");
            general.add(threeDCheckBox, "threeDCheckBox");
            general.add(clockwiseCheckBox, "clockwiseCheckBox");
            general.add(polarOffsetLabel, "polarOffsetLabel");
            general.add(polarOffsetText, "polarOffsetText");
            general.add(lineStepTypeLabel, "lineStepTypeLabel");
            general.add(lineStepTypeCombo, "lineStepTypeCombo");
            JLabel zeroForDefaultsLabel1 = new JLabel("<html><font size=-3 color=gray>(let zero for defaults)</font>");
            JLabel zeroForDefaultsLabel2 = new JLabel("<html><font size=-3 color=gray>(let zero for defaults)</font>");
            general.add(zeroForDefaultsLabel1, "zeroForDefaultsLabel1");
            general.add(zeroForDefaultsLabel2, "zeroForDefaultsLabel2");
            ModelSeriesItem[] lines = new ModelSeriesItem[ytitles.length];
            JColorPalette defaultPaintArray = Maths.DEFAULT_PALETTE;
            config.ensureChildrenSize(ytitles.length);
            for (int i = 0; i < ytitles.length; i++) {
                lines[i] = new ModelSeriesItem();
                lines[i].setIndex(i);
                lines[i].setTitle(ytitles[i]);
                PlotConfig lineConfig = config.children.get(i);
                Color col = (Color) lineConfig.color;
                if (col == null) {
                    col = defaultPaintArray.getColor(((float) i) / ytitles.length);
                }
                lines[i].setColor(col);
                lines[i].setVisible(model.getYVisible(i));
                lines[i].setLineType(lineConfig.lineType);
                lines[i].setNodeType(lineConfig.nodeType);
                lines[i].setXmultiplier(lineConfig.xmultiplier);
                lines[i].setYmultiplier(lineConfig.ymultiplier);
            }
            jTabbedPane.addTab("General", general);
            ;

            ModelSeriesModel seriesModel = new ModelSeriesModel(lines);
            JTableHelper series = JTableHelper.prepareIndexedTable(seriesModel);
            JButton cornerButton = new JButton("#");
            JMenuBar b = new JMenuBar();
            JMenu m = new JMenu("#");
            b.add(m);
            ValuesPlotTableModel tmodel = new ValuesPlotTableModel(modelProvider);
            JMenuItem select_all = new JMenuItem("select all");
            select_all.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    seriesModel.setSelectAll();
                }
            });
            m.add(select_all);
            JMenuItem select_none = new JMenuItem("select none");
            select_none.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    seriesModel.setSelectNone();
                }
            });
            m.add(select_none);
            series.getPane().setCorner(JScrollPane.UPPER_LEFT_CORNER, b);

            TableColumn column = series.getTable().getColumnModel().getColumn(4);

//            ColorChooserEditor editor = new ColorChooserEditor();
            column.setCellEditor(new ColorChooserEditor());
            column.setCellRenderer(new ColorChooserEditor());
//            series.getPane().setCorner(JScrollPane.UPPER_TRAILING_CORNER, cornerButton);

            jTabbedPane.addTab("Series", series.getPane());

            jTabbedPane.addTab("Data", JTableHelper.prepareIndexedTable(tmodel).getPane());
            if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null, jTabbedPane, "Configure Series...", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE)) {
                config.showLegend = showLegendCheckBox.isSelected();
                config.alternateColor = alternateColorCheckBox.isSelected();
                config.alternateNode = alternateNodeCheckBox.isSelected();
                config.alternateLine = alternateLineCheckBox.isSelected();
                config.threeD = threeDCheckBox.isSelected();
                config.nodeLabel = nodeLabelCheckBox.isSelected();
                config.clockwise = clockwiseCheckBox.isSelected();
                config.polarAngleOffset = StringUtils.parseInt(polarOffsetText.getText(), 0);
                config.nodeType = ((Number) defaultNodeType.getValue()).intValue();
                config.lineType = ((Number) defaultLineType.getValue()).intValue();
                config.maxLegendCount = StringUtils.parseInt(defaultMaxLegendText.getText(), 0);
                config.lineStepType = (PlotConfigLineStepType) lineStepTypeCombo.getSelectedItem();

                for (int i = 0; i < ytitles.length; i++) {
                    model.setYVisible(i, lines[i].isVisible());
                    PlotConfig lineConfig = config.children.get(i);
                    lineConfig.lineType = lines[i].getLineType();
                    lineConfig.nodeType = lines[i].getNodeType();
                    lineConfig.xmultiplier = lines[i].getXmultiplier();
                    lineConfig.ymultiplier = lines[i].getYmultiplier();
                    lineConfig.color = lines[i].getColor();
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
        } else if (e.equals(ObjectCache.CACHE_OBJECT_FILE_EXTENSION)) {
            Object o = null;
            try {
                o = IOUtils.loadZippedObject(file.getPath());
            } catch (ClassNotFoundException ee) {
                throw new IOException(ee);
            }
            if (o != null) {
                return Plot.title(file.getName()).createModel(o);
            } else {
                throw new IllegalArgumentException("Unsupported Type " + file);
            }
        } else if (e.equals("m")) {
            Matrix matrix = Maths.loadMatrix(file);
            return Plot.title(file.getName()).asMatrix().createModel(matrix);
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

    public static PlotBuilder plotBuilderListener(PlotBuilderListener listener) {
        return builder().addPlotBuilderListener(listener);
    }

    public static PlotBuilder cd(String path) {
        return builder().cd(path);
    }

//    private static class DoubleABSAction extends ValuesModelAction implements Serializable {
//
//        public DoubleABSAction(PlotModelProvider modelProvider) {
//            super("Abs", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setZDoubleFunction(ComplexAsDouble.ABS);
//        }
//    }
//
//    private static class DoubleREALAction extends ValuesModelAction implements Serializable {
//
//        public DoubleREALAction(PlotModelProvider modelProvider) {
//            super("Real", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setZDoubleFunction(ComplexAsDouble.REAL);
//        }
//    }
//
//    private static class DoubleIMAGAction extends ValuesModelAction implements Serializable {
//
//        public DoubleIMAGAction(PlotModelProvider modelProvider) {
//            super("Imag", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setZDoubleFunction(ComplexAsDouble.IMG);
//        }
//    }
//
//    private static class DoubleDBAction extends ValuesModelAction implements Serializable {
//
//        public DoubleDBAction(PlotModelProvider modelProvider) {
//            super("DB", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setZDoubleFunction(ComplexAsDouble.DB);
//        }
//    }
//
//    private static class DoubleDB2Action extends ValuesModelAction implements Serializable {
//
//        public DoubleDB2Action(PlotModelProvider modelProvider) {
//            super("DB2", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setZDoubleFunction(ComplexAsDouble.DB2);
//        }
//    }
//
//    private static class DoubleArgAction extends ValuesModelAction implements Serializable {
//
//        public DoubleArgAction(PlotModelProvider modelProvider) {
//            super("Arg", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setZDoubleFunction(ComplexAsDouble.ARG);
//        }
//    }
//
//    private static class ComplexAction extends ValuesModelAction implements Serializable {
//
//        public ComplexAction(PlotModelProvider modelProvider) {
//            super("Complex", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setZDoubleFunction(ComplexAsDouble.COMPLEX);
//        }
//    }

//    private static class PlotCourbeAction extends ValuesModelAction implements Serializable {
//
//        public PlotCourbeAction(PlotModelProvider modelProvider) {
//            super("Curves", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setPlotType(PlotType.CURVE);
//        }
//    }
//
//    private static class PlotHeatMapAction extends ValuesModelAction implements Serializable {
//
//        public PlotHeatMapAction(PlotModelProvider modelProvider) {
//            super("Heat Map", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setPlotType(PlotType.HEATMAP);
//        }
//    }
//
//    private static class PlotTableAction extends ValuesModelAction implements Serializable {
//
//        public PlotTableAction(PlotModelProvider modelProvider) {
//            super("Table", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setPlotType(PlotType.TABLE);
//        }
//    }

    public static class Config {
        private static int maxLegendCount = 20;
        private static String defaultWindowTitle = "Hadrumaths Plot";
        private static ClassMap<Converter> objectConverters = new ClassMap<Converter>(Object.class, Converter.class);

        public static int getMaxLegendCount() {
            return maxLegendCount;
        }

        public static void setMaxLegendCount(int maxLegendCount) {
            Config.maxLegendCount = maxLegendCount;
        }

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

    private static class DoubleTypeAction extends ValuesModelAction implements Serializable {
        private ComplexAsDouble type;

        public DoubleTypeAction(PlotModelProvider modelProvider, String name, ComplexAsDouble type) {
            super(name, modelProvider);
            this.type = type;
        }


        public void actionPerformed(ActionEvent e) {
            getModel().setZDoubleFunction(type);
        }
    }

//    private static class PlotMeshAction extends ValuesModelAction {
//        public PlotMeshAction(PlotModelProvider modelProvider) {
//            super("Mesh 3D", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setPlotType(PlotType.MESH);
//        }
//    }
//
//    private static class PlotMatrixAction extends ValuesModelAction {
//        public PlotMatrixAction(PlotModelProvider modelProvider) {
//            super("Matrix", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setPlotType(PlotType.MATRIX);
//        }
//    }
//
//    private static class PlotPolarAction extends ValuesModelAction {
//        public PlotPolarAction(PlotModelProvider modelProvider) {
//            super("Polar", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setPlotType(PlotType.POLAR);
//        }
//    }

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
            return (ValuesPlotModel) getModelProvider().getModel();
        }
    }

    private static class PlotTypeAction extends ValuesModelAction {
        private PlotType type;

        public PlotTypeAction(PlotModelProvider modelProvider, String name, PlotType type) {
            super(name, modelProvider);
            this.type = type;
        }


        public void actionPerformed(ActionEvent e) {
            getModel().setPlotType(type);
        }
    }
}
