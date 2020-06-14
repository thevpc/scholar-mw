/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruplot;

import net.vpc.scholar.hadruplot.PlotValue;

/**
 *
 * @author vpc
 */
public class PlotValueAndPriority {

    private PlotValue value;
    private int priority;

    public PlotValueAndPriority(PlotValue value, int priority) {
        this.value = value;
        this.priority = priority;
    }

    public PlotValue getValue() {
        return value;
    }

    public int getPriority() {
        return priority;
    }

}
