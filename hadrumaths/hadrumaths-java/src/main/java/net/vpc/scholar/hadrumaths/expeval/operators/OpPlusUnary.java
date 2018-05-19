/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.expeval.operators;

import net.vpc.scholar.hadrumaths.Complex;

/**
 * @author vpc
 */
public class OpPlusUnary extends UnaryOp {

    public OpPlusUnary() {
        super("+U");
    }

    @Override
    public Object evaluate(Complex aa) {
        return aa;
    }


}
