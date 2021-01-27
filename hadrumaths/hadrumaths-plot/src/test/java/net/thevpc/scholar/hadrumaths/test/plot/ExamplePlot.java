/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.test.plot;

import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadruplot.Plot;

import static net.thevpc.scholar.hadrumaths.Maths.expr;

/**
 *
 * @author vpc
 */
public class ExamplePlot {
    public static void main(String[] args) {
//        Plot.asCurve().plot(new double[]{1,2,3});
//        Plot.asCurve().plot(Maths.columnVector(new double[]{1,2,3}));
//        Plot.asCurve().plot(Maths.rowVector(new double[]{1,2,3}));
//        Plot.asCurve().plot(new double[][]{{1},{2},{3}});

//        Plot.plot(Maths.vector(expr("X"),expr("X+1")));
        Plot.xsamples(100).ysamples(5).plot(Maths.vector(expr("sin(X)*cos(Y)*domain(0,3.14,0,3.14)")));
    }
}
