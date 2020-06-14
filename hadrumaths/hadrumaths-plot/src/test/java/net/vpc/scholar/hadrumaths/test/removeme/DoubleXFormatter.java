package net.vpc.scholar.hadrumaths.test.removeme;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package net.vpc.scholar.math.format.impl;
//
//import net.vpc.scholar.math.FormatFactory;
//import net.vpc.scholar.math.format.FormatParam;
//import net.vpc.scholar.math.format.Formatter;
//import net.vpc.scholar.math.functions.Domain;
//import net.vpc.scholar.math.functions.dfxy.DoubleX;
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
