/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.expeval;

import net.vpc.common.jeep.DefaultExpressionManager;
import net.vpc.common.jeep.ExpressionManager;
import net.vpc.common.jeep.PlatformHelper;
import net.vpc.common.jeep.UtilClassExpressionEvaluatorResolver;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.MathsBase;

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

    private static final UtilClassExpressionEvaluatorResolver EVAL_RESOLVER = new UtilClassExpressionEvaluatorResolver(false, MathsBase.class
            , PlatformHelper.class
            , PlatformHelper2.class
    );

    private static final UtilClassExpressionEvaluatorResolver PARSER = new UtilClassExpressionEvaluatorResolver(true,
            PlatformHelperAsExpr.class).addImportFields(MathsBase.class);

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
        e.declareBinaryOperators("**", "->","+","-","*","/","^","%","<",">",">=","<=","==","=",",");
        e.declareUnaryOperators("-","!", "~");
        e.declareListOperator(",");
        return e;
    }


}