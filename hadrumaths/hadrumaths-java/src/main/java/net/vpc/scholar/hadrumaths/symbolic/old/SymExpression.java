package net.vpc.scholar.hadrumaths.symbolic.old;

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
    public SymExpression diff(String varName);

    public SymExpression eval(SymContext context);

    public SymExpression simplify();

    public String toString(SymStringContext context);

    public SymExpression diff(String varName, int depth);

    SymExpression diff(String... varNames);
}
