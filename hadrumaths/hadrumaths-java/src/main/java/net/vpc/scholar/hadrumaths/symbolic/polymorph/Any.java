/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic.polymorph;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.random.CanProduceClass;
import net.vpc.scholar.hadrumaths.random.IgnoreRandomGeneration;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Inv;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Sub;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransform;

import java.util.*;

/**
 * @author vpc
 */
@CanProduceClass({DoubleToDouble.class, DoubleToComplex.class, DoubleToVector.class, DoubleToMatrix.class})
public abstract class Any implements ExprRef {
    private static final long serialVersionUID = 1L;

    static {
        ExpressionTransformFactory.setExpressionTransformer(Any.class, ExpressionTransform.class, (expression, transform) -> {
            Any e = (Any) expression;
            return of(ExpressionTransformFactory.transform(e.object, transform));
        });
    }

    private final Map<String, Object> properties;
    protected Expr object;
    protected String name;

//    public Any(Expr object) {
//        this.object = unwrap(object);
//    }


    public Any(Expr object, String name, Map<String, Object> properties) {
        this.name = name;
        this.properties = properties == null ? Collections.EMPTY_MAP : Collections.unmodifiableMap(new HashMap<>(properties));
        this.object = unwrap(object);
    }

    @IgnoreRandomGeneration
    public static Expr unwrap(Expr e) {
        return !(e instanceof Any) ? e : (((Any) e).object);
    }

    public Any scalarProduct(Expr e) {
        Expr first = object;
        Expr second = unwrap(e);
        return of(
                Maths.scalarProduct(first, second)
        );
    }

    public static Any of(Expr object) {
        return of(object, object.getTitle(), object.getProperties());
    }

    @IgnoreRandomGeneration
    public static Any of(Expr e, String title, Map<String, Object> properties) {
        if (e instanceof Any) {
            e = ((Any) e).getReference();
        }
        switch (e.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                return new AnyDoubleToDouble(e, title, properties);
            }
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                return new AnyDoubleToComplex(e, title, properties);
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR: {
                return new AnyDoubleToVector(e, title, properties);
            }
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX: {
                return new AnyDoubleToMatrix(e, title, properties);
            }
            default: {
                throw new IllegalArgumentException("Unsupported " + e.getType());
            }
        }
    }

    public Expr getReference() {
        return object;
    }

    public DoubleDomainExpr toD() {
        if (object.is(ExprType.DOUBLE_DOUBLE)) {
            return object.toDD();
        }
        return object.toDC();
    }

    public DoubleToDouble getRealDD() {
        return of(object.toDC().getRealDD()).toDD();
    }

//    protected boolean isZeroImpl() {
//        return object.isZero();
//    }

//    protected boolean isInfiniteImpl() {
//        return object.isInfinite();
//    }
//
//    protected boolean isInvariantImpl(Axis axis) {
//        return object.isInvariant(axis);
//    }

    public DoubleToDouble getImagDD() {
        return of(object.toDC().getImagDD()).toDD();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass().getName(), object, name, properties);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Any any = (Any) o;
        return Objects.equals(object, any.object) &&
                Objects.equals(name, any.name) &&
                Objects.equals(properties, any.properties);
    }

    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }

    @Override
    public boolean isInvariant(Axis axis) {
        return object.isInvariant(axis);
    }

    @Override
    public Complex toComplex() {
        return object.toComplex();
    }

    public Any inv() {
        Expr first = object;
        return of(
                Inv.of(first)
        );
    }

    @Override
    public double toDouble() {
        return object.toDouble();
    }

    @Override
    public boolean isZero() {
        return object.isZero();
    }

    @Override
    public boolean isNaN() {
        return object.isNaN();
    }

    public List<Expr> getChildren() {
        return Arrays.asList(object);
    }

    @Override
    public boolean hasParams() {
        return object.hasParams();
    }

    @Override
    public Expr setParams(ParamValues params) {
        return of(
                object.setParams(params),
                getTitle(), getProperties()
        );
    }

    @Override
    public Any setParam(String name, double value) {
        return of(
                object.setParam(name, value),
                getTitle(), getProperties()
        );
    }

    @Override
    public Any setParam(String name, Expr value) {
        Expr e = object.setParam(name, value);
        if (e == object) {
            return this;
        }
        return of(e, getTitle(), getProperties());
    }

//    @Override
//    public boolean isDM() {
//        return object.isDM();
//    }

