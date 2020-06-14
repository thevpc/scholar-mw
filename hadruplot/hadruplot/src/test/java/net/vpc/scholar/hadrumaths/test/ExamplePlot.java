/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.test;

import net.vpc.scholar.hadruplot.Plot;
import net.vpc.scholar.hadruplot.console.PlotConsole;

/**
 *
 * @author vpc
 */
public class ExamplePlot {
    public static void main(String[] args) {
        PlotConsole c=new PlotConsole();
        c.setGlobal();
        Plot.asCurve().plot(new double[]{0,0.5,1});
        Plot.asCurve().plot(new double[]{0,0.5,1});
//        Plot.asCurve().plot(new double[][]{{100},{200},{300}});
    }
}
