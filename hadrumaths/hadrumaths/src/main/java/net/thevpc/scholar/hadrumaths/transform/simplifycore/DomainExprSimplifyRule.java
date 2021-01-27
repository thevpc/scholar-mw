/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.transform.simplifycore;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.DomainExpr;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.transform.AbstractExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.thevpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class DomainExprSimplifyRule extends AbstractExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new DomainExprSimplifyRule();
    public static final Class<? extends DomainExpr>[] TYPES = new Class[]{DomainExpr.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset, ExprType targetExprType) {
        DomainExpr ee = (DomainExpr) e;
        switch (ee.getDomain().getDimension()) {
            case 1: {
                boolean ok = true;

                Expr xmin = ruleset.rewriteOrSame(ee.getXmin(), null);
                if (isRealExpr(xmin)) {
                    xmin = xmin.toComplex();
                } else {
                    ok = false;
                }
                Expr xmax = ruleset.rewriteOrSame(ee.getXmax(), null);
                if (isRealExpr(xmax)) {
                    xmax = xmax.toComplex();
                } else {
                    ok = false;
                }

                if (ok) {
                    return RewriteResult.bestEffort(Maths.expr(1, Domain.ofBounds(
                            xmin.toDouble(),
                            xmax.toDouble()
                    )));
                } else {
                    return RewriteResult.bestEffort(DomainExpr.ofBounds(xmin, xmax));
                }
            }
            case 2: {
                boolean ok = true;

                Expr xmin = ruleset.rewriteOrSame(ee.getXmin(), null);
                if (isRealExpr(xmin)) {
                    xmin = xmin.toComplex();
                } else {
                    ok = false;
                }
                Expr xmax = ruleset.rewriteOrSame(ee.getXmax(), null);
                if (isRealExpr(xmax)) {
                    xmax = xmax.toComplex();
                } else {
                    ok = false;
                }

                Expr ymin = ruleset.rewriteOrSame(ee.getYmin(), null);
                if (isRealExpr(ymin)) {
                    ymin = ymin.toComplex();
                } else {
                    ok = false;
                }
                Expr ymax = ruleset.rewriteOrSame(ee.getYmax(), null);
                if (isRealExpr(ymax)) {
                    ymax = ymax.toComplex();
                } else {
                    ok = false;
                }

                if (ok) {
                    return RewriteResult.bestEffort(Maths.expr(1, Domain.ofBounds(
                            xmin.toDouble(),
                            xmax.toDouble(),
                            ymin.toDouble(),
                            ymax.toDouble()
                    )));
                } else {
                    return RewriteResult.bestEffort(DomainExpr.ofBounds(xmin, xmax, ymin, ymax));
                }
            }
            case 3: {
                boolean ok = true;

                Expr xmin = ruleset.rewriteOrSame(ee.getXmin(), null);
                if (isRealExpr(xmin)) {
                    xmin = xmin.toComplex();
                } else {
                    ok = false;
                }
                Expr xmax = ruleset.rewriteOrSame(ee.getXmax(), null);
                if (isRealExpr(xmax)) {
                    xmax = xmax.toComplex();
                } else {
                    ok = false;
                }

                Expr ymin = ruleset.rewriteOrSame(ee.getYmin(), null);
                if (isRealExpr(ymin)) {
                    ymin = ymin.toComplex();
                } else {
                    ok = false;
                }
                Expr ymax = ruleset.rewriteOrSame(ee.getYmax(), null);
                if (isRealExpr(ymax)) {
                    ymax = ymax.toComplex();
                } else {
                    ok = false;
                }

                Expr zmin = ruleset.rewriteOrSame(ee.getZmin(), null);
                if (isRealExpr(zmin)) {
                    zmin = zmin.toComplex();
                } else {
                    ok = false;
                }
                Expr zmax = ruleset.rewriteOrSame(ee.getZmax(), null);
                if (isRealExpr(zmax)) {
                    zmax = zmax.toComplex();
                } else {
                    ok = false;
                }
                if (ok) {
                    return RewriteResult.bestEffort(Maths.expr(1, Domain.ofBounds(
                            xmin.toDouble(),
                            xmax.toDouble(),
                            ymin.toDouble(),
                            ymax.toDouble(),
                            zmin.toDouble(),
                            zmax.toDouble()
                    )));
                } else {
                    return RewriteResult.bestEffort(DomainExpr.ofBounds(xmin, xmax, ymin, ymax, zmin, zmax));
                }
            }
        }
        return RewriteResult.unmodified();
    }

    private boolean isRealExpr(Expr d) {
        return d.isNarrow(ExprType.COMPLEX_EXPR) && d.toComplex().isReal();
    }

    private double toRealExpr(Expr d) {
        return d.toComplex().toReal();
    }


}
