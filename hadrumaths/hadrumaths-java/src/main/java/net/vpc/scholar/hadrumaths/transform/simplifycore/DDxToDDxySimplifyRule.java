///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.vpc.scholar.math.transform.simplifycore;
//
//import net.vpc.scholar.math.Expr;
//import net.vpc.scholar.math.functions.dfxy.DDxToDDxy;
//import net.vpc.scholar.math.transform.ExpressionRewriter;
//import net.vpc.scholar.math.transform.ExpressionRewriterRule;
//
///**
// *
// * @author vpc
// */
//@Deprecated
//public class DDxToDDxySimplifyRule implements ExpressionRewriterRule {
//
//    public static final ExpressionRewriterRule INSTANCE = new DDxToDDxySimplifyRule();
//    public static final Class<? extends Expr>[] TYPES = new Class[]{DDxToDDxy.class};
//
//    @Override
//    public Class<? extends Expr>[] getTypes() {
//        return TYPES;
//    }
//
//    public Expr rewriteOrSame(Expr e, ExpressionRewriter ruleset) {
//        if ((e instanceof DDxToDDxy)) {
//            DDxToDDxy f = (DDxToDDxy) e;
//            Expr t = ruleset.rewriteOrSame(f.getBaseFunction());
//            if(!t.equals(f.getBaseFunction())){
//                return new DDxToDDxy(t.toDDx());
//            }
//        }
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