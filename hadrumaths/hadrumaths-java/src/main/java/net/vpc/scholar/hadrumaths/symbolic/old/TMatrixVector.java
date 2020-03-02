//package net.vpc.scholar.hadrumaths.symbolic;
//
//import net.vpc.scholar.hadrumaths.*;
//import net.vpc.scholar.hadrumaths.random.IntValidator;
//import net.vpc.scholar.hadruplot.PlotCube;
//import net.vpc.scholar.hadruplot.Samples;
//
//import java.util.Arrays;
//import java.util.List;
//
///**
// * @author Taha Ben Salah (taha.bensalah@gmail.com)
// * @creationtime 18 juil. 2007 23:36:28
// */
//public class TMatrixVector<T> implements Cloneable, Normalizable{
//    private static final long serialVersionUID = 1L;
//    private Matrix<T>[] values;
//    private Domain domain;
//    private ComponentDimension componentDimension;
//
//    public TMatrixVector(Matrix<T> fx) {
//        values = new Matrix[1];
//        if (fx == null) {
//            throw new NullPointerException();
//        }
//        values[Axis.X.index()] = fx;
////        values[Axis.Y.index()] = CDiscrete.cst(fx.getDomain(), Complex.ZERO, fx.getDx(), fx.getDy(), fx.getDz(), fx.getAxis()[0], fx.getAxis()[1], fx.getAxis()[2]);
////        values[Axis.Z.index()] = CDiscrete.cst(fx.getDomain(), Complex.ZERO, fx.getDx(), fx.getDy(), fx.getDz(), fx.getAxis()[0], fx.getAxis()[1], fx.getAxis()[2]);
////        domain = values[0].getDomain();//.expand(values[1].getDomain()).expand(values[2].getDomain());
//        componentDimension = ComponentDimension.SCALAR;
//    }
//
//    public TMatrixVector(Matrix<T> fx, Matrix<T> fy) {
//        values = new Matrix[2];
//        if (fx == null) {
//            throw new NullPointerException();
//        }
//        if (fy == null) {
//            throw new NullPointerException();
//        }
//        values[Axis.X.index()] = fx;
//        values[Axis.Y.index()] = fy;
//        //values[Axis.Z.index()] = CDiscrete.cst(fx.getDomain(), Complex.ZERO, fx.getDx(), fx.getDy(), fx.getDz(), fx.getAxis()[0], fx.getAxis()[1], fx.getAxis()[2]);
////        domain = values[0].getDomain().expand(values[1].getDomain());//.expand(values[2].getDomain());
//        componentDimension = ComponentDimension.VECTOR2;
//    }
//
//    public TMatrixVector(Matrix<T> fx, Matrix<T> fy, Matrix<T> fz) {
//        values = new Matrix[3];
//        if (fx == null) {
//            throw new NullPointerException();
//        }
//        if (fy == null) {
//            throw new NullPointerException();
//        }
//        if (fz == null) {
//            throw new NullPointerException();
//        }
//        values[Axis.X.index()] = fx;
//        values[Axis.Y.index()] = fy;
//        values[Axis.Z.index()] = fz;
////        domain = values[0].getDomain().expand(values[1].getDomain()).expand(values[2].getDomain());
//        componentDimension = ComponentDimension.VECTOR3;
//    }
//
//    public TMatrixVector<T> mul(T c) {
//        switch (componentDimension.rows) {
//            case 1: {
//                return new TMatrixVector(values[0].mul(c));
//            }
//            case 2: {
//                return new TMatrixVector(values[0].mul(c), values[1].mul(c));
//            }
//            case 3: {
//                return new TMatrixVector(values[0].mul(c), values[1].mul(c), values[2].mul(c));
//            }
//        }
//        throw new UnsupportedComponentDimensionException(componentDimension.rows);
//    }
//
//    public TMatrixVector<T> mul(double d) {
//        switch (componentDimension.rows) {
//            case 1: {
//                return new TMatrixVector(values[0].mul(d));
//            }
//            case 2: {
//                return new TMatrixVector(values[0].mul(d), values[1].mul(d));
//            }
//            case 3: {
//                return new TMatrixVector(values[0].mul(d), values[1].mul(d), values[2].mul(d));
//            }
//        }
//        throw new UnsupportedComponentDimensionException(componentDimension.rows);
//    }
//
////    @Override
////    public Expr mul(Domain domain) {
////        switch (componentDimension.rows) {
////            case 1: {
////                return new TMatrixVector(values[0].mul(domain));
////            }
////            case 2: {
////                return new TMatrixVector(values[0].mul(domain), values[1].mul(domain));
////            }
////            case 3: {
////                return new TMatrixVector(values[0].mul(domain), values[1].mul(domain), values[2].mul(domain));
////            }
////        }
////        throw new UnsupportedComponentDimensionException(componentDimension.rows);
////    }
//
//    //    @Override
//    public TMatrixVector<T> abssqr() {
//        switch (componentDimension.rows) {
//            case 1: {
//                return new TMatrixVector(values[0].abssqr());
//            }
//            case 2: {
//                return new TMatrixVector(values[0].abssqr(), values[1].abssqr());
//            }
//            case 3: {
//                return new TMatrixVector(values[0].abssqr(), values[1].abssqr(), values[2].abssqr());
//            }
//        }
//        throw new UnsupportedComponentDimensionException(componentDimension.rows);
//    }
//
//    public TMatrixVector<T> sqr() {
//        switch (componentDimension.rows) {
//            case 1: {
//                return new TMatrixVector(values[0].sqr());
//            }
//            case 2: {
//                return new TMatrixVector(values[0].sqr(), values[1].sqr());
//            }
//            case 3: {
//                return new TMatrixVector(values[0].sqr(), values[1].sqr(), values[2].sqr());
//            }
//        }
//        throw new UnsupportedComponentDimensionException(componentDimension.rows);
//    }
//
//    public TMatrixVector<T> sqrt() {
//        switch (componentDimension.rows) {
//            case 1: {
//                return new TMatrixVector(values[0].sqrt());
//            }
//            case 2: {
//                return new TMatrixVector(values[0].sqrt(), values[1].sqrt());
//            }
//            case 3: {
//                return new TMatrixVector(values[0].sqrt(), values[1].sqrt(), values[2].sqrt());
//            }
//        }
//        throw new UnsupportedComponentDimensionException(componentDimension.rows);
//    }
//
////    public TMatrixVector<T> rot() {
////        switch (componentDimension.rows) {
////            case 3: {
////                Matrix<T> fx = values[Axis.X.index()];
////                Matrix<T> fy = values[Axis.Y.index()];
////                Matrix<T> fz = values[Axis.Z.index()];
////                return new TMatrixVector(
////                        (fz.diff(Axis.Y)).sub((fy.diff(Axis.Z))),
////                        (fx.diff(Axis.Z)).sub((fz.diff(Axis.X))),
////                        (fy.diff(Axis.X)).sub((fx.diff(Axis.Y)))
////                );
////            }
////        }
////        throw new UnsupportedComponentDimensionException(componentDimension.rows);
////    }
//
//
//    public Matrix<T> getComponent(Axis axis) {
//        if (axis.index() >= values.length) {
//            throw new IllegalArgumentException("Component " + axis + " is not defined");
//        }
//        Matrix<T> value = values[axis.index()];
//        if (value == null) {
//            throw new IllegalArgumentException("Component " + axis + " is not defined");
//        }
//        return value;
//    }
//
//    /**
//     * vector product
//     *
//     * @param other
//     * @return
//     */
//    public TMatrixVector<T> crossprod(TMatrixVector other) {
//        switch (componentDimension.rows) {
//            case 3: {
//                Matrix<T> u1 = values[Axis.X.index()];
//                Matrix<T> u2 = values[Axis.Y.index()];
//                Matrix<T> u3 = values[Axis.Z.index()];
//
//                Matrix<T> v1 = other.values[Axis.X.index()];
//                Matrix<T> v2 = other.values[Axis.Y.index()];
//                Matrix<T> v3 = other.values[Axis.Z.index()];
//
//                return new TMatrixVector(
//                        (u2.mul(v3)).sub((u3.mul(v2))),
//                        (u3.mul(v1)).sub((u1.mul(v3))),
//                        (u1.mul(v2)).sub((u2.mul(v1)))
//                );
//            }
//        }
//        throw new UnsupportedComponentDimensionException(componentDimension.rows);
//
//
//    }
//
////    public TMatrixVector<T> diff(Axis axis) {
////        switch (componentDimension.rows) {
////            case 1: {
////                CDiscrete fx = values[Axis.X.index()];
////                return new TMatrixVector(
////                        fx.diff(axis)
////                );
////            }
////            case 2: {
////                CDiscrete fx = values[Axis.X.index()];
////                CDiscrete fy = values[Axis.Y.index()];
////                return new TMatrixVector(
////                        fx.diff(axis),
////                        fy.diff(axis)
////                );
////            }
////            case 3: {
////                CDiscrete fx = values[Axis.X.index()];
////                CDiscrete fy = values[Axis.Y.index()];
////                CDiscrete fz = values[Axis.Z.index()];
////                return new TMatrixVector(
////                        fx.diff(axis),
////                        fy.diff(axis),
////                        fz.diff(axis)
////                );
////            }
////        }
////        throw new UnsupportedComponentDimensionException(componentDimension.rows);
////    }
//
////    /**
////     * divergence
////     *
////     * @return divergence
////     */
////    public CDiscrete divergence() {
////        switch (componentDimension.rows) {
////            case 1: {
////                Matrix<T> fx = values[Axis.X.index()];
////                return (fx.diff(Axis.X));
////            }
////            case 2: {
////                Matrix<T> fx = values[Axis.X.index()];
////                Matrix<T> fy = values[Axis.Y.index()];
////                return (fx.diff(Axis.X)).add((fy.diff(Axis.Y)));
////            }
////            case 3: {
////                Matrix<T> fx = values[Axis.X.index()];
////                Matrix<T> fy = values[Axis.Y.index()];
////                Matrix<T> fz = values[Axis.Z.index()];
////                return (fx.diff(Axis.X)).add((fy.diff(Axis.Y))).add((fz.diff(Axis.Z)));
////            }
////        }
////        throw new UnsupportedComponentDimensionException(componentDimension.rows);
////    }
//
//    public TMatrixVector<T> sub(TMatrixVector other) {
//        switch (componentDimension.rows) {
//            case 1: {
//                return new TMatrixVector(
//                        values[Axis.X.index()].sub(other.getComponent(Axis.X))
//                );
//            }
//            case 2: {
//                return new TMatrixVector(
//                        values[Axis.X.index()].sub(other.getComponent(Axis.X)),
//                        values[Axis.Y.index()].sub(other.getComponent(Axis.Y))
//                );
//            }
//            case 3: {
//                return new TMatrixVector(
//                        values[Axis.X.index()].sub(other.getComponent(Axis.X)),
//                        values[Axis.Y.index()].sub(other.getComponent(Axis.Y)),
//                        values[Axis.Z.index()].sub(other.getComponent(Axis.Z))
//                );
//            }
//        }
//        throw new UnsupportedComponentDimensionException(componentDimension.rows);
//    }
//
//    public TMatrixVector<T> getErrorMatrix(TMatrixVector other) {
//        switch (componentDimension.rows) {
//            case 1: {
//                return new TMatrixVector(
//                        values[Axis.X.index()].getErrorMatrix(other.getComponent(Axis.X))
//                );
//            }
//            case 2: {
//                return new TMatrixVector(
//                        values[Axis.X.index()].getErrorMatrix(other.getComponent(Axis.X)),
//                        values[Axis.Y.index()].getErrorMatrix(other.getComponent(Axis.Y))
//                );
//            }
//            case 3: {
//                return new TMatrixVector(
//                        values[Axis.X.index()].getErrorMatrix(other.getComponent(Axis.X)),
//                        values[Axis.Y.index()].getErrorMatrix(other.getComponent(Axis.Y)),
//                        values[Axis.Z.index()].getErrorMatrix(other.getComponent(Axis.Z))
//                );
//            }
//        }
//        throw new UnsupportedComponentDimensionException(componentDimension.rows);
//    }
//
//    public T sum() {
//        switch (componentDimension.rows) {
//            case 1: {
//                return values[Axis.X.index()].sum();
//            }
//            case 2: {
//                VectorSpace<T> s = values[Axis.X.index()].getComponentVectorSpace();
//                return s.add(values[Axis.X.index()].sum(),values[Axis.Y.index()].sum());
//            }
//            case 3: {
//                VectorSpace<T> s = values[Axis.X.index()].getComponentVectorSpace();
//                RepeatableOp<T> op = s.addRepeatableOp();
//                op.append(values[Axis.X.index()].sum());
//                op.append(values[Axis.Y.index()].sum());
//                op.append(values[Axis.Z.index()].sum());
//                return op.eval();
//            }
//        }
//        throw new UnsupportedComponentDimensionException(componentDimension.rows);
//    }
//
////    @Override
//    public int getComponentSize() {
//        return values.length;
//    }
//
//    @Override
//    public double getDistance(Normalizable other) {
//        if (other instanceof TMatrixVector) {
//            TMatrixVector o = (TMatrixVector) other;
//            return (this.sub(o)).norm() / o.norm();
//        } else {
//            Normalizable o = other;
//            return (this.norm() - o.norm()) / o.norm();
//        }
//    }
//
//
//        public double norm() {
//        switch (componentDimension.rows) {
//            case 1: {
//                Matrix<T> fx = values[Axis.X.index()];
//                return fx.norm();
//            }
//            case 2: {
//                Matrix<T> fx = values[Axis.X.index()];
//                Matrix<T> fy = values[Axis.Y.index()];
//                return fx.norm() + fy.norm();
//            }
//            case 3: {
//                Matrix<T> fx = values[Axis.X.index()];
//                Matrix<T> fy = values[Axis.Y.index()];
//                Matrix<T> fz = values[Axis.Z.index()];
//                return fx.norm() + fy.norm() + fz.norm();
//            }
//        }
//        throw new UnsupportedComponentDimensionException(componentDimension.rows);
//    }
//
//    public Matrix<T>[] getValues() {
//        return values;
//    }
//
//    public ComponentDimension getComponentDimension() {
//        return componentDimension;
//    }
//
//    public Matrix<T> get(int index) {
//        return values[index];
//    }
//
//    public Matrix<T> get(Enum e) {
//        return values[e.index()];
//    }
//
//    //TODO
////    public T avg() {
////        int cc = getCountX() * getCountY() * getCountZ();
////        switch (componentDimension.rows) {
////            case 1: {
////                return values[Axis.X.index()].avg();
////            }
////            case 2: {
////                return values[Axis.X.index()].sum().add(values[Axis.Y.index()].sum()).div(cc);
////            }
////            case 3: {
////                return values[Axis.X.index()].sum().add(values[Axis.Y.index()].sum()).add(values[Axis.Z.index()].sum()).div(cc);
////            }
////        }
////        throw new UnsupportedComponentDimensionException(componentDimension.rows);
////    }
////
////    public DoubleToVector vavg() {
////        int cc = getCountX() * getCountY() * getCountZ();
////        switch (componentDimension.rows) {
////            case 1: {
////                return Maths.vector(values[Axis.X.index()].avg());
////            }
////            case 2: {
////                return Maths.vector(values[Axis.X.index()].avg(), values[Axis.Y.index()].avg());
////            }
////            case 3: {
////                return Maths.vector(values[Axis.X.index()].avg(), values[Axis.Y.index()].avg(), values[Axis.Z.index()].avg());
////            }
////        }
////        throw new UnsupportedComponentDimensionException(componentDimension.rows);
////    }
////
////    public DoubleToVector vsum() {
////        int cc = getCountX() * getCountY() * getCountZ();
////        switch (componentDimension.rows) {
////            case 1: {
////                Maths.vector(values[Axis.X.index()].sum());
////            }
////            case 2: {
////                Maths.vector(values[Axis.X.index()].sum(), values[Axis.Y.index()].sum());
////            }
////            case 3: {
////                Maths.vector(values[Axis.X.index()].sum(), values[Axis.Y.index()].sum(), values[Axis.Z.index()].sum());
////            }
////        }
////        throw new UnsupportedComponentDimensionException(componentDimension.rows);
////    }
////
////    public int getCountX() {
////        return values[Axis.X.index()].getCountX();
////    }
////
////    public int getCountY() {
////        return values[Axis.Y.index()].getCountX();
////    }
////
////    public int getCountZ() {
////        return values[Axis.Y.index()].getCountX();
////    }
////
////    public TMatrixVector add(TMatrixVector other) {
////        switch (componentDimension.rows) {
////            case 1: {
////                return new TMatrixVector(
////                        values[Axis.X.index()].add(other.getComponent(Axis.X))
////                );
////            }
////            case 2: {
////                return new TMatrixVector(
////                        values[Axis.X.index()].add(other.getComponent(Axis.X)),
////                        values[Axis.Y.index()].add(other.getComponent(Axis.Y))
////                );
////            }
////            case 3: {
////                return new TMatrixVector(
////                        values[Axis.X.index()].add(other.getComponent(Axis.X)),
////                        values[Axis.Y.index()].add(other.getComponent(Axis.Y)),
////                        values[Axis.Z.index()].add(other.getComponent(Axis.Z))
////                );
////            }
////        }
////        throw new UnsupportedComponentDimensionException(componentDimension.rows);
////    }
////
////    /**
////     * @return gradient
////     */
////    public TMatrixVector grad() {
////        switch (componentDimension.rows) {
////            case 1: {
////                CDiscrete fx = values[Axis.X.index()];
////                return new TMatrixVector(
////                        (fx.diff(Axis.X))
////                );
////            }
////            case 2: {
////                CDiscrete fx = values[Axis.X.index()];
////                CDiscrete fy = values[Axis.Y.index()];
////
////                return new TMatrixVector(
////                        (fx.diff(Axis.X)),
////                        (fy.diff(Axis.Y))
////                );
////            }
////            case 3: {
////                CDiscrete fx = values[Axis.X.index()];
////                CDiscrete fy = values[Axis.Y.index()];
////                CDiscrete fz = values[Axis.Z.index()];
////
////                return new TMatrixVector(
////                        (fx.diff(Axis.X)),
////                        (fy.diff(Axis.Y)),
////                        (fz.diff(Axis.Z))
////                );
////            }
////        }
////        throw new UnsupportedComponentDimensionException(componentDimension.rows);
////    }
////
////
////
////    public Matrix getMatrix(Axis axis, PlaneAxis plane, int index) {
////        return Maths.matrix(getArray(axis, plane.getNormalAxis(), index));
////    }
////
////    public Matrix getMatrix(Axis axis, Axis fixedNormalAxis, int index) {
////        return Maths.matrix(getComponent(axis).getArray(fixedNormalAxis, index));
////    }
////
////    public Complex[][] getArray(Axis axis, PlaneAxis plane, int index) {
////        return getArray(axis, plane.getNormalAxis(), index);
////    }
////
////    public Complex[][] getArray(Axis axis, Axis fixedNormalAxis, int index) {
////        return getComponent(axis).getArray(fixedNormalAxis, index);
////    }
////
////    public Complex getValueAt(Axis axis, int xIndex, int yIndex, int zIndex) {
////        return getComponent(axis).getValues()[zIndex][yIndex][xIndex];
////    }
////
////    /**
////     * Example : getVector(Axis.X, Axis.X, Axis.Y,0, Axis.Z,0)
////     * from the X cube (cubeAxis) of the vector
////     * retrieve the X axis values for y=0 (first index in y values)
////     * and z=0 (first index in z values)
////     *
////     * @param cubeAxis   axis of the cube
////     * @param fixedAxis1 first fixed axis
////     * @param index1
////     * @param fixedAxis2
////     * @param index2
////     * @return
////     */
////    public Vector getVector(Axis cubeAxis, Axis fixedAxis1, int index1, Axis fixedAxis2, int index2) {
////        return getComponent(cubeAxis).getVector(cubeAxis, fixedAxis1, index1, fixedAxis2, index2);
////    }
////
////    public Vector getVector(Axis cubeAxis) {
////        return getComponent(cubeAxis).getVector(cubeAxis);
////    }
////
////
////
////    @Override
////    public Expr getComponent(int row, int col) {
////        if (col == 0) {
////            return values[row];
////        }
////        throw new IllegalArgumentException("Invalid");
////    }
////
////    @Override
////    public String getComponentTitle(int row, int col) {
////        if (col == 0) {
////            return Axis.cartesianValues()[row].toString();
////        }
////        throw new IllegalArgumentException("Invalid");
////    }
////
////    @Override
////    public Domain getDomainImpl() {
////        return domain;
////    }
////
////    @Override
////    public boolean isInvariantImpl(Axis axis) {
////        return false;
////    }
////
////
//////    @Override
//////    public boolean isDDx() {
//////        return false;
//////    }
////
//////    @Override
//////    public IDDx toDDx() {
//////        throw new IllegalArgumentException("Unsupported");
//////    }
////
////
////    @Override
////    public Expr setParam(String name, Expr value) {
////        return this;
////    }
////
////    @Override
////    public List<Expr> getSubExpressions() {
////        return (List) Arrays.asList(values);
////    }
////
////
////    public Complex integrate(Domain domain, PlaneAxis axis, double fixedValue) {
////        ComponentDimension dims = getComponentDimension();
////        MutableComplex c = new MutableComplex();
////        for (int i = 0; i < componentDimension.rows; i++) {
////            c.add(getComponent(Axis.cartesianValues()[i]).integrate(domain, axis, fixedValue));
////        }
////        return c.toComplex();
////    }
////
////    public Complex integrate(Domain domain) {
////        ComponentDimension dims = getComponentDimension();
////        MutableComplex c = new MutableComplex();
////        for (int i = 0; i < componentDimension.rows; i++) {
////            c.add(getComponent(Axis.cartesianValues()[i]).integrate(domain));
////        }
////        return c.toComplex();
////    }
////
////    @Override
////    public boolean equals(Object o) {
////        if (this == o) return true;
////        if (!(o instanceof TMatrixVector)) return false;
////        if (!super.equals(o)) return false;
////
////        TMatrixVector vDiscrete = (TMatrixVector) o;
////
////        if (componentDimension != null ? !componentDimension.equals(vDiscrete.componentDimension) : vDiscrete.componentDimension != null)
////            return false;
////        if (domain != null ? !domain.equals(vDiscrete.domain) : vDiscrete.domain != null) return false;
////        if (!Arrays.equals(values, vDiscrete.values)) return false;
////
////        return true;
////    }
////
////    @Override
////    public int hashCode() {
////        int result = super.hashCode();
////        result = 31 * result + (values != null ? Arrays.hashCode(values) : 0);
////        result = 31 * result + (domain != null ? domain.hashCode() : 0);
////        result = 31 * result + (componentDimension != null ? componentDimension.hashCode() : 0);
////        return result;
////    }
////
////    public static TMatrixVector discretize(Expr expr, @IntValidator(min = 1, max = 100000) int xSamples, @IntValidator(min = 1, max = 100000) int ySamples, @IntValidator(min = 1, max = 100000) int zSamples) {
////        if (expr.isScalarExpr()) {
////            return new TMatrixVector(CDiscrete.discretize(expr, xSamples, ySamples, zSamples));
////        } else {
////            DoubleToVector v = expr.toDV();
////            ComponentDimension d = v.getComponentDimension();
////            if (d.columns == 1) {
////                if (d.rows == 1) {
////                    return new TMatrixVector(
////                            CDiscrete.discretize(v.getComponent(Axis.X), xSamples, ySamples, zSamples)
////                    );
////
////                } else if (d.rows == 2) {
////                    return new TMatrixVector(
////                            CDiscrete.discretize(v.getComponent(Axis.X), xSamples, ySamples, zSamples),
////                            CDiscrete.discretize(v.getComponent(Axis.Y), xSamples, ySamples, zSamples)
////                    );
////                } else if (d.rows == 3) {
////                    return new TMatrixVector(
////                            CDiscrete.discretize(v.getComponent(Axis.X), xSamples, ySamples, zSamples),
////                            CDiscrete.discretize(v.getComponent(Axis.Y), xSamples, ySamples, zSamples),
////                            CDiscrete.discretize(v.getComponent(Axis.Z), xSamples, ySamples, zSamples)
////                    );
////                }
////            }
////            throw new UnsupportedComponentDimensionException(d);
////        }
////    }
////
////    public static TMatrixVector discretize(Expr expr, Domain domain, Samples samples) {
////        if (expr.isScalarExpr()) {
////            return new TMatrixVector(CDiscrete.discretize(expr, domain, samples));
////        } else {
////            DoubleToVector v = expr.toDV();
////            ComponentDimension d = v.getComponentDimension();
////            if (d.columns == 1) {
////                if (d.rows == 1) {
////                    return new TMatrixVector(
////                            CDiscrete.discretize(v.getComponent(Axis.X), domain, samples)
////                    );
////
////                } else if (d.rows == 2) {
////                    return new TMatrixVector(
////                            CDiscrete.discretize(v.getComponent(Axis.X), domain, samples),
////                            CDiscrete.discretize(v.getComponent(Axis.Y), domain, samples)
////                    );
////                } else if (d.rows == 3) {
////                    return new TMatrixVector(
////                            CDiscrete.discretize(v.getComponent(Axis.X), domain, samples),
////                            CDiscrete.discretize(v.getComponent(Axis.Y), domain, samples),
////                            CDiscrete.discretize(v.getComponent(Axis.Z), domain, samples)
////                    );
////                }
////            }
////            throw new UnsupportedComponentDimensionException(d);
////        }
////    }
////
////
////    @Override
////    public Expr add(Expr other) {
////        CDiscrete[] values2 = new CDiscrete[values.length];
////        for (int i = 0; i < values2.length; i++) {
////            Expr e = values2[i].add(other);
////            if (e instanceof CDiscrete) {
////                values2[i] = (CDiscrete) e;
////            } else {
////                return super.add(other);
////            }
////        }
////        return create(values2);
////    }
////
////    @Override
////    public Expr sub(Expr other) {
////        CDiscrete[] values2 = new CDiscrete[values.length];
////        for (int i = 0; i < values2.length; i++) {
////            Expr e = values2[i].sub(other);
////            if (e instanceof CDiscrete) {
////                values2[i] = (CDiscrete) e;
////            } else {
////                return super.sub(other);
////            }
////        }
////        return create(values2);
////    }
////
////    @Override
////    public Expr mul(Expr other) {
////        CDiscrete[] values2 = new CDiscrete[values.length];
////        for (int i = 0; i < values2.length; i++) {
////            Expr e = values2[i].mul(other);
////            if (e instanceof CDiscrete) {
////                values2[i] = (CDiscrete) e;
////            } else {
////                return super.mul(other);
////            }
////        }
////        return create(values2);
////    }
////
////    @Override
////    public Expr div(Expr other) {
////        CDiscrete[] values2 = new CDiscrete[values.length];
////        for (int i = 0; i < values2.length; i++) {
////            Expr e = values2[i].div(other);
////            if (e instanceof CDiscrete) {
////                values2[i] = (CDiscrete) e;
////            } else {
////                return super.div(other);
////            }
////        }
////        return create(values2);
////    }
//
//    private static <T> TMatrixVector<T> create(Matrix<T>[] values2) {
//        switch (values2.length) {
//            case 1: {
//                return new TMatrixVector<T>(values2[0]);
//            }
//            case 2: {
//                return new TMatrixVector(values2[0], values2[1]);
//            }
//            case 3: {
//                return new TMatrixVector(values2[0], values2[1], values2[2]);
//            }
//        }
//        throw new IllegalArgumentException("Invalid dimension " + values2.length);
//    }
//
////    @Override
////    public int getCubesCount() {
////        return componentDimension.rows;
////    }
////
////    @Override
////    public PlotCube getCube(int index) {
////        CDiscrete i = getComponent(Axis.cartesianValues()[index]);
////        return new PlotCube(
////                i.getX(), i.getY(), i.getZ(),
////                i.getValues()
////        );
////    }
//}
