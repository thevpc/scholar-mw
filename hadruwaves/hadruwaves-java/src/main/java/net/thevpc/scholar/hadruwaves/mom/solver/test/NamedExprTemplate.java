/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.mom.solver.test;

import net.thevpc.common.props.WritableValue;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadruwaves.project.Props2;
import net.thevpc.scholar.hadruwaves.props.WritablePExpression;

/**
 *
 * @author vpc
 */
public class NamedExprTemplate {

    private WritableString name = Props2.of("name").valueOf(String.class, null);
    private WritablePExpression<Expr> expr = Props2.of("expr").exprOf(Maths.expr(0));

    public NamedExprTemplate() {
    }

    public NamedExprTemplate(Expr expr, String name) {
        this.expr.set(expr.toString());
        this.name.set(name);
    }

    public WritableString name() {
        return name;
    }

    public WritablePExpression<Expr> expr() {
        return expr;
    }
}
