package net.vpc.scholar.hadrumaths.plot;

import net.vpc.common.strings.StringUtils;
import net.vpc.common.util.DoubleFormat;
import net.vpc.common.util.TypeReference;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.geom.Point;
import net.vpc.scholar.hadrumaths.symbolic.Discrete;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import javax.swing.*;
import java.io.File;
import java.util.*;

/**
 * Created by vpc on 5/6/14.
 */
public class PlotBuilder {

    private Domain domain;
    private PlotContainer parent;
    private PlotComponent update;
    private String updateName;
    private String name;
    private String title;
    private List<String> titles = new ArrayList<>();
    private boolean display = true;
    private boolean constX = false;
    private PlotType plotType;
    private ComplexAsDouble converter;
    private String xname;
    private String yname;
    private int xsamples = -1;
    private int ysamples = -1;
    private int zsamples = -1;
    private Map<String, Object> properties = new HashMap<String, Object>();
    private EnumSet<ExternalLibrary> enabledExternalLibraries;
    private EnumSet<ExternalLibrary> preferredLibraries;
    private PlotWindowManager windowManager;
    private DoubleFormat xformat;
    private DoubleFormat yformat;
    private DoubleFormat zformat;
    private Samples samples;
    private String path = "/";
    private List<Object> itemsToPlot = new ArrayList<>();
    private List<Object> xsamplesToPlot = new ArrayList<>();
    private List<PlotBuilderListener> listeners = new ArrayList<>();

    public PlotComponent plotAll(Object... any) {
        return plot((Object) any);
    }

    public PlotComponent plot() {
        return plot(new Object[0]);
    }

    public PlotComponent plot(Object any) {
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
        enabledExternalLibraries = other.enabledExternalLibraries == null ? null : EnumSet.copyOf(other.enabledExternalLibraries);
        preferredLibraries = other.preferredLibraries == null ? null : EnumSet.copyOf(other.preferredLibraries);
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
        return xformat(Maths.dblformat(format));
    }

    public PlotBuilder xformat(DoubleFormat format) {
        this.xformat = format;
        return this;
    }

    public DoubleFormat zformat() {
        return xformat;
    }

    public PlotBuilder zformat(String format) {
        return zformat(Maths.dblformat(format));
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
        return yformat(Maths.dblformat(format));
    }

    public PlotBuilder setNoLibraryPreferred() {
        preferredLibraries = EnumSet.noneOf(ExternalLibrary.class);
        return this;
    }

    public PlotBuilder setPreferred(ExternalLibrary library) {
        if (library != null) {
            if (preferredLibraries == null) {
                preferredLibraries = EnumSet.noneOf(ExternalLibrary.class);
            }
            preferredLibraries.add(library);
        }
        return this;
    }

    public PlotBuilder setUnPreferred(ExternalLibrary library) {
        if (library != null) {
            if (preferredLibraries == null) {
                preferredLibraries = EnumSet.allOf(ExternalLibrary.class);
            }
            preferredLibraries.remove(library);
        }
        return this;
    }

    public PlotBuilder setEnabled(ExternalLibrary library) {
        if (library != null) {
            if (enabledExternalLibraries == null) {
                enabledExternalLibraries = EnumSet.noneOf(ExternalLibrary.class);
            }
            preferredLibraries.add(library);
        }
        return this;
    }

    public PlotBuilder setDisabled(ExternalLibrary library) {
        if (library != null) {
            if (enabledExternalLibraries == null) {
                enabledExternalLibraries = EnumSet.allOf(ExternalLibrary.class);
            }
            enabledExternalLibraries.remove(library);
            setUnPreferred(library);
        }
        return this;
    }

