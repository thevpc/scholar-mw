/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.transform;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.HSerializable;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;

/**
 * @author vpc
 */
public interface ExpressionRewriter extends HSerializable {
    RewriteResult rewrite(Expr e, ExprType targetExprType);

    Expr rewriteOrSame(Expr e, ExprType targetExprType);

    Expr rewriteOrNull(Expr e, ExprType targetExprType);

    ExprRewriteSuccessListener[] getRewriteSuccessListeners();

    ExpressionRewriter addRewriteSuccessListener(ExprRewriteSuccessListener listener);

    ExpressionRewriter removeRewriteSuccessListener(ExprRewriteSuccessListener listener);

    ExprRewriteFailListener[] getRewriteFailListeners();

    ExpressionRewriter addRewriteFailListener(ExprRewriteFailListener listener);

    ExpressionRewriter removeRewriteFailListener(ExprRewriteFailListener listener);

    ExpressionRewriter addRewriteListener(ExprRewriteListener listener);

    ExpressionRewriter removeRewriteListener(ExprRewriteListener listener);
}
