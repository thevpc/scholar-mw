/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.transform;

import net.thevpc.scholar.hadrumaths.Expr;

/**
 * @author vpc
 */
public interface ExpressionTransformer {

    Expr transform(Expr expression, ExpressionTransform transform);
}
