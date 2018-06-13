package net.vpc.scholar.hadrumaths.expeval;

import net.vpc.common.jeep.ExpressionNodeResolver;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.DoubleParam;

public class ExprNodeResolver implements ExpressionNodeResolver {
    public static final ExprNodeResolver INSTANCE=new ExprNodeResolver();
    @Override
    public Class resolveNodeType(Class type) {
        if (type.equals(Double.class)) {
            return DoubleParam.class;
        }
        return Expr.class;
    }

    @Override
    public Object resolveNodeParam(Class type, String name) {
        if (type.equals(Double.class)) {
            return Maths.param(name);
        }
        return null;
    }
}
