package net.thevpc.scholar.hadrumaths.plot;

import net.thevpc.scholar.hadrumaths.plot.model.AxisVectorPlotModel;
import net.thevpc.scholar.hadrumaths.symbolic.*;
import net.thevpc.scholar.hadruplot.extension.ClassResolvers;
import net.thevpc.scholar.hadruplot.model.PlotModel;
import net.thevpc.scholar.hadruplot.model.ValuesPlotModel;
import net.thevpc.scholar.hadruplot.model.PlotModelList;
import net.thevpc.scholar.hadrumaths.plot.model.ExpressionsPlotModel;
import net.thevpc.scholar.hadruplot.model.PlotHyperCubePlotModel;
import net.thevpc.scholar.hadruplot.model.value.PlotValueDoubleType;
import net.thevpc.scholar.hadruplot.model.value.PlotValueFileType;
import net.thevpc.scholar.hadruplot.model.value.PlotValueType;
import net.thevpc.scholar.hadruplot.model.value.PlotValueArrayType;
import net.thevpc.scholar.hadrumaths.Samples;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.geom.Point;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.CDiscrete;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.scholar.hadrumaths.util.ArrayUtils;
import net.thevpc.scholar.hadruplot.*;
import net.thevpc.scholar.hadruplot.console.PlotConfigManager;
import net.thevpc.scholar.hadruplot.util.PlotModelUtils;
import net.thevpc.scholar.hadruplot.util.PlotUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.ToDoubleFunction;

import static net.thevpc.scholar.hadruplot.Plot.update;

public class MathsPlotModelFactory implements PlotModelFactory {

    public static final PlotModelFactory INSTANCE = new MathsPlotModelFactory();

