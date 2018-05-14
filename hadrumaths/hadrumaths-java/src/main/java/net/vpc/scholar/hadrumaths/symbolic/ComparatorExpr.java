package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

public abstract class ComparatorExpr extends GenericFunctionXY {
    private static final long serialVersionUID = 1L;

    public ComparatorExpr(Expr xargument, Expr yargument) {
        super(xargument, yargument);
    }

    public ComparatorExpr(Expr xargument, Expr yargument, FunctionType lowerFunctionType) {
        super(xargument, yargument, lowerFunctionType);
    }

//    @Override
//    public String toString() {
//        return "("+getXArgument()+" "+getFunctionName()+" "+getYArgument()+")";
//    }

    @Override
    public Expr mul(Domain domain) {
        return newInstance(
                getXArgument().getDomain().intersect(domain),
                getYArgument().getDomain().intersect(domain)
        );
    }

    @Override
    public boolean isDDImpl() {
        return true;
    }

    @Override
    public boolean isDCImpl() {
        return true;
    }

    @Override
    public boolean isDMImpl() {
        return true;
    }

    @Override
    public double computeDouble(double x, BooleanMarker defined) {
        if (contains(x)) {
            BooleanRef xdefined = BooleanMarker.ref();
            BooleanRef ydefined = BooleanMarker.ref();
            switch (getFunctionType()) {
                case DOUBLE: {
                    double a = getXArgument().toDD().computeDouble(x, xdefined);
                    double b = getYArgument().toDD().computeDouble(x, ydefined);
                    return computeDoubleArg(a, b, ydefined.get(), xdefined.get(), defined);
                }
                case COMPLEX: {
                    Complex a = getXArgument().toDC().computeComplex(x, xdefined);
                    Complex b = getYArgument().toDC().computeComplex(x, ydefined);
                    return computeComplexArg(a, b, ydefined.get(), xdefined.get(), defined).getReal();
                }
                case MATRIX: {
                    Matrix a = getXArgument().toDM().computeMatrix(x/*, xdefined*/);
                    Matrix b = getYArgument().toDM().computeMatrix(x/*, xdefined*/);
//                    return evalMatrix(a, b, ydefined.get(), xdefined.get(), defined).getReal();
                    return evalMatrix(a, b).isZero() ? 0 : 1;
                }
            }
        }
        return 0;
    }

