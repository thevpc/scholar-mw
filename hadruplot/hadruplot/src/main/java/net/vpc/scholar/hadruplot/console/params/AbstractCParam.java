package net.vpc.scholar.hadruplot.console.params;

/**
 * @author taha.bensalah@gmail.com on 8/5/16.
 */
public abstract class AbstractCParam implements CParam {
    private String name;

    public AbstractCParam(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.valueOf(name);
    }
}
