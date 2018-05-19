/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.expeval.functions;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.expeval.ExpressionFunction;
import net.vpc.scholar.hadrumaths.expeval.operators.OpListComma.Uplet;

import java.util.Map;

/**
 * @author vpc
 */
public class FunctionCos extends ExpressionFunction {

    public FunctionCos() {
        super("cos");
    }

    @Override
    public Object evaluate(Uplet params, Map<String, Object> variables) {
        Number n = (Number) params.get(0);
        if (n instanceof Complex) {
            return ((Complex) n).cos();
        } else {
            return Maths.cos2(n.doubleValue());
        }
    }


}
