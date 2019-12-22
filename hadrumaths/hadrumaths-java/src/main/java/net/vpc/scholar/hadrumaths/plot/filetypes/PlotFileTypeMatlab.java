package net.vpc.scholar.hadrumaths.plot.filetypes;

import net.vpc.scholar.hadrumaths.DMatrix;
import net.vpc.scholar.hadrumaths.MathsBase;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;
import net.vpc.scholar.hadruplot.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlotFileTypeMatlab implements PlotFileType {
    public static final PlotFileType INSTANCE=new PlotFileTypeMatlab();
    @Override
    public String getTitle() {
        return "Matlab";
    }

    @Override
    public String[] getExtensions() {
        return new String[]{"m"};
    }

    @Override
    public void save(File file, PlotModelProvider plotProvider) throws IOException {
        ValuesPlotModel model = (ValuesPlotModel) plotProvider.getModel();
        String commentChar = "%";
        String endLine = ";";
        PrintStream printStream = null;
        try {
            printStream = new PrintStream(new FileOutputStream(file), true);
            String lineSep = System.getProperty("line.separator");
            printStream.println(commentChar + " Plot data");
            printStream.println(commentChar + " Last modified " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            printStream.println(commentChar + " " + model.getTitle());
            printStream.println();
            printStream.println();
            printStream.println(((model.getX() == null || model.getX().length == 0 || model.getX()[0] == null) ? ("x =" + lineSep) : (new DMatrix(model.getX()).toString(commentChar, "x"))) + endLine);
            printStream.println();
            printStream.println(((model.getY() == null || model.getY().length == 0 || model.getY()[0] == null) ? ("y =" + lineSep) : (new DMatrix(model.getY()).toString(commentChar, "y"))) + endLine);
            printStream.println();
            printStream.println(((model.getZ() == null || model.getZ().length == 0 || model.getZ()[0] == null) ? ("z =" + lineSep) : (MathsBase.matrix(ArrayUtils.toComplex(model.getZ())).format(commentChar, "z"))) + endLine);
//            printStream.println("title =" + (model.getTitle() == null ? "" : model.getTitle()) + endLine);
//            printStream.println("xlabel =" + (model.getXtitle() == null ? "" : model.getXtitle()) + endLine);
//            printStream.println("ylabel =" + (model.getYtitle() == null ? "" : model.getYtitle()) + endLine);
//            printStream.println("zlabel =" + (model.getZtitle() == null ? "" : model.getZtitle()) + endLine);
//            printStream.println("plotType =" + model.getPlotType().toString() + endLine);
//            printStream.println("zDoubleFunction =" + model.getConverter().toString() + endLine);
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

    @Override
    public void save(File file, PlotComponent component) throws IOException {
        save(file,new SimplePlotModelProvider(component.getModel(),component.toComponent()));
    }

    @Override
    public PlotModel loadModel(File file) {
        ComplexMatrix matrix = MathsBase.loadMatrix(file);
        return Plot.title(file.getName()).asMatrix().createModel(matrix);
    }
}
