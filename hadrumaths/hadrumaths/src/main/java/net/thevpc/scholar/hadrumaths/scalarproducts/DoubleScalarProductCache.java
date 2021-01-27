package net.thevpc.scholar.hadrumaths.scalarproducts;

public interface DoubleScalarProductCache extends ScalarProductCache {
    double gfDouble(int p, int n);

    double fgDouble(int n, int p);

    double[] getRowDouble(int row);

}
