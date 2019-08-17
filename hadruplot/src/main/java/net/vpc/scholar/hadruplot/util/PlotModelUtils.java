package net.vpc.scholar.hadruplot.util;

import net.vpc.common.util.ArrayUtils;
import net.vpc.scholar.hadruplot.*;

public class PlotModelUtils {

    public static String resolveYTitle(String[] ytitles, int i) {
        String t = null;
        if (ytitles != null && i < ytitles.length) {
            t = ytitles[i];
        }
        if (t == null) {
            t = "Y";
        }
        return t;
    }

    public static String resolveYTitle(ValuesPlotModel model, int i) {
        String t = null;
        if (model.getYtitles() != null && i < model.getYtitles().length) {
            t = model.getYtitles()[i];
        }
        if (t == null) {
            t = model.getZtitle();
        }
        if (t == null) {
            t = "Y";
        }
        return t;
    }

    public static double[] mul(double[] all, double v) {
        double[] a = new double[all.length];
        for (int i = 0; i < a.length; i++) {
            a[i] = all[i] * v;
        }
        return a;
    }

    public static ValuesPlotModel _defaultPlotModelXY(PlotBuilder builder, String xtitle, String ztitle, double[][] x, Object[][] z, PlotDoubleConverter zDoubleFunction, PlotType plotType) {
        return new ValuesPlotModel(builder.getTitle(), xtitle, ztitle, builder.getTitles().toArray(new String[0]),
                x, z, zDoubleFunction, plotType, builder.getProperties())
                .setLibraries(builder.getLibraries())
                .setXformat(builder.getXformat())
                .setYformat(builder.getYformat())
                .setZformat(builder.getZformat())
                .setConverter(builder.getConverter());
    }

    public static ValuesPlotModel _defaultPlotModelXYZ(PlotBuilder builder, String xtitle, String ytitle, String ztitle, double[] x, double[] y, Object[][] z, PlotDoubleConverter zDoubleFunction, PlotType plotType) {
        double[][] xx = x == null ? null : new double[][]{x};
        double[][] yy = y == null ? null : new double[][]{y};
        return _defaultPlotModelXYZ(builder, xtitle, ytitle, ztitle, xx, yy, z, zDoubleFunction, plotType, false);
    }

    public static ValuesPlotModel _defaultPlotModelXYZ(PlotBuilder builder,
            String xtitle,
            String ytitle,
            String ztitle,
            double[][] x, double[][] y,
            Object[][] z,
            PlotDoubleConverter zDoubleFunction,
            PlotType plotType, boolean swapxy) {
        if (x == null && builder.getSamples() != null) {
            if (!(builder.getSamples() instanceof AbsoluteSamples)) {
                throw new IllegalArgumentException("Samples should be absolute");
            }
            x = new double[][]{((AbsoluteSamples) builder.getSamples()).getX()};
        }
        if (y == null && builder.getSamples() != null) {
            if (!(builder.getSamples() instanceof AbsoluteSamples)) {
                throw new IllegalArgumentException("Samples should be absolute");
            }
            y = new double[][]{((AbsoluteSamples) builder.getSamples()).getY()};
        }
        ValuesPlotModel mm = new ValuesPlotModel(builder.getTitle(), xtitle, ytitle, ztitle, builder.getTitles().toArray(new String[0]), x, y, z, zDoubleFunction, plotType, builder.getProperties())
                .setLibraries(builder.getLibraries())
                .setXformat(swapxy ? builder.getYformat() : builder.getXformat())
                .setYformat(swapxy ? builder.getXformat() : builder.getYformat())
                .setZformat(builder.getZformat())
                .setConverter(builder.getConverter());
        postConfigure(mm, builder);
        return mm;
    }

    public static ValuesPlotModel _defaultPlotModelX(PlotBuilder builder,
            String xtitle,
            String ytitle,
            double[] x,
            Object[] y,
            PlotDoubleConverter zDoubleFunction,
            PlotType plotType, boolean preferColumn) {
        if (x == null && builder.getSamples() != null) {
            if (!(builder.getSamples() instanceof AbsoluteSamples)) {
                throw new IllegalArgumentException("Samples should be absolute");
            }
            x = ((AbsoluteSamples) builder.getSamples()).getX();
        }
        ValuesPlotModel mm = new ValuesPlotModel(
                builder.getTitle(), xtitle, ytitle,
                builder.getTitles().toArray(new String[0]), x, y, preferColumn, zDoubleFunction, plotType, builder.getProperties())
                .setLibraries(builder.getLibraries())
                .setXformat(builder.getXformat())
                .setYformat(builder.getYformat())
                .setConverter(builder.getConverter());
        postConfigure(mm, builder);
        return mm;
    }

    public static void postConfigure(PlotModel mm, PlotBuilder builder) {
        mm.setTitle(builder.getTitle());
        mm.setName(builder.getName());
    }

    public static void postConfigureNonNull(PlotModel mm, PlotBuilder builder) {
        if (builder.getTitle() != null) {
            mm.setTitle(builder.getTitle());
        }
        if (builder.getName() != null) {
            mm.setName(builder.getName());
        }
    }

