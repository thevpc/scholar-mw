package net.vpc.scholar.hadrumaths.expeval;

import java.util.ArrayList;

public class ExprInvocation {
    private ExpressionNode node;
    private Object[] args;

    public ExprInvocation(ExpressionNode node, Object[] args) {
        this.node = node;
        this.args = args;
    }

    public Class getReturnType() {
        return node.getExprType();
    }

    public Object eval(ExpressionEvaluatorContext context) {
        ArrayList u = new ArrayList();
        for (Object o : args) {
            if (o instanceof ExprInvocation) {
                o = ((ExprInvocation) o).eval(context);
            }
            u.add(o);
        }
        Object e = node.evaluate(u.toArray(), context);
        return transformResult(e);
    }

    private Object transformResult(Object result) {
//        if (result instanceof Number && Utils.isPlatformNumber((Number) result)) {
//            result = XNumber.valueOf((Number) result);
//        }
        return result;
    }

    @Override
    public String toString() {
        return node.getString(args);
    }
}
