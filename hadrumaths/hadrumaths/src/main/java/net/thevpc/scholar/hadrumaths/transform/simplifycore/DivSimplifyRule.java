/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.transform.simplifycore;

import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Expressions;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.ExprDefaults;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Div;
import net.thevpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class DivSimplifyRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new DivSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Div.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        if (!(e instanceof Div)) {
            return null;
        }

        Div ee = (Div) e;
        Expr first = ee.getChild(0);
        Expr second = ee.getChild(1);
        RewriteResult ar = ruleset.rewrite(first, targetExprType);
        RewriteResult br = ruleset.rewrite(second, targetExprType);

        Expr arv = ar.isUnmodified() ? first : ar.getValue();
        Expr brv = br.isUnmodified() ? second : br.getValue();

        Expr ac0 = Expressions.toConstantExprOrNull(arv);

        Expr bc0 = Expressions.toConstantExprOrNull(brv);
        if (ac0 != null && bc0 != null) {
            Expr o = ac0.toNumber().div(bc0.toNumber()).mul(ac0.getDomain().intersect(bc0.getDomain()));
            return RewriteResult.bestEffort(ruleset.rewriteOrSame(o,null));
        }
        if (bc0 != null) {
            Expr mul = arv.mul(bc0.toNumber().inv().mul(bc0.getDomain()));
            RewriteResult mulRes = ruleset.rewrite(mul, targetExprType);
            return mulRes.isUnmodified()? RewriteResult.bestEffort(mul):mulRes;
        }
        if (ar.isUnmodified() && br.isUnmodified()) {
            return RewriteResult.unmodified();
        }
        Expr eee = arv.div(brv);
        eee = ExprDefaults.copyProperties(e, eee);
        return (ar.isBestEffort() && br.isBestEffort()) ? RewriteResult.bestEffort(eee) : RewriteResult.newVal(eee);
    }


}
