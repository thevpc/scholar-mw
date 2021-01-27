///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.thevpc.scholar.math.eval.canonicalrules;
//
//import net.thevpc.scholar.math.Expr;
//import net.thevpc.scholar.math.functions.Domain;
//import net.thevpc.scholar.math.functions.FunctionFactory;
//import net.thevpc.scholar.math.functions.dfx.DDxLinear;
//import net.thevpc.scholar.math.eval.ExpressionRewriter;
//import net.thevpc.scholar.math.eval.ExpressionRewriterRule;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static net.thevpc.scholar.math.Maths.*;
//
///**
// *
// * @author vpc
// */
//public class DDxLinearSymbolRule extends AbstractExpressionRewriterRule {
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
