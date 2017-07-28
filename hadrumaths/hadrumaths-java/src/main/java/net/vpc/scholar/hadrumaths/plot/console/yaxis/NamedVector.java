package net.vpc.scholar.hadrumaths.plot.console.yaxis;

import net.vpc.scholar.hadrumaths.Complex;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 2 juin 2007 11:34:26
 */
public class NamedVector {
    private Complex[] matrix;
    private double[] indexes;
    private String[] titles;
    private String name;

    public NamedVector(String name, Complex[] matrix, double[] indexes, String[] titles) {
        this.name = name;
        this.matrix = matrix;
        this.indexes = indexes;
        this.titles = titles;
    }

    public Complex[] getMatrix() {
        return matrix;
    }

    public double[] getIndexes() {
        return indexes;
    }

    public String[] getTitles() {
        return titles;
    }

    public String getName() {
        return name;
    }
}
