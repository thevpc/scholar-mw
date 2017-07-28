package net.vpc.scholar.hadruwaves.mom.console.yaxis;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;
import net.vpc.scholar.hadrumaths.plot.console.params.ParamSet;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadruwaves.console.yaxis.PlotAxisSeriesComplex;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

public class PlotMatrixACond extends PlotAxisSeriesComplex implements Cloneable {

    public PlotMatrixACond(YType... type) {
        super("ACond", type);
    }

    @Override
    protected Complex computeComplex(MomStructure structure, ParamSet x,ConsoleActionParams p) {
        double cond = structure.matrixA().monitor(this).computeMatrix().cond();
        //int ndx=x.getMode();
        //if(cond>2000){
        //    structure.wdebug(plotTitle, new Throwable(),structure.computeMatrixA());
        //}
        return Complex.valueOf(cond);
    }

    @Override
    public String toString() {
        return "ACond";
    }
}
