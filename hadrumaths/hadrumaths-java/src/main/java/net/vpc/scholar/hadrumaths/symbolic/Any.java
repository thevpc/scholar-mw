/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransform;
import net.vpc.scholar.hadrumaths.ExpressionTransformFactory;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vpc
 */
public class Any extends AbstractVerboseExprRef implements Cloneable {

    static {
        ExpressionTransformFactory.setExpressionTransformer(Any.class, ExpressionTransform.class, new ExpressionTransformer() {

            public Expr transform(Expr expression, ExpressionTransform transform) {
                Any e = (Any) expression;
                return e.wrap(ExpressionTransformFactory.transform(e.object, transform));
            }
        });
    }
    protected String name;
    private Map<String, Object> properties;

    public Expr object;

    public Any(Expr object) {
        this.object = unwrap(object);
    }

    public static Expr unwrap(Expr e) {
        return !(e instanceof Any) ? e : (((Any) e).object);
    }

    public static Expr copyProperties(Expr a, Expr b) {
        if(a.hasProperties()) {
            b=b.setProperties(a.getProperties());
        }
        if(a.getTitle()!=null){
            b=b.setTitle(a.getTitle());
        }
        return b;
    }

    public static Expr updateTitleVars(Expr b, String paramName, Expr paramValue) {
        String paramValueS=paramName==null?"":paramValue.toString();
        if(b.getTitle()!=null && b.getTitle().contains("${"+paramName+"}")){
            return b.setTitle(b.getTitle().replace("${"+paramName+"}",paramValueS));
        }
        return b;
    }

    public static Expr updateTitleVars(Expr b, String paramName, double paramValue) {
        String paramValueS=String.valueOf(paramValue);
        if(b.getTitle()!=null && b.getTitle().contains("${"+paramName+"}")){
            return b.setTitle(b.getTitle().replace("${"+paramName+"}",paramValueS));
        }
        return b;
    }

    public String getTitle() {
        return object.getTitle();
    }

    @Override
    public Expr setTitle(String name) {
        this.name=name;
        return this;
    }

    @Override
    public Expr clone() {
        Any cloned = (Any) super.clone();
//        cloned.object = object.clone();
        if (properties != null && properties.size()>0) {
            cloned.properties = new HashMap<String, Object>(properties.size());
            for (Map.Entry<String, Object> e : properties.entrySet()) {
                cloned.properties.put(e.getKey(), e.getValue());
            }
        }
        return cloned;
    }

    public Any wrap(Expr e) {
        return !(e instanceof Any) ? new Any(e) : (((Any) e));
    }

    public boolean isZeroImpl() {
        return object.isZero();
    }

    public boolean isNaNImpl() {
        return object.isNaN();
    }

    public boolean isInfiniteImpl() {
        return object.isInfinite();
    }

    public boolean isInvariantImpl(Axis axis) {
        return object.isInvariant(axis);
    }

    public Expr getObject() {
        return object;
    }

    public Any add(Expr e) {
        Expr first = object;
        Expr second = unwrap(e);
        return wrap(
                Maths.sum(first, second)
        );
    }

    public Any sub(Expr e) {
        Expr first = object;
        Expr second = unwrap(e);
        return wrap(
                new Sub(first, second)
        );
    }

    public Any mul(Expr e) {
        Expr first = object;
        Expr second = (e instanceof Any) ? ((Any) e).object : e;
        return wrap(
                Maths.mul(first, second)
        );
    }

    public Any div(Expr e) {
        Expr first = object;
        Expr second = unwrap(e);
        return wrap(
                new Div(first, second)
        );
    }

    public Any scalarProduct(boolean hermitian, Expr e) {
        Expr first = object;
        Expr second = unwrap(e);
        return wrap(
                Maths.scalarProduct(hermitian, first, second)
        );
    }

    public Any pow(Expr e) {
        Expr first = object;
        Expr second = unwrap(e);
        return wrap(
                new Pow(first, second)
        );
    }

    public Any lt(Expr e) {
        Expr first = object;
        Expr second = unwrap(e);
        return wrap(
                Maths.lt(first, second)
        );
    }
    public Any lte(Expr e) {
        Expr first = object;
        Expr second = unwrap(e);
        return wrap(
                Maths.lte(first, second)
        );
    }
    public Any gt(Expr e) {
        Expr first = object;
        Expr second = unwrap(e);
        return wrap(
                Maths.gt(first, second)
        );
    }
    public Any gte(Expr e) {
        Expr first = object;
        Expr second = unwrap(e);
        return wrap(
                Maths.gte(first, second)
        );
    }
    public Any eq(Expr e) {
        Expr first = object;
        Expr second = unwrap(e);
        return wrap(
                Maths.eq(first, second)
        );
    }
    public Any ne(Expr e) {
        Expr first = object;
        Expr second = unwrap(e);
        return wrap(
                Maths.ne(first, second)
        );
    }

    public Any inv() {
        Expr first = object;
        return wrap(
                new Inv(first)
        );
    }

    @Override
    public boolean isDC() {
        return object.isDC();
    }

