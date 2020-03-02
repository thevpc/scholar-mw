/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;
import net.vpc.scholar.hadrumaths.symbolic.double2double.CosXCosY;
import net.vpc.scholar.hadrumaths.symbolic.double2double.CosXPlusY;
import net.vpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class CosXPlusYSimplifyRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new CosXPlusYSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{CosXPlusY.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
//        if (!(e instanceof CosXCosY)) {
//            return null;
//        }

        CosXPlusY ee = (CosXPlusY) e;
        if (ee.getAmp() == 0) {
            return RewriteResult.bestEffort(Maths.ZERO);
        }
        if (ee.getA() == 0) {
            if (ee.getB() == 0) {
                return RewriteResult.bestEffort(Maths.expr(ee.getAmp() * Maths.cos(ee.getC()), ee.getDomain()));
            }
            return RewriteResult.bestEffort(new CosXCosY(ee.getAmp(), 0, 0, ee.getB(), ee.getC(), ee.getDomain()));
        }
        if (ee.getB() == 0) {
            return RewriteResult.bestEffort(new CosXCosY(ee.getAmp(), ee.getA(), ee.getC(), 0, 0, ee.getDomain()));
        }
        return RewriteResult.unmodified();
    }


}
