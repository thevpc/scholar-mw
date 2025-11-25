package net.thevpc.scholar.hadrumaths;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.scholar.hadrumaths.geom.Geometry;
import net.thevpc.scholar.hadrumaths.symbolic.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 juil. 2007 15:45:33
 */
public interface Expr extends HSerializable {

    /**
     * @param axis
     * @return true if expression does not depend on Axis
     */
    default boolean isInvariant(Axis axis) {
        return true;
    }

    default boolean isUnbounded() {
        return false;
    }


    /**
     * return complex value if this expression is defined as a valid non param
     * complex value. It may have domain. Otherwise throws ClassCastException
     *
     * @return complex value if this expression is defined as a valid non param
     * complex value. It may have domain. Otherwise throws ClassCastException
     */
    default Complex toComplex() {
        throw new ExprNarrowException(toString() + " of type " + getClass().getName() + " cannot be casted to Complex");
    }

    default boolean isCstComplex() {
        return false;
    }

    default boolean isCstDouble() {
        return false;
    }


    default NumberExpr toNumber() {
        return ExprDefaults.toNumber(this);
    }

    default Expr inv() {
        return ExprDefaults.inv(this);
    }

    default Expr not() {
        return ExprDefaults.not(this);
    }

    /**
     * return double value if this expression is defined as a valid non param
     * double value. It may have domain. Otherwise throws ClassCastException
     *
     * @return double value if this expression is defined as a valid non param
     * double value. It may have domain. Otherwise throws ClassCastException
     */
    default double toDouble() {
        throw new ExprNarrowException(toString() + " of type " + getClass().getName() + " cannot be casted to double");
    }

    default boolean toBoolean() {
        return toDouble() != 0;
    }

    default Expr cast(ExprType other) {
        if (!isNarrow(other)) {
            throw new ExprNarrowException("Unable to Cast " + getType() + " to " + other
                    + ""
                    + " :: " + getClass().getName() + " = " + toString());
        }
        return this;
    }

    /**
     * tries to if cast down is possible
     *
     * @param other
     * @return
     */
    default boolean isNarrow(ExprType other) {
        return ExprDefaults.isNarrow(this, other);
    }

    ExprType getType(); //ExprDefaults.widest(getChildren());

    /**
     * tries to if cast down is possible
     *
     * @param other
     * @return
     */
    default boolean isNarrow(ExprNumberType other) {
        return ExprDefaults.isNarrow(this, other);
    }

    default boolean is(ExprNumberType other) {
        return ExprDefaults.is(this, other);
    }

    default boolean is(ExprDim other) {
        return ExprDefaults.is(this, other);
    }

    default boolean isNarrow(ExprDim other) {
        return ExprDefaults.isNarrow(this, other);
    }

    default Expr narrow(ExprNumberType other) {
        return ExprDefaults.narrow(this, other);
    }

    default Expr narrow(ExprDim other) {
        return ExprDefaults.narrow(this, other);
    }

    default Expr narrow() {
        return narrow(getNarrowType());
    }

    default Expr narrow(ExprType other) {
        return ExprDefaults.narrow(this, other);
    }

    /**
     * return the narrowest (most specific) def type. For instance a complex
     * real value is narrowed to CONSTANT_DOUBLE
     *
     * @return
     */
    ExprType getNarrowType();

    default boolean is(ExprType other) {
        return ExprDefaults.is(this, other);
    }

//    boolean isDDx();
//    IDDx toDDx();
//    boolean isDV();
    DoubleToComplex toDC();

    DoubleToDouble toDD();

    DoubleToVector toDV();

    DoubleToMatrix toDM();

    default boolean isZero() {
        return false;
    }

    default boolean isNaN() {
        return ExprDefaults.isNaNAny(getChildren());
    }

    default List<Expr> getChildren() {
        return Collections.emptyList();
    }

    default boolean hasParams() {
        return ExprDefaults.hasParamsAny(getChildren());
    }

    default Expr setParams(ParamValues params) {
        return ExprDefaults.setParams(this, params);
    }

    default Expr setParam(String name, double value) {
        return ExprDefaults.setParam(this, name, value);
    }

    default Expr setParam(String name, Expr value) {
        return ExprDefaults.setParam(this, name, value);
    }

    default Set<Param> getParams() {
        return ExprDefaults.getParams(this);
    }

    default Expr setParam(Param paramExpr, double value) {
        return ExprDefaults.setParam(this, paramExpr, value);
    }

    default Expr setParam(Param paramExpr, Expr value) {
        return ExprDefaults.setParam(this, paramExpr, value);
    }

    default boolean isInfinite() {
        return false;
    }

    default boolean isEvaluatable() {
        return ExprDefaults.isEvaluatableAll(getChildren());
    }

    default Expr getChild(int index) {
        return getChildren().get(index);
    }

    default boolean hasProperties() {
        return ExprDefaults.hasProperties(this);
    }

    default Integer getIntProperty(String name) {
        return ExprDefaults.getIntProperty(this, name);
    }

