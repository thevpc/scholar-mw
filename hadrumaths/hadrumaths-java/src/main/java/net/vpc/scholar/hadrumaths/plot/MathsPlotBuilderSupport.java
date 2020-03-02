package net.vpc.scholar.hadrumaths.plot;

import net.vpc.common.util.TypeName;
import net.vpc.scholar.hadrumaths.DoubleVector;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Vector;
import net.vpc.scholar.hadruplot.PlotBuilder;
import net.vpc.scholar.hadruplot.PlotBuilderSupport;

public class MathsPlotBuilderSupport implements PlotBuilderSupport {

    public MathsPlotBuilderSupport() {
    }

    @Override
    public boolean xsamples(Object xvalue, PlotBuilder builder) {
        if (xvalue instanceof Vector) {
            Vector lxvalue = (Vector) xvalue;
            if (lxvalue.length() == 0) {
                builder.xsamples(new double[0]);
                return true;
            }
            TypeName componentType = lxvalue.getComponentType();
            if (componentType.getTypeClass().equals(Double.TYPE)
                    || componentType.getTypeClass().equals(Double.class)) {
                DoubleVector to = (DoubleVector) lxvalue.to(Maths.$DOUBLE);
                builder.xsamples(to.toDoubleArray());
            } else {
                builder.samples(null);
                builder.xformat(new PlotBuilder.ListDoubleFormat(lxvalue.toList()));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean ysamples(Object xvalue, PlotBuilder builder) {
        if (xvalue instanceof Vector) {
            DoubleVector to = (DoubleVector) ((Vector) xvalue).to(Maths.$DOUBLE);
            builder.ysamples(to.toDoubleArray());
            return true;
        }
        return false;
    }

    @Override
    public boolean zsamples(Object xvalue, PlotBuilder builder) {
        if (xvalue instanceof Vector) {
            DoubleVector to = (DoubleVector) ((Vector) xvalue).to(Maths.$DOUBLE);
            builder.zsamples(to.toDoubleArray());
            return true;
        }
        return false;
    }


//    public PlotModel plotAny(Object any, PlotBuilder builder) {
//        if (any instanceof PlotLines) {
//            PlotLines y = (PlotLines) any;
//            builder.titles(y.titles());
//            builder.xsamples(y.xsamples());
//            any = y.getValues();
//        }
//        Vector a2 = toTVectorD(any);
//        if (a2 != null) {
//            any = a2;
//        }
//
////        if(true){
////            throw new IllegalArgumentException("Unsupported plot model "+plotData);
////        }
////        String s = typeAndValue.type;
////        Object o = typeAndValue.value;
////        switch (s) {
////            case "complex[][]": {
////                boolean matrix = "true".equals(typeAndValue.props.get("matrix"));
////                return _plotComplexArray2(PlotTypesHelper.toComplexArray2(o), matrix ? PlotType.MATRIX : PlotType.HEATMAP, false,builder);
////            }
////            case "complex[][][]": {
////                return _plotComplexArray3(PlotTypesHelper.toComplexArray3(o), null,builder);
////            }
////            case "number[][][]": {
////                return _plotComplexArray3(PlotTypesHelper.toComplexArray3(o), null,builder);
////            }
////            case "number[][]": {
////                builder.asReal();
////                boolean matrix = "true".equals(typeAndValue.props.get("matrix"));
////                return _plotComplexArray2(PlotTypesHelper.toComplexArray2(o), matrix ? PlotType.MATRIX : PlotType.HEATMAP, false,builder);
////            }
////            case "expr[]": {
////                return _plotExprArray(PlotTypesHelper.toExprArray(o),builder);
////            }
////            case "complex[]": {
////                Complex[] y = PlotTypesHelper.toComplexArray(o);
////                boolean col = "true".equals(typeAndValue.props.get("column"));
////                return _plotComplexArray2((new Complex[][]{y}), PlotType.CURVE, col,builder);
////            }
////            case "number[]": {
////                builder.asReal();
////                Complex[] y = PlotTypesHelper.toComplexArray(o);
////                boolean col = "true".equals(typeAndValue.props.get("column"));
////                return _plotComplexArray2((new Complex[][]{y}), PlotType.CURVE, col,builder);
////            }
////            case "point":
////                return _plotPoints(new Point[][]{{(Point) o}},builder);
////            case "point[][]":
////                return _plotPoints(PlotTypesHelper.toPointArray2(o),builder);
////            case "point[]":
////                return _plotPoints(new Point[][]{PlotTypesHelper.toPointArray(o)},builder);
////            case "expr":
////                return _plotExprArray(new Expr[]{(Expr) o},builder);
////            case "complex":
////                return plotAny(new Complex[]{PlotTypesHelper.toComplex(o)},builder);
////            case "number": {
////                double[] y = {((PlotDataDouble)typeAndValue).toDouble()};
////                builder.asReal();
////                return _plotComplexArray2((ArrayUtils.toComplex(new double[][]{y})), PlotType.CURVE, false,builder);
////            }
////            case "expr[][]":
////                throw new IllegalArgumentException("Not Supported Plot " + s);
////            case "null[][]":
////                return _plotComplexArray2(new Complex[0][], builder.getPlotType(), false,builder);
//////                throw new IllegalArgumentException("Nothing to plot ... (" + s + ")");
////            case "null[]": {
////                return _plotComplexArray2(new Complex[0][], builder.getPlotType(), false,builder);
//////                throw new IllegalArgumentException("Nothing to plot ... (" + s + ")");
////            }
////            default:
////                throw new IllegalArgumentException("Not Supported Plot " + s);
////        }
//    }


}