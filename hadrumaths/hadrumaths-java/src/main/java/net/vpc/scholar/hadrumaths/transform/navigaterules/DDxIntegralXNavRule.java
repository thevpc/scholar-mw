///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.vpc.scholar.math.transform.navigaterules;
//
//import net.vpc.scholar.math.*;
//import net.vpc.scholar.math.functions.dfx.DDxIntegralX;
//import net.vpc.scholar.math.transform.ExpressionRewriter;
//import net.vpc.scholar.math.transform.ExpressionRewriterRule;
//
///**
// *
// * @author vpc
// */
//public class DDxIntegralXNavRule implements ExpressionRewriterRule {
//
//    public static final ExpressionRewriterRule INSTANCE = new DDxIntegralXNavRule();
//    public static final Class<? extends Expr>[] TYPES = new Class[]{DDxIntegralX.class};
//
//    @Override
//    public Class<? extends Expr>[] getTypes() {
//        return TYPES;
//    }
//
//    public Expr rewriteOrSame(Expr e, ExpressionRewriter ruleset) {
//        DDxIntegralX ee = (DDxIntegralX) e;
//        IDDx base = ee.getBase();
//        Expr a = ruleset.rewriteOrSame(base);
//        if(!a.equals(base)){
//            Expr eee = new DDxIntegralX(a.toDD(),ee.getIntegral(),ee.getX0());
//            AbstractExprPropertyAware.copyProperties(e, eee);
//            return eee;
//        }
//        return e;
//    }
//    @Override
//    public int hashCode() {
//        return getClass().getName().hashCode();
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if(obj==null || !obj.getClass().equals(getClass())){
//            return false;
//        }
//        return true;
//    }
//
//}
