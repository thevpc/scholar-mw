/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.expeval.operators;

import net.vpc.scholar.hadrumaths.expeval.*;
import java.util.Map;
import java.util.Stack;
import net.vpc.scholar.hadrumaths.Complex;

/**
 *
 * @author vpc
 */
public abstract class UnaryOp extends AbstractExpressionNode{
    
    public UnaryOp(String name) {
        super(name);
    }

    @Override
    public Object evaluate(Stack<Object> valuesStack, Map<String, Object> variables) {
        Object a=valuesStack.pop();
        Complex aa=(Complex)a;
        return evaluate(aa);
    }
    
    public abstract Object evaluate(Complex a);
    

}
