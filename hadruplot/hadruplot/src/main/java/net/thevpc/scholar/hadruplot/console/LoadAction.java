package net.thevpc.scholar.hadruplot.console;

import net.thevpc.common.swing.file.ExtensionFileChooserFilter;
import net.thevpc.scholar.hadruplot.console.extension.PlotConsoleFileSupport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class LoadAction extends AbstractFrameAction {
    public LoadAction(PlotConsoleFrame frame) {
        super(frame, "Load", "Open.png");
    }

    public void actionPerformed(ActionEvent e) {
        JFileChooser c = new JFileChooser();
        PlotConsole console = frame.getConsole();
        c.setCurrentDirectory(frame.getConsole().getCurrentDirectory());
        c.addChoosableFileFilter(PlotConsole.CHOOSER_FILTER);
        for (PlotConsoleFileSupport plotConsoleFileSupport : console.getPlotConsoleFileSupports()) {
            c.addChoosableFileFilter(new ExtensionFileChooserFilter(plotConsoleFileSupport.getFileExtension(), plotConsoleFileSupport.getFileDesc()));
        }
        if (JFileChooser.APPROVE_OPTION == c.showOpenDialog((Component) frame)) {
            File selectedFile = c.getSelectedFile();
            try {
                console.loadFile(selectedFile);
                frame.addRecentFile(selectedFile);
            } catch (Throwable e1) {
                console.getLog().error(e1);
                e1.printStackTrace();
                JOptionPane.showMessageDialog(frame.frameComponent(), "unable to load " + selectedFile + "\n" + e1);
            }
        }

    }
}
