/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Out;

/**
 * @author vpc
 */
public interface DoubleToComplex extends Expr {


    Complex computeComplex(double x);

    Complex computeComplex(double x, double y);

    Complex computeComplex(double x, double y, double z);


    Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges);

    Complex[] computeComplex(double[] x, double y, Domain d0, Out<Range> ranges);

    Complex[] computeComplex(double x, double[] y, Domain d0, Out<Range> ranges);

    Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges);

    Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges);

    Complex[] computeComplex(double[] x, Domain d0);

    Complex[] computeComplex(double[] x, double y, Domain d0);

    Complex[] computeComplex(double x, double[] y, Domain d0);

    Complex[][] computeComplex(double[] x, double[] y, Domain d0);

    Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0);


    Complex[] computeComplex(double[] x);

    Complex[] computeComplex(double[] x, double y);

    Complex[] computeComplex(double x, double[] y);

    Complex[][] computeComplex(double[] x, double[] y);

    Complex[][][] computeComplex(double[] x, double[] y, double[] z);


    DoubleToDouble getReal();

    DoubleToDouble getImag();

//    public IDCxy setName(String name);
}
