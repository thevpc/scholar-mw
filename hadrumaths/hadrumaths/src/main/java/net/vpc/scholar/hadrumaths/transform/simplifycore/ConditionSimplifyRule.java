/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.BooleanMarker;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.NoneOutBoolean;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.cond.AbstractComparatorExpr;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.cond.IfExpr;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.cond.NotExpr;
import net.vpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class ConditionSimplifyRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new ConditionSimplifyRule();
    public static final Class<? extends Expr>[] TYPES = new Class[]{AbstractComparatorExpr.class, NotExpr.class, IfExpr.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        if (e instanceof AbstractComparatorExpr) {
            AbstractComparatorExpr c = (AbstractComparatorExpr) e;
            Expr xxa = c.getChild(0);
            Expr yya = c.getChild(1);
            RewriteResult rxa = ruleset.rewrite(xxa, null);
            RewriteResult rya = ruleset.rewrite(yya, null);
            Expr xa = rxa.isUnmodified() ? xxa : rxa.getValue();
            Expr ya = rya.isUnmodified() ? yya : rya.getValue();
            if (xa.isNarrow(ExprType.DOUBLE_EXPR)
                    && ya.isNarrow(ExprType.DOUBLE_EXPR)) {
                double a = xa.toDouble();
                double b = ya.toDouble();
                double rr = c.evalDouble(a, b, BooleanMarker.ref());
                return zeroOneBestResult(targetExprType, rr);
            } else if (rxa.isUnmodified() && rya.isUnmodified()) {
                return RewriteResult.unmodified();
            } else {
                Expr c2 = c.newInstance(xa, ya);
//                if (c2.equals(e)) {
//                    return RewriteResult.unmodified();
//                }
                return RewriteResult.bestEffort(c2);
            }
        }
        if (e instanceof NotExpr) {
            NotExpr c = (NotExpr) e;
            RewriteResult rxa = ruleset.rewrite(c.getChild(0), null);
            Expr xa = rxa.isUnmodified() ? c.getChild(0) : rxa.getValue();
            if ((xa.isNarrow(ExprType.DOUBLE_EXPR))) {
                double a = xa.toDouble();
                double rr = c.evalDouble(a, NoneOutBoolean.INSTANCE);
                return zeroOneBestResult(targetExprType, rr);
            } else if (rxa.isUnmodified()) {
                return RewriteResult.unmodified();
            } else {
                Expr c2 = c.newInstance(xa);
//                return c2.equals(e) ? RewriteResult.unmodified() : RewriteResult.bestEffort(c2);
                return RewriteResult.bestEffort(c2);
            }

        }
        if (e instanceof IfExpr) {
            IfExpr c = (IfExpr) e;
            RewriteResult rxa = ruleset.rewrite(c.getChild(0), null);
            Expr xa = rxa.isUnmodified() ? c.getChild(0) : rxa.getValue();
            if ((xa.isNarrow(ExprType.DOUBLE_NBR))) {
                double a = xa.toDouble();
                if (a != 0) {
                    RewriteResult rya = ruleset.rewrite(c.getChild(1), targetExprType);
                    Expr ya = rya.isUnmodified() ? c.getChild(1) : rya.getValue();
                    return RewriteResult.bestEffort(ya);
                } else {
                    RewriteResult rza = ruleset.rewrite(c.getChild(2), targetExprType);
                    Expr za = rza.isUnmodified() ? c.getChild(2) : rza.getValue();
                    return RewriteResult.bestEffort(za);
                }
            } else {
                RewriteResult rya = ruleset.rewrite(c.getChild(1), targetExprType);
                RewriteResult rza = ruleset.rewrite(c.getChild(2), targetExprType);
                if (rxa.isUnmodified() && rya.isUnmodified() && rza.isUnmodified()) {
                    return RewriteResult.unmodified();
                } else {
                    Expr ya = rya.isUnmodified() ? c.getChild(1) : rya.getValue();
                    Expr za = rza.isUnmodified() ? c.getChild(2) : rza.getValue();
                    return RewriteResult.bestEffort(c.newInstance(xa, ya, za));
                }
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    protected RewriteResult zeroOneBestResult(ExprType targetExprType, double rr) {
        Expr rre = null;
        if (rr == 0) {
            if (targetExprType == null) {
                rre = Maths.ZERO;
            } else {
                switch (targetExprType) {
                    case DOUBLE_NBR:
                    case DOUBLE_EXPR:
                    case DOUBLE_DOUBLE: {
                        rre = Maths.ZERO;
                        break;
                    }
                    case COMPLEX_NBR:
                    case COMPLEX_EXPR:
                    case DOUBLE_COMPLEX: {
                        rre = Maths.CZERO;
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException("Unsupported");
                    }
                }
            }
            return RewriteResult.bestEffort(rre);
        } else {
            if (targetExprType == null) {
                rre = Maths.ONE;
            } else {
                switch (targetExprType) {
                    case DOUBLE_NBR:
                    case DOUBLE_EXPR:
                    case DOUBLE_DOUBLE: {
                        rre = Maths.ONE;
                        break;
                    }
                    case COMPLEX_NBR:
                    case COMPLEX_EXPR:
                    case DOUBLE_COMPLEX: {
                        rre = Maths.CONE;
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException("Unsupported");
                    }
                }
            }
            return RewriteResult.bestEffort(rre);
        }
    }


}
