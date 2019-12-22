/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.canonicalrules;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.MathsBase;
import net.vpc.scholar.hadrumaths.symbolic.Cos;
import net.vpc.scholar.hadrumaths.symbolic.CosXCosY;
import net.vpc.scholar.hadrumaths.symbolic.Linear;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class CosXCosYSymbolRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new CosXCosYSymbolRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{CosXCosY.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        if (!(e instanceof CosXCosY)) {
            return null;
        }

        CosXCosY ee = (CosXCosY) e;
        Domain domainxy = ee.getDomain();
        return RewriteResult.newVal(MathsBase.mul(
                new Cos(new Linear(ee.getA(), 0, ee.getB(), domainxy)),
                new Cos(new Linear(0, ee.getC(), ee.getD(), domainxy)),
                MathsBase.expr(ee.getAmp(), domainxy)
        ));
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        return true;
    }

}
