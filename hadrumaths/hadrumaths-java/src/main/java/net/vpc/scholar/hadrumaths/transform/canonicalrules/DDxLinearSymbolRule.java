///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.vpc.scholar.math.eval.canonicalrules;
//
//import net.vpc.scholar.math.Expr;
//import net.vpc.scholar.math.functions.Domain;
//import net.vpc.scholar.math.functions.FunctionFactory;
//import net.vpc.scholar.math.functions.dfx.DDxLinear;
//import net.vpc.scholar.math.eval.ExpressionRewriter;
//import net.vpc.scholar.math.eval.ExpressionRewriterRule;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static net.vpc.scholar.math.Maths.*;
//
///**
// *
// * @author vpc
// */
//public class DDxLinearSymbolRule implements ExpressionRewriterRule {
//
//    public static final ExpressionRewriterRule INSTANCE = new DDxLinearSymbolRule();
//    public static final Class<? extends Expr>[] TYPES = new Class[]{DDxLinear.class};
//
//
//    @Override
//    public Class<? extends Expr>[] getTypes() {
//        return TYPES;
//    }
//
//    public Expr rewriteOrSame(Expr e, ExpressionRewriter ruleset) {
//        if (!(e instanceof DDxLinear)) {
//            return null;
//        }
//
//        DDxLinear ee = (DDxLinear) e;
//        List<Expr> p=new ArrayList<Expr>();
//        if(ee.getA()!=0){
//            p.add(mul(FunctionFactory.cst(ee.getA(),Domain.toDomainXY(ee.getDomain())), X));
//        }
//        if(ee.getB()!=0){
//            p.add(FunctionFactory.cst(ee.getB(), Domain.toDomainXY(ee.getDomain())));
//        }
//        if(p.isEmpty()){
//            return FunctionFactory.CZEROXY;
//        }
//        return sum(p.toArray(new Expr[p.size()]));
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
