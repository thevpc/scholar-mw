/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.canonicalrules;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.symbolic.CosXPlusY;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 *
 * @author vpc
 */
public class CosXPlusYSymbolRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new CosXPlusYSymbolRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{CosXPlusY.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        if (!(e instanceof CosXPlusY)) {
            return null;
        }

        CosXPlusY ee = (CosXPlusY) e;
        return RewriteResult.unmodified(ee);
    }
    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null || !obj.getClass().equals(getClass())){
            return false;
        }
        return true;
    }

}
