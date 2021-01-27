//package net.thevpc.scholar.math.scalarproducts.formal;
//
//import net.thevpc.scholar.math.functions.DomainXY;
//import net.thevpc.scholar.math.functions.dfxy.DCosCosFunctionXY;
//import net.thevpc.scholar.math.functions.dfxy.DCosCosFunctionXY;
//import net.thevpc.scholar.math.functions.dfxy.DLinearFunctionXY;
//import net.thevpc.scholar.math.functions.dfxy.DCstFunctionXY;
//import net.thevpc.scholar.math.functions.dfxy.DCosCosFunctionXY;
//import net.thevpc.scholar.math.functions.dfxy.DCstFunctionXY;
//
//
///**
// * User: taha
// * Date: 2 juil. 2003
// * Time: 13:10:16
// */
//class ScalarProductHelper {
//
//    public static double ps_cc_cc(DomainXY a, DCosCosFunctionXY f1, DCosCosFunctionXY f2) {
//        double b1 = a.xmin;
//        double b2 = a.xmax;
//        double b3 = a.ymin;
//        double b4 = a.ymax;
//        return _ps_cc_cc(b1, b3, f1, f2)
//                + _ps_cc_cc(b2, b4, f1, f2)
//                - _ps_cc_cc(b1, b4, f1, f2)
//                - _ps_cc_cc(b2, b3, f1, f2);
//    }
//
//    public static double ps_cc_x(DomainXY a, DCosCosFunctionXY f1, DLinearFunctionXY f2) {
//        double b1 = a.xmin;
//        double b2 = a.xmax;
//        double b3 = a.ymin;
//        double b4 = a.ymax;
//        return _ps_cc_x(b1, b3, f1, f2)
//                + _ps_cc_x(b2, b4, f1, f2)
//                - _ps_cc_x(b1, b4, f1, f2)
//                - _ps_cc_x(b2, b3, f1, f2);
//    }
//
//    public static double ps_cc_cst(DomainXY a, DCosCosFunctionXY f1, DCstFunctionXY f2) {
//        double b1 = a.xmin;
//        double b2 = a.xmax;
//        double b3 = a.ymin;
//        double b4 = a.ymax;
//        return _ps_cc_cst(b1, b3, f1, f2)
//                + _ps_cc_cst(b2, b4, f1, f2)
//                - _ps_cc_cst(b1, b4, f1, f2)
//                - _ps_cc_cst(b2, b3, f1, f2);
//    }
//
//    private static double _ps_cc_cc(double x, double y, DCosCosFunctionXY f1, DCosCosFunctionXY f2) {
//        if (f1.a == 0 && f1.c == 0 && f2.a == 0 && f2.c == 0)
//            return f1.amp * Math.cos(f1.b) * Math.cos(f1.d) * f2.amp * Math.cos(f2.b) * Math.cos(f2.d) * x * y;
//
//        else if (f1.a != 0 && f1.c == 0 && f2.a == 0 && f2.c == 0)
//            return f1.amp * Math.cos(f1.d) * f2.amp * Math.cos(f2.b) * Math.cos(f2.d) / f1.a * Math.sin(f1.a * x + f1.b) * y;
//
//        else if (f1.a == 0 && f1.c != 0 && f2.a == 0 && f2.c == 0)
//            return f1.amp * Math.cos(f1.b) * f2.amp * Math.cos(f2.b) * Math.cos(f2.d) * x / f1.c * Math.sin(f1.c * y + f1.d);
//
//        else if (f1.a != 0 && f1.c != 0 && f2.a == 0 && f2.c == 0)
//            return f1.amp * f2.amp * Math.cos(f2.b) * Math.cos(f2.d) / f1.a * Math.sin(f1.a * x + f1.b) / f1.c * Math.sin(f1.c * y + f1.d);
//
//        else if (f1.a == 0 && f1.c == 0 && f2.a != 0 && f2.c == 0)
//            return f1.amp * Math.cos(f1.b) * Math.cos(f1.d) * f2.amp * Math.cos(f2.d) / f2.a * Math.sin(f2.a * x + f2.b) * y;
//
//        else if (f1.a != 0 && f1.c == 0 && f2.a != 0 && f2.c == 0)
//            return f1.amp * Math.cos(f1.d) * f2.amp * Math.cos(f2.d) * (1 / 2 / (f1.a - f2.a) * Math.sin((f1.a - f2.a) * x + f1.b - f2.b) + 1 / 2 / (f1.a + f2.a) * Math.sin((f1.a + f2.a) * x + f1.b + f2.b)) * y;
//
//        else if (f1.a == 0 && f1.c != 0 && f2.a != 0 && f2.c == 0)
//            return f1.amp * Math.cos(f1.b) * f2.amp * Math.cos(f2.d) / f2.a * Math.sin(f2.a * x + f2.b) / f1.c * Math.sin(f1.c * y + f1.d);
//
//        else if (f1.a != 0 && f1.c != 0 && f2.a != 0 && f2.c == 0)
//            return f1.amp * f2.amp * Math.cos(f2.d) * (1 / 2 / (f1.a - f2.a) * Math.sin((f1.a - f2.a) * x + f1.b - f2.b) + 1 / 2 / (f1.a + f2.a) * Math.sin((f1.a + f2.a) * x + f1.b + f2.b)) / f1.c * Math.sin(f1.c * y + f1.d);
//
//        else if (f1.a == 0 && f1.c == 0 && f2.a == 0 && f2.c != 0)
//            return f1.amp * Math.cos(f1.b) * Math.cos(f1.d) * f2.amp * Math.cos(f2.b) * x / f2.c * Math.sin(f2.c * y + f2.d);
//
//        else if (f1.a != 0 && f1.c == 0 && f2.a == 0 && f2.c != 0)
//            return f1.amp * Math.cos(f1.d) * f2.amp * Math.cos(f2.b) / f1.a * Math.sin(f1.a * x + f1.b) / f2.c * Math.sin(f2.c * y + f2.d);
//
//        else if (f1.a == 0 && f1.c != 0 && f2.a == 0 && f2.c != 0)
//            return f1.amp * Math.cos(f1.b) * f2.amp * Math.cos(f2.b) * x * (1 / 2 / (f1.c - f2.c) * Math.sin((f1.c - f2.c) * y + f1.d - f2.d) + 1 / 2 / (f1.c + f2.c) * Math.sin((f1.c + f2.c) * y + f1.d + f2.d));
//
//        else if (f1.a != 0 && f1.c != 0 && f2.a == 0 && f2.c != 0)
//            return f1.amp * f2.amp * Math.cos(f2.b) / f1.a * Math.sin(f1.a * x + f1.b) * (1 / 2 / (f1.c - f2.c) * Math.sin((f1.c - f2.c) * y + f1.d - f2.d) + 1 / 2 / (f1.c + f2.c) * Math.sin((f1.c + f2.c) * y + f1.d + f2.d));
//
//        else if (f1.a == 0 && f1.c == 0 && f2.a != 0 && f2.c != 0)
//            return f1.amp * Math.cos(f1.b) * Math.cos(f1.d) * f2.amp / f2.a * Math.sin(f2.a * x + f2.b) / f2.c * Math.sin(f2.c * y + f2.d);
//
//        else if (f1.a != 0 && f1.c == 0 && f2.a != 0 && f2.c != 0)
//            return f1.amp * Math.cos(f1.d) * f2.amp * (1 / 2 / (f1.a - f2.a) * Math.sin((f1.a - f2.a) * x + f1.b - f2.b) + 1 / 2 / (f1.a + f2.a) * Math.sin((f1.a + f2.a) * x + f1.b + f2.b)) / f2.c * Math.sin(f2.c * y + f2.d);
//
//        else if (f1.a == 0 && f1.c != 0 && f2.a != 0 && f2.c != 0)
//            return f1.amp * Math.cos(f1.b) * f2.amp / f2.a * Math.sin(f2.a * x + f2.b) * (1 / 2 / (f1.c - f2.c) * Math.sin((f1.c - f2.c) * y + f1.d - f2.d) + 1 / 2 / (f1.c + f2.c) * Math.sin((f1.c + f2.c) * y + f1.d + f2.d));
//
//        else //none is null
//            return f1.amp * f2.amp * (1 / 2 / (f1.a - f2.a) * Math.sin((f1.a - f2.a) * x + f1.b - f2.b) + 1 / 2 / (f1.a + f2.a) * Math.sin((f1.a + f2.a) * x + f1.b + f2.b)) * (1 / 2 / (f1.c - f2.c) * Math.sin((f1.c - f2.c) * y + f1.d - f2.d) + 1 / 2 / (f1.c + f2.c) * Math.sin((f1.c + f2.c) * y + f1.d + f2.d));
//    }
//
//
//    private static double _ps_cc_x(double x, double y, DCosCosFunctionXY f, DLinearFunctionXY g) {
//        if (f.a == 0 && f.c == 0 && g.a == 0)
//            return f.amp * Math.cos(f.b) * Math.cos(f.d) * g.b * x * y;
//
//        else if (f.a != 0 && f.c == 0 && g.a == 0)
//            return f.amp * Math.cos(f.d) * g.b / f.a * Math.sin(f.a * x + f.b) * y;
//
//        else if (f.a == 0 && f.c != 0 && g.a == 0)
//            return f.amp * Math.cos(f.b) * g.b * x / f.c * Math.sin(f.c * y + f.d);
//
//        else if (f.a != 0 && f.c != 0 && g.a == 0)
//            return f.amp * g.b / f.a * Math.sin(f.a * x + f.b) / f.c * Math.sin(f.c * y + f.d);
//
//        else if (f.a == 0 && f.c == 0 && g.a != 0)
//            return f.amp * Math.cos(f.b) * Math.cos(f.d) * (1 / 2 * g.a * x * x + g.b * x) * y;
//
//        else if (f.a != 0 && f.c == 0 && g.a != 0)
//            return f.amp * Math.cos(f.d) / f.a * (g.a / f.a * (Math.cos(f.a * x + f.b) + (f.a * x + f.b) * Math.sin(f.a * x + f.b)) - g.a / f.a * f.b * Math.sin(f.a * x + f.b) + g.b * Math.sin(f.a * x + f.b)) * y;
//
//        else if (f.a == 0 && f.c != 0 && g.a != 0)
//            return f.amp * Math.cos(f.b) * (1 / 2 * g.a * x * x + g.b * x) / f.c * Math.sin(f.c * y + f.d);
//
//        else //%none is null
//            return f.amp / f.a * (g.a / f.a * (Math.cos(f.a * x + f.b) + (f.a * x + f.b) * Math.sin(f.a * x + f.b)) - g.a / f.a * f.b * Math.sin(f.a * x + f.b) + g.b * Math.sin(f.a * x + f.b)) / f.c * Math.sin(f.c * y + f.d);
//    }
//
//
//    private static double _ps_cc_cst(double x, double y, DCosCosFunctionXY f, DCstFunctionXY g) {
//        if (f.a == 0 && f.c == 0)
//            return f.amp * Math.cos(f.b) * Math.cos(f.d) * g.cst * x * y;
//        else if (f.a != 0 && f.c == 0)
//            return f.amp * Math.cos(f.d) * g.cst / f.a * Math.sin(f.a * x + f.b) * y;
//
//        else if (f.a == 0 && f.c != 0)
//            return f.amp * Math.cos(f.b) * g.cst * x / f.c * Math.sin(f.c * y + f.d);
//
//        else //%none is null
//            return f.amp * g.cst / f.a * Math.sin(f.a * x + f.b) / f.c * Math.sin(f.c * y + f.d);
//    }
//}
