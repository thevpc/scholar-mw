package net.thevpc.scholar.hadruplot.actions;

import net.thevpc.echo.api.Action;
import net.thevpc.echo.api.ActionEvent;

import javax.swing.*;
import java.io.Serializable;

/**
 * Created by vpc on 1/27/17.
 */
public abstract class AbstractPlotAction extends AbstractAction implements Serializable, Action {
    public AbstractPlotAction() {
    }

    public AbstractPlotAction(String name) {
        super(name);
    }

    public AbstractPlotAction(String name, Icon icon) {
        super(name, icon);
    }

    @Override
    public void run(ActionEvent event) {
        actionPerformed(null);
    }
}
