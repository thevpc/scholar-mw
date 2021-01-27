package net.thevpc.scholar.hadrumaths.test.removeme;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package net.thevpc.scholar.math.format.impl;
//
//import net.thevpc.scholar.math.FormatFactory;
//import net.thevpc.scholar.math.format.FormatParam;
//import net.thevpc.scholar.math.format.Formatter;
//import net.thevpc.scholar.math.functions.Domain;
//import net.thevpc.scholar.math.functions.dfxy.DoubleX;
//
///**
// * @author vpc
// */
//public class DoubleXFormatter implements Formatter<DoubleX> {
//    public DoubleXFormatter() {
//    }
//
//    @Override
//    public String format(DoubleX o, FormatParam... format) {
//        double v = o.getValue();
//        if (o.getDomain().equals(Domain.FULLX)) {
//            return FormatFactory.format(v, format);
//        }
//        if (v == 0) {
//            return FormatFactory.format(0.0, format);
//        } else if (v == 1) {
//            return FormatFactory.format(o.getDomain(), format);
//        } else if (v == -1) {
//            return "-" + FormatFactory.format(o.getDomain(), format);
//        } else {
//            return o.getValue() + " * " + FormatFactory.format(o.getDomain(), format);
//        }
//    }
//
//}
