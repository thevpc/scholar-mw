package net.vpc.scholar.hadrumaths.plot.filetypes;

import java.io.UncheckedIOException;
import net.vpc.common.util.DoubleFormat;
import net.vpc.scholar.hadrumaths.DMatrix;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.io.HadrumathsIOUtils;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;
import net.vpc.scholar.hadruplot.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class PlotFileTypeJFig implements PlotFileType {
    public static final PlotFileType INSTANCE=new PlotFileTypeJFig();
    @Override
    public String getTitle() {
        return "JFig";
    }

    @Override
    public String[] getExtensions() {
        return new String[]{"jfig"};
    }


    @Override
    public PlotModel loadModel(File file) {
        return loadDataJfig(file);
    }

    @Override
    public void save(File file, PlotModelProvider plotProvider) throws IOException {
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
            printStream.println(((model.getZ() == null || model.getZ().length == 0 || model.getZ()[0] == null) ? ("z =" + lineSep) : (Maths.matrix(ArrayUtils.toComplex(model.getZ())).format(commentChar, "z"))) + endLine);
            printStream.println("title =" + (model.getTitle() == null ? "" : model.getTitle()) + endLine);
            printStream.println("xlabel =" + (model.getXtitle() == null ? "" : model.getXtitle()) + endLine);
            printStream.println("ylabel =" + (model.getYtitle() == null ? "" : model.getYtitle()) + endLine);
            printStream.println("zlabel =" + (model.getZtitle() == null ? "" : model.getZtitle()) + endLine);
            printStream.println("plotType =" + model.getPlotType().toString() + endLine);
            if (model.getConverter() != null) {
                printStream.println("zDoubleFunction =" + model.getConverter().toString() + endLine);
            }
            String[] ytitles = model.getYtitles();
            if (ytitles != null) {
                for (int i = 0; i < ytitles.length; i++) {
                    printStream.println("ytitle =" + (model.getYtitles()[i] == null ? "" : model.getYtitles()[i]) + endLine);
                }
            }
            if (model.getXformat() != null) {
                printStream.println("xformat =" + HadrumathsIOUtils.serializeObjectToString(model.getXformat()));
            }
            if (model.getYformat() != null) {
                printStream.println("yformat =" + HadrumathsIOUtils.serializeObjectToString(model.getXformat()));
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


    public static ValuesPlotModel loadDataJfig(File file) throws UncheckedIOException {
        FileReader fileReader = null;
        try {
            try {
                fileReader = new FileReader(file);
                return loadDataJfig(fileReader);
            } finally {
                if (fileReader != null) {
                    fileReader.close();
                }
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static ValuesPlotModel loadDataJfig(InputStream reader) throws UncheckedIOException {
        InputStreamReader streamReader = null;
        try {
            try {
                streamReader = new InputStreamReader(reader);
                return loadDataJfig(streamReader);
            } finally {
                if (streamReader != null) {
                    streamReader.close();
                }
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static ValuesPlotModel loadDataJfig(Reader reader) throws UncheckedIOException {
        ValuesPlotModel m = new ValuesPlotModel();
        BufferedReader br = null;
        try {
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
                    } else if (line.startsWith("title =")) {
                        m.setTitle(line.substring("title".length() + 2));
                    } else if (line.startsWith("name =")) {
                        m.setTitle(line.substring("name".length() + 2));
                    } else if (line.startsWith("xlabel =")) {
                        m.setxTitle(line.substring("xlabel".length() + 2));
                    } else if (line.startsWith("ylabel =")) {
                        m.setyTitle(line.substring("ylabel".length() + 2));
                    } else if (line.startsWith("plotType =")) {
                        m.setPlotType(PlotType.valueOf(line.substring("plotType".length() + 2)));
                    } else if (line.startsWith("zDoubleFunction =")) {
                        m.setConverter(PlotDoubleConverter.valueOf(line.substring("zDoubleFunction".length() + 2)));
                    } else if (line.startsWith("ytitle =")) {
                        ytitlesList.add(line.substring("ytitle".length() + 2));
                    } else if (line.startsWith("xformat =")) {
                        m.setXformat((DoubleFormat) HadrumathsIOUtils.deserializeObjectToString(line.substring("xformat".length() + 2)));
                    } else if (line.startsWith("yformat =")) {
                        m.setYformat((DoubleFormat) HadrumathsIOUtils.deserializeObjectToString(line.substring("yformat".length() + 2)));
                    } else if (line.startsWith("zformat =")) {
                        m.setZformat((DoubleFormat) HadrumathsIOUtils.deserializeObjectToString(line.substring("zformat".length() + 2)));
                    }
                }
                m.setYtitles(ytitlesList.toArray(new String[0]));
            } finally {
                if (br != null) {
                    br.close();
                }
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
        return m;
    }

}
