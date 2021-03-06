/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadruplot;

import net.thevpc.scholar.hadruplot.console.PlotComponentDisplayer;

/**
 * @author vpc
 */
public interface PlotContainer extends PlotComponent, PlotComponentDisplayer {
    int indexOfPlotComponent(String name);

    PlotComponent getPlotComponent(String name);

    PlotComponent getPlotComponent(int index);

    int getPlotComponentsCount();

    PlotContainer add(int index, String containerName);

    void add(PlotComponent component, net.thevpc.scholar.hadruplot.PlotPath path);

    void add(PlotComponent component);

    void clear();

    void remove(PlotComponent component);

    PlotContainer add(String name);

    boolean containsPlotComponent(PlotComponent plotComponent);

    int indexOfPlotComponent(PlotComponent plotComponent);
}
