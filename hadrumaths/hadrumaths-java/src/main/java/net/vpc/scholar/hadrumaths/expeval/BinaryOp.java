/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.expeval;

/**
 * @author vpc
 */
public abstract class BinaryOp extends AbstractExpressionNode {

    public BinaryOp(String name,Class resultType) {
        super(name,resultType);
    }

//    @Override
//    public Object evaluate(Stack<Object> valuesStack, ExpressionEvaluatorContext context) {
//        Object b = valuesStack.pop();
//        Object a = valuesStack.pop();
//        return evaluate(a, b);
//    }

    public Object evaluate(Object[] args, ExpressionEvaluatorContext context){
        return evaluate(args[0], args[1]);
    }

    public abstract Object evaluate(Object a, Object b);

    @Override
    public String getString(Object[] args) {
        return "(("+args[0]+")"+getName()+"("+args[1]+"))";
    }
}
