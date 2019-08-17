package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.geom.Point;
import net.vpc.scholar.hadruplot.DefaultPlotValueFactory;
import net.vpc.scholar.hadruplot.PlotBuilder;
import net.vpc.scholar.hadruplot.PlotValue;
import net.vpc.scholar.hadruplot.PlotValueFactory;

public class MathsPlotValueFactory extends DefaultPlotValueFactory {
    public static final PlotValueFactory INSTANCE = new MathsPlotValueFactory();

    public MathsPlotValueFactory() {
        super(10);
    }

    protected PlotValue createCustomPlotValue(Object obj, PlotBuilder builder) {
        if (obj instanceof Complex) {
            return createPlotValue("complex", obj);
        }
        if (obj instanceof Expr && ((Expr) obj).isDouble()) {
            return createPlotValue("number", ((Expr) obj).toDouble());
        }
        if (obj instanceof TMatrix) {
            TMatrix m = (TMatrix) obj;
            int c = m.getColumnCount();
            int r = m.getRowCount();
            if (c == 1) {
                if (r == 1) {
                    return createPlotValue(m.get(0, 0), builder);
                }
                return createPlotValue(m.getColumn(0), builder);
            }
            for (Object o : m.getRows()) {
                PlotValue typeAndValue = createPlotValue(o, builder);
                if (typeAndValue.getType().getName().equals("complex[]")) {
                    PlotValue typeAndValue1 = createPlotValue("complex[][]", obj);
                    typeAndValue1.set("matrix", "true");
                    return typeAndValue1;
                }
            }
            PlotValue typeAndValue = createPlotValue("number[][]", obj);
            typeAndValue.set("matrix", "true");
            return typeAndValue;
        }
        if (obj instanceof TVector) {
            TVector vv = (TVector) obj;
            if (TVector.class.isAssignableFrom(vv.getComponentType().getTypeClass())) {
                Object[] arr = vv.toArray();
                return createPlotValue(arr, builder);
            }
            if (vv.isConvertibleTo(Maths.$DOUBLE)) {
                vv = vv.to(Maths.$DOUBLE);
            } else if (vv.isConvertibleTo(Maths.$COMPLEX)) {
                vv = vv.to(Maths.$COMPLEX);
            } else if (vv.isConvertibleTo(Maths.$EXPR)) {
                vv = vv.to(Maths.$EXPR);
            }
            String subType = "object";
            if (vv.getComponentType().equals(Maths.$COMPLEX)) {
                subType = "complex";
            } else if (vv.getComponentType().equals(Maths.$EXPR)) {
                subType = "expr";
            } else if (vv.getComponentType().equals(Maths.$DOUBLE)) {
                subType = "number";
            } else if (vv.getComponentType().equals(Maths.$BOOLEAN)) {
                subType = "boolean";
            } else if (vv.getComponentType().equals(Maths.$POINT)) {
                subType = "point";
            } else if (vv.getComponentType().equals(Maths.$FILE)) {
                subType = "file";
            }
            PlotValue typeAndValue = createPlotValue(subType + "[]", obj);
            typeAndValue.set("column", String.valueOf(vv.isColumn()));
            return typeAndValue;
        }
        if (obj instanceof ToDoubleArrayAware) {
            return (createPlotValue("number[]", ((ToDoubleArrayAware) obj).toDoubleArray()));
        }
        if (obj instanceof Expr) {
            return createPlotValue("expr", obj);
        }
        if (obj instanceof Point) {
            return createPlotValue("point", obj);
        }
        return null;
    }

    public boolean isColumn(Object obj) {
        if (obj instanceof TVector) {
            TVector vv = (TVector) obj;
            if (vv.isColumn()) {
                return true;
            }
        }
        return false;
    }

}
