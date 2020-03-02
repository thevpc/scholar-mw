package net.vpc.scholar.hadruplot;

import net.vpc.common.strings.StringUtils;
import net.vpc.common.swings.SwingUtilities3;
import net.vpc.common.util.DoubleArrayList;
import net.vpc.common.util.DoubleFormat;
import net.vpc.scholar.hadruplot.console.PlotConfigManager;
import net.vpc.scholar.hadruplot.console.PlotConsole;

import javax.swing.*;
import java.io.File;
import java.util.*;

/**
 * Created by vpc on 5/6/14.
 */
public class PlotBuilder {
    private static PlotBuilderSupport defaultSupport = new PlotBuilderSupport() {
        @Override
        public boolean xsamples(Object xvalue, PlotBuilder builder) {
            return false;
        }

        @Override
        public boolean ysamples(Object xvalue, PlotBuilder builder) {
            return false;
        }

        @Override
        public boolean zsamples(Object xvalue, PlotBuilder builder) {
            return false;
        }

    };
    private static List<PlotBuilderSupport> supports = new ArrayList<>();

    static {
        ServiceLoader<PlotBuilderSupport> s = ServiceLoader.load(PlotBuilderSupport.class);
        for (PlotBuilderSupport plotBuilderSupport : s) {
            supports.add(plotBuilderSupport);
        }
        supports.add(defaultSupport);
    }

    private PlotDomain domain;
    private PlotContainer parent;
    private PlotComponent update;
    private String updateName;
    private String name;
    private String title;
    private List<String> titles = new ArrayList<>();
    private boolean display = true;
    private boolean constX = false;
    private PlotType plotType;
    private PlotDoubleConverter converter;
    private String xname;
    private String yname;
    private int xsamples = -1;
    private int ysamples = -1;
    private int zsamples = -1;
    private Map<String, Object> properties = new HashMap<String, Object>();
    private String library;
    private PlotWindowManager windowManager;
    private DoubleFormat xformat;
    private DoubleFormat yformat;
    private DoubleFormat zformat;
    private Samples samples;
    private String path = "/";
    private List<Object> itemsToPlot = new ArrayList<>();
    private List<Object> xsamplesToPlot = new ArrayList<>();
    private List<PlotBuilderListener> listeners = new ArrayList<>();

//    public PlotComponent plotAll(Object... any) {
//        return plot((Object) any);
//    }

    public PlotComponent plot() {
        return plot(new Object[0]);
    }

    public PlotComponent plot(Object ... any) {
        return createPlotComponent(createModel(any));
    }

    public PlotModel createModel(Object... any) {
        List<Object> all = new ArrayList<>();
        all.addAll(itemsToPlot);
        for (Object o : any) {
            if (o != null) {
                all.add(o);
            }
        }
        return (_plotArr(all.toArray()));
    }

    public PlotBuilder mimic(PlotBuilder other) {
        domain = other.domain;
        title = other.title;
        name = other.name;
        plotType = other.plotType;
        converter = other.converter;
        xname = other.xname;
        yname = other.yname;
        samples = other.samples;
        xsamples = other.xsamples;
        ysamples = other.ysamples;
        zsamples = other.zsamples;
        display = other.display;
        windowManager = other.windowManager;
        library = other.library;
        properties = other.properties == null ? null : new HashMap<>(other.properties);
        titles = other.titles == null ? null : new ArrayList<>(other.titles);
        xformat = other.xformat;
        yformat = other.yformat;
        zformat = other.zformat;
        parent = other.parent;
        itemsToPlot = new ArrayList<Object>(other.itemsToPlot);
        xsamplesToPlot = new ArrayList<Object>(other.xsamplesToPlot);
        return this;
    }

    public PlotBuilder addTitle(String item) {
        titles.add(item);
        return this;
    }

    public PlotBuilder addXsample(Object item) {
        xsamplesToPlot.add(item);
        return this;
    }

    public PlotBuilder add(Object item) {
        if (item != null) {
            itemsToPlot.add(item);
        }
        return this;
    }

    public PlotBuilder add(Object item, String title) {
        if (item != null) {
            itemsToPlot.add(item);
            titles.add(title);
        }
        return this;
    }

    public DoubleFormat xformat() {
        return xformat;
    }

    public PlotBuilder xformat(String format) {
        return xformat(PlotConfigManager.Config.dblformat(format));
    }

    public PlotBuilder xformat(DoubleFormat format) {
        this.xformat = format;
        return this;
    }

