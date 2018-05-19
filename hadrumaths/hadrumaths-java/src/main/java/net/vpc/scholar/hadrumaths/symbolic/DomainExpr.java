package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by vpc on 4/29/14.
 */
public class DomainExpr extends AbstractExprPropertyAware implements /*IDDx,*/DoubleToDouble, DoubleToComplex, DoubleToMatrix, DoubleToVector {
    private static final long serialVersionUID = 1L;
    private final int dimension;
    private final Expr xmin;
    private final Expr xmax;
    private final Expr ymin;
    private final Expr ymax;
    private final Expr zmin;
    private final Expr zmax;

    public static DomainExpr forBounds(Expr xmin, Expr xmax, Expr ymin, Expr ymax, Expr zmin, Expr zmax) {
        return new DomainExpr(
                xmin, xmax, ymin, ymax,
                zmin,
                zmax,
                3
        );
    }

    public static DomainExpr forBounds(Expr xmin, Expr xmax) {
        return new DomainExpr(
                xmin, xmax,
                Complex.NEGATIVE_INFINITY,
                Complex.POSITIVE_INFINITY,
                Complex.NEGATIVE_INFINITY,
                Complex.POSITIVE_INFINITY,
                1
        );
    }

    public static DomainExpr forBounds(Expr xmin, Expr xmax, Expr ymin, Expr ymax) {
        return new DomainExpr(
                xmin, xmax, ymin, ymax,
                Complex.NEGATIVE_INFINITY,
                Complex.POSITIVE_INFINITY,
                2
        );
    }

    protected DomainExpr(Expr xmin, Expr xmax, Expr ymin, Expr ymax, Expr zmin, Expr zmax, int dimension) {
        this.dimension = dimension;
        ComponentDimension cd = xmin.getComponentDimension().expand(xmax.getComponentDimension())
                .expand(ymin.getComponentDimension()).expand(ymax.getComponentDimension())
                .expand(zmin.getComponentDimension()).expand(zmax.getComponentDimension());
        this.xmin = Maths.expandComponentDimension(xmin, cd);
        this.xmax = Maths.expandComponentDimension(xmax, cd);
        this.ymin = Maths.expandComponentDimension(ymin, cd);
        this.ymax = Maths.expandComponentDimension(ymax, cd);
        this.zmin = Maths.expandComponentDimension(zmin, cd);
        this.zmax = Maths.expandComponentDimension(zmax, cd);
        check(xmin);
        check(xmax);
        check(ymin);
        check(ymax);
        check(zmin);
        check(zmax);
    }

    private static void check(Expr e) {
        if (!e.isInvariant(Axis.X)) {
            throw new IllegalArgumentException("X not Allowed in Domain definition : " + e);
        }
        if (!e.isInvariant(Axis.Y)) {
            throw new IllegalArgumentException("Y not Allowed in Domain definition: " + e);
        }
        if (!e.isInvariant(Axis.Z)) {
            throw new IllegalArgumentException("Y not Allowed in Domain definition: " + e);
        }
        if (e.getDomain().getDomainDimension() != 1) {
            throw new IllegalArgumentException("Domain Exp value should not be a domain it self " + e);
        }
        if (!e.getDomain().isUnconstrained()) {
            throw new IllegalArgumentException("Domain Exp value should not be a domain it self " + e);
        }
    }


    public Expr getXmin() {
        return xmin;
    }

    public Expr getXmax() {
        return xmax;
    }

    public Expr getYmin() {
        return ymin;
    }

    public Expr getYmax() {
        return ymax;
    }

    public Expr getZmin() {
        return zmin;
    }

    public Expr getZmax() {
        return zmax;
    }

    public Expr xmin() {
        return xmin;
    }

    public Expr xmax() {
        return xmax;
    }

    public Expr ymin() {
        return ymin;
    }

    public Expr ymax() {
        return ymax;
    }

    public Expr zmin() {
        return zmin;
    }

