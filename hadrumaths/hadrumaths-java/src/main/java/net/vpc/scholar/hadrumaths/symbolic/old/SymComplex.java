package net.vpc.scholar.hadrumaths.symbolic.old;

import net.vpc.scholar.hadrumaths.Complex;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 18 juil. 2007 21:46:57
 */
public class SymComplex extends SymAbstractExpression {
    public static final SymComplex XZERO = new SymComplex(Complex.ZERO);
    public static final SymComplex XONE = new SymComplex(Complex.ONE);
    private final Complex value;

    public SymComplex(Complex value) {
        this.value = value;
    }

    public String toString() {
        return value.toString();
    }

    public Complex getValue() {
        return value;
    }

    public SymExpression diff(String varName) {
        return XZERO;
    }

    public SymExpression eval(SymContext context) {
        return this;
    }

    public String toString(SymStringContext context) {
        String s = value.toString();
        if (
                (context.isPreferParentheses()) ||
                        (
                                context.getOperatorPrecedence() > SymStringContext.ADD_PRECEDENCE
                                        && value.getReal() != 0
                                        && value.getImag() != 0
                                        && !value.isNaN()
                                        && !value.isNaN()
                        )) {

        }
        return context.isPreferParentheses() ? ("(" + s + ")") : s;
    }
}
