package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.FormatFactory;
//import net.vpc.scholar.math.functions.dfxy.DoubleX;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vpc on 4/29/14.
 */
public abstract class AbstractExprPropertyAware extends AbstractExpBase{
    protected String name;
    private Map<String, Object> properties;
    protected int _cache_isProperties;//new BitSet(40);

    public AbstractExprPropertyAware() {
    }

    public static Expr copyProperties(Expr a, Expr b) {
        if(a.hasProperties()) {
            b=b.setProperties(a.getProperties());
        }
        if(a.getName()!=null){
            b=b.setName(a.getName());
        }
        return b;
    }
    public static Expr updateNameVars(Expr b,String paramName,Expr paramValue) {
        String paramValueS=paramName==null?"":paramValue.toString();
        if(b.getName()!=null && b.getName().contains("${"+paramName+"}")){
            return b.setName(b.getName().replace("${"+paramName+"}",paramValueS));
        }
        return b;
    }
    public static Expr updateNameVars(Expr b,String paramName,double paramValue) {
        String paramValueS=String.valueOf(paramValue);
        if(b.getName()!=null && b.getName().contains("${"+paramName+"}")){
            return b.setName(b.getName().replace("${"+paramName+"}",paramValueS));
        }
        return b;
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
    public Expr setProperty(String name, Object value) {
        HashMap<String, Object> m=new HashMap<>(1);
        m.put(name,value);
        return setProperties(m);
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
        if(map!=null && !map.isEmpty()){
            AbstractExprPropertyAware a=(AbstractExprPropertyAware) clone();
            if (a.properties == null) {
                a.properties = new HashMap<String, Object>(2);
            }
            a.properties.putAll(map);
            return a;
        }
        return this;
    }

    protected boolean hasParamsImpl() {
        for (Expr expression : getSubExpressions()) {
            if (expression.hasParams()) {
                return true;
            }
        }
        return false;
    }


    public Expr setParam(String name, double value) {
        return setParam(name, DoubleValue.valueOf(value, Domain.FULL(getDomainDimension())));
    }

    protected boolean isDoubleImpl() {
        return false;
    }

    protected boolean isComplexImpl() {
        return false;
    }

    protected boolean isMatrixImpl() {
        return false;
    }

    @Override
    public Complex toComplex() {
        throw new ClassCastException(toString()+" of type "+getClass().getName()+" cannot be casted to Complex");
    }

    @Override
    public Matrix toMatrix() {
        throw new ClassCastException(toString()+" of type "+getClass().getName()+" cannot be casted to Matrix");
    }

    @Override
    public Expr clone() {
        AbstractExprPropertyAware clone = (AbstractExprPropertyAware) super.clone();
        if (properties != null) {
            clone.properties = new HashMap<String, Object>(Math.max(1,properties.size()));
            for (Map.Entry<String, Object> e : properties.entrySet()) {
                Object v = null;
//                    if(e.getValue()!=null && e.getValue() instanceof Cloneable){
//                        v=e.getValue().clone();
//                    }
                clone.properties.put(e.getKey(), e.getValue());
            }
        }
        clone.name = name;
//            clone._cache_isProperties = _cache_isProperties==null?null:(BitSet) _cache_isProperties.clone();
        return clone;
    }

    @Override
    public String toString() {
        return FormatFactory.toString(this);
    }

    @Override
    public Expr composeX(Expr xreplacement) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expr composeY(Expr yreplacement) {
        throw new UnsupportedOperationException();
    }

//    @Override
//    public Expr setTitle(String title) {
//        getProperties().put("title", title);
//        return this;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractExprPropertyAware that = (AbstractExprPropertyAware) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (properties != null ? !properties.equals(that.properties) : that.properties != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        return result;
    }


//    @Override
//    public boolean isScalarExpr(){
//        get
//        return false;
//    }

    @Override
    public Expr simplify() {
        return Maths.simplify(this);
    }

    @Override
    public Expr normalize() {
        return Maths.normalize(this);
    }

    @Override
    public Expr setParam(ParamExpr paramExpr, double value) {
        return setParam(paramExpr.getName(), value);
    }

