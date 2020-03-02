package net.vpc.scholar.hadruwaves.mom.console.yaxis;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadruplot.console.ConsoleActionParams;
import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruplot.console.params.ParamSet;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.scholar.hadruplot.PlotAxisSeriesSingleValue;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

public class PlotMatrixACond extends PlotAxisSeriesSingleValue implements Cloneable {

    public PlotMatrixACond(YType... type) {
        super("ACond", type);
    }

    @Override
    protected Complex evalComplex(ConsoleAwareObject structure, ParamSet x, ConsoleActionParams p) {
        double cond = ((MomStructure)structure).matrixA().monitor(this).evalMatrix().cond();
        //int ndx=x.getMode();
        //if(cond>2000){
        //    structure.wdebug(plotTitle, new Throwable(),structure.computeMatrixA());
        //}
        return Complex.of(cond);
    }

    @Override
    public String toString() {
        return "ACond";
    }
}
