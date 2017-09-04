/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.plot;

import javax.swing.*;
import java.awt.*;

/**
 * @author vpc
 */
public class ScatteredFramesWindowManager extends AbstractPlotWindowManager {
    private PlotContainer rootContainer;

    public ScatteredFramesWindowManager() {
        rootContainer=new FramePlotContainer();
    }

    @Override
    public PlotContainer getRootContainer() {
        return rootContainer;
    }
}
