/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;
import net.vpc.scholar.hadrumaths.symbolic.conv.DefaultDoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.double2complex.DefaultComplexValue;
import net.vpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class DCxySimplifyRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new DCxySimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{DefaultDoubleToComplex.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        DefaultDoubleToComplex ee = (DefaultDoubleToComplex) e;
        DoubleToDouble real = ee.getRealDD();
        DoubleToDouble imag = ee.getImagDD();
        RewriteResult exreal = ruleset.rewrite(real, targetExprType);
        RewriteResult eximag = ruleset.rewrite(imag, targetExprType);
        Expr exrv = exreal.isUnmodified() ? real : exreal.getValue();
        Expr exiv = eximag.isUnmodified() ? imag : eximag.getValue();
        if (exiv.isZero()) {
            return RewriteResult.bestEffort(exrv);
        }
        if (exrv.getDomain().equals(exiv.getDomain())) {
            if (exrv.isNarrow(ExprType.DOUBLE_EXPR) && exiv.isNarrow(ExprType.DOUBLE_EXPR)) {
                //FIX ME may be not DoubleValue
                DoubleValue a = (DoubleValue) exrv;
                DoubleValue b = (DoubleValue) exiv;
                return RewriteResult.bestEffort(new DefaultComplexValue(Complex.of(a.toDouble(), b.toDouble()), exrv.getDomain()));
            }
        }
        if (exreal.isUnmodified() && eximag.isUnmodified()) {
            return RewriteResult.unmodified();
        }
        DoubleToComplex a = Maths.complex(real, imag);
        if(a.equals(e)){
            return RewriteResult.unmodified();
        }
        return RewriteResult.bestEffort(a);
    }


}
