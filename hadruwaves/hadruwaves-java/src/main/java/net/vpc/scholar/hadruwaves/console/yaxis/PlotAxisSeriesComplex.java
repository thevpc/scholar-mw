package net.vpc.scholar.hadruwaves.console.yaxis;

import net.vpc.scholar.hadrumaths.plot.console.params.ParamSet;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.NamedVector;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;
import net.vpc.scholar.hadrumaths.plot.PlotType;
import net.vpc.scholar.hadrumaths.Complex;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 31 juil. 2007 23:00:27
 */
public abstract class PlotAxisSeriesComplex extends PlotAxisSeriesComplexVector {

    protected PlotAxisSeriesComplex(String name, YType... type) {
        super(name, type);
    }

    protected PlotAxisSeriesComplex(String name, YType[] type, PlotType graphix) {
        super(name, type, graphix);
    }

    protected abstract Complex computeComplex(MomStructure structure, ParamSet x, ConsoleActionParams p);

    @Override
    protected final NamedVector computeComplexes(MomStructure structure, ParamSet x, ConsoleActionParams p) {
        return new NamedVector(toString()+"-"+p.getSerieTitle(), new Complex[]{computeComplex(structure, x, p)}, new double[]{1}, new String[]{toString()});
    }
}