    public PlotBuilder domain(Domain domain) {
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
            DoubleList to = new DoubleArrayList(xvalue.size());
            for (Object o : xvalue) {
                to.append(((Number) o).doubleValue());
            }
            return xsamples(to.toDoubleArray());
        } else {
            xsamples = -1;
            samples = null;
            xformat(new ListDoubleFormat(xvalue));
            return this;
        }
    }

    public PlotBuilder xsamples(TList xvalue) {
        if (xvalue.length() == 0) {
            return xsamples(new double[0]);
        }
        TypeReference componentType = xvalue.getComponentType();
        if (componentType.getTypeClass().equals(Double.TYPE)
                || componentType.getTypeClass().equals(Double.class)) {
            DoubleList to = (DoubleList) xvalue.to(Maths.$DOUBLE);
            return xsamples(to.toDoubleArray());
        } else {
            xsamples = -1;
            samples = null;
            xformat(new ListDoubleFormat(xvalue.toJList()));
            return this;
        }
    }

    public PlotBuilder ysamples(TList xvalue) {
        DoubleList to = (DoubleList) xvalue.to(Maths.$DOUBLE);
        return ysamples(to.toDoubleArray());
    }

    public PlotBuilder zsamples(TList xvalue) {
        DoubleList to = (DoubleList) xvalue.to(Maths.$DOUBLE);
        return zsamples(to.toDoubleArray());
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

    public PlotBuilder titles(TList<String> titles) {
        this.titles = new ArrayList<>(titles.toJList());
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

    public PlotBuilder converter(ComplexAsDouble converter) {
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
        return converter(ComplexAsDouble.ABS);
    }

    public PlotBuilder asImag() {
        return converter(ComplexAsDouble.IMG);
    }

    public PlotBuilder asReal() {
        return converter(ComplexAsDouble.REAL);
    }

    public PlotBuilder asDB() {
        return converter(ComplexAsDouble.DB);
    }

    public PlotBuilder asDB2() {
        return converter(ComplexAsDouble.DB2);
    }

    public PlotBuilder asArg() {
        return converter(ComplexAsDouble.ARG);
    }

    public PlotBuilder asComplex() {
//        return converter(ComplexAsDouble.COMPLEX);
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
            return _plotAny(null);
        }
        if (any.length == 1) {
            return _plotAny(any[0]);
        }
        return _plotAny(any);
    }

    private TVector toTVectorD(Object any) {
        if (any instanceof double[]) {
            return DoubleArrayList.row((double[]) any);
        } else if (any instanceof int[]) {
            return new IntArrayList(true, (int[]) any);
        } else if (any instanceof double[][]) {
            double[][] d = (double[][]) any;
            double[] d2 = new double[d.length];
            for (int i = 0; i < d.length; i++) {
                if (d[i].length != 1) {
                    return null;
                }
                d2[i] = d[i][0];
            }
            return DoubleArrayList.column((double[]) d2);
        } else if (any instanceof int[][]) {
            int[][] d = (int[][]) any;
            int[] d2 = new int[d.length];
            for (int i = 0; i < d.length; i++) {
                if (d[i].length != 1) {
                    return null;
                }
                d2[i] = d[i][0];
            }
            return new IntArrayList(false, (int[]) d2);
        }
        return null;
    }

    private PlotModel _plotAny(Object any) {
        if (any instanceof PlotModel) {
            PlotModel mm = (PlotModel) any;
            postConfigure(mm);
            return mm;
        }
        if (any instanceof PlotLines) {
            PlotLines y = (PlotLines) any;
            titles(y.titles());
            xsamples(y.xsamples());
            any = y.getValues();
        }
        if (any instanceof JComponent) {
            return new SwingComponentPlotModel((JComponent) any);
        }
        TVector a2 = toTVectorD(any);
        if (a2 != null) {
            any = a2;
        }
        PlotTypesHelper.TypeAndValue typeAndValue = PlotTypesHelper.resolveType(any);
        String s = typeAndValue.type;
        Object o = typeAndValue.value;
        switch (s) {
            case "object":
            case "null":
                //check if this can be operated recursively
                typeAndValue = PlotTypesHelper.resolveType(o);
                o = typeAndValue.value;
                if (!PlotTypesHelper.isComponentType(o)) {
                    throw new IllegalArgumentException("Unsupported plot type " + (o == null ? "null" : o.getClass().getName()));
                }
//                if (s.equals("object") || s.equals("null")) {
                //TODO should i return a container instead?
                PlotModelList list = new PlotModelList(title);
                for (Object a : PlotTypesHelper.toObjectArray(o)) {
                    list.add(_plotAny(a));
                }
                postConfigure(list);
                return list;
            case "complex[][]": {
                boolean matrix = "true".equals(typeAndValue.props.get("matrix"));
                return _plotComplexArray2(PlotTypesHelper.toComplexArray2(o), matrix ? PlotType.MATRIX : PlotType.HEATMAP, false);
            }
            case "complex[][][]": {
                return _plotComplexArray3(PlotTypesHelper.toComplexArray3(o), null);
            }
            case "number[][][]": {
                return _plotComplexArray3(PlotTypesHelper.toComplexArray3(o), null);
            }
            case "number[][]": {
                asReal();
                boolean matrix = "true".equals(typeAndValue.props.get("matrix"));
                return _plotComplexArray2(PlotTypesHelper.toComplexArray2(o), matrix ? PlotType.MATRIX : PlotType.HEATMAP, false);
            }
            case "expr[]": {
                return _plotExprArray(PlotTypesHelper.toExprArray(o));
            }
            case "complex[]": {
                Complex[] y = PlotTypesHelper.toComplexArray(o);
                boolean col = "true".equals(typeAndValue.props.get("column"));
                return _plotComplexArray2((new Complex[][]{y}), PlotType.CURVE, col);
            }
            case "number[]": {
                asReal();
                Complex[] y = PlotTypesHelper.toComplexArray(o);
                boolean col = "true".equals(typeAndValue.props.get("column"));
                return _plotComplexArray2((new Complex[][]{y}), PlotType.CURVE, col);
            }
            case "point":
                return _plotPoints(new Point[][]{{(Point) o}});
            case "point[][]":
                return _plotPoints(PlotTypesHelper.toPointArray2(o));
            case "point[]":
                return _plotPoints(new Point[][]{PlotTypesHelper.toPointArray(o)});
            case "expr":
                return _plotExprArray(new Expr[]{(Expr) o});
            case "complex":
                return _plotAny(new Complex[]{PlotTypesHelper.toComplex(o)});
            case "number": {
                double[] y = {PlotTypesHelper.toDouble(o)};
                asReal();
                return _plotComplexArray2((ArrayUtils.toComplex(new double[][]{y})), PlotType.CURVE, false);
            }
            case "expr[][]":
                throw new IllegalArgumentException("Not Supported Plot " + s);
            case "null[][]":
                return _plotComplexArray2(new Complex[0][], plotType, false);
//                throw new IllegalArgumentException("Nothing to plot ... (" + s + ")");
            case "null[]": {
                return _plotComplexArray2(new Complex[0][], plotType, false);
//                throw new IllegalArgumentException("Nothing to plot ... (" + s + ")");
            }
            case "file": {

                PlotModel plotModel = Plot.loadPlotModel((File) o);
                if (plotModel instanceof ValuesPlotModel) {
                    ValuesPlotModel v = (ValuesPlotModel) plotModel;
                    if (xformat != null) {
                        v.setXformat(xformat);
                    }
                    if (yformat != null) {
                        v.setYformat(yformat);
                    }
                    if (xname != null) {
                        v.setxTitle(xname);
                    }
                    if (yname != null) {
                        v.setyTitle(yname);
                    }
                    if (converter != null) {
                        v.setConverter(converter);
                    }
                    if (titles != null && titles.size() > 0) {
                        v.setYtitles(titles.toArray(new String[titles.size()]));
                    }
                }
                postConfigureNonNull(plotModel);
                return plotModel;

//                throw new IllegalArgumentException("Nothing to plot ... (" + s + ")");
            }
            default:
                throw new IllegalArgumentException("Not Supported Plot " + s);
        }
    }

//    public PlotComponent plot(List list) {
//        ListMap<String, Object> all = new ListMap<String, Object>();
//        for (Object o : list) {
//            if (o instanceof Expr) {
//                all.add("Expr", o);
//            } else if (o instanceof TVector[Expr]) {
//                for (Expr expr : ((ExprList) o)) {
//                    all.add("Expr", expr);
//                }
//            } else if (o instanceof Matrix) {
//                all.add("Matrix", o);
//            } else if (o instanceof Vector) {
//                all.add("Complex[]", ((Vector) o).toArray());
//            } else if (o instanceof Complex[]) {
//                all.add("Complex[]", (Complex[]) o);
//            } else if (o instanceof double[]) {
//                all.add("Complex[]", ArrayUtils.toComplex((double[]) o));
//            } else if (o instanceof Complex[][]) {
//                all.add("Complex[][]", o);
//            } else if (o instanceof double[][]) {
//                all.add("Complex[][]", ArrayUtils.toComplex((double[][]) o));
//            } else {
//                throw new IllegalArgumentException("Unsupported");
//            }
//        }
//        if (all.size() > 0) {
//            PlotContainer container = null;
//            boolean oldDisplay = display;
//            nodisplay();
//            PlotComponent ret = null;
//            for (Map.Entry<String, List<Object>> e : all.entrySet()) {
//                String k = e.getKey();
//                if ("Expr".equals(k)) {
//                    asCurve();
//                    PlotComponent plot = plot(e.getValue().toArray(new Expr[e.getValue().size()]));
//                    if (ret == null) {
//                        ret = plot;
//                    } else {
//                        if (container == null) {
//                            container = getWindowManager().add(title);
//                            container.add(ret);
//                        }
//                        container.add(plot);
//                    }
//                } else if ("Matrix".equals(k)) {
//                    asMatrix();
//                    for (Object m : e.getValue()) {
//                        PlotComponent plot = (plot((Matrix) m));
//                        if (ret == null) {
//                            ret = plot;
//                        } else {
//                            if (container == null) {
//                                container = getWindowManager().add(title);
//                                container.add(ret);
//                            }
//                            container.add(plot);
//                        }
//                    }
//                } else if ("Complex[][]".equals(k)) {
//                    asSurface();
//                    for (Object m : e.getValue()) {
//                        PlotComponent plot = (plot((Complex[][]) m));
//                        if (ret == null) {
//                            ret = plot;
//                        } else {
//                            if (container == null) {
//                                container = getWindowManager().add(title);
//                                container.add(ret);
//                            }
//                            container.add(plot);
//                        }
//                    }
//                } else if ("Complex[]".equals(k)) {
//                    Complex[][] curves = new Complex[e.getValue().size()][];
//                    List<Object> value = e.getValue();
//                    for (int i = 0; i < value.size(); i++) {
//                        Object m = value.get(i);
//                        curves[i] = (Complex[]) m;
//                    }
//                    PlotComponent plot = (asCurve().plot(curves));
//                    if (ret == null) {
//                        ret = plot;
//                    } else {
//                        if (container == null) {
//                            container = getWindowManager().add(title);
//                            container.add(ret);
//                        }
//                        container.add(plot);
//                    }
//                }
//            }
//
//            if (container != null) {
//                if (oldDisplay) {
//                    container.display();
//                }
//                return container;
//            }
//            if (ret != null && oldDisplay) {
//                ret.display();
//            }
//            return ret;
//        }
//        return null;
//    }
//
//    public PlotComponent plot(ExprList list) {
//        if (list.isComplex()) {
//            return plot(list.toArray(new Complex[list.length()]));
//        }
//        return plot(list.toArray());
//    }
//
//    public PlotComponent plot(Expr... expressions) {
//        return _plotExprArray(expressions);
//    }
    private PlotModel _plotExprArray(Expr[] expressions) {
        if (expressions.length == 1 && samples instanceof AdaptiveSamples) {
            AdaptiveSamples adaptiveSamples = (AdaptiveSamples) samples;
            String updateName0 = updateName;
            if (updateName0 == null) {
                updateName0 = UUID.randomUUID().toString();
                update(updateName0);
            }
            Object[] ref = new Object[1];
            Maths.adaptiveEval(expressions[0], new AdaptiveConfig()
                    .setError(adaptiveSamples.getError())
                    .setMinimumXSamples(adaptiveSamples.getMinimumXSamples())
                    .setMaximumXSamples(adaptiveSamples.getMaximumXSamples())
                    .setListener(new SamplifyListener() {
                        @Override
                        public void onNewElements(AdaptiveEvent event) {
                            samples(Samples.absolute(event.getSamples().x.toDoubleArray()));
                            ref[0] = _plotExprArray(expressions);
                        }
                    }));
            if (updateName == null) {
                Plot.setCachedPlotComponent(updateName0, null);
            }
            return (PlotModel) ref[0];
        } else {
            return _plotExprArray0(expressions);
        }
    }

    private PlotModel _plotExprArray0(Expr[] expressions) {
        List<VDiscrete> discretes = new ArrayList<VDiscrete>();
        List<Expr> other = new ArrayList<Expr>();
        for (Expr expression : expressions) {
            if (expression instanceof VDiscrete) {
                discretes.add((VDiscrete) expression);
            } else if (expression instanceof Discrete) {
                discretes.add(new VDiscrete((Discrete) expression));
            } else if (expression.getDomainDimension() == 3) {
                if (expression.isScalarExpr()) {
                    if (samples != null) {
                        Discrete discretized = (Discrete) Maths.discrete(expression, samples);
                        discretes.add(new VDiscrete(discretized));
                    } else {
                        int xs = xsamples <= 0 ? 10 : xsamples;
                        int ys = ysamples <= 0 ? 10 : ysamples;
                        int zs = zsamples <= 0 ? 10 : zsamples;
                        Discrete discretized = (Discrete) Maths.discrete(expression, xs, ys, zs);
                        discretes.add(new VDiscrete(discretized));
                    }
                } else {
                    if (samples != null) {
                        Expr discretized = Maths.discrete(expression, samples);
                        discretes.add((VDiscrete) discretized);
                    } else {
                        int xs = xsamples <= 0 ? 10 : xsamples;
                        int ys = ysamples <= 0 ? 10 : ysamples;
                        int zs = zsamples <= 0 ? 10 : zsamples;
                        Expr discretized = Maths.discrete(expression, xs, ys, zs);
                        discretes.add((VDiscrete) discretized);
                    }
                }
            } else {
                other.add(expression);
            }
        }
        if (discretes.size() == 0) {
            ComplexAsDouble c = getConverter(ComplexAsDouble.ABS);
            ExpressionsPlotModel m = _createExpressionsPlotModel()
                    .setExpressions(other.toArray(new Expr[other.size()]))
                    .setComplexAsDouble(c);
            PlotType pt = getPlotType();
            if (pt == null) {
                int dd = 1;
                for (Expr expr : other) {
                    int domainDimension = expr.getDomain().getDomainDimension();
                    if (domainDimension > dd) {
                        dd = domainDimension;
                    }
                }
                switch (dd) {
                    case 1: {
                        pt = PlotType.CURVE;
                        break;
                    }
                    case 2: {
                        pt = PlotType.HEATMAP;
                        break;
                    }
                    case 3: {
                        pt = PlotType.HEATMAP;
                        break;
                    }
                    default: {
                        pt = PlotType.HEATMAP;
                        break;
                    }
                }
                m.setPlotType(pt);
            }
            return m;
        } else if (other.size() == 0) {
            VDiscretePlotModel mm = new VDiscretePlotModel()
                    .setPreferredLibraries(preferredLibraries)
                    .setVdiscretes(discretes)
                    .setConverter(converter);
            postConfigure(mm);
            return mm;
        } else {
            PlotModelList list = new PlotModelList("Expressions");
            VDiscretePlotModel mm = new VDiscretePlotModel()
                    .setPreferredLibraries(preferredLibraries)
                    .setVdiscretes(discretes)
                    .setConverter(converter);
            postConfigure(mm);
            list.add(mm);
            list.add(_plotArr(other.toArray(new Expr[other.size()])));
//            PlotContainer container = getWindowManager().add("Expressions");
//            container.add(new VDiscretePlotPanel(
//                    new VDiscretePlotModel()
//                            .setTitle(title)
//                            .setPreferredLibraries(preferredLibraries)
//                            .setVdiscretes(discretes),
//                    getWindowManager()));
//            container.add(_plotArr(other.toArray(new Expr[other.size()])));
            return list;
        }
    }

    private ExpressionsPlotModel _createExpressionsPlotModel() {
        ExpressionsPlotModel mm = new ExpressionsPlotModel().setDomain(domain).setSamples(samples).setXprec(xsamples).setYprec(ysamples)
                .setPlotType(_getPlotType(PlotType.CURVE))
                .setConstX(isConstX())
                .setPreferredLibraries(preferredLibraries)
                .setProperties(properties)
                .setPreferredLibraries(preferredLibraries)
                .setComplexAsDouble(getConverter(ComplexAsDouble.ABS));
        postConfigure(mm);
        return mm;
    }

//    public PlotComponent plot(int xs, int ys, Expr... expressions) {
//        ComplexAsDouble c = getConverter(ComplexAsDouble.ABS);
//        ExpressionsPlotModel m = _createExpressionsPlotModel()
//                .setXprec(xs).setYprec(ys)
//                .setExpressions(expressions);
//        return createPlotComponent(m);
//    }
//    @Deprecated
//    public PlotComponent plot(double[] x, double[] y) {
//        return plot(x, new double[0], new double[][]{y});
//    }
//    public PlotComponent plot(double[] x, Complex[] y) {
//        return plot(x, new double[0], new Complex[][]{y}, PlotType.HEATMAP);
//    }
//    public PlotComponent plot(double[] x, Vector y) {
//        return plot(x, new double[0], new Complex[][]{y.toArray()}, PlotType.HEATMAP);
//    }
//    public PlotComponent plot(double[] x, double[] y, double[][] z) {
//        return plot(x, y, ArrayUtils.toComplex(z), PlotType.HEATMAP);
//    }
//    public PlotComponent plot(Samples xy, double[][] z) {
//        return plot(xy.getX(), xy.getY(), z);
//    }
//    public PlotComponent plot(Samples xy, Complex[][] z) {
//        return plot(xy.getX(), xy.getY(), z, PlotType.HEATMAP);
//    }
//    public PlotComponent plot(double x, double[] y, Expr e) {
//        DoubleToMatrix m = e.toDM();//TODO
//        int[][] items = m.getComponentDimension().iterate();
//        if (items.length == 1) {
//            Complex[] z = e.toDC().computeComplexArg(x, y, null, null);
//            return plot(y, z);
//        } else {
//            PlotContainer plotContainer = getWindowManager().add(title);
//            for (int[] rc : items) {
//                Complex[] z = m.getComponent(rc[0], rc[1]).toDC().computeComplexArg(x, y, null, null);
//                plotContainer.add(Plot.builder().mimic(this).nodisplay().plot(y, z));
//            }
//            return plotContainer;
//        }
//    }
//    public PlotComponent plot(double[] x, double y, Expr e) {
//        DoubleToMatrix m = e.toDM();
//        int[][] items = m.getComponentDimension().iterate();
//        if (items.length == 1) {
//            Complex[] z = e.toDC().computeComplexArg(x, y, null, null);
//            return plot(x, z);
//        } else {
//            PlotContainer plotContainer = getWindowManager().add(title);
//            for (int[] rc : items) {
//                Complex[] z = m.getComponent(rc[0], rc[1]).toDC().computeComplexArg(x, y, null, null);
//                plotContainer.add(Plot.builder().mimic(this).nodisplay().plot(x, z));
//            }
//            return plotContainer;
//        }
//    }
//    public PlotComponent plot(Complex[][] z) {
//        return plot(null, null, z, PlotType.HEATMAP);
//    }
//    public PlotComponent plot(double[][] z) {
//        return plot(null, null, z);
//    }
//    public PlotComponent plot(double[][] z) {
//        return plot(null,null,z);
//    }
//    public PlotComponent plot(double[] y) {
//        //return plot(null,y);
//        return plot(null, new double[0], ArrayUtils.toComplex(new double[][]{y}), PlotType.CURVE);
//    }
//    public PlotComponent plot(Complex[] y) {
////        return plot((double[]) null,y);
//        return plot((double[]) null, new double[0], new Complex[][]{y}, PlotType.CURVE);
//    }
//    public PlotComponent plot(Point[]... xy) {
//        return _plotPoints(xy);
//    }
    private PlotModel _plotPoints(Point[][] xy) {
        double[][] x = new double[xy.length][];
        double[][] y = new double[xy.length][];
        for (int i = 0; i < xy.length; i++) {
            x[i] = new double[xy[i].length];
            y[i] = new double[xy[i].length];
            for (int j = 0; j < xy[i].length; j++) {
                x[i][j] = xy[i][j].x;
                y[i][j] = xy[i][j].y;
            }
        }
        //TODO
//        if(true){
//            throw new IllegalArgumentException("Unsupported yet");
//        }
        samples(Samples.absolute(x[0]));
        return _plotAny(y);
    }

//    @Deprecated
//    public PlotComponent plot(double[][] x, double[][] y) {
//        ComplexAsDouble c = getConverter(ComplexAsDouble.ABS);
//        PlotType t = _getPlotType(PlotType.CURVE);
//        return createPlotComponent(_defaultPlotModelXYZ(xname, null, yname, x, null, ArrayUtils.toComplex(y), c, t));
//    }
//
//    @Deprecated
//    public PlotComponent plot(Complex[][] x, Complex[][] y) {
//        return createPlotComponent(_defaultPlotModelXY(xname, yname, ArrayUtils.absdbl(x), y, getConverter(ComplexAsDouble.ABS), _getPlotType(PlotType.CURVE)));
//    }
//
//    @Deprecated
//    public PlotComponent plot(Complex[] x, Complex[] y) {
//        return createPlotComponent(_defaultPlotModelXY(xname, yname, new double[][]{ArrayUtils.absdbl(x)}, new Complex[][]{y}, getConverter(ComplexAsDouble.ABS), _getPlotType(PlotType.CURVE)));
//    }
//
//    @Deprecated
//    public PlotComponent plot(double[] x, double[][] y) {
//        return createPlotComponent(_defaultPlotModelXY(null, null, new double[][]{x}, ArrayUtils.toComplex(y), getConverter(ComplexAsDouble.ABS), _getPlotType(PlotType.CURVE)));
//    }
//
//    @Deprecated
//    public PlotComponent plot(double[] x, Complex[][] y) {
//        return createPlotComponent(_defaultPlotModelXY(null, null, new double[][]{x}, y, getConverter(ComplexAsDouble.ABS), _getPlotType(PlotType.CURVE)));
//    }
//
//    @Deprecated
//    public PlotComponent plot(String globalTitle, String[] title, double[] x, Complex[][] y, ComplexAsDouble type) {
//        return createPlotComponent(_defaultPlotModelXY(null, null, new double[][]{x}, y, type, _getPlotType(PlotType.CURVE)));
//    }
    private PlotModel _plotComplexArray3(Complex[][][] z, PlotType preferredPlotType) {
        VDiscretePlotModel mm = new VDiscretePlotModel()
                .setPreferredLibraries(preferredLibraries)
                .setVdiscretes(new VDiscrete(
                        Discrete.create(z)
                ));
        postConfigure(mm);
        return mm;
    }

    private PlotModel _plotDoubleArray3(double[][][] z, PlotType preferredPlotType) {
        VDiscretePlotModel mm = new VDiscretePlotModel()
                .setPreferredLibraries(preferredLibraries)
                .setVdiscretes(new VDiscrete(
                        Discrete.create(z)
                ));
        postConfigure(mm);
        return mm;
    }

    private PlotModel _plotComplexArray2(Complex[][] z, PlotType preferredPlotType, boolean preferColumn) {
        if (preferColumn) {
            if (z.length != 1) {
                preferColumn = false;
            } else {
                //TODO
                z = (Complex[][]) PlotTypesHelper.toColumn(z[0], Complex.class);
            }
        }
        //should i do differently?
//        Complex[][] z=mm.getArray();
        AbsoluteSamples aa = null;

        if ((samples instanceof AdaptiveSamples)) {
            throw new IllegalArgumentException("Expected Absolute");
        }
        if (samples != null) {
            aa = Samples.toAbsoluteSamples(samples, domain);
        }
        double[] x = (aa == null ? null : aa.getX());
        double[] y = (aa == null ? null : aa.getY());
        ComplexAsDouble c = getConverter(ComplexAsDouble.ABS);
        if (y == null) {
            y = new double[0];
        }
        if (x == null) {
            x = new double[0];
        }
        if (z == null) {
            z = new Complex[0][];
        }
        if (preferColumn && x.length > 0 && y.length == 0) {
            double[] s = x;
            x = y;
            y = s;
        }
        if (x.length > 0 && y.length == 0 && z.length > 0) {
            PlotType t = _getPlotType(preferredPlotType != null ? preferredPlotType : PlotType.CURVE);
            return (_defaultPlotModelXYZ(
                    xname,
                    yname,
                    null,
                    new double[][]{x},
                    null,
                    z,
                    c, t, preferColumn));
        }
        if (x.length == 0 && y.length > 0 && z.length > 0) {
            PlotType t = _getPlotType(preferredPlotType != null ? preferredPlotType : PlotType.CURVE);
            return (_defaultPlotModelXYZ(
                    xname,
                    yname,
                    null,
                    new double[][]{y},
                    null,
                    new Complex[][]{ArrayUtils.getColumn(z, 0)},
                    c, t, preferColumn));
        }
        if (z.length == 0) {
            PlotType t = _getPlotType(preferredPlotType != null ? preferredPlotType : PlotType.CURVE);
            c = getConverter(ComplexAsDouble.REAL);
            z = new Complex[1][y.length];
            for (int i = 0; i < y.length; i++) {
                z[0][i] = Complex.valueOf(y[i]);
            }
            return (_defaultPlotModelXYZ(
                    xname,
                    yname,
                    null,
                    new double[][]{x},
                    null,
                    z,
                    c, t, preferColumn));
        } else {
            PlotType t = _getPlotType(preferredPlotType != null ? preferredPlotType : PlotType.HEATMAP);

            if (x.length == 0) {
                PlotType plotType = _getPlotType(null);
                if (PlotType.POLAR != plotType) {
                    x = Maths.dsteps(1.0, z.length == 0 ? 0 : z[0].length, 1.0);
                }
            }
            if (y.length == 0) {
                y = Maths.dsteps(1.0, z.length, 1.0);
            }

            return (_defaultPlotModelXYZ(
                    xname,
                    yname,
                    null,
                    new double[][]{x},
                    new double[][]{y},
                    z,
                    c, t, preferColumn));
        }
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

    public ComplexAsDouble getConverter(ComplexAsDouble converter) {
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

    private ValuesPlotModel _defaultPlotModelXY(String xtitle, String ztitle, double[][] x, Complex[][] z, ComplexAsDouble zDoubleFunction, PlotType plotType) {
        return new ValuesPlotModel(title, xtitle, ztitle, titles.toArray(new String[titles.size()]), x, z, zDoubleFunction, plotType, properties)
                .setEnabledLibraries(enabledExternalLibraries)
                .setPreferredLibraries(preferredLibraries)
                .setXformat(xformat)
                .setYformat(yformat)
                .setZformat(zformat)
                .setConverter(converter);
    }

    private ValuesPlotModel _defaultPlotModelXYZ(String xtitle, String ytitle, String ztitle, double[] x, double[] y, Complex[][] z, ComplexAsDouble zDoubleFunction, PlotType plotType) {
        double[][] xx = x == null ? null : new double[][]{x};
        double[][] yy = y == null ? null : new double[][]{y};
        return _defaultPlotModelXYZ(xtitle, ytitle, ztitle, xx, yy, z, zDoubleFunction, plotType, false);
    }

    private ValuesPlotModel _defaultPlotModelXYZ(String xtitle, String ytitle, String ztitle, double[][] x, double[][] y, Complex[][] z, ComplexAsDouble zDoubleFunction, PlotType plotType, boolean swapxy) {
        if (x == null && samples != null) {
            if (!(samples instanceof AbsoluteSamples)) {
                throw new IllegalArgumentException("Samples should be absolute");
            }
            x = new double[][]{((AbsoluteSamples) samples).getX()};
        }
        if (y == null && samples != null) {
            if (!(samples instanceof AbsoluteSamples)) {
                throw new IllegalArgumentException("Samples should be absolute");
            }
            y = new double[][]{((AbsoluteSamples) samples).getY()};
        }
        ValuesPlotModel mm = new ValuesPlotModel(title, xtitle, ytitle, ztitle, titles.toArray(new String[titles.size()]), x, y, z, zDoubleFunction, plotType, properties)
                .setEnabledLibraries(enabledExternalLibraries)
                .setPreferredLibraries(preferredLibraries)
                .setXformat(swapxy ? yformat : xformat)
                .setYformat(swapxy ? xformat : yformat)
                .setZformat(zformat)
                .setConverter(converter);
        postConfigure(mm);
        return mm;
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

    private static class ArrayDoubleFormat implements DoubleFormat {

        private final Object[] format;

        public ArrayDoubleFormat(Object[] format) {
            this.format = format;
        }

        @Override
        public String formatDouble(double value) {
            int index = (int) value;
            if (index < 0 || index >= format.length) {
                return "";
            }
            return String.valueOf(format[index]);
        }
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

    private static class ListDoubleFormat implements DoubleFormat {

        private final List format;

        public ListDoubleFormat(List format) {
            this.format = format;
        }

        @Override
        public String formatDouble(double value) {
            int index = (int) value;
            if (index < 0 || index >= format.size()) {
                return "";
            }
            return String.valueOf(format.get(index));
        }
    }
}
