/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.transform.canonicalrules;

import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.CosXPlusY;
import net.thevpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class CosXPlusYSymbolRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new CosXPlusYSymbolRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{CosXPlusY.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        if (!(e instanceof CosXPlusY)) {
            return null;
        }

        CosXPlusY ee = (CosXPlusY) e;
        return RewriteResult.unmodified();
    }


}
