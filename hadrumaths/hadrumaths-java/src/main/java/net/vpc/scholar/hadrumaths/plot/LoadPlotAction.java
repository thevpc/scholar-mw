package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Plot;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.util.IOUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 9 sept. 2005 10:32:40
 */

public class LoadPlotAction extends AbstractPlotAction implements Serializable {

    private File selectedFile;
    private static int openFrames = 0;
    private static WindowAdapter wa = new WindowAdapter() {

        @Override
        public void windowClosing(WindowEvent e) {
            openFrames--;
            if (openFrames <= 0) {
                System.exit(0);
            }
        }
    };

    public static void main(String[] args) {
        System.out.println("LoadPlotAction version 1.1");
        LoadPlotAction loadJFigAction = new LoadPlotAction();
        ArrayList<File> files = new ArrayList<File>();

        for (String arg : args) {
            if (arg.trim().length() > 0) {
                files.add(new File(Maths.Config.expandPath(arg)));
            }
        }
        if (files.size() > 0) {
            loadJFigAction.openFiles(files.toArray(new File[files.size()]));
        } else {
            loadJFigAction.showOpenFilesDialog(null);
        }
    }

    public LoadPlotAction() {
        super("Load Plot");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        showOpenFilesDialog(e == null ? null : (Component) e.getSource());
    }

    private void showOpenFilesDialog(Component parent) {
        JFileChooser jfc = new JFileChooser();
        jfc.addChoosableFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                String e = IOUtils.getFileExtension(f).toLowerCase();
                return f.isDirectory()
                        || e.equals(Plot.JFIG_FILE_EXTENSION)
                        || e.equals(Plot.JFIGOBJ_FILE_EXTENSION)
                        || e.equals(Plot.JFIGDATA_FILE_EXTENSION)
                        || e.equals(Plot.JFB_FILE_EXTENSION)
                        ;
            }

            @Override
            public String getDescription() {
                return "Figure";
            }
        });
        jfc.addChoosableFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                String e = IOUtils.getFileExtension(f).toLowerCase();
                return f.isDirectory()
                        || e.equals(ObjectCache.CACHE_OBJECT_FILE_EXTENSION);
            }

            @Override
            public String getDescription() {
                return "Cached Object";
            }
        });
        jfc.addChoosableFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                String e = IOUtils.getFileExtension(f).toLowerCase();
                return f.isDirectory()
                        || e.equals(Plot.JFIG_FILE_EXTENSION)
                        || e.equals(Plot.JFIGOBJ_FILE_EXTENSION)
                        || e.equals(Plot.JFIGDATA_FILE_EXTENSION)
                        || e.equals(Plot.JFB_FILE_EXTENSION)
                        || e.equals(ObjectCache.CACHE_OBJECT_FILE_EXTENSION);
            }

            @Override
            public String getDescription() {
                return "All supported Types";
            }
        });
        if (selectedFile != null && selectedFile.exists()) {
            if (selectedFile.isDirectory()) {
                jfc.setCurrentDirectory(selectedFile);
            } else {
                jfc.setSelectedFile(selectedFile);
            }
        }
        jfc.setMultiSelectionEnabled(true);
        int i = jfc.showOpenDialog(parent);
        if (i == JFileChooser.APPROVE_OPTION) {
            File[] all = jfc.getSelectedFiles();
            openFiles(all);
        }
    }

    private void openFiles(File[] all) {
        for (File file : all) {
            try {
                Plot.plot(file, null);
                selectedFile = file;
            } catch (Throwable e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(null, e1.getMessage() == null ? e1.toString() : e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }

}
