package net.vpc.scholar.hadrumaths.symbolic.double2double;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.internal.IgnoreRandomGeneration;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDoubleDefaults;
import net.vpc.scholar.hadrumaths.symbolic.ExprDefaults;

import java.util.Collections;
import java.util.List;

/**
 * User: taha Date: 2 juil. 2003 Time: 10:39:39
 */
@IgnoreRandomGeneration
public abstract class AbstractDoubleToDouble implements DoubleToDoubleDefaults.DoubleToDoubleNormal {
    private static final long serialVersionUID = 1L;

//    protected Domain domain;
//    protected DoubleToDoubleOneToManyHelper helper;

    public AbstractDoubleToDouble() {
//        helper= new SimpleDoubleToDoubleOneToManyHelper();
    }

    public Domain intersect(DoubleToDouble other) {
        return getDomain().intersect(other.getDomain());
    }

    public Domain intersect(DoubleToDouble other, Domain someDomain) {
        //return Domain.intersect(domain, other.domain, domain);
        return this.getDomain().intersect(someDomain).intersect(other.getDomain());
    }

    public DoubleToDouble add(DoubleToDouble... others) {
        return Maths.sum(this, Maths.sum(others)).toDD();
    }

    public DoubleToDouble getSymmetricX(Domain newDomain) {
        return getSymmetricX(((newDomain.xmin() + newDomain.xmax()) / 2));
    }

    public DoubleToDouble getSymmetricX(double x0) {
        return ((AbstractDoubleToDouble) getSymmetricX()).translate(2 * (x0 - this.getDomain().xmiddle()), 0);
    }

    public DoubleToDouble translate(double deltaX, double deltaY) {
        throw new IllegalArgumentException("Not Implemented in " + getClass().getSimpleName());
    }

    public DoubleToDouble getSymmetricX() {
        throw new IllegalArgumentException("Not Implemented in " + getClass().getSimpleName());
    }

    public DoubleToDouble getSymmetricY(Domain newDomain) {
        return getSymmetricY(((newDomain.ymin() + newDomain.ymax()) / 2));
    }

    public DoubleToDouble getSymmetricY(double y0) {
        return ((AbstractDoubleToDouble) getSymmetricY()).translate(0, 2 * (y0 - this.getDomain().ymiddle()));
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
//    public DoubleToDouble simplify(SimplifyOptions options) {
//        return this;
//    }

    public DoubleToDouble getSymmetricY() {
        throw new IllegalArgumentException("Not Implemented in " + getClass().getSimpleName());
    }

    public DoubleToDouble mul(double factor, Domain newDomain) {
        return mul(newDomain).mul(factor).toDD();
    }

    public DoubleToDouble toXOpposite() {
        throw new IllegalArgumentException("Not Implemented in " + getClass().getSimpleName());
    }

    public DoubleToDouble toYOpposite() {
        throw new IllegalArgumentException("Not Implemented in " + getClass().getSimpleName());
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

    @Override
    public Complex toComplex() {
        return Complex.of(toDouble());
    }

    public List<Expr> getChildren() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
    }

    //////////////////////////////////////////////////////////////

    public abstract Domain getDomain();

    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }

}
