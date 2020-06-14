package net.vpc.scholar.hadrumaths;

import java.util.ArrayList;
import java.util.List;

public class AdaptiveResult1 {

    public DoubleArray x = new DoubleArray();

    public List<Object> values = new ArrayList();

    public double error = Double.NaN;

    public int size() {
        return x.size();
    }
}
