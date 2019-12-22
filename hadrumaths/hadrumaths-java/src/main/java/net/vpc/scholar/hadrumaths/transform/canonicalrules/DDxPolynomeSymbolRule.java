///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.vpc.scholar.math.eval.canonicalrules;
//
//import net.vpc.scholar.math.Expr;
//import net.vpc.scholar.math.MathsBase;
//import net.vpc.scholar.math.functions.Domain;
//import net.vpc.scholar.math.functions.FunctionFactory;
//import net.vpc.scholar.math.functions.dfx.DDxPolynome;
//import net.vpc.scholar.math.eval.ExpressionRewriter;
//import net.vpc.scholar.math.eval.ExpressionRewriterRule;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static net.vpc.scholar.math.MathsBase.*;
//
///**
// *
// * @author vpc
// */
//public class DDxPolynomeSymbolRule implements ExpressionRewriterRule {
//
//    public static final ExpressionRewriterRule INSTANCE = new DDxPolynomeSymbolRule();
//    public static final Class<? extends Expr>[] TYPES = new Class[]{DDxPolynome.class};
//
//
//    @Override
//    public Class<? extends Expr>[] getTypes() {
//        return TYPES;
//    }
//
//    public Expr rewriteOrSame(Expr e, ExpressionRewriter ruleset) {
//        if (!(e instanceof DDxPolynome)) {
//            return null;
//        }
//
//        DDxPolynome ee = (DDxPolynome) e;
//        List<Expr> p=new ArrayList<Expr>();
//        Domain domain = Domain.toDomainXY(ee.getDomain());
//        double[] a = ee.getA();
//        for (int i = 0; i < a.length; i++) {
//            double v=a[i];
//            if(i==0){
//                p.add(FunctionFactory.cst(v, domain));
//            }else if(i==1){
//                p.add(mul(FunctionFactory.cst(v, domain), MathsBase.X));
//            }else{
//                p.add(mul(FunctionFactory.cst(v, domain), MathsBase.pow(MathsBase.X, FunctionFactory.cst(v, domain))));
//            }
//        }
//        if(p.isEmpty()){
//            return FunctionFactory.CZEROXY;
//        }
//        return sum(p.toArray(new Expr[0]));
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
