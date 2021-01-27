package net.thevpc.scholar.hadrumaths.symbolic.old;

import net.thevpc.scholar.hadrumaths.symbolic.old.symop.SymOpAdd;
import net.thevpc.scholar.hadrumaths.symbolic.old.symop.SymOpDiv;
import net.thevpc.scholar.hadrumaths.symbolic.old.symop.SymOpMul;
import net.thevpc.scholar.hadrumaths.symbolic.old.symop.SymOpSub;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 20 juil. 2007 08:05:37
 */
public abstract class SymAbstractExpression implements SymExpression {

    public String toString() {
        SymStringContext context = new SymStringContext(1000, false);
        return toString(context);
    }

    public SymExpression add(SymExpression e) {
        return new SymOpAdd(this, e);
    }

    public SymExpression sub(SymExpression e) {
        return new SymOpSub(this, e);
    }

    public SymExpression mul(SymExpression e) {
        return new SymOpMul(this, e);
    }

    public SymExpression div(SymExpression e) {
        return new SymOpDiv(this, e);
    }

    public SymExpression inv(SymExpression e) {
        return new SymOpDiv(this, e);
    }

    public SymExpression simplify() {
        return eval(SymContext.NONE);
    }

    public SymExpression diff(String varName, int depth) {
        SymExpression e = this;
        for (int i = 0; i < depth; i++) {
            e = e.diff(varName);
        }
        return e;
    }

    public SymExpression diff(String... varNames) {
        SymExpression e = this;
        for (String varName : varNames) {
            e = e.diff(varName);
        }
        return e;
    }
}
