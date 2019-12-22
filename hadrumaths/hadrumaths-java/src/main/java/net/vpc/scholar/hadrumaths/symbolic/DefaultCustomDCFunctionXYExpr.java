package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.BooleanMarker;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.MathsBase;

/**
 * Created by vpc on 4/30/14.
 */
public class DefaultCustomDCFunctionXYExpr extends GenericFunctionXY implements Cloneable, CustomDCFunctionXYExpr {
    private static final long serialVersionUID = 1L;
    private CustomDCFunctionXYDefinition definition;

    public DefaultCustomDCFunctionXYExpr(Expr xarg, Expr yarg, CustomDCFunctionXYDefinition definition) {
        super(xarg, yarg, FunctionType.COMPLEX);
        this.definition = definition;
    }

    @Override
    public String getFunctionName() {
        return definition.getName();
    }

    @Override
    public CustomDCFunctionXYDefinition getDefinition() {
        return definition;
    }

    public Expr newInstance(Expr... arguments){
        return new DefaultCustomDCFunctionXYExpr(arguments[0].toDC(),arguments[1].toDC(),getDefinition());
    }

    public Complex computeComplexArg(Complex x, Complex y, boolean xdef, boolean ydef, BooleanMarker defined) {
        if (!xdef && !ydef) {
            return Complex.ZERO;
        }
        defined.set();
        return definition.getEval().evalComplex(x.toDouble(), y.toDouble());
    }

    public Complex computeComplexArg(double x, double y, boolean xdef, boolean ydef, BooleanMarker defined) {
        if (!xdef && !ydef) {
            return Complex.ZERO;
        }
        defined.set();
        return (definition.getEval().evalComplex(x, y));
    }

    public double computeDoubleArg(double x, double y, boolean xdef, boolean ydef, BooleanMarker defined) {
        if (!xdef && !ydef) {
            return 0;
        }
        defined.set();
        return definition.getEval().evalComplex(x, y).toDouble();
    }

    @Override
    public Expr newInstance(Expr xargument, Expr yargument) {
        return new DefaultCustomDCFunctionXYExpr(xargument, yargument, definition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DefaultCustomDCFunctionXYExpr that = (DefaultCustomDCFunctionXYExpr) o;

        return definition != null ? definition.equals(that.definition) : that.definition == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (definition != null ? definition.hashCode() : 0);
        return result;
    }
}
