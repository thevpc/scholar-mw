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
public abstract class BinaryOp extends AbstractExpressionNode{
    
    public BinaryOp(String name) {
        super(name);
    }

    @Override
    public Object evaluate(Stack<Object> valuesStack, Map<String, Object> variables) {
        Object b=valuesStack.pop();
        Object a=valuesStack.pop();
        Complex aa=(Complex)a;
        Complex bb=(Complex)b;
        return evaluate(aa, bb);
    }
    
    public abstract Object evaluate(Complex a,Complex b);
    

}
