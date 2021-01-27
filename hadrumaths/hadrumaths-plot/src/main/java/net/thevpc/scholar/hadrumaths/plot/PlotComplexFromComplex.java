/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.plot;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadruplot.PlotComplex;

/**
 *
 * @author vpc
 */
public class PlotComplexFromComplex implements PlotComplex {

    private Complex value;

    public PlotComplexFromComplex(Number d) {
        if (d instanceof Complex) {
            this.value = (Complex) d;
        } else {
            this.value = Complex.of(((Number) d).doubleValue());
        }
    }

    public PlotComplexFromComplex(Complex d) {
        this.value = d;
    }

    @Override
    public double getReal() {
        return value.getReal();
    }

    @Override
    public double getImag() {
        return value.getImag();
    }

}
