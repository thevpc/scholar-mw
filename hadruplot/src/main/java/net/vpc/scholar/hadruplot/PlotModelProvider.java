package net.vpc.scholar.hadruplot;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 29 juin 2008
 * Time: 22:25:14
 * To change this template use File | Settings | File Templates.
 */
public interface PlotModelProvider {
    PlotModel getModel();

    Component getComponent();
}
