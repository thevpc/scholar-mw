package net.vpc.scholar.hadruplot.console;

import net.vpc.common.util.*;
import net.vpc.scholar.hadruplot.*;

import java.awt.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.vpc.scholar.hadruplot.util.DefaultValuesPlotModelPopupFactory;

public class PlotConfigManager {
    private static java.util.List<PlotModelPanelFactory> plotModelPanelFactories = new ArrayList<>();
    private static java.util.List<PlotModelPopupFactory> plotModelPopupFactories = new ArrayList<>();
    private static java.util.List<PlotConsoleCacheSupport> cacheSupports = new ArrayList<>();
    private static List<PlotFileType> plotFileTypes = new ArrayList<>();
    private static List<PlotBuilderSupport> plotBuilderSupports = new ArrayList<>();
    private static PlotValueTypeFactory plotValueTypeFactory = new PlotValueTypeFactory();
    private static List<PlotValueFactory> plotValueFactories = new ArrayList<>();
    private static List<PlotModelFactory> plotModelFactories = new ArrayList<>();

    static {
        addPlotValueFactory(DefaultPlotValueFactory.INSTANCE);
        addPlotModelFactory(DefaultPlotModelFactory.INSTANCE);
        addPlotModelPopupFactory(new DefaultValuesPlotModelPopupFactory());
        addPlotModelPanelFactory(new PlotHyperCubePlotModelPanelFactory());
    }

    public static PlotFileType[] getPlotFileTypes() {
        return plotFileTypes.toArray(new PlotFileType[0]);
    }


    public static net.vpc.scholar.hadruplot.console.PlotConfig Config = new PlotConfig() {
        @Override
        public String getCacheFolder(String path) {
            return null;
        }

        @Override
        public DoubleFormat dblformat(String format) {
            if (format == null) {
                format = "";
            }
            switch (format.trim()) {
                case "":
                    return new DecimalDoubleFormat("0.0");
                case "f":
                case "freq":
                    return FrequencyFormat.INSTANCE;
                case "b":
                case "bytes":
                    return BytesSizeFormat.INSTANCE;
                case "%":
                case "percent":
                    return PercentDoubleFormat.INSTANCE;
            }
            return new DecimalDoubleFormat("0.0");
        }
    };

    public static ColorPalette DEFAULT_PALETTE = new ColorArrayPalette("Array", new Color[]{
            new Color(0xFF, 0x55, 0x55),
            new Color(0x55, 0x55, 0xFF),
            new Color(0x55, 0xFF, 0x55),
            new Color(0xFF, 0xFF, 0x55),
            new Color(0xFF, 0x55, 0xFF),
            new Color(0x55, 0xFF, 0xFF),
            Color.pink,
            Color.gray,
            new Color(0xc0, 0x00, 0x00),
            new Color(0x00, 0x00, 0xC0),
            new Color(0x00, 0xC0, 0x00),
            new Color(0xC0, 0xC0, 0x00),
            new Color(0xC0, 0x00, 0xC0),
            new Color(0x00, 0xC0, 0xC0),
            new Color(64, 64, 64),
            new Color(0xFF, 0x40, 0x40),
            new Color(0x40, 0x40, 0xFF),
            new Color(0x40, 0xFF, 0x40),
            new Color(0xFF, 0xFF, 0x40),
            new Color(0xFF, 0x40, 0xFF),
            new Color(0x40, 0xFF, 0xFF),
            new Color(192, 192, 192),
            new Color(0x80, 0x00, 0x00),
            new Color(0x00, 0x00, 0x80),
            new Color(0x00, 0x80, 0x00),
            new Color(0x80, 0x80, 0x00),
            new Color(0x80, 0x00, 0x80),
            new Color(0x00, 0x80, 0x80),
            new Color(0xFF, 0x80, 0x80),
            new Color(0x80, 0x80, 0xFF),
            new Color(0x80, 0xFF, 0x80),
            new Color(0xFF, 0xFF, 0x80),
            new Color(0x00, 0x80, 0x00),
            new Color(0x80, 0xFF, 0xFF)
    });

    public static PlotModelPopupFactory[] getPlotModelPopupFactories() {
        return plotModelPopupFactories.toArray(new PlotModelPopupFactory[0]);
    }

    public static void addPlotBuilderSupport(PlotBuilderSupport any) {
        if (any != null && plotBuilderSupports.indexOf(any) < 0) {
            plotBuilderSupports.add(any);
        }
    }

    public static PlotBuilderSupport[] getPlotBuilderSupports() {
        return plotBuilderSupports.toArray(new PlotBuilderSupport[0]);
    }

    public static void addPlotModelPopupFactory(PlotModelPopupFactory f) {
        plotModelPopupFactories.add(f);
    }

    public static PlotModelPanelFactory[] getPlotModelPanelFactories() {
        return plotModelPanelFactories.toArray(new PlotModelPanelFactory[0]);
    }

    public static PlotConsoleCacheSupport[] getCacheSupports() {
        return cacheSupports.toArray(new PlotConsoleCacheSupport[0]);
    }

    public static void addPlotModelPanelFactory(PlotModelPanelFactory f) {
        plotModelPanelFactories.add(f);
    }

    public static void addPlotFileType(PlotFileType any) {
        if (any != null && plotFileTypes.indexOf(any) < 0) {
            plotFileTypes.add(any);
        }
    }

