/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.plot;

import javax.swing.*;

/**
 * @author vpc
 */
public interface PlotComponent {


    String getPlotTitle();

    String getLayoutConstraints();

    JComponent toComponent();

    void display();

    void addPlotPropertyListener(PlotPropertyListener listener);

    void removePlotPropertyListener(PlotPropertyListener listener);

}