    public static PlotModel _plotComplexArray1(Object[] z, PlotType preferredPlotType, boolean preferColumn, PlotBuilder builder, PlotValueType type) {
        AbsoluteSamples aa = null;

        if ((builder.getSamples() instanceof AdaptiveSamples)) {
            throw new IllegalArgumentException("Expected Absolute");
        }
        if (builder.getSamples() != null) {
            aa = Samples.toAbsoluteSamples(builder.getSamples(), builder.getDomain());
        }
        double[] x = (aa == null ? null : aa.getX());
        PlotDoubleConverter c = builder.getConverter(PlotDoubleConverter.ABS);
        if (x == null) {
            x = new double[0];
        }
        if (z == null) {
            z = new Object[0];
        }

        PlotType t = _getPlotType(preferredPlotType != null ? preferredPlotType : PlotType.CURVE, builder);
        return (_defaultPlotModelX(builder,
                builder.getXname(),
                builder.getYname(),
                x,
                z,
                c, t, preferColumn));

    }

    public static PlotModel _plotComplexArray2(Object[][] z, PlotType preferredPlotType, boolean preferColumn, PlotBuilder builder, PlotValueType type) {
        if (preferColumn) {
            if (z.length != 1) {
                preferColumn = false;
            } else {
                //TODO
                z = (Object[][]) PlotUtils.toColumn(z[0], Object.class);
            }
        }
        //should i do differently?
//        Complex[][] z=mm.getArray();
        AbsoluteSamples aa = null;

        if ((builder.getSamples() instanceof AdaptiveSamples)) {
            throw new IllegalArgumentException("Expected Absolute");
        }
        if (builder.getSamples() != null) {
            aa = Samples.toAbsoluteSamples(builder.getSamples(), builder.getDomain());
        }
        double[] x = (aa == null ? null : aa.getX());
        double[] y = (aa == null ? null : aa.getY());
        PlotDoubleConverter c = builder.getConverter(PlotDoubleConverter.ABS);
        if (y == null) {
            y = new double[0];
        }
        if (x == null) {
            x = new double[0];
        }
        if (z == null) {
            z = new Object[0][];
        }
        if (preferColumn && x.length > 0 && y.length == 0) {
            double[] s = x;
            x = y;
            y = s;
        }
        if (x.length > 0 && y.length == 0 && z.length > 0) {
            PlotType t = _getPlotType(preferredPlotType != null ? preferredPlotType : PlotType.CURVE, builder);
            return (_defaultPlotModelXYZ(builder,
                    builder.getXname(),
                    builder.getYname(),
                    null,
                    new double[][]{x},
                    null,
                    z,
                    c, t, preferColumn));
        }
        if (x.length == 0 && y.length > 0 && z.length > 0) {
            PlotType t = _getPlotType(preferredPlotType != null ? preferredPlotType : PlotType.CURVE, builder);
            DefaultPlotValueArrayType r1 = new DefaultPlotValueArrayType(type, 1);
            DefaultPlotValueArrayType r2 = new DefaultPlotValueArrayType(type, 2);
            return (_defaultPlotModelXYZ(builder,
                    builder.getXname(),
                    builder.getYname(),
                    null,
                    new double[][]{y},
                    null,
                    new Object[][]{
                        r1.toArray(r2.get(z, 0))
                    },
                    c, t, preferColumn));
        }
        if (z.length == 0) {
            PlotType t = _getPlotType(preferredPlotType != null ? preferredPlotType : PlotType.CURVE, builder);
            c = builder.getConverter(PlotDoubleConverter.REAL);
            z = new Object[1][y.length];
            for (int i = 0; i < y.length; i++) {
                z[0][i] = type.getValue(y[i]);
            }
            return (_defaultPlotModelXYZ(builder,
                    builder.getXname(),
                    builder.getYname(),
                    null,
                    new double[][]{x},
                    null,
                    z,
                    c, t, preferColumn));
        } else {
            PlotType t = _getPlotType(preferredPlotType != null ? preferredPlotType : PlotType.HEATMAP, builder);

            if (x.length == 0) {
                PlotType plotType = _getPlotType(null, builder);
                if (PlotType.POLAR != plotType) {
                    x = ArrayUtils.dsteps(1.0, z.length == 0 ? 0 : z[0].length, 1.0);
                }
            }
            if (y.length == 0) {
                y = ArrayUtils.dsteps(1.0, z.length, 1.0);
            }

            return (_defaultPlotModelXYZ(builder,
                    builder.getXname(),
                    builder.getYname(),
                    null,
                    new double[][]{x},
                    new double[][]{y},
                    z,
                    c, t, preferColumn));
        }
    }

    private static PlotType _getPlotType(PlotType plotType, PlotBuilder builder) {
        if (builder.getPlotType() != null) {
            return builder.getPlotType();
        }
        return plotType;
    }

    public static PlotModel _plotArr(Object[] any, PlotBuilder builder) {
        if (any == null) {
            return builder.createModel(null, builder);
        }
        if (any.length == 1) {
            return builder.createModel(any[0], builder);
        }
        return builder.createModel(any, builder);
    }

    public static PlotModel _plotComplexArray3(Object[][][] z, PlotType preferredPlotType, PlotBuilder builder) {
        PlotHyperCubePlotModel mm = new PlotHyperCubePlotModel()
                .setLibraries(builder.getLibraries())
                .setVdiscretes(new DefaultPlotHyperCube(new PlotCube(null, null, null, z)));
        postConfigure(mm, builder);
        return mm;
    }

    public static PlotModel _plotDoubleArray3(double[][][] z, PlotType preferredPlotType, PlotBuilder builder) {
        PlotHyperCubePlotModel mm = new PlotHyperCubePlotModel()
                .setLibraries(builder.getLibraries())
                .setVdiscretes(new DefaultPlotHyperCube(new PlotCube(null, null, null,
                        ArrayUtils.box(z))));
        postConfigure(mm, builder);
        return mm;
    }
}
