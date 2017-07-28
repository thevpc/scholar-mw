package net.vpc.scholar.hadruwaves.mom.console.yaxis;

//package net.vpc.scholar.tmwlib.mom.console.yaxis;
//
//import net.vpc.scholar.math.Complex;
//
//import net.vpc.scholar.tmwlib.mom.MomStructure;
//import net.vpc.scholar.tmwlib.mom.enums.YType;
//import net.vpc.scholar.math.plot.plotconsole.paramsets.ParamSet;
//import net.vpc.scholar.math.plot.plotconsole.ConsoleActionParams;
//
//@Deprecated
//public class Y_Zin2 extends PlotAxisSeriesComplex implements Cloneable {
//    private int x;
//    private int y;
//
//    public Y_Zin2(YType... type) {
//        this(type, 1, 1);
//    }
//
//    public Y_Zin2(YType[] type, int x, int y) {
//        super("Z'"+(x)+""+(y),type);
//        this.x = x - 1;
//        this.y = y - 1;
//    }
//
//    protected Complex computeComplex(MomStructure structure, ParamSet x, ConsoleActionParams p) {
//        return structure.computeZin2().get(this.x, this.y);
//    }
//}
