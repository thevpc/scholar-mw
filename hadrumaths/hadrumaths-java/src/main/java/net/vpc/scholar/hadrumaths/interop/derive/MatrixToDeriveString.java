/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.interop.derive;

import net.vpc.scholar.hadrumaths.Complex;

/**
 *
 * @author vpc
 */
public class MatrixToDeriveString implements ToDeriveString<Complex> {

    public MatrixToDeriveString() {
    }

    @Override
    public String toDeriveString(Complex o, ToDeriveStringParam... format) {
        return toString().replace('i','\u00EE');
    }
}
