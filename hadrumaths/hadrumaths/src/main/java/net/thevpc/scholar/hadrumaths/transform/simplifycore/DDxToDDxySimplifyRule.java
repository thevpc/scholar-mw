///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.thevpc.scholar.math.eval.simplifycore;
//
//import net.thevpc.scholar.math.Expr;
//import net.thevpc.scholar.math.functions.dfxy.DDxToDDxy;
//import net.thevpc.scholar.math.eval.ExpressionRewriter;
//import net.thevpc.scholar.math.eval.ExpressionRewriterRule;
//
///**
// *
// * @author vpc
// */
//@Deprecated
//public class DDxToDDxySimplifyRule extends AbstractExpressionRewriterRule {
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
