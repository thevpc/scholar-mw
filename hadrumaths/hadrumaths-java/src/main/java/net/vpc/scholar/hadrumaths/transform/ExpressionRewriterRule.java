/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform;

import net.vpc.scholar.hadrumaths.Expr;

/**
 * @author vpc
 */
public interface ExpressionRewriterRule {

    public Class<? extends Expr>[] getTypes();

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset);
}
