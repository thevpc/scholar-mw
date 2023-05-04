package net.thevpc.scholar.hadruplot.console;

import java.awt.event.ActionEvent;

public class CloseAction extends AbstractFrameAction {
    public CloseAction(PlotConsoleFrame frame) {
        super(frame, "Close", "Close.png");
    }

    public void actionPerformed(ActionEvent e) {
        frame.closeFrame();
    }
}
