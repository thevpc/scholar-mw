package net.vpc.scholar.hadruplot;

import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.scholar.hadruplot.console.params.ParamSet;
import net.vpc.scholar.hadruplot.console.ConsoleActionParams;

import java.util.ArrayList;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 31 juil. 2007 23:00:27
 */
public abstract class PlotAxisSeriesMatrixValue extends PlotAxisSeriesRow {

    protected PlotAxisSeriesMatrixValue(String name, YType... type) {
        super(name, type);
    }

    protected PlotAxisSeriesMatrixValue(String name, YType[] type, PlotType graphix) {
        super(name, type, graphix);
    }

    protected abstract Object[][] evalMatrixItems(ConsoleAwareObject structure, ParamSet x);

    protected final PlotNamedVector evalComplexes(ConsoleAwareObject structure, ParamSet x, ConsoleActionParams p) {
        return convertMatrixToNamedVector(true, evalMatrixItems(structure, x), getName(), getName(), p);
    }

    protected PlotNamedVector convertMatrixToNamedVector(boolean all, Object[][] matrix, String name, String shortName, ConsoleActionParams p) {
        ArrayList<Object> a1 = new ArrayList<Object>();
        ArrayList<String> t1 = new ArrayList<String>();
        int rows = matrix.length;
        int columns = rows == 0 ? 0 : matrix[0].length;
        for (int i = 0; i < rows; i++) {
            Object[] ri = matrix[i];
            for (int j = 0; j < columns; j++) {
                if (all || (i == 0 && j == 0)) {
                    a1.add(ri[j]);
                    t1.add(shortName + (i + 1) + "" + (j + 1) + "" + p.getSerieTitle());
                }
            }
        }
        double[] xx = new double[a1.size()];
        for (int i = 0; i < xx.length; i++) {
            xx[i] = i + 1;
        }
        return new PlotNamedVector(name + p.getSerieTitle(), a1.toArray(), xx, t1.toArray(new String[0]));
    }
}