//    @Override
//    public boolean isDV() {
//        return object.isDV();
//    }

    @Override
    public Expr setParam(Param paramExpr, double value) {
        return of(object.setParam(paramExpr, value));
    }

    @Override
    public Expr setParam(Param paramExpr, Expr value) {
        return of(object.setParam(paramExpr, value));
    }

    @Override
    public boolean isInfinite() {
        return object.isInfinite();
    }

    @Override
    public boolean hasProperties() {
        return properties != null && properties.size() > 0;
    }

//    @Override
//    public DoubleToComplex toDC() {
//        return new AnyDoubleToComplex(object.toDC(), name, properties);
//    }

//    @Override
//    public DoubleToDouble toDD() {
//        return new AnyDoubleToDouble(object.toDD(), name, properties);
//    }


//    @Override
//    public Vector[] computeVector(double[] x, double y, Domain d0, Out<Range> ranges) {
//        return toDV().computeVector(x, y, d0, ranges);
//    }

//    @Override
//    public Vector[] computeVector(double x, double[] y, Domain d0, Out<Range> ranges) {
//        return toDV().computeVector(x, y, d0, ranges);
//    }

//    @Override
//    public DoubleToVector toDV() {
//        return new AnyDoubleToVector(object.toDV(), name, properties);
//    }
//
//    @Override
//    public DoubleToMatrix toDM() {
//        return new AnyDoubleToMatrix(object.toDM(), name, properties);
//    }

    @Override
    public Map<String, Object> getProperties() {
        if (properties == null) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(properties);
    }

    @Override
    public Expr setProperties(Map<String, Object> map, boolean merge) {
        if (map == null || map.isEmpty()) {
            return this;
        }
        if (merge) {
            Map<String, Object> map2 = new HashMap<>(getProperties());
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Object v = entry.getValue();
                if (v == null) {
                    map2.remove(entry.getKey());
                } else {
                    map2.put(entry.getKey(), v);
                }
            }
            map2.putAll(map);
            return Any.of(object, name, map2);
        } else {
            return Any.of(object, name, map);
        }
    }
    //    @Override
//    public boolean isDouble() {
//        return object.isDouble();
//    }

//    public boolean isDoubleExpr() {
//        return object.isDoubleExpr();
//    }

    @Override
    public Expr simplify(SimplifyOptions options) {
        if (options == null) {
            options = SimplifyOptions.DEFAULT;
        }
        String t = getTitle();
        Expr o = object.simplify();
        if (options.isPreserveRootName() && t != null) {
            return o.setTitle(t);
        }
        return o;
    }

    @Override
    public Expr normalize() {
        return of(object.normalize());
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return object.getComponentDimension();
    }

    public Any mul(Complex e) {
        return of(object.mul(e));
    }

    public Any add(int e) {
        return add(Maths.expr(e));
    }

//    @Override
//    public Map<String, Object> getProperties() {
//        return getObject().getProperties();
//    }
//
//    @Override
//    public Expr setProperties(Map<String, Object> map) {
//        return wrap(getObject().setProperties(map));
//    }

    public Any add(double e) {
        return add(Maths.expr(e));
    }

    public Any add(Expr e) {
        Expr first = object;
        Expr second = unwrap(e);
        return of(
                Maths.sum(first, second)
        );
    }

    public String getTitle() {
        if (name != null) {
            return name;
        }
        return object.getTitle();
    }

    @Override
    public Expr setTitle(String name) {
        return Any.of(object, name, properties);
    }

    @Override
    public boolean isSmartMulDouble() {
        return object.isSmartMulDouble();
    }

    @Override
    public boolean isSmartMulComplex() {
        return object.isSmartMulComplex();
    }

    @Override
    public boolean isSmartMulDomain() {
        return object.isSmartMulDomain();
    }

    public Any mul(Domain e) {
        return of(object.mul(e));
    }

    public Any mul(double e) {
        return of(object.mul(e));
    }

    public Any mul(Expr e) {
        Expr first = object;
        Expr second = (e instanceof Any) ? ((Any) e).object : e;
        return of(
                first.mul(second)
        );
    }

    public Any div(int e) {
        return div(Maths.expr(e));
    }

    public Any div(double e) {
        return div(Maths.expr(e));
    }

    public Any div(Expr e) {
        Expr first = object;
        Expr second = unwrap(e);
        return of(
                Maths.div(first, second)
        );
    }

    public Any rem(Expr e) {
        Expr first = object;
        Expr second = unwrap(e);
        return of(
                Maths.rem(first, second)
        );
    }

    public Any pow(Expr e) {
        Expr first = object;
        Expr second = unwrap(e);
        return of(
                Maths.pow(first, second)
        );
    }

    public Any sub(int e) {
        return sub(Maths.expr(e));
    }

    public Any sub(double e) {
        return sub(Maths.expr(e));
    }

    public Any sub(Expr e) {
        Expr first = object;
        Expr second = unwrap(e);
        return of(
                Sub.of(first, second)
        );
    }

    public Any eq(Expr e) {
        Expr first = object;
        Expr second = unwrap(e);
        return of(
                Maths.eq(first, second)
        );
    }

    public Any ne(Expr e) {
        Expr first = object;
        Expr second = unwrap(e);
        return of(
                Maths.ne(first, second)
        );
    }

    public Any lt(Expr e) {
        Expr first = object;
        Expr second = unwrap(e);
        return of(
                Maths.lt(first, second)
        );
    }

    public Any lte(Expr e) {
        Expr first = object;
        Expr second = unwrap(e);
        return of(
                Maths.lte(first, second)
        );
    }

    public Any gt(Expr e) {
        Expr first = object;
        Expr second = unwrap(e);
        return of(
                Maths.gt(first, second)
        );
    }

    public Any gte(Expr e) {
        Expr first = object;
        Expr second = unwrap(e);
        return of(
                Maths.gte(first, second)
        );
    }

    public Any cos() {
        return of(
                Maths.cos(object)
        );
    }

    public Any sin() {
        return of(
                Maths.sin(object)
        );
    }

    public Any tan() {
        return of(
                Maths.tan(object)
        );
    }

    public Any cotan() {
        return of(
                Maths.cotan(object)
        );
    }

    @Override
    public Domain getDomain() {
        return object.getDomain();
    }

