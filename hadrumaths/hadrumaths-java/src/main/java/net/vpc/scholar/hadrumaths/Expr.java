package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 juil. 2007 15:45:33
 */
public interface Expr extends Serializable {
//    double compute(double x,DomainX d);
//    double compute(double x,double y,DomainXY d);
//    double compute(double x,double y,double z,DomainXYZ d);

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
     * @return true if this expression is defined as a valid non param, non constrained complex value (full domain)
     */
    boolean isComplex();

    /**
     * true if this expression is defined only for double values and is not handling complex values
     *
     * @return
     */
    boolean isDoubleExpr();

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

    Complex toComplex();

    double toDouble();

    Matrix toMatrix();

    boolean isDC();

    DoubleToComplex toDC();

    boolean isDD();

    DoubleToDouble toDD();

//    boolean isDDx();

//    IDDx toDDx();

    boolean isDV();

    DoubleToVector toDV();

    boolean isDM();

    DoubleToMatrix toDM();

    boolean isZero();

    boolean isNaN();

    boolean hasParams();

    Expr setParam(String name, double value);

    Expr setParam(String name, Expr value);

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

    Object getProperty(String name);

    Map<String, Object> getProperties();

    Expr setProperties(Map<String, Object> map);

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
     * simple call to Maths.normalize(this);
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
}
