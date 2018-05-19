/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.expeval.functions;

import net.vpc.scholar.hadrumaths.expeval.ExpressionFunction;
import net.vpc.scholar.hadrumaths.expeval.operators.OpListComma.Uplet;

import java.util.Map;

/**
 * @author vpc
 */
public class FunctionToInt extends ExpressionFunction {

    public FunctionToInt() {
        super("toint");
    }

    @Override
    public Object evaluate(Uplet params, Map<String, Object> variables) {
        Number n = (Number) params.get(0);
        return n.intValue();
    }


}