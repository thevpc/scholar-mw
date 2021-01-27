package net.thevpc.scholar.hadruplot;

import java.util.*;
import java.util.function.ToDoubleFunction;
import net.thevpc.scholar.hadruplot.console.PlotConfigManager;

/**
 * Created by IntelliJ IDEA. User: vpc Date: 9 sept. 2005 Time: 22:35:03 To
 * change this template use File | Settings | File Templates.
 */
public abstract class PlotDoubleConverter implements ToDoubleFunction<Object> {

    //    ABS, REAL, IMG, DB, DB2, ARG, COMPLEX
    private static Map<String, ToDoubleFunction<Object>> registered = new LinkedHashMap<>();
    public static final PlotDoubleConverter ABS = new AbstractPlotDoubleConverter("ABS", true) {
        @Override
        public double toDouble(Object c) {
            if (c == null) {
                return Double.NaN;
            }
            if (c instanceof Number) {
                return Math.abs(((Number) c).doubleValue());
            }
            return Double.NaN;
        }

        @Override
        public Object toComplex(double d) {
            return d;
        }
    };
    public static final PlotDoubleConverter REAL = new AbstractPlotDoubleConverter("REAL", true) {
        @Override
        public double toDouble(Object c) {
            if (c == null) {
                return Double.NaN;
            }
            if (c instanceof Number) {
                return (((Number) c).doubleValue());
            }
            return Double.NaN;
        }

        @Override
        public Object toComplex(double d) {
            return d;
        }
    };

    public static ToDoubleFunction<Object> intern(ToDoubleFunction<Object> c) {
        if (c == null) {
            c = registered.get(REAL.toString());
        } else {
            c = registered.get(c.toString());
        }
        if (c == null) {
            return REAL;
        }
        return c;
    }

    public static ToDoubleFunction<Object>[] values() {
        return registered.values().toArray(new PlotDoubleConverter[0]);
    }

    public static ToDoubleFunction<Object> of(String n) {
        return registered.get(n);
    }

    public static abstract class AbstractPlotDoubleConverter extends PlotDoubleConverter {

        private String name;

        public AbstractPlotDoubleConverter(String name, boolean override) {
            this.name = name;
            if (!override) {
                if (registered.containsKey(name)) {
                    throw new IllegalArgumentException("Already registered " + name);
                }
            }
            registered.put(name, this);
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return String.valueOf(name);
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 23 * hash + Objects.hashCode(this.name);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final AbstractPlotDoubleConverter other = (AbstractPlotDoubleConverter) obj;
            if (!Objects.equals(this.name, other.name)) {
                return false;
            }
            return true;
        }

    }

    public abstract String getName();

    public double[] toDouble(Object[] c) {
        double[] d = new double[c.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = applyAsDouble(c[i]);
        }
        return d;
    }

    public double[][] toDouble(Object[][] c) {
        double[][] d = new double[c.length][];
        for (int i = 0; i < d.length; i++) {
            d[i] = toDouble(c[i]);
        }
        return d;
    }

    public double[][][] toDouble(Object[][][] c) {
        double[][][] d = new double[c.length][][];
        for (int i = 0; i < d.length; i++) {
            d[i] = toDouble(c[i]);
        }
        return d;
    }

    public abstract Object toComplex(double d);

    public Object[][] toComplex(double[][] d) {
        Object[][] x = new Object[d.length][];
        for (int i = 0; i < d.length; i++) {
            x[i] = toComplex(d[i]);
        }
        return x;
    }

    public Object[][][] toComplex(double[][][] d) {
        Object[][][] x = new Object[d.length][][];
        for (int i = 0; i < d.length; i++) {
            x[i] = toComplex(d[i]);
        }
        return x;
    }

    public Object[] toComplex(double[] d) {
        Object[] xx = new Object[d.length];
        for (int j = 0; j < xx.length; j++) {
            xx[j] = toComplex(d[j]);
        }
        return xx;
    }

    @Override
    public double applyAsDouble(Object t) {
        return toDouble(t);
    }

    @Override
    public String toString() {
        return getName();
    }

    public abstract double toDouble(Object c);
//            public static double toDouble(Complex c) {
//        if (d == null) {
//            return c.absdbl();
//        }
//        switch (d) {
//            case ABS:
//                return c.absdbl();
//            case REAL:
//                return c.realdbl();
//            case IMG:
//                return c.imagdbl();
//            case DB:
//                return db(c.absdbl());
//            case DB2:
//                return db2(c.absdbl());
//            case ARG:
//                return c.arg().getReal();
//            case COMPLEX:
//                return c.absdbl();
//        }
//        return Double.NaN;
//    }

    public static double toDouble(ToDoubleFunction<Object> cc, Object c) {
        cc=PlotDoubleConverter.intern(cc);
        return cc.applyAsDouble(c);
    }

    public static Object toComplex(ToDoubleFunction<Object> cc, Object c) {
        cc=PlotDoubleConverter.intern(cc);
        PlotComplex u = PlotConfigManager.getPlotComplexResolvers().resolve(c);
        if (u != null) {
            return u;
        }
        return c;
    }

    public static double[] toDouble(ToDoubleFunction<Object> cc, Object[] c) {
        cc=PlotDoubleConverter.intern(cc);
        double[] d = new double[c.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = cc.applyAsDouble(c[i]);
        }
        return d;
    }

    public static double[][] toDouble(ToDoubleFunction<Object> cc, Object[][] c) {
        cc=PlotDoubleConverter.intern(cc);
        double[][] d = new double[c.length][];
        for (int i = 0; i < d.length; i++) {
            d[i] = toDouble(cc, c[i]);
        }
        return d;
    }

    public static double[][][] toDouble(ToDoubleFunction<Object> cc, Object[][][] c) {
        cc=PlotDoubleConverter.intern(cc);
        double[][][] d = new double[c.length][][];
        for (int i = 0; i < d.length; i++) {
            d[i] = toDouble(cc, c[i]);
        }
        return d;
    }

    public static Object[][] toComplex(ToDoubleFunction<Object> cc, double[][] d) {
        cc=PlotDoubleConverter.intern(cc);
        Object[][] x = new Object[d.length][];
        for (int i = 0; i < d.length; i++) {
            x[i] = toComplex(cc, d[i]);
        }
        return x;
    }

    public static Object[][][] toComplex(ToDoubleFunction<Object> cc, double[][][] d) {
        cc=PlotDoubleConverter.intern(cc);
        Object[][][] x = new Object[d.length][][];
        for (int i = 0; i < d.length; i++) {
            x[i] = toComplex(cc, d[i]);
        }
        return x;
    }

    public static Object[] toComplex(ToDoubleFunction<Object> cc, double[] d) {
        cc=PlotDoubleConverter.intern(cc);
        Object[] xx = new Object[d.length];
        for (int j = 0; j < xx.length; j++) {
            xx[j] = toComplex(cc, d[j]);
        }
        return xx;
    }

}
