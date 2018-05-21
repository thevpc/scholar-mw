/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.expeval;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Maths;

import java.io.IOException;
import java.io.StringReader;
import java.util.Stack;

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
public abstract class AbstractExpressionEvaluator implements ExpressionEvaluator{
    ExpressionEvaluatorContext context=new ExpressionEvaluatorContext();
    /***
     * creates an empty MathEvaluator. You need to use setExpression(String s) to assign a math expression string to it.
     */
    public AbstractExpressionEvaluator() {
    }


    public ExpressionEvaluatorContext getContext() {
        return context;
    }



}
