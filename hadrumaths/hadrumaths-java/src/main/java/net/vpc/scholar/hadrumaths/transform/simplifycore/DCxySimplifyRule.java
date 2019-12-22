/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.MathsBase;
import net.vpc.scholar.hadrumaths.symbolic.ComplexValue;
import net.vpc.scholar.hadrumaths.symbolic.conv.DD2DC;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class DCxySimplifyRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new DCxySimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{DD2DC.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        DD2DC ee = (DD2DC) e;
        DoubleToDouble real = ee.getRealDD();
        DoubleToDouble imag = ee.getImagDD();
        RewriteResult exreal = ruleset.rewrite(real);
        RewriteResult eximag = ruleset.rewrite(imag);
        if (eximag.getValue().isZero()) {
            return RewriteResult.bestEffort(exreal.getValue());
        }
        if (exreal.getValue().getDomain().equals(eximag.getValue().getDomain())) {
            if (exreal.getValue().isDoubleExpr() && eximag.getValue().isDoubleExpr()) {
                //FIX ME may be not DoubleValue
                DoubleValue a = (DoubleValue) exreal.getValue();
                DoubleValue b = (DoubleValue) eximag.getValue();
                return RewriteResult.bestEffort(new ComplexValue(Complex.valueOf(a.value, b.value), exreal.getValue().getDomain()));
            }
        }
        if (exreal.isUnmodified() && eximag.isUnmodified()) {
            return RewriteResult.unmodified(e);
        }
        return RewriteResult.bestEffort(MathsBase.complex(real, imag));
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