    public static void addPlotCacheSupport(PlotConsoleCacheSupport any) {
        if (any != null && cacheSupports.indexOf(any) < 0) {
            cacheSupports.add(any);
        }
    }

    public static void addPlotFileTypes(PlotFileType... any) {
        if (any != null) {
            for (PlotFileType o : any) {
                addPlotFileType(o);
            }

        }
    }

    public static PlotNumbers Numbers = new PlotNumbers() {
        @Override
        public double toDouble(Object o) {
            double d = Double.NaN;
            if (o == null) {
                d = 0;
            } else if (o instanceof Number) {
                d = ((Number) o).doubleValue();
            }
            if (Double.isNaN(d)) {
                d = 0;
            }
            return d;
        }

        @Override
        public Object plus(Object a, Object b) {
            if (a == null) {
                return b;
            }
            if (b == null) {
                return a;
            }

            if (a instanceof Number && b instanceof Number) {
                return ((Number) a).doubleValue() + ((Number) b).doubleValue();
            }
            return Double.NaN;
        }

        @Override
        public int compare(Object a, Object b) {
            if (a == b) {
                return 0;
            }
            if (a == null) {
                return -1;
            }
            if (b == null) {
                return 1;
            }

            if (a instanceof Number && b instanceof Number) {
                return Double.compare(((Number) a).doubleValue(), ((Number) b).doubleValue());
            }
            return Integer.MIN_VALUE;
        }

        @Override
        public PlotDoubleComplex toDoubleComplex(Object o) {
            if (o instanceof Number) {
                return new PlotDoubleComplex(
                        ((Number) o).doubleValue(),
                        0
                );
            }
            throw new IllegalArgumentException("Unsupported");
        }

        @Override
        public Object mul(Object o, double b) {
            if (o instanceof BigDecimal) {
                return ((BigDecimal) o).multiply(new BigDecimal(b));
            } else if (o instanceof BigInteger) {
                return new BigDecimal((BigInteger) o).multiply(new BigDecimal(b));
            } else if (o instanceof Number) {
                return ((Number) o).doubleValue() * b;
            }
            throw new IllegalArgumentException("Unsupported");
        }

        @Override
        public double relativeError(Object aa, Object bb){
            double a = ((Number)aa).doubleValue();
            double b = ((Number)bb).doubleValue();
            double c;
            if (a == b || (Double.isNaN(a) && Double.isNaN(b)) || (Double.isInfinite(a) && Double.isInfinite(b))) {
                c = 0;
//                    } else if (b.isNaN() || b.isInfinite() || b.equals(Math2.CZERO)) {
//                        c[i][j] = (a.substract(b));
            } else {
                c = Math.abs(((a - b) * 100 / (b)));
            }

            return c;
        }

        @Override
        public PlotDoubleConverter resolveDoubleConverter(Object d) {
            return PlotDoubleConverter.REAL;
        }
    };

    public static PlotValueTypeFactory getPlotValueTypeFactory() {
        return plotValueTypeFactory;
    }

    public static PlotValueFactory[] getPlotValueFactories() {
        return plotValueFactories.toArray(new PlotValueFactory[0]);
    }

    public static PlotModelFactory[] getPlotModelFactories() {
        return plotModelFactories.toArray(new PlotModelFactory[0]);
    }

    public static void addPlotValueFactory(PlotValueFactory f) {
        if (f != null && plotValueFactories.indexOf(f) < 0) {
            plotValueFactories.add(f);
        }
    }

    public static void addPlotModelFactory(PlotModelFactory f) {
        if (f != null && plotModelFactories.indexOf(f) < 0) {
            plotModelFactories.add(f);
        }
    }

    public static void setPlotModelFactories(PlotModelFactory[] array) {
        plotModelFactories.clear();
        if (array != null) {
            for (PlotModelFactory m : array) {
                addPlotModelFactory(m);
            }
        }
    }

    public static void setPlotValueFactories(PlotValueFactory[] array) {
        plotValueFactories.clear();
        if (array != null) {
            for (PlotValueFactory m : array) {
                addPlotValueFactory(m);
            }
        }
    }

    public static boolean isColumn(Object any){
        for (PlotValueFactory plotValueFactory : PlotConfigManager.getPlotValueFactories()) {
            if(plotValueFactory.isColumn(any)){
                return true;
            }
        }
        return false;
    }

    public static PlotValue createPlotValue(Object any,PlotBuilder builder){
        List<PlotValue> p = createPlotValues(any, builder);
        return p.get(0);
    }

    public static List<PlotValue> createPlotValues(Object any,PlotBuilder builder){
        List<PlotValue> plottableData = new ArrayList<>();
        for (PlotValueFactory plotValueFactory : PlotConfigManager.getPlotValueFactories()) {
            PlotValue plotData = plotValueFactory.createPlotValue(any, builder);
            if (plotData != null) {
                plottableData.add(plotData);
                break; //TODO FIX ME
            }
        }
        Collections.sort(plottableData, new Comparator<PlotValue>() {
            @Override
            public int compare(PlotValue o1, PlotValue o2) {
                return -Integer.compare(o1.getPriority(),o2.getPriority());
            }
        });
        return plottableData;
    }
}
