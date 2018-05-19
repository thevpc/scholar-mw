package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.Plot;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class SaveJFigAction extends AbstractPlotAction implements Serializable {
    private static File staticLastFolder;
    private PlotModelProvider plot;
    private File selectedFile;

    public SaveJFigAction(PlotModelProvider plot) {
        super("Save Data 'jfig'");
        this.plot = plot;
    }

    public void actionPerformed(ActionEvent e) {
        JFileChooser jfc = new JFileChooser(staticLastFolder);
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.addChoosableFileFilter(new ExtensionFileFilter("csv", "Text CSV"));
        jfc.addChoosableFileFilter(new ExtensionFileFilter("m", "Matlab"));
        jfc.addChoosableFileFilter(new ExtensionFileFilter("png", "PNG Image"));
        jfc.addChoosableFileFilter(new ExtensionFileFilter("jpeg", "JPEG Image"));
        jfc.addChoosableFileFilter(new ExtensionFileFilter(Plot.JFIG_FILE_EXTENSION, "Java Figure"));
        jfc.addChoosableFileFilter(new ExtensionFileFilter(Plot.JFB_FILE_EXTENSION, "Java Figure Bundle"));
        if (selectedFile != null && selectedFile.exists()) {
            if (selectedFile.isDirectory()) {
                jfc.setCurrentDirectory(selectedFile);
            } else {
                jfc.setSelectedFile(selectedFile);
            }
        } else {
            jfc.setCurrentDirectory(staticLastFolder);
        }
        int i = jfc.showSaveDialog(e == null ? null : (Component) e.getSource());
        if (i == JFileChooser.APPROVE_OPTION) {
            selectedFile = jfc.getSelectedFile();
            staticLastFolder = selectedFile.getParentFile();
            FileFilter filter = jfc.getFileFilter();
            if (filter instanceof ExtensionFileFilter) {
                ExtensionFileFilter f = (ExtensionFileFilter) filter;
                if (!selectedFile.getName().contains(".")) {
                    selectedFile = new File(selectedFile.getPath() + "." + f.getExtension());
                }
            } else {
                if (!selectedFile.getName().contains(".")) {
                    selectedFile = new File(selectedFile.getPath() + ".jfb");
                }
            }
            try {
                Plot.savePlot(selectedFile, null, plot);
            } catch (IOException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(plot.getComponent(), e1.getMessage() == null ? e1.toString() : e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    private class ExtensionFileFilter extends FileFilter {
        private String extension;
        private String description;

        public ExtensionFileFilter(String extension, String description) {
            this.extension = extension;
            this.description = description;
        }

        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith("." + extension);
        }

        public String getDescription() {
            return description;
        }

        public String getExtension() {
            return extension;
        }
    }
}
