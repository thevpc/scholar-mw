package net.thevpc.scholar.hadrumaths.symbolic.old;

import net.thevpc.scholar.hadrumaths.Complex;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 18 juil. 2007 21:56:12
 */
public class SymContext {
    public static final SymContext NONE = new NONEContext();
    private final Map<String, SymExpression> vars = new HashMap<String, SymExpression>();

    public SymContext() {
    }

    public SymContext removeVar(String name) {
        vars.remove(name);
        return this;
    }

    public SymContext addVar(String name, Complex value) {
        return addVar(name, value == null ? null : new SymComplex(value));
    }

    public SymContext addVar(String name, SymExpression expression) {
        if (expression == null) {
            vars.remove(name);
        } else {
            vars.put(name, expression);
        }
        return this;
    }

    public SymExpression getValue(String name) {
        return vars.get(name);
    }

    private static class NONEContext extends SymContext implements Cloneable {
        public SymContext addVar(String name, SymExpression expression) {
            //
            return this;
        }

        public SymContext removeVar(String name) {
            return this;
        }

        public SymContext addVar(String name, Complex value) {
            //
            return this;
        }

        public SymContext clone() {
            return new SymContext();
        }
    }
}
