///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.thevpc.scholar.math.eval.canonicalrules;
//
//import net.thevpc.scholar.math.Expr;
//import net.thevpc.scholar.math.Maths;
//import net.thevpc.scholar.math.functions.Cos;
//import net.thevpc.scholar.math.functions.Domain;
//import net.thevpc.scholar.math.functions.FunctionFactory;
//import net.thevpc.scholar.math.functions.dfx.DDxCos;
//import net.thevpc.scholar.math.functions.Linear;
//import net.thevpc.scholar.math.eval.ExpressionRewriter;
//import net.thevpc.scholar.math.eval.ExpressionRewriterRule;
//
///**
// *
// * @author vpc
// */
//public class DDxCosSymbolRule extends AbstractExpressionRewriterRule {
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
