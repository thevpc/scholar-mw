package net.vpc.scholar.hadrumaths.expeval;

import net.vpc.scholar.hadrumaths.Maths;

public class VarExpressionNode implements ExpressionNode {
    private final String varName;
    private final Class type;

    public VarExpressionNode(String varName, Class type) {
        this.varName = varName;
        this.type = type;
    }

    @Override
    public String getName() {
        return varName;
    }

    @Override
    public Class getExprType() {
        return type;
    }

//    @Override
//    public Object evaluate(Stack<Object> valuesStack, ExpressionEvaluatorContext context) {
//        return context.getVariableValue(varName);
//    }

    @Override
    public Object evaluate(Object[] args, ExpressionEvaluatorContext context) {
        ExpressionEvaluatorContext.ExprVar v = context.getVariable(varName);
        if(!v.isDefinedValue()){
            if(v.getType().equals(Double.class)){
                return Maths.param(v.getName());
            }
            throw new IllegalArgumentException("Unsupported Var "+v.getName()+" of type "+v.getType());
        }
        return v.getValue();
    }

    @Override
    public String getString(Object[] args) {
        return varName;
    }
}
