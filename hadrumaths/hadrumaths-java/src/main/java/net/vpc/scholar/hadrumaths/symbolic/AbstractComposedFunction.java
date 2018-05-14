package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by vpc on 4/30/14.
 */
public abstract class AbstractComposedFunction extends AbstractVerboseExpr {
    private static final long serialVersionUID = 1L;

    public AbstractComposedFunction() {

    }

    @Override
    public boolean isDoubleTyped() {
        for (Expr e : getSubExpressions()) {
            if (!e.isDoubleTyped()) {
                return false;
            }
        }
        return true;
    }

    public abstract Expr[] getArguments();

    @Override
    public List<Expr> getSubExpressions() {
        return Arrays.asList(getArguments());
    }


    @Override
    public boolean isInfiniteImpl() {
        return false;
    }

    @Override
    public boolean isZeroImpl() {
        return false;
    }

    @Override
    public boolean isNaNImpl() {
        return false;
    }

    @Override
    public DoubleToComplex toDC() {
        if (!isDC()) {
            throw new ClassCastException();
        }
        return this;
    }

    @Override
    public DoubleToDouble toDD() {
        if (!isDD()) {
            throw new ClassCastException("Unable to Cast to DD :: "+getClass().getName()+" = "+toString());
        }
        return this;
    }

//    @Override
//    public IDDx toDDx() {
//        if (!isDDx()) {
//            throw new ClassCastException();
//        }
//        return this;
//    }

    @Override
    public DoubleToMatrix toDM() {
        if (!isDM()) {
            throw new ClassCastException();
        }
        return this;
    }

    @Override
    public DoubleToVector toDV() {
        if (!isDV()) {
            throw new ClassCastException();
        }
        return this;
    }

    public abstract Expr newInstance(Expr... arguments);

    @Override
    public String getComponentTitle(int row, int col) {
        return Expressions.getMatrixExpressionTitleByChildren(this, row, col);
    }

    @Override
    public Expr setParam(String name, double value) {
        Expr[] a = getArguments();
        Expr[] b = new Expr[a.length];
        boolean updated = false;
        for (int i = 0; i < a.length; i++) {
            Expr ai = a[i];
            Expr bi = ai.setParam(name, value);
            if (bi == null) {
                b[i] = ai;
            } else if (bi != ai) {
                updated = true;
            }
            b[i] = bi;
        }
        if (updated) {
            Expr e = newInstance(b);
            e = Any.copyProperties(this, e);
            return Any.updateTitleVars(e, name, value);
        }
        return this;
    }

    @Override
    public Expr composeX(Expr xreplacement) {
        Expr[] a = getArguments();
        Expr[] b = new Expr[a.length];
        boolean updated = false;
        for (int i = 0; i < a.length; i++) {
            Expr ai = a[i];
            Expr bi = ai.composeX(xreplacement);
            if (bi != null && bi != ai) {
                updated = true;
            }
            b[i] = bi;
        }
        if (updated) {
            Expr e = newInstance(b);
            e = Any.copyProperties(this, e);
            return e;
        }
        return null;
    }

    @Override
    public Expr composeY(Expr yreplacement) {
        Expr[] a = getArguments();
        Expr[] b = new Expr[a.length];
        boolean updated = false;
        for (int i = 0; i < a.length; i++) {
            Expr ai = a[i];
            Expr bi = ai.composeY(yreplacement);
            if (bi != null && bi != ai) {
                updated = true;
            }
            b[i] = bi;
        }
        if (updated) {
            Expr e = newInstance(b);
            e = Any.copyProperties(this, e);
            return e;
        }
        return null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractComposedFunction)) return false;

        AbstractComposedFunction that = (AbstractComposedFunction) o;

        return true;
    }

    public abstract String getFunctionName();

//    public String getFunctionName() {
//        return functionName;
//    }

    @Override
    public Expr getComponent(Axis a) {
        switch (a) {
            case X: {
                return getComponent(0, 0);
            }
            case Y: {
                return getComponent(1, 0);
            }
            case Z: {
                return getComponent(2, 0);
            }
        }
        throw new IllegalArgumentException("Illegal axis");
    }

//    @Override
//    public Complex computeComplex(double x,BooleanMarker defined) {
//        Out<Range> ranges = new Out<>();
//        Complex complex = computeComplex(new double[]{x}, null, ranges)[0];
//        Range range = ranges.get();
//        defined.set(range!=null && range.getDefined1().get(0));
//        return complex;
//    }

    @Override
    public boolean isInvariantImpl(Axis axis) {
        for (Expr e : getSubExpressions()) {
            if (!e.isInvariant(axis)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isDoubleImpl() {
        return false;
//        for (Expr e : getSubExpressions()) {
//            if (!e.isDouble()) {
//                return false;
//            }
//        }
//        return true;
    }

//    @Override
//    public boolean isComplexImpl() {
//        return false;
////        for (Expr e : getSubExpressions()) {
////            if (!e.isComplex()) {
////                return false;
////            }
////        }
////        return true;
//    }

//    @Override
//    public boolean isDoubleExprImpl() {
//        return false;
////        for (Expr e : getSubExpressions()) {
////            if (!e.isDoubleExpr()) {
////                return false;
////            }
////        }
////        return true;
//    }

//    @Override
//    public boolean isMatrixImpl() {
//        for (Expr e : getSubExpressions()) {
//            if (!e.isMatrix()) {
//                return false;
//            }
//        }
//        return true;
//    }


    @Override
    public boolean isDCImpl() {
        for (Expr e : getSubExpressions()) {
            if (!e.isDC()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isDDImpl() {
        for (Expr e : getSubExpressions()) {
            if (!e.isDD()) {
                return false;
            }
        }
        return true;
    }

//    @Override
//    public boolean isDDx() {
//        for (Expr e : getSubExpressions()) {
//            if(!e.isDDx()){
//                return false;
//            }
//        }
//        return true;
//    }

    @Override
    public boolean isDMImpl() {
        for (Expr e : getSubExpressions()) {
            if (!e.isDM()) {
                return false;
            }
        }
        return true;
    }


    @Override
    public boolean isDVImpl() {
        for (Expr e : getSubExpressions()) {
            if (!e.isDV()) {
                return false;
            }
        }
        return true;
    }


//    @Override
//    public boolean isScalarExpr() {
//        for (Expr e : getSubExpressions()) {
//            if(!e.isScalarExpr()){
//                return false;
//            }
//        }
//        return true;
//    }
}
