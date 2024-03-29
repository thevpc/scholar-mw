/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruplot.containers;

import net.thevpc.scholar.hadruplot.PlotContainer;

/**
 * @author vpc
 */
public class CustomContainerWindowManager extends AbstractComponentPlotWindowManager {
    private PlotContainer rootContainer;

    public CustomContainerWindowManager(String id) {
        rootContainer = PlotWindowContainerFactories.create(id).create();
    }

    @Override
    public PlotContainer getRootContainer() {
        return rootContainer;
    }
}
