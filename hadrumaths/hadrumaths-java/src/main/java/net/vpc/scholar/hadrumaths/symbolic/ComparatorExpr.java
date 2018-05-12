package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;

public abstract class ComparatorExpr extends GenericFunctionXY {
    private static final long serialVersionUID = 1L;
    public ComparatorExpr(Expr xargument, Expr yargument) {
        super(xargument, yargument);
    }

    public ComparatorExpr(Expr xargument, Expr yargument, FunctionType lowerFunctionType) {
        super(xargument, yargument, lowerFunctionType);
    }

//    @Override
//    public String toString() {
//        return "("+getXArgument()+" "+getFunctionName()+" "+getYArgument()+")";
//    }

    @Override
    public Expr mul(Domain domain) {
        return newInstance(
                getXArgument().getDomain().intersect(domain),
                getYArgument().getDomain().intersect(domain)
        );
    }


}
