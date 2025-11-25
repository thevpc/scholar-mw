package net.thevpc.scholar.hadrumaths.symbolic.double2vector;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToMatrix;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.CDiscrete;
import net.thevpc.scholar.hadrumaths.symbolic.double2matrix.DefaultDoubleToMatrix;

import java.util.Arrays;
import java.util.List;
import net.thevpc.scholar.hadrumaths.util.internal.IntValidator;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 18 juil. 2007 23:36:28
 */
public class VDiscrete extends AbstractDoubleToVector implements Normalizable {
    private static final long serialVersionUID = 1L;
    private final CDiscrete[] values;
    private final Domain domain;
    private final ComponentDimension componentDimension;
    private final Coordinates coordinates= Coordinates.CARTESIAN;

    public VDiscrete(CDiscrete fx) {
        values = new CDiscrete[1];
        if (fx == null) {
            throw new NullPointerException();
        }
        values[Axis.X.index()] = fx;
//        values[1] = CDiscrete.cst(fx.getDomain(), Complex.ZERO, fx.getDx(), fx.getDy(), fx.getDz(), fx.getAxis()[0], fx.getAxis()[1], fx.getAxis()[2]);
//        values[2] = CDiscrete.cst(fx.getDomain(), Complex.ZERO, fx.getDx(), fx.getDy(), fx.getDz(), fx.getAxis()[0], fx.getAxis()[1], fx.getAxis()[2]);
        domain = values[0].getDomain();//.expand(values[1].getDomain()).expand(values[2].getDomain());
        componentDimension = ComponentDimension.SCALAR;
    }

    public VDiscrete(CDiscrete fx, CDiscrete fy) {
        values = new CDiscrete[2];
        if (fx == null) {
            throw new NullPointerException();
        }
        if (fy == null) {
            throw new NullPointerException();
        }
        values[0] = fx;
        values[1] = fy;
        //values[2] = CDiscrete.cst(fx.getDomain(), Complex.ZERO, fx.getDx(), fx.getDy(), fx.getDz(), fx.getAxis()[0], fx.getAxis()[1], fx.getAxis()[2]);
        domain = values[0].getDomain().expand(values[1].getDomain());//.expand(values[2].getDomain());
        componentDimension = ComponentDimension.VECTOR2;
    }

