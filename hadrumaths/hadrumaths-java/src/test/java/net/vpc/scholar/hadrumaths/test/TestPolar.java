package net.vpc.scholar.hadrumaths.test;

import net.vpc.scholar.hadrumaths.plot.console.PlotConsole;

public class TestPolar {
    public static void main(String[] args) {
//        System.out.println(Maths.dlist(new double[]{1,2,3,4}).toString());
        PlotConsole pc = new PlotConsole();
        pc.plotter().asPolar().polarAngleOffset(90).plot(new double[]{1, 2, 3, 4});
    }
}
