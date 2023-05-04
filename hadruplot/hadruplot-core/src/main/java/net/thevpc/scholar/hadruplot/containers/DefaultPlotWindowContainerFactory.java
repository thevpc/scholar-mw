/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruplot.containers;

import net.thevpc.scholar.hadruplot.PlotContainer;
import net.thevpc.scholar.hadruplot.extension.PlotWindowContainerFactory;

/**
 *
 * @author vpc
 */
public final class DefaultPlotWindowContainerFactory implements PlotWindowContainerFactory {
    public static final PlotWindowContainerFactory INSTANCE=new DefaultPlotWindowContainerFactory();

    private DefaultPlotWindowContainerFactory() {
    }
    
    @Override
    public PlotContainer create() {
        return new TreeCardPlotContainer();
    }
    
}
