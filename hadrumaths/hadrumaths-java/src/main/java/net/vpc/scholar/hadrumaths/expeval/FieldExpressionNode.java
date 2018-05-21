package net.vpc.scholar.hadrumaths.expeval;

import java.lang.reflect.Field;

class FieldExpressionNode implements ExpressionNode {
    private final Field field;

    public FieldExpressionNode(Field field) {
        this.field = field;
    }

    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public Class getExprType() {
        return field.getType();
    }

//    @Override
//    public Object evaluate(Stack<Object> valuesStack, ExpressionEvaluatorContext context) {
//        try {
//            return field.get(null);
//        }catch (IllegalAccessException ex){
//            throw new IllegalArgumentException(ex);
//        }
//    }

    @Override
    public Object evaluate(Object[] args, ExpressionEvaluatorContext context) {
        try {
            return field.get(null);
        }catch (IllegalAccessException ex){
            throw new IllegalArgumentException(ex);
        }
    }

    @Override
    public String getString(Object[] args) {
        return field.getName();
    }
}