    public Expr zmax() {
        return zmax;
    }

    @Override
    public boolean isInvariantImpl(Axis axis) {
        return true;
    }

    @Override
    public boolean isZeroImpl() {
        return false;
    }

    @Override
    public boolean isNaNImpl() {
        return false;
    }

    @Override
    public boolean isInfiniteImpl() {
        return false;
    }

    @Override
    public Expr clone() {
        return (Expr) super.clone();
    }

    @Override
    public List<Expr> getSubExpressions() {
        return new ArrayList<>(Arrays.asList(xmin, xmax, ymin, ymax, zmin, zmax));
    }

    @Override
    public DoubleToComplex toDC() {
        return this;
//        throw new IllegalArgumentException("expr domain "+getName()+" could not be evaluated");
    }

    @Override
    public DoubleToDouble toDD() {
        return this;
//        throw new IllegalArgumentException("expr domain "+getName()+" could not be evaluated");
    }

    //    @Override
//    public IDDx toDDx() {
//        return this;
////        throw new IllegalArgumentException("expr domain "+getName()+" could not be evaluated");
//    }
    @Override
    public DoubleToMatrix toDM() {
        return this;
        //throw new IllegalArgumentException("expr domain "+getName()+" could not be evaluated");
    }

    @Override
    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("expr domain " + getTitle() + " could not be evaluated");
    }

    @Override
    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("expr domain " + getTitle() + " could not be evaluated");
    }

    @Override
    public Complex[] computeComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("expr domain " + getTitle() + " could not be evaluated");
    }

    @Override
    public Complex[] computeComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("expr domain " + getTitle() + " could not be evaluated");
    }

    @Override
    public double[] computeDouble(double[] x, Domain d0, Out<Range> range) {
        throw new IllegalArgumentException("expr domain " + getTitle() + " could not be evaluated");
    }

