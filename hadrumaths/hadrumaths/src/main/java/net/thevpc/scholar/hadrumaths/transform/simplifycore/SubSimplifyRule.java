/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.transform.simplifycore;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Minus;
import net.thevpc.scholar.hadrumaths.transform.*;

/**
 * @author vpc
 */
public class SubSimplifyRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new SubSimplifyRule();
    public static final Class<? extends Minus>[] TYPES = new Class[]{Minus.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        if (!(e instanceof Minus)) {
            return null;
        }
        Minus ee = (Minus) e;
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
