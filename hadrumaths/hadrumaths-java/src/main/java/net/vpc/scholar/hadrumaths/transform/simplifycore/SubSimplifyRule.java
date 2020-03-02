/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Sub;
import net.vpc.scholar.hadrumaths.transform.*;

/**
 * @author vpc
 */
public class SubSimplifyRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new SubSimplifyRule();
    public static final Class<? extends Sub>[] TYPES = new Class[]{Sub.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        if (!(e instanceof Sub)) {
            return null;
        }
        Sub ee = (Sub) e;
        ExprType nt = ee.getNarrowType();
        Expr a = ruleset.rewriteOrSame(ee.getChild(0), nt);
        Expr b = ruleset.rewriteOrSame(ee.getChild(1), nt);
        Expr sum = Maths.sum(a, Maths.mul(b, Complex.MINUS_ONE));
        RewriteResult rewrite = ruleset.rewrite(sum, targetExprType);
        if (rewrite.getType() == RewriteResultType.UNMODIFIED) {
            return RewriteResult.bestEffort(sum);
        }
        return rewrite;
    }


}
