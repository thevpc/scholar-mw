//package net.vpc.scholar.tmwlib.mom.console.yaxis;
//
//import net.vpc.scholar.math.Complex;
//
//import net.vpc.scholar.math.DMatrix;
//import net.vpc.scholar.math.Matrix;
//import net.vpc.scholar.math.util.ProgressMonitor;
//import net.vpc.scholar.tmwlib.console.yaxis.PlotAxisSeriesSingleValue;
//import net.vpc.scholar.tmwlib.mom.MomStructure;
//import net.vpc.scholar.math.plot.console.yaxis.YType;
//import net.vpc.scholar.math.plot.console.paramsets.ParamSet;
//import net.vpc.scholar.math.plot.console.ConsoleActionParams;
//
//public class PlotTestConvergenceForZin extends PlotAxisSeriesSingleValue implements Cloneable {
//    private int start;
//    private double error;
//    private boolean returnActualGpCount;
//
//    public PlotTestConvergenceForZin(YType type, int start, double error, boolean returnActualGpCount) {
//        super("TestConvergence(start="+(start)+",err="+(error)+","+(returnActualGpCount?"Gpcount":"GpZoneMax")+")",type);
//        this.start = start;
//        this.error = error;
//        this.returnActualGpCount = returnActualGpCount;
//    }
//
//    protected Complex computeComplexArg(MomStructure structure, ParamSet x, ConsoleActionParams p) {
//        return new Complex(computeTestConvergenceForZs(structure,this.start, this.error,this.returnActualGpCount,this));
//    }
//
//    /**
//     * @param start               minimum Gp count (start with), it corresponds to GpTestMax
//     *                            and not to getGpTestFunctions().gpCache().length
//     * @param relativeError       target relative error to stop iterations
//     * @param returnActualGpCount if true return
//     *                            getGpTestFunctions().gpCache().length otherwise return GpTestMax
//     * @return number* of Test functions needed to converge. (*) number is
//     * getGpTestFunctions().gpCache().length if returnActualGpCount is true, if
//     * not it returns GpTestMax
//     */
//    public int computeTestConvergenceForZs(MomStructure str,int start, double relativeError, boolean returnActualGpCount, ProgressMonitor monitor) {
//        int i = start;
//        int x = str.testFunctionsCount();
//        str.setTestFunctionsCount(i);
//        Matrix old = str.inputImpedance().monitor(monitor).computeMatrix();
//        //        System.out.println("old="+old);
//        while (true) {
//            i++;
//            str.setTestFunctionsCount(i);
//            Matrix m = str.inputImpedance().monitor(monitor).computeMatrix();
//            DMatrix err = m.getErrorMatrix(old, relativeError);
//            System.out.println(getClass() + ":" + i + " : ERR =" + err.maxAbs());
//            if (err.maxAbs() == 0) {
//                break;
//            }
//            old = m;
//        }
//        int realCount = str.testFunctions().arr().length;
//        str.setTestFunctionsCount(x);
//        return returnActualGpCount ? realCount : i;
//    }
//}
