/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Expressions;
import net.vpc.scholar.hadrumaths.NoneOutBoolean;
import net.vpc.scholar.hadrumaths.symbolic.Any;
import net.vpc.scholar.hadrumaths.symbolic.ComplexValue;
import net.vpc.scholar.hadrumaths.symbolic.GenericFunctionX;
import net.vpc.scholar.hadrumaths.symbolic.IConstantValue;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class GenericFunctionXSimplifyRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new GenericFunctionXSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{GenericFunctionX.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        GenericFunctionX ee = (GenericFunctionX) e;
        Expr arg = ee.getArgument();
        RewriteResult arg2 = ruleset.rewrite(arg);
        if (arg2.isUnmodified()) {
            IConstantValue argc = Expressions.toComplexValue(arg2.getValue());
            if (argc != null) {
                //cos(0) is not zero!
//            return new ComplexValue(ee.computeComplexArg(argc.getValue()), argc.getDomain().intersect(ee.getDomain()));
                return RewriteResult.bestEffort(new ComplexValue(ee.computeComplexArg(argc.getComplexConstant(), NoneOutBoolean.INSTANCE), ee.getDomain()));
            }
            return RewriteResult.unmodified(e);
        }
        Expr a = ee.newInstance(arg2.getValue());
        a = Any.copyProperties(e, a);
        return arg2.newVal(a);
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        return true;
    }

}
