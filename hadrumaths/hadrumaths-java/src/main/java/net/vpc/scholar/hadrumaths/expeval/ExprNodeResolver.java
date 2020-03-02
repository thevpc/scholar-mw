package net.vpc.scholar.hadrumaths.expeval;

import net.vpc.common.jeep.*;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.vpc.scholar.hadrumaths.symbolic.double2double.DefaultDoubleValue;
import net.vpc.scholar.hadrumaths.symbolic.double2double.DoubleParam;

public class ExprNodeResolver extends AbstractExpressionEvaluatorResolver {
    public static final ExprNodeResolver INSTANCE = new ExprNodeResolver();

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

    @Override
    public Object implicitConvertLiteral(Object literal, ExpressionManager evaluator) {
        if (literal instanceof ExprDoubleComplex) {
            ExprDoubleComplex li = ((ExprDoubleComplex) literal);
            return Complex.of(li.getReal(), li.getImag());
        }
        return literal;
    }

//    @Override
//    public Class resolveNodeType(Class type) {
//        if (type.equals(Double.class)) {
//            return ParamExpr.class;
//        }
//        return Expr.class;
//    }

    @Override
    public ExpressionEvaluatorConverter[] resolveImplicitConverters(Class type) {
        if (type.equals(Double.class)) {
            return new ExpressionEvaluatorConverter[]{

                    new AbstractExpressionEvaluatorConverter(double.class, Complex.class) {
                        @Override
                        public Object convert(Object value) {
                            return Complex.of((Double) value);
                        }
                    },
                    new AbstractExpressionEvaluatorConverter(Double.class, DoubleValue.class) {
                        @Override
                        public Object convert(Object value) {
                            return Maths.expr((Double) value);
                        }
                    }
            };
        }
        if (type.equals(double.class)) {
            return new ExpressionEvaluatorConverter[]{
                    new AbstractExpressionEvaluatorConverter(double.class, Complex.class) {
                        @Override
                        public Object convert(Object value) {
                            return Complex.of((Double) value);
                        }
                    }, new AbstractExpressionEvaluatorConverter(double.class, DoubleValue.class) {
                @Override
                public Object convert(Object value) {
                    return Maths.expr((Double) value);
                }
            }

            };
        }
        return null;// new ExpressionEvaluatorConverter[0];
    }
}
