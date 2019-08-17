package net.vpc.scholar.hadruplot;

import net.vpc.scholar.hadruplot.console.PlotConfigManager;
import net.vpc.scholar.hadruplot.util.PlotModelUtils;

public class DefaultPlotModelFactory implements PlotModelFactory{
    public static final PlotModelFactory INSTANCE=new DefaultPlotModelFactory();
    @Override
    public PlotModel createModel(PlotValue data, PlotBuilder builder) {
        PlotValueType type = data.getType();
        PlotValueType complexType = PlotConfigManager.getPlotValueTypeFactory().getType("number");
        switch (type.getName()) {
            case "number[][]": {
                PlotValueArrayType oo = (PlotValueArrayType) type;
                builder.asReal();
                boolean matrix = "true".equals(data.get("matrix"));
                Object o = oo.toArray(data.getValue(), Object[][].class);
                return PlotModelUtils._plotComplexArray2((Object[][]) o, matrix ? PlotType.MATRIX : PlotType.HEATMAP, false, builder,complexType);
            }
            case "number[]": {
                builder.asReal();
                PlotValueArrayType oo = (PlotValueArrayType) type;
                Object[] y = (Object[]) oo.toArray(data.getValue(), Object[].class);
                boolean col = "true".equals(data.get("column"));
                return PlotModelUtils._plotComplexArray1(y, PlotType.CURVE, col, builder,complexType);
            }
            case "number[][][]": {
                PlotValueArrayType oo = (PlotValueArrayType) type;
                Object[][][] y = (Object[][][]) oo.toArray(data.getValue(), Object[][][].class);
                return PlotModelUtils._plotComplexArray3(y, null, builder);
            }
            case "number": {
                builder.asReal();
                return PlotModelUtils._plotComplexArray2((new Object[][]{{(Object) type.toValue(data.getValue(), Object.class)}}), PlotType.CURVE, true, builder,complexType);
            }
            case "null[][]":
                return PlotModelUtils._plotComplexArray2(new Object[0][], builder.getPlotType(), false, builder,complexType);
//                throw new IllegalArgumentException("Nothing to plot ... (" + s + ")");
            case "null[]": {
                return PlotModelUtils._plotComplexArray2(new Object[0][], builder.getPlotType(), false, builder,complexType);
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
            case "object[]":
                {
                //check if this can be operated recursively
                //typeAndValue = PlotTypesHelper.resolveType(o);
                PlotValueArrayType arr=(PlotValueArrayType) type;
//                if (s.equals("object") || s.equals("null")) {
                //TODO should i return a container instead?
                PlotModelList list = new PlotModelList(builder.getTitle());
                for (Object a : arr.toArray(data.getValue())) {
                    list.add(builder.createModel(a, builder));
                }
                PlotModelUtils.postConfigure(list, builder);


                double[] y = {((PlotValueDoubleType) data).toDouble(data.getValue())};
                Double[] doubles=new Double[y.length];
                    for (int i = 0; i < y.length; i++) {
                        doubles[i]=y[i];
                    }
                builder.asReal();
                return PlotModelUtils._plotComplexArray2(new Double[][]{doubles}, PlotType.CURVE, false, builder,complexType);
            }
        }
        return null;
    }
}
