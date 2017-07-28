/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.expeval.operators;

import net.vpc.scholar.hadrumaths.expeval.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author vpc
 */
public class OpListComma extends AbstractExpressionNode{
    public static class Uplet extends ArrayList<Object>{
        
    }
    public OpListComma() {
        super(",");
    }

    public Object evaluate(Stack<Object> valuesStack, Map<String, Object> variables) {
        Object b=valuesStack.pop();
        Object a=valuesStack.pop();
        if(a instanceof Uplet){
            ((Uplet)a).add(b);
            return a;
        }else{
            Uplet x=new Uplet();
            x.add(a);
            x.add(b);
            return x;
        }
    }

    
}
