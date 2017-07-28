package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 4/29/14.
 */
public abstract class AbstractCompositeExpr extends AbstractExprPropertyAware {

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
        for (Expr e : getSubExpressions()) {
            if (!e.isDouble()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isComplexImpl() {
        for (Expr e : getSubExpressions()) {
            if (!e.isComplex()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isDoubleExprImpl() {
        for (Expr e : getSubExpressions()) {
            if (!e.isDoubleExpr()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isMatrixImpl() {
        for (Expr e : getSubExpressions()) {
            if(!e.isMatrix()){
                return false;
            }
        }
        return true;
    }


    @Override
    public boolean isDCImpl() {
        for (Expr e : getSubExpressions()) {
            if(!e.isDC()){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isDDImpl() {
        for (Expr e : getSubExpressions()) {
            if(!e.isDD()){
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
            if(!e.isDM()){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isDVImpl() {
        for (Expr e : getSubExpressions()) {
            if(!e.isDV()){
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
