/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.expeval.operators;

import net.vpc.scholar.hadrumaths.Complex;

/**
 * @author vpc
 */
public class OpMultiply extends BinaryOp {

    public OpMultiply() {
        super("*");
    }

    @Override
    public Object evaluate(Complex aa, Complex bb) {
        return aa.mul(bb);
    }


}
