/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.navigaterules;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.symbolic.DefaultDoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class DefaultDoubleToVectorNavRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new DefaultDoubleToVectorNavRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{DefaultDoubleToVector.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        DefaultDoubleToVector r = (DefaultDoubleToVector) e;
        DoubleToComplex[] s = new DoubleToComplex[r.getComponentSize()];
        boolean newVal = false;
        boolean bestVal = false;
        for (int k = 0; k < s.length; k++) {
            DoubleToComplex i = r.getComponent(Axis.values()[k]);
            RewriteResult s2 = ruleset.rewrite(i);
            if (s2.isUnmodified()) {
                s[k] = i;
            } else if (s2.isBestEffort()) {
                s[k] = s2.getValue().toDC();
                bestVal = true;
            } else {
                s[k] = s2.getValue().toDC();
                newVal = true;
            }
        }
        if (newVal) {
            switch (r.getComponentSize()) {
                case 1: {
                    return RewriteResult.newVal(DefaultDoubleToVector.create(s[0]));
                }
                case 2: {
                    return RewriteResult.newVal(DefaultDoubleToVector.create(s[0], s[1]));
                }
                case 3: {
                    return RewriteResult.newVal(DefaultDoubleToVector.create(s[0], s[1], s[2]));
                }
            }
        } else if (bestVal) {
            switch (r.getComponentSize()) {
                case 1: {
                    return RewriteResult.bestEffort(DefaultDoubleToVector.create(s[0]));
                }
                case 2: {
                    return RewriteResult.bestEffort(DefaultDoubleToVector.create(s[0], s[1]));
                }
                case 3: {
                    return RewriteResult.bestEffort(DefaultDoubleToVector.create(s[0], s[1], s[2]));
                }
            }
        } else {
            return RewriteResult.unmodified(r);
        }
        throw new IllegalArgumentException("Unsupported");
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
