/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.expeval.functions;

import net.vpc.scholar.hadrumaths.expeval.*;
import java.util.Map;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.expeval.operators.OpListComma.Uplet;

/**
 *
 * @author vpc
 */
public class FunctionImag extends ExpressionFunction{

    public FunctionImag() {
        super("imag");
    }

    @Override
    public Object evaluate(Uplet params, Map<String, Object> variables) {
        Number n=(Number)params.get(0);
        if(n instanceof Complex){
            return ((Complex)n).getImag();
        }
        return n.doubleValue();
    }


}