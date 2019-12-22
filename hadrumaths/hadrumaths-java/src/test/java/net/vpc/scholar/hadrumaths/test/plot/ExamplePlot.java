/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.test.plot;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadruplot.Plot;

/**
 *
 * @author vpc
 */
public class ExamplePlot {
    public static void main(String[] args) {
        Plot.asCurve().plot(new double[]{1,2,3});
        Plot.asCurve().plot(Maths.columnVector(new double[]{1,2,3}));
        Plot.asCurve().plot(Maths.rowVector(new double[]{1,2,3}));
        Plot.asCurve().plot(new double[][]{{1},{2},{3}});
    }
}
