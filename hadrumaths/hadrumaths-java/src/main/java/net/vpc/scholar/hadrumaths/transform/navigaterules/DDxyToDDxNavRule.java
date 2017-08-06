///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.vpc.scholar.math.eval.navigaterules;
//
//import net.vpc.scholar.math.AbstractExprPropertyAware;
//import net.vpc.scholar.math.Expr;
//import net.vpc.scholar.math.IDoubleToDouble;
//import net.vpc.scholar.math.functions.dfx.DDxyToDDx;
//import net.vpc.scholar.math.eval.ExpressionRewriter;
//import net.vpc.scholar.math.eval.ExpressionRewriterRule;
//
///**
// *
// * @author vpc
// */
//public class DDxyToDDxNavRule implements ExpressionRewriterRule {
//
//    public static final ExpressionRewriterRule INSTANCE = new DDxyToDDxNavRule();
//    public static final Class<? extends Expr>[] TYPES = new Class[]{DDxyToDDx.class};
//
//    @Override
//    public Class<? extends Expr>[] getTypes() {
//        return TYPES;
//    }
//
//    public Expr rewriteOrSame(Expr e, ExpressionRewriter ruleset) {
//        DDxyToDDx ee = (DDxyToDDx) e;
//        IDoubleToDouble base = ee.getBase();
//        Expr a = ruleset.rewriteOrSame(base);
//        if(!a.equals(base)){
//            Expr eee = new DDxyToDDx(a.toDD(),ee.getDefaultY());
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
