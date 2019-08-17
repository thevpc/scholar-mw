//package net.vpc.scholar.tmwlib.mom.console.yaxis;
//
//import net.vpc.scholar.math.Complex;
//
//import net.vpc.scholar.math.plot.console.paramsets.ParamSet;
//import net.vpc.scholar.tmwlib.mom.MomStructure;
//import net.vpc.scholar.math.plot.console.yaxis.YType;
//import net.vpc.scholar.math.plot.console.ConsoleActionParams;
//
//public class PlotFixedPoint extends PlotAxisSeriesSingleValue implements Cloneable {
//    private int x;
//    private int y;
//
//    public PlotFixedPoint(YType... type) {
//        this(type, 1, 1);
//    }
//
//    public PlotFixedPoint(YType[] type, int x, int y) {
//        super("PtFixe"+(x)+(y),type);
//        this.x = x - 1;
//        this.y = y - 1;
//    }
//
//    protected Complex computeComplexArg(MomStructure structure, ParamSet x, ConsoleActionParams p) {
//        return structure.computeFixedPoint(null).get(this.x, this.y);
//    }
//}
