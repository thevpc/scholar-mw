package net.vpc.scholar.hadruwaves.mom.console.yaxis;

import net.vpc.scholar.hadrumaths.Vector;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.NamedVector;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadruwaves.console.yaxis.PlotAxisSeriesComplexVector;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadrumaths.plot.console.params.ParamSet;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;
import net.vpc.scholar.hadrumaths.plot.PlotType;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Matrix;

import java.util.ArrayList;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 31 juil. 2007 23:00:27
 */
public abstract class PlotAxisSeriesMatrixContent extends PlotAxisSeriesComplexVector {

    protected PlotAxisSeriesMatrixContent(String name, YType... type) {
        super(name, type);
    }

    protected PlotAxisSeriesMatrixContent(String name, YType[] type, PlotType graphix) {
        super(name, type, graphix);
    }

    protected abstract Matrix computeMatrixItems(MomStructure structure, ParamSet x);

    protected final NamedVector computeComplexes(MomStructure structure, ParamSet x, ConsoleActionParams p) {
        return convertMatrixToNamedVector(true, computeMatrixItems(structure, x), getName(), getName(), p);
    }

    protected NamedVector convertMatrixToNamedVector(boolean all, Matrix matrix, String name, String shortName, ConsoleActionParams p) {
        ArrayList<Complex> a1 = new ArrayList<Complex>();
        ArrayList<String> t1 = new ArrayList<String>();
        int rows = matrix.getRowCount();
        int columns = matrix.getColumnCount();
        for (int i = 0; i < rows; i++) {
            Vector ri = matrix.getRow(i);
            for (int j = 0; j < columns; j++) {
                if (all || (i == 0 && j == 0)) {
                    a1.add(ri.get(j));
                    t1.add(shortName + (i + 1) + "" + (j + 1)+""+p.getSerieTitle());
                }
            }
        }
        double[] xx = new double[a1.size()];
        for (int i = 0; i < xx.length; i++) {
            xx[i] = i + 1;
        }
        return new NamedVector(name+p.getSerieTitle(), a1.toArray(new Complex[a1.size()]), xx, t1.toArray(new String[t1.size()]));
    }
}
