package net.thevpc.scholar.hadruplot.console;

import java.awt.event.ActionEvent;

public class ExitAction extends AbstractFrameAction {
    public ExitAction(PlotConsoleFrame frame) {
        super(frame, "Exit", "Exit.png");
    }

    public void actionPerformed(ActionEvent e) {
        frame.exitFrame();
    }
}
