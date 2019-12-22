package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.geom.Point;
import net.vpc.scholar.hadrumaths.symbolic.Discrete;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;
import net.vpc.scholar.hadruplot.*;
import net.vpc.scholar.hadruplot.console.PlotConfigManager;
import net.vpc.scholar.hadruplot.util.PlotModelUtils;
import net.vpc.scholar.hadruplot.util.PlotUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.vpc.scholar.hadruplot.Plot.update;

public class MathsPlotModelFactory implements PlotModelFactory {
    public static final PlotModelFactory INSTANCE=new MathsPlotModelFactory();

    @Override
    public PlotModel createModel(PlotValue data, PlotBuilder builder) {
        PlotValueType type = data.getType();
        PlotValueType complexType = PlotConfigManager.getPlotValueTypeFactory().getType("complex");
        switch (type.getName()) {
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
                        (Complex[][]) oo.toArray(data.getValue(), Complex[][].class), matrix ? PlotType.MATRIX : PlotType.HEATMAP, false, builder,complexType);
            }
            case "number[][]": {
                PlotValueArrayType oo = (PlotValueArrayType) type;
                boolean matrix = "true".equals(data.get("matrix"));
                Complex[][] z = (Complex[][]) oo.toArray(data.getValue(), Complex[][].class);
                boolean real=true;
                for (int i = 0; i < z.length; i++) {
                    for (int j = 0; j < z[i].length; j++) {
                        if(!z[i][j].isReal()){
                            real=false;
                            break;
                        }
                    }
                }
                if(real){
                    builder.asReal();
                }
                return PlotModelUtils._plotComplexArray2(z, matrix ? PlotType.MATRIX : PlotType.HEATMAP, false, builder,complexType);
            }
            case "expr[]": {
                PlotValueArrayType oo = (PlotValueArrayType) type;
                return _plotExprArray((Expr[]) oo.toArray(data.getValue(), Expr[].class), builder);
            }
            case "complex[]": {
                PlotValueArrayType oo = (PlotValueArrayType) type;
                Complex[] y = (Complex[]) oo.toArray(data.getValue(), Complex[].class);
                boolean col = "true".equals(data.get("column"));
                return PlotModelUtils._plotComplexArray1(y, PlotType.CURVE, col, builder,complexType);
            }
            case "number[]": {
                builder.asReal();
                PlotValueArrayType oo = (PlotValueArrayType) type;
                Complex[] y = (Complex[]) oo.toArray(data.getValue(), Complex[].class);
                boolean col = "true".equals(data.get("column"));
                return PlotModelUtils._plotComplexArray1(y, PlotType.CURVE, col, builder,complexType);
            }
            case "point":
                return _plotPoints(new Point[][]{{(Point) type.toValue(data.getValue(),Point.class)}}, builder);
            case "point[][]": {
                PlotValueArrayType oo = (PlotValueArrayType) type;
                return _plotPoints((Point[][]) oo.toArray(type.toValue(data.getValue(), Point[][].class), Point[][].class), builder);
            }
            case "point[]": {
                PlotValueArrayType oo = (PlotValueArrayType) type;
                return _plotPoints(new Point[][]{(Point[]) oo.toArray(type.toValue(data.getValue(), Point[].class), Point[].class)}, builder);
            }
            case "expr":
                return _plotExprArray(new Expr[]{(Expr) type.toValue(data.getValue(),Expr.class)}, builder);
            case "complex":
                return PlotModelUtils._plotComplexArray2((new Complex[][]{{(Complex) type.toValue(data.getValue(), Complex.class)}}), PlotType.CURVE, true, builder,complexType);
            case "number": {
                builder.asReal();
                return PlotModelUtils._plotComplexArray2((new Complex[][]{{(Complex) type.toValue(data.getValue(), Complex.class)}}), PlotType.CURVE, true, builder,complexType);
            }
            case "expr[][]":
                throw new IllegalArgumentException("Not Supported Plot " + type.getName());
            case "null[][]":
                return PlotModelUtils._plotComplexArray2(new Complex[0][], builder.getPlotType(), false, builder,complexType);
//                throw new IllegalArgumentException("Nothing to plot ... (" + s + ")");
            case "null[]": {
                return PlotModelUtils._plotComplexArray2(new Complex[0][], builder.getPlotType(), false, builder,complexType);
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
                return PlotModelUtils._plotComplexArray2((ArrayUtils.toComplex(new double[][]{y})), PlotType.CURVE, false, builder,complexType);
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

    private TVector toTVectorD(Object any) {
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
            return ArrayDoubleVector.column((double[]) d2);
        } else if (any instanceof int[][]) {
            int[][] d = (int[][]) any;
            int[] d2 = new int[d.length];
            for (int i = 0; i < d.length; i++) {
                if (d[i].length != 1) {
                    return null;
                }
                d2[i] = d[i][0];
            }
            return new ArrayIntVector(false, (int[]) d2);
        }
        return null;
    }

