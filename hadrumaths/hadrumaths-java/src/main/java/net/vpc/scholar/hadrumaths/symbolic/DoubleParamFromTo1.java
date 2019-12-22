package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.common.util.DoubleFilter;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.MathsBase;

public class DoubleParamFromTo1 implements DoubleParamValues {
    private DoubleParam p;
    private double min;
    private double max;
    private double step;
    private DoubleFilter filter;

    public DoubleParamFromTo1(DoubleParam p, double min) {
        this(p, min, min, 1, null);
    }

    public DoubleParamFromTo1(DoubleParam p, double min, double max, double step, DoubleFilter filter) {
        this.p = p;
        this.min = min;
        this.max = max;
        this.step = step;
        this.filter = filter;
    }

    public DoubleParam getParam() {
        return p;
    }

    public DoubleParamFromTo1 to(double t) {
        return new DoubleParamFromTo1(p, min, t, step, filter);
    }

    public DoubleParamFromTo1 times(double times) {
        double step = (max - min) / (times - 1);
        return new DoubleParamFromTo1(p, min, max, step, filter);
    }

    public DoubleParamFromTo1 step(double step) {
        return new DoubleParamFromTo1(p, min, max, step, filter);
    }

    public DoubleParamFromTo1 by(double b) {
        return new DoubleParamFromTo1(p, min, max, b, filter);
    }

    public DoubleParamFromTo1 where(DoubleFilter filter) {
        return new DoubleParamFromTo1(p, min, max, step, filter);
    }

    public DoubleParamFromTo1 where(Expr filter) {
        return new DoubleParamFromTo1(p, min, max, step, filter == null ? null : new DoubleFilter() {
            @Override
            public boolean accept(double a) {
                Expr t = filter.setParam(p, a).simplify();
                if (t instanceof Complex) {
                    if (t.isDouble()) {
                        return t.toDouble() != 0;
                    }
                }
                throw new RuntimeException("Unable to evaluate " + filter + " to a valid boolean");
            }
        });
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
        return MathsBase.dsteps(min, max, step);
    }

    public DoubleParamFromTo2 cross(DoubleParam m) {
        return new DoubleParamFromTo2(this, new DoubleParamFromTo1(m, 0, 0, 1, null), null);
    }

    public DoubleParamFromTo2 and(DoubleParamFromTo1 m) {
        return cross(m);
    }

    public DoubleParamFromTo2 cross(DoubleParamFromTo1 m) {
        return new DoubleParamFromTo2(this, m, null);
    }

    @Override
    public DoubleParam[] getParams() {
        return new DoubleParam[]{getParam()};
    }

    @Override
    public double[][] getParamValues() {
        if (step >= 0) {
            if (max < min) {
                return new double[0][0];
            }
            int times = (int) Math.abs((max - min) / step) + 1;
            double[][] d = new double[times][1];
            for (int i = 0; i < d.length; i++) {
                d[i][0] = min + i * step;
            }
            return d;
        } else {
            if (min < max) {
                return new double[0][0];
            }
            int times = (int) Math.abs((max - min) / step) + 1;
            double[][] d = new double[times][];
            for (int i = 0; i < d.length; i++) {
                d[i][0] = min + i * step;
            }
            return d;
        }
    }
}