    public DoubleFormat zformat() {
        return xformat;
    }

    public PlotBuilder zformat(String format) {
        return zformat(PlotConfigManager.Config.dblformat(format));
    }

    public PlotBuilder zformat(DoubleFormat format) {
        this.zformat = format;
        return this;
    }

    //    public PlotBuilder xsamples(Object[] format) {
//        this.samples=null;
//        this.xformat = new ArrayDoubleFormat(format);
//        return this;
//    }
//
//    public PlotBuilder xsamples(List format) {
//        this.samples=null;
//        this.xformat = new ListDoubleFormat(format);
//        return this;
//    }
    public DoubleFormat yformat() {
        return xformat;
    }

    public PlotBuilder yformat(DoubleFormat format) {
        this.yformat = format;
        return this;
    }

    public PlotBuilder yformat(String format) {
        return yformat(PlotConfigManager.Config.dblformat(format));
    }

    public PlotBuilder setLibrary(String library) {
        this.library = library;
        return this;
    }

    public PlotBuilder domain(PlotDomain domain) {
        this.domain = domain;
        return this;
    }

    public PlotBuilder windowManager(PlotWindowManager windowManager) {
        this.windowManager = windowManager;
        return this;
    }

    public PlotBuilder samples(Samples samples) {
        xsamples(-1).ysamples(-1).zsamples(-1);
        this.samples = samples;
        return this;
    }

    public PlotBuilder samples(int xsamples, int ysamples, int zsamples) {
        return xsamples(xsamples).ysamples(ysamples).zsamples(zsamples);
    }

    public PlotBuilder samples(int xsamples, int ysamples) {
        return xsamples(xsamples).ysamples(ysamples);
    }

    //    public PlotBuilder xsamples(ToDoubleArrayAware xvalue) {
//        return xsamples(xvalue.toDoubleArray());
//    }
//
//    public PlotBuilder ysamples(ToDoubleArrayAware yvalue) {
//        return ysamples(yvalue.toDoubleArray());
//    }
//
//    public PlotBuilder zsamples(ToDoubleArrayAware yvalue) {
//        return zsamples(yvalue.toDoubleArray());
//    }
    public PlotBuilder xsamples(Object[] xvalue) {
        return xsamples(Arrays.asList(xvalue));
    }

    public PlotBuilder xsamples(List xvalue) {
        if (xvalue.size() == 0) {
            return xsamples(new double[0]);
        }
        Class componentType = xvalue.get(0).getClass();
        if (componentType.equals(Double.TYPE)
                || componentType.equals(Double.class)
                || componentType.equals(Float.TYPE)
                || componentType.equals(Float.class)
                || componentType.equals(Integer.TYPE)
                || componentType.equals(Integer.class)
                || componentType.equals(Short.TYPE)
                || componentType.equals(Short.class)
                || componentType.equals(Long.TYPE)
                || componentType.equals(Long.class)) {
            DoubleArrayList to = new DoubleArrayList(xvalue.size());
            for (Object o : xvalue) {
                to.add(((Number) o).doubleValue());
            }
            return xsamples(to.toDoubleArray());
        } else {
            xsamples = -1;
            samples = null;
            xformat(new ListDoubleFormat(xvalue));
            return this;
        }
    }