    default Long getLongProperty(String name) {
        return ExprDefaults.getLongProperty(this, name);
    }

    default String getStringProperty(String name) {
        return ExprDefaults.getStringProperty(this, name);
    }

    default Double getDoubleProperty(String name) {
        return ExprDefaults.getDoubleProperty(this, name);
    }

    default Map<String, Object> getProperties() {
        return ExprDefaults.getProperties(this);
    }

    default Expr setProperties(Map<String, Object> map) {
        return ExprDefaults.setProperties(this, map);
    }

    default Expr setMergedProperties(Map<String, Object> map) {
        return ExprDefaults.setMergedProperties(this, map);
    }

    default Expr setProperties(Map<String, Object> map, boolean merge) {
        return ExprDefaults.setProperties(this, map, merge);
    }

    default Expr setProperty(String name, Object value) {
        return ExprDefaults.setProperty(this, name, value);
    }

    default Expr compose(Expr xreplacement) {
        return ExprDefaults.compose(this, xreplacement);
    }

    default Expr compose(Expr xreplacement, Expr yreplacement) {
        return ExprDefaults.compose(this, xreplacement, yreplacement);
    }

    default Expr compose(Expr xreplacement, Expr yreplacement, Expr zreplacement) {
        return ExprDefaults.compose(this, xreplacement, yreplacement, zreplacement);
    }

    /**
     * simple call to Maths.simplify(this);
     *
     * @return
     */
    default Expr simplify() {
        return simplify(null);
    }

    default Expr simplify(SimplifyOptions options) {
        return ExprDefaults.simplify(this, options);
    }

    /**
     * simple call to Maths.normalizeString(this);
     *
     * @return
     */
    default Expr normalize() {
        return ExprDefaults.normalize(this);
    }

    ComponentDimension getComponentDimension();

    default Expr mul(Complex other) {
        return ExprDefaults.mul(this, other);
    }

    default Expr plus(int other) {
        return ExprDefaults.add(this, other);
    }

    default Expr plus(double other) {
        return ExprDefaults.add(this, other);
    }

    default Expr plus(Expr other) {
        return ExprDefaults.add(this, other);
    }

    default Expr rdiv(double other) {
        return ExprDefaults.rdiv(this, other);
    }

    default Expr rmul(double other) {
        return ExprDefaults.rmul(this, other);
    }

    default Expr radd(double other) {
        return ExprDefaults.radd(this, other);
    }

    default Expr rsub(double other) {
        return ExprDefaults.rsub(this, other);
    }

    /**
     * create an inflated list of all values of the given param This method is
     * equivalent to {@link #inflate(DoubleParamValues)}
     *
     * @param paramValues params and values to inflate with
     * @return new inflated list of all values of the given param
     */
    default ExprVector allOf(DoubleParamValues paramValues) {
        return inflate(paramValues);
    }

    /**
     * create an inflated list of all values of the given param. This method is
     * equivalent to {@link #allOf(DoubleParamValues)}
     *
     * @param paramValues params and values to inflate with
     * @return new inflated list of all values of the given param
     */
    default ExprVector inflate(DoubleParamValues paramValues) {
        return Maths.evector(this).inflate(paramValues, getTitle());
    }

    default String getTitle() {
        return ExprDefaults.getTitle(this);
    }

    /**
     * create a clone expression with changed name
     *
     * @param name
     */
    default Expr setTitle(String name) {
        return ExprDefaults.setTitle(this, name);
    }

    /**
     * create a singleton list containing this expression.
     *
     * @return a singleton list containing this expression.
     */
    default ExprVector toList() {
        return Maths.evector(this);
    }

    default boolean isSmartMulDouble() {
        return false;
    }

    default boolean isSmartMulComplex() {
        return false;
    }

    default boolean isSmartMulDomain() {
        return false;
    }

    default NElement toElement() {
        return ExprDefaults.toElement(this);
    }

    default Expr mul(Domain domain) {
        return ExprDefaults.mul(this, domain);
    }

    default Expr mul(Geometry domain) {
        return ExprDefaults.mul(this, domain);
    }

    default Expr mul(int other) {
        return ExprDefaults.mul(this, other);
    }

    default Expr mul(double other) {
        return ExprDefaults.mul(this, other);
    }

    default Expr mul(Expr other) {
        return ExprDefaults.mul(this, other);
    }

    default Expr div(int other) {
        return ExprDefaults.div(this, other);
    }

    /// USER-CENTRIC
    default Expr div(double other) {
        return ExprDefaults.div(this, other);
    }

    default Expr div(Expr other) {
        return ExprDefaults.div(this, other);
    }

    default Expr rem(Expr other) {
        return ExprDefaults.rem(this, other);
    }

    default Expr pow(Expr other) {
        return ExprDefaults.pow(this, other);
    }

    default Expr sub(int other) {
        return ExprDefaults.sub(this, other);
    }

    default Expr sub(double other) {
        return ExprDefaults.sub(this, other);
    }

    default Expr sub(Expr other) {
        return ExprDefaults.sub(this, other);
    }

