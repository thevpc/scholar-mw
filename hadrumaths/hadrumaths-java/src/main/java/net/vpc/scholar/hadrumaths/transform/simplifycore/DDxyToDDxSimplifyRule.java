///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.vpc.scholar.math.eval.simplifycore;
//
//import net.vpc.scholar.math.Axis;
//import net.vpc.scholar.math.Expr;
//import net.vpc.scholar.math.functions.dfx.DDxyToDDx;
//import net.vpc.scholar.math.eval.ExpressionRewriter;
//import net.vpc.scholar.math.eval.ExpressionRewriterRule;
//
///**
// *
// * @author vpc
// */
//@Deprecated
//public class DDxyToDDxSimplifyRule implements ExpressionRewriterRule {
//
//    public static final ExpressionRewriterRule INSTANCE = new DDxyToDDxSimplifyRule();
//    public static final Class<? extends Expr>[] TYPES = new Class[]{DDxyToDDx.class};
//
//    @Override
//    public Class<? extends Expr>[] getTypes() {
//        return TYPES;
//    }
//
//    public Expr rewriteOrSame(Expr e, ExpressionRewriter ruleset) {
//        if ((e instanceof DDxyToDDx)) {
//            DDxyToDDx f = (DDxyToDDx) e;
//            Expr t = ruleset.rewriteOrSame(f.getBase());
//            if(t.isInvariant(Axis.Y)){
//                return t;
//            }
//            if(!t.equals(f.getBase())){
//                return new DDxyToDDx(t.toDD(),f.getDefaultY());
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
