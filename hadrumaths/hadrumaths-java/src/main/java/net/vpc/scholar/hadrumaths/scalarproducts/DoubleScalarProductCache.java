package net.vpc.scholar.hadrumaths.scalarproducts;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Vector;

public interface DoubleScalarProductCache extends ScalarProductCache{
    double gfDouble(int p, int n);

    double fgDouble(int n, int p);

    double[] getRowDouble(int row);

}
