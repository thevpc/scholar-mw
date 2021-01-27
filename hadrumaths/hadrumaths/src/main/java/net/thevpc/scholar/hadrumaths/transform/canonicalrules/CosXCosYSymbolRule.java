/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.transform.canonicalrules;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.CosXCosY;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.Linear;
import net.thevpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.RewriteResult;

import static net.thevpc.scholar.hadrumaths.Maths.*;

/**
 * @author vpc
 */
public class CosXCosYSymbolRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new CosXCosYSymbolRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{CosXCosY.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        if (!(e instanceof CosXCosY)) {
            return null;
        }

        CosXCosY ee = (CosXCosY) e;
        Domain domainxy = ee.getDomain();
        return RewriteResult.bestEffort(
                ruleset.rewriteOrSame(
                        prod(
                                cos(new Linear(ee.getA(), 0, ee.getB(), domainxy)),
                                cos(new Linear(0, ee.getC(), ee.getD(), domainxy)),
                                expr(ee.getAmp(), domainxy)
                        ), targetExprType));
    }


}