    public VDiscrete(CDiscrete fx, CDiscrete fy, CDiscrete fz) {
        values = new CDiscrete[3];
        if (fx == null) {
            throw new NullPointerException();
        }
        if (fy == null) {
            throw new NullPointerException();
        }
        if (fz == null) {
            throw new NullPointerException();
        }
        values[0] = fx;
        values[1] = fy;
        values[2] = fz;
        domain = values[0].getDomain().expand(values[1].getDomain()).expand(values[2].getDomain());
        componentDimension = ComponentDimension.VECTOR3;
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("VDiscrete");
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public static VDiscrete discretize(Expr expr, Domain domain, @IntValidator(min = 1, max = 100000) int xSamples, @IntValidator(min = 1, max = 100000) int ySamples, @IntValidator(min = 1, max = 100000) int zSamples) {
        if (expr.is(ExprDim.SCALAR)) {
            return new VDiscrete(CDiscrete.discretize(expr, domain, xSamples, ySamples, zSamples));
        } else {
            DoubleToVector v = expr.toDV();
            ComponentDimension d = v.getComponentDimension();
            if (d.columns == 1) {
                if (d.rows == 1) {
                    return new VDiscrete(
                            CDiscrete.discretize(v.getComponent(Axis.X), domain, xSamples, ySamples, zSamples)
                    );

                } else if (d.rows == 2) {
                    return new VDiscrete(
                            CDiscrete.discretize(v.getComponent(Axis.X), domain, xSamples, ySamples, zSamples),
                            CDiscrete.discretize(v.getComponent(Axis.Y), domain, xSamples, ySamples, zSamples)
                    );
                } else if (d.rows == 3) {
                    return new VDiscrete(
                            CDiscrete.discretize(v.getComponent(Axis.X), domain, xSamples, ySamples, zSamples),
                            CDiscrete.discretize(v.getComponent(Axis.Y), domain, xSamples, ySamples, zSamples),
                            CDiscrete.discretize(v.getComponent(Axis.Z), domain, xSamples, ySamples, zSamples)
                    );
                }
            }
            throw new UnsupportedComponentDimensionException(d);
        }
    }

    //    @Override
    public VDiscrete abssqr() {
        switch (componentDimension.rows) {
            case 1: {
                return new VDiscrete(values[0].abssqr());
            }
            case 2: {
                return new VDiscrete(values[0].abssqr(), values[1].abssqr());
            }
            case 3: {
                return new VDiscrete(values[0].abssqr(), values[1].abssqr(), values[2].abssqr());
            }
        }
        throw new UnsupportedComponentDimensionException(componentDimension.rows);
    }

    public VDiscrete sqr() {
        switch (componentDimension.rows) {
            case 1: {
                return new VDiscrete(values[0].sqr());
            }
            case 2: {
                return new VDiscrete(values[0].sqr(), values[1].sqr());
            }
            case 3: {
                return new VDiscrete(values[0].sqr(), values[1].sqr(), values[2].sqr());
            }
        }
        throw new UnsupportedComponentDimensionException(componentDimension.rows);
    }

    public VDiscrete sqrt() {
        switch (componentDimension.rows) {
            case 1: {
                return new VDiscrete(values[0].sqrt());
            }
            case 2: {
                return new VDiscrete(values[0].sqrt(), values[1].sqrt());
            }
            case 3: {
                return new VDiscrete(values[0].sqrt(), values[1].sqrt(), values[2].sqrt());
            }
        }
        throw new UnsupportedComponentDimensionException(componentDimension.rows);
    }

    public VDiscrete rot() {
        switch (componentDimension.rows) {
            case 3: {
                CDiscrete fx = values[0];
                CDiscrete fy = values[1];
                CDiscrete fz = values[2];
                return new VDiscrete(
                        (fz.diff(Axis.Y)).sub((fy.diff(Axis.Z))),
                        (fx.diff(Axis.Z)).sub((fz.diff(Axis.X))),
                        (fy.diff(Axis.X)).sub((fx.diff(Axis.Y)))
                );
            }
        }
        throw new UnsupportedComponentDimensionException(componentDimension.rows);
    }

    /**
     * vector product
     *
     * @param other
     * @return
     */
    public VDiscrete crossprod(VDiscrete other) {
        switch (componentDimension.rows) {
            case 3: {
                CDiscrete u1 = values[0];
                CDiscrete u2 = values[1];
                CDiscrete u3 = values[2];

                CDiscrete v1 = other.values[0];
                CDiscrete v2 = other.values[1];
                CDiscrete v3 = other.values[2];

                return new VDiscrete(
                        (u2.mul(v3)).sub((u3.mul(v2))),
                        (u3.mul(v1)).sub((u1.mul(v3))),
                        (u1.mul(v2)).sub((u2.mul(v1)))
                );
            }
        }
        throw new UnsupportedComponentDimensionException(componentDimension.rows);


    }

    public VDiscrete diff(Axis axis) {
        switch (componentDimension.rows) {
            case 1: {
                CDiscrete fx = values[0];
                return new VDiscrete(
                        fx.diff(axis)
                );
            }
            case 2: {
                CDiscrete fx = values[0];
                CDiscrete fy = values[1];
                return new VDiscrete(
                        fx.diff(axis),
                        fy.diff(axis)
                );
            }
            case 3: {
                CDiscrete fx = values[0];
                CDiscrete fy = values[1];
                CDiscrete fz = values[2];
                return new VDiscrete(
                        fx.diff(axis),
                        fy.diff(axis),
                        fz.diff(axis)
                );
            }
        }
        throw new UnsupportedComponentDimensionException(componentDimension.rows);
    }

    /**
     * divergence
     *
     * @return divergence
     */
    public CDiscrete divergence() {
        switch (componentDimension.rows) {
            case 1: {
                CDiscrete fx = values[0];
                return (fx.diff(Axis.X));
            }
            case 2: {
                CDiscrete fx = values[0];
                CDiscrete fy = values[1];
                return (fx.diff(Axis.X)).plus((fy.diff(Axis.Y)));
            }
            case 3: {
                CDiscrete fx = values[0];
                CDiscrete fy = values[1];
                CDiscrete fz = values[2];
                return (fx.diff(Axis.X)).plus((fy.diff(Axis.Y))).plus((fz.diff(Axis.Z)));
            }
        }
        throw new UnsupportedComponentDimensionException(componentDimension.rows);
    }

    public VDiscrete relativeError(VDiscrete other) {
        switch (componentDimension.rows) {
            case 1: {
                return new VDiscrete(
                        values[0].relativeError(other.getComponentDiscrete(Axis.X))
                );
            }
            case 2: {
                return new VDiscrete(
                        values[0].relativeError(other.getComponentDiscrete(Axis.X)),
                        values[1].relativeError(other.getComponentDiscrete(Axis.Y))
                );
            }
            case 3: {
                return new VDiscrete(
                        values[0].relativeError(other.getComponentDiscrete(Axis.X)),
                        values[1].relativeError(other.getComponentDiscrete(Axis.Y)),
                        values[2].relativeError(other.getComponentDiscrete(Axis.Z))
                );
            }
        }
        throw new UnsupportedComponentDimensionException(componentDimension.rows);
    }

    public CDiscrete getComponentDiscrete(Axis axis) {
        if (axis.index() >= values.length) {
            throw new IllegalArgumentException("Component " + axis + " is not defined");
        }
        CDiscrete value = values[axis.index()];
        if (value == null) {
            throw new IllegalArgumentException("Component " + axis + " is not defined");
        }
        return value;
    }

    public Expr getComponent(Axis axis) {
        if (axis.index() >= values.length) {
            return Maths.CZEROXY;
        }
        CDiscrete value = values[axis.index()];
        if (value == null) {
            throw new IllegalArgumentException("Component " + axis + " is not defined");
        }
        return value;
    }

    @Override
    public int getComponentSize() {
        return values.length;
    }

    public Complex sum() {
        switch (componentDimension.rows) {
            case 1: {
                return values[0].sum();
            }
            case 2: {
                return values[0].sum().plus(values[1].sum());
            }
            case 3: {
                return values[0].sum().plus(values[1].sum()).plus(values[2].sum());
            }
        }
        throw new UnsupportedComponentDimensionException(componentDimension.rows);
    }

    public Complex avg() {
        int cc = getCountX() * getCountY() * getCountZ();
        switch (componentDimension.rows) {
            case 1: {
                return values[0].avg();
            }
            case 2: {
                return values[0].sum().plus(values[1].sum()).div(cc);
            }
            case 3: {
                return values[0].sum().plus(values[1].sum()).plus(values[2].sum()).div(cc);
            }
        }
        throw new UnsupportedComponentDimensionException(componentDimension.rows);
    }

    public int getCountX() {
        return values[0].getCountX();
    }

    public int getCountY() {
        return values[1].getCountX();
    }

    public int getCountZ() {
        return values[1].getCountX();
    }

    public DoubleToVector vavg() {
        int cc = getCountX() * getCountY() * getCountZ();
        switch (componentDimension.rows) {
            case 1: {
                return Maths.vector(values[0].avg()).toDV();
            }
            case 2: {
                return Maths.vector(values[0].avg(), values[1].avg()).toDV();
            }
            case 3: {
                return Maths.vector(values[0].avg(), values[1].avg(), values[2].avg()).toDV();
            }
        }
        throw new UnsupportedComponentDimensionException(componentDimension.rows);
    }

    public DoubleToVector vsum() {
        int cc = getCountX() * getCountY() * getCountZ();
        switch (componentDimension.rows) {
            case 1: {
                Maths.vector(values[0].sum());
            }
            case 2: {
                Maths.vector(values[0].sum(), values[1].sum());
            }
            case 3: {
                Maths.vector(values[0].sum(), values[1].sum(), values[2].sum());
            }
        }
        throw new UnsupportedComponentDimensionException(componentDimension.rows);
    }

    public VDiscrete plus(VDiscrete other) {
        switch (componentDimension.rows) {
            case 1: {
                return new VDiscrete(
                        values[0].plus(other.getComponentDiscrete(Axis.X))
                );
            }
            case 2: {
                return new VDiscrete(
                        values[0].plus(other.getComponentDiscrete(Axis.X)),
                        values[1].plus(other.getComponentDiscrete(Axis.Y))
                );
            }
            case 3: {
                return new VDiscrete(
                        values[0].plus(other.getComponentDiscrete(Axis.X)),
                        values[1].plus(other.getComponentDiscrete(Axis.Y)),
                        values[2].plus(other.getComponentDiscrete(Axis.Z))
                );
            }
        }
        throw new UnsupportedComponentDimensionException(componentDimension.rows);
    }

    /**
     * @return gradient
     */
    public VDiscrete grad() {
        switch (componentDimension.rows) {
            case 1: {
                CDiscrete fx = values[0];
                return new VDiscrete(
                        (fx.diff(Axis.X))
                );
            }
            case 2: {
                CDiscrete fx = values[0];
                CDiscrete fy = values[1];

                return new VDiscrete(
                        (fx.diff(Axis.X)),
                        (fy.diff(Axis.Y))
                );
            }
            case 3: {
                CDiscrete fx = values[0];
                CDiscrete fy = values[1];
                CDiscrete fz = values[2];

                return new VDiscrete(
                        (fx.diff(Axis.X)),
                        (fy.diff(Axis.Y)),
                        (fz.diff(Axis.Z))
                );
            }
        }
        throw new UnsupportedComponentDimensionException(componentDimension.rows);
    }

    public CDiscrete[] getValues() {
        return values;
    }

    public ComplexMatrix getMatrix(Axis axis, PlaneAxis plane, int index) {
        return Maths.matrix(getArray(axis, plane.getNormalAxis(), index));
    }

    public Complex[][] getArray(Axis axis, Axis fixedNormalAxis, int index) {
        return getComponentDiscrete(axis).getArray(fixedNormalAxis, index);
    }

    public ComplexMatrix getMatrix(Axis axis, Axis fixedNormalAxis, int index) {
        return Maths.matrix(getComponentDiscrete(axis).getArray(fixedNormalAxis, index));
    }

    public Complex[][] getArray(Axis axis, PlaneAxis plane, int index) {
        return getArray(axis, plane.getNormalAxis(), index);
    }

    public Complex getValueAt(Axis axis, int xIndex, int yIndex, int zIndex) {
        return getComponentDiscrete(axis).getValues()[zIndex][yIndex][xIndex];
    }

    /**
     * Example : getVector(Axis.X, Axis.X, Axis.Y,0, Axis.Z,0)
     * from the X cube (cubeAxis) of the vector
     * retrieve the X axis values for y=0 (first index in y values)
     * and z=0 (first index in z values)
     *
     * @param cubeAxis   axis of the cube
     * @param fixedAxis1 first fixed axis
     * @param index1
     * @param fixedAxis2
     * @param index2
     * @return
     */
    public ComplexVector getVector(Axis cubeAxis, Axis fixedAxis1, int index1, Axis fixedAxis2, int index2) {
        return getComponentDiscrete(cubeAxis).getVector(cubeAxis, fixedAxis1, index1, fixedAxis2, index2);
    }

    public ComplexVector getVector(Axis cubeAxis) {
        return getComponentDiscrete(cubeAxis).getVector(cubeAxis);
    }

    @Override
    public boolean isInvariant(Axis axis) {
        return false;
    }

    @Override
    public DoubleToMatrix toDM() {
        return DefaultDoubleToMatrix.of(this);
    }

    @Override
    public Expr setParam(String name, Expr value) {
        return this;
    }

    @Override
    public List<Expr> getChildren() {
        return (List) Arrays.asList(values);
    }

    @Override
    public String getTitle() {
        return null;
    }

//    @Override
//    public Expr getComponent(int row, int col) {
//        if (col == 0) {
//            return values[row];
//        }
//        throw new IllegalArgumentException("Invalid");
//    }
//
//    @Override
//    public String getComponentTitle(int row, int col) {
//        if (col == 0) {
//            return Axis.cartesianValues()[row].toString();
//        }
//        throw new IllegalArgumentException("Invalid");
//    }

    @Override
    public ComponentDimension getComponentDimension() {
        return componentDimension;
    }

    @Override
    public Expr mul(Domain domain) {
        switch (componentDimension.rows) {
            case 1: {
                return new VDiscrete(values[0].mul(domain));
            }
            case 2: {
                return new VDiscrete(values[0].mul(domain), values[1].mul(domain));
            }
            case 3: {
                return new VDiscrete(values[0].mul(domain), values[1].mul(domain), values[2].mul(domain));
            }
        }
        throw new UnsupportedComponentDimensionException(componentDimension.rows);
    }


//    @Override
//    public boolean isDDx() {
//        return false;
//    }

//    @Override
//    public IDDx toDDx() {
//        throw new IllegalArgumentException("Unsupported");
//    }

    public VDiscrete mul(double d) {
        switch (componentDimension.rows) {
            case 1: {
                return new VDiscrete(values[0].mul(d));
            }
            case 2: {
                return new VDiscrete(values[0].mul(d), values[1].mul(d));
            }
            case 3: {
                return new VDiscrete(values[0].mul(d), values[1].mul(d), values[2].mul(d));
            }
        }
        throw new UnsupportedComponentDimensionException(componentDimension.rows);
    }

    public VDiscrete mul(Complex c) {
        switch (componentDimension.rows) {
            case 1: {
                return new VDiscrete(values[0].mul(c));
            }
            case 2: {
                return new VDiscrete(values[0].mul(c), values[1].mul(c));
            }
            case 3: {
                return new VDiscrete(values[0].mul(c), values[1].mul(c), values[2].mul(c));
            }
        }
        throw new UnsupportedComponentDimensionException(componentDimension.rows);
    }

    @Override
    public Expr mul(Expr other) {
        CDiscrete[] values2 = new CDiscrete[values.length];
        for (int i = 0; i < values2.length; i++) {
            Expr e = values2[i].mul(other);
            if (e instanceof CDiscrete) {
                values2[i] = (CDiscrete) e;
            } else {
                return super.mul(other);
            }
        }
        return of(values2);
    }

    @Override
    public Expr plus(Expr other) {
        CDiscrete[] values2 = new CDiscrete[values.length];
        for (int i = 0; i < values2.length; i++) {
            Expr e = values2[i].plus(other);
            if (e instanceof CDiscrete) {
                values2[i] = (CDiscrete) e;
            } else {
                return super.plus(other);
            }
        }
        return of(values2);
    }

    @Override
    public Expr div(Expr other) {
        CDiscrete[] values2 = new CDiscrete[values.length];
        for (int i = 0; i < values2.length; i++) {
            Expr e = values2[i].div(other);
            if (e instanceof CDiscrete) {
                values2[i] = (CDiscrete) e;
            } else {
                return super.div(other);
            }
        }
        return of(values2);
    }

    @Override
    public Expr sub(Expr other) {
        CDiscrete[] values2 = new CDiscrete[values.length];
        for (int i = 0; i < values2.length; i++) {
            Expr e = values2[i].sub(other);
            if (e instanceof CDiscrete) {
                values2[i] = (CDiscrete) e;
            } else {
                return super.sub(other);
            }
        }
        return of(values2);
    }

    @Override
    public boolean isSmartMulDouble() {
        return true;
    }

    @Override
    public boolean isSmartMulComplex() {
        return true;
    }

    @Override
    public boolean isSmartMulDomain() {
        return true;
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        switch (domain.dimension()) {
            case 1:
                new VDiscrete((CDiscrete) subExpressions[0]);
            case 2:
                new VDiscrete((CDiscrete) subExpressions[0], (CDiscrete) subExpressions[1]);
            case 3:
                new VDiscrete((CDiscrete) subExpressions[0], (CDiscrete) subExpressions[1], (CDiscrete) subExpressions[2]);
        }
        throw new IllegalArgumentException("Unsupported");
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    private static VDiscrete of(CDiscrete[] values2) {
        switch (values2.length) {
            case 1: {
                return new VDiscrete(values2[0]);
            }
            case 2: {
                return new VDiscrete(values2[0], values2[1]);
            }
            case 3: {
                return new VDiscrete(values2[0], values2[1], values2[2]);
            }
        }
        throw new IllegalArgumentException("Invalid dimension " + values2.length);
    }

    public Complex integrate(Domain domain, PlaneAxis axis, double fixedValue) {
        ComponentDimension dims = getComponentDimension();
        MutableComplex c = new MutableComplex();
        for (int i = 0; i < componentDimension.rows; i++) {
            c.add(getComponentDiscrete(Axis.cartesian(i)).integrate(domain, axis, fixedValue));
        }
        return c.toComplex();
    }

    public Complex integrate(Domain domain) {
        ComponentDimension dims = getComponentDimension();
        MutableComplex c = new MutableComplex();
        for (int i = 0; i < componentDimension.rows; i++) {
            c.add(getComponentDiscrete(Axis.cartesian(i)).integrate(domain));
        }
        return c.toComplex();
    }

    @Override
    public int hashCode() {
        int result = getClass().getName().hashCode();
        result = 31 * result + (values != null ? Arrays.hashCode(values) : 0);
        result = 31 * result + (domain != null ? domain.hashCode() : 0);
        result = 31 * result + (componentDimension != null ? componentDimension.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VDiscrete)) return false;

        VDiscrete vDiscrete = (VDiscrete) o;

        if (componentDimension != null ? !componentDimension.equals(vDiscrete.componentDimension) : vDiscrete.componentDimension != null)
            return false;
        if (domain != null ? !domain.equals(vDiscrete.domain) : vDiscrete.domain != null) return false;
        return Arrays.equals(values, vDiscrete.values);
    }

    @Override
    public double getDistance(Normalizable other) {
        if (other instanceof VDiscrete) {
            VDiscrete o = (VDiscrete) other;
            return (this.sub(o)).norm() / o.norm();
        } else {
            Normalizable o = other;
            return (this.norm() - o.norm()) / o.norm();
        }
    }

    public VDiscrete sub(VDiscrete other) {
        switch (componentDimension.rows) {
            case 1: {
                return new VDiscrete(
                        values[0].sub(other.getComponentDiscrete(Axis.X))
                );
            }
            case 2: {
                return new VDiscrete(
                        values[0].sub(other.getComponentDiscrete(Axis.X)),
                        values[1].sub(other.getComponentDiscrete(Axis.Y))
                );
            }
            case 3: {
                return new VDiscrete(
                        values[0].sub(other.getComponentDiscrete(Axis.X)),
                        values[1].sub(other.getComponentDiscrete(Axis.Y)),
                        values[2].sub(other.getComponentDiscrete(Axis.Z))
                );
            }
        }
        throw new UnsupportedComponentDimensionException(componentDimension.rows);
    }

    public double norm() {
        switch (componentDimension.rows) {
            case 1: {
                CDiscrete fx = values[0];
                return fx.norm();
            }
            case 2: {
                CDiscrete fx = values[0];
                CDiscrete fy = values[1];
                return fx.norm() + fy.norm();
            }
            case 3: {
                CDiscrete fx = values[0];
                CDiscrete fy = values[1];
                CDiscrete fz = values[2];
                return fx.norm() + fy.norm() + fz.norm();
            }
        }
        throw new UnsupportedComponentDimensionException(componentDimension.rows);
    }

    public int getCubesCount() {
        return componentDimension.rows;
    }

    @Override
    public String toLatex() {
        throw new UnsupportedOperationException("Not Implemented toLatex for "+getClass().getName());
    }

}
