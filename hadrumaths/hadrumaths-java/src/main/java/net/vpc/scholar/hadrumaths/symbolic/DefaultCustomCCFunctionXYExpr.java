package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.BooleanMarker;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.MathsBase;

/**
 * Created by vpc on 4/30/14.
 */
public class DefaultCustomCCFunctionXYExpr extends GenericFunctionXY implements Cloneable,CustomCCFunctionXYExpr {
    private static final long serialVersionUID = 1L;
    private CustomCCFunctionXYDefinition definition;

    public DefaultCustomCCFunctionXYExpr(Expr xarg, Expr yarg, CustomCCFunctionXYDefinition definition) {
        super(xarg, yarg, FunctionType.COMPLEX);
        this.definition = definition;
    }

    public Expr newInstance(Expr... arguments){
        return new DefaultCustomCCFunctionXYExpr(arguments[0],arguments[1],getDefinition());
    }

    @Override
    public CustomCCFunctionXYDefinition getDefinition() {
        return definition;
    }

    @Override
    public String getFunctionName() {
        return definition.getName();
    }

    public Complex computeComplexArg(Complex x, Complex y, boolean xdef, boolean ydef, BooleanMarker defined) {
        if (!xdef && !ydef) {
            return Complex.ZERO;
        }
        defined.set();
        return definition.getEval().evalComplex(x, y);
    }

    public Complex computeComplexArg(double x, double y, boolean xdef, boolean ydef, BooleanMarker defined) {
        if (!xdef && !ydef) {
            return Complex.ZERO;
        }
        defined.set();
        return (definition.getEval().evalComplex(Complex.valueOf(x), Complex.valueOf(y)));
    }

    public double computeDoubleArg(double x, double y, boolean xdef, boolean ydef, BooleanMarker defined) {
        if (!xdef && !ydef) {
            return 0;
        }
        defined.set();
        return definition.getEval().evalComplex(Complex.valueOf(x), Complex.valueOf(y)).toDouble();
    }

    @Override
    public Expr newInstance(Expr xargument, Expr yargument) {
        return new DefaultCustomCCFunctionXYExpr(xargument, yargument, definition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DefaultCustomCCFunctionXYExpr that = (DefaultCustomCCFunctionXYExpr) o;

        return definition != null ? definition.equals(that.definition) : that.definition == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (definition != null ? definition.hashCode() : 0);
        return result;
    }
}