    @Override
    public PlotModel createModel(PlotValue data, PlotBuilder builder) {
        if (data.getType().getName().equals("AxisVector")) {
            return new AxisVectorPlotModel((AxisVector) data.getValue());
        }
        if (data.getType().getName().equals("PlotLines")) {
            PlotLines y = (PlotLines) data.getValue();
            builder.titles(y.titles());
            builder.xsamples(y.xsamples());
            List<List<Object>> any = y.getValues();
            Complex[][] oo = new Complex[any.size()][];
            for (int i = 0; i < oo.length; i++) {
                oo[i] = new Complex[any.get(i).size()];
                for (int j = 0; j < oo[i].length; j++) {
                    Object o = any.get(i).get(j);
                    if (o instanceof Complex) {
                        oo[i][j] = (Complex) o;
                    } else {
                        oo[i][j] = Complex.of(((Number) o).doubleValue());
                    }
                }
            }
            PlotValueType complexType = PlotConfigManager.getPlotValueTypeFactory().getType("complex");
            if (oo.length == 1) {
                return PlotModelUtils._plotComplexArray1(oo[0], PlotType.CURVE, false, builder, complexType);
            }
            return PlotModelUtils._plotComplexArray2(oo, PlotType.CURVE, false, builder, complexType);

        }
        PlotValueType type = data.getType();
        PlotValueType complexType = PlotConfigManager.getPlotValueTypeFactory().getType("complex");
        switch (type.getName()) {
            case "custom-function": {
                return _plotCustomFunctionArray(new CustomFunction[]{(CustomFunction) type.toValue(data.getValue(), CustomFunction.class)}, builder);
            }
            case "complex[][][]": {
                PlotValueArrayType oo = (PlotValueArrayType) type;
                return PlotModelUtils._plotComplexArray3(
                        (Complex[][][]) oo.toArray(data.getValue(), Complex[][][].class), null, builder);
            }
            case "number[][][]": {
                PlotValueArrayType oo = (PlotValueArrayType) type;
                return PlotModelUtils._plotComplexArray3((Complex[][][]) oo.toArray(data.getValue(), Complex[][][].class), null, builder);
            }
            case "complex[][]": {
                boolean matrix = "true".equals(data.get("matrix"));
                PlotValueArrayType oo = (PlotValueArrayType) type;
                return PlotModelUtils._plotComplexArray2(
                        (Complex[][]) oo.toArray(data.getValue(), Complex[][].class), matrix ? PlotType.MATRIX : PlotType.HEATMAP, false, builder, complexType);
            }
            case "number[][]": {
                PlotValueArrayType oo = (PlotValueArrayType) type;
                boolean matrix = "true".equals(data.get("matrix"));
                Complex[][] z = (Complex[][]) oo.toArray(data.getValue(), Complex[][].class);
                boolean real = true;
                for (int i = 0; i < z.length; i++) {
                    for (int j = 0; j < z[i].length; j++) {
                        if (!z[i][j].isReal()) {
                            real = false;
                            break;
                        }
                    }
                }
                if (real) {
                    builder.asReal();
                }
                return PlotModelUtils._plotComplexArray2(z, matrix ? PlotType.MATRIX : PlotType.HEATMAP, false, builder, complexType);
            }
            case "expr[]": {
                PlotValueArrayType oo = (PlotValueArrayType) type;
                return _plotExprArray((Expr[]) oo.toArray(data.getValue(), Expr[].class), builder);
            }
            case "complex[]": {
                PlotValueArrayType oo = (PlotValueArrayType) type;
                Complex[] y = (Complex[]) oo.toArray(data.getValue(), Complex[].class);
                boolean col = "true".equals(data.get("column"));
                return PlotModelUtils._plotComplexArray1(y, PlotType.CURVE, col, builder, complexType);
            }
            case "number[]": {
                builder.asReal();
                PlotValueArrayType oo = (PlotValueArrayType) type;
                Complex[] y = (Complex[]) oo.toArray(data.getValue(), Complex[].class);
                boolean col = "true".equals(data.get("column"));
                return PlotModelUtils._plotComplexArray1(y, col ? PlotType.MATRIX : PlotType.CURVE, col, builder, complexType);
            }
            case "point":
                return _plotPoints(new Point[][]{{(Point) type.toValue(data.getValue(), Point.class)}}, builder);
            case "point[][]": {
                PlotValueArrayType oo = (PlotValueArrayType) type;
                return _plotPoints((Point[][]) oo.toArray(type.toValue(data.getValue(), Point[][].class), Point[][].class), builder);
            }
            case "point[]": {
                PlotValueArrayType oo = (PlotValueArrayType) type;
                return _plotPoints(new Point[][]{(Point[]) oo.toArray(type.toValue(data.getValue(), Point[].class), Point[].class)}, builder);
            }
            case "expr":
                return _plotExprArray(new Expr[]{(Expr) type.toValue(data.getValue(), Expr.class)}, builder);
            case "complex":
                return PlotModelUtils._plotComplexArray2((new Complex[][]{{(Complex) type.toValue(data.getValue(), Complex.class)}}), PlotType.CURVE, true, builder, complexType);
            case "number": {
                builder.asReal();
                return PlotModelUtils._plotComplexArray2((new Complex[][]{{(Complex) type.toValue(data.getValue(), Complex.class)}}), PlotType.CURVE, true, builder, complexType);
            }
            case "expr[][]":
                throw new IllegalArgumentException("Not Supported Plot " + type.getName());
            case "null[][]":
                return PlotModelUtils._plotComplexArray2(new Complex[0][], builder.getPlotType(), false, builder, complexType);
//                throw new IllegalArgumentException("Nothing to plot ... (" + s + ")");
            case "null[]": {
                return PlotModelUtils._plotComplexArray2(new Complex[0][], builder.getPlotType(), false, builder, complexType);
//                throw new IllegalArgumentException("Nothing to plot ... (" + s + ")");
            }
            case "file": {
                PlotModel plotModel = Plot.loadPlotModel(((PlotValueFileType) type).toFile(data.getValue()));
                if (plotModel instanceof ValuesPlotModel) {
                    ValuesPlotModel v = (ValuesPlotModel) plotModel;
                    if (builder.getXformat() != null) {
                        v.setXformat(builder.getXformat());
                    }
                    if (builder.getYformat() != null) {
                        v.setYformat(builder.getYformat());
                    }
                    if (builder.getXname() != null) {
                        v.setxTitle(builder.getXname());
                    }
                    if (builder.getYname() != null) {
                        v.setyTitle(builder.getYname());
                    }
                    if (builder.getConverter() != null) {
                        v.setConverter(builder.getConverter());
                    }
                    if (builder.getTitles() != null && builder.getTitles().size() > 0) {
                        v.setYtitles(builder.getTitles().toArray(new String[0]));
                    }
                }
                PlotModelUtils.postConfigureNonNull(plotModel, builder);
                return plotModel;
            }
            case "object":
            case "null": {
                //check if this can be operated recursively
                //typeAndValue = PlotTypesHelper.resolveType(o);
                Object o = data.getValue();
                if (!PlotUtils.isComponentType(o)) {
                    throw new IllegalArgumentException("Unsupported plot type " + (o == null ? "null" : o.getClass().getName()));
                }
//                if (s.equals("object") || s.equals("null")) {
                //TODO should i return a container instead?
                PlotModelList list = new PlotModelList(builder.getTitle());
                for (Object a : PlotTypesHelper.toObjectArray(o)) {
                    list.add(builder.createModel(a, builder));
                }
                PlotModelUtils.postConfigure(list, builder);

                double[] y = {((PlotValueDoubleType) data).toDouble(data.getValue())};
                builder.asReal();
                return PlotModelUtils._plotComplexArray2((ArrayUtils.toComplex(new double[][]{y})), PlotType.CURVE, false, builder, complexType);
            }
            default:
                throw new IllegalArgumentException("Not Supported Plot " + type.getName());
        }
    }

