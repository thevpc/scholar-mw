/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.expeval;

import java.util.Map;
import java.util.Stack;

/**
 *
 * @author vpc
 */
public interface ExpressionNode {
    public String getName();
    public Object evaluate(Stack<Object> valuesStack,Map<String,Object> variables);
}
