package net.vpc.scholar.hadrumaths.expeval;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.core.AbstractJConverter;
import net.vpc.common.jeep.impl.vars.JVarReadOnly;
import net.vpc.common.jeep.util.JTypeUtils;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.vpc.scholar.hadrumaths.symbolic.double2double.DoubleParam;

public class ExprNodeResolver implements JResolver {
    public static final ExprNodeResolver INSTANCE = new ExprNodeResolver();

    @Override
    public JVar resolveVariable(String name, JContext context) {
        return new JVarReadOnly(name) {

            public JType type() {
                return context.types().forName(DoubleParam.class.getName());
            }

            @Override
            public Object getValue(JContext context) {
                return Maths.param(name);
            }
        };
    }

    @Override
    public Object implicitConvertLiteral(Object literal, JContext context) {
//        if (literal instanceof JComplex64) {
//            JComplex64 li = ((JComplex64) literal);
//            return Complex.of(li.real(), li.imag());
//        }
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
    public JConverter[] resolveImplicitConverters(JTypeOrLambda type) {
        if(type==null || type.isLambda()){
            return new JConverter[0];
        }
//        if (type.equals(type.types().forName(JComplex64.class))) {
//            return new JConverter1[]{
//
//                    new AbstractJConverter(JComplex64.class, Complex.class,2,type.types()) {
//                        @Override
//                        public Object convert(Object value, JInvokeContext context) {
//                            JComplex64 c=(JComplex64) value;
//                            return Complex.of(c.real(),c.imag());
//                        }
//                    }
//            };
//        }
        JType ttype = type.getType();
        final JTypes types = ttype.types();
        if (ttype.equals(types.forName(Double.class.getName()))) {
            return new JConverter[]{

                    new AbstractJConverter(JTypeUtils.forDouble(types), types.forName(Complex.class.getName()),2) {
                        @Override
                        public Object convert(Object value, JInvokeContext context) {
                            return Complex.of((Double) value);
                        }
                    },
                    new AbstractJConverter(types.forName(Double.class.getName()), types.forName(DoubleValue.class.getName()),2) {
                        @Override
                        public Object convert(Object value, JInvokeContext context) {
                            return Maths.expr((Double) value);
                        }
                    }
            };
        }
        if (ttype.equals(JTypeUtils.forDouble(types))) {
            return new JConverter[]{
                    new AbstractJConverter(JTypeUtils.forDouble(types), types.forName(Complex.class.getName()),2) {
                        @Override
                        public Object convert(Object value, JInvokeContext context) {
                            return Complex.of((Double) value);
                        }
                    }, new AbstractJConverter(JTypeUtils.forDouble(types), types.forName(DoubleValue.class.getName()),2) {
                @Override
                public Object convert(Object value, JInvokeContext context) {
                    return Maths.expr((Double) value);
                }
            }

            };
        }
        return null;// new JConverter1[0];
    }
}
