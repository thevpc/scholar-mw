///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.vpc.scholar.math.eval.canonicalrules;
//
//import net.vpc.scholar.math.Expr;
//import net.vpc.scholar.math.functions.Domain;
//import net.vpc.scholar.math.functions.dfx.DDxUx;
//import net.vpc.scholar.math.functions.dfxy.UFunction;
//import net.vpc.scholar.math.eval.ExpressionRewriter;
//import net.vpc.scholar.math.eval.ExpressionRewriterRule;
//
///**
// *
// * @author vpc
// */
//public class DDxUxSymbolRule implements ExpressionRewriterRule {
//
//    public static final ExpressionRewriterRule INSTANCE = new DDxUxSymbolRule();
//    public static final Class<? extends Expr>[] TYPES = new Class[]{DDxUx.class};
//
//
//    @Override
//    public Class<? extends Expr>[] getTypes() {
//        return TYPES;
//    }
//
//    public Expr rewriteOrSame(Expr e, ExpressionRewriter ruleset) {
//        if (!(e instanceof DDxUx)) {
//            return null;
//        }
//
//        DDxUx ee = (DDxUx) e;
//        Domain domainxy = Domain.toDomainXY(ee.getDomain());
//        return new UFunction(domainxy,ee.getAmp(),ee.getA(),ee.getB(),ee.getC(),ee.getD(),ee.getE());
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
