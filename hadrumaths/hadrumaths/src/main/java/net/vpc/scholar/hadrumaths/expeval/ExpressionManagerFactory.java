/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.expeval;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.core.DefaultJeep;
import net.vpc.common.jeep.core.imports.PlatformHelperImports;
import net.vpc.common.jeep.core.UtilClassJResolver;
import net.vpc.common.jeep.impl.functions.JMethodInvocationFunction;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.RightArrowUplet2;

import java.lang.reflect.InvocationTargetException;

import net.vpc.scholar.hadrumaths.Vector;

/**
 * <i>Mathematic expression evaluator.</i> Supports the following functions: +,
 * -, *, /, ^, %, cos, sin, tan, acos, asin, atan, sqrt, sqr, log, min, max,
 * ceil, floor, absdbl, neg, rndr.<br>
 * <pre>
 * Sample:
 * MathEvaluator m = new MathEvaluator();
 * m.declare("x", 15.1d);
 * System.out.println( m.evaluate("-5-6/(-2) + sqr(15+x)") );
 * </pre>
 *
 * @author Taha BEN SALAH
 * @version 1.0
 * @date April 2008
 */
public final class ExpressionManagerFactory {

    private static Jeep PARSER_M;
    private static Jeep EVALUATOR_M;

    private static Expr dblOrExpr(Object o) {
        return (o instanceof Number) ? Maths.expr(((Number) o).doubleValue()) : (Expr) o;
    }

    public static JContext createEvaluator() {
        if (EVALUATOR_M == null) {
            EVALUATOR_M = createEvaluator0();
        }
        return EVALUATOR_M.newContext();
    }

    public static JContext createParser() {
        if (PARSER_M == null) {
            PARSER_M = createParser0();
        }
        return PARSER_M.newContext();
    }

    public static Jeep createEvaluator0() {
        DefaultJeep e = new DefaultJeep();
        e.resolvers().importType(Maths.class);
        e.resolvers().importType(PlatformHelperImports.class);
        e.resolvers().importType(PlatformHelper2.class);
        e.resolvers().addResolver(ExprNodeResolver.INSTANCE);
        return e;
    }

    public static Jeep createParser0() {
        DefaultJeep e = new DefaultJeep();
        JTypes types = e.types();
        UtilClassJResolver PARSER = new MathsParserUtilClassJResolver(types);//.addImportFields(Maths.class);
        e.resolvers().addResolver(PARSER);
        e.resolvers().addResolver(ExprNodeResolver.INSTANCE);
        e.operators().declareBinaryOperators("**", "->", "+", "-", "*", "/", "^", "%", "<", ">", ">=", "<=", "==", "=", ",");
        e.operators().declarePrefixUnaryOperators("-", "!", "~");
        e.operators().declareListOperator(",");
        JType Expr_type = types.forName(Expr.class.getName());
        JType Vector_type = types.forName(Vector.class.getName());
        e.functions().declare("{", new JType[]{Expr_type}, Vector_type, false, new JInvoke() {
            @Override
            public Object invoke(JInvokeContext context) {
                JEvaluable[] args = context.arguments();
                return Maths.vector((Expr) context.evaluate(args[0]));
            }
        });
        e.functions().declare("{", new JType[]{Expr_type,Expr_type}, Vector_type, false, new JInvoke() {
            @Override
            public Object invoke(JInvokeContext context) {
                JEvaluable[] args = context.arguments();
                return Maths.vector(
                        (Expr) context.evaluate(args[0]),
                         (Expr) context.evaluate(args[1])
                );
            }
        });
        e.functions().declare("{",  new JType[]{Expr_type,Expr_type}, Vector_type, false, new JInvoke() {
            @Override
            public Object invoke(JInvokeContext context) {
                JEvaluable[] args = context.arguments();
                return Maths.vector(
                        (Expr) context.evaluate(args[0]),
                         (Expr) context.evaluate(args[1]),
                         (Expr) context.evaluate(args[2])
                );
            }
        });
        return e;
    }

    private static class MathsParserUtilClassJResolver extends UtilClassJResolver {
        public MathsParserUtilClassJResolver(JTypes types) {
            super(true, false,types.forName(PlatformHelperAsExpr.class.getName()));
        }

