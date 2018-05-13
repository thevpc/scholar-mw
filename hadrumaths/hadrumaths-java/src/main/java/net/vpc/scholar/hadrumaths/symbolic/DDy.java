package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.Expr;

import java.util.Arrays;
import java.util.List;

/**
* Created by IntelliJ IDEA.
* User: vpc
* Date: 29 juil. 2005
* Time: 20:33:56
* To change this template use File | Settings | File Templates.
*/
public class DDy extends AbstractDoubleToDouble implements Cloneable{
    private static final long serialVersionUID = 1L;
    DoubleToDouble base;
    double defaultX;
    double defaultZ;

    public DDy(DoubleToDouble base, double defaultX,double defaultZ) {
        super(Domain.forBounds(base.getDomain().ymin(), base.getDomain().ymax()));
        this.base = base;
        this.defaultZ = defaultZ;
        this.defaultX = defaultX;
    }

//    public double computeDouble0(double x) {
//        return base.computeDouble(defaultX, x,defaultZ);
//    }

//    public double[] computeDouble(double[] x, Domain d0, Out<Range> range) {
//        Out<Range> outRange = new Out<Range>();
//        double[] ret = base.computeDouble(defaultX, x, d0 == null ? null : Domain.forBounds(base.getDomain().xmin, base.getDomain().xmax, d0.xmin, d0.xmax), outRange);
//        if (range != null) {
//            range.set(Range.forBounds(outRange.get().xmin, outRange.get().xmax));
//        }
//        return ret;
//    }

    @Override
    protected double computeDouble0(double x, OutBoolean defined) {
        return base.computeDouble(defaultX,x,defaultZ,defined);
    }

    @Override
    protected double computeDouble0(double x, double y, OutBoolean defined) {
        return base.computeDouble(defaultX,x,defaultZ,defined);
    }

    @Override
    protected double computeDouble0(double x, double y, double z, OutBoolean defined) {
        return base.computeDouble(defaultX,x,defaultZ,defined);
    }

    public boolean isInvariantImpl(Axis axis) {
        switch (axis) {
            case X: {
                return base.isInvariant(Axis.Y);
            }
        }
        return true;
    }

    public boolean isZeroImpl() {
        return base.isZero();
    }

    public boolean isNaNImpl() {
        return base.isNaN();
    }

    public boolean isInfiniteImpl() {
        return base.isZero();
    }

//    public DDx multiply(double factor,DomainX newDomain) {
//        return new DDxyToDDy(base.multiply(factor,
//                newDomain==null?null:new DomainXY(base.getDomain().xmin, newDomain.xmin, base.getDomain().xmax, newDomain.xmax)),defaultX);
//    }
//
//    public DDx toXOpposite() {
//        return new DDxyToDDy(base.toXOpposite(),defaultX);
//    }
//
//    public DDx toSymmetric() {
//        return new DDxyToDDy(base.getSymmetricX(),defaultX);
//    }
//
//    public DDx translate(double xDelta, double yDelta) {
//        return new DDxyToDDy(base.translate(xDelta,yDelta),defaultX);
//    }

    public double getDefaultX() {
        return defaultX;
    }

    public void setDefaultX(double defaultX) {
        this.defaultX = defaultX;
    }

    public double getDefaultZ() {
        return defaultZ;
    }

    //    public DDx simplify() {
//        return new DDxyToDDy(base.simplify(),defaultX);
//    }

    public DoubleToDouble getArg() {
        return base;
    }

    public List<Expr> getSubExpressions() {
        return Arrays.asList(new Expr[]{base});
    }

    @Override
    public Expr setParam(String name, Expr value) {
        DoubleToDouble base = (DoubleToDouble) this.base.setParam(name, value);
        if (base != this.base) {
            Expr e = new DDy(base, defaultX,defaultZ);
            e= Any.copyProperties(this, e);
            return Any.updateTitleVars(e,name,value);
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DDy)) return false;
        if (!super.equals(o)) return false;

        DDy dDxyToDDy = (DDy) o;

        if (Double.compare(dDxyToDDy.defaultX, defaultX) != 0) return false;
        if (Double.compare(dDxyToDDy.defaultZ, defaultZ) != 0) return false;
        if (base != null ? !base.equals(dDxyToDDy.base) : dDxyToDDy.base != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + (base != null ? base.hashCode() : 0);
        temp = Double.doubleToLongBits(defaultX);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(defaultZ);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
