//package net.thevpc.scholar.tmwlib.mom.console.yaxis;
//
//import net.thevpc.scholar.math.Complex;
//
//import net.thevpc.scholar.math.plot.console.paramsets.ParamSet;
//import net.thevpc.scholar.tmwlib.mom.MomStructure;
//import net.thevpc.scholar.math.plot.console.yaxis.YType;
//import net.thevpc.scholar.math.plot.console.ConsoleActionParams;
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
