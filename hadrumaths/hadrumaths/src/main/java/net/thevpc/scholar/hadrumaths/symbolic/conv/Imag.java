/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.symbolic.conv;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.scholar.hadrumaths.BooleanMarker;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToComplex;

/**
 * @author vpc
 */
public class Imag extends AbstractDCxyToDDxyExpr {
    private static final long serialVersionUID = 1L;

    public static Imag of(Expr r){
        return new Imag(r.toDC());
    }

    public Imag(DoubleToComplex base) {
        super(base);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Imag");
    }

    @Override
    public Expr newInstance(Expr[] arg) {
        return new Imag(arg[0].toDC());
    }

    @Override
    public double evalDouble(double x, BooleanMarker defined) {
        return getArg().evalComplex(x, defined).getImag();
    }

    @Override
    public double evalDouble(double x, double y, BooleanMarker defined) {
        return getArg().evalComplex(x, y, defined).getImag();
    }

    @Override
    public double evalDouble(double x, double y, double z, BooleanMarker defined) {
        return getArg().evalComplex(x, y, z, defined).getImag();
    }

    @Override
    public String toLatex() {
        StringBuilder sb=new StringBuilder();
        sb.append("\\text{").append("imag").append("}\\left(");
        sb.append(getArg().toLatex());
        sb.append("\\right)");
        return sb.toString();
    }

}
