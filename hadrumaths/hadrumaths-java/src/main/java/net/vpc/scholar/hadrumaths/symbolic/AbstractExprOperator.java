package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.conv.DC2DM;
import net.vpc.scholar.hadrumaths.symbolic.conv.DV2DM;

/**
 * Created by vpc on 8/24/14.
 */
public abstract class AbstractExprOperator extends AbstractPolymorphExpr implements ExprOperator {
    private static final long serialVersionUID = 1L;

    public ComplexMatrix computeMatrix(double x) {
        return Expressions.computeMatrix(this, x);
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

    protected boolean isDCImpl() {
        for (Expr e : getSubExpressions()) {
            if (!e.isDC()) {
                return false;
            }
        }
        return true;
    }

//    protected boolean isComplexImpl() {
//        for (Expr e : getSubExpressions()) {
//            if (!e.isComplex()) {
//                return false;
//            }
//        }
//        return true;
//    }

//    protected boolean isDoubleImpl() {
//        for (Expr e : getSubExpressions()) {
//            if (!e.isDouble()) {
//                return false;
//            }
//        }
//        return true;
//    }

    protected boolean isMatrixImpl() {
        for (Expr e : getSubExpressions()) {
            if (!e.isMatrix()) {
                return false;
            }
        }
        return true;
    }

    protected boolean isDDImpl() {
        for (Expr e : getSubExpressions()) {
            if (!e.isDD()) {
                //e.isDD();
                return false;
            }
        }
        return true;
    }

    protected boolean isDVImpl() {
        for (Expr e : getSubExpressions()) {
            if (!e.isDV()) {
                return false;
            }
        }
        return true;
    }

    protected boolean isDMImpl() {
        for (Expr e : getSubExpressions()) {
            if (!e.isDM()) {
                return false;
            }
        }
        return true;
    }

    public DoubleToComplex toDC() {
        if (!isDC()) {
            throw new ClassCastException();
        }
        return this;
    }

    public DoubleToDouble toDD() {
        if (!isDD()) {
//            boolean aa=isDD();
            throw new ClassCastException();
        }
        return this;
    }

//    public IDDx toDDx() {
//        if (!isDDx()) {
//            throw new ClassCastException();
//        }
//        return this;
//    }

//    public DoubleToMatrix toDM() {
//        if (!isDM()) {
//            if (isDV()) {
//                return new DV2DM(toDV());
//            }
//            if (isDC()) {
//                return new DC2DM(toDC());
//            }
//            if (isDD()) {
//                return new DD2DC(toDD()).toDM();
//            }
//            throw new ClassCastException();
//        }
//        return this;
//    }

    protected boolean isNaNImpl() {
        for (Expr expression : getSubExpressions()) {
            if (expression.isNaN()) {
                return true;
            }
        }
        return false;
    }

    protected boolean isInfiniteImpl() {
        for (Expr expression : getSubExpressions()) {
            if (expression.isInfinite()) {
                return true;
            }
        }
        return false;
    }

    public Complex[] computeComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, x, y, d0, ranges);
    }

    public Complex[] computeComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, x, y, d0, ranges);
    }

//    public Complex computeComplexArg(double x, double y) {
//        return Expressions.computeComplexArg(this, x, y);
//    }

    public ComplexMatrix[] computeMatrix(double[] x, double y, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, x, y, d0, ranges);
    }

    public ComplexMatrix[] computeMatrix(double x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, x, y, d0, ranges);
    }

    public ComplexMatrix computeMatrix(double x, double y) {
        return Expressions.computeMatrix(this, x, y);
    }

    public double[] computeDouble(double[] x, double y, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, x, y, d0, ranges);
    }

    public double[] computeDouble(double x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, x, y, d0, ranges);
    }

//    public double computeDouble(double x, double y) {
//        return Expressions.computeDouble(this, x, y);
//    }

//    public double computeDouble(double x) {
//        return Expressions.computeDouble(this, x);
//    }

    public DoubleToDouble getRealDD() {
        return new Real(this);
    }

    public DoubleToDouble getImagDD() {
        return new Imag(this);
    }

    @Override
    public String getComponentTitle(int row, int col) {
        return Expressions.getMatrixExpressionTitleByChildren(this, row, col);
    }


    @Override
    public DoubleToComplex getComponent(Axis a) {
        switch (a) {
            case X: {
                return getComponent(0, 0).toDC();
            }
            case Y: {
                return getComponent(1, 0).toDC();
            }
            case Z: {
                return getComponent(2, 0).toDC();
            }
        }
        throw new IllegalArgumentException("Illegal axis");
    }


    @Override
    public DoubleToVector toDV() {
        if (!isDV()) {
            throw new ClassCastException();
        }
        return this;
    }

//    public Complex computeComplex(double x, double y, double z){
//        return computeComplex(new double[]{x},new double[]{y},new double[]{z})[0][0][0];
//    }

//    public Complex computeComplex(double x, double y){
//        return computeComplex(new double[]{x},new double[]{y})[0][0];
//    }

//    public Complex computeComplexArg(double x,BooleanMarker defined){
//        Out<Range> ranges = new Out<>();
//        Complex complex = computeComplexArg(new double[]{x}, null, ranges)[0];
//        defined.set(ranges.get().getDefined1().get(0));
//        return complex;
//    }

    protected boolean isInvariantImpl(Axis axis) {
        for (Expr expr : getSubExpressions()) {
            if (!expr.isInvariant(axis)) {
                return false;
            }
        }
        return true;
    }


}
