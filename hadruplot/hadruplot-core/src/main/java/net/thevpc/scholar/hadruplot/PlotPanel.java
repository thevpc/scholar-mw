/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruplot;

import net.thevpc.scholar.hadruplot.model.PlotModel;

/**
 * @author vpc
 */
public interface PlotPanel extends PlotComponent {
    PlotModel getModel();

    void setModel(PlotModel model);

    boolean accept(PlotModel model);

    void display();
}
