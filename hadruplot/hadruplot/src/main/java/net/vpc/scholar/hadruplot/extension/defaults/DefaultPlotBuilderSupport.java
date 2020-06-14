/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruplot.extension.defaults;

import net.vpc.scholar.hadruplot.PlotBuilder;
import net.vpc.scholar.hadruplot.extension.PlotBuilderSupport;

/**
 *
 * @author vpc
 */
public class DefaultPlotBuilderSupport implements PlotBuilderSupport {
    
    public DefaultPlotBuilderSupport() {
    }

    @Override
    public boolean xsamples(Object xvalue, PlotBuilder builder) {
        return false;
    }

    @Override
    public boolean ysamples(Object xvalue, PlotBuilder builder) {
        return false;
    }

    @Override
    public boolean zsamples(Object xvalue, PlotBuilder builder) {
        return false;
    }
    
}
