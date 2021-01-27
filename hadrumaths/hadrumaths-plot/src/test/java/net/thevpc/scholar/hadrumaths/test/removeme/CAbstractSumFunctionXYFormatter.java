package net.thevpc.scholar.hadrumaths.test.removeme;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.thevpc.scholar.math.format.impl;
//
//import net.thevpc.scholar.math.IDCxy;
//import net.thevpc.scholar.math.FormatFactory;
//import net.thevpc.scholar.math.format.FormatParam;
//import net.thevpc.scholar.math.format.Formatter;
////import net.thevpc.scholar.math.functions.cfxy.DCxyAbstractSum;
//
///**
// *
// * @author vpc
// */
//public class CAbstractSumFunctionXYFormatter implements Formatter<DCxyAbstractSum> {
//
//    @Override
//    public String format(DCxyAbstractSum o, FormatParam... format) {
//        StringBuilder sb = new StringBuilder();
//        IDCxy[] segments = o.getSegments();
//        for (int i = 0; i < segments.length; i++) {
//            if (i > 0) {
//                sb.append(" + ");
//            }
//            sb.append("(");
//            sb.append(FormatFactory.format(segments[i], format));
//            sb.append(")");
//        }
//        return sb.toString();
//    }
//
//}
