/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.expeval.functions;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.expeval.ExpressionFunction;
import net.vpc.scholar.hadrumaths.expeval.operators.OpListComma.Uplet;

import java.util.Map;

/**
 * @author vpc
 */
public class FunctionReal extends ExpressionFunction {

    public FunctionReal() {
        super("real");
    }

    @Override
    public Object evaluate(Uplet params, Map<String, Object> variables) {
        Number n = (Number) params.get(0);
        if (n instanceof Complex) {
            return ((Complex) n).getReal();
        }
        return n.doubleValue();
    }


}