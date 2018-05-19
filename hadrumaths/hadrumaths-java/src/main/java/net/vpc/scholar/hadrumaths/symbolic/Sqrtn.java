package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.BooleanMarker;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;

/**
 * Created by vpc on 4/30/14.
 */
public class Sqrtn extends GenericFunctionX implements Cloneable {
    private static final long serialVersionUID = 1L;
    private int n;

    public Sqrtn(Expr arg, int n) {
        super("sqrtn", arg, FunctionType.COMPLEX);
        this.n = n;
    }

    @Override
    public String getFunctionName() {
        return "sqrtn";
    }


    public Complex computeComplexArg(Complex c, BooleanMarker defined) {
        defined.set();
        return c.sqrt(n);
    }

    public double computeDoubleArg(double c, BooleanMarker defined) {
        defined.set();
        return Maths.sqrt(c, n);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Sqrtn(argument, n);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Sqrtn sqrtn = (Sqrtn) o;

        return n == sqrtn.n;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + n;
        return result;
    }
}
