///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package net.vpc.scholar.math.interop.matlab.impl;
//
//import net.vpc.scholar.math.functions.dfx.DDxIntegralX;
//import net.vpc.scholar.math.interop.matlab.MatlabFactory;
//import net.vpc.scholar.math.interop.matlab.ToMatlabString;
//import net.vpc.scholar.math.interop.matlab.ToMatlabStringParam;
//
///**
// *
// * @author vpc
// */
//public class DFunctionIntegralXToMatlabString implements ToMatlabString<DDxIntegralX>{
//
//    @Override
//    public String toMatlabString(DDxIntegralX o, ToMatlabStringParam... format) {
//        return "symmetric(" + MatlabFactory.toMatlabString(o.getBase(), format) + ")";
//    }
//
//
//}
