/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruplot.extension;

import net.thevpc.scholar.hadruplot.containers.FrameWindowManager;
import net.thevpc.scholar.hadruplot.PlotWindowManager;
import net.thevpc.scholar.hadruplot.containers.ScatteredFramesWindowManager;

/**
 * @author vpc
 */
public class PlotWindowManagerFactory{

    public static PlotWindowManager create() {
        return createSingleFrame();
    }

    public static PlotWindowManager createScatteredFrames() {
        return new ScatteredFramesWindowManager();
    }

    public static PlotWindowManager createSingleFrame() {
        return new FrameWindowManager();
    }
}