    private PlotModel _plotExprArray(Expr[] expressions, PlotBuilder builder) {
        if (expressions.length == 1 && builder.getSamples() instanceof AdaptiveSamples) {
            AdaptiveSamples adaptiveSamples = (AdaptiveSamples) builder.getSamples();
            String updateName0 = builder.getUpdateName();
            if (updateName0 == null) {
                updateName0 = UUID.randomUUID().toString();
                update(updateName0);
            }
            Object[] ref = new Object[1];
            MathsBase.adaptiveEval(expressions[0], new AdaptiveConfig()
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

    private PlotModel _plotExprArray0(Expr[] expressions, PlotBuilder builder) {
        List<PlotHyperCube> discretes = new ArrayList<PlotHyperCube>();
        List<Expr> other = new ArrayList<Expr>();
        for (Expr expression : expressions) {
            if (expression instanceof VDiscrete) {
                discretes.add((VDiscrete) expression);
            } else if (expression instanceof Discrete) {
                discretes.add(new VDiscrete((Discrete) expression));
            } else if (expression.getDomainDimension() == 3) {
                if (expression.isScalarExpr()) {
                    if (builder.getSamples() != null) {
                        Discrete discretized = (Discrete) MathsBase.discrete(expression, builder.getSamples());
                        discretes.add(new VDiscrete(discretized));
                    } else {
                        int xs = builder.getXsamples() <= 0 ? 10 : builder.getXsamples();
                        int ys = builder.getYsamples() <= 0 ? 10 : builder.getYsamples();
                        int zs = builder.getZsamples() <= 0 ? 10 : builder.getZsamples();
                        Discrete discretized = (Discrete) MathsBase.discrete(expression, xs, ys, zs);
                        discretes.add(new VDiscrete(discretized));
                    }
                } else {
                    if (builder.getSamples() != null) {
                        Expr discretized = MathsBase.discrete(expression, builder.getSamples());
                        discretes.add((VDiscrete) discretized);
                    } else {
                        int xs = builder.getXsamples() <= 0 ? 10 : builder.getXsamples();
                        int ys = builder.getYsamples() <= 0 ? 10 : builder.getYsamples();
                        int zs = builder.getZsamples() <= 0 ? 10 : builder.getZsamples();
                        Expr discretized = MathsBase.discrete(expression, xs, ys, zs);
                        discretes.add((VDiscrete) discretized);
                    }
                }
            } else {
                other.add(expression);
            }
        }
        if (discretes.size() == 0) {
            PlotDoubleConverter c = builder.getConverter(PlotDoubleConverter.ABS);
            ExpressionsPlotModel m = _createExpressionsPlotModel(builder)
                    .setExpressions(other.toArray(new Expr[0]))
                    .setComplexAsDouble(c);
            PlotType pt = builder.getPlotType();
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
            PlotHyperCubePlotModel mm = new PlotHyperCubePlotModel()
                    .setLibraries(builder.getLibraries())
                    .setHyperCubes(discretes)
                    .setConverter(builder.getConverter());
            PlotModelUtils.postConfigure(mm, builder);
            return mm;
        } else {
            PlotModelList list = new PlotModelList("Expressions");
            PlotHyperCubePlotModel mm = new PlotHyperCubePlotModel()
                    .setLibraries(builder.getLibraries())
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

    private ExpressionsPlotModel _createExpressionsPlotModel(PlotBuilder builder) {
        ExpressionsPlotModel mm = new ExpressionsPlotModel().setDomain((Domain) builder.getDomain()).setSamples(builder.getSamples()).setXprec(builder.getXsamples()).setYprec(builder.getYsamples())
                .setPlotType(_getPlotType(PlotType.CURVE,builder))
                .setConstX(builder.isConstX())
                .setLibraries(builder.getLibraries())
                .setProperties(builder.getProperties())
                .setComplexAsDouble(builder.getConverter(PlotDoubleConverter.ABS));
        PlotModelUtils.postConfigure(mm,builder);
        return mm;
    }



}
