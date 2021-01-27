/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.transform;

import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;

/**
 * @author vpc
 */
public interface ExpressionRewriterRule {

    Class<? extends Expr>[] getTypes();

    RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType);
}
