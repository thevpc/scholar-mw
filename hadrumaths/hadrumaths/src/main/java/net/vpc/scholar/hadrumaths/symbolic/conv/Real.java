/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic.conv;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.BooleanMarker;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.ExprDefaults;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;

/**
 * @author vpc
 */
public class Real extends AbstractDCxyToDDxyExpr {
    private static final long serialVersionUID = 1L;

    public static Real of(Expr r){
        return new Real(r.toDC());
    }

    public Real(DoubleToComplex base) {
        super(base);
    }

    public boolean isInvariant(Axis axis) {
//        switch (axis) {
//            case X:
//            case Y: {
//                return false;
//
//            }
//        }
        return getArg().isInvariant(axis);
    }

    @Override
    public Expr setParam(String name, Expr value) {
        DoubleToComplex old = getArg();
        DoubleToComplex updated = old.setParam(name, value).toDC();
        if (updated != old) {
            Expr e = new Real(updated);
            e = ExprDefaults.copyProperties(this, e);
            return ExprDefaults.updateTitleVars(e, name, value);
        }
        return this;
    }

    @Override
    public DoubleToComplex toDC() {
        if (getBase().isNarrow(ExprType.DOUBLE_DOUBLE)) {
            return getBase();
        }
        return new DefaultDoubleToComplex(this);
    }

    @Override
    public Expr newInstance(Expr[] arg) {
        return new Real(arg[0].toDC());
    }

    @Override
    public double evalDouble(double x, BooleanMarker defined) {
        return getArg().evalComplex(x, defined).getReal();
    }

    public double evalDouble(double x, double y, BooleanMarker defined) {
        return getArg().evalComplex(x, y, defined).getReal();
    }

    public double evalDouble(double x, double y, double z, BooleanMarker defined) {
        return getArg().evalComplex(x, y, z, defined).getReal();
    }

    @Override
    public String toLatex() {
        StringBuilder sb=new StringBuilder();
        sb.append("\\text{").append("real").append("}\\left(");
        sb.append(getArg().toLatex());
        sb.append("\\right)");
        return sb.toString();
    }
}
