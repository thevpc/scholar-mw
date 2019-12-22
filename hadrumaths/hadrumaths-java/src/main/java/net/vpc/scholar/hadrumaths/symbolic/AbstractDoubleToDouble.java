package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.conv.DoubleToDoubleOneToManyHelper;

import java.util.Collections;
import java.util.List;

/**
 * User: taha Date: 2 juil. 2003 Time: 10:39:39
 */
public abstract class AbstractDoubleToDouble extends AbstractExprPropertyAware implements DoubleToDouble {
    private static final long serialVersionUID = 1L;

    protected Domain domain;
    protected DoubleToDoubleOneToManyHelper helper;

    public AbstractDoubleToDouble(Domain domain) {
        this.domain = domain;
        helper=new DoubleToDoubleOneToManyHelper(this) {
            @Override
            protected double computeDouble0(double x, BooleanMarker defined) {
                return AbstractDoubleToDouble.this.computeDouble0(x, defined);
            }

            @Override
            protected double computeDouble0(double x, double y, BooleanMarker defined) {
                return AbstractDoubleToDouble.this.computeDouble0(x,y, defined);
            }

            @Override
            protected double computeDouble0(double x, double y, double z, BooleanMarker defined) {
                return AbstractDoubleToDouble.this.computeDouble0(x, y,z,defined);
            }
        };
    }

    public Domain getDomainImpl() {
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
        return MathsBase.sum(this, MathsBase.sum(others)).toDD();
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

    public DoubleToDouble mul(double factor, Domain newDomain) {
        return mul(newDomain).mul(factor).toDD();
    }

    //    public IDDxy multiply(IDDxy other) {
//        if (other instanceof DDxyCst) {
//            DDxyCst cst = ((DDxyCst) other);
//            return cst.value == 0 ? FunctionFactory.DZEROXY : multiply(cst.value, domain.intersect(other.domain));
//        } else if (other instanceof DDxyAbstractSum) {
//            return (other).multiply(this);
//        } else {
//            return new DDxyProduct(this, other);
//        }
//    }
    public DoubleToDouble simplify(SimplifyOptions options) {
        return this;
    }

    public DoubleToDouble toXOpposite() {
        throw new IllegalArgumentException("Not Implemented in " + getClass().getSimpleName());
    }

    public DoubleToDouble toYOpposite() {
        throw new IllegalArgumentException("Not Implemented in " + getClass().getSimpleName());
    }

    public DoubleToDouble getSymmetricX() {
        throw new IllegalArgumentException("Not Implemented in " + getClass().getSimpleName());
    }

    public DoubleToDouble getSymmetricY() {
        throw new IllegalArgumentException("Not Implemented in " + getClass().getSimpleName());
    }

    public DoubleToDouble translate(double deltaX, double deltaY) {
        throw new IllegalArgumentException("Not Implemented in " + getClass().getSimpleName());
    }

    @Override
    public DoubleToDouble clone() {
        return (DoubleToDouble) super.clone();
    }

    public boolean isZeroImpl() {
        return false;
    }

    public boolean isNaNImpl() {
        return false;
    }

    public boolean isInfiniteImpl() {
        return false;
    }

    public boolean isInvariantImpl(Axis axis) {
        return false;
    }

    public boolean isSymmetric(AxisXY axis) {
        switch (axis) {
            case X: {
                return getSymmetricX().equals(this);
            }
            case Y: {
                return getSymmetricY().equals(this);
            }
            case XY: {
                return getSymmetricX().equals(this) && getSymmetricY().equals(this);
            }
        }
        throw new IllegalArgumentException("Not supported");
    }

//    @Override
//    public String toString() {
//        return FormatFactory.toString(this);
//    }

    public boolean isDCImpl() {
        return true;
    }

    public DoubleToComplex toDC() {
        return MathsBase.complex(this);
    }

    public boolean isDDImpl() {
        return true;
    }

    public DoubleToDouble toDD() {
        return this;
    }

    public boolean isDMImpl() {
        return true;
    }

    public DoubleToMatrix toDM() {
        return toDC().toDM();
    }

    @Override
    public boolean isDVImpl() {
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
    public List<Expr> getSubExpressions() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public Expr setParam(String name, Expr value) {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractDoubleToDouble)) {
            return false;
        }
        if (!(o.getClass().equals(getClass()))) {
            return false;
        }

        AbstractDoubleToDouble dDxy = (AbstractDoubleToDouble) o;

        if (domain != null ? !domain.equals(dDxy.domain) : dDxy.domain != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = domain != null ? domain.hashCode() : 0;
        return result;
    }

    @Override
    public boolean isScalarExprImpl() {
        return true;//getDomainDimension()<=1;
    }

//    @Override
//    public boolean isDoubleExprImpl() {
//        return false;
//    }

    @Override
    public boolean isDoubleTyped() {
        return true;
    }

    @Override
    public boolean isComplexImpl() {
        return isDouble();
    }


    @Override
    public ComplexMatrix toMatrix() {
        return toComplex().toMatrix();
    }

    @Override
    public Complex toComplex() {
        return Complex.valueOf(toDouble());
    }

    @Override
    public double toDouble() {
        if (!isDoubleExpr()) {
            throw new ClassCastException("Not Double");
        }
        throw new RuntimeException("Unsupported yet toDouble in " + getClass().getName());
    }

    //////////////////////////////////////////////////////////////

    public double[] computeDouble(double[] x, double y, Domain d0, Out<Range> ranges) {
        return helper.computeDouble(x, y, d0, ranges);
    }

    public double[] computeDouble(double x, double[] y, Domain d0, Out<Range> ranges) {
        return helper.computeDouble(x, y, d0, ranges);
    }

    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return helper.computeDouble(x, y, z,d0, ranges);
    }

    public double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return helper.computeDouble(x, y,d0, ranges);
    }

    @Override
    public double[] computeDouble(double[] x, Domain d0, Out<Range> ranges) {
        return helper.computeDouble(x, d0, ranges);
    }

    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return helper.computeComplex(x,y,z, d0, ranges);
    }

    public ComplexMatrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return helper.computeMatrix(x,y,z, d0, ranges);
    }

    @Override
    public final double computeDouble(double x, BooleanMarker defined) {
        return helper.computeDouble(x,defined);
    }

    public double[] computeDouble(double x, double[] y, Domain d0) {
        return helper.computeDouble(x, y, d0);
    }

    public double[] computeDouble(double[] x, double y, Domain d0) {
        return helper.computeDouble(x, y, d0);
    }

    @Override
    public final double computeDouble(double x, double y, double z, BooleanMarker defined) {
        return helper.computeDouble(x, y, z,defined);
    }

    @Override
    public double computeDouble(double x, double y, BooleanMarker defined) {
        return helper.computeDouble(x, y, defined);
    }

    //////////////////////////////////////////////////////////////

    @Override
    public ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
    }

    protected abstract double computeDouble0(double x, BooleanMarker defined);

    protected abstract double computeDouble0(double x, double y, BooleanMarker defined);

    protected abstract double computeDouble0(double x, double y, double z, BooleanMarker defined);


}
