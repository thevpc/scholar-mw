///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package net.thevpc.scholar.math.interop.matlab.impl;
//
//import net.thevpc.scholar.math.interop.matlab.*;
//import net.thevpc.scholar.math.functions.DomainX;
//import net.thevpc.scholar.math.interop.matlab.params.MatlabDomainFormat;
//import net.thevpc.scholar.math.interop.matlab.params.MatlabDoubleFormat;
//
///**
// *
// * @author vpc
// */
//public class DomainXToMatlabString implements ToMatlabString<DomainX>{
//    public DomainXToMatlabString() {
//    }
//
//    @Override
//    public String toMatlabString(DomainX o, ToMatlabStringParam... format) {
//        ToMatlabStringParamArray formatArray = new ToMatlabStringParamArray(format);
//        MatlabDomainFormat d = (MatlabDomainFormat) formatArray.getParam(MatlabFactory.GATE_DOMAIN);
//        MatlabDoubleFormat df = (MatlabDoubleFormat) formatArray.getParam(MatlabDoubleFormat.class, false);
//        switch (d.getType()) {
//            case GATE:
//                {
//                    return df==null ?
//                            ("gate(x,"+ o.xmin +","+ o.xmax +")")
//                           :("gate(x,"+df.getFormat().format(o.xmin)+","+df.getFormat().format(o.xmax)+")")
//                            ;
//                }
//            case none:
//                {
//                    return "";
//                }
//        }
//        return null;
//    }
//
//}
