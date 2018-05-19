package net.vpc.scholar.hadrumaths.symbolic.old.symexprerssions;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.symbolic.old.SymAbstractFct1Param;
import net.vpc.scholar.hadrumaths.symbolic.old.SymExpression;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 18 juil. 2007 21:50:31
 */
public class SymSin extends SymAbstractFct1Param {
    public SymSin(SymExpression value) {
        super("sin", value);
    }

    protected Complex eval(Complex value) {
        return value.sin();
    }

    protected SymExpression diff() {
        return new SymCos(getValue());
    }

}