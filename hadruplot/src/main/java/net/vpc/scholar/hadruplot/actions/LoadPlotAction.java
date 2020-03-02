package net.vpc.scholar.hadruplot.actions;

import net.vpc.common.io.FileUtils;
import net.vpc.scholar.hadruplot.Plot;
import net.vpc.scholar.hadruplot.PlotFileType;
import net.vpc.scholar.hadruplot.actions.AbstractPlotAction;
import net.vpc.scholar.hadruplot.console.PlotConfigManager;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.Serializable;

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

//    public static void main(String[] args) {
//        System.out.println("LoadPlotAction version 1.1");
//        LoadPlotAction loadJFigAction = new LoadPlotAction();
//        ArrayList<File> files = new ArrayList<File>();
//
//        for (String arg : args) {
//            if (arg.trim().length() > 0) {
//                files.add(new File(Maths.Config.expandPath(arg)));
//            }
//        }
//        if (files.size() > 0) {
//            loadJFigAction.openFiles(files.toArray(new File[0]));
//        } else {
//            loadJFigAction.showOpenFilesDialog(null);
//        }
//    }

    public LoadPlotAction() {
        super("Load Plot");
    }

//    @Override
    public void actionPerformed(ActionEvent e) {
        showOpenFilesDialog(e == null ? null : (Component) e.getSource());
    }

    private void showOpenFilesDialog(Component parent) {
        JFileChooser jfc = new JFileChooser();
        jfc.addChoosableFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                String e = FileUtils.getFileExtension(f).toLowerCase();
                if(f.isDirectory()){
                    return true;
                }
                for (PlotFileType plotFileType : PlotConfigManager.getPlotFileTypes()) {
                    for (String extension : plotFileType.getExtensions()) {
                        if(e.equalsIgnoreCase(extension)){
                            return true;
                        }
                    }
                }
                return false;
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
