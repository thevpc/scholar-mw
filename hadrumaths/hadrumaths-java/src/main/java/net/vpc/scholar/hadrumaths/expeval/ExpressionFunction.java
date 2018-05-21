/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.expeval;

/**
 * @author vpc
 */
public abstract class ExpressionFunction extends AbstractExpressionNode {

    public ExpressionFunction(String name,Class returnType) {
        super(name,returnType);
    }

//    public Object evaluate(Stack<Object> valuesStack, ExpressionEvaluatorContext context) {
//        Object o = valuesStack.pop();
//        if (!(o instanceof OpListComma.Uplet)) {
//            return evaluate(new Object[]{oo}, context);
//        } else {
//            return evaluate(((OpListComma.Uplet) o).toArray(), context);
//        }
//    }

    @Override
    public String getString(Object[] args) {
        StringBuilder sb=new StringBuilder(getName()).append("(");
        for (int i = 0; i < args.length; i++) {
            if(i>0){
                sb.append(",");
            }
            sb.append(args[i]);
        }
        sb.append(")");
        return sb.toString();
    }
}
