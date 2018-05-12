package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.OutBoolean;

/**
 * Created by vpc on 4/30/14.
 */
public class CustomDCFunctionXYExpr extends GenericFunctionXY implements Cloneable{
    private static final long serialVersionUID = 1L;
    private CustomDCFunctionXYDefinition definition;
    public CustomDCFunctionXYExpr(Expr xarg,Expr yarg, CustomDCFunctionXYDefinition definition) {
        super(xarg,yarg,FunctionType.COMPLEX);
        this.definition=definition;
    }

    @Override
    public String getFunctionName() {
        return definition.getName();
    }

    public Complex computeComplexArg(Complex x, Complex y, boolean xdef, boolean ydef, OutBoolean defined){
        if(!xdef && !ydef){
            return Complex.ZERO;
        }
        defined.set();
        return definition.getEval().evalComplex(x.toDouble(),y.toDouble());
    }

    public Complex computeComplexArg(double x, double y, boolean xdef, boolean ydef, OutBoolean defined){
        if(!xdef && !ydef){
            return Complex.ZERO;
        }
        defined.set();
        return (definition.getEval().evalComplex(x,y));
    }

    public double computeDoubleArg(double x, double y, boolean xdef, boolean ydef, OutBoolean defined){
        if(!xdef && !ydef){
            return 0;
        }
        defined.set();
        return definition.getEval().evalComplex(x,y).toDouble();
    }

    @Override
    public Expr newInstance(Expr xargument,Expr yargument) {
        return new CustomDCFunctionXYExpr(xargument,yargument,definition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CustomDCFunctionXYExpr that = (CustomDCFunctionXYExpr) o;

        return definition != null ? definition.equals(that.definition) : that.definition == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (definition != null ? definition.hashCode() : 0);
        return result;
    }
}
