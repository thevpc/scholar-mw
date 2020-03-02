/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.expeval;

import net.vpc.common.jeep.*;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.RightArrowUplet2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <i>Mathematic expression evaluator.</i> Supports the following functions:
 * +, -, *, /, ^, %, cos, sin, tan, acos, asin, atan, sqrt, sqr, log, min, max, ceil, floor, absdbl, neg, rndr.<br>
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

    private static final UtilClassExpressionEvaluatorResolver EVAL_RESOLVER = new UtilClassExpressionEvaluatorResolver(false, Maths.class
            , PlatformHelper.class
            , PlatformHelper2.class
    );

    private static Expr dblOrExpr(Object o) {
        return (o instanceof Number)? Maths.expr(((Number)o).doubleValue()):(Expr)o;
    }
    private static final UtilClassExpressionEvaluatorResolver PARSER = new UtilClassExpressionEvaluatorResolver(true,
            PlatformHelperAsExpr.class){
        @Override
        public Function createMethodInvocationFunction(String name, Method m, int baseIndex, int... argIndices) {
            switch (m.toString()){
                case "public static net.vpc.scholar.hadrumaths.Expr net.vpc.scholar.hadrumaths.expeval.PlatformHelperAsExpr.domain(net.vpc.scholar.hadrumaths.RightArrowUplet2)":{
                    return new MethodInvocationFunction(name,m,baseIndex,argIndices){
                        @Override
                        protected Object evaluateImpl(Object base, Method m, Object[] argsv, ExpressionNode[] args, ExpressionEvaluator evaluator) throws InvocationTargetException, IllegalAccessException {
                            RightArrowUplet2 a=(RightArrowUplet2) argsv[0];
                            if(a.getFirst() instanceof Expr && a.getSecond() instanceof Expr){
                                return super.eval(args, evaluator);
                            }
                            if(a.getFirst() instanceof Number && a.getSecond() instanceof Number){
                                return PlatformHelperAsExpr.domain(((Number)a.getFirst()).doubleValue(),((Number)a.getSecond()).doubleValue());
                            }
                            return PlatformHelperAsExpr.domain(dblOrExpr(a.getFirst()), dblOrExpr(a.getSecond()));
                        }

                    };
                }
                case "public static net.vpc.scholar.hadrumaths.Expr net.vpc.scholar.hadrumaths.expeval.PlatformHelperAsExpr.domain(net.vpc.scholar.hadrumaths.RightArrowUplet2,net.vpc.scholar.hadrumaths.RightArrowUplet2)":{
                    return new MethodInvocationFunction(name,m,baseIndex,argIndices){
                        @Override
                        protected Object evaluateImpl(Object base, Method m, Object[] argsv, ExpressionNode[] args, ExpressionEvaluator evaluator) throws InvocationTargetException, IllegalAccessException {
                            RightArrowUplet2 a=(RightArrowUplet2) argsv[0];
                            RightArrowUplet2 b=(RightArrowUplet2) argsv[1];
                            if(a.getFirst() instanceof Expr && a.getSecond() instanceof Expr && b.getFirst() instanceof Expr && b.getSecond() instanceof Expr){
                                return super.eval(args, evaluator);
                            }
                            if(a.getFirst() instanceof Number && a.getSecond() instanceof Number && b.getFirst() instanceof Number && b.getSecond() instanceof Number){
                                return PlatformHelperAsExpr.domain(((Number)a.getFirst()).doubleValue(),((Number)a.getSecond()).doubleValue(),((Number)b.getFirst()).doubleValue(),((Number)b.getSecond()).doubleValue());
                            }
                            return PlatformHelperAsExpr.domain(dblOrExpr(a.getFirst()), dblOrExpr(a.getSecond()), dblOrExpr(b.getFirst()), dblOrExpr(b.getSecond()));
                        }

                    };
                }
                case "public static net.vpc.scholar.hadrumaths.Expr net.vpc.scholar.hadrumaths.expeval.PlatformHelperAsExpr.domain(net.vpc.scholar.hadrumaths.RightArrowUplet2,net.vpc.scholar.hadrumaths.RightArrowUplet2,net.vpc.scholar.hadrumaths.RightArrowUplet2)":{
                    return new MethodInvocationFunction(name,m,baseIndex,argIndices){
                        @Override
                        protected Object evaluateImpl(Object base, Method m, Object[] argsv, ExpressionNode[] args, ExpressionEvaluator evaluator) throws InvocationTargetException, IllegalAccessException {
                            RightArrowUplet2 a=(RightArrowUplet2) argsv[0];
                            RightArrowUplet2 b=(RightArrowUplet2) argsv[1];
                            RightArrowUplet2 c=(RightArrowUplet2) argsv[2];
                            if(a.getFirst() instanceof Expr && a.getSecond() instanceof Expr && b.getFirst() instanceof Expr && b.getSecond() instanceof Expr && c.getFirst() instanceof Expr && c.getSecond() instanceof Expr){
                                return super.eval(args, evaluator);
                            }
                            if(a.getFirst() instanceof Number && a.getSecond() instanceof Number && b.getFirst() instanceof Number && b.getSecond() instanceof Number && c.getFirst() instanceof Number && c.getSecond() instanceof Number){
                                return PlatformHelperAsExpr.domain(((Number)a.getFirst()).doubleValue(),((Number)a.getSecond()).doubleValue(),((Number)b.getFirst()).doubleValue(),((Number)b.getSecond()).doubleValue(),((Number)c.getFirst()).doubleValue(),((Number)c.getSecond()).doubleValue());
                            }
                            return PlatformHelperAsExpr.domain(dblOrExpr(a.getFirst()), dblOrExpr(a.getSecond()), dblOrExpr(b.getFirst()), dblOrExpr(b.getSecond()), dblOrExpr(c.getFirst()), dblOrExpr(c.getSecond()));
                        }

                    };
                }
            }
            return super.createMethodInvocationFunction(name, m, baseIndex, argIndices);
        }
    }.addImportFields(Maths.class);

    public static ExpressionManager createEvaluator() {
        DefaultExpressionManager e = new DefaultExpressionManager();
        e.addResolver(EVAL_RESOLVER);
        e.addResolver(ExprNodeResolver.INSTANCE);
        return e;
    }

    public static ExpressionManager createParser() {
        DefaultExpressionManager e = new DefaultExpressionManager();
        e.addResolver(PARSER);
        e.addResolver(ExprNodeResolver.INSTANCE);
        e.declareBinaryOperators("**", "->", "+", "-", "*", "/", "^", "%", "<", ">", ">=", "<=", "==", "=", ",");
        e.declareUnaryOperators("-", "!", "~");
        e.declareListOperator(",");
        return e;
    }


}
