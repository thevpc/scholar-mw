package net.vpc.scholar.hadruplot;

import net.vpc.common.util.ClassMap;
import net.vpc.scholar.hadruplot.console.PlotManager;

import java.util.function.Function;

//    private static class DoubleABSAction extends ValuesModelAction implements Serializable {
//
//        public DoubleABSAction(PlotModelProvider modelProvider) {
//            super("Abs", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setConverter(PlotDoubleConverter.ABS);
//        }
//    }
//
//    private static class DoubleREALAction extends ValuesModelAction implements Serializable {
//
//        public DoubleREALAction(PlotModelProvider modelProvider) {
//            super("Real", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setConverter(PlotDoubleConverter.REAL);
//        }
//    }
//
//    private static class DoubleIMAGAction extends ValuesModelAction implements Serializable {
//
//        public DoubleIMAGAction(PlotModelProvider modelProvider) {
//            super("Imag", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setConverter(PlotDoubleConverter.IMG);
//        }
//    }
//
//    private static class DoubleDBAction extends ValuesModelAction implements Serializable {
//
//        public DoubleDBAction(PlotModelProvider modelProvider) {
//            super("DB", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setConverter(PlotDoubleConverter.DB);
//        }
//    }
//
//    private static class DoubleDB2Action extends ValuesModelAction implements Serializable {
//
//        public DoubleDB2Action(PlotModelProvider modelProvider) {
//            super("DB2", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setConverter(PlotDoubleConverter.DB2);
//        }
//    }
//
//    private static class DoubleArgAction extends ValuesModelAction implements Serializable {
//
//        public DoubleArgAction(PlotModelProvider modelProvider) {
//            super("Arg", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setConverter(PlotDoubleConverter.ARG);
//        }
//    }
//
//    private static class ComplexAction extends ValuesModelAction implements Serializable {
//
//        public ComplexAction(PlotModelProvider modelProvider) {
//            super("Complex", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setConverter(PlotDoubleConverter.COMPLEX);
//        }
//    }
//    private static class PlotCourbeAction extends ValuesModelAction implements Serializable {
//
//        public PlotCourbeAction(PlotModelProvider modelProvider) {
//            super("Curves", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setPlotType(PlotType.CURVE);
//        }
//    }
//
//    private static class PlotHeatMapAction extends ValuesModelAction implements Serializable {
//
//        public PlotHeatMapAction(PlotModelProvider modelProvider) {
//            super("Heat Map", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setPlotType(PlotType.HEATMAP);
//        }
//    }
//
//    private static class PlotTableAction extends ValuesModelAction implements Serializable {
//
//        public PlotTableAction(PlotModelProvider modelProvider) {
//            super("Table", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setPlotType(PlotType.TABLE);
//        }
//    }
public class PlotConfig {

    private int maxLegendCount = 20;
    private String defaultWindowTitle = "Hadrumaths Plot";
    private ClassMap<Function> objectConverters = new ClassMap<Function>(Object.class, Function.class);
    private PlotManager manager = null;

    public PlotManager getManager() {
        return manager;
    }

    public void setManager(PlotManager manager) {
        this.manager = manager;
    }

    public   int getMaxLegendCount() {
        return maxLegendCount;
    }

    public   void setMaxLegendCount(int maxLegendCount) {
        this.maxLegendCount = maxLegendCount;
    }

    public   String getDefaultWindowTitle() {
        return defaultWindowTitle;
    }

    public   void setDefaultWindowTitle(String defaultWindowTitle) {
        this.defaultWindowTitle = defaultWindowTitle;
    }

    public   Object convert(Object object) {
        if (object == null) {
            return null;
        }
        Function converter = objectConverters.get(object.getClass());
        if (converter != null) {
            return converter.apply(object);
        }
        return object;
    }

    public   void registerConverter(Class cls, Function converter) {
        objectConverters.put(cls, converter);
    }

    public   void unregisterConverter(Class cls) {
        objectConverters.remove(cls);
    }
}
