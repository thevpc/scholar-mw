package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

import java.util.Collections;
import java.util.List;

/**
 * User: taha Date: 2 juil. 2003 Time: 14:29:58
 */
public final class DoubleValue extends AbstractExpBase implements Cloneable, IConstantValue, DoubleToDouble {

    public static final DoubleValue ZERO = new DoubleValue(0, Domain.EMPTYXY);

    public static final DoubleValue ZERO1 = new DoubleValue(0, Domain.EMPTYX);
    public static final DoubleValue ZERO2 = new DoubleValue(0, Domain.EMPTYXY);
    public static final DoubleValue ZERO3 = new DoubleValue(0, Domain.EMPTYXYZ);

    public static final DoubleValue ONE1 = new DoubleValue(1, Domain.FULLX);
    public static final DoubleValue ONE2 = new DoubleValue(1, Domain.FULLXY);
    public static final DoubleValue ONE3 = new DoubleValue(1, Domain.FULLXYZ);

    public static final DoubleValue TWO1 = new DoubleValue(2, Domain.FULLX);
    public static final DoubleValue TWO2 = new DoubleValue(2, Domain.FULLXY);
    public static final DoubleValue TWO3 = new DoubleValue(2, Domain.FULLXYZ);

    public static final DoubleValue NAN1 = new DoubleValue(Double.NaN, Domain.FULLX);
    public static final DoubleValue NAN2 = new DoubleValue(Double.NaN, Domain.FULLXY);
    public static final DoubleValue NAN3 = new DoubleValue(Double.NaN, Domain.FULLXYZ);

    private static final long serialVersionUID = 1L;
    //    public static final int CODE = 1;
    public double value;
    protected Domain domain;

    public DoubleValue(Domain domain) {
        this(
                domain.dimension() == 1 ? domain.xwidth() : domain.dimension() == 2 ? Maths.sqrt(domain.xwidth() * domain.ywidth()) : Maths.sqrt(domain.xwidth() * domain.ywidth() * domain.zwidth()), domain);
    }

    public DoubleValue(double cst, Domain domain) {
        this.value = cst;
        this.domain = cst == 0 ? (domain == null ? Domain.FULL(1) : Domain.ZERO(domain.dimension())) : domain == null ? Domain.FULL(1) : domain;
    }

    public static DoubleValue valueOf(double cst) {
        if (cst == 0) {
            return ZERO1;
        } else if (cst == 1) {
            return ONE1;
        } else if (cst != cst) {
            return NAN1;
        }
        return new DoubleValue(cst, Domain.FULLX);
    }

    public static DoubleValue valueOf(double cst, Domain domain) {
        if (cst == 0) {
            switch (domain.getDimension()) {
                case 1:
                    return ZERO1;
                case 2:
                    return ZERO2;
                case 3:
                    return ZERO3;
            }
        } else if (cst != cst) {
            switch (domain.getDimension()) {
                case 1:
                    return NAN1;
                case 2:
                    return NAN2;
                case 3:
                    return NAN3;
            }
        }
        return new DoubleValue(cst, domain);
    }

    public boolean isInvariantImpl(Axis axis) {
        return true;
    }

//    public double compute(double x, double y) {
//        if (contains(x, y)) {
//            return value;
//        }
//        return 0;
//    }

    public DoubleToDouble mul(double factor, Domain newDomain) {
        return DoubleValue.valueOf(factor * value,
                newDomain == null ? domain : domain.intersect(newDomain)
        );
    }

    public DoubleToDouble getSymmetricX() {
        return this;
    }

    public DoubleToDouble getSymmetricY() {
        return this;
    }

    public DoubleToDouble translate(double deltaX, double deltaY) {
        return DoubleValue.valueOf(value, domain.translate(deltaX, deltaY));
    }

    public DoubleToDouble toXOpposite() {
        return this;
    }

    public DoubleToDouble toYOpposite() {
        return this;
    }

    public boolean isZero() {
        return value == 0;
    }

    public boolean isNaN() {
        return Double.isNaN(value);
    }

