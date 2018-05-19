package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.BooleanMarker;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.integration.DIntegralXY;
import net.vpc.scholar.hadrumaths.integration.DQuadIntegralXY;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: vpc Date: 29 juil. 2005 Time: 20:33:56 To
 * change this template use File | Settings | File Templates.
 */
public class DDyIntegralX extends AbstractDoubleToDouble implements Cloneable {

    private static final long serialVersionUID = 1L;
    private DoubleToDouble base;
    //    DFunctionXFromXY base0;
    private DIntegralXY integral;
    private double x0;
    private double x1;

    /**
     * f=integral2([x0,x],[y0,y] base(x,y).dx.dy)
     * f(y)=integral([x0,x], base(x,y).dx)
     * Let X=y
     * f(X)=integral([x0,x], base(x,X).dx)
     *
     * @param base
     * @param integral
     * @param x0
     */
    public DDyIntegralX(DoubleToDouble base, DIntegralXY integral, double x0, double x1) {
        super(Domain.forBounds(base.getDomain().ymin(), base.getDomain().ymax()));
        this.base = base;
//        this.base0 = new DFunctionXFromXY(FunctionFactory.simplify(base), 0);
        this.integral = integral;
        this.x0 = x0;
        this.x1 = x1;
    }

    public DoubleToDouble getBase() {
        return base;
    }

    public DIntegralXY getIntegralXY() {
        return integral;
    }

    public double getX0() {
        return x0;
    }

    public double getX1() {
        return x1;
    }

    public DDyIntegralX(DoubleToDouble base) {
        this(base, new DQuadIntegralXY(), base.getDomain().xmin(), base.getDomain().xmax());
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


    public boolean isInvariantImpl(Axis axis) {
        switch (axis) {
            case X: {
                return false;
            }
        }
        return getArg().isInvariant(axis);
    }


    @Override
    protected double computeDouble0(double x, BooleanMarker defined) {
        defined.set();
        return integral.integrateX(base, x, x0, x1);
    }

    @Override
    protected double computeDouble0(double x, double y, BooleanMarker defined) {
        defined.set();
        return integral.integrateX(base, x, x0, x1);
    }

    @Override
    protected double computeDouble0(double x, double y, double z, BooleanMarker defined) {
        defined.set();
        return integral.integrateX(base, x, x0, x1);
    }

    //    public DDx simplify() {
//        return new DFunctionYIntegralX(base.base.simplify(),integral,x0,x1);
//    }
    public DoubleToDouble getArg() {
        return base;
    }

    public List<Expr> getSubExpressions() {
        return Arrays.asList(new Expr[]{base});
    }

    @Override
    public Expr setParam(String name, Expr value) {
        DoubleToDouble last = this.base;
        DoubleToDouble updated = (DoubleToDouble) last.setParam(name, value);
        if (updated != last) {
            Expr e = new DDyIntegralX(updated, integral, x0, x1);
            e = Any.copyProperties(this, e);
            return Any.updateTitleVars(e, name, value);
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DDyIntegralX)) return false;
        if (!super.equals(o)) return false;

        DDyIntegralX that = (DDyIntegralX) o;

        if (Double.compare(that.x0, x0) != 0) return false;
        if (Double.compare(that.x1, x1) != 0) return false;
        if (base != null ? !base.equals(that.base) : that.base != null) return false;
        if (integral != null ? !integral.equals(that.integral) : that.integral != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + (base != null ? base.hashCode() : 0);
        result = 31 * result + (integral != null ? integral.hashCode() : 0);
        temp = Double.doubleToLongBits(x0);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(x1);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