//    @Override
//    public double computeDouble(double x) {
//        throw new IllegalArgumentException("expr domain " + getTitle() + " could not be evaluated");
//    }

    @Override
    public double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public double[] computeDouble(double[] x, double y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public double[] computeDouble(double x, double[] y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public double computeDouble(double x, BooleanMarker defined) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public double computeDouble(double x, double y, BooleanMarker defined) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public double computeDouble(double x, double y, double z, BooleanMarker defined) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public Complex computeComplex(double x, BooleanMarker defined) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public Complex computeComplex(double x, double y, BooleanMarker defined) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public Complex computeComplex(double x, double y, double z, BooleanMarker defined) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }


    @Override
    public Matrix[][] computeMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public Matrix[] computeMatrix(double[] x, double y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public Matrix[] computeMatrix(double x, double[] y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public Matrix computeMatrix(double x, double y) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public Matrix computeMatrix(double x, double y, double z) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

//    @Override
//    public Complex computeComplex(double x, double y, double z) {
//        throw new IllegalArgumentException("expr domain " + getTitle() + " could not be evaluated");
//    }

//    @Override
//    public double computeDouble(double x, double y, double z) {
//        throw new IllegalArgumentException("expr domain " + getTitle() + " could not be evaluated");
//    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public Matrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public Matrix[] computeMatrix(double[] x, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public Matrix computeMatrix(double x) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }


    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public Complex[][] computeComplex(double[] x, double[] y) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public Complex[] computeComplex(double[] x) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public double[] computeDouble(double[] x) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public double[][] computeDouble(double[] x, double[] y) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public double[] computeDouble(double x, double[] y) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public double[][][] computeDouble(double[] x, double[] y, double[] z) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public Matrix[][][] computeMatrix(double[] x, double[] y, double[] z) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public Matrix[][] computeMatrix(double[] x, double[] y) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public Matrix[] computeMatrix(double[] x) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    public String dump() {
        switch (dimension) {
            case 1: {
                Dumper h = new Dumper("Domain", Dumper.Type.SIMPLE);
                h.add("x", xmin + "->" + xmax);
                return h.toString();
            }
            case 2: {
                Dumper h = new Dumper("Domain", Dumper.Type.SIMPLE);
                h.add("x", xmin + "->" + xmax);
                h.add("y", ymin + "->" + ymax);
                return h.toString();
            }
        }
        Dumper h = new Dumper("Domain", Dumper.Type.SIMPLE);
        h.add("x", xmin + "->" + xmax);
        h.add("y", ymin + "->" + ymax);
        h.add("z", zmin + "->" + zmax);
        return h.toString();
    }

//    @Override
//    public String toString() {
//        return dump();
//    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof DomainExpr)) return false;
//        if (!(o.getClass().equals(getClass()))) return false;
//
//        DomainExpr paramExpr = (DomainExpr) o;
//
//        if (name != null ? !name.equals(paramExpr.name) : paramExpr.name != null) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        return name != null ? name.hashCode() : 0;
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainExpr)) return false;
        if (!super.equals(o)) return false;

        DomainExpr that = (DomainExpr) o;

        if (dimension != that.dimension) return false;
        if (xmax != null ? !xmax.equals(that.xmax) : that.xmax != null) return false;
        if (xmin != null ? !xmin.equals(that.xmin) : that.xmin != null) return false;
        if (ymax != null ? !ymax.equals(that.ymax) : that.ymax != null) return false;
        if (ymin != null ? !ymin.equals(that.ymin) : that.ymin != null) return false;
        if (zmax != null ? !zmax.equals(that.zmax) : that.zmax != null) return false;
        if (zmin != null ? !zmin.equals(that.zmin) : that.zmin != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + dimension;
        result = 31 * result + (xmin != null ? xmin.hashCode() : 0);
        result = 31 * result + (xmax != null ? xmax.hashCode() : 0);
        result = 31 * result + (ymin != null ? ymin.hashCode() : 0);
        result = 31 * result + (ymax != null ? ymax.hashCode() : 0);
        result = 31 * result + (zmin != null ? zmin.hashCode() : 0);
        result = 31 * result + (zmax != null ? zmax.hashCode() : 0);
        return result;
    }

    @Override
    public Expr composeX(Expr xreplacement) {
        return new DomainExpr(

                xmin.composeX(xreplacement),
                xmax.composeX(xreplacement),
                ymin.composeX(xreplacement),
                ymax.composeX(xreplacement),
                zmin.composeX(xreplacement),
                zmax.composeX(xreplacement),
                dimension
        );
    }

    @Override
    public Expr composeY(Expr yreplacement) {
        return new DomainExpr(
                xmin.composeY(yreplacement),
                xmax.composeY(yreplacement),
                ymin.composeY(yreplacement),
                ymax.composeY(yreplacement),
                zmin.composeY(yreplacement),
                zmax.composeY(yreplacement),
                dimension
        );
    }

    public int getDimension() {
        return dimension;
    }

    @Override
    public int getDomainDimension() {
        return dimension;
    }

    public ComponentDimension getComponentDimension() {
        return this.xmin.getComponentDimension();
    }

    @Override
    public Expr getComponent(int row, int col) {
        if (getComponentDimension().equals(ComponentDimension.SCALAR)) {
            if (row == 0 && col == 0) {
                return this;
            }
            return FunctionFactory.DZEROXY;
        }
        return new DomainExpr(
                getXmin().toDM().getComponent(row, col),
                getXmax().toDM().getComponent(row, col),
                getYmin().toDM().getComponent(row, col),
                getYmax().toDM().getComponent(row, col),
                getZmin().toDM().getComponent(row, col),
                getZmax().toDM().getComponent(row, col),
                dimension
        );
    }

    @Override
    public DoubleToComplex getComponent(Axis a) {
        if (a == Axis.X) {
            return this;
        }
        return FunctionFactory.CZEROXY;
    }

    @Override
    public Domain getDomainImpl() {
        return Domain.FULL(dimension);
    }


    @Override
    public String getComponentTitle(int row, int col) {
        return null;
    }


    @Override
    public DoubleToDouble getRealDD() {
        throw new IllegalArgumentException("expr domain " + getTitle() + " could not be evaluated");
//        return this;
    }

    @Override
    public DoubleToDouble getImagDD() {
        throw new IllegalArgumentException("expr domain " + getTitle() + " could not be evaluated");
//        return FunctionFactory.DZEROXY;
    }


    @Override
    public boolean isScalarExprImpl() {
        return true;
    }

    @Override
    public boolean isDoubleExprImpl() {
        return true;
    }

    @Override
    public boolean isDVImpl() {
        return true;
    }

    @Override
    public DoubleToVector toDV() {
        return this;
    }

    @Override
    public boolean hasParamsImpl() {
        return
                xmin.hasParams() || xmax.hasParams()
                        || ymin.hasParams() || ymax.hasParams()
                        || zmin.hasParams() || zmax.hasParams()
                ;
    }

    @Override
    protected boolean isDDImpl() {
        return
                xmin.isDD() && xmax.isDD()
                        && ymin.isDD() && ymax.isDD()
                        && zmin.isDD() && zmax.isDD()

                ;
    }

    @Override
    protected boolean isDCImpl() {
        return
                xmin.isDC() && xmax.isDC()
                        && ymin.isDC() && ymax.isDC()
                        && zmin.isDC() && zmax.isDC()

                ;
    }

    @Override
    protected boolean isDMImpl() {
        return
                xmin.isDM() && xmax.isDM()
                        && ymin.isDM() && ymax.isDM()
                        && zmin.isDM() && zmax.isDM()

                ;
    }

    @Override
    public int getComponentSize() {
        return 1;
    }

    @Override
    public Expr setParam(String name, Expr value) {
        Expr xmin_ = this.xmin.setParam(name, value);
        Expr xmax_ = this.xmax.setParam(name, value);
        Expr ymin_ = this.ymin.setParam(name, value);
        Expr ymax_ = this.ymax.setParam(name, value);
        Expr zmin_ = this.zmin.setParam(name, value);
        Expr zmax_ = this.zmax.setParam(name, value);
        if (xmin_ != xmin || xmax_ != xmax
                || ymin_ != ymin || ymax_ != ymax
                || zmin_ != zmin || zmax_ != zmax
                ) {
            return new DomainExpr(
                    xmin_,
                    xmax_,
                    ymin_,
                    ymax_,
                    zmin_,
                    zmax_,
                    dimension
            );
        } else {
            return this;
        }
    }


    @Override
    public Complex[] computeComplex(double[] x, Domain d0) {
        return computeComplex(x, d0, null);
    }

    @Override
    public Complex[] computeComplex(double[] x, double y, Domain d0) {
        return computeComplex(x, y, d0, null);
    }

    @Override
    public Complex[] computeComplex(double x, double[] y, Domain d0) {
        return computeComplex(x, y, d0, null);
    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0) {
        return computeComplex(x, y, z, d0, null);
    }

    @Override
    public Complex[] computeComplex(double x, double[] y) {
        return computeComplex(x, y, (Domain) null, null);
    }

    @Override
    public Complex[][] computeComplex(double[] x, double[] y, Domain d0) {
        return computeComplex(x, y, d0, null);
    }

    @Override
    public Complex[] computeComplex(double[] x, double y) {
        return computeComplex(x, y, (Domain) null, null);
    }

    @Override
    public Expr getX() {
        return getComponent(Axis.X);
    }

    @Override
    public Expr getY() {
        return getComponent(Axis.Y);
    }

    @Override
    public Expr getZ() {
        return getComponent(Axis.Z);
    }

    @Override
    public boolean isDoubleTyped() {
        return true;
    }
}
