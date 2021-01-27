///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package net.thevpc.scholar.math.interop.matlab.impl;
//
//import net.thevpc.scholar.math.functions.dfx.DDxSymmetric;
//import net.thevpc.scholar.math.interop.matlab.MatlabFactory;
//import net.thevpc.scholar.math.interop.matlab.ToMatlabString;
//import net.thevpc.scholar.math.interop.matlab.ToMatlabStringParam;
//
///**
// *
// * @author vpc
// */
//public class DFunctionXYFromXToMatlabString implements ToMatlabString<DDxSymmetric>{
//
//    @Override
//    public String toMatlabString(DDxSymmetric o, ToMatlabStringParam... format) {
//        return "symmetric(" + MatlabFactory.toMatlabString(o.getBase(),format) + ")";
//    }
//
//
//}
