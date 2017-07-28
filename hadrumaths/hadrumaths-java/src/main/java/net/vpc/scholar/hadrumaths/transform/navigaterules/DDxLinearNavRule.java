///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.vpc.scholar.math.transform.navigaterules;
//
//import net.vpc.scholar.math.Expr;
//import net.vpc.scholar.math.functions.dfx.DDxLinear;
//import net.vpc.scholar.math.transform.ExpressionRewriter;
//import net.vpc.scholar.math.transform.ExpressionRewriterRule;
//
///**
// *
// * @author vpc
// */
//public class DDxLinearNavRule implements ExpressionRewriterRule {
//
//    public static final ExpressionRewriterRule INSTANCE = new DDxLinearNavRule();
//    public static final Class<? extends Expr>[] TYPES = new Class[]{DDxLinear.class};
//
//
//    @Override
//    public Class<? extends Expr>[] getTypes() {
//        return TYPES;
//    }
//
//    public Expr rewriteOrSame(Expr e, ExpressionRewriter ruleset) {
//        return null;
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
