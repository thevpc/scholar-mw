/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.transform.simplifycore;

import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.CosXCosY;
import net.thevpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class CosXCosYSimplifyRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new CosXCosYSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{CosXCosY.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
//        if (!(e instanceof CosXCosY)) {
//            return null;
//        }

        CosXCosY ee = (CosXCosY) e;
        if (ee.getAmp() == 0) {
            return RewriteResult.bestEffort(Maths.expr(0, e.getDomain()));
        }
        if (ee.getA() == 0 && ee.getC() == 0) {
            return RewriteResult.bestEffort(Maths.expr(Maths.cos2(ee.getB()) * Maths.cos2(ee.getD()), e.getDomain()));
        }

        if (ee.getA() == 0 && ee.getB() != 0) {
            return RewriteResult.bestEffort(new CosXCosY(ee.getAmp() * Maths.cos2(ee.getB()), 0, 0, ee.getC(), ee.getD(), ee.getDomain()));
        }
        if (ee.getC() == 0 && ee.getD() != 0) {
            return RewriteResult.bestEffort(new CosXCosY(ee.getAmp() * Maths.cos2(ee.getD()), ee.getA(), ee.getB(), 0, 0, ee.getDomain()));
        }
        return RewriteResult.unmodified();
    }


}
