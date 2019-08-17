package net.vpc.scholar.hadruplot;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 2 juin 2007 11:34:26
 */
public class PlotNamedVector {
    private Object[] matrix;
    private double[] indexes;
    private String[] titles;
    private String name;

    public PlotNamedVector(String name, Object[] matrix, double[] indexes, String[] titles) {
        this.name = name;
        this.matrix = matrix;
        this.indexes = indexes;
        this.titles = titles;
    }

    public Object[] getMatrix() {
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
