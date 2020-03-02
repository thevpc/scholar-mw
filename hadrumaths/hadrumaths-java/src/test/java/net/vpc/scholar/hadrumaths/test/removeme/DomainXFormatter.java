///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.vpc.scholar.math.format.impl;
//
//import net.vpc.scholar.math.FormatFactory;
//import net.vpc.scholar.math.format.FormatParam;
//import net.vpc.scholar.math.format.FormatParamArray;
//import net.vpc.scholar.math.format.Formatter;
//import net.vpc.scholar.math.format.params.DomainFormat;
//import net.vpc.scholar.math.format.params.DoubleFormat;
//import net.vpc.scholar.math.functions.DomainX;
//
///**
// *
// * @author vpc
// */
//public class DomainXFormatter implements Formatter<DomainX> {
//
//    public DomainXFormatter() {
//    }
//
//    @Override
//    public String format(DomainX o, FormatParam... format) {
//        FormatParamArray formatArray = new FormatParamArray(format);
//        DomainFormat d = (DomainFormat) formatArray.getParam(FormatFactory.GATE_DOMAIN);
//        DoubleFormat df = (DoubleFormat) formatArray.getParam(DoubleFormat.class, false);
//        switch (d.getType()) {
//            case GATE: {
//                return df == null
//                        ? ("gate(x," + o.xmin + "," + o.xmax + ")")
//                        : ("gate(x," + df.getFormat().format(o.xmin) + "," + df.getFormat().format(o.xmax) + ")");
//            }
//            case none: {
//                return "";
//            }
//        }
//        return null;
//    }
//
//}
