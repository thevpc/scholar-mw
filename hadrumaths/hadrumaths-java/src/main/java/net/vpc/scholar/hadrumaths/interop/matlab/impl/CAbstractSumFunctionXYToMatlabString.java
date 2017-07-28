///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package net.vpc.scholar.math.interop.matlab.impl;
//
//import net.vpc.scholar.math.IDCxy;
//import net.vpc.scholar.math.interop.matlab.*;
//
///**
// *
// * @author vpc
// */
//public class CAbstractSumFunctionXYToMatlabString implements ToMatlabString<DCxyAbstractSum>{
//
//    @Override
//    public String toMatlabString(DCxyAbstractSum o, ToMatlabStringParam... format) {
//        StringBuilder sb = new StringBuilder();
//        IDCxy[] segments=o.getSegments();
//        for (int i = 0; i < segments.length; i++) {
//            if (i > 0) {
//                sb.append(" + ");
//            }
//            sb.append("(");
//            sb.append(MatlabFactory.toMatlabString(segments[i],format));
//            sb.append(")");
//        }
//        return sb.toString();
//    }
//
//}
