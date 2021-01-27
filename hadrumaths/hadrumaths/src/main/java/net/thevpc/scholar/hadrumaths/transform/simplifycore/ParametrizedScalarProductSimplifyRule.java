/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.transform.simplifycore;

import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.ParametrizedScalarProduct;
import net.thevpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class ParametrizedScalarProductSimplifyRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new ParametrizedScalarProductSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{ParametrizedScalarProduct.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        if (e instanceof ParametrizedScalarProduct) {
            ParametrizedScalarProduct c = (ParametrizedScalarProduct) e;
            RewriteResult rxa = ruleset.rewrite(c.getChild(0), targetExprType);
            RewriteResult rya = ruleset.rewrite(c.getChild(1), targetExprType);
            Expr rxqv = rxa.isUnmodified() ? c.getChild(0) : rxa.getValue();
            Expr ryav = rya.isUnmodified() ? c.getChild(1) : rya.getValue();
            if (!rxqv.hasParams() && !ryav.hasParams()) {
                return RewriteResult.bestEffort(Maths.scalarProduct(rxqv, ryav));
            }
            Expr xa = rxqv;
            Expr ya = ryav;
            if (rxa.isUnmodified() && rya.isUnmodified()) {
                return RewriteResult.unmodified();
            } else if (rxa.isBestEffort() && rya.isBestEffort()) {
                return RewriteResult.bestEffort(e);
            } else {
                Expr c2 = c.newInstance(xa, ya);
                return RewriteResult.newVal(c2);
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }


}
