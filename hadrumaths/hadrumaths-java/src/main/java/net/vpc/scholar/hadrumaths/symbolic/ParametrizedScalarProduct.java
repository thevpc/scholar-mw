package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.impl.AbstractObjectFormat;

import java.lang.ref.WeakReference;

public class ParametrizedScalarProduct extends GenericFunctionXY {
    static {
        FormatFactory.register(ParametrizedScalarProduct.class, new AbstractObjectFormat<ParametrizedScalarProduct>() {
            @Override
            public void format(StringBuilder sb, ParametrizedScalarProduct o, ObjectFormatParamSet format) {
                boolean par = format.containsParam(FormatFactory.REQUIRED_PARS);
                format = format.add(FormatFactory.REQUIRED_PARS);
                if (par) {
                    sb.append("(");
                }
                FormatFactory.format(sb, o.getXArgument(), format);
                sb.append(" ").append(o.getFunctionName()).append(" ");
                FormatFactory.format(sb, o.getYArgument(), format);
                if (par) {
                    sb.append(")");
                }
            }
        });
    }

    private static final long serialVersionUID = 1L;
    private transient WeakReference<Expr> meSimplified = new WeakReference<Expr>(null);

    public ParametrizedScalarProduct(Expr xargument, Expr yargument) {
        super();
        init(xargument, yargument, null, false);
//        if( xargument instanceof DoubleValue && yargument instanceof DoubleValue && !isDD()){
//            init(xargument, yargument, null,false);
//        }
    }

    @Override
    public String getFunctionName() {
        return "**";
    }


//    public ParametrizedScalarProduct(Expr xargument, Expr yargument, FunctionType lowerFunctionType,boolean hermitian) {
//        super();
//        init(xargument, yargument, lowerFunctionType,false);
//        this.hermitian=hermitian;
//    }

    @Override
    public Complex computeComplexArg(Complex x, Complex y, boolean xdef, boolean ydef, BooleanMarker defined) {
        if (!xdef && !ydef) {
            return Complex.ZERO;
        }
        defined.set();
        Expr e = getSimplifiedExpr();
        return e.toComplex();
    }

    private Expr getSimplifiedExpr() {
        Expr e;
        if (meSimplified == null) {
            e = this.simplify();
            meSimplified = new WeakReference<Expr>(e);
        } else {
            e = meSimplified.get();
            if (e == null) {
                e = this.simplify();
                meSimplified = new WeakReference<Expr>(e);
            }
        }
        return e;
    }

    @Override
    public Complex computeComplexArg(double x, double y, boolean xdef, boolean ydef, BooleanMarker defined) {
        if (!xdef && !ydef) {
            return Complex.ZERO;
        }
        defined.set();
        Expr e = getSimplifiedExpr();
        return e.toComplex();
    }

    @Override
    public double computeDoubleArg(double x, double y, boolean xdef, boolean ydef, BooleanMarker defined) {
        if (!xdef && !ydef) {
            return 0;
        }
        defined.set();
        Expr e = getSimplifiedExpr();
        return e.toComplex().toDouble();
    }

    @Override
    public Expr newInstance(Expr xargument, Expr yargument) {
        return new ParametrizedScalarProduct(xargument, yargument);
    }

//    @Override
//    public String toString() {
//        return "("+getXArgument()+getFunctionName()+getYArgument()+")";
//    }
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        if (!super.equals(o)) return false;
//
//        ParametrizedScalarProduct that = (ParametrizedScalarProduct) o;
//
//        return true;
//    }

//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        return result;
//    }

    @Override
    public Expr mul(double other) {
        return new ParametrizedScalarProduct(getXArgument().mul(other), getYArgument().mul(other));
    }

    @Override
    public Expr mul(Complex other) {
        return new ParametrizedScalarProduct(getXArgument().mul(other), getYArgument().mul(other));
    }

    @Override
    public Expr mul(Domain other) {
        return new ParametrizedScalarProduct(getXArgument().mul(other), getYArgument().mul(other));
    }
}
