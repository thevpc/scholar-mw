/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.scalarproducts.formal.rewriter;

import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.Linear;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.XX;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.YY;
import net.thevpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class ToDDxyLinearRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new ToDDxyLinearRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{XX.class, YY.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        if (e instanceof XX) {
            return RewriteResult.bestEffort(new Linear(1, 0, 0, e.getDomain()));
        } else if (e instanceof YY) {
            return RewriteResult.bestEffort(new Linear(0, 1, 0, e.getDomain()));
        }
        return RewriteResult.unmodified();
    }


}
