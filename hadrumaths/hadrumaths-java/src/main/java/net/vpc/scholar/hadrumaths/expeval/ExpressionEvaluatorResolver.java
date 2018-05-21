package net.vpc.scholar.hadrumaths.expeval;

public interface ExpressionEvaluatorResolver {
    ExpressionNode resolveVar(String name, boolean checkEmptyArgMethods);

    ExpressionNode resolveFunction(String name, Class[] args);
}
