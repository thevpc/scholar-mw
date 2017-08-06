/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Out;

/**
 *
 * @author vpc
 */
public class Real extends DCxyToDDxy implements Cloneable{

    public Real(DoubleToComplex base) {
        super(base);
    }

    public boolean isInvariantImpl(Axis axis) {
        switch (axis) {
            case X:
            case Y: {
                return false;

            }
        }
        return true;
    }

    @Override
    protected double convertDouble(Complex d) {
        return d.getReal();
    }

    @Override
    protected double computeDouble0(double x) {
        return getArg().computeComplex(x).getReal();
    }


    @Override
    protected double computeDouble0(double x, double y) {
        return getArg().computeComplex(x,y).getReal();
    }

    @Override
    protected double computeDouble0(double x, double y, double z) {
        return getArg().computeComplex(x,y,z).getReal();
    }

//    public double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
//        double[][] r = new double[y.length][x.length];
//        Out<Range> r2 = new Out<Range>();
//        Complex[][] cc = getArg().computeComplex(x, y, d0, r2);
//        Range currRange = r2.get();
//        if (currRange != null) {
//            int ax = currRange.xmin;
//            int bx = currRange.xmax;
//            int cy = currRange.ymin;
//            int dy = currRange.ymax;
//            for (int xIndex = ax; xIndex <= bx; xIndex++) {
//                for (int yIndex = cy; yIndex <= dy; yIndex++) {
//                    r[yIndex][xIndex] = cc[yIndex][xIndex].getReal();
//                }
//            }
//            if (ranges != null) {
//                ranges.set(currRange);
//            }
//        }
//        return r;
//    }

//    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
//        double[][][] r = new double[z.length][y.length][x.length];
//        Out<Range> r2 = new Out<Range>();
//        Complex[][][] cc = getArg().computeComplex(x, y, z, d0, r2);
//        Range currRange = r2.get();
//        if (currRange != null) {
//            int ax = currRange.xmin;
//            int bx = currRange.xmax;
//            int cy = currRange.ymin;
//            int dy = currRange.ymax;
//            int z1 = currRange.zmin;
//            int z2 = currRange.zmax;
//            for (int zIndex = z1; zIndex <= z2; zIndex++) {
//                for (int xIndex = ax; xIndex <= bx; xIndex++) {
//                    for (int yIndex = cy; yIndex <= dy; yIndex++) {
//                        r[zIndex][yIndex][xIndex] = cc[zIndex][yIndex][xIndex].getReal();
//                    }
//                }
//            }
//            if (ranges != null) {
//                ranges.set(currRange);
//            }
//        }
//        return r;
//    }


    @Override
    public Expr setParam(String name, Expr value) {
        DoubleToComplex old = getArg();
        DoubleToComplex updated = old.setParam(name,value).toDC();
        if(updated!=old){
            Expr e = new Real(updated);
            e= Any.copyProperties(this, e);
            return Any.updateTitleVars(e,name,value);
        }
        return this;
    }

    @Override
    public String toString() {
        return "real("+ FormatFactory.format(getArg())+")";
    }

    @Override
    public boolean equals(Object o) {
        if(!super.equals(o)){
            return false;
        }
        return getArg().equals(((Real) o).getArg());
    }

    @Override
    public boolean isScalarExprImpl() {
        return getArg().isScalarExpr();
    }

    @Override
    public boolean isDoubleExprImpl() {
        return true;
    }

    @Override
    public double[] computeDouble(double[] x, Domain d0, Out<Range> range) {
        Complex[] v = getArg().computeComplex(x, d0, range);
        return ArrayUtils.getReal(v);
    }

}