    @Override
    public boolean isDD() {
        return object.isDD();
    }

//    @Override
//    public boolean isDDx() {
//        return object.isDDx();
//    }
    @Override
    public boolean isDM() {
        return object.isDM();
    }

    @Override
    public boolean isDV() {
        return object.isDV();
    }

    @Override
    public DoubleToVector toDV() {
        return
                //wrap(
                        object.toDV()
                //)
        ;
    }

    @Override
    public DoubleToComplex toDC() {
        return
                //wrap(
                object.toDC()
        //)
        ;
    }

    @Override
    public DoubleToDouble toDD() {
        return
                //wrap(
                        object.toDD()
                //)
                ;
    }

    @Override
    public DoubleToMatrix toDM() {
        return
                //wrap(
                        object.toDM()
                //)
                ;
    }

//    @Override
//    public IDDx toDDx() {
//        return wrap(object.toDDx());
//    }
    @Override
    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return object.toDC().computeComplex(x, y, d0, ranges);
    }

    @Override
    public Complex[] computeComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
        return object.toDC().computeComplex(x, y, d0, ranges);
    }

    @Override
    public Complex[] computeComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
        return object.toDC().computeComplex(x, y, d0, ranges);
    }

    @Override
    public Complex computeComplex(double x, double y) {
        return object.toDC().computeComplex(x, y);
    }

    @Override
    public Domain getDomain() {
        return object.getDomain();
    }

    @Override
    public double[] computeDouble(double[] x, Domain d0, Out<Range> range) {
        return object.toDD().computeDouble(x, d0, range);
    }

    @Override
    public double computeDouble(double x) {
        return object.toDD().computeDouble(x);
    }

    @Override
    public double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return object.toDD().computeDouble(x, y, d0, ranges);
    }

    @Override
    public double[] computeDouble(double[] x, double y, Domain d0, Out<Range> ranges) {
        return object.toDD().computeDouble(x, y, d0, ranges);
    }

    @Override
    public double[] computeDouble(double x, double[] y, Domain d0, Out<Range> ranges) {
        return object.toDD().computeDouble(x, y, d0, ranges);
    }

    @Override
    public double computeDouble(double x, double y) {
        return object.toDD().computeDouble(x, y);
    }

    @Override
    public Matrix[][] computeMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return object.toDM().computeMatrix(x, y, d0, ranges);
    }

    @Override
    public Matrix[] computeMatrix(double[] x, double y, Domain d0, Out<Range> ranges) {
        return object.toDM().computeMatrix(x, y, d0, ranges);
    }

    @Override
    public Matrix[] computeMatrix(double x, double[] y, Domain d0, Out<Range> ranges) {
        return object.toDM().computeMatrix(x, y, d0, ranges);
    }

    @Override
    public Matrix computeMatrix(double x, double y) {
        return object.toDM().computeMatrix(x, y);
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return object.getComponentDimension();
    }

    @Override
    public Expr getComponent(int row, int col) {
        return wrap(object.toDM().getComponent(row, col));
    }

    public DoubleToDouble getReal() {
        return wrap(object.toDC().getReal());
    }

    public DoubleToDouble getImag() {
        return wrap(object.toDC().getImag());
    }

    public List<Expr> getSubExpressions() {
        List<Expr> subExpressions = object.getSubExpressions();
        List<Expr> subExpressions2 = new ArrayList<Expr>(subExpressions.size());
        for (Expr subExpression : subExpressions) {
            subExpressions2.add(wrap(subExpression));
        }
        return subExpressions2;
    }

    @Override
    public boolean isDouble() {
        return object.isDouble();
    }

    @Override
    public boolean isComplex() {
        return object.isComplex();
    }

    public boolean isDoubleExpr() {
        return object.isDoubleExpr();
    }

    @Override
    public boolean isMatrix() {
        return object.isMatrix();
    }

    @Override
    public Complex toComplex() {
        return object.toComplex();
    }

    @Override
    public double toDouble() {
        return object.toDouble();
    }

    @Override
    public Matrix toMatrix() {
        return object.toMatrix();
    }

    @Override
    public boolean hasParams() {
        return object.hasParams();
    }

    @Override
    public Any setParam(String name, double value) {
        Any a=wrap(
                        object.setParam(name, value)
                );
        copyProperties(this, a);
        return a;
    }

    @Override
    public Any setParam(String name, Expr value) {
        Expr e = object.setParam(name, value);
        if(e==object) {
            return this;
        }
        Any a=wrap(e);
        copyProperties(this, a);
        return a;
    }

    @Override
    public String toString() {
        return String.valueOf(object);
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

    @Override
    public Expr simplify() {
        return //wrap(
                object.simplify()
        //)
        ;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
//        if (!super.equals(o)) return false;

        Any any = (Any) o;

        return object != null ? object.equals(any.object) : any.object == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (object != null ? object.hashCode() : 0);
        return result;
    }

    @Override
    public String getComponentTitle(int row, int col) {
//        System.out.println(this+" : getComponentTitle("+col+", "+row+")");
        return object.toDM().getComponentTitle(row, col);
    }

    @Override
    public Expr composeX(Expr xreplacement) {
        return
                //wrap(
                        object.composeX(xreplacement)
                //)
                ;
    }

    @Override
    public Expr composeY(Expr yreplacement) {
        return
                //wrap(
                        object.composeY(yreplacement)
                        //)
                ;
    }

//    @Override
//    public Expr setTitle(String title) {
//        getProperties().put("title", title);
//        return this;
//    }

    @Override
    public boolean isScalarExpr() {
        return object.isScalarExpr();
    }

    public Any add(double e) {
        return add(Maths.expr(e));
    }

    public Any mul(double e) {
        return mul(Maths.expr(e));
    }

    public Any sub(double e) {
        return sub(Maths.expr(e));
    }

    public Any div(double e) {
        return div(Maths.expr(e));
    }

    @Override
    public DoubleToComplex getComponent(Axis a) {
        return
                //wrap(
                (DoubleToComplex) object.toDV().getComponent(a)
                        //)
                ;
    }

    public DoubleToComplex getX() {
        return
                //wrap(
                        (DoubleToComplex)object.toDV().getComponent(Axis.X)
                //)
        ;
    }

    public DoubleToComplex getY() {
        return
                //wrap(
                (DoubleToComplex)  object.toDV().getComponent(Axis.Y)
                //)
                ;
    }

    public DoubleToComplex getZ() {
        return
                //wrap(
                (DoubleToComplex)           object.toDV().getComponent(Axis.Z)
        //)
        ;
    }

    @Override
    public int getDomainDimension() {
        return object.getDomainDimension();
    }

    @Override
    public Complex computeComplex(double x, double y, double z) {
        return object.toDC().computeComplex(x, y, z);
    }

    @Override
    public Matrix computeMatrix(double x, double y, double z) {
        return object.toDM().computeMatrix(x, y, z);
    }

    @Override
    public double computeDouble(double x, double y, double z) {
        return object.toDD().computeDouble(x, y, z);
    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return object.toDC().computeComplex(x, y, z, d0, ranges);
    }

    @Override
    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return object.toDD().computeDouble(x, y, z, d0, ranges);
    }

    @Override
    public Matrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return object.toDM().computeMatrix(x, y, z, d0, ranges);
    }

    @Override
    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
        return object.toDC().computeComplex(x, d0, ranges);
    }

    @Override
    public Complex computeComplex(double x) {
        return object.toDC().computeComplex(x);
    }

    @Override
    public int getComponentSize() {
        return object.toDV().getComponentSize();
    }

    @Override
    public Matrix[] computeMatrix(double[] x, Domain d0, Out<Range> ranges) {
        return object.toDM().computeMatrix(x, d0, ranges);
    }

    @Override
    public Matrix computeMatrix(double x) {
        return object.toDM().computeMatrix(x);
    }


    public Any cos() {
        return wrap(
                Maths.cos(object)
        );
    }
    public Any sin() {
        return wrap(
                Maths.sin(object)
        );
    }
    public Any tan() {
        return wrap(
                Maths.tan(object)
        );
    }
    public Any cotan() {
        return wrap(
                Maths.cotan(object)
        );
    }
    public Any exp() {
        return wrap(
                Maths.exp(object)
        );
    }

    @Override
    public boolean isInvariant(Axis axis) {
        return object.isInvariant(axis);
    }

    @Override
    public boolean isZero() {
        return object.isZero();
    }

    @Override
    public boolean isNaN() {
        return object.isNaN();
    }

    @Override
    public Expr setParam(ParamExpr paramExpr, double value) {
        return wrap(object.setParam(paramExpr,value));
    }

    @Override
    public Expr setParam(ParamExpr paramExpr, Expr value) {
        return wrap(object.setParam(paramExpr,value));
    }

    @Override
    public boolean isInfinite() {
        return object.isInfinite();
    }

    @Override
    public Expr setProperty(String name, Object value) {
        setSelfProperty(name, value);
        return this;
    }
    

    public void setSelfProperty(String name, Object value) {
        if(value==null){
            if(properties!=null){
                properties.remove(name);
            }
        }else{
            if(properties==null){
                properties=new HashMap<String, Object>(2);
            }
            properties.put(name, value);
        }
    }

    @Override
    public Expr normalize() {
        return wrap(object.normalize());
    }

    @Override
    public boolean hasProperties() {
        return properties!=null && properties.size()>0;
    }

    @Override
    public Object getProperty(String name) {
        return properties!=null ? properties.get(name):null;
    }

    @Override
    public Map<String, Object> getProperties() {
        if (properties == null) {
            properties = new HashMap<String, Object>(2);
        }
        return properties;
    }

    @Override
    public Expr setProperties(Map<String, Object> map) {
        setSelfProperties(map);
        return this;
//        if(map!=null && !map.isEmpty()){
//            Any a=(Any) clone();
//            a.setSelfProperties(map);
//        }
//        return this;
    }

    public void setSelfProperties(Map<String, Object> map) {
        if(map!=null && !map.isEmpty()){
            if (properties == null) {
                properties = new HashMap<String, Object>(2);
            }
            properties.putAll(map);
        }
    }

}
