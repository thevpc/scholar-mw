package net.thevpc.scholar.hadrumaths.test.plot;

import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruplot.console.PlotConsole;

public class TestPolar {
    public static void main(String[] args) {
//        System.out.println(Maths.dlist(new double[]{1,2,3,4}).toString());
        //PlotConsole pc = new PlotConsole();
        //pc.newPlot().asPolar().polarAngleOffset(90).plot(new double[]{1, 2, 3, 4});
        Plot.asPolar().polarAngleOffsetDegree(0).plot(new double[]{1, 2, 3, 4});
        Plot.asPolar().polarAngleOffsetDegree(-90).plot(new double[]{1, 2, 3, 4});
    }
}
