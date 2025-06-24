package net.thevpc.scholar.hadrumaths.symbolic;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.AbstractDoubleToDouble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by vpc on 4/29/14.
 */
public class DomainExpr extends AbstractDoubleToDouble /*implements DoubleToDouble*/ /*IDDx,*/ {
    private static final long serialVersionUID = 1L;
    private final Domain domain;
    private final Expr xmin;
    private final Expr xmax;
    private final Expr ymin;
    private final Expr ymax;
    private final Expr zmin;
    private final Expr zmax;

    protected DomainExpr(Expr xmin, Expr xmax, Expr ymin, Expr ymax, Expr zmin, Expr zmax, int dimension) {
        this.domain = Domain.FULL(dimension);
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
        if (e.getDomain().getDomain().getDimension() != 1) {
            throw new IllegalArgumentException("Domain Exp value should not be a domain it self " + e);
        }
        if (!e.isZero() && (!e.getDomain().isUnbounded())) {
            throw new IllegalArgumentException("Domain Exp value should not be a domain it self " + e);
        }
    }

    public static DomainExpr ofBounds(Expr xmin, Expr xmax, Expr ymin, Expr ymax, Expr zmin, Expr zmax) {
        return new DomainExpr(
                xmin, xmax, ymin, ymax,
                zmin,
                zmax,
                3
        );
    }

    public static DomainExpr ofBounds(Expr xmin, Expr xmax) {
        return new DomainExpr(
                xmin, xmax,
                Complex.NEGATIVE_INFINITY,
                Complex.POSITIVE_INFINITY,
                Complex.NEGATIVE_INFINITY,
                Complex.POSITIVE_INFINITY,
                1
        );
    }

    public static DomainExpr ofBounds(Expr xmin, Expr xmax, Expr ymin, Expr ymax) {
        return new DomainExpr(
                xmin, xmax, ymin, ymax,
                Complex.NEGATIVE_INFINITY,
                Complex.POSITIVE_INFINITY,
                2
        );
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

    @Override
    public boolean isInvariant(Axis axis) {
        return false;
    }

    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public boolean isNaN() {
        return false;
    }

    @Override
    public boolean hasParams() {
        return
                xmin.hasParams() || xmax.hasParams()
                        || ymin.hasParams() || ymax.hasParams()
                        || zmin.hasParams() || zmax.hasParams()
                ;
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
                    getDimension()
            );
        } else {
            return this;
        }
    }

    @Override
    public boolean isInfinite() {
        return false;
    }

    @Override
    public boolean isEvaluatable() {
        return false;
    }

    @Override
    public Expr compose(Expr xreplacement, Expr yreplacement, Expr zreplacement) {
        return new DomainExpr(

                xmin.compose(xreplacement, yreplacement, zreplacement),
                xmax.compose(xreplacement, yreplacement, zreplacement),
                ymin.compose(xreplacement, yreplacement, zreplacement),
                ymax.compose(xreplacement, yreplacement, zreplacement),
                zmin.compose(xreplacement, yreplacement, zreplacement),
                zmax.compose(xreplacement, yreplacement, zreplacement),
                getDimension()
        );
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        switch (getDimension()) {
            case 1: {
                return Tson.ofUplet("domain", context.elem(xmin()), context.elem(xmax())).build();
            }
            case 2: {
                return Tson.ofUplet("domain",
                        context.elem(xmin()), context.elem(xmax()),
                        context.elem(ymin()), context.elem(ymax())
                ).build();
            }
        }
        return Tson.ofUplet("domain",
                context.elem(xmin()), context.elem(xmax()),
                context.elem(ymin()), context.elem(ymax()),
                context.elem(zmin()), context.elem(zmax())
        ).build();
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return new DomainExpr(
                subExpressions[0], subExpressions[1]
                , subExpressions[2], subExpressions[3]
                , subExpressions[4], subExpressions[5],
                this.domain.getDimension()
        );
    }

//    @Override
//    public Expr clone() {
//        return (Expr) super.clone();
//    }

    public int getDimension() {
        return getDomain().getDimension();
    }

    public Expr xmin() {
        return xmin;
    }


    public Expr xmax() {
        return xmax;
    }

    //    @Override
//    public IDDx toDDx() {
//        return this;
////        throw new IllegalArgumentException("expr domain "+getName()+" could not be evaluated");
//    }
//    @Override
//    public DoubleToMatrix toDM() {
//        return new DC2DM(this);
//        //throw new IllegalArgumentException("expr domain "+getName()+" could not be evaluated");
//    }
//
//    @Override
//    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
//        throw new IllegalArgumentException("expr domain " + getTitle() + " could not be evaluated");
//    }
//
//    @Override
//    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
//        throw new IllegalArgumentException("expr domain " + getTitle() + " could not be evaluated");
//    }
//
//    @Override
//    public Complex[] computeComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
//        throw new IllegalArgumentException("expr domain " + getTitle() + " could not be evaluated");
//    }
//
//    @Override
//    public Complex[] computeComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
//        throw new IllegalArgumentException("expr domain " + getTitle() + " could not be evaluated");
//    }

    public Expr ymin() {
        return ymin;
    }

