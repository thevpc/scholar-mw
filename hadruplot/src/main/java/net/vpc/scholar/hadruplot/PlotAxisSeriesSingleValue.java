package net.vpc.scholar.hadruplot;

import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruplot.console.params.ParamSet;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.scholar.hadruplot.console.ConsoleActionParams;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 31 juil. 2007 23:00:27
 */
public abstract class PlotAxisSeriesSingleValue extends PlotAxisSeriesRow {

    protected PlotAxisSeriesSingleValue(String name, YType... type) {
        super(name, type);
    }

    protected PlotAxisSeriesSingleValue(String name, YType[] type, PlotType graphix) {
        super(name, type, graphix);
    }

    protected abstract Object evalComplex(ConsoleAwareObject structure, ParamSet x, ConsoleActionParams p);

    @Override
    protected final PlotNamedVector evalComplexes(ConsoleAwareObject structure, ParamSet x, ConsoleActionParams p) {
        return new PlotNamedVector(toString()+"-"+p.getSerieTitle(), new Object[]{evalComplex(structure, x, p)}, new double[]{1}, new String[]{toString()});
    }
}
