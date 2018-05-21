/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.expeval;

/**
 * @author vpc
 */
public abstract class AbstractExpressionNode implements ExpressionNode {
    private String name;
    private Class resultType;

    public AbstractExpressionNode(String name,Class resultType) {
        this.name = name;
        this.resultType = resultType;
    }

    @Override
    public Class getExprType() {
        return resultType;
    }

    @Override
    public String getName() {
        return name;
    }

}
