package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

import java.util.Collections;
import java.util.List;

/**
 * User: taha Date: 2 juil. 2003 Time: 14:29:58
 */
public final class DoubleValue extends AbstractDoubleToDouble implements Cloneable, IConstantValue {

    public static final DoubleValue ZERO = new DoubleValue(0, Domain.EMPTYXY);

    public static final DoubleValue ZERO1 = new DoubleValue(0, Domain.EMPTYX);
    public static final DoubleValue ZERO2 = new DoubleValue(0, Domain.EMPTYXY);
    public static final DoubleValue ZERO3 = new DoubleValue(0, Domain.EMPTYXYZ);

    public static final DoubleValue NAN1 = new DoubleValue(Double.NaN, Domain.EMPTYX);
    public static final DoubleValue NAN2 = new DoubleValue(Double.NaN, Domain.EMPTYXY);
    public static final DoubleValue NAN3 = new DoubleValue(Double.NaN, Domain.EMPTYXYZ);

    private static final long serialVersionUID = -1010101010101001006L;
    //    public static final int CODE = 1;
    public double value;

    public DoubleValue(Domain domain) {
        this(
                domain.dimension() == 1 ? domain.xwidth() : domain.dimension() == 2 ? Math.sqrt(domain.xwidth() * domain.ywidth()) : Math.sqrt(domain.xwidth() * domain.ywidth() * domain.zwidth()), domain);
    }

    public DoubleValue(double cst, Domain domain) {
        super(cst == 0 ? (domain == null ? Domain.FULL(1) : Domain.ZERO(domain.dimension())) : domain == null ? Domain.FULL(1) : domain);
        this.value = cst;
//        if(value!=0 && this.domain.isEmpty()){
//            System.out.println("Why");
//        }
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

    @Override
    public boolean isInvariantImpl(Axis axis) {
        return true;
    }

//    public double compute(double x, double y) {
//        if (getDomain().contains(x, y)) {
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

    @Override
    public boolean isZeroImpl() {
        return value == 0;
    }

    @Override
    public boolean isNaNImpl() {
        return Double.isNaN(value);
    }

    @Override
    public boolean isInfiniteImpl() {
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

    @Override
    public boolean isDoubleImpl() {
        return getDomain().equals(Domain.FULL(getDomainDimension()));
    }

    @Override
    public boolean isMatrixImpl() {
        return isDouble();
    }


    @Override
    public double toDouble() {
        if (!isDouble()) {
            throw new ClassCastException("Constrained double");
        }
        return value;
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
    public boolean isDoubleExprImpl() {
        return true;
    }


    @Override
    protected double computeDouble0(double x) {
        return value;
    }

    @Override
    protected double computeDouble0(double x, double y) {
        return value;
    }

    @Override
    protected double computeDouble0(double x, double y, double z) {
        return value;
    }

    @Override
    public Complex getComplexConstant() {
        return Complex.valueOf(getValue());
    }
}
