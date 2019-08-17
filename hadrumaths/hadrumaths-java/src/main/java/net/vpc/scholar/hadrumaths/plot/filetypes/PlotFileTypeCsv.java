package net.vpc.scholar.hadrumaths.plot.filetypes;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;
import net.vpc.scholar.hadruplot.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class PlotFileTypeCsv implements PlotFileType {
    public static final PlotFileType INSTANCE=new PlotFileTypeCsv();
    @Override
    public String getTitle() {
        return "Csv";
    }

    @Override
    public String[] getExtensions() {
        return new String[]{"csv"};
    }

    @Override
    public void save(File file, PlotModelProvider plotProvider) throws IOException {
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
            Object[][] z = model.getZ();
            if (z != null && z.length > 0) {
                for (int i = 0; i < z.length; i++) {
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
                    if (z != null && z.length > 0) {
                        Complex[][] cz= ArrayUtils.toComplex(z);
                        for (int i = 0; i < cz.length; i++) {
                            if (!newLine) {
                                printStream.print(endCell);
                            } else {
                                newLine = false;
                            }
                            final Complex[] zzi = cz[i];
                            if (j < zzi.length) {
                                printStream.print(zzi[j]);
                                printStream.print(endCell + cz[i][j].abs());
                                printStream.print(endCell + cz[i][j].getReal());
                                printStream.print(endCell + cz[i][j].getImag());
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

    @Override
    public void save(File file, PlotComponent component) throws IOException {
        save(file,new SimplePlotModelProvider(component.getModel(),component.toComponent()));
    }

    @Override
    public PlotModel loadModel(File file) {
        Matrix matrix = Maths.loadMatrix(file);
        return Plot.title(file.getName()).asMatrix().createModel(matrix);
    }
}
