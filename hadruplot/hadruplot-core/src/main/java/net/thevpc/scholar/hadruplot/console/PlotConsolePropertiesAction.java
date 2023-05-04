package net.thevpc.scholar.hadruplot.console;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class PlotConsolePropertiesAction extends AbstractFrameAction {
    public PlotConsolePropertiesAction(PlotConsoleFrame frame) {
        super(frame, "Properties", "Info.png");
    }

    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(frame.frameComponent(), new PlotConsoleProperties(frame.getConsole()), "Properties", JOptionPane.PLAIN_MESSAGE);
    }
}
