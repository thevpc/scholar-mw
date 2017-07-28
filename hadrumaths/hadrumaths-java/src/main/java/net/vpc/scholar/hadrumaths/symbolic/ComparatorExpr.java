package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Expr;

public abstract class ComparatorExpr extends GenericFunctionXY {
    public ComparatorExpr(String functionName,Expr xargument, Expr yargument) {
        super(functionName,xargument, yargument);
    }

    public ComparatorExpr(String functionName,Expr xargument, Expr yargument, FunctionType lowerFunctionType) {
        super(functionName,xargument, yargument, lowerFunctionType);
    }

    @Override
    public String toString() {
        return "("+getXArgument()+getFunctionName()+getYArgument()+")";
    }
}
