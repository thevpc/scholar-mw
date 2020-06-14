package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.double2complex.CDiscrete;
import net.vpc.scholar.hadrumaths.symbolic.double2double.DDiscrete;
import net.vpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class MathsSampler {
    public static DoubleVector refineSamples(Vector<Double> values, int n) {
        DoubleVector values2 = (DoubleVector) values.to(Maths.$DOUBLE);
        return (DoubleVector) Maths.dvector(refineSamples(values2.toDoubleArray(), n));
    }

    /**
     * adds n points between each 2 points
     *
     * @param values initial sample
     * @return
     */
    public static double[] refineSamples(double[] values, int n) {
        if (n == 0) {
            return Arrays.copyOf(values, values.length);
        }
        double[] d2 = new double[values.length + n * (values.length - 1)];
        for (int i = 0; i < values.length - 1; i++) {
            int s = i * (1 + n);
            double[] d3 = Maths.dtimes(values[i], values[i + 1], n + 2);
            System.arraycopy(d3, 0, d2, s, d3.length - 1);
        }
        d2[d2.length - 1] = values[values.length - 1];
        return d2;
    }

    public static <T> AdaptiveResult1 adaptiveEval(AdaptiveFunction1<T> expr, DistanceStrategy<T> distance, DomainX domain, AdaptiveConfig config) {
        if (config == null) {
            config = new AdaptiveConfig();
        }
        double xmin = domain.xmin();
        double xmax = domain.xmax();
        int minSteps = config.getMinimumXSamples();
        int maxSamples = config.getMaximumXSamples();
        double err = config.getError();
        SamplifyListener listener = config.getListener();
        double[] dsteps = Maths.dtimes(xmin, xmax, minSteps);
        if (err <= 0) {
            err = 0.1;
        }
        double yerr = 0.02;
//        DoubleToComplex dc = expr.toDC();
//        Complex[][] complexes2 = dc.computeComplexArg(dsteps,dsteps);
        AdaptiveResult1 s = new AdaptiveResult1();
        s.x.addAll(dsteps);
        for (double x : dsteps) {
            T v = expr.eval(x);
            s.values.add(v);
        }
        BooleanArray stop = new BooleanArray();

        for (int i = 0; i < s.size(); i++) {
            stop.add(false);
        }

        if (listener != null) {
            listener.onNewElements(new AdaptiveEvent(0, s.size(), Double.NaN, 0, s));
        }

        class Diff implements Comparable<Diff> {
            final String name;
            final int type;
            int minIndex = -1;
            int maxIndex = -1;
            double minValue = 0;
            double maxValue = Double.POSITIVE_INFINITY;
            double ratio = 0;

            public Diff(String name, int type) {
                this.name = name;
                this.type = type;
            }

            void regRegular(int index, double v) {
                if (Double.isNaN(v) || Double.isInfinite(v)) {
                    return;
                }
                reg(index, v);
            }

            void reg(int index, double v) {
                if (minIndex < 0) {
                    minIndex = index;
                    maxIndex = index;
                    minValue = v;
                    maxValue = v;
                } else if (v < minValue) {
                    minValue = v;
                    minIndex = index;
                } else if (v > maxValue) {
                    maxValue = v;
                    maxIndex = index;
                }
                if (minValue == 0) {
                    ratio = maxValue;
                } else {
                    ratio = (maxValue - minValue) / minValue;
                }
            }

            void regRegularOrMax(int index, double v, double max) {
                if (Double.isNaN(v) || Double.isInfinite(v)) {
                    return;
                }
                if (v > max) {
                    v = max;
                }
                reg(index, v);
            }

            @Override
            public String toString() {
                return "Diff{" +
                        "name='" + name + '\'' +
                        ", type=" + type +
                        ", minIndex=" + minIndex +
                        ", maxIndex=" + maxIndex +
                        ", minValue=" + minValue +
                        ", maxValue=" + maxValue +
                        ", ratio=" + ratio +
                        '}';
            }

            @Override
            public int compareTo(Diff o) {
                return Double.compare(ratio, o.ratio);
            }
        }

        int TYPE_ERROR = 1;
        int TYPE_WIDTH = 2;
        int TYPE_DERIVE = 3;
        while (s.x.size() < maxSamples) {
            Diff ediff = new Diff("error", TYPE_ERROR);//error diff
            Diff wdiff = new Diff("width", TYPE_WIDTH);//width diff
            Diff ddiff = new Diff("derive", TYPE_DERIVE);//derive diff
            List<Diff> diffs = new ArrayList<>(Arrays.asList(ediff, wdiff, ddiff));
//            double maxErr = 0;
//            double minErr = Double.POSITIVE_INFINITY;
//            double minWidth = Double.POSITIVE_INFINITY;
//            double maxWidth = 0;
//            int indexWithMaxError = -1;
//            int indexWithMinWidth = -1;
//            int indexWithMaxWidth = -1;
            int count = s.values.size();
            for (int i = count - 2; i >= 0; i--) {
                if (!stop.get(i)) {
                    T c1 = (T) s.values.get(i);
                    T c2 = (T) s.values.get(i + 1);
                    double d1 = s.x.get(i);
                    double d2 = s.x.get(i + 1);
                    double d = d2 - d1;
                    ediff.regRegularOrMax(i, distance.distance(c1, c2), 200);
                    wdiff.regRegular(i, d);
                    ddiff.regRegularOrMax(i, err / d, 200);
                }
            }
            Collections.sort(diffs);

            Diff worst = diffs.get(diffs.size() - 1);
            int bestIndex = worst.maxIndex;
            s.error = worst.ratio;
            if (bestIndex == -1) {
                break;
            }
            if (s.error < err) {
                stop.add(bestIndex, true);
                continue;
            }
            double d1 = s.x.get(bestIndex);
            double d2 = s.x.get(bestIndex + 1);
            T c1 = (T) s.values.get(bestIndex);
            T c2 = (T) s.values.get(bestIndex + 1);
            double d = (d1 + d2) / 2.0;
            if (d == d1 || d == d2) {
                stop.set(bestIndex, true);
            } else {
                T c = expr.eval(d);
                if (distance.distance(c, c1) <= yerr || distance.distance(c, c2) <= yerr) {
                    //no need to add this !
                    stop.add(bestIndex, true);
                } else {


                    s.x.add(bestIndex + 1, d);
                    s.values.add(bestIndex + 1, c);
                    stop.add(bestIndex + 1, false);
                    if (listener != null) {
                        listener.onNewElements(new AdaptiveEvent(bestIndex + 1, 1, worst.ratio, worst.type, s));
                    }
                }
            }
        }
        return s;
        //now check if i have to
    }


    public static Expr discrete(Expr expr, Domain domain, int xSamples) {
        if (expr instanceof CDiscrete || expr instanceof VDiscrete) {
            return expr;
        }
        if (domain == null) {
            domain = expr.getDomain();
        }
        if (expr.is(ExprDim.SCALAR)) {
            AbsoluteSamples samples = domain.times(xSamples);
            Complex[][][] model = expr.toDC().evalComplex(samples.getX(), samples.getY(), samples.getZ(), null, null);
            return CDiscrete.of(domain, model);
        } else {
            DoubleToVector v = expr.toDV();
            ComponentDimension d = v.getComponentDimension();
            if (d.columns == 1) {
                if (d.rows == 1) {
                    AbsoluteSamples samples = domain.times(xSamples);
                    Complex[][][] model = expr.toDC().evalComplex(samples.getX(), samples.getY(), samples.getZ(), null, null);
                    return CDiscrete.of(domain, model);
                } else if (d.rows == 2) {
                    return new VDiscrete(
                            (CDiscrete) discrete(v.getComponent(Axis.X), domain, xSamples),
                            (CDiscrete) discrete(v.getComponent(Axis.Y), domain, xSamples),
                            null
                    );
                } else if (d.rows == 3) {
                    return new VDiscrete(
                            (CDiscrete) discrete(v.getComponent(Axis.X), domain, xSamples),
                            (CDiscrete) discrete(v.getComponent(Axis.Y), domain, xSamples),
                            (CDiscrete) discrete(v.getComponent(Axis.Z), domain, xSamples)
                    );
                }
            }
            throw new IllegalArgumentException("Unsupported");

        }
    }

    public static Expr discrete(Expr expr, Domain domain, int xSamples, int ySamples) {
        if (expr instanceof CDiscrete || expr instanceof VDiscrete) {
            return expr;
        }
        if (domain == null) {
            domain = expr.getDomain();
        }
        if (expr.is(ExprDim.SCALAR)) {
            AbsoluteSamples samples = domain.times(xSamples, ySamples);
            Complex[][][] model = expr.toDC().evalComplex(samples.getX(), samples.getY(), samples.getZ(), null, null);
            return CDiscrete.of(domain, model);
        } else {
            DoubleToVector v = expr.toDV();
            ComponentDimension d = v.getComponentDimension();
            if (d.columns == 1) {
                if (d.rows == 1) {
                    AbsoluteSamples samples = domain.times(xSamples, ySamples);
                    Complex[][][] model = expr.toDC().evalComplex(samples.getX(), samples.getY(), samples.getZ(), null, null);
                    return CDiscrete.of(domain, model);
                } else if (d.rows == 2) {
                    return new VDiscrete(
                            (CDiscrete) discrete(v.getComponent(Axis.X), domain, xSamples, ySamples),
                            (CDiscrete) discrete(v.getComponent(Axis.Y), domain, xSamples, ySamples),
                            null
                    );
                } else if (d.rows == 3) {
                    return new VDiscrete(
                            (CDiscrete) discrete(v.getComponent(Axis.X), domain, xSamples, ySamples),
                            (CDiscrete) discrete(v.getComponent(Axis.Y), domain, xSamples, ySamples),
                            (CDiscrete) discrete(v.getComponent(Axis.Z), domain, xSamples, ySamples)
                    );
                }
            }
            throw new IllegalArgumentException("Unsupported");

        }
    }

    public static CDiscrete discrete(Expr expr) {
        return (CDiscrete) expr.simplify();
    }

    public static VDiscrete vdiscrete(Expr expr) {
        return (VDiscrete) expr.simplify();
    }

    public static Expr discrete(Expr expr, Domain domain, int nx, int ny, int nz) {
        if (expr.is(ExprDim.SCALAR)) {
            switch (expr.getType()) {
                case DOUBLE_DOUBLE: {
                    return DDiscrete.of(expr, domain, nx, ny, nz);
                }
            }
            return CDiscrete.discretize(expr, domain, nx, ny, nz);
        } else {
            return VDiscrete.discretize(expr, domain, nx, ny, nz);
        }
    }

    public static Complex avg(VDiscrete d) {
        return d.avg();
    }

    public static Complex sum(VDiscrete d) {
        return d.sum();
    }

    public static Complex sum(CDiscrete d) {
        return d.sum();
    }

    public static Complex avg(CDiscrete d) {
        return d.avg();
    }

    public static CDiscrete sqr(CDiscrete d) {
        return d.sqr();
    }

    public static CDiscrete sqrt(CDiscrete d) {
        return d.sqrt();
    }

    public static DoubleToVector vsum(VDiscrete d) {
        return d.vsum();
    }

    public static DoubleToVector vavg(VDiscrete d) {
        return d.vavg();
    }

    public static VDiscrete sqr(VDiscrete d) {
        return d.sqr();
    }

    public static VDiscrete sqrt(VDiscrete d) {
        return d.sqrt();
    }

    public static VDiscrete abssqr(VDiscrete e) {
        return e.abssqr();
    }

    public static CDiscrete abssqr(CDiscrete e) {
        return e.abssqr();
    }


}
