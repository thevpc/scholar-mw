package net.thevpc.scholar.hadrumaths.plot;

import java.util.HashMap;
import java.util.Map;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.geom.Point;
import net.thevpc.scholar.hadrumaths.symbolic.CustomFunction;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadruplot.extension.defaults.DefaultPlotValueFactory;
import net.thevpc.scholar.hadruplot.PlotBuilder;
import net.thevpc.scholar.hadruplot.PlotValueAndPriority;
import net.thevpc.scholar.hadruplot.extension.PlotValueFactory;

public class MathsPlotValueFactory extends DefaultPlotValueFactory {

    public static final PlotValueFactory INSTANCE = new MathsPlotValueFactory();

    public MathsPlotValueFactory() {
        super(10);
    }

    @Override
    protected PlotValueAndPriority createCustomPlotValue(Object obj, PlotBuilder builder) {
        if (obj instanceof Complex) {
            return createPlotValue("complex", obj);
        }
        if (obj instanceof Expr) {
            Expr e = ((Expr) obj);
            if (e.isNarrow(ExprType.DOUBLE_EXPR) && e.getDomain().isUnbounded()) {
                return createPlotValue("number", ((Expr) obj).toDouble());
            }
        }
        if (obj instanceof AxisVector) {
            return createPlotValue("AxisVector", obj);
        }
        if (obj instanceof Matrix) {
            Matrix m = (Matrix) obj;
            int c = m.getColumnCount();
            int r = m.getRowCount();
            if (c == 1) {
                if (r == 1) {
                    return createPlotValue(m.get(0, 0), builder);
                }
                return createPlotValue(m.getColumn(0), builder);
            }
            Map<String, String> props = new HashMap<>();
            props.put("matrix", "true");
            for (Object o : m.getRows()) {
                PlotValueAndPriority typeAndValue = createPlotValue(o, builder);
                if (typeAndValue.getValue().getType().getName().equals("complex[]")) {
                    return createPlotValue("complex[][]", obj, props);
                }
            }
            return createPlotValue("number[][]", obj, props);
        }
        if (obj instanceof Vector) {
            Vector vv = (Vector) obj;
            if (Vector.class.isAssignableFrom(vv.getComponentType().getTypeClass())) {
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
            Map<String, String> props = new HashMap<>();
            props.put("column", String.valueOf(vv.isColumn()));

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
            return createPlotValue(subType + "[]", obj, props);
        }
        if (obj instanceof ToDoubleArrayAware) {
            return (createPlotValue("number[]", ((ToDoubleArrayAware) obj).toDoubleArray()));
        }
        if (obj instanceof Expr) {
            return createPlotValue("expr", obj);
        }
        if (obj instanceof CustomFunction) {
            return createPlotValue("custom-function", obj);
        }
        if (obj instanceof Point) {
            return createPlotValue("point", obj);
        }
        return null;
    }

    @Override
    public boolean isColumn(Object obj) {
        if (obj instanceof Vector) {
            Vector vv = (Vector) obj;
            return vv.isColumn();
        }
        return false;
    }

}
