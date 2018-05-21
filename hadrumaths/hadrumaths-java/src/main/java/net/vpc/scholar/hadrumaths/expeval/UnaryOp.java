/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.expeval;

/**
 * @author vpc
 */
public abstract class UnaryOp extends AbstractExpressionNode {

    public UnaryOp(String name,Class type) {
        super(name,type);
    }

//    @Override
//    public Object evaluate(Stack<Object> valuesStack, ExpressionEvaluatorContext context) {
//        Object a = valuesStack.pop();
//        return evaluate(a);
//    }

    public Object evaluate(Object[] args, ExpressionEvaluatorContext context){
        return evaluate(args[0]);
    }

    public abstract Object evaluate(Object a);

    @Override
    public String getString(Object[] args) {
        return "("+getName()+"("+args[0]+"))";
    }
}
