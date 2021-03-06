package net.thevpc.scholar.hadruwaves.mom.console.yaxis;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruplot.console.params.ParamSet;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadruplot.PlotAxisSeriesSingleValue;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

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