//    @Override
//    public double computeDouble(double x) {
//        throw new IllegalArgumentException("expr domain " + getTitle() + " could not be evaluated");
//    }

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
    public List<Expr> getChildren() {
        return new ArrayList<>(Arrays.asList(xmin, xmax, ymin, ymax, zmin, zmax));
    }

    public ComponentDimension getComponentDimension() {
        return this.xmin.getComponentDimension();
    }

    @Override
    public Domain getDomain() {
        return domain;
    }
//    public String dump() {
//        switch (dimension) {
//            case 1: {
//                Dumper h = new Dumper("Domain", Dumper.Type.SIMPLE);
//                h.add("x", xmin + "->" + xmax);
//                return h.toString();
//            }
//            case 2: {
//                Dumper h = new Dumper("Domain", Dumper.Type.SIMPLE);
//                h.add("x", xmin + "->" + xmax);
//                h.add("y", ymin + "->" + ymax);
//                return h.toString();
//            }
//        }
//        Dumper h = new Dumper("Domain", Dumper.Type.SIMPLE);
//        h.add("x", xmin + "->" + xmax);
//        h.add("y", ymin + "->" + ymax);
//        h.add("z", zmin + "->" + zmax);
//        return h.toString();
//    }

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
    public double evalDouble(double x, BooleanMarker defined) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public double evalDouble(double x, double y, BooleanMarker defined) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public double evalDouble(double x, double y, double z, BooleanMarker defined) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public double[][][] evalDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public double[][] evalDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

//    @Override
//    public Expr getComponent(int row, int col) {
//        if (getComponentDimension().equals(ComponentDimension.SCALAR)) {
//            if (row == 0 && col == 0) {
//                return this;
//            }
//            return FunctionFactory.DZEROXY;
//        }
//        return new DomainExpr(
//                getXmin().toDM().getComponent(row, col),
//                getXmax().toDM().getComponent(row, col),
//                getYmin().toDM().getComponent(row, col),
//                getYmax().toDM().getComponent(row, col),
//                getZmin().toDM().getComponent(row, col),
//                getZmax().toDM().getComponent(row, col),
//                dimension
//        );
//    }
//
//    @Override
//    public DoubleToComplex getComponent(Axis a) {
//        if (a == Axis.X) {
//            return this;
//        }
//        return FunctionFactory.CZEROXY;
//    }

    @Override
    public double[] evalDouble(double[] x, Domain d0, Out<Range> range) {
        throw new IllegalArgumentException("expr domain " + getTitle() + " could not be evaluated");
    }

    @Override
    public double[] evalDouble(double[] x, double y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }

    @Override
    public double[] evalDouble(double x, double[] y, Domain d0, Out<Range> ranges) {
        throw new IllegalArgumentException("expr domain " + toString() + " could not be evaluated");
    }


//    @Override
//    public int getComponentSize() {
//        return 1;
//    }

    @Override
    public int hashCode() {
        int result = getClass().getName().hashCode();
        result = 31 * result + domain.getDimension(); //only dimension matters
        result = 31 * result + (xmin != null ? xmin.hashCode() : 0);
        result = 31 * result + (xmax != null ? xmax.hashCode() : 0);
        result = 31 * result + (ymin != null ? ymin.hashCode() : 0);
        result = 31 * result + (ymax != null ? ymax.hashCode() : 0);
        result = 31 * result + (zmin != null ? zmin.hashCode() : 0);
        result = 31 * result + (zmax != null ? zmax.hashCode() : 0);
        return result;
    }

//    @Override
//    public boolean isDoubleTyped() {
//        return true;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainExpr)) return false;

        DomainExpr that = (DomainExpr) o;

        if (domain.getDimension() != that.domain.getDimension()) return false; //only dimension matters
        if (xmax != null ? !xmax.equals(that.xmax) : that.xmax != null) return false;
        if (xmin != null ? !xmin.equals(that.xmin) : that.xmin != null) return false;
        if (ymax != null ? !ymax.equals(that.ymax) : that.ymax != null) return false;
        if (ymin != null ? !ymin.equals(that.ymin) : that.ymin != null) return false;
        if (zmax != null ? !zmax.equals(that.zmax) : that.zmax != null) return false;
        return zmin != null ? zmin.equals(that.zmin) : that.zmin == null;
    }

    @Override
    public String toLatex() {
        switch (this.domain.dimension()){
            case 1:{
                return "\\prod\\left("+(xmin()).toLatex()+"\\rightarrow "+(xmax()).toLatex()+"\\right)";
            }
            case 2:{
                return "\\prod\\left("
                        +(xmin()).toLatex()+"\\rightarrow "+(xmax()).toLatex()
                        +","+(ymin()).toLatex()+"\\rightarrow "+(ymax()).toLatex()
                        +"\\right)";
            }
            case 3:{
                return "\\prod\\left("
                        +(xmin()).toLatex()+"\\rightarrow "+(xmax()).toLatex()
                        +","+(ymin()).toLatex()+"\\rightarrow "+(ymax()).toLatex()
                        +","+(zmin()).toLatex()+"\\rightarrow "+(zmax()).toLatex()
                        +"\\right)";
            }
        }
        return toString();
    }

}
