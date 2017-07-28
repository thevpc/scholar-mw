package net.vpc.scholar.hadrumaths;

/**
 * @author taha.bensalah@gmail.com on 8/5/16.
 */
public abstract class AbstractParam implements Param {
    private String name;

    public AbstractParam(String name) {
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