        @Override
        public JFunction createMethodInvocationFunction(String name, JMethod m, int baseIndex, int... argIndices) {
            switch (m.toString()) {
                case "public static net.vpc.scholar.hadrumaths.Expr net.vpc.scholar.hadrumaths.expeval.PlatformHelperAsExpr.domain(net.vpc.scholar.hadrumaths.RightArrowUplet2)": {
                    return new JMethodInvocationFunction(name, m, baseIndex, argIndices) {
                        @Override
                        protected Object evaluateImpl(JMethod m, JInvokeContext icontext) throws InvocationTargetException, IllegalAccessException {
                            RightArrowUplet2 a = (RightArrowUplet2) icontext.evaluateArg(0);
//                            if (a.getFirst() instanceof Expr && a.getSecond() instanceof Expr) {
//                                return super.eval(args, evaluator);
//                            }
                            if (a.getFirst() instanceof Number && a.getSecond() instanceof Number) {
                                return PlatformHelperAsExpr.domain(((Number) a.getFirst()).doubleValue(), ((Number) a.getSecond()).doubleValue());
                            }
                            return PlatformHelperAsExpr.domain(dblOrExpr(a.getFirst()), dblOrExpr(a.getSecond()));
                        }

                    };
                }
                case "public static net.vpc.scholar.hadrumaths.Expr net.vpc.scholar.hadrumaths.expeval.PlatformHelperAsExpr.domain(net.vpc.scholar.hadrumaths.RightArrowUplet2,net.vpc.scholar.hadrumaths.RightArrowUplet2)": {
                    return new JMethodInvocationFunction(name, m, baseIndex, argIndices) {
                        @Override
                        protected Object evaluateImpl(JMethod m, JInvokeContext icontext) throws InvocationTargetException, IllegalAccessException {
                            RightArrowUplet2 a = (RightArrowUplet2) icontext.evaluateArg(0);
                            RightArrowUplet2 b = (RightArrowUplet2) icontext.evaluateArg(1);
//                            if (a.getFirst() instanceof Expr && a.getSecond() instanceof Expr && b.getFirst() instanceof Expr && b.getSecond() instanceof Expr) {
//                                return super.eval(args, evaluator);
//                            }
                            if (a.getFirst() instanceof Number && a.getSecond() instanceof Number && b.getFirst() instanceof Number && b.getSecond() instanceof Number) {
                                return PlatformHelperAsExpr.domain(((Number) a.getFirst()).doubleValue(), ((Number) a.getSecond()).doubleValue(), ((Number) b.getFirst()).doubleValue(), ((Number) b.getSecond()).doubleValue());
                            }
                            return PlatformHelperAsExpr.domain(dblOrExpr(a.getFirst()), dblOrExpr(a.getSecond()), dblOrExpr(b.getFirst()), dblOrExpr(b.getSecond()));
                        }

                    };
                }
                case "public static net.vpc.scholar.hadrumaths.Expr net.vpc.scholar.hadrumaths.expeval.PlatformHelperAsExpr.domain(net.vpc.scholar.hadrumaths.RightArrowUplet2,net.vpc.scholar.hadrumaths.RightArrowUplet2,net.vpc.scholar.hadrumaths.RightArrowUplet2)": {
                    return new JMethodInvocationFunction(name, m, baseIndex, argIndices) {
                        @Override
                        protected Object evaluateImpl(JMethod m, JInvokeContext icontext) throws InvocationTargetException, IllegalAccessException {
                            RightArrowUplet2 a = (RightArrowUplet2) icontext.evaluateArg(0);
                            RightArrowUplet2 b = (RightArrowUplet2) icontext.evaluateArg(1);
                            RightArrowUplet2 c = (RightArrowUplet2) icontext.evaluateArg(2);
                            if (a.getFirst() instanceof Expr && a.getSecond() instanceof Expr && b.getFirst() instanceof Expr && b.getSecond() instanceof Expr && c.getFirst() instanceof Expr && c.getSecond() instanceof Expr) {
                                return super.invoke(icontext);
                            }
                            if (a.getFirst() instanceof Number && a.getSecond() instanceof Number && b.getFirst() instanceof Number && b.getSecond() instanceof Number && c.getFirst() instanceof Number && c.getSecond() instanceof Number) {
                                return PlatformHelperAsExpr.domain(((Number) a.getFirst()).doubleValue(), ((Number) a.getSecond()).doubleValue(), ((Number) b.getFirst()).doubleValue(), ((Number) b.getSecond()).doubleValue(), ((Number) c.getFirst()).doubleValue(), ((Number) c.getSecond()).doubleValue());
                            }
                            return PlatformHelperAsExpr.domain(dblOrExpr(a.getFirst()), dblOrExpr(a.getSecond()), dblOrExpr(b.getFirst()), dblOrExpr(b.getSecond()), dblOrExpr(c.getFirst()), dblOrExpr(c.getSecond()));
                        }

                    };
                }
            }
            return super.createMethodInvocationFunction(name, m, baseIndex, argIndices);
        }
    }
}
