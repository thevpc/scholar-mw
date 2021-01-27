package net.thevpc.scholar.hadruplot.actions;

import net.thevpc.scholar.hadruplot.console.PlotConsoleFrame;

import javax.swing.*;
import java.io.Serializable;

/**
 * Created by vpc on 1/27/17.
 */
public abstract class AbstractPlotAction extends AbstractAction implements Serializable {
    public AbstractPlotAction() {
    }

    public AbstractPlotAction(String name) {
        super(name);
    }

    public AbstractPlotAction(String name, Icon icon) {
        super(name, icon);
    }
}
