package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.FormatFactory;
//import net.vpc.scholar.math.functions.dfxy.DoubleX;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vpc on 4/29/14.
 */
public abstract class AbstractExprPropertyAware extends AbstractExpBase{
    public static final int CACHE_EVALUATED_hasPrams=1 << 0;
    public static final int CACHE_VALUE_OF__hasPrams=1 << 1;
    public static final int CACHE_EVALUATED_isComplex=1 << 2;
    public static final int CACHE_VALUE_OF__isComplex=1 << 3;
    public static final int CACHE_EVALUATED_isDC=1 << 4;
    public static final int CACHE_VALUE_OF__isDC=1 << 5;
    public static final int CACHE_EVALUATED_isDD=1 << 6;
    public static final int CACHE_VALUE_OF__isDD=1 << 7;
    public static final int CACHE_EVALUATED_isDV=1 << 8;
    public static final int CACHE_VALUE_OF__isDV=1 << 9;
    public static final int CACHE_EVALUATED_isDouble=1 << 10;
    public static final int CACHE_VALUE_OF__isDouble=1 << 11;
    public static final int CACHE_EVALUATED_isDoubleExpr=1 << 12;
    public static final int CACHE_VALUE_OF__isDoubleExpr=1 << 13;

    public static final int CACHE_EVALUATED_isScalarExpr=1 << 14;
    public static final int CACHE_VALUE_OF__isScalarExpr=1 << 15;
    public static final int CACHE_EVALUATED_isInfinite=1 << 16;
    public static final int CACHE_VALUE_OF__isInfinite=1 << 17;
    public static final int CACHE_EVALUATED_isMatrix=1 << 18;
    public static final int CACHE_VALUE_OF__isMatrix=1 << 19;
    public static final int CACHE_EVALUATED_isNaN=1 << 20;
    public static final int CACHE_VALUE_OF__isNaN=1 << 21;
    public static final int CACHE_EVALUATED_isZero=1 << 22;
    public static final int CACHE_VALUE_OF__isZero=1 << 23;
    public static final int CACHE_EVALUATED_isInvariant_X=1 << 24;
    public static final int CACHE_VALUE_OF__isInvariant_X=1 << 24;
    public static final int CACHE_EVALUATED_isInvariant_Y=1 << 26;
    public static final int CACHE_VALUE_OF__isInvariant_Y=1 << 27;
//    public static final int CACHE_EVALUATED_isInvariant_Z=1 << 28;
//    public static final int CACHE_VALUE_OF__isInvariant_Z=1 << 29;
    public static final int CACHE_EVALUATED_isDM=1 << 28;
    public static final int CACHE_VALUE_OF__isDM=1 << 29;


    protected int _cache_isProperties;//new BitSet(40);

    public AbstractExprPropertyAware() {
    }




    protected boolean hasParamsImpl() {
        for (Expr expression : getSubExpressions()) {
            if (expression.hasParams()) {
                return true;
            }
        }
        return false;
    }



    protected boolean isDoubleImpl() {
        return getDomain().isUnconstrained() && isDoubleExpr();
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
//        if (properties != null) {
//            clone.properties = new HashMap<String, Object>(Math.max(1,properties.size()));
//            for (Map.Entry<String, Object> e : properties.entrySet()) {
//                Object v = null;
////                    if(e.getValue()!=null && e.getValue() instanceof Cloneable){
////                        v=e.getValue().clone();
////                    }
//                clone.properties.put(e.getKey(), e.getValue());
//            }
//        }
//        clone.name = name;
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

//        if (name != null ? !name.equals(that.name) : that.name != null) return false;
//        if (properties != null ? !properties.equals(that.properties) : that.properties != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return 31;//super.hashCode();
//        int result = name != null ? name.hashCode() : 0;
//        result = 31 * result + (properties != null ? properties.hashCode() : 0);
//        return result;
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
        if( !_cache_is_evaluated(CACHE_EVALUATED_isDD)){
            boolean v = isDDImpl();
            _cache_set(CACHE_VALUE_OF__isDD, v);
            return v;
        }
        return _cache_get(CACHE_VALUE_OF__isDD);
    }

