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
public interface PlotWindowManager extends PlotComponentDisplayer {
    void add(PlotComponent component, PlotPath path);

    void add(PlotComponent component);

    void remove(PlotComponent component);

    PlotContainer add(String name);


}