    //    private PlotType _getPlotType(PlotType plotType, PlotBuilder builder) {
//        if (builder.getPlotType() != null) {
//            return builder.getPlotType();
//        }
//        return plotType;
//    }
    private PlotModel _plotPoints(Point[][] xy, PlotBuilder builder) {
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
//        if(true){_getPlotType
//            throw new IllegalArgumentException("Unsupported yet");
//        }
        builder.samples(Samples.absolute(x[0]));
        return builder.createModel(y, builder);
    }

    private PlotType _getPlotType(PlotType plotType, PlotBuilder builder) {
        if (builder.getPlotType() != null) {
            return builder.getPlotType();
        }
        return plotType;
    }

    private Vector toTVectorD(Object any) {
        if (any instanceof double[]) {
            return ArrayDoubleVector.row((double[]) any);
        } else if (any instanceof int[]) {
            return new ArrayIntVector(true, (int[]) any);
        } else if (any instanceof double[][]) {
            double[][] d = (double[][]) any;
            double[] d2 = new double[d.length];
            for (int i = 0; i < d.length; i++) {
                if (d[i].length != 1) {
                    return null;
                }
                d2[i] = d[i][0];
            }
            return ArrayDoubleVector.column(d2);
        } else if (any instanceof int[][]) {
            int[][] d = (int[][]) any;
            int[] d2 = new int[d.length];
            for (int i = 0; i < d.length; i++) {
                if (d[i].length != 1) {
                    return null;
                }
                d2[i] = d[i][0];
            }
            return new ArrayIntVector(false, d2);
        }
        return null;
    }

