/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.expeval.DefaultExpressionEvaluator;
import net.vpc.scholar.hadrumaths.expeval.ExpressionEvaluator;
import net.vpc.scholar.hadrumaths.expeval.UtilClassExpressionEvaluatorResolver;
import net.vpc.scholar.hadrumaths.expeval.PlatformHelper;
import net.vpc.scholar.hadrumaths.expeval.PlatformHelperAsExpr;

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
    public static ExpressionEvaluator createEvaluator() {
        DefaultExpressionEvaluator e = new DefaultExpressionEvaluator();
        UtilClassExpressionEvaluatorResolver resolver = new UtilClassExpressionEvaluatorResolver(false, Maths.class, PlatformHelper.class);
        e.getContext().setResolver(resolver);
        return e;
    }

    public static ExpressionEvaluator createParser() {
        DefaultExpressionEvaluator e = new DefaultExpressionEvaluator();
        UtilClassExpressionEvaluatorResolver resolver = new UtilClassExpressionEvaluatorResolver(true, PlatformHelperAsExpr.class);
        resolver.addImportFields(Maths.class);
        e.getContext().setResolver(resolver);
        return e;
    }


}
