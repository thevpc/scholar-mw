package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.OutBoolean;

/**
 * Created by vpc on 4/30/14.
 */
public class Db2 extends TrigoFunctionX implements Cloneable {
    private static final long serialVersionUID = 1L;
    public Db2(Expr arg) {
        super("db2", arg, FunctionType.DOUBLE);
    }

    @Override
    public String getFunctionName() {
        return "db2";
    }

    public Complex computeComplexArg(Complex c, OutBoolean defined) {
        defined.set();
        return c.db2();
    }

    public double computeDoubleArg(double c, OutBoolean defined) {
        defined.set();
        return Maths.db2(c);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Db2(argument);
    }

}
