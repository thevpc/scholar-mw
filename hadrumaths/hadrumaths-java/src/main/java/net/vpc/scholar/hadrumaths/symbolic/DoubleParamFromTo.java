package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.common.util.DoubleFilter;
import net.vpc.scholar.hadrumaths.Double2Filter;
import net.vpc.scholar.hadrumaths.MathsBase;

public class DoubleParamFromTo {
    private DoubleParam p;
    private double f;
    private double t;
    private double b;
    private DoubleFilter filter;

    public DoubleParamFromTo(DoubleParam p, double f) {
        this(p, f, f, 1,null);
    }

    public DoubleParamFromTo(DoubleParam p, double f, double t, double b,DoubleFilter filter) {
        this.p = p;
        this.f = f;
        this.t = t;
        this.b = b;
        this.filter = filter;
    }

    public DoubleParam getParam() {
        return p;
    }

    public DoubleParamFromTo to(double t) {
        return new DoubleParamFromTo(p, f, t, b,filter);
    }

    public DoubleParamFromTo by(double b) {
        return new DoubleParamFromTo(p, f, t, b,filter);
    }
    public DoubleParamFromTo where(DoubleFilter filter) {
        return new DoubleParamFromTo(p, f, t, b,filter);
    }

//    public DoubleParam getP() {
//        return p;
//    }
//
//    public double getF() {
//        return f;
//    }
//
//    public double getT() {
//        return t;
//    }
//
//    public double getB() {
//        return b;
//    }

    public double[] values() {
        return MathsBase.dsteps(f, t, b);
    }
    public DoubleParamFromTo2 cross(DoubleParam m) {
        return new DoubleParamFromTo2(this,new DoubleParamFromTo(m,0,0,1,null),null);
    }
}