//    @Override
//    public boolean isDoubleTyped() {
//        return object.isDoubleTyped();
//    }

    @Override
    public Object getProperty(String name) {
        return properties != null ? properties.get(name) : null;
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return Any.of(subExpressions[0]);
    }

    public Any exp() {
        return of(
                Maths.exp(object)
        );
    }


}

class AnyDoubleToVector extends Any implements DoubleToVector {
    public AnyDoubleToVector(Expr object, String name, Map<String, Object> properties) {
        super(object, name, properties);
    }

    @Override
    public DoubleToComplex getComponent(Axis a) {
        return
                //wrap(
                (DoubleToComplex) object.toDV().getComponent(a)
                //)
                ;
    }

    @Override
    public ComplexVector evalVector(double x, double y, double z, BooleanMarker defined) {
        return object.toDV().evalVector(x, y, z, defined);
    }

    @Override
    public ComplexVector[][] evalVector(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return toDV().evalVector(x, y, d0, ranges);
    }

    @Override
    public ComplexVector[][][] evalVector(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return toDV().evalVector(x, y, z, d0, ranges);
    }

    @Override
    public ComplexVector[] evalVector(double[] x, Domain d0, Out<Range> ranges) {
        return toDV().evalVector(x, d0, ranges);
    }

    @Override
    public ComplexVector evalVector(double x, BooleanMarker defined) {
        return object.toDV().evalVector(x, defined);
    }

    @Override
    public ComplexVector evalVector(double x, double y, BooleanMarker defined) {
        return object.toDV().evalVector(x, y, defined);
    }

    @Override
    public int getComponentSize() {
        return object.toDV().getComponentSize();
    }
}

class AnyDoubleToComplex extends Any implements DoubleToComplex {
    public AnyDoubleToComplex(Expr object, String name, Map<String, Object> properties) {
        super(object, name, properties);
    }

    @Override
    public Complex[] evalComplex(double[] x, Domain d0, Out<Range> ranges) {
        return object.toDC().evalComplex(x, d0, ranges);
    }

    @Override
    public Complex[] evalComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
        return object.toDC().evalComplex(x, y, d0, ranges);
    }

    @Override
    public Complex[] evalComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
        return object.toDC().evalComplex(x, y, d0, ranges);
    }

    @Override
    public Complex[][][] evalComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return object.toDC().evalComplex(x, y, z, d0, ranges);
    }

    @Override
    public Complex[][] evalComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return object.toDC().evalComplex(x, y, d0, ranges);
    }

    @Override
    public Complex evalComplex(double x) {
        return object.toDC().evalComplex(x);
    }

    @Override
    public Complex evalComplex(double x, BooleanMarker defined) {
        return object.toDC().evalComplex(x, defined);
    }

    @Override
    public Complex evalComplex(double x, double y) {
        return object.toDC().evalComplex(x, y);
    }

    @Override
    public Complex evalComplex(double x, double y, BooleanMarker defined) {
        return object.toDC().evalComplex(x, y, defined);
    }

    @Override
    public Complex evalComplex(double x, double y, double z) {
        return object.toDC().evalComplex(x, y, z);
    }

    @Override
    public Complex evalComplex(double x, double y, double z, BooleanMarker defined) {
        return object.toDC().evalComplex(x, y, z, defined);
    }

}


