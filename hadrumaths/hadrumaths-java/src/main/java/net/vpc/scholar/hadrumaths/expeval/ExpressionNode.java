/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.expeval;

/**
 * @author vpc
 */
public interface ExpressionNode {
    int PRECEDENCE_1 = 5;
    int PRECEDENCE_2 = 10;
    int PRECEDENCE_3 = 15;
    int PRECEDENCE_4 = 20;
    int PRECEDENCE_5 = 25;
    int PRECEDENCE_6 = 30;
    int PRECEDENCE_MAX = 32;

    String getName();

    Class getExprType();

    Object evaluate(Object[] args, ExpressionEvaluatorContext context);

    String getString(Object[] args);
}
