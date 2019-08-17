package net.vpc.scholar.hadrumaths.plot.filetypes;

import java.io.UncheckedIOException;
import net.vpc.scholar.hadruplot.*;
import net.vpc.scholar.hadruplot.filetypes.PlotFileTypePng;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class PlotFileTypeBundle implements PlotFileType {
    public static final PlotFileType INSTANCE=new PlotFileTypeBundle();

    @Override
    public String getTitle() {
        return "JFig Bundle";
    }

    @Override
    public String[] getExtensions() {
        return new String[]{"jfb"};
    }

    @Override
    public void save(File file, PlotModelProvider plotProvider) throws IOException {
        File tempFolder = File.createTempFile("_p_", "plot");
        tempFolder.delete();
        tempFolder.mkdirs();
        File file1 = new File(tempFolder, "data.figdata");
        File file2 = new File(tempFolder, "data.csv");
        File file3 = new File(tempFolder, "data.png");
        File[] files = new File[]{file1, file2, file3};
        PlotFileTypeJFig.INSTANCE.save(file1, plotProvider);
        PlotFileTypeCsv.INSTANCE.save(file2, plotProvider);
        PlotFileTypePng.INSTANCE.save(file3, plotProvider);
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

    @Override
    public void save(File file, PlotComponent component) throws IOException {
        save(file,new SimplePlotModelProvider(component.getModel(),component.toComponent()));
    }

    @Override
    public PlotModel loadModel(File file) {
        return loadDataBundle(file);
    }

    private static ValuesPlotModel loadDataBundle(File file) throws UncheckedIOException {
        ZipInputStream in = null;
        ZipEntry ze = null;
//        File ff = null;
        try {
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
                        return PlotFileTypeJFig.loadDataJfig(in);
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
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
        throw new UnsupportedOperationException("Unknown file format");
    }


}