    public synchronized final boolean isDC() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return isDCImpl();
        }
        if( !_cache_is_evaluated(CACHE_EVALUATED_isDC)){
            boolean v = isDCImpl();
            _cache_set(CACHE_VALUE_OF__isDC, v);
            return v;
        }
        return _cache_get(CACHE_VALUE_OF__isDC);
    }

    public synchronized final boolean isDV() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return isDVImpl();
        }
        if( !_cache_is_evaluated(CACHE_EVALUATED_isDV)){
            boolean v = isDVImpl();
            _cache_set(CACHE_VALUE_OF__isDV, v);
            return v;
        }
        return _cache_get(CACHE_VALUE_OF__isDV);
    }

    public synchronized final boolean isDM() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return isDMImpl();
        }
        if( !_cache_is_evaluated(CACHE_EVALUATED_isDM)){
            boolean v = isDMImpl();
            _cache_set(CACHE_VALUE_OF__isDM, v);
            return v;
        }
        return _cache_get(CACHE_VALUE_OF__isDM);
    }

    public synchronized final boolean isNaN() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return isNaNImpl();
        }
        if( !_cache_is_evaluated(CACHE_EVALUATED_isNaN)){
            boolean v = isNaNImpl();
            _cache_set(CACHE_VALUE_OF__isNaN, v);
            return v;
        }
        return _cache_get(CACHE_VALUE_OF__isNaN);
    }

    public synchronized final boolean isInfinite() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return isInfiniteImpl();
        }
        if( !_cache_is_evaluated(CACHE_EVALUATED_isInfinite)){
            boolean v = isInfiniteImpl();
            _cache_set(CACHE_VALUE_OF__isInfinite, v);
            return v;
        }
        return _cache_get(CACHE_VALUE_OF__isInfinite);
    }

    public synchronized final boolean isDoubleExpr() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return isDoubleExprImpl();
        }
        if( !_cache_is_evaluated(CACHE_EVALUATED_isDoubleExpr)){
            boolean v = isDoubleExprImpl();
            _cache_set(CACHE_VALUE_OF__isDoubleExpr, v);
            return v;
        }
        return _cache_get(CACHE_VALUE_OF__isDoubleExpr);
    }

    public synchronized  boolean isDouble() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return isDoubleImpl();
        }
        if( !_cache_is_evaluated(CACHE_EVALUATED_isDouble)){
            boolean v = isDoubleImpl();
            _cache_set(CACHE_VALUE_OF__isDouble, v);
            return v;
        }
        return _cache_get(CACHE_VALUE_OF__isDouble);
    }

    public synchronized final boolean isComplex() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return isComplexImpl();
        }
        if( !_cache_is_evaluated(CACHE_EVALUATED_isComplex)){
            boolean v = isComplexImpl();
            _cache_set(CACHE_VALUE_OF__isComplex, v);
            return v;
        }
        return _cache_get(CACHE_VALUE_OF__isComplex);
    }

    public synchronized final boolean isZero() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return isZeroImpl();
        }
        if( !_cache_is_evaluated(CACHE_EVALUATED_isZero)){
            boolean v = isZeroImpl();
            _cache_set(CACHE_VALUE_OF__isZero, v);
            return v;
        }
        return _cache_get(CACHE_VALUE_OF__isZero);
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
                if( !_cache_is_evaluated(CACHE_EVALUATED_isInvariant_X)){
                    boolean v = isInvariantImpl(axis);
                    _cache_set(CACHE_VALUE_OF__isInvariant_X, v);
                    return v;
                }
                return _cache_get(CACHE_VALUE_OF__isInvariant_X);
            }
            case Y:{
                if( !_cache_is_evaluated(CACHE_EVALUATED_isInvariant_Y)){
                    boolean v = isInvariantImpl(axis);
                    _cache_set(CACHE_VALUE_OF__isInvariant_Y, v);
                    return v;
                }
                return _cache_get(CACHE_VALUE_OF__isInvariant_Y);
            }
            case Z:{
                return isInvariantImpl(axis);
                //TODO Z cache is not supported to help optimize memory usage (no more 'bits' in the int cache element)
//                if( !_cache_is_evaluated(CACHE_EVALUATED_isInvariant_Z)){
//                    boolean v = isInvariantImpl(axis);
//                    _cache_set(CACHE_VALUE_OF__isInvariant_Z, v);
//                    return v;
//                }
//                return _cache_get(CACHE_VALUE_OF__isInvariant_Z);
            }
        }
        throw new UnsupportedDomainDimensionException();
    }

    public final boolean isMatrix() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return isMatrixImpl();
        }
        if( !_cache_is_evaluated(CACHE_EVALUATED_isMatrix)){
            boolean v = isMatrixImpl();
            _cache_set(CACHE_VALUE_OF__isMatrix, v);
            return v;
        }
        return _cache_get(CACHE_VALUE_OF__isMatrix);
    }

    public final boolean isScalarExpr() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return isScalarExprImpl();
        }
        if( !_cache_is_evaluated(CACHE_EVALUATED_isScalarExpr)){
            boolean v = isScalarExprImpl();
            _cache_set(CACHE_VALUE_OF__isScalarExpr, v);
            return v;
        }
        return _cache_get(CACHE_VALUE_OF__isScalarExpr);
    }

    public final boolean hasParams() {
        if(!Maths.Config.isCacheExpressionPropertiesEnabled()){
            return hasParamsImpl();
        }
        if( !_cache_is_evaluated(CACHE_EVALUATED_hasPrams)){
            boolean v = hasParamsImpl();
            _cache_set(CACHE_VALUE_OF__hasPrams, v);
            return v;
        }
        return _cache_get(CACHE_VALUE_OF__hasPrams);
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

//    protected void _cache_isProperties_set(int bitPower,boolean value){
//        if(value){
//            _cache_isProperties |= (bitPower);
//        }else{
//            _cache_isProperties &= ~(bitPower);
//        }
////        if(value){
////            _cache_isProperties |= (1 << bitIndex);
////        }else{
////            _cache_isProperties &= ~(1 << bitIndex);
////        }
////        if(_cache_isProperties==null){
////            _cache_isProperties=new BitSet(40);
////        }
////        _cache_isProperties.set(bitIndex,value);
//    }

    protected boolean _cache_get(int bitPower) {
        return ((_cache_isProperties & (bitPower)) != 0);
    }
    protected void _cache_set(int bitPower,boolean value) {
        if (value) {
            _cache_isProperties |= (bitPower);
        } else {
            _cache_isProperties &= ~(bitPower);
        }
    }
    protected boolean _cache_is_evaluated(int bitPower) {
        boolean b = (_cache_isProperties & (bitPower)) != 0;
        if(!b){
            _cache_set_evaluated(bitPower,true);
        }
        return b;
    }
//    protected void _cache_set_evaluated(int bitPower) {
//        _cache_set_evaluated(bitPower,true);
//    }

    protected void _cache_set_evaluated(int bitPower,boolean value) {
        if (value) {
            _cache_isProperties |= (bitPower);
        } else {
            _cache_isProperties &= ~(bitPower);
        }
    }

//    protected boolean _cache_isProperties_get(int bitPower){
//        return ((_cache_isProperties & (bitPower)) != 0);
////        return ((_cache_isProperties & (1 << bitIndex)) != 0);
////        if(_cache_isProperties==null){
////            _cache_isProperties=new BitSet(40);
////        }
////        return _cache_isProperties.get(bitIndex);
//    }


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
