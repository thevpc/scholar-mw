/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.scalarproducts.formal.rewriter;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Mul;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Plus;
import net.vpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vpc
 */
public class MulAddLinerizeRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new MulAddLinerizeRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{Mul.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        Mul ee = (Mul) e;
        Expr r = null;
        ExprType nt = ee.getNarrowType();
        for (Expr expression : ee.getChildren()) {
            expression = ruleset.rewriteOrSame(expression, nt);
            r = (r == null) ? expression : mul(r, expression, ruleset);
        }
        if (r == null) {
            throw new IllegalArgumentException("Unexpected");
        }
        if (r.equals(e)) {
            return RewriteResult.unmodified();
        }
        return RewriteResult.newVal(r);
    }

    private Expr mul(Expr a, Expr b, ExpressionRewriter ruleset) {
        if (a instanceof Plus && b instanceof Plus) {
            Plus ap = ((Plus) a);
            Plus bp = ((Plus) b);
            List<Expr> n = new ArrayList<Expr>();
            List<Expr> apSimpl = new ArrayList<Expr>();
            for (Expr ee : ap.getChildren()) {
                apSimpl.add(ruleset.rewriteOrSame(ee, null));
            }
            List<Expr> bpSimpl = new ArrayList<Expr>();
            for (Expr ee : bp.getChildren()) {
                bpSimpl.add(ruleset.rewriteOrSame(ee, null));
            }

            for (Expr ta : apSimpl) {
                for (Expr tb : bpSimpl) {
                    n.add(Mul.of(ta, tb));
                }
            }
            return Plus.of(n.toArray(new Expr[0]));
        } else if (a instanceof Plus) {
            Plus ap = ((Plus) a);
            List<Expr> n = new ArrayList<Expr>();
            for (Expr ta : ap.getChildren()) {
                ta = ruleset.rewriteOrSame(ta, null);
                n.add(Mul.of(ta, b));
            }
            return Plus.of(n.toArray(new Expr[0]));
        } else if (b instanceof Plus) {
            Plus bp = ((Plus) b);
            List<Expr> n = new ArrayList<Expr>();
            for (Expr tb : bp.getChildren()) {
                tb = ruleset.rewriteOrSame(tb, null);
                n.add(Mul.of(a, tb));
            }
            return Plus.of(n.toArray(new Expr[0]));
        }
//        return Mul.of(a, b);
        return a.mul(b);
    }


}