    default Expr or(Expr other) {
        return ExprDefaults.or(this, other);
    }

    default Expr and(Expr other) {
        return ExprDefaults.and(this, other);
    }

    default Expr ne(double other) {
        return ExprDefaults.ne(this, other);
    }

    default Expr eq(double other) {
        return ExprDefaults.eq(this, other);
    }

    default Expr lt(double other) {
        return ExprDefaults.lt(this, other);
    }

    default Expr lte(double other) {
        return ExprDefaults.lte(this, other);
    }

    default Expr gt(double other) {
        return ExprDefaults.gt(this, other);
    }

    default Expr gte(double other) {
        return ExprDefaults.gte(this, other);
    }

    default Expr or(double other) {
        return ExprDefaults.or(this, other);
    }

    default Expr and(double other) {
        return ExprDefaults.and(this, other);
    }

    default Expr eq(Expr other) {
        return ExprDefaults.eq(this, other);
    }

    default Expr ne(Expr other) {
        return ExprDefaults.ne(this, other);
    }

    default Expr lt(Expr other) {
        return ExprDefaults.lt(this, other);
    }

    default Expr lte(Expr other) {
        return ExprDefaults.lte(this, other);
    }

    default Expr gt(Expr other) {
        return ExprDefaults.gt(this, other);
    }

    default Expr gte(Expr other) {
        return ExprDefaults.gte(this, other);
    }

    default Expr negate() {
        return neg();
    }

    default Expr neg() {
        return ExprDefaults.neg(this);
    }

    default Expr conj() {
        return ExprDefaults.conj(this);
    }

    default Expr cos() {
        return ExprDefaults.cos(this);
    }

    default Expr cosh() {
        return ExprDefaults.cosh(this);
    }

    default Expr sin() {
        return ExprDefaults.sin(this);
    }

    default Expr sinh() {
        return ExprDefaults.sinh(this);
    }

    default Expr sincard() {
        return ExprDefaults.sincard(this);
    }

    default Expr tan() {
        return ExprDefaults.tan(this);
    }

    default Expr cotan() {
        return ExprDefaults.cotan(this);
    }

    default Expr tanh() {
        return ExprDefaults.tanh(this);
    }

    default Expr cotanh() {
        return ExprDefaults.cotanh(this);
    }

    default Expr atan() {
        return ExprDefaults.atan(this);
    }

    default Expr acotan() {
        return ExprDefaults.acotan(this);
    }

    default Expr atanh() {
        return ExprDefaults.atanh(this);
    }

    default Expr acos() {
        return ExprDefaults.acos(this);
    }

    default Expr asin() {
        return ExprDefaults.asin(this);
    }

    default Expr asinh() {
        return ExprDefaults.asinh(this);
    }

    default Expr acosh() {
        return ExprDefaults.acosh(this);
    }

    default double norm() {
        return ExprDefaults.norm(this);
    }

    default Domain domain() {
        return getDomain();
    }

    Domain getDomain();

    /**
     * create a clone expression with changed name
     *
     * @param name
     */
    default Expr title(String name) {
        return setTitle(name);
    }

    /// SPECIFIC METHODS
    default Object prop(String name) {
        return getProperty(name);
    }

    default Object getProperty(String name) {
        return ExprDefaults.getProperty(this, name);
    }

    Expr newInstance(Expr... subExpressions);

    default Expr sqrt() {
        return ExprDefaults.sqrt(this);
    }

    default Expr sqrt(int n) {
        return ExprDefaults.sqrt(this, n);
    }

    default Expr sqr() {
        return ExprDefaults.sqr(this);
    }

    default Expr log() {
        return ExprDefaults.log(this);
    }

    default Expr log10() {
        return ExprDefaults.log(this);
    }

    default Expr abs() {
        return ExprDefaults.abs(this);
    }

    default Expr db() {
        return ExprDefaults.db(this);
    }

    default Expr db2() {
        return ExprDefaults.db2(this);
    }

    default Expr imag() {
        return ExprDefaults.imag(this);
    }

    default Expr real() {
        return ExprDefaults.real(this);
    }

    default Expr exp() {
        return ExprDefaults.exp(this);
    }

    //long op names
    default Expr subtract(int other) {
        return sub(other);
    }

    default Expr subtract(double other) {
        return sub(other);
    }

    default Expr subtract(Expr other) {
        return sub(other);
    }

    default Expr multiply(Domain domain) {
        return mul(domain);
    }

    default Expr multiply(Geometry geometry) {
        return mul(geometry);
    }

    default Expr multiply(int other) {
        return mul(other);
    }

    default Expr multiply(double other) {
        return mul(other);
    }

    default Expr multiply(Expr other) {
        return mul(other);
    }

    default Expr divide(int other) {
        return div(other);
    }

    default Expr divide(double other) {
        return div(other);
    }

    default Expr divide(Expr other) {
        return div(other);
    }

//    default String toLatex() {
//        throw new UnsupportedOperationException("Not Implemented toLatex for "+getClass().getName());
//    }
    String toLatex();
}
