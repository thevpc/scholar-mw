package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

/**
 * Created by vpc on 4/30/14.
 */
public class LtExpr extends ComparatorExpr implements Cloneable {
    private static final long serialVersionUID = 1L;

    public LtExpr(Expr xarg, Expr yarg) {
        super(xarg, yarg, FunctionType.DOUBLE);
    }

    @Override
    public String getFunctionName() {
        return "<";
    }

    public Complex computeComplexArg(Complex x, Complex y, boolean xdef, boolean ydef, BooleanMarker defined) {
        if (!xdef && !ydef) {
            return Complex.ZERO;
        }
        defined.set();
        return (x.compareTo(y) < 0) ? Complex.ONE : Complex.ZERO;
    }

    public Complex computeComplexArg(double x, double y, boolean xdef, boolean ydef, BooleanMarker defined) {
        if (!xdef && !ydef) {
            return Complex.ZERO;
        }
        defined.set();
        return x < y ? Complex.ONE : Complex.ZERO;
    }

    public double computeDoubleArg(double x, double y, boolean xdef, boolean ydef, BooleanMarker defined) {
        if (!xdef && !ydef) {
            return 0;
        }
        defined.set();
        return x < y ? 1 : 0;
    }

    @Override
    public Expr newInstance(Expr xargument, Expr yargument) {
        return new LtExpr(xargument, yargument);
    }


}
