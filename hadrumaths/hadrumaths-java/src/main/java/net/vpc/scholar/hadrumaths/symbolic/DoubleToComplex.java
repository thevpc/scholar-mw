/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

/**
 * @author vpc
 */
public interface DoubleToComplex extends DoubleDomainExpr {


    default Complex computeComplex(double x) {
        return computeComplex(x, new OutBoolean());
    }

    default Complex computeComplex(double x, double y) {
        return computeComplex(x, y, new OutBoolean());
    }

    default Complex computeComplex(double x, double y, double z) {
        return computeComplex(x, y, z, new OutBoolean());
    }

    Complex computeComplex(double x, OutBoolean defined);

    Complex computeComplex(double x, double y, OutBoolean defined);

    Complex computeComplex(double x, double y, double z, OutBoolean defined);

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


    DoubleToDouble getRealDD();

    DoubleToDouble getImagDD();

//    public IDCxy setName(String name);
}
