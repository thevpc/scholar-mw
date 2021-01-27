/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.transform.simplifycore;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.DefaultComplexValue;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.trigo.Exp;
import net.thevpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.RewriteResult;

import static net.thevpc.scholar.hadrumaths.Maths.*;

/**
 * @author vpc
 */
public class ExpSimplifyRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new ExpSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Exp.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
//        if (!(e instanceof CosXCosY)) {
//            return null;
//        }

        Exp ee = (Exp) e;
        Expr aa = ee.getChild(0);
        RewriteResult rewriteResult = ruleset.rewrite(aa, targetExprType);
        Expr simplifiedArg = rewriteResult.isUnmodified() ? aa : rewriteResult.getValue();
        if (simplifiedArg.isNarrow(ExprType.DOUBLE_EXPR)) {
            return RewriteResult.newVal(
                    Maths.expr(exp(simplifiedArg.toDouble()), simplifiedArg.getDomain())
            );
        } else if (simplifiedArg.isNarrow(ExprType.COMPLEX_EXPR)) {
            Complex c = simplifiedArg.toComplex();
            if (c.isReal()) {
                return RewriteResult.newVal(
                        Maths.expr(Math.exp(c.realdbl()))
                );
            } else if (c.isImag()) {
                return RewriteResult.newVal(
                        Complex.of(Math.cos(c.imagdbl()), Math.sin(c.imagdbl()))
                );
            } else {
                return RewriteResult.newVal(
                        Complex.of(Math.cos(c.imagdbl()), Math.sin(c.imagdbl())).mul(Math.exp(c.realdbl()))
                );
            }
        } else if (simplifiedArg.isNarrow(ExprType.COMPLEX_EXPR)) {
            DefaultComplexValue cv = (DefaultComplexValue) simplifiedArg;
            Complex c = cv.getValue();
            if (c.isReal()) {
                return RewriteResult.newVal(
                        Maths.expr(Math.exp(c.realdbl()), simplifiedArg.getDomain())
                );
            } else if (c.isImag()) {
                return RewriteResult.newVal(
                        new DefaultComplexValue(Complex.of(Math.cos(c.imagdbl()), Math.sin(c.imagdbl())), simplifiedArg.getDomain())
                );
            } else {
                return RewriteResult.newVal(
                        new DefaultComplexValue(Complex.of(Math.cos(c.imagdbl()), Math.sin(c.imagdbl())).mul(Math.exp(c.realdbl())), simplifiedArg.getDomain())
                );
            }
        } else {
            switch (simplifiedArg.getType()) {
                case DOUBLE_DOUBLE: {
                    break;
                }
                case DOUBLE_COMPLEX: {
                    DoubleToComplex doubleToComplex = simplifiedArg.toDC();
                    Expr a = ruleset.rewriteOrSame(doubleToComplex.getRealDD(), ExprType.DOUBLE_DOUBLE).toDD();
                    Expr b = ruleset.rewriteOrSame(doubleToComplex.getImagDD(), ExprType.DOUBLE_DOUBLE).toDD();
                    return RewriteResult.newVal(mul(exp(a), sum(cos(b), mul(I, sin(b)))));
                }
            }
            //nothing to do
        }
        if (rewriteResult.isUnmodified()) {
            return RewriteResult.unmodified();
        } else if (rewriteResult.isBestEffort()) {
            return RewriteResult.bestEffort(exp(rewriteResult.getValue()));
        } else {
            return RewriteResult.newVal(exp(rewriteResult.getValue()));
        }
    }

}
