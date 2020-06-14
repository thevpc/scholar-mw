/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic.conv;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.util.internal.IgnoreRandomGeneration;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.ExprDefaults;
import net.vpc.scholar.hadrumaths.symbolic.double2double.AbstractDoubleToDouble;

import java.util.Arrays;
import java.util.List;

/**
 * @author vpc
 */
@IgnoreRandomGeneration
public abstract class AbstractDCxyToDDxyExpr extends AbstractDoubleToDouble {
    private static final long serialVersionUID = 1L;

    private final DoubleToComplex base;

    public AbstractDCxyToDDxyExpr(DoubleToComplex base) {
        super();
        this.base = base;
    }

    public DoubleToComplex getBase() {
        return base;
    }

    public Domain getDomain() {
        return base.getDomain();
    }

    public List<Expr> getChildren() {
        return Arrays.asList(new Expr[]{base});
    }

    @Override
    public boolean isInvariant(Axis axis) {
        return base.isInvariant(axis);
    }

    public boolean isZero() {
        return base.isZero();
    }

    public boolean isNaN() {
        return base.isNaN();
    }

    @Override
    public Expr setParam(String name, Expr value) {
        DoubleToComplex old = getArg();
        DoubleToComplex updated = old.setParam(name, value).toDC();
        if (updated != old) {
            Expr e = newInstance(updated);
            e = ExprDefaults.copyProperties(this, e);
            return ExprDefaults.updateTitleVars(e, name, value);
        }
        return this;
    }

    public boolean isInfinite() {
        return base.isInfinite();
    }

    public DoubleToComplex getArg() {
        return base;
    }

    @Override
    public int hashCode() {
        int result = getClass().getName().hashCode();
        result = 31 * result + (base != null ? base.hashCode() : 0);
        return result;
    }

//    protected abstract Expr newInstance(DoubleToComplex arg);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractDCxyToDDxyExpr that = (AbstractDCxyToDDxyExpr) o;

        return base != null ? base.equals(that.base) : that.base == null;
    }
}
