///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.vpc.scholar.math.transform.canonicalrules;
//
//import net.vpc.scholar.math.Expr;
//import net.vpc.scholar.math.Maths;
//import net.vpc.scholar.math.functions.Cos;
//import net.vpc.scholar.math.functions.Domain;
//import net.vpc.scholar.math.functions.FunctionFactory;
//import net.vpc.scholar.math.functions.dfx.DDxCos;
//import net.vpc.scholar.math.functions.Linear;
//import net.vpc.scholar.math.transform.ExpressionRewriter;
//import net.vpc.scholar.math.transform.ExpressionRewriterRule;
//
///**
// *
// * @author vpc
// */
//public class DDxCosSymbolRule implements ExpressionRewriterRule {
//
//    public static final ExpressionRewriterRule INSTANCE = new DDxCosSymbolRule();
//    public static final Class<? extends Expr>[] TYPES = new Class[]{DDxCos.class};
//
//
//    @Override
//    public Class<? extends Expr>[] getTypes() {
//        return TYPES;
//    }
//
//    public Expr rewriteOrSame(Expr e, ExpressionRewriter ruleset) {
//        if (!(e instanceof DDxCos)) {
//            return null;
//        }
//
//        DDxCos ee = (DDxCos) e;
//        Domain domainxy = Domain.toDomainXY(ee.getDomain());
//        return Maths.mul(new Cos(new Linear(ee.getA(), 0, ee.getB(), domainxy)), FunctionFactory.cst(ee.getAmp(), domainxy));
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
