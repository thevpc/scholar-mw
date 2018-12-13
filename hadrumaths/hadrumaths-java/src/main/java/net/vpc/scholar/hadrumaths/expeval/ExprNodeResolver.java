package net.vpc.scholar.hadrumaths.expeval;

import net.vpc.common.jeep.*;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.DoubleParam;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;

public class ExprNodeResolver implements ExpressionEvaluatorResolver {
    public static final ExprNodeResolver INSTANCE = new ExprNodeResolver();

    @Override
    public Object implicitConvertLiteral(Object literal, ExpressionManager evaluator) {
        if (literal instanceof ExprDoubleComplex) {
            ExprDoubleComplex li = ((ExprDoubleComplex) literal);
            return Complex.valueOf(li.getReal(), li.getImag());
        }
        return literal;
    }

    @Override
    public ExpressionEvaluatorConverter[] resolveImplicitConverters(Class type) {
        if (type.equals(Double.class)) {
            return new ExpressionEvaluatorConverter[]{
                    new AbstractExpressionEvaluatorConverter(Double.class, DoubleValue.class) {
                        @Override
                        public Object convert(Object value) {
                            return Maths.expr((Double) value);
                        }
                    }
            };
        }
        return null;// new ExpressionEvaluatorConverter[0];
    }

//    @Override
//    public Class resolveNodeType(Class type) {
//        if (type.equals(Double.class)) {
//            return DoubleParam.class;
//        }
//        return Expr.class;
//    }


    @Override
    public Variable resolveVariable(String name, ExpressionManager context) {
        return new ReadOnlyVariable(name) {
            @Override
            public Class getType() {
                return DoubleParam.class;
            }

            @Override
            public Object getValue(ExpressionEvaluator evaluator) {
                return Maths.param(name);
            }
        };
    }
}
