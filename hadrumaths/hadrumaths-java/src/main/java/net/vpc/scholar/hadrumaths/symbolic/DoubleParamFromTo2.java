package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Double2Filter;
import net.vpc.scholar.hadrumaths.MathsBase;

public class DoubleParamFromTo2 {
    private DoubleParamFromTo a;
    private DoubleParamFromTo b;
    private Double2Filter filter;

    public DoubleParamFromTo2(DoubleParamFromTo a, DoubleParamFromTo b, Double2Filter filter) {
        this.a = a;
        this.b = b;
        this.filter = filter;
    }

    public DoubleParamFromTo2 to(double t) {
        return new DoubleParamFromTo2(a, b.to(t), filter);
    }

    public DoubleParamFromTo2 by(double b) {
        return new DoubleParamFromTo2(a, this.b.by(b), filter);
    }

    public DoubleParamFromTo2 where(Double2Filter filter) {
        return new DoubleParamFromTo2(a, this.b, filter);
    }

    public DoubleParam getParam1() {
        return a.getParam();
    }

    public DoubleParam getParam2() {
        return b.getParam();
    }

    public DoubleParamFromTo getB() {
        return b;
    }

    public double[][] values() {
        return MathsBase.cross(a.values(), b.values(), filter);
    }
}
