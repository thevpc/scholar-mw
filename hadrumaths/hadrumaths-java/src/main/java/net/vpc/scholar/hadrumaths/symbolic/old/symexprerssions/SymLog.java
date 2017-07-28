package net.vpc.scholar.hadrumaths.symbolic.old.symexprerssions;

import net.vpc.scholar.hadrumaths.symbolic.old.SymAbstractFct1Param;
import net.vpc.scholar.hadrumaths.symbolic.old.SymExpression;
import net.vpc.scholar.hadrumaths.symbolic.old.symop.SymOpInv;
import net.vpc.scholar.hadrumaths.Complex;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 18 juil. 2007 21:50:31
 */
public class SymLog extends SymAbstractFct1Param {
    public SymLog(SymExpression value) {
        super("log",value);
    }

    protected Complex eval(Complex value) {
        return value.log();
    }

    protected SymExpression diff() {
        return new SymOpInv(getValue());
    }

}