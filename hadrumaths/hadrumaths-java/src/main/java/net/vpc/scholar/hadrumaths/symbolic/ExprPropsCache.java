package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.UnsupportedDomainDimensionException;

import java.util.function.BooleanSupplier;
import java.util.function.Function;

public class ExprPropsCache {
    public static final int CACHE_EVALUATED_hasPrams = 1 << 0;
    public static final int CACHE_VALUE_OF__hasPrams = 1 << 1;
    public static final int CACHE_EVALUATED_isComplex = 1 << 2;
    public static final int CACHE_VALUE_OF__isComplex = 1 << 3;
    public static final int CACHE_EVALUATED_isDC = 1 << 4;
    public static final int CACHE_VALUE_OF__isDC = 1 << 5;
    public static final int CACHE_EVALUATED_isDD = 1 << 6;
    public static final int CACHE_VALUE_OF__isDD = 1 << 7;
    public static final int CACHE_EVALUATED_isDV = 1 << 8;
    public static final int CACHE_VALUE_OF__isDV = 1 << 9;
    public static final int CACHE_EVALUATED_isDouble = 1 << 10;
    public static final int CACHE_VALUE_OF__isDouble = 1 << 11;
    public static final int CACHE_EVALUATED_isDoubleExpr = 1 << 12;
    public static final int CACHE_VALUE_OF__isDoubleExpr = 1 << 13;

    public static final int CACHE_EVALUATED_isScalarExpr = 1 << 14;
    public static final int CACHE_VALUE_OF__isScalarExpr = 1 << 15;
    public static final int CACHE_EVALUATED_isInfinite = 1 << 16;
    public static final int CACHE_VALUE_OF__isInfinite = 1 << 17;
    public static final int CACHE_EVALUATED_isMatrix = 1 << 18;
    public static final int CACHE_VALUE_OF__isMatrix = 1 << 19;
    public static final int CACHE_EVALUATED_isNaN = 1 << 20;
    public static final int CACHE_VALUE_OF__isNaN = 1 << 21;
    public static final int CACHE_EVALUATED_isZero = 1 << 22;
    public static final int CACHE_VALUE_OF__isZero = 1 << 23;
    public static final int CACHE_EVALUATED_isInvariant_X = 1 << 24;
    public static final int CACHE_VALUE_OF__isInvariant_X = 1 << 24;
    public static final int CACHE_EVALUATED_isInvariant_Y = 1 << 26;
    public static final int CACHE_VALUE_OF__isInvariant_Y = 1 << 27;
    //    public static final int CACHE_EVALUATED_isInvariant_Z=1 << 28;
//    public static final int CACHE_VALUE_OF__isInvariant_Z=1 << 29;
    public static final int CACHE_EVALUATED_isDM = 1 << 28;
    public static final int CACHE_VALUE_OF__isDM = 1 << 29;


    protected transient int _cache_isProperties;//new BitSet(40);

    public synchronized final boolean isNaN(BooleanSupplier evaluator) {
        return isThing(CACHE_EVALUATED_isNaN, CACHE_VALUE_OF__isNaN, evaluator);
    }

    public synchronized final boolean isThing(int evalIndex, int valIndex, BooleanSupplier evaluator) {
        if (!Maths.Config.isCacheExpressionPropertiesEnabled()) {
            return evaluator.getAsBoolean();
        }
        if (!_cache_is_evaluated(evalIndex)) {
            boolean v = evaluator.getAsBoolean();
            _cache_set(valIndex, v);
            return v;
        }
        return _cache_get(valIndex);
    }

    protected boolean _cache_is_evaluated(int bitPower) {
        boolean b = (_cache_isProperties & (bitPower)) != 0;
        if (!b) {
            _cache_set_evaluated(bitPower, true);
        }
        return b;
    }

    protected void _cache_set(int bitPower, boolean value) {
        if (value) {
            _cache_isProperties |= (bitPower);
        } else {
            _cache_isProperties &= ~(bitPower);
        }
    }

    protected boolean _cache_get(int bitPower) {
        return ((_cache_isProperties & (bitPower)) != 0);
    }

    protected void _cache_set_evaluated(int bitPower, boolean value) {
        if (value) {
            _cache_isProperties |= (bitPower);
        } else {
            _cache_isProperties &= ~(bitPower);
        }
    }

    public synchronized final boolean isInfinite(BooleanSupplier evaluator) {
        return isThing(CACHE_EVALUATED_isInfinite, CACHE_VALUE_OF__isInfinite, evaluator);
    }

    public synchronized final boolean isZero(BooleanSupplier evaluator) {
        return isThing(CACHE_EVALUATED_isZero, CACHE_VALUE_OF__isZero, evaluator);
    }

    public final boolean hasParams(BooleanSupplier evaluator) {
        return isThing(CACHE_EVALUATED_hasPrams, CACHE_VALUE_OF__hasPrams, evaluator);
    }
//    protected void _cache_set_evaluated(int bitPower) {
//        _cache_set_evaluated(bitPower,true);
//    }

    public synchronized final boolean isInvariant(Axis axis, Function<Axis, Boolean> evaluator) {
        if (!Maths.Config.isCacheExpressionPropertiesEnabled()) {
            switch (axis) {
                case X: {
                    return evaluator.apply(axis);
                }
                case Y: {
                    return evaluator.apply(axis);
                }
                case Z: {
                    return evaluator.apply(axis);
                }
            }
            throw new UnsupportedDomainDimensionException();
        }
        switch (axis) {
            case X: {
                if (!_cache_is_evaluated(CACHE_EVALUATED_isInvariant_X)) {
                    boolean v = evaluator.apply(axis);
                    _cache_set(CACHE_VALUE_OF__isInvariant_X, v);
                    return v;
                }
                return _cache_get(CACHE_VALUE_OF__isInvariant_X);
            }
            case Y: {
                if (!_cache_is_evaluated(CACHE_EVALUATED_isInvariant_Y)) {
                    boolean v = evaluator.apply(axis);
                    _cache_set(CACHE_VALUE_OF__isInvariant_Y, v);
                    return v;
                }
                return _cache_get(CACHE_VALUE_OF__isInvariant_Y);
            }
            case Z: {
                return evaluator.apply(axis);
                //TODO Z cache is not supported to help optimize memory usage (no more 'bits' in the int cache primitiveElement3D)
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

//    protected boolean _cache_isProperties_get(int bitPower){
//        return ((_cache_isProperties & (bitPower)) != 0);
////        return ((_cache_isProperties & (1 << bitIndex)) != 0);
////        if(_cache_isProperties==null){
////            _cache_isProperties=new BitSet(40);
////        }
////        return _cache_isProperties.get(bitIndex);
//    }

}
