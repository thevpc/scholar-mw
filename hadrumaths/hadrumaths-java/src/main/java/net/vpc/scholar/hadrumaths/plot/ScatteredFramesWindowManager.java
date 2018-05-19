/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.plot;

/**
 * @author vpc
 */
public class ScatteredFramesWindowManager extends AbstractComponentPlotWindowManager {
    private PlotContainer rootContainer;

    public ScatteredFramesWindowManager() {
        rootContainer = new FramePlotContainer();
    }

    @Override
    public PlotContainer getRootContainer() {
        return rootContainer;
    }
}
