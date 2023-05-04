package net.thevpc.scholar.hadruplot.console;

import net.thevpc.common.swing.SwingUtilities3;
import net.thevpc.scholar.hadruplot.actions.AbstractPlotAction;

import java.net.URL;
import java.util.NoSuchElementException;

public abstract class AbstractFrameAction extends AbstractPlotAction {
    protected PlotConsoleFrame frame;

    public AbstractFrameAction(PlotConsoleFrame frame, String name, String icon) {
        this.frame = frame;
        putValue(NAME, name);
        if(icon!=null) {
            URL resource = DefaultPlotConsoleFrame.class.getResource(icon);
            if (resource == null) {
                throw new NoSuchElementException("icon resource not found: " + icon);
            }
            putValue(SMALL_ICON, SwingUtilities3.getScaledIcon(resource, 16, 16));
        }
    }

}
