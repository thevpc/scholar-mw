package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.geom.Geometry;
import net.vpc.scholar.hadrumaths.symbolic.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 juil. 2007 15:45:33
 */
public interface Expr extends Serializable {

    /**
     * @param axis
     * @return true if expression does not depend on Axis
     */
    boolean isInvariant(Axis axis);

    /**
     * @return true if this expression is defined as a valid non param, non constrained double value (full domain)
     */
    boolean isDouble();

    /**
     * <table>
     * <tr><th>Expr</th><th>Type</th><th>isDoubleExpr</th><th>isDoubleTyped</th><th>isDouble</th></tr>
     * <tr><td>2.0</td><td>DoubleValue</td><td>true</td><td>true</td><td>true</td></tr>
     * <tr><td>2.0*II(0,10)</td><td>DoubleValue</td><td>true</td><td>true</td><td>false</td></tr>
     * <tr><td>2.0*II(0,10)</td><td>ComplexValue</td><td>true</td><td>true</td><td>false</td></tr>
     * <tr><td>(2.0+2.0î)*II(0,10)</td><td>ComplexValue</td><td>false</td><td>false</td><td>false</td></tr>
     * <tr><td>(2.0)*II(0,10)</td><td>Mul</td><td>true</td><td>true</td><td>true</td></tr>
     * <tr><td>(2.0+2.0î)*II(0,10)</td><td>Mul</td><td>false</td><td>false</td><td>false</td></tr>
     * <tr><td>X</td><td>XX</td><td>false</td><td>true</td><td>false</td></tr>
     * <tr><td>X+î</td><td>Plus</td><td>false</td><td>false</td><td>false</td></tr>
     * <tr><td>param("m")</td><td>Param</td><td>false</td><td>true</td><td>false</td></tr>
     * </table>
     *
     * @return true if this expression is defined as a valid,non function, non param, (may be) constrained double value
     */
    boolean isDoubleExpr();

    /**
     * true if this expression is defined only for double values and is not handling complex values
     *
     * @return
     */
    boolean isDoubleTyped();

    /**
     * @return true if this expression is defined as a valid non param, non constrained complex value (full domain)
     */
    boolean isComplex();

    boolean isComplexExpr();


    /**
     * defines a scalar expression aka defines only for X axis
     * (non vector nor matrix or a vector and matrix with the very first element only defined)
     *
     * @return
     */
    boolean isScalarExpr();

    /**
     * @return true if this expression is defined as a valid non param, non constrained matrix (full domain)
     */
    boolean isMatrix();

    /**
     * return complex value if this expression is defined as a valid non param complex value. It may have domain. Otherwise throws ClassCastException
     *
     * @return complex value if this expression is defined as a valid non param complex value. It may have domain. Otherwise throws ClassCastException
     */
    default Complex toComplex() {
        throw new ClassCastException(toString() + " of type " + getClass().getName() + " cannot be casted to Complex");
    }

    /**
     * return double value if this expression is defined as a valid non param double value. It may have domain. Otherwise throws ClassCastException
     *
     * @return double value if this expression is defined as a valid non param double value. It may have domain. Otherwise throws ClassCastException
     */
    double toDouble();

    default Matrix toMatrix() {
        throw new ClassCastException(toString() + " of type " + getClass().getName() + " cannot be casted to Matrix");
    }

    boolean isDC();

    default DoubleToComplex toDC() {
        if (!isDC()) {
            throw new ClassCastException("Unable to Cast to DC :: " + getClass().getName() + " = " + toString());
        }
        return (DoubleToComplex) this;
    }

    boolean isDD();

    default DoubleToDouble toDD() {
        if (!isDD()) {
            throw new ClassCastException("Unable to Cast to DD :: " + getClass().getName() + " = " + toString());
        }
        return (DoubleToDouble) this;
    }

//    boolean isDDx();

//    IDDx toDDx();

    boolean isDV();

    default DoubleToVector toDV() {
        if (!isDV()) {
            throw new ClassCastException("Unable to Cast to DV :: " + getClass().getName() + " = " + toString());
        }
        return (DoubleToVector) this;
    }

    boolean isDM();

    default DoubleToMatrix toDM() {
        if (!isDM()) {
            throw new ClassCastException("Unable to Cast to DM :: " + getClass().getName() + " = " + toString());
        }
        return (DoubleToMatrix) this;
    }

    boolean isZero();

    boolean isNaN();

    boolean hasParams();

    Expr setParam(String name, double value);

    Expr setParam(String name, Expr value);

    Set<ParamExpr> getParams();

    Expr setParam(ParamExpr paramExpr, double value);

    Expr setParam(ParamExpr paramExpr, Expr value);

    boolean isInfinite();

    Expr clone();

    List<Expr> getSubExpressions();

    boolean hasProperties();

    Integer getIntProperty(String name);

    Long getLongProperty(String name);

    String getStringProperty(String name);

    Double getDoubleProperty(String name);

    Object prop(String name);

    Object getProperty(String name);

    Map<String, Object> getProperties();

    Expr setProperties(Map<String, Object> map);

    Expr setMergedProperties(Map<String, Object> map);

    Expr setProperties(Map<String, Object> map, boolean merge);

    Expr setProperty(String name, Object value);

    Expr composeX(Expr xreplacement);

    Expr composeY(Expr yreplacement);

    /**
     * simple call to Maths.simplify(this);
     *
     * @return
     */
    Expr simplify();

    /**
     * simple call to Maths.normalizeString(this);
     *
     * @return
     */
    Expr normalize();

    String getTitle();

    /**
     * create a clone expression with changed name
     *
     * @param name
     */
    Expr setTitle(String name);

    int getDomainDimension();

    Domain getDomain();

    Domain domain();

    ComponentDimension getComponentDimension();

    Expr mul(Domain domain);

    Expr multiply(Domain domain);

    Expr mul(Geometry domain);

    Expr multiply(Geometry domain);

    Expr mul(int other);

    Expr mul(double other);

    Expr mul(Complex other);

    Expr mul(Expr other);

    Expr multiply(int other);

    Expr multiply(double other);

    Expr multiply(Expr other);

    Expr add(int other);

    Expr add(double other);

    Expr add(Expr other);

    Expr divide(int other);

    Expr divide(double other);

    Expr divide(Expr other);

    Expr div(int other);

    Expr div(double other);

    Expr div(Expr other);

    Expr subtract(int other);

    Expr subtract(double other);

    Expr subtract(Expr other);

    Expr sub(int other);

    Expr sub(double other);

    Expr sub(Expr other);

    Expr negate();

    Expr neg();

    Expr rdiv(double other);

    Expr rmul(double other);

    Expr radd(double other);

    Expr rsub(double other);

    default TList<Expr> asSeq(DoubleParamFromTo d) {
        return MathsBase.seq(this, d.getParam(), d.values());
    }

    default TList<Expr> asSeq(DoubleParamFromTo2 d) {
        return MathsBase.seq(this,
                new DoubleParam[]{d.getParam1(), d.getParam2()},
                d.values()
        );
    }
}
