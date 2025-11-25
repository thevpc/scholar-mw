package net.thevpc.scholar.hadrumaths.symbolic.double2double;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.BooleanMarker;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.integration.DIntegralXY;
import net.thevpc.scholar.hadrumaths.integration.DQuadIntegralXY;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.thevpc.scholar.hadrumaths.symbolic.ExprDefaults;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: vpc Date: 29 juil. 2005 Time: 20:33:56 To
 * change this template use File | Settings | File Templates.
 */
public class DDyIntegralX extends AbstractDoubleToDouble {

    private static final long serialVersionUID = 1L;
    private final DoubleToDouble base;
    //    DFunctionXFromXY base0;
    private final DIntegralXY integral;
    private final double x0;
    private final double x1;
    private final Domain domain;

    public DDyIntegralX(DoubleToDouble base) {
        this(base, new DQuadIntegralXY(), base.getDomain().xmin(), base.getDomain().xmax());
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("DDyIntegralX");
    }

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
        this.domain = Domain.ofBounds(base.getDomain().ymin(), base.getDomain().ymax());
        this.base = base;
//        this.base0 = new DFunctionXFromXY(FunctionFactory.simplify(base), 0);
        this.integral = integral;
        this.x0 = x0;
        this.x1 = x1;
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    public List<Expr> getChildren() {
        return Arrays.asList(new Expr[]{base});
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

    public boolean isInvariant(Axis axis) {
        switch (axis) {
            case X: {
                return false;
            }
        }
        return getArg().isInvariant(axis);
    }

    public boolean isZero() {
        return base.isZero();
    }

    public boolean isNaN() {
        return base.isNaN();
    }

    @Override
    public Expr setParam(String name, Expr value) {
        DoubleToDouble last = this.base;
        DoubleToDouble updated = (DoubleToDouble) last.setParam(name, value);
        if (updated != last) {
            Expr e = new DDyIntegralX(updated, integral, x0, x1);
            e = ExprDefaults.copyProperties(this, e);
            return ExprDefaults.updateTitleVars(e, name, value);
        }
        return this;
    }

    public boolean isInfinite() {
        return base.isInfinite();
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return new DDyIntegralX(subExpressions[0].toDD(), integral, x0, x1);
    }

    public DoubleToDouble getArg() {
        return base;
    }

    @Override
    public double evalDouble(double x, BooleanMarker defined) {
        defined.set();
        return integral.integrateX(base, x, x0, x1);
    }

    @Override
    public double evalDouble(double x, double y, BooleanMarker defined) {
        defined.set();
        return integral.integrateX(base, x, x0, x1);
    }

    @Override
    public double evalDouble(double x, double y, double z, BooleanMarker defined) {
        defined.set();
        return integral.integrateX(base, x, x0, x1);
    }

    @Override
    public int hashCode() {
        int result = getClass().getName().hashCode();
        long temp;
        result = 31 * result + (base != null ? base.hashCode() : 0);
        result = 31 * result + (integral != null ? integral.hashCode() : 0);
        temp = Double.doubleToLongBits(x0);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(x1);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DDyIntegralX)) return false;

        DDyIntegralX that = (DDyIntegralX) o;

        if (Double.compare(that.x0, x0) != 0) return false;
        if (Double.compare(that.x1, x1) != 0) return false;
        if (base != null ? !base.equals(that.base) : that.base != null) return false;
        return integral != null ? integral.equals(that.integral) : that.integral == null;
    }

    @Override
    public String toLatex() {
        throw new UnsupportedOperationException("Not Implemented toLatex for "+getClass().getName());
    }

}
