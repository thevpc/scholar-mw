package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadruplot.AbstractPlotValueType;

public class PlotValueExprType extends AbstractPlotValueType {
    public PlotValueExprType() {
        super("expr");
    }

    public Expr toExpr(Object o){
        return (Expr) o;
    }

    @Override
    public Object getValue(Object o) {
        return (Expr) o;
    }
}