    @Override
    public double computeDouble(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            BooleanRef xdefined = BooleanMarker.ref();
            BooleanRef ydefined = BooleanMarker.ref();
            switch (getFunctionType()) {
                case DOUBLE: {
                    double a = getXArgument().toDD().computeDouble(x, y, xdefined);
                    double b = getYArgument().toDD().computeDouble(x, y, ydefined);
                    return computeDoubleArg(a, b, ydefined.get(), xdefined.get(), defined);
                }
                case COMPLEX: {
                    Complex a = getXArgument().toDC().computeComplex(x, y, xdefined);
                    Complex b = getYArgument().toDC().computeComplex(x, y, ydefined);
                    return computeComplexArg(a, b, ydefined.get(), xdefined.get(), defined).getReal();
                }
                case MATRIX: {
                    Matrix a = getXArgument().toDM().computeMatrix(x, y/*, xdefined*/);
                    Matrix b = getYArgument().toDM().computeMatrix(x, y/*, xdefined*/);
//                    return evalMatrix(a, b, ydefined.get(), xdefined.get(), defined).getReal();
                    return evalMatrix(a, b).isZero() ? 0 : 1;
                }
            }
        }
        return 0;
    }

    @Override
    public double computeDouble(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            BooleanRef xdefined = BooleanMarker.ref();
            BooleanRef ydefined = BooleanMarker.ref();
            switch (getFunctionType()) {
                case DOUBLE: {
                    double a = getXArgument().toDD().computeDouble(x, y, z, xdefined);
                    double b = getYArgument().toDD().computeDouble(x, y, z, ydefined);
                    return computeDoubleArg(a, b, ydefined.get(), xdefined.get(), defined);
                }
                case COMPLEX: {
                    Complex a = getXArgument().toDC().computeComplex(x, y, z, xdefined);
                    Complex b = getYArgument().toDC().computeComplex(x, y, z, ydefined);
                    return computeComplexArg(a, b, ydefined.get(), xdefined.get(), defined).getReal();
                }
                case MATRIX: {
                    Matrix a = getXArgument().toDM().computeMatrix(x,y,z/*, xdefined*/);
                    Matrix b = getYArgument().toDM().computeMatrix(x,y,z/*, xdefined*/);
//                    return evalMatrix(a, b, ydefined.get(), xdefined.get(), defined).getReal();
                    return evalMatrix(a, b).isZero() ? 0 : 1;
                }
            }
        }
        return 0;
    }

    @Override
    public Complex computeComplex(double x, BooleanMarker defined) {
        if (contains(x)) {
            BooleanRef xdefined = BooleanMarker.ref();
            BooleanRef ydefined = BooleanMarker.ref();
            switch (getFunctionType()) {
                case DOUBLE:
                case COMPLEX: {
                    Complex a = getXArgument().toDC().computeComplex(x, xdefined);
                    Complex b = getYArgument().toDC().computeComplex(x, ydefined);
                    return computeComplexArg(a, b, ydefined.get(), xdefined.get(), defined);
                }
                case MATRIX: {
                    Matrix a = getXArgument().toDM().computeMatrix(x/*, xdefined*/);
                    Matrix b = getYArgument().toDM().computeMatrix(x/*, xdefined*/);
//                    return evalMatrix(a, b, ydefined.get(), xdefined.get(), defined).getReal();
                    return evalMatrix(a, b).isZero() ? Complex.ZERO : Complex.ONE;
                }
            }
        }
        return Complex.ZERO;
    }

    @Override
    public Complex computeComplex(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            BooleanRef xdefined = BooleanMarker.ref();
            BooleanRef ydefined = BooleanMarker.ref();
            switch (getFunctionType()) {
                case DOUBLE:
                case COMPLEX: {
                    Complex a = getXArgument().toDC().computeComplex(x, y, xdefined);
                    Complex b = getYArgument().toDC().computeComplex(x, y, ydefined);
                    return computeComplexArg(a, b, ydefined.get(), xdefined.get(), defined);
                }
                case MATRIX: {
                    Matrix a = getXArgument().toDM().computeMatrix(x,y/*, xdefined*/);
                    Matrix b = getYArgument().toDM().computeMatrix(x,y/*, xdefined*/);
//                    return evalMatrix(a, b, ydefined.get(), xdefined.get(), defined).getReal();
                    return evalMatrix(a, b).isZero() ? Complex.ZERO : Complex.ONE;
                }
            }
        }
        return Complex.ZERO;
    }

    @Override
    public Complex computeComplex(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            BooleanRef xdefined = BooleanMarker.ref();
            BooleanRef ydefined = BooleanMarker.ref();
            switch (getFunctionType()) {
                case DOUBLE:
                case COMPLEX: {
                    Complex a = getXArgument().toDC().computeComplex(x, y, z, xdefined);
                    Complex b = getYArgument().toDC().computeComplex(x, y, z, ydefined);
                    return computeComplexArg(a, b, ydefined.get(), xdefined.get(), defined);
                }
                case MATRIX: {
                    Matrix a = getXArgument().toDM().computeMatrix(x,y,z/*, xdefined*/);
                    Matrix b = getYArgument().toDM().computeMatrix(x,y,z/*, xdefined*/);
//                    return evalMatrix(a, b, ydefined.get(), xdefined.get(), defined).getReal();
                    return evalMatrix(a, b).isZero() ? Complex.ZERO : Complex.ONE;
                }
            }
        }
        return Complex.ZERO;
    }
}
