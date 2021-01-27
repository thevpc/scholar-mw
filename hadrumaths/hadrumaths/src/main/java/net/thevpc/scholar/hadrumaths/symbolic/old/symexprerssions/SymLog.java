package net.thevpc.scholar.hadrumaths.symbolic.old.symexprerssions;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.symbolic.old.SymAbstractFct1Param;
import net.thevpc.scholar.hadrumaths.symbolic.old.SymExpression;
import net.thevpc.scholar.hadrumaths.symbolic.old.symop.SymOpInv;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 18 juil. 2007 21:50:31
 */
public class SymLog extends SymAbstractFct1Param {
    public SymLog(SymExpression value) {
        super("log", value);
    }

    protected Complex eval(Complex value) {
        return value.log();
    }

    protected SymExpression diff() {
        return new SymOpInv(getValue());
    }

}