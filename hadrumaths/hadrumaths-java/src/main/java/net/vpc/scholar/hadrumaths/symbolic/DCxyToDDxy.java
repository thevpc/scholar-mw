/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author vpc
 */
public abstract class DCxyToDDxy extends AbstractDoubleToDouble {
    private static final long serialVersionUID = 1L;

    private DoubleToComplex base;

    public DCxyToDDxy(DoubleToComplex base) {
        super(base.getDomain());
        this.base = base;
    }

    public boolean isDCImpl() {
        return true;
    }

    public DoubleToComplex toDC() {
        return Maths.complex(this);
    }

    public boolean isDDImpl() {
        return true;
    }

    public DoubleToDouble toDD() {
        return this;
    }

//    public boolean isDDx() {
//        return false;
//    }

//    public IDDx toDDx() {
//        if (isDDx()) {
//            return new DDxyToDDx(this, getDomain().getCenterY());
//        }
//        throw new UnsupportedOperationException("Not supported.");
//    }

    public Domain getDomainImpl() {
        return base.getDomain();
    }

    @Override
    public boolean isScalarExprImpl() {
        return true;
    }

    @Override
    public boolean isInvariantImpl(Axis axis) {
        return base.isInvariant(axis);
    }

//    @Override
//    public boolean isDoubleExprImpl() {
//        return false;
//    }

    @Override
    public boolean isDVImpl() {
        return true;
    }

    public boolean isDMImpl() {
        return base.isDM();
    }

    public DoubleToMatrix toDM() {
        return toDC().toDM();
    }

    public boolean isZeroImpl() {
        return base.isZero();
    }

    public boolean isNaNImpl() {
        return base.isNaN();
    }

    public boolean isInfiniteImpl() {
        return base.isInfinite();
    }

    protected abstract double convertDouble(Complex d);

    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        Out<Range> r2 = ranges == null ? new Out<Range>() : ranges;
        Complex[][][] c = base.computeComplex(x, y, z, d0, r2);
        double[][][] r = new double[z.length][y.length][x.length];
        Range d = r2.get();
        if (d != null) {
            int ax = d.xmin;
            int bx = d.xmax;
            int cy = d.ymin;
            int dy = d.ymax;
            for (int i = d.zmin; i <= d.zmax; i++) {
                for (int j = cy; j <= dy; j++) {
                    for (int k = ax; k <= bx; k++) {
                        r[i][j][k] = convertDouble(c[i][j][k]);
                    }
                }
            }
        }
        return r;
    }


    public double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        Out<Range> r2 = ranges == null ? new Out<Range>() : ranges;
        Complex[][] c = base.computeComplex(x, y, d0, r2);
        double[][] r = new double[y.length][x.length];
        Range d = r2.get();
        if (d != null) {
            int ax = d.xmin;
            int bx = d.xmax;
            int cy = d.ymin;
            int dy = d.ymax;
            for (int j = cy; j <= dy; j++) {
                for (int k = ax; k <= bx; k++) {
                    r[j][k] = convertDouble(c[j][k]);
                }
            }
        }
        return r;
    }

    public double[] computeDouble(double[] x, Domain d0, Out<Range> ranges) {
        Out<Range> r2 = ranges == null ? new Out<Range>() : ranges;
        Complex[] c = base.computeComplex(x, d0, r2);
        double[] r = new double[x.length];
        Range d = r2.get();
        if (d != null) {
            int ax = d.xmin;
            int bx = d.xmax;
            int cy = d.ymin;
            int dy = d.ymax;
            for (int k = ax; k <= bx; k++) {
                r[k] = convertDouble(c[k]);
            }
        }
        return r;
    }

    public double[] computeDouble(double[] x, double y, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, x, y, d0, ranges);
    }

    public double[] computeDouble(double x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, x, y, d0, ranges);
    }

    public double computeDouble(double x, double y,BooleanMarker defined) {
        if (contains(x, y)) {
            return convertDouble(base.computeComplex(x, y,defined));
        }
        return 0;
    }

    public DoubleToComplex getArg() {
        return base;
    }

    public List<Expr> getSubExpressions() {
        return Arrays.asList(new Expr[]{base});
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DCxyToDDxy that = (DCxyToDDxy) o;

        if (base != null ? !base.equals(that.base) : that.base != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (base != null ? base.hashCode() : 0);
        return result;
    }


}
