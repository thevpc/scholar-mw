package net.thevpc.scholar.hadrumaths.symbolic.old;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 18 juil. 2007 21:45:58
 */
public interface SymExpression {
    /**
     * differentiate (derivation)
     *
     * @param varName var name
     * @return differentiated expression
     */
    SymExpression diff(String varName);

    SymExpression eval(SymContext context);

    SymExpression simplify();

    String toString(SymStringContext context);

    SymExpression diff(String varName, int depth);

    SymExpression diff(String... varNames);
}
