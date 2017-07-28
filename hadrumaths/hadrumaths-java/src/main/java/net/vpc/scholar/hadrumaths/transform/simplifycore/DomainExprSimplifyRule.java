/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform.simplifycore;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.DomainExpr;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;

/**
 * @author vpc
 */
public class DomainExprSimplifyRule implements ExpressionRewriterRule {

    public static final ExpressionRewriterRule INSTANCE = new DomainExprSimplifyRule();
    public static final Class<? extends DomainExpr>[] TYPES = new Class[]{DomainExpr.class};


    @Override
    public Class<? extends Expr>[] getTypes() {
        return TYPES;
    }

    public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
        DomainExpr ee = (DomainExpr) e;
        switch (ee.getDomainDimension()) {
            case 1: {
                boolean ok = true;

                Expr xmin = ruleset.rewriteOrSame(ee.getXmin());
                if (isRealExpr(xmin)) {
                    xmin = xmin.toComplex();
                } else {
                    ok = false;
                }
                Expr xmax = ruleset.rewriteOrSame(ee.getXmax());
                if (isRealExpr(xmax)) {
                    xmax = xmax.toComplex();
                } else {
                    ok = false;
                }

                if(ok){
                    return RewriteResult.bestEffort(DoubleValue.valueOf(1,Domain.forBounds(
                            xmin.toDouble(),
                            xmax.toDouble()
                    )));
                }else{
                    return RewriteResult.bestEffort(DomainExpr.forBounds(xmin,xmax));
                }
            }
            case 2: {
                boolean ok = true;

                Expr xmin = ruleset.rewriteOrSame(ee.getXmin());
                if (isRealExpr(xmin)) {
                    xmin = xmin.toComplex();
                } else {
                    ok = false;
                }
                Expr xmax = ruleset.rewriteOrSame(ee.getXmax());
                if (isRealExpr(xmax)) {
                    xmax = xmax.toComplex();
                } else {
                    ok = false;
                }

                Expr ymin = ruleset.rewriteOrSame(ee.getYmin());
                if (isRealExpr(ymin)) {
                    ymin = ymin.toComplex();
                } else {
                    ok = false;
                }
                Expr ymax = ruleset.rewriteOrSame(ee.getYmax());
                if (isRealExpr(ymax)) {
                    ymax = ymax.toComplex();
                } else {
                    ok = false;
                }

                if(ok){
                    return RewriteResult.bestEffort(DoubleValue.valueOf(1,Domain.forBounds(
                            xmin.toDouble(),
                            xmax.toDouble(),
                            ymin.toDouble(),
                            ymax.toDouble()
                    )));
                }else{
                    return RewriteResult.bestEffort(DomainExpr.forBounds(xmin,xmax,ymin,ymax));
                }
            }
            case 3: {
                boolean ok = true;

                Expr xmin = ruleset.rewriteOrSame(ee.getXmin());
                if (isRealExpr(xmin)) {
                    xmin = xmin.toComplex();
                } else {
                    ok = false;
                }
                Expr xmax = ruleset.rewriteOrSame(ee.getXmax());
                if (isRealExpr(xmax)) {
                    xmax = xmax.toComplex();
                } else {
                    ok = false;
                }

                Expr ymin = ruleset.rewriteOrSame(ee.getYmin());
                if (isRealExpr(ymin)) {
                    ymin = ymin.toComplex();
                } else {
                    ok = false;
                }
                Expr ymax = ruleset.rewriteOrSame(ee.getYmax());
                if (isRealExpr(ymax)) {
                    ymax = ymax.toComplex();
                } else {
                    ok = false;
                }

                Expr zmin = ruleset.rewriteOrSame(ee.getZmin());
                if (isRealExpr(zmin)) {
                    zmin = zmin.toComplex();
                } else {
                    ok = false;
                }
                Expr zmax = ruleset.rewriteOrSame(ee.getZmax());
                if (isRealExpr(zmax)) {
                    zmax = zmax.toComplex();
                } else {
                    ok = false;
                }
                if(ok){
                    return RewriteResult.bestEffort(DoubleValue.valueOf(1,Domain.forBounds(
                            xmin.toDouble(),
                            xmax.toDouble(),
                            ymin.toDouble(),
                            ymax.toDouble(),
                            zmin.toDouble(),
                            zmax.toDouble()
                    )));
                }else{
                    return RewriteResult.bestEffort(DomainExpr.forBounds(xmin,xmax,ymin,ymax,zmin,zmax));
                }
            }
        }
        return RewriteResult.unmodified(e);
    }

    private boolean isRealExpr(Expr d) {
        return d.isComplex() && d.toComplex().isReal();
    }

    private double toRealExpr(Expr d) {
        return d.toComplex().toReal();
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
