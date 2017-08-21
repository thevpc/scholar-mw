package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Expr;

public abstract class ComparatorExpr extends GenericFunctionXY {
    public ComparatorExpr(Expr xargument, Expr yargument) {
        super(xargument, yargument);
    }

    public ComparatorExpr(Expr xargument, Expr yargument, FunctionType lowerFunctionType) {
        super(xargument, yargument, lowerFunctionType);
    }

    @Override
    public String toString() {
        return "("+getXArgument()+getFunctionName()+getYArgument()+")";
    }
}
