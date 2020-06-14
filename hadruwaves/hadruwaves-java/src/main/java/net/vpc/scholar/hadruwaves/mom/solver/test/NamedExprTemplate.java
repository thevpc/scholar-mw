/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.solver.test;

import net.vpc.common.props.WritablePValue;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadruwaves.project.Props2;
import net.vpc.scholar.hadruwaves.props.WritablePExpression;

/**
 *
 * @author vpc
 */
public class NamedExprTemplate {

    private WritablePValue<String> name = Props2.of("name").valueOf(String.class, null);
    private WritablePExpression<Expr> expr = Props2.of("expr").exprOf(Maths.expr(0));

    public NamedExprTemplate() {
    }

    public NamedExprTemplate(Expr expr, String name) {
        this.expr.set(expr.toString());
        this.name.set(name);
    }

    public WritablePValue<String> name() {
        return name;
    }

    public WritablePExpression<Expr> expr() {
        return expr;
    }
}
