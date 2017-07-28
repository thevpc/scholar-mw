/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.expeval.operators;

import net.vpc.scholar.hadrumaths.Complex;

/**
 *
 * @author vpc
 */
public class OpDivide extends BinaryOp{

    public OpDivide() {
        super("/");
    }

    @Override
    public Object evaluate(Complex aa, Complex bb) {
        return aa.div(bb);
    }

    

}
