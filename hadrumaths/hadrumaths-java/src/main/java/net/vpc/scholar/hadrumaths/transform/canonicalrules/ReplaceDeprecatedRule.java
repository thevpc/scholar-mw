/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.canonicalrules;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.DDxyAbstractSum;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class ReplaceDeprecatedRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new ReplaceDeprecatedRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{DDxyAbstractSum.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        if ((e instanceof DDxyAbstractSum)) {
            DDxyAbstractSum f = (DDxyAbstractSum) e;
            return RewriteResult.newVal(Maths.sum(f.getSegments()));
        }
        return RewriteResult.unmodified(e);
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
