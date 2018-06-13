/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.expeval;

import net.vpc.common.jeep.DefaultExpressionEvaluator;
import net.vpc.common.jeep.ExpressionEvaluator;
import net.vpc.common.jeep.PlatformHelper;
import net.vpc.common.jeep.UtilClassExpressionEvaluatorResolver;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.expeval.PlatformHelper2;
import net.vpc.scholar.hadrumaths.expeval.PlatformHelperAsExpr;
import net.vpc.scholar.hadrumaths.expeval.ExprNodeResolver;

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
public final class ExpressionEvaluatorFactory {

    private static final UtilClassExpressionEvaluatorResolver EVAL_RESOLVER = new UtilClassExpressionEvaluatorResolver(false, Maths.class
            , PlatformHelper.class
            , PlatformHelper2.class
    );

    private static final UtilClassExpressionEvaluatorResolver PARSER = new UtilClassExpressionEvaluatorResolver(true, PlatformHelperAsExpr.class).addImportFields(Maths.class);

    public static ExpressionEvaluator createEvaluator() {
        DefaultExpressionEvaluator e = new DefaultExpressionEvaluator();
        e.getContext().setResolver(EVAL_RESOLVER);
        e.getContext().setNodeResolver(ExprNodeResolver.INSTANCE);
        return e;
    }

    public static ExpressionEvaluator createParser() {
        DefaultExpressionEvaluator e = new DefaultExpressionEvaluator();
        e.getContext().setResolver(PARSER);
        e.getContext().setNodeResolver(ExprNodeResolver.INSTANCE);
        return e;
    }


}
