package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.geom.Point;
import net.vpc.scholar.hadrumaths.symbolic.Discrete;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.util.*;

/**
 * Created by vpc on 5/6/14.
 */
public class PlotBuilder {
    private Domain domain;
    private PlotContainer parent;
    private PlotComponent update;
    private String updateName;
    private String title;
    private String[] titles;
    private boolean display = true;
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
    private DoubleFormatter xformat;
    private DoubleFormatter yformat;
    private Samples samples;
    private List<Object> itemsToPlot = new ArrayList<>();
    private List<PlotBuilderListener> listeners = new ArrayList<>();

    public static ExpressionsPlotPanel.ShowType toShowType(PlotType pt) {
        switch (pt) {
            case AUTO: {
                return ExpressionsPlotPanel.ShowType.CURVE_FX;
            }
            case CURVE: {
                return ExpressionsPlotPanel.ShowType.CURVE_FX;
            }
            case POLAR: {
                return ExpressionsPlotPanel.ShowType.POLAR;
            }
            case MESH: {
                return ExpressionsPlotPanel.ShowType.MESH;
            }
            case HEATMAP: {
                return ExpressionsPlotPanel.ShowType.SURFACE_2D;
            }
            case MATRIX: {
                return ExpressionsPlotPanel.ShowType.MATRIX;
            }
            case TABLE: {
                return ExpressionsPlotPanel.ShowType.TABLE;
            }
        }
        return ExpressionsPlotPanel.ShowType.CURVE_FX;
    }

    public PlotComponent plot(Object... any) {
        List<Object> all = new ArrayList<>();
        all.addAll(itemsToPlot);
        for (Object o : any) {
            if (o != null) {
                all.add(o);
            }
        }
        return _plotArr(all.toArray());
    }

    public PlotBuilder mimic(PlotBuilder other) {
        domain = other.domain;
        title = other.title;
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
        titles = other.titles == null ? null : Arrays.copyOf(other.titles, other.titles.length);
        xformat = other.xformat;
        yformat = other.yformat;
        parent = other.parent;
        itemsToPlot = new ArrayList<Object>(other.itemsToPlot);
        return this;
    }

    public PlotBuilder add(Object item) {
        if (item != null) {
            itemsToPlot.add(item);
        }
        return this;
    }

    public DoubleFormatter xformat() {
        return xformat;
    }

    public PlotBuilder xformat(String format) {
        return xformat(Maths.dblformat(format));
    }

    public PlotBuilder xformat(DoubleFormatter format) {
        this.xformat = format;
        return this;
    }

    public DoubleFormatter yformat() {
        return xformat;
    }

    public PlotBuilder yformat(DoubleFormatter format) {
        this.yformat = format;
        return this;
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

    public PlotBuilder xsamples(TList xvalue) {
        DoubleList to = (DoubleList) xvalue.to(Maths.$DOUBLE);
        return xsamples(to.toDoubleArray());
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
            AbsoluteSamples a=(AbsoluteSamples) samples;
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
            AbsoluteSamples a=(AbsoluteSamples) samples;
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
            AbsoluteSamples a=(AbsoluteSamples) samples;
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

    public PlotBuilder titles(String... titles) {
        this.titles = titles;
        return this;
    }

    public PlotBuilder titles(TList<String> titles) {
        this.titles = titles.toArray(new String[titles.size()]);
        return this;
    }

    public PlotBuilder titles(List<String> titles) {
        this.titles = titles.toArray(new String[titles.size()]);
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

    public PlotBuilder asPolar() {
        return plotType(PlotType.POLAR);
    }

    public PlotBuilder polarClockwise(boolean value) {
        return param("polarClockwise", value);
    }

    public PlotBuilder polarAngleOffset(int value) {
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

    public PlotBuilder asComplex() {
        return converter(ComplexAsDouble.COMPLEX);
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


    private PlotComponent _plotArr(Object[] any) {
        if (any == null) {
            return _plotAny(null);
        }
        if (any.length == 1) {
            return _plotAny(any[0]);
        }
        return _plotAny(any);
    }

    private PlotComponent _plotAny(Object any) {
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
                PlotComponent c = null;
                for (Object a : PlotTypesHelper.toObjectArray(o)) {
                    c = _plotAny(a);
                }
                return c;
            case "complex[][]":
                return _plotComplexArray(PlotTypesHelper.toComplexArray2(o), PlotType.HEATMAP);
            case "number[][]":
                asReal();
                return _plotComplexArray(PlotTypesHelper.toComplexArray2(o), PlotType.HEATMAP);
            case "expr[]":
                return _plotExprArray(PlotTypesHelper.toExprArray(o));
            case "complex[]": {
                Complex[] y = PlotTypesHelper.toComplexArray(o);
                return _plotComplexArray((new Complex[][]{y}), PlotType.CURVE);
            }
            case "number[]": {
                asReal();
                Complex[] y = PlotTypesHelper.toComplexArray(o);
                return _plotComplexArray((new Complex[][]{y}), PlotType.CURVE);
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
                return _plotComplexArray((ArrayUtils.toComplex(new double[][]{y})), PlotType.CURVE);
            }
            case "expr[][]":
                throw new IllegalArgumentException("Not Supported Plot " + s);
            case "null[][]":
                return _plotComplexArray(new Complex[0][], plotType);
//                throw new IllegalArgumentException("Nothing to plot ... (" + s + ")");
            case "null[]": {
                return _plotComplexArray(new Complex[0][], plotType);
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

    private PlotComponent _plotExprArray(Expr[] expressions) {
        if(expressions.length==1 && samples instanceof AdaptiveSamples){
            AdaptiveSamples adaptiveSamples=(AdaptiveSamples) samples;
            String updateName0=updateName;
            if(updateName0==null){
                updateName0=UUID.randomUUID().toString();
                update(updateName0);
            }
            Object[] ref=new Object[1];
            Maths.adaptiveEval(expressions[0],new AdaptiveConfig()
                    .setError(adaptiveSamples.getError())
                    .setMinimumXSamples(adaptiveSamples.getMinimumXSamples())
                    .setMaximumXSamples(adaptiveSamples.getMaximumXSamples())
                    .setListener(new SamplifyListener() {
                @Override
                public void onNewElements(AdaptiveEvent event) {
                    samples(Samples.absolute(event.getSamples().x.toDoubleArray()));
                    ref[0]=_plotExprArray(expressions);
                }
            }));
            if(updateName==null) {
                Plot.setCachedPlotComponent(updateName0, null);
            }
            return (PlotComponent) ref[0];
        }else{
            return _plotExprArray0(expressions);
        }
    }

    private PlotComponent _plotExprArray0(Expr[] expressions) {
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
                        Discrete discretized = (Discrete) Maths.discretize(expression, samples);
                        discretes.add(new VDiscrete(discretized));
                    } else {
                        int xs = xsamples <= 0 ? 10 : xsamples;
                        int ys = ysamples <= 0 ? 10 : ysamples;
                        int zs = zsamples <= 0 ? 10 : zsamples;
                        Discrete discretized = (Discrete) Maths.discretize(expression, xs, ys, zs);
                        discretes.add(new VDiscrete(discretized));
                    }
                } else {
                    if (samples != null) {
                        Expr discretized = Maths.discretize(expression, samples);
                        discretes.add((VDiscrete) discretized);
                    } else {
                        int xs = xsamples <= 0 ? 10 : xsamples;
                        int ys = ysamples <= 0 ? 10 : ysamples;
                        int zs = zsamples <= 0 ? 10 : zsamples;
                        Expr discretized = Maths.discretize(expression, xs, ys, zs);
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
            return _register(m);
        } else if (other.size() == 0) {
            return _register(
                    new VDiscretePlotModel()
                            .setTitle(title)
                            .setPreferredLibraries(preferredLibraries)
                            .setVdiscretes(discretes)
            );
        } else {
            PlotContainer container = getWindowManager().add("Expressions");
            container.add(new VDiscretePlotPanel(
                    new VDiscretePlotModel()
                            .setTitle(title)
                            .setPreferredLibraries(preferredLibraries)
                            .setVdiscretes(discretes),
                    getWindowManager()));
            container.add(_plotArr(other.toArray(new Expr[other.size()])));
            return container;
        }
    }

    private ExpressionsPlotModel _createExpressionsPlotModel() {
        return new ExpressionsPlotModel().setTitle(title).setDomain(domain).setSamples(samples).setXprec(xsamples).setYprec(ysamples)
                .setShowType(toShowType(_getPlotType(PlotType.CURVE)))
                .setPreferredLibraries(preferredLibraries)
                .setProperties(properties)
                .setPreferredLibraries(preferredLibraries)
                .setComplexAsDouble(getConverter(ComplexAsDouble.ABS))
                ;
    }

//    public PlotComponent plot(int xs, int ys, Expr... expressions) {
//        ComplexAsDouble c = getConverter(ComplexAsDouble.ABS);
//        ExpressionsPlotModel m = _createExpressionsPlotModel()
//                .setXprec(xs).setYprec(ys)
//                .setExpressions(expressions);
//        return _register(m);
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
//            Complex[] z = e.toDC().computeComplex(x, y, null, null);
//            return plot(y, z);
//        } else {
//            PlotContainer plotContainer = getWindowManager().add(title);
//            for (int[] rc : items) {
//                Complex[] z = m.getComponent(rc[0], rc[1]).toDC().computeComplex(x, y, null, null);
//                plotContainer.add(Plot.builder().mimic(this).nodisplay().plot(y, z));
//            }
//            return plotContainer;
//        }
//    }

//    public PlotComponent plot(double[] x, double y, Expr e) {
//        DoubleToMatrix m = e.toDM();
//        int[][] items = m.getComponentDimension().iterate();
//        if (items.length == 1) {
//            Complex[] z = e.toDC().computeComplex(x, y, null, null);
//            return plot(x, z);
//        } else {
//            PlotContainer plotContainer = getWindowManager().add(title);
//            for (int[] rc : items) {
//                Complex[] z = m.getComponent(rc[0], rc[1]).toDC().computeComplex(x, y, null, null);
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

    private PlotComponent _plotPoints(Point[][] xy) {
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
        return plot(y);
    }

//    @Deprecated
//    public PlotComponent plot(double[][] x, double[][] y) {
//        ComplexAsDouble c = getConverter(ComplexAsDouble.ABS);
//        PlotType t = _getPlotType(PlotType.CURVE);
//        return _register(_defaultPlotModelXYZ(xname, null, yname, x, null, ArrayUtils.toComplex(y), c, t));
//    }
//
//    @Deprecated
//    public PlotComponent plot(Complex[][] x, Complex[][] y) {
//        return _register(_defaultPlotModelXY(xname, yname, ArrayUtils.absdbl(x), y, getConverter(ComplexAsDouble.ABS), _getPlotType(PlotType.CURVE)));
//    }
//
//    @Deprecated
//    public PlotComponent plot(Complex[] x, Complex[] y) {
//        return _register(_defaultPlotModelXY(xname, yname, new double[][]{ArrayUtils.absdbl(x)}, new Complex[][]{y}, getConverter(ComplexAsDouble.ABS), _getPlotType(PlotType.CURVE)));
//    }
//
//    @Deprecated
//    public PlotComponent plot(double[] x, double[][] y) {
//        return _register(_defaultPlotModelXY(null, null, new double[][]{x}, ArrayUtils.toComplex(y), getConverter(ComplexAsDouble.ABS), _getPlotType(PlotType.CURVE)));
//    }
//
//    @Deprecated
//    public PlotComponent plot(double[] x, Complex[][] y) {
//        return _register(_defaultPlotModelXY(null, null, new double[][]{x}, y, getConverter(ComplexAsDouble.ABS), _getPlotType(PlotType.CURVE)));
//    }
//
//    @Deprecated
//    public PlotComponent plot(String globalTitle, String[] title, double[] x, Complex[][] y, ComplexAsDouble type) {
//        return _register(_defaultPlotModelXY(null, null, new double[][]{x}, y, type, _getPlotType(PlotType.CURVE)));
//    }

    private PlotComponent _plotComplexArray(Complex[][] z, PlotType preferredPlotType) {
        //should i do differently?
//        Complex[][] z=mm.getArray();
        AbsoluteSamples aa=null;

        if((samples instanceof AdaptiveSamples)){
            throw new IllegalArgumentException("Expected Absolute");
        }
        if(samples!=null) {
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
        if (x.length > 0 && y.length == 0 && z.length > 0) {
            PlotType t = _getPlotType(preferredPlotType != null ? preferredPlotType : PlotType.CURVE);
            return _register(_defaultPlotModelXYZ(
                    xname,
                    yname,
                    null,
                    x,
                    null,
                    z,
                    c, t));
        }
        if (x.length == 0 && y.length > 0 && z.length > 0) {
            PlotType t = _getPlotType(preferredPlotType != null ? preferredPlotType : PlotType.CURVE);
            return _register(_defaultPlotModelXYZ(
                    xname,
                    yname,
                    null,
                    y,
                    null,
                    new Complex[][]{ArrayUtils.getColumn(z, 0)},
                    c, t));
        }
        if (z.length == 0) {
            PlotType t = _getPlotType(preferredPlotType != null ? preferredPlotType : PlotType.CURVE);
            c = getConverter(ComplexAsDouble.REAL);
            z = new Complex[1][y.length];
            for (int i = 0; i < y.length; i++) {
                z[0][i] = Complex.valueOf(y[i]);
            }
            return _register(_defaultPlotModelXYZ(
                    xname,
                    yname,
                    null,
                    x,
                    null,
                    z,
                    c, t));
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

            return _register(_defaultPlotModelXYZ(
                    xname,
                    yname,
                    null,
                    x,
                    y,
                    z,
                    c, t));
        }
    }

    private PlotComponent _register(PlotModel model) {
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
                        PlotPanel c = Plot.create(model, getWindowManager());
                        parent.add(c);
                        fireOnPlot(c);
                        return c;
                    } else {
                        getWindowManager().remove(update);
                        PlotPanel c = Plot.create(model, getWindowManager());
                        c.display();
                        fireOnPlot(c);
                        return c;
                    }
                }
            } else {
                getWindowManager().remove(update);
                PlotPanel c = Plot.create(model, getWindowManager());
                c.display();
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
                        PlotPanel c = Plot.create(model, getWindowManager());
                        parent.add(c);
                        Plot.setCachedPlotComponent(updateName, c);
                        fireOnPlot(c);
                        return c;
                    } else {
                        getWindowManager().remove(u);
                        PlotPanel c = Plot.create(model, getWindowManager());
                        c.display();
                        Plot.setCachedPlotComponent(updateName, c);
                        fireOnPlot(c);
                        return c;
                    }
                }
            } else {
                getWindowManager().remove(u);
                PlotPanel c = Plot.create(model, getWindowManager());
                c.display();
                Plot.setCachedPlotComponent(updateName, c);
                fireOnPlot(c);
                return c;
            }
        } else {
            PlotComponent c = Plot.create(model, getWindowManager());
            if (parent != null) {
                parent.add(c);
            } else if (display) {
                c.display();
            }
            fireOnPlot(c);
            return c;
        }
    }

    private void fireOnPlot(PlotComponent c) {
        for (PlotBuilderListener listener : listeners) {
            listener.onPlot(c,this);
        }
    }

    public ComplexAsDouble getConverter(ComplexAsDouble converter) {
        if (this.converter != null) {
            return this.converter;
        }
        return converter;
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
        return new ValuesPlotModel(title, xtitle, ztitle, titles, x, z, zDoubleFunction, plotType, properties)
                .setEnabledLibraries(enabledExternalLibraries)
                .setPreferredLibraries(preferredLibraries)
                ;
    }

    private ValuesPlotModel _defaultPlotModelXYZ(String xtitle, String ytitle, String ztitle, double[] x, double[] y, Complex[][] z, ComplexAsDouble zDoubleFunction, PlotType plotType) {
        double[][] xx = x == null ? null : new double[][]{x};
        double[][] yy = y == null ? null : new double[][]{y};
        return _defaultPlotModelXYZ(xtitle, ytitle, ztitle, xx, yy, z, zDoubleFunction, plotType);
    }

    private ValuesPlotModel _defaultPlotModelXYZ(String xtitle, String ytitle, String ztitle, double[][] x, double[][] y, Complex[][] z, ComplexAsDouble zDoubleFunction, PlotType plotType) {
        if (x == null && samples != null) {
            if (! (samples instanceof AbsoluteSamples)) {
                throw new IllegalArgumentException("Samples should be absolute");
            }
            x = new double[][]{((AbsoluteSamples)samples).getX()};
        }
        if (y == null && samples != null) {
            if (! (samples instanceof AbsoluteSamples)) {
                throw new IllegalArgumentException("Samples should be absolute");
            }
            y = new double[][]{((AbsoluteSamples)samples).getY()};
        }
        return new ValuesPlotModel(title, xtitle, ytitle, ztitle, titles, x, y, z, zDoubleFunction, plotType, properties)
                .setEnabledLibraries(enabledExternalLibraries)
                .setPreferredLibraries(preferredLibraries)
                .setXformat(xformat)
                .setYformat(yformat)
                ;
    }

    public PlotBuilder addPlotBuilderListener(PlotBuilderListener listener){
        listeners.add(listener);
        return this;
    }

    public PlotBuilder removePlotBuilderListener(PlotBuilderListener listener){
        listeners.remove(listener);
        return this;
    }
    public PlotBuilder plotBuilderListener(PlotBuilderListener listener){
        return this.addPlotBuilderListener(listener);
    }

}
