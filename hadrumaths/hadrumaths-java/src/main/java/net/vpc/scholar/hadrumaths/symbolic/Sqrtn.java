package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;

/**
 * Created by vpc on 4/30/14.
 */
public class Sqrtn extends GenericFunctionX implements Cloneable{
    int n;
    public Sqrtn(Expr arg,int n) {
        super("sqrtn",arg,FunctionType.COMPLEX);
        this.n=n;
    }


    public Complex evalComplex(Complex c){
        return c.sqrt(n);
    }

    protected double evalDouble(double c){
        return Maths.sqrt(c,n);
    }

    @Override
    public Expr newInstance(Expr argument) {
        return new Sqrtn(argument,n);
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
