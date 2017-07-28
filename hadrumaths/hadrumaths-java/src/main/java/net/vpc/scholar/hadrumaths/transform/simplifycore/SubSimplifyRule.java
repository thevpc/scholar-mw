/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.Sub;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 *
 * @author vpc
 */
public class SubSimplifyRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new SubSimplifyRule();
    public static final Class<? extends Sub>[] TYPES = new Class[]{Sub.class};

    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        if (!(e instanceof Sub)) {
            return null;
        }
        Sub ee = (Sub) e;
        Expr a = ruleset.rewriteOrSame(ee.getFirst());
        Expr b = ruleset.rewriteOrSame(ee.getSecond());
        return ruleset.rewrite(Maths.sum(a, Maths.mul(b, Complex.MINUS_ONE)));
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
