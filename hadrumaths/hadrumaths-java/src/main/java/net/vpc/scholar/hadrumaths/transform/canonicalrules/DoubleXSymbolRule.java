///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.vpc.scholar.math.transform.canonicalrules;
//
//import net.vpc.scholar.math.Expr;
//import net.vpc.scholar.math.functions.Domain;
//import net.vpc.scholar.math.functions.dfxy.DoubleX;
//import net.vpc.scholar.math.functions.DoubleValue;
//import net.vpc.scholar.math.transform.ExpressionRewriter;
//import net.vpc.scholar.math.transform.ExpressionRewriterRule;
//
///**
// *
// * @author vpc
// */
//public class DoubleXSymbolRule implements ExpressionRewriterRule {
//
//    public static final ExpressionRewriterRule INSTANCE = new DoubleXSymbolRule();
//    public static final Class<? extends Expr>[] TYPES = new Class[]{DoubleX.class};
//
//
//    @Override
//    public Class<? extends Expr>[] getTypes() {
//        return TYPES;
//    }
//
//    public Expr rewriteOrSame(Expr e, ExpressionRewriter ruleset) {
//        if (!(e instanceof DoubleX)) {
//            return null;
//        }
//
//        DoubleX ee = (DoubleX) e;
//        Domain domainxy = Domain.toDomainXY(ee.getDomain());
//        return new DoubleValue(ee.getValue(),domainxy);
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