    public PlotBuilder xsamples(Object xvalue) {
        for (PlotBuilderSupport support : supports) {
            if (support.xsamples(xvalue, this)) {
                return this;
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public PlotBuilder ysamples(Object xvalue) {
        for (PlotBuilderSupport support : supports) {
            if (support.ysamples(xvalue, this)) {
                return this;
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public PlotBuilder zsamples(Object xvalue) {
        for (PlotBuilderSupport support : supports) {
            if (support.zsamples(xvalue, this)) {
                return this;
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public PlotBuilder xsamples(double[] xvalue) {
        if (samples instanceof AbsoluteSamples) {
            AbsoluteSamples a = (AbsoluteSamples) samples;
            switch (samples.getDimension()) {
                case 1: {
                    samples = Samples.absolute(xvalue);
                    break;
                }
                case 2: {
                    samples = Samples.absolute(xvalue, a.getY());
                    break;
                }
                case 3: {
                    samples = Samples.absolute(xvalue, a.getY(), a.getZ());
                    break;
                }
            }
        } else {
            samples = Samples.absolute(xvalue);
        }
        return this;
    }

    public PlotBuilder ysamples(double[] yvalue) {
        if (samples instanceof AbsoluteSamples) {
            AbsoluteSamples a = (AbsoluteSamples) samples;
            switch (samples.getDimension()) {
                case 1: {
                    samples = Samples.absolute(a.getX(), yvalue);
                    break;
                }
                case 2: {
                    samples = Samples.absolute(a.getX(), yvalue);
                    break;
                }
                case 3: {
                    samples = Samples.absolute(a.getX(), yvalue, a.getZ());
                    break;
                }
            }
        } else {
            samples = Samples.absolute(yvalue, yvalue);
        }
        return this;
    }

    public PlotBuilder zsamples(double[] zvalue) {
        if (samples instanceof AbsoluteSamples) {
            AbsoluteSamples a = (AbsoluteSamples) samples;
            switch (samples.getDimension()) {
                case 1: {
                    samples = Samples.absolute(a.getX(), a.getY(), zvalue);
                    break;
                }
                case 2: {
                    samples = Samples.absolute(a.getX(), a.getY(), zvalue);
                    break;
                }
                case 3: {
                    samples = Samples.absolute(a.getX(), a.getY(), zvalue);
                    break;
                }
            }
        } else {
            samples = Samples.absolute(zvalue, zvalue, zvalue);
        }
        return this;
    }

    public PlotBuilder xsamples(int value) {
        this.xsamples = value;
        this.samples = null;
        return this;
    }

    public PlotBuilder ysamples(int value) {
        this.ysamples = value;
        this.samples = null;
        return this;
    }

    public PlotBuilder zsamples(int value) {
        this.zsamples = value;
        this.samples = null;
        return this;
    }

    public PlotBuilder xname(String xname) {
        this.xname = xname;
        return this;
    }

    public PlotBuilder yname(String yname) {
        this.yname = yname;
        return this;
    }

    public PlotBuilder title(String title) {
        this.title = title;
        return this;
    }

    public PlotBuilder name(String name) {
        this.name = name;
        return this;
    }

    public PlotBuilder titles(String... titles) {
        this.titles = new ArrayList<>(Arrays.asList(titles));
        return this;
    }

    public PlotBuilder titles(Iterable<String> titles) {
        this.titles = new ArrayList<>();
        for (String s : titles) {
            this.titles.add(s);
        }
        return this;
    }

    public PlotBuilder titles(List<String> titles) {
        this.titles = new ArrayList<>(titles);
        return this;
    }

    public PlotBuilder display() {
        return display(true);
    }

    public PlotBuilder nodisplay() {
        return display(false);
    }

    public PlotBuilder display(boolean display) {
        this.display = display;
        return this;
    }

    public PlotBuilder converter(PlotDoubleConverter converter) {
        this.converter = converter;
        return this;
    }

    public PlotContainer parent() {
        return parent;
    }

    public PlotBuilder display(PlotContainer parent) {
        this.parent = parent;
        return this;
    }

    public PlotBuilder noupdate() {
        this.updateName = null;
        this.update = null;
        return this;
    }

    public PlotBuilder update() {
        return update(path + "/" + name);
    }

    public PlotBuilder update(String updateName) {
        this.updateName = updateName;
        this.update = null;
        return this;
    }

    public PlotBuilder update(PlotComponent component) {
        this.update = component;
        this.updateName = null;
        return this;
    }

    public PlotBuilder asHeatMap() {
        return plotType(PlotType.HEATMAP);
    }

    public PlotBuilder asMatrix() {
        return plotType(PlotType.MATRIX);
    }

    public PlotBuilder asMesh() {
        return plotType(PlotType.MESH);
    }

    public PlotBuilder asTable() {
        return plotType(PlotType.TABLE);
    }

    public PlotBuilder asCurve() {
        return plotType(PlotType.CURVE);
    }

    public PlotBuilder asBar() {
        return plotType(PlotType.BAR);
    }

    public PlotBuilder asRing() {
        return plotType(PlotType.RING);
    }

    public PlotBuilder asBubble() {
        return plotType(PlotType.BUBBLE);
    }

    public PlotBuilder asArea() {
        return plotType(PlotType.AREA);
    }

    public PlotBuilder asPie() {
        return plotType(PlotType.PIE);
    }

    public PlotBuilder asPolar() {
        return plotType(PlotType.POLAR);
    }

    public PlotBuilder asField() {
        return plotType(PlotType.FIELD);
    }

    public PlotBuilder polarClockwise(boolean value) {
        return param("polarClockwise", value);
    }

    public PlotBuilder polarAngleOffset(double value) {
        return param("polarAngleOffset", value);
    }

    public PlotBuilder param(String name, Object value) {
        properties.put(name, value);
        return this;
    }

    public PlotBuilder asAbs() {
        return converter(PlotDoubleConverter.ABS);
    }

    public PlotBuilder asImag() {
        return converter(PlotDoubleConverter.of("IMG"));
    }

    public PlotBuilder asReal() {
        return converter(PlotDoubleConverter.REAL);
    }

    public PlotBuilder asDB() {
        return converter(PlotDoubleConverter.of("DB"));
    }

    public PlotBuilder asDB2() {
        return converter(PlotDoubleConverter.of("DB2"));
    }

    public PlotBuilder asArg() {
        return converter(PlotDoubleConverter.of("ARG"));
    }

    public PlotBuilder asComplex() {
//        return converter(PlotDoubleConverter.COMPLEX);
        return converter(null);
    }

    public PlotBuilder plotType(PlotType plotType) {
        this.plotType = plotType;
        return this;
    }

    //    public PlotComponent plot(Matrix matrix) {
//        Complex[][] z = matrix.getArray();
//        return plot(null, null, z, PlotType.MATRIX);
//    }
//
//    public PlotComponent plot(Vector vector) {
//        return plot(vector.toArray());
//    }
//
//    public PlotComponent plot(Vector... vector) {
//        Complex[][] all = new Complex[vector.length][];
//        for (int i = 0; i < vector.length; i++) {
//            all[i] = vector[i].toArray();
//        }
//        return plot(all);
//    }
    private PlotModel _plotArr(Object[] any) {
        if (any == null) {
            return createPlotModel(null);
        }
        if (any.length == 1) {
            return createPlotModel(any[0]);
        }
        return createPlotModel(any);
    }


    public PlotModel createPlotModel(Object any) {
        if (any instanceof JComponent) {
            SwingComponentPlotModel s = new SwingComponentPlotModel((JComponent) any);
            s.setTitle(getTitle());
            return s;
        }
        if (any instanceof PlotModel) {
            PlotModel mm = (PlotModel) any;
            postConfigure(mm);
            return mm;
        }
        List<PlotValue> plottableData = PlotConfigManager.createPlotValues(any,this);
        for (PlotValue plotData : plottableData) {
            for (PlotModelFactory plotFactory : PlotConfigManager.getPlotModelFactories()) {
                PlotModel m = plotFactory.createModel(plotData, this);
                if (m != null) {
                    return m;
                }
            }
        }
        throw new IllegalArgumentException("Unsupported " + any);
    }


    private PlotComponent createPlotComponent(PlotModel model) {
        PlotWindowManager windowManager = getWindowManager();
        if (update != null) {
            if (update instanceof PlotPanel) {
                PlotPanel updatePanel = (PlotPanel) update;
                if (updatePanel.accept(model)) {
                    updatePanel.setModel(model);
                    fireOnPlot(updatePanel);
                    return update;
                } else {
                    if (parent != null) {
                        parent.remove(update);
                        PlotPanel c = Plot.create(model, windowManager);
                        parent.add(c);
                        fireOnPlot(c);
                        return c;
                    } else {
                        windowManager.remove(update);
                        PlotPanel c = Plot.create(model, windowManager);
                        windowManager.add(c, cwd());
                        fireOnPlot(c);
                        return c;
                    }
                }
            } else {
                windowManager.remove(update);
                PlotPanel c = Plot.create(model, windowManager);
                windowManager.add(c, cwd());
                fireOnPlot(c);
                return c;
            }
        } else if (updateName != null) {
            PlotComponent u = Plot.getCachedPlotComponent(updateName);
            if (u instanceof PlotPanel) {
                PlotPanel updatePanel = (PlotPanel) u;
                if (updatePanel.accept(model)) {
                    updatePanel.setModel(model);
                    //Plot.setCachedPlotComponent(updateName,u);
                    fireOnPlot(updatePanel);
                    return u;
                } else {
                    if (parent != null) {
                        parent.remove(u);
                        PlotPanel c = Plot.create(model, windowManager);
                        parent.add(c);
                        Plot.setCachedPlotComponent(updateName, c);
                        fireOnPlot(c);
                        return c;
                    } else {
                        windowManager.remove(u);
                        PlotPanel c = Plot.create(model, windowManager);
                        windowManager.add(c, cwd());
                        Plot.setCachedPlotComponent(updateName, c);
                        fireOnPlot(c);
                        return c;
                    }
                }
            } else {
                windowManager.remove(u);
                PlotPanel c = Plot.create(model, windowManager);
                windowManager.add(c, cwd());
                Plot.setCachedPlotComponent(updateName, c);
                fireOnPlot(c);
                return c;
            }
        } else {
            PlotComponent c = Plot.create(model, windowManager);
            if (parent != null) {
                parent.add(c);
            } else if (display) {
                windowManager.add(c, cwd());
            }
            fireOnPlot(c);
            return c;
        }
    }

    private void fireOnPlot(PlotComponent c) {
        for (PlotBuilderListener listener : listeners) {
            listener.onPlot(c, this);
        }
    }

    public PlotDoubleConverter getConverter(PlotDoubleConverter converter) {
        if (this.converter != null) {
            return this.converter;
        }
        return converter;
    }

    public PlotType getPlotType() {
        return plotType;
    }

    private PlotType _getPlotType(PlotType plotType) {
        if (this.plotType != null) {
            return this.plotType;
        }
        return plotType;
    }

    public PlotWindowManager getWindowManager() {
        return windowManager == null ? Plot.getDefaultWindowManager() : windowManager;
    }


    public PlotBuilder addPlotBuilderListener(PlotBuilderListener listener) {
        listeners.add(listener);
        return this;
    }

    public PlotBuilder removePlotBuilderListener(PlotBuilderListener listener) {
        listeners.remove(listener);
        return this;
    }

    public PlotBuilder plotBuilderListener(PlotBuilderListener listener) {
        return this.addPlotBuilderListener(listener);
    }

    public String cwd() {
        return path;
    }

    public PlotBuilder cd(String path) {
        if (path == null) {
            path = "/";
        }
        if (!path.startsWith("/")) {
            path = this.path + "/" + path;
        }
        StringBuilder sb = new StringBuilder();
        for (String s : StringUtils.split(path, "/")) {
            sb.append("/").append(s);
        }
        if (sb.length() == 0) {
            sb.append("/");
        }
        this.path = sb.toString();
        return this;
    }

    public boolean isConstX() {
        return constX;
    }

    public PlotBuilder setConstX(boolean constX) {
        this.constX = constX;
        return this;
    }

    public PlotBuilder constX(boolean constX) {
        this.constX = constX;
        return this;
    }

    private void postConfigure(PlotModel mm) {
        mm.setTitle(title);
        mm.setName(name);
    }

    private void postConfigureNonNull(PlotModel mm) {
        if (title != null) {
            mm.setTitle(title);
        }
        if (name != null) {
            mm.setName(name);
        }
    }

    public static class ListDoubleFormat implements DoubleFormat{

        private final List<String> values;

        public ListDoubleFormat(List values) {
            this.values = new ArrayList<>();
            for (Object o : values) {
                this.values.add(String.valueOf(o));
            }
        }

        @Override
        public String formatDouble(double value) {
            int index = (int) value;
            if (index < 0 || index >= values.size()) {
                return "";
            }
            return values.get(index);
        }

        public List<String> getValues() {
            return values;
        }
    }

    public PlotDoubleConverter getConverter() {
        return converter;
    }

    public DoubleFormat getXformat() {
        return xformat;
    }

    public DoubleFormat getYformat() {
        return yformat;
    }

    public DoubleFormat getZformat() {
        return zformat;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getTitles() {
        return titles;
    }

    public String getXname() {
        return xname;
    }

    public String getYname() {
        return yname;
    }

    public Samples getSamples() {
        return samples;
    }

    public PlotDomain getDomain() {
        return domain;
    }

    public String getLibrary() {
        return library;
    }

    public int getXsamples() {
        return xsamples;
    }

    public int getYsamples() {
        return ysamples;
    }

    public int getZsamples() {
        return zsamples;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public String getUpdateName() {
        return updateName;
    }

    public  PlotConsole console() {
        final PlotConsole plotConsole = new PlotConsole();
        try {
            SwingUtilities3.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    plotConsole.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plotConsole;
    }

    public  PlotConsole console(boolean autoSave) {
        return new PlotConsole(autoSave);
    }

    public  PlotConsole console(File folder, boolean autoSave) {
        return new PlotConsole(folder, autoSave);
    }

    public  PlotConsole console(File folder) {
        return new PlotConsole(folder);
    }

    public  PlotConsole console(String title, File folder) {
        return new PlotConsole(title, folder);
    }

}