    public boolean isInfinite() {
        return Double.isInfinite(value);
    }


    public double getValue() {
        return value;
    }

    public boolean isSymmetric(AxisXY axis) {
        return true;
    }


    @Override
    public DoubleToDouble clone() {
        return (DoubleToDouble) super.clone();
    }

    @Override
    public String toString() {
        return FormatFactory.toString(this);
    }

    public DoubleToComplex toDC() {
        return new ComplexValue(Complex.valueOf(getValue()), getDomain());
    }

    public List<Expr> getSubExpressions() {
        return Collections.EMPTY_LIST;
    }


    public boolean isMatrix() {
        return isDouble();
    }


    @Override
    public Expr setParam(String name, Expr value) {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DoubleValue)) return false;

        DoubleValue doubleXY = (DoubleValue) o;

        if (Double.compare(doubleXY.value, value) != 0) return false;
        if (domain != null ? !domain.equals(doubleXY.domain) : doubleXY.domain != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = domain != null ? domain.hashCode() : 0;
        temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public Expr composeX(Expr xreplacement) {
        return this;
    }

    @Override
    public Expr composeY(Expr yreplacement) {
        return this;
    }

    @Override
    public boolean isInvariant(Axis axis) {
        return true;
    }

    @Override
    public boolean hasParams() {
        return false;
    }


//    @Override
//    public double computeDouble(double x, double y, double z) {
//        return computeDouble(x,y,z,new BooleanMarker());
//    }

    @Override
    public double computeDouble(double x, double y, double z, BooleanMarker defined) {
        if ((contains(x, y, z))) {
            defined.set();
            return value;
        }
        return 0;
    }

//    @Override
//    public double computeDouble(double x) {
//        return (contains(x)) ? value : 0;
//    }

    @Override
    public double computeDouble(double x, BooleanMarker defined) {
        if (contains(x)) {
            defined.set();
            return value;
        }
        return 0;
    }

//    protected double computeDouble0(double x) {
//        return value;
//    }
//
//    protected double computeDouble0(double x, double y) {
//        return value;
//    }
//
//    protected double computeDouble0(double x, double y, double z) {
//        return value;
//    }

    public Complex getComplexConstant() {
        return Complex.valueOf(getValue());
    }

    //////////////////////////////
    // INHERITED
    /////////////////////////////


    public double[] computeDouble(double x, double[] y, Domain d0) {
        double[] r = new double[y.length];
        Range abcd = (d0 == null ? domain : domain.intersect(d0)).range(new double[]{x}, y);
        if (abcd != null) {
            int cy = abcd.ymin;
            int dy = abcd.ymax;
            for (int yIndex = cy; yIndex <= dy; yIndex++) {
                r[yIndex] = computeDouble(x, y[yIndex]);
            }
        }
        return r;
    }

    public double[] computeDouble(double[] x, double y, Domain d0) {
        double[] r = new double[x.length];
        Range abcd = (d0 == null ? domain : domain.intersect(d0)).range(x, new double[]{y});
        if (abcd != null) {
            int cy = abcd.ymin;
            int dy = abcd.ymax;
            for (int xIndex = cy; xIndex <= dy; xIndex++) {
                r[xIndex] = computeDouble(x[xIndex], y);
            }
        }
        return r;
    }


    public Domain getDomain() {
        return domain;
    }

    public Domain intersect(DoubleToDouble other) {
        return domain.intersect(other.getDomain());
    }

    public Domain intersect(DoubleToDouble other, Domain someDomain) {
        //return Domain.intersect(domain, other.domain, domain);
        return this.domain.intersect(someDomain).intersect(other.getDomain());
    }

    public DoubleToDouble add(DoubleToDouble... others) {
        return Maths.sum(this, Maths.sum(others)).toDD();
    }

    public DoubleToDouble getSymmetricX(Domain newDomain) {
        return getSymmetricX(((newDomain.xmin() + newDomain.xmax()) / 2));
    }

    public DoubleToDouble getSymmetricY(Domain newDomain) {
        return getSymmetricY(((newDomain.ymin() + newDomain.ymax()) / 2));
    }

    public DoubleToDouble getSymmetricX(double x0) {
        return ((AbstractDoubleToDouble) getSymmetricX()).translate(2 * (x0 - ((domain.xmin() + domain.xmax()) / 2)), 0);
    }

    public DoubleToDouble getSymmetricY(double y0) {
        return ((AbstractDoubleToDouble) getSymmetricY()).translate(0, 2 * (y0 - ((domain.ymin() + domain.ymax()) / 2)));
    }

    public DoubleToDouble simplify() {
        return this;
    }

    public boolean isDC() {
        return true;
    }

    public boolean isDD() {
        return true;
    }

    public DoubleToDouble toDD() {
        return this;
    }

    public boolean isDM() {
        return true;
    }

    public DoubleToMatrix toDM() {
        return toDC().toDM();
    }

    public boolean isDV() {
        return true;
    }

    @Override
    public DoubleToVector toDV() {
        return toDC().toDV();
    }

//    public boolean isDDx() {
//        return (isInvariant(Axis.Y) && isInvariant(Axis.Z));
//    }
//
//    public IDDx toDDx() {
//        if (isDDx()) {
//            return new DDxyToDDx(this, getDomain().getCenterY());
//        }
//        throw new IllegalArgumentException("Unsupported");
//    }

    public boolean isScalarExpr() {
        return true;//getDomainDimension()<=1;
    }

    public boolean isComplex() {
        return getDomain().isUnconstrained();
    }


    public double[] computeDouble(double[] x, double y, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, x, y, d0, ranges);
    }

    public double[] computeDouble(double x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, x, y, d0, ranges);
    }

    @Override
    public Matrix toMatrix() {
        return toComplex().toMatrix();
    }

    @Override
    public Complex toComplex() {
        return Complex.valueOf(toDouble());
    }
