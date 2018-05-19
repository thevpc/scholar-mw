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

    public AbstractExpressionNode(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

}
