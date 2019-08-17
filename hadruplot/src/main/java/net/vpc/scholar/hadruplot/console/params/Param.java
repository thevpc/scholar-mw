package net.vpc.scholar.hadruplot.console.params;

/**
 * @author taha.bensalah@gmail.com on 8/5/16.
 */
public interface Param {
    String getName();

    void configure(Object source, Object value);

}
