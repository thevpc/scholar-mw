package net.thevpc.scholar.hadrumaths.plot;

import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadruplot.model.value.AbstractPlotValueType;

public class PlotValueCustomFunctionType extends AbstractPlotValueType {
    public PlotValueCustomFunctionType() {
        super("custom-function");
    }

    public Expr toExpr(Object o) {
        return (Expr) o;
    }

    @Override
    public Object getValue(Object o) {
        return o;
    }
}
