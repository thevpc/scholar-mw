/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.expeval;

import net.vpc.scholar.hadrumaths.expeval.operators.OpListComma;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author vpc
 */
public abstract class ExpressionFunction extends AbstractExpressionNode {

    public ExpressionFunction(String name) {
        super(name);
    }

    public Object evaluate(Stack<Object> valuesStack, Map<String, Object> variables) {
        Object o = valuesStack.pop();
        if (!(o instanceof OpListComma.Uplet)) {
            OpListComma.Uplet oo = new OpListComma.Uplet();
            oo.add(o);
            return evaluate(oo, variables);
        } else {
            return evaluate((OpListComma.Uplet) o, variables);
        }
    }

    public abstract Object evaluate(OpListComma.Uplet params, Map<String, Object> variables);
}
