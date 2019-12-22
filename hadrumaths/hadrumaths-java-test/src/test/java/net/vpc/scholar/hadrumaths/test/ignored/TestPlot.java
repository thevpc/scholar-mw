package net.vpc.scholar.hadrumaths.test.ignored;


import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadruplot.Plot;
import net.vpc.scholar.hadruplot.console.PlotConsole;

import static net.vpc.scholar.hadrumaths.Maths.*;

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
        ComplexMatrix m=matrix(items);
        PlotConsole c= Plot.console();
        c.newPlot().title("Une matrice").asMatrix().plot(m);
        c.newPlot().title("Une matrice").asMatrix().plot(cos(X));
    }

}