    private PlotModel _plotExprArray(Expr[] expressions, PlotBuilder builder) {
        if (expressions.length == 1 && builder.getSamples() instanceof AdaptivePlotSamples) {
            AdaptivePlotSamples adaptiveSamples = (AdaptivePlotSamples) builder.getSamples();
            String updateName0 = builder.getUpdateName();
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
                            builder.samples(Samples.absolute(event.getSamples().x.toDoubleArray()));
                            ref[0] = _plotExprArray(expressions, builder);
                        }
                    }));
            if (builder.getUpdateName() == null) {
                Plot.setCachedPlotComponent(updateName0, null);
            }
            return (PlotModel) ref[0];
        } else {
            return _plotExprArray0(expressions, builder);
        }
    }

    private PlotModel _plotCustomFunctionArray(CustomFunction[] functions, PlotBuilder builder) {
        if (functions.length == 1 && builder.getSamples() instanceof AdaptivePlotSamples) {
            AdaptivePlotSamples adaptiveSamples = (AdaptivePlotSamples) builder.getSamples();
            String updateName0 = builder.getUpdateName();
            if (updateName0 == null) {
                updateName0 = UUID.randomUUID().toString();
                update(updateName0);
            }
            Object[] ref = new Object[1];
            AdaptiveFunction1<Double> af = new AdaptiveFunction1<Double>() {
                @Override
                public Double eval(double x) {
                    return ((FunctionDDX) functions[0]).eval(x);
                }
            };
            DistanceStrategy<Double> ds = Maths.DISTANCE_DOUBLE;
            Maths.adaptiveEval(
                    af,
                    ds, (DomainX) Domain.ofBounds(0.0, 1.0)
                    , new AdaptiveConfig()
                            .setError(adaptiveSamples.getError())
                            .setMinimumXSamples(adaptiveSamples.getMinimumXSamples())
                            .setMaximumXSamples(adaptiveSamples.getMaximumXSamples())
                            .setListener(new SamplifyListener() {
                                @Override
                                public void onNewElements(AdaptiveEvent event) {
                                    builder.samples(Samples.absolute(event.getSamples().x.toDoubleArray()));
                                    ref[0] = _plotCustomFunctionArray(functions, builder);
                                }
                            }));
            if (builder.getUpdateName() == null) {
                Plot.setCachedPlotComponent(updateName0, null);
            }
            return (PlotModel) ref[0];
        } else {
            return _plotCustomFunctionArray0(functions, builder);
        }
    }

    private PlotModel _plotExprArray0(Expr[] expressions, PlotBuilder builder) {
        List<PlotHyperCube> discretes = new ArrayList<PlotHyperCube>();
        List<Expr> other = new ArrayList<Expr>();
        ClassResolvers<PlotHyperCube> plotHyperCubeResolvers = PlotConfigManager.getPlotHyperCubeResolvers();
        for (Expr expression : expressions) {
            if (expression instanceof VDiscrete) {
                discretes.add(plotHyperCubeResolvers.resolve((VDiscrete) expression));
            } else if (expression instanceof CDiscrete) {
                discretes.add(
                        plotHyperCubeResolvers.resolve(
                                new VDiscrete((CDiscrete) expression)
                        )
                );
            } else if (expression.getDomain().getDimension() == 3) {
                if (expression.is(ExprDim.SCALAR)) {
                    if (builder.getSamples() != null) {
                        PlotDomain d = builder.getDomain();
                        if (d == null) {
                            d = PlotConfigManager.getPlotDomainResolvers().resolve(expression.getDomain());
                        }
                        PlotSamples samples = builder.getSamples();
                        AbsolutePlotSamples a = d.toAbsolute(samples);
                        int xs = a.getX().length;
                        int ys = a.getY().length;
                        int zs = a.getZ().length;
                        CDiscrete discretized = (CDiscrete) Maths.discrete(expression,
                                (Domain) d,
                                xs, ys, zs);
                        discretes.add(plotHyperCubeResolvers.resolve(new VDiscrete(discretized)));
                    } else {
                        PlotDomain d = builder.getDomain();
                        if (d == null) {
                            d = PlotConfigManager.getPlotDomainResolvers().resolve(expression.getDomain());
                        }
                        int xs = builder.getXsamples() <= 0 ? 10 : builder.getXsamples();
                        int ys = builder.getYsamples() <= 0 ? 10 : builder.getYsamples();
                        int zs = builder.getZsamples() <= 0 ? 10 : builder.getZsamples();
                        CDiscrete discretized = (CDiscrete) Maths.discrete(expression, (Domain) d, xs, ys, zs);
                        discretes.add(plotHyperCubeResolvers.resolve(new VDiscrete(discretized)));
                    }
                } else {
                    PlotDomain d = builder.getDomain();
                    if (d == null) {
                        d = PlotConfigManager.getPlotDomainResolvers().resolve(expression.getDomain());
                    }
                    if (builder.getSamples() != null) {
                        PlotSamples samples = builder.getSamples();
                        AbsolutePlotSamples a = d.toAbsolute(samples);
                        int xs = a.getX().length;
                        int ys = a.getY().length;
                        int zs = a.getZ().length;
                        Expr discretized = Maths.discrete(expression, (Domain) d, xs, ys, zs);
                        discretes.add(plotHyperCubeResolvers.resolve((VDiscrete) discretized));
                    } else {
                        int xs = builder.getXsamples() <= 0 ? 10 : builder.getXsamples();
                        int ys = builder.getYsamples() <= 0 ? 10 : builder.getYsamples();
                        int zs = builder.getZsamples() <= 0 ? 10 : builder.getZsamples();
                        Expr discretized = Maths.discrete(expression, (Domain) d, xs, ys, zs);
                        discretes.add(plotHyperCubeResolvers.resolve((VDiscrete) discretized));
                    }
                }
            } else {
                other.add(expression);
            }
        }
        if (discretes.size() == 0) {
            ToDoubleFunction<Object> c = builder.getConverter(PlotDoubleConverter.ABS);
            ExpressionsPlotModel m = (ExpressionsPlotModel) _createExpressionsPlotModel(builder)
                    .setExpressions(other.toArray(new Expr[0]))
                    .setConverter(c);
            PlotType pt = builder.getPlotType();
            if (pt == null) {
                int dd = 1;
                for (Expr expr : other) {
                    int domainDimension = expr.getDomain().getDomain().getDimension();
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
                m.setPlotType(new LibraryPlotType(pt));
            }
            return m;
        } else if (other.size() == 0) {
            PlotHyperCubePlotModel mm = (PlotHyperCubePlotModel) new PlotHyperCubePlotModel()
                    //.setLibraries(builder.getLibrary())
                    .setHyperCubes(discretes)
                    .setConverter(builder.getConverter());
            PlotModelUtils.postConfigure(mm, builder);
            return mm;
        } else {
            PlotModelList list = new PlotModelList("Expressions");
            PlotHyperCubePlotModel mm = (PlotHyperCubePlotModel) new PlotHyperCubePlotModel()
                    //.setLibraries(builder.getLibrary())
                    .setHyperCubes(discretes)
                    .setConverter(builder.getConverter());
            PlotModelUtils.postConfigure(mm, builder);
            list.add(mm);
            list.add(PlotModelUtils._plotArr(other.toArray(new Expr[0]), builder));
//            PlotContainer container = getWindowManager().add("Expressions");
//            container.add(new PlotHyperCubePlotPanel(
//                    new PlotHyperCubePlotModel()
//                            .setTitle(title)
//                            .setPreferredLibraries(preferredLibraries)
//                            .setHyperCubes(discretes),
//                    getWindowManager()));
//            container.add(_plotArr(other.toArray(new Expr[0])));
            return list;
        }
    }

    private PlotModel _plotCustomFunctionArray0(CustomFunction[] expressions, PlotBuilder builder) {
        List<PlotHyperCube> discretes = new ArrayList<PlotHyperCube>();
        List<Expr> other = new ArrayList<Expr>();
        ClassResolvers<PlotHyperCube> plotHyperCubeResolvers = PlotConfigManager.getPlotHyperCubeResolvers();
        for (CustomFunction f : expressions) {
            PlotSamples samples0 = builder.getSamples();
            PlotDomain domain = builder.getDomain();
            if (samples0 instanceof RelativePlotSamples && domain != null) {
                samples0 = domain.toAbsolute(samples0);
            }
            if (!(samples0 instanceof AbsolutePlotSamples)) {
                throw new IllegalArgumentException("Expected absolute samples");
            }
            AbsolutePlotSamples samples1 = (AbsolutePlotSamples) samples0;
            if (f instanceof FunctionDDXY) {
                FunctionDDXY ff = (FunctionDDXY) f;
                double[] x = samples1.getX();
                double[] y = samples1.getY();
                Double[][] vals = new Double[y.length][x.length];
                for (int xi = 0; xi < x.length; xi++) {
                    for (int yj = 0; yj < y.length; yj++) {
                        vals[yj][xi] = ff.eval(x[xi], y[yj]);
                    }
                }
                //PlotValueType complexType = PlotConfigManager.getPlotValueTypeFactory().getType("complex");
                PlotValueType doubleType = PlotConfigManager.getPlotValueTypeFactory().getType("number");
                return PlotModelUtils._plotComplexArray2(vals, PlotType.MATRIX, false, builder, doubleType);
            } else if (f instanceof FunctionDCXY) {
                FunctionDCXY ff = (FunctionDCXY) f;
                double[] x = samples1.getX();
                double[] y = samples1.getY();
                Complex[][] vals = new Complex[y.length][x.length];
                for (int xi = 0; xi < x.length; xi++) {
                    for (int yj = 0; yj < y.length; yj++) {
                        vals[yj][xi] = ff.eval(x[xi], y[yj]);
                    }
                }
                PlotValueType complexType = PlotConfigManager.getPlotValueTypeFactory().getType("complex");
                return PlotModelUtils._plotComplexArray2(vals, PlotType.MATRIX, false, builder, complexType);
            } else if (f instanceof FunctionDDX) {
                FunctionDDX ff = (FunctionDDX) f;
                double[] x = samples1.getX();
                Double[] vals = new Double[x.length];
                for (int xi = 0; xi < x.length; xi++) {
                    vals[xi] = ff.eval(x[xi]);
                }
                //PlotValueType complexType = PlotConfigManager.getPlotValueTypeFactory().getType("complex");
                PlotValueType doubleType = PlotConfigManager.getPlotValueTypeFactory().getType("number");
                return PlotModelUtils._plotComplexArray1(vals, PlotType.MATRIX, true, builder, doubleType);
            } else if (f instanceof FunctionDCX) {
                FunctionDCX ff = (FunctionDCX) f;
                double[] x = samples1.getX();
                Complex[] vals = new Complex[x.length];
                for (int xi = 0; xi < x.length; xi++) {
                    vals[xi] = ff.eval(x[xi]);
                }
                PlotValueType complexType = PlotConfigManager.getPlotValueTypeFactory().getType("complex");
                return PlotModelUtils._plotComplexArray1(vals, PlotType.MATRIX, false, builder, complexType);
            }
        }
        throw new IllegalArgumentException("unsupported");
    }

    private ExpressionsPlotModel _createExpressionsPlotModel(PlotBuilder builder) {
        ExpressionsPlotModel mm = (ExpressionsPlotModel) new ExpressionsPlotModel().setDomain(
                        PlotConfigManager.getPlotDomainResolvers().resolve(builder.getDomain())
                )
                .setSamples(builder.getSamples()).setXprec(builder.getXsamples()).setYprec(builder.getYsamples())
                .setConstX(builder.isConstX())
                //.setLibraries(builder.getLibrary())
                .setProperties(builder.getProperties())
                .setPlotType(new LibraryPlotType(_getPlotType(PlotType.CURVE, builder)))
                .setConverter(builder.getConverter(PlotDoubleConverter.ABS));
        PlotModelUtils.postConfigure(mm, builder);
        return mm;
    }

}
