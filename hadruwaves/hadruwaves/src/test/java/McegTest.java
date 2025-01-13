//package test;
//
//import static net.thevpc.scholar.math.functions.FunctionFactory.cosXcosY;
//import static net.thevpc.scholar.math.functions.FunctionFactory.cst;
//import static net.thevpc.scholar.math.functions.FunctionFactory.line;
//
//import java.text.DecimalFormat;
//import java.text.DecimalFormatSymbols;
//import java.util.ArrayList;
//
//import net.thevpc.scholar.math.functions.DomainXY;
//import net.thevpc.scholar.math.functions.FunctionFactory;
//import net.thevpc.scholar.math.functions.dfx.DFunctionYIntegralX;
//import net.thevpc.scholar.math.functions.dfxy.DCosCosFunctionXY;
//import net.thevpc.scholar.math.functions.dfxy.DCstFunctionXY;
//import net.thevpc.scholar.math.functions.dfxy.DFunctionProductXY;
//import net.thevpc.scholar.math.functions.dfxy.DSumFunctionXY;
//import net.thevpc.scholar.math.functions.dfxy.DFunctionXY;
//import net.thevpc.scholar.math.functions.dfxy.DFunctionXYFromX;
//import net.thevpc.scholar.math.functions.dfxy.DLinearFunctionXY;
//import net.thevpc.scholar.math.functions.dfxy.RooftopXFunctionXY;
//import net.thevpc.scholar.math.integration.DQuadIntegralX;
//import net.thevpc.scholar.math.integration.DQuadIntegralXY;
//import net.thevpc.scholar.math.interop.matlab.MatlabDoubleFomat;
//import net.thevpc.scholar.math.plot.Plot;
//import net.thevpc.scholar.math.scalarproducts.ScalarProduct;
//import net.thevpc.scholar.math.scalarproducts.numeric.NumericScalarProduct;
//import net.thevpc.scholar.math.Axis;
//import net.thevpc.scholar.math.Math2;
//
//public class McegTest {
//    public static void main(String[] args) {
//        main1();
//    }
//
//
//    public static void main1() {
//        DecimalFormat f = new DecimalFormat("#.#");
//        DecimalFormatSymbols symbols = f.getDecimalFormatSymbols();
//        symbols.setDecimalSeparator('.');
//        f.setDecimalFormatSymbols(symbols);
//        MatlabDoubleFomat mtlbFomat = new MatlabDoubleFomat(f);
//        DomainXY domain = new DomainXY(0, 0, 100, 100);
//        ScalarProduct nsp = Math2.NUMERIC_SCALAR_PRODUCT;
//        ScalarProduct fsp = Math2.FORMAL_SCALAR_PRODUCT;
//        int max = 100;
//        int ok = 0;
//        long s = 0;
//        long t = 0;
//        double tolerance = 1.0;
//        int filter = FunctionsFilter.CST
//                + FunctionsFilter.LINEAR
//                + FunctionsFilter.ROOFTOP
//                + FunctionsFilter.COSCOS
//                + FunctionsFilter.SUM;
////        ProgressMonitor monitor = new ProgressMonitor(null, null, null, 0, max);
//
//        boolean trace = true;
//        DFunctionXY[] f1 = new DFunctionXY[max];
//        DFunctionXY[] f2 = new DFunctionXY[max];
//        double[] nps = new double[max];
//        double[] fps = new double[max];
//        long ftime = 0;
//        long ntime = 0;
//        for (int i = 0; i < max; i++) {
//            f1[i] = random(domain, filter, ValuesType.DOUBLE);
//            f2[i] = random(domain, filter, ValuesType.DOUBLE);
//        }
//
//        s = System.currentTimeMillis();
//        for (int i = 0; i < max; i++) {
//            System.out.println("[" + i + "] Formal SP");
//            fps[i] = fsp.scalarProduct(f1[i], f2[i]);
//        }
//        t = (System.currentTimeMillis() - s);
//        ftime = t;
//        System.out.println("Formal time : " + ftime);
//        s = System.currentTimeMillis();
//        for (int i = 0; i < max; i++) {
//            System.out.println("[" + i + "] Numeric SP");
//            System.out.println("\tf1=" + f1[i].toMatlapString(mtlbFomat));
//            System.out.println("\tf2=" + f2[i].toMatlapString(mtlbFomat));
//            DomainXY inter = f1[i].intersect(f2[i]);
//            System.out.println("\tmtlb_val=dblquad('" + new DFunctionProductXY(f1[i], f2[i]).toMatlapString(mtlbFomat) + "'," + inter.xmin + "," + inter.xmax + "," + inter.ymin + "," + inter.ymax + ")");
//            long s0 = System.currentTimeMillis();
//            nps[i] = nsp.scalarProduct(f1[i], f2[i]);
//            System.out.println("\tvalue=" + nps[i]);
//            System.out.println("\ttoc=" + (System.currentTimeMillis() - s0));
//        }
//        t = (System.currentTimeMillis() - s);
//        ntime = t;
//        System.out.println("Numeric time : " + ntime + " = ( " + ((double) ntime / ftime) + " of formal time)");
//
//        for (int i = 0; i < max; i++) {
//            double err = Math.abs((nps[i] - fps[i]) / fps[i]) * 100;
//            if (err > tolerance) {
//                System.out.println((i + 1) + ":trouble " + err + " : types : " + f1.getClass().getSimpleName() + "/" + f2.getClass().getSimpleName() + " : values :" + nps[i] + "/" + fps[i]);
//                System.out.println("\tf1=" + f1[i].toMatlapString(mtlbFomat));
//                System.out.println("\tf2=" + f1[i].toMatlapString(mtlbFomat));
//                DomainXY inter = f1[i].intersect(f2[i]);
//                System.out.println("\tmtlb_val=dblquad('" + new DFunctionProductXY(f1[i], f2[i]).toMatlapString(mtlbFomat) + "'," + inter.xmin + "," + inter.xmax + "," + inter.ymin + "," + inter.ymax + ")");
//            } else {
////                if (trace) {
////                    System.out.println((i + 1) + ":Ok");
////                }
//                ok++;
//            }
//        }
//        System.out.println("ok = " + ok + "/" + max + " : (numerictime : " + ntime + ") / (formaltime : " + ftime + ") : ==> " + (((double) ntime) / ftime));
//    }
//
//    public static void main2() {
////
////        f1=gate(x,y,50,75,96,112) * 51.0 * cos(20 * x + 63) * cos(82 * y + 41)
////        f2=gate(x,y,57,68,80,105) * (75 * x + 46)
//
//        DFunctionXY f1 = cosXcosY(51.0, 20, 63, 82, 41, new DomainXY(50, 96, 75, 112));
//        DFunctionXY f2 = line(75, 0,46, new DomainXY(57, 80, 68, 105));
//        System.out.println("result1 = " + Math2.FORMAL_SCALAR_PRODUCT.scalarProduct(f1, f2));
//        System.out.println("result2 = " + new NumericScalarProduct(new DQuadIntegralXY(new DQuadIntegralX(1E-5, 1E-52, 0.1, -1))).scalarProduct(f1, f2));
//    }
//
//    public static void main3() {
//        //DomainXY domain = new DomainXY(0, 1, 0, 1);
//        ScalarProduct nsp = Math2.NUMERIC_SCALAR_PRODUCT;
//        ScalarProduct fsp = Math2.FORMAL_SCALAR_PRODUCT;
//        long ns = 0;
//        long fs = 0;
//        long s = 0;
//        long t = 0;
//        //int filer = 1 + 0+0+0+16; // cst+sum
//        boolean trace = true;
//        DFunctionXY f1 = cst(2, new DomainXY(0, 50, 1, 100)).add(cst(8, new DomainXY(0, 54, 1, 60)));
//        DFunctionXY f2 = cst(1, new DomainXY(0, 50, 1, 100));
////        DFunctionXY f1 = cst(1, new DomainXY(0, 1, 0, 8)).add(cst(1, new DomainXY(0, 1, 1, 1.5)));
////        DFunctionXY f2 = cst(1, new DomainXY(0, 1, 0, 7.4));
//        DFunctionXY f3 = new DFunctionProductXY(f1, f2);
//        DFunctionXY f3s = FunctionFactory.simplify(f3);
//        DFunctionXY f4 = new DFunctionXYFromX(new DFunctionYIntegralX(f3), 0, 100);
//        Plot.showAllFunctionsXYPlot("f1/f2", new DFunctionXY[]{
//            f1,
//            f2,
//            f3,
//            f3s,
//            f4
//        }, null);
//        if (trace) {
//            System.out.println("evaluating ...");
//            System.out.println("\tf1=" + f1.toMatlapString());
//            System.out.println("\tf2=" + f2.toMatlapString());
//        }
//
//        if (trace) {
//            System.out.print("\tformal...=");
//        }
//        s = System.currentTimeMillis();
//        double fv = fsp.scalarProduct(f1, f2);
//        t = (System.currentTimeMillis() - s);
//        fs += t;
//        if (trace) {
//            System.out.println(t + "->" + fs);
//        }
//
//        if (trace) {
//            System.out.print("\tnumeric...=");
//        }
//        s = System.currentTimeMillis();
//        double nv = nsp.scalarProduct(f1, f2);
//        t = (System.currentTimeMillis() - s);
//        ns += t;
//        if (trace) {
//            System.out.println(t + "->" + ns);
//        }
//
//        double err = Math.abs((nv - fv) / nv) * 100;
//        if (err > 10) {
//            System.out.println("trouble " + err + " : types : " + f1.getClass().getSimpleName() + "/" + f2.getClass().getSimpleName() + " : values :" + nv + "/" + fv);
//            System.out.println("\tf1=" + f1.toMatlapString());
//            System.out.println("\tf2=" + f2.toMatlapString());
//        } else {
//            if (trace) {
//                System.out.println("Ok");
//            }
//        }
//
//        System.out.println(" time : " + ns + "/" + fs + " : " + (ns / fs));
//    }
//
//    public static void main4() {
//        ScalarProduct nsp = Math2.NUMERIC_SCALAR_PRODUCT;
//        ScalarProduct fsp = Math2.FORMAL_SCALAR_PRODUCT;
//
//        DFunctionXY f1 = cst(2, new DomainXY(0, 0, 1, 8)).add(cst(8, new DomainXY(0, 1, 1, 1.5)));
//        DFunctionXY f2 = cst(4, new DomainXY(0, 0, 1, 3.1));
//        double fv = fsp.scalarProduct(f1, f2);
//        double nv = nsp.scalarProduct(f1, f2);
//        double err = Math.abs((nv - fv) / nv) * 100;
//        if (err >= 10) {
//            System.out.println("trouble " + err + " : types : " + f1.getClass().getSimpleName() + "/" + f2.getClass().getSimpleName() + " : values :" + nv + "/" + fv);
//            System.out.println("\tf1=" + f1.toMatlapString());
//            System.out.println("\tf2=" + f2.toMatlapString());
//        }
//
//    }
//
//    public static void main5() {
//        DFunctionXY f1 = cst(16, new DomainXY(16, 46, 66, 126)).add(cst(98, new DomainXY(20, 54, 34, 61)));
//        DFunctionXY f2 = cst(1, new DomainXY(0, 0, 10000, 100000));
//        DFunctionXY f3 = new DFunctionProductXY(f1, f2);
//        DFunctionXY f4 = new DFunctionXYFromX(new DFunctionYIntegralX(f3), 0, 100);
//        Plot.showAllFunctionsXYPlot("f1/f2", new DFunctionXY[]{
////            f1,
////            f2,
////            f3,
//            f4
//        }, null);
//
//    }
//
//
//    private static enum ValuesType {
//        DOUBLE_2,INTEGER,DOUBLE
//    }
//
//    private static class FunctionsFilter {
//        public static final int CST = 1;
//        public static final int LINEAR = 2;
//        public static final int ROOFTOP = 4;
//        public static final int COSCOS = 8;
//        public static final int SUM = 16;
//    }
//
//    private static double random_simplify(double d, ValuesType simpleValues) {
//        switch (simpleValues) {
//            case DOUBLE_2:
//                {
//                    return Math.floor(d * 100) / 100.0;
//                }
//            case INTEGER:
//                {
//                    return Math.floor(d);
//                }
//        }
//        return d;
//    }
//
//    public static DFunctionXY random(DomainXY domain, int filter, ValuesType simpleValues) {
////        double w=domain.width;
//        double x0 = domain.xmin + Math.random() * domain.width;
//        double w0 = Math.random() * (domain.width - x0);
//        double y0 = domain.ymin + Math.random() * domain.height;
//        double h0 = Math.random() * (domain.height - x0);
//        DomainXY d = new DomainXY(random_simplify(x0, simpleValues), random_simplify(y0, simpleValues), random_simplify(x0 + w0, simpleValues), random_simplify(y0 + h0, simpleValues));
//        ArrayList<Integer> a = new ArrayList<Integer>();
//        if ((filter & FunctionsFilter.CST) != 0) {
//            a.add(0);
//        }
//        if ((filter & FunctionsFilter.LINEAR) != 0) {
//            a.add(1);
//        }
//        if ((filter & FunctionsFilter.ROOFTOP) != 0) {
//            a.add(2);
//        }
//        if ((filter & FunctionsFilter.COSCOS) != 0) {
//            a.add(3);
//        }
//        if ((filter & FunctionsFilter.SUM) != 0) {
//            a.add(4);
//        }
//        double rd = a.size() * Math.random();
//        int r = a.get((int) rd);
//
//        double ax = random_simplify(100 * Math.random(), simpleValues);
//        double bx = random_simplify(100 * Math.random(), simpleValues);
//        double cx = random_simplify(100 * Math.random(), simpleValues);
//        double dx = random_simplify(100 * Math.random(), simpleValues);
//        double ex = random_simplify(100 * Math.random(), simpleValues);
//        switch (r) {
//            case 0:
//                return new DCstFunctionXY(ax, d);
//            case 1:
//                return new DLinearFunctionXY(ax, bx, cx,d);
//            case 2:
//                return new RooftopXFunctionXY(d, bx<50?Axis.X:Axis.Y,ax, (int) (5 * Math.random()), true);
//            case 3:
//                return new DCosCosFunctionXY(ax, bx, cx, dx, ex, d);
//            case 4:
//                {
//                    DFunctionXY[] f = new DFunctionXY[(int) (2 * Math.random()) + 1];
////                    System.out.println("sum : "+f.length);
//                    for (int i = 0; i < f.length; i++) {
//                        f[i] = random(domain, filter & (1 + 2 + 4 + 8), simpleValues);
//                    }
//                    return new DSumFunctionXY(f);
//                }
//        }
//        return new DCosCosFunctionXY(100 * Math.random(), 100 * Math.random(), 100 * Math.random(), 100 * Math.random(), 100 * Math.random(), d);
//    }
//}
