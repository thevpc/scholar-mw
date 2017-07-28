package net.vpc.scholar.hadrumaths.symbolic.old;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 18 juil. 2007 21:45:11
 */
public class SymVar extends SymAbstractExpression {
    public static final SymVar x=new SymVar("x");
    public static final SymVar y=new SymVar("y");
    public static final SymVar z=new SymVar("z");
    private String name;

    public SymVar(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public SymExpression diff(String varName) {
        return varName.equals(name)? SymComplex.XONE: SymComplex.XZERO;
    }

    public SymExpression eval(SymContext context) {
        SymExpression expression = context.getValue(name);
        return expression==null?this:expression;
    }

    public String toString(SymStringContext context) {
        return name;
    }
}