    @Override
    public Expr setParam(ParamExpr paramExpr, Expr value) {
        return setParam(paramExpr.getName(), value);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Expr setName(String name) {
        if(isUpdatableName()) {
            AbstractExprPropertyAware r = (AbstractExprPropertyAware) clone();
            r.name = name;
            return r;
        }else{
            return this;
        }
    }

//    public Matrix computeMatrix(double x, double y, double z) {
//        throw new IllegalArgumentException("computeMatrix() Not yet supported in " + getClass().getName());
//    }

//    public Complex computeComplex(double x, double y, double z) {
//        return Expressions.computeComplex((DoubleToComplex) this, x, y, z);
//    }

//    public double computeDouble(double x, double y, double z) {
//        return toDC().computeComplex(x, y, z).toDouble();
//    }


//    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
//        throw new IllegalArgumentException("computeComplex() Not yet supported in " + getClass().getName());
//    }
//
//    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
//        throw new IllegalArgumentException("computeDouble() Not yet supported in " + getClass().getName());
//    }
//
//    public CMatrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
//        throw new IllegalArgumentException("computeMatrix() Not yet supported in " + getClass().getName());
//    }

//    @Override
//    public int getDomainDimension() {
//        throw new IllegalArgumentException("getDomainDimension() Not yet supported in "+getClass().getName());
//    }

    @Override
    public int getDomainDimension() {
        return getDomain().getDimension();
    }

    protected boolean isScalarExprImpl() {
        return getComponentDimension().equals(ComponentDimension.SCALAR);
    }


    public synchronized final boolean isDD() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return isDDImpl();
        }
        if( !_cache_isProperties_get(0)){
            _cache_isProperties_set(0,true);
            boolean v = isDDImpl();
            if(!v){
                v=isDDImpl();
            }
            _cache_isProperties_set(1, v);
            return v;
        }
        return _cache_isProperties_get(1);
    }

    public synchronized final boolean isDC() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return isDCImpl();
        }
        if( !_cache_isProperties_get(2)){
            _cache_isProperties_set(2,true);
            boolean v = isDCImpl();
            _cache_isProperties_set(3, v);
            return v;
        }
        return _cache_isProperties_get(3);
    }

    public synchronized final boolean isDV() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return isDVImpl();
        }
        if( !_cache_isProperties_get(4)){
            _cache_isProperties_set(4,true);
            boolean v = isDVImpl();
            _cache_isProperties_set(5, v);
            return v;
        }
        return _cache_isProperties_get(5);
    }

    public synchronized final boolean isDM() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return isDMImpl();
        }
        if( !_cache_isProperties_get(6)){
            _cache_isProperties_set(6,true);
            boolean v = isDMImpl();
            _cache_isProperties_set(7, v);
            return v;
        }
        return _cache_isProperties_get(7);
    }

    public synchronized final boolean isNaN() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return isNaNImpl();
        }
        if( !_cache_isProperties_get(8)){
            _cache_isProperties_set(8,true);
            boolean v = isNaNImpl();
            _cache_isProperties_set(9, v);
            return v;
        }
        return _cache_isProperties_get(9);
    }

    public synchronized final boolean isInfinite() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return isInfiniteImpl();
        }
        if( !_cache_isProperties_get(10)){
            _cache_isProperties_set(10,true);
            boolean v = isInfiniteImpl();
            _cache_isProperties_set(11, v);
            return v;
        }
        return _cache_isProperties_get(11);
    }

    public synchronized final boolean isDoubleExpr() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return isDoubleExprImpl();
        }
        if( !_cache_isProperties_get(12)){
            _cache_isProperties_set(12,true);
            boolean v = isDoubleExprImpl();
            _cache_isProperties_set(13, v);
            return v;
        }
        return _cache_isProperties_get(13);
    }

    public synchronized final boolean isDouble() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return isDoubleImpl();
        }
        if( !_cache_isProperties_get(14)){
            _cache_isProperties_set(14,true);
            boolean v = isDoubleImpl();
            _cache_isProperties_set(15, v);
            return v;
        }
        return _cache_isProperties_get(15);
    }

    public synchronized final boolean isComplex() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return isComplexImpl();
        }
        if( !_cache_isProperties_get(15)){
            _cache_isProperties_set(15,true);
            boolean v = isComplexImpl();
            _cache_isProperties_set(15, v);
            return v;
        }
        return _cache_isProperties_get(15);
    }

    public synchronized final boolean isZero() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return isZeroImpl();
        }
        if( !_cache_isProperties_get(16)){
            _cache_isProperties_set(16,true);
            boolean v = isZeroImpl();
            _cache_isProperties_set(17, v);
            return v;
        }
        return _cache_isProperties_get(17);
    }

    public synchronized final boolean isInvariant(Axis axis) {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            switch (axis){
                case X:{
                    return isInvariantImpl(axis);
                }
                case Y:{
                    return isInvariantImpl(axis);
                }
                case Z:{
                    return  isInvariantImpl(axis);
                }
            }
            throw new UnsupportedDomainDimensionException();
        }
        switch (axis){
            case X:{
                if( !_cache_isProperties_get(18)){
                    _cache_isProperties_set(18,true);
                    boolean v = isInvariantImpl(axis);
                    _cache_isProperties_set(19, v);
                    return v;
                }
                return _cache_isProperties_get(19);
            }
            case Y:{
                if( !_cache_isProperties_get(20)){
                    _cache_isProperties_set(20,true);
                    boolean v = isInvariantImpl(axis);
                    _cache_isProperties_set(21, v);
                    return v;
                }
                return _cache_isProperties_get(21);
            }
            case Z:{
                if( !_cache_isProperties_get(22)){
                    _cache_isProperties_set(22,true);
                    boolean v = isInvariantImpl(axis);
                    _cache_isProperties_set(23, v);
                    return v;
                }
                return _cache_isProperties_get(23);
            }
        }
        throw new UnsupportedDomainDimensionException();
    }

    public final boolean isMatrix() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return isMatrixImpl();
        }
        if( !_cache_isProperties_get(24)){
            _cache_isProperties_set(24,true);
            boolean v = isMatrixImpl();
            _cache_isProperties_set(25, v);
            return v;
        }
        return _cache_isProperties_get(25);
    }

    public final boolean isScalarExpr() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return isScalarExprImpl();
        }
        if( !_cache_isProperties_get(26)){
            _cache_isProperties_set(26,true);
            boolean v = isScalarExprImpl();
            _cache_isProperties_set(27, v);
            return v;
        }
        return _cache_isProperties_get(27);
    }

    public final boolean hasParams() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return hasParamsImpl();
        }
        if( !_cache_isProperties_get(28)){
            _cache_isProperties_set(28,true);
            boolean v = hasParamsImpl();
            _cache_isProperties_set(29, v);
            return v;
        }
        return _cache_isProperties_get(29);
    }

    @Override
    public Domain domain() {
        return getDomain();
    }

    @Override
    public Domain getDomain() {
        return getDomainImpl();
//        protected Domain _cache_domain;
//        if(!Maths.Config.isCacheEnabled()){
//            return getDomainImpl();
//        }
//        if( _cache_domain==null){
//            _cache_domain=getDomainImpl();
//        }
//        return _cache_domain;
    }

    protected void _cache_isProperties_set(int bitIndex,boolean value){
        if(value){
            _cache_isProperties |= (1 << bitIndex);
        }else{
            _cache_isProperties &= ~(1 << bitIndex);
        }
//        if(_cache_isProperties==null){
//            _cache_isProperties=new BitSet(40);
//        }
//        _cache_isProperties.set(bitIndex,value);
    }

    protected boolean _cache_isProperties_get(int bitIndex){
        return ((_cache_isProperties & (1 << bitIndex)) != 0);
//        if(_cache_isProperties==null){
//            _cache_isProperties=new BitSet(40);
//        }
//        return _cache_isProperties.get(bitIndex);
    }


    public boolean isUpdatableName(){
        return true;
    }
    protected abstract Domain getDomainImpl() ;
    protected abstract boolean isInvariantImpl(Axis axis) ;
    protected abstract boolean isZeroImpl() ;
    protected abstract boolean isInfiniteImpl() ;
    protected abstract boolean isNaNImpl() ;
    protected abstract boolean isDoubleExprImpl() ;
    protected abstract boolean isDDImpl() ;
    protected abstract boolean isDCImpl() ;
    protected abstract boolean isDVImpl() ;
    protected abstract boolean isDMImpl() ;
}
