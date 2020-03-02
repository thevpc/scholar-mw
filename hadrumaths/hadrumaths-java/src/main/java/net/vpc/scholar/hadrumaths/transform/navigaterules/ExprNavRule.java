/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.navigaterules;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;
import net.vpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

import java.util.List;

/**
 * @author vpc
 */
public class ExprNavRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new ExprNavRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Expr.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        List<Expr> oldChildren = e.getChildren();
        if (oldChildren.isEmpty()) {
            return RewriteResult.unmodified();
        }
        Expr[] newChildren = new Expr[oldChildren.size()];
        boolean someUpdates = false;
        for (int i = 0; i < newChildren.length; i++) {
            Expr c = oldChildren.get(i);
            RewriteResult r = ruleset.rewrite(c, targetExprType);
            if (r.isRewritten()) {
                someUpdates = true;
                newChildren[i] = r.getValue();
            } else {
                newChildren[i] = c;
            }
        }
        if (!someUpdates) {
            return RewriteResult.unmodified();
        }
        Expr value = e.newInstance(newChildren);
        if(value.equals(e)){
            return RewriteResult.unmodified();
        }
        return RewriteResult.bestEffort(value);
    }


}
