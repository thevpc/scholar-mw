package net.vpc.scholar.hadrumaths.symbolic.old;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 20 juil. 2007 20:24:11
 */
public abstract class SymFunction extends SymAbstractExpression {
    private String name;

    protected SymFunction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
