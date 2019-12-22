package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

import java.util.List;

public abstract class AdaptedExpr extends AbstractExpBase {
    protected Expr base;

    public AdaptedExpr(Expr base) {
        this.base = base;
    }

    @Override
    public boolean isInvariant(Axis axis) {
        return base.isInvariant(axis);
    }

    @Override
    public boolean isDouble() {
        return base.isDouble();
    }

    @Override
    public boolean isDoubleExpr() {
        return base.isDoubleExpr();
    }

    @Override
    public boolean isComplex() {
        return base.isComplex();
    }

    @Override
    public boolean isScalarExpr() {
        return base.isScalarExpr();
    }

    @Override
    public boolean isMatrix() {
        return base.isMatrix();
    }

    @Override
    public boolean isDC() {
        return base.isDC();
    }

    @Override
    public boolean isDD() {
        return base.isDD();
    }

    @Override
    public boolean isDV() {
        return base.isDV();
    }

    @Override
    public boolean isDM() {
        return base.isDM();
    }

    @Override
    public boolean isZero() {
        return base.isZero();
    }

    @Override
    public boolean isNaN() {
        return base.isNaN();
    }

    @Override
    public boolean hasParams() {
        return base.hasParams();
    }


    @Override
    public Expr setParam(String name, Expr value) {
        Expr r = this.base.setParam(name, value);
        if (r != base) {
            return newInstance(r);
        }
        return this;
    }

    @Override
    public boolean isInfinite() {
        return base.isInfinite();
    }

    @Override
    public List<Expr> getSubExpressions() {
        return base.getSubExpressions();
    }

    @Override
    public Expr compose(Axis axis, Expr xreplacement) {
        Expr r = this.base.compose(axis, xreplacement);
        if (r != base) {
            return newInstance(r);
        }
        return this;
    }

    @Override
    public Expr simplify(SimplifyOptions options) {
        Expr r = this.base.simplify(options);
        if (r != base) {
            return newInstance(r);
        }
        return this;
    }

    @Override
    public Expr normalize() {
        Expr r = this.base.normalize();
        if (r != base) {
            return newInstance(r);
        }
        return this;
    }

    @Override
    public int getDomainDimension() {
        return base.getDomainDimension();
    }

    @Override
    public Domain getDomain() {
        return base.getDomain();
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return base.getComponentDimension();
    }

    protected abstract Expr newInstance(Expr base);

    public Expr getBaseExpr() {
        return base;
    }
}
