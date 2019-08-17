/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.test;

import net.vpc.scholar.hadruplot.Plot;

/**
 *
 * @author vpc
 */
public class ExamplePlot {
    public static void main(String[] args) {
        Plot.asCurve().plot(new double[]{1,2,3});
        Plot.asCurve().plot(new double[][]{{1},{2},{3}});
    }
}
