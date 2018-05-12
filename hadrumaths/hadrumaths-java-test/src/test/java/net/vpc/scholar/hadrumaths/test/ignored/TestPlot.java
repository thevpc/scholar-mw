package net.vpc.scholar.hadrumaths.test.ignored;


import static net.vpc.scholar.hadrumaths.Maths.*;

import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.Plot;
import net.vpc.scholar.hadrumaths.plot.console.PlotConsole;

/**
 *
 * @author vpc
 */
public class TestPlot {

    public static void main(String[] args) {
        double[][] items=new double[100][];
        for(int i=0;i<items.length;i++){
            double step=tan((i*PI/items.length));
            items[i]=cos(dtimes(step, PI/2+step,5));
        }
        Matrix m=matrix(items);
        Plot.title("Une matrice").asMatrix().plot(m);
        PlotConsole c=Plot.console();
        c.start();
    }

}