class AnyDoubleToDouble extends Any implements DoubleToDoubleDefaults.DoubleToDoubleNormal {
    public AnyDoubleToDouble(Expr object, String name, Map<String, Object> properties) {
        super(object, name, properties);
    }

    @Override
    public double evalDouble(double x, double y, double z) {
        return object.toDD().evalDouble(x, y, z);
    }

    @Override
    public double evalDouble(double x, double y, double z, BooleanMarker defined) {
        return object.toDD().evalDouble(x, y, z, defined);
    }

    @Override
    public double evalDouble(double x) {
        return object.toDD().evalDouble(x);
    }

    @Override
    public double evalDouble(double x, BooleanMarker defined) {
        return object.toDD().evalDouble(x, defined);
    }

    @Override
    public double evalDouble(double x, double y) {
        return object.toDD().evalDouble(x, y);
    }

    @Override
    public double evalDouble(double x, double y, BooleanMarker defined) {
        return object.toDD().evalDouble(x, y, defined);
    }

    @Override
    public boolean contains(double x) {
        return toD().contains(x);
    }

    @Override
    public boolean contains(double x, double y) {
        return toD().contains(x, y);
    }

    @Override
    public boolean contains(double x, double y, double z) {
        return toD().contains(x, y, z);
    }

    @Override
    public double[] evalDouble(double[] x, double y, Domain d0, Out<Range> ranges) {
        return object.toDD().evalDouble(x, y, d0, ranges);
    }

    @Override
    public double[] evalDouble(double[] x, Domain d0, Out<Range> range) {
        return object.toDD().evalDouble(x, d0, range);
    }

    @Override
    public double[] evalDouble(double x, double[] y, Domain d0, Out<Range> ranges) {
        return object.toDD().evalDouble(x, y, d0, ranges);
    }

    @Override
    public double[][][] evalDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return object.toDD().evalDouble(x, y, z, d0, ranges);
    }

    @Override
    public double[][] evalDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return object.toDD().evalDouble(x, y, d0, ranges);
    }
}

class AnyDoubleToMatrix extends Any implements DoubleToMatrix {
    public AnyDoubleToMatrix(Expr object, String name, Map<String, Object> properties) {
        super(object, name, properties);
    }

    @Override
    public String getComponentTitle(int row, int col) {
//        System.out.println(this+" : getComponentTitle("+col+", "+row+")");
        return object.toDM().getComponentTitle(row, col);
    }

    @Override
    public ComplexMatrix[] evalMatrix(double[] x, double y, Domain d0, Out<Range> ranges) {
        return object.toDM().evalMatrix(x, y, d0, ranges);
    }

    @Override
    public ComplexMatrix[] evalMatrix(double x, double[] y, Domain d0, Out<Range> ranges) {
        return object.toDM().evalMatrix(x, y, d0, ranges);
    }

    @Override
    public ComplexMatrix evalMatrix(double x) {
        return object.toDM().evalMatrix(x);
    }

    @Override
    public ComplexMatrix evalMatrix(double x, BooleanMarker defined) {
        return object.toDM().evalMatrix(x, defined);
    }

    @Override
    public ComplexMatrix evalMatrix(double x, double y) {
        return object.toDM().evalMatrix(x, y);
    }

    @Override
    public ComplexMatrix evalMatrix(double x, double y, BooleanMarker defined) {
        return object.toDM().evalMatrix(x, y, defined);
    }

    @Override
    public ComplexMatrix evalMatrix(double x, double y, double z) {
        return object.toDM().evalMatrix(x, y, z);
    }

    @Override
    public ComplexMatrix evalMatrix(double x, double y, double z, BooleanMarker defined) {
        return object.toDM().evalMatrix(x, y, z, defined);
    }

    @Override
    public ComplexMatrix[] evalMatrix(double[] x, Domain d0, Out<Range> ranges) {
        return object.toDM().evalMatrix(x, d0, ranges);
    }

    @Override
    public ComplexMatrix[][] evalMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return object.toDM().evalMatrix(x, y, d0, ranges);
    }

    @Override
    public ComplexMatrix[][][] evalMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return object.toDM().evalMatrix(x, y, z, d0, ranges);
    }

    @Override
    public Expr getComponent(int row, int col) {
        return of(object.toDM().getComponent(row, col));
    }
}