//    public double computeDouble(double x, double y) {
//        return Expressions.computeDouble(this, x, y);
//    }

    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        double[][][] r = new double[z.length][y.length][x.length];
        Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x, y, z);
        if (currRange != null) {
            int ax = currRange.xmin;
            int bx = currRange.xmax;
            int cy = currRange.ymin;
            int dy = currRange.ymax;
            BooleanArray3 d = currRange.setDefined3(x.length, y.length, z.length);
            for (int i = currRange.zmin; i <= currRange.zmax; i++) {
                for (int k = ax; k <= bx; k++) {
                    for (int j = cy; j <= dy; j++) {
                        if (contains(x[k], y[j])) {
                            double v = value;//computeDouble0(x[k], y[j], z[i]);
                            r[i][j][k] = v;
                            d.set(i, j, k);
                        }
                    }
                }
            }
            if (ranges != null) {
                ranges.set(currRange);
            }
        }
        return r;
    }

    public double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        double[][] r = new double[y.length][x.length];
        Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x, y);
        if (currRange != null) {
            int ax = currRange.xmin;
            int bx = currRange.xmax;
            int cy = currRange.ymin;
            int dy = currRange.ymax;
            BooleanArray2 d = BooleanArrays.newArray(y.length, x.length);
            currRange.setDefined(d);
            for (int k = ax; k <= bx; k++) {
                for (int j = cy; j <= dy; j++) {
                    if (contains(x[k], y[j])) {
                        double v = value;//computeDouble0(x[k], y[j]);
                        r[j][k] = v;
                        d.set(j, k);
                    }
                }
            }
            if (ranges != null) {
                ranges.set(currRange);
            }
        }
        return r;
    }


    @Override
    public double[] computeDouble(double[] x, Domain d0, Out<Range> ranges) {
        double[] r = new double[x.length];
        Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x);
        if (currRange != null) {
            int ax = currRange.xmin;
            int bx = currRange.xmax;
            BooleanArray1 d = currRange.setDefined1(x.length);
            for (int xIndex = ax; xIndex <= bx; xIndex++) {
                if (contains(x[xIndex])) {
                    d.set(xIndex);
                    r[xIndex] = value;//computeDouble0(x[xIndex]);
                } else {
                    //d.clear(xIndex);
                }
            }
            if (ranges != null) {
                ranges.set(currRange);
            }
        }
        return r;
    }

    //    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        double[][][] d = computeDouble(x, y, z, d0, ranges);
        Complex[][][] m = new Complex[d.length][d[0].length][d[0][0].length];
        for (int zi = 0; zi < m.length; zi++) {
            for (int yi = 0; yi < m[zi].length; zi++) {
                for (int xi = 0; xi < m[zi][yi].length; xi++) {
                    m[zi][yi][xi] = Complex.valueOf(d[zi][yi][xi]);
                }
            }
        }
        return m;
    }

    public Matrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        double[][][] d = computeDouble(x, y, z, d0, ranges);
        Matrix[][][] m = new Matrix[d.length][d[0].length][d[0][0].length];
        for (int zi = 0; zi < m.length; zi++) {
            for (int yi = 0; yi < m[zi].length; zi++) {
                for (int xi = 0; xi < m[zi][yi].length; xi++) {
                    m[zi][yi][xi] = Maths.constantMatrix(1, Complex.valueOf(d[zi][yi][xi]));
                }
            }
        }
        return m;
    }

    @Override
    public double computeDouble(double x, double y, BooleanMarker defined) {//
        switch (getDomainDimension()) {
            case 1: {
                if (contains(x)) {
                    defined.set();
                    return value;//computeDouble0(x);
                }
                return 0;
            }
            case 2: {
                if (contains(x, y)) {
                    defined.set();
                    return value;//computeDouble0(x, y);
                }
                return 0;
            }
        }
        if (contains(x, y)) {
            defined.set();
            return value;//computeDouble0(x, y);
        }
        return 0;
    }


    public boolean contains(double x) {
        return domain.contains(x);
    }

    public boolean contains(double x, double y) {
        return domain.contains(x, y);
    }

    public boolean contains(double x, double y, double z) {
        return domain.contains(x, y, z);
    }

    @Override
    public double[] computeDouble(double[] x) {
        return computeDouble(x, (Domain) null, null);
    }

    @Override
    public double[] computeDouble(double x, double[] y) {
        return computeDouble(x, y, (Domain) null, null);
    }

    @Override
    public double[][][] computeDouble(double[] x, double[] y, double[] z) {
        return computeDouble(x, y, z, (Domain) null, null);
    }

    @Override
    public double[][] computeDouble(double[] x, double[] y) {
        return computeDouble(x, y, (Domain) null, null);
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
    }

    @Override
    public Expr normalize() {
        if (isZero()) {
            return this;
        }
        switch (domain.getDimension()) {
            case 1:
                return DoubleValue.valueOf(1.0 / domain.xwidth(), domain);
            case 2:
                return DoubleValue.valueOf(1.0 / Maths.sqrt(domain.xwidth() * domain.ywidth()), domain);
            case 3:
                return DoubleValue.valueOf(1.0 / Maths.sqrt(domain.xwidth() * domain.ywidth() * domain.zwidth(), 3), domain);
        }
        throw new IllegalArgumentException("Unsupported domain dimension " + domain);
    }


    @Override
    public int getDomainDimension() {
        return domain.getDimension();
    }

    public boolean isDouble() {
        return getDomain().isUnconstrained();
    }

    @Override
    public double toDouble() {
//        if (!isDoubleValue()) {
//            throw new ClassCastException("Constrained double");
//        }
        return value;
    }

    public boolean isDoubleExpr() {
        return true;
    }

    @Override
    public boolean isDoubleTyped() {
        return true;
    }

    @Override
    public Expr mul(Domain domain) {
        return new DoubleValue(value, getDomain().intersect(domain));
    }

    @Override
    public Expr mul(double other) {
        return new DoubleValue(value * other, getDomain().intersect(domain));
    }

    @Override
    public Expr mul(Complex other) {
        if (other.isReal()) {
            return mul(other.toDouble());
        }
        return new ComplexValue(other.mul(value), getDomain().intersect(domain));
    }
}
