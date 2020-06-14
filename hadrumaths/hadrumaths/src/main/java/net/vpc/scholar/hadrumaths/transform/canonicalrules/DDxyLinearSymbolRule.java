/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.canonicalrules;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.FunctionFactory;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;
import net.vpc.scholar.hadrumaths.symbolic.double2double.Linear;
import net.vpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

import java.util.ArrayList;
import java.util.List;

import static net.vpc.scholar.hadrumaths.Maths.*;

/**
 * @author vpc
 */
public class DDxyLinearSymbolRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new DDxyLinearSymbolRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Linear.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        if (!(e instanceof Linear)) {
            return null;
        }

        Linear ee = (Linear) e;
        List<Expr> p = new ArrayList<Expr>();
        if (ee.getA() != 0) {
            p.add(mul(Maths.expr(ee.getA(), ee.getDomain()), X));
        }
        if (ee.getB() != 0) {
            p.add(mul(Maths.expr(ee.getB(), ee.getDomain()), Y));
        }
        if (ee.getC() != 0) {
            p.add(Maths.expr(ee.getC(), ee.getDomain()));
        }
        if (p.isEmpty()) {
            return RewriteResult.bestEffort(Maths.CZEROXY);
        }
        return RewriteResult.newVal(Maths.sum(p.toArray(new Expr[0])));
    }


}
