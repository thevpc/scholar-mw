package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.OutBoolean;

/**
 * Created by vpc on 4/30/14.
 */
public class Sqrt extends GenericFunctionX implements Cloneable{
    private static final long serialVersionUID = 1L;
    public Sqrt(Expr arg) {
        super("sqrt",arg,FunctionType.COMPLEX);
    }

    @Override
    public String getFunctionName() {
        return "sqrt";
    }

    public Complex computeComplexArg(Complex c, OutBoolean defined){
        defined.set();
        return c.sqrt();
    }

    public double computeDoubleArg(double c, OutBoolean defined){
        defined.set();
        return Maths.sqrt(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Sqrt(argument);
    }

}
