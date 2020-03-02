package net.vpc.scholar.hadrumaths.symbolic.double2double;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.integration.DIntegralXY;
import net.vpc.scholar.hadrumaths.integration.DQuadIntegralXY;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.ExprDefaults;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: vpc Date: 29 juil. 2005 Time: 20:33:56 To
 * change this template use File | Settings | File Templates.
 */
public class DDzIntegralXY extends AbstractDoubleToDouble {

    private static final long serialVersionUID = 1L;
    private final DoubleToDouble base;
    private final Domain domain;
    //    DFunctionXFromXY base0;
    private final DIntegralXY integral;
    private final double x0;
    private final double x1;
    private final double y0;
    private final double y1;

    public DDzIntegralXY(DoubleToDouble base) {
        this(base, new DQuadIntegralXY(),
                base.getDomain().xmin(), base.getDomain().xmax(),
                base.getDomain().ymin(), base.getDomain().ymax()
        );
    }

    /**
     * f(x)=integral([x0,x], base(x).dx)
     *
     * @param base
     * @param integral
     * @param x0
     */
    public DDzIntegralXY(DoubleToDouble base, DIntegralXY integral, double x0, double x1, double y0, double y1) {
        this.domain = Domain.ofBounds(base.getDomain().zmin(), base.getDomain().zmax());
        this.base = base;
//        this.base0 = new DFunctionXFromXY(FunctionFactory.simplify(base), 0);
        this.integral = integral;
        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    public List<Expr> getChildren() {
        return Arrays.asList(new Expr[]{base});
    }

    public DIntegralXY getIntegral() {
        return integral;
    }

    public double getX0() {
        return x0;
    }

    public double getX1() {
        return x1;
    }

    public double getY0() {
        return y0;
    }

    public double getY1() {
        return y1;
    }

    public boolean isInvariant(Axis axis) {
        switch (axis) {
            case X: {
                return false;
            }
        }
        return true;
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
            Expr e = new DDzIntegralXY(updated, integral, x0, x1, y0, y1);
            e = ExprDefaults.copyProperties(this, e);
            return ExprDefaults.updateTitleVars(e, name, value);
        }
        return this;
    }

    public boolean isInfinite() {
        return base.isInfinite();
    }    @Override
    public double evalDouble(final double z, BooleanMarker defined) {
        defined.set();
        DoubleToDouble basez=base.compose(null,null, Maths.expr(z)).toDD();
//        FixedAxisZFunction basez = new FixedAxisZFunction(base, z);
        return integral.integrateXY(basez, x0, x1, y0, y1);
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return new DDzIntegralXY(subExpressions[0].toDD(), integral, x0, x1, y0, y1);
    }    @Override
    public double evalDouble(double x, double y, BooleanMarker defined) {
        return evalDouble(x, defined);
    }

    public DoubleToDouble getArg() {
        return base;
    }    @Override
    public double evalDouble(double x, double y, double z, BooleanMarker defined) {
        return evalDouble(x, defined);
    }

    @Override
    public int hashCode() {
        int result = getClass().getName().hashCode();
        long temp;
        result = 31 * result + base.hashCode();
        result = 31 * result + integral.hashCode();
        result = 31 * result + Double.hashCode(x0);
        result = 31 * result + Double.hashCode(x1);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DDzIntegralXY)) return false;
        DDzIntegralXY that = (DDzIntegralXY) o;

        if (Double.compare(that.x0, x0) != 0) return false;
        if (Double.compare(that.x1, x1) != 0) return false;
        if (base != null ? !base.equals(that.base) : that.base != null) return false;
        return integral != null ? integral.equals(that.integral) : that.integral == null;
    }







}
