package net.vpc.scholar.hadrumaths.scalarproducts.formal;

import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.Domain;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 10 juin 2007 13:48:04
 */
public class ReverseFormalScalarProductHelper implements FormalScalarProductHelper{
    private FormalScalarProductHelper reversed;

    public ReverseFormalScalarProductHelper(FormalScalarProductHelper reversed) {
        this.reversed = reversed;
    }

    public double compute(Domain domain, DoubleToDouble f1, DoubleToDouble f2, FormalScalarProductOperator sp) {
        return reversed.compute(domain, f2, f1, sp);
    }

    public FormalScalarProductHelper getReversed() {
        return reversed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReverseFormalScalarProductHelper)) return false;

        ReverseFormalScalarProductHelper that = (ReverseFormalScalarProductHelper) o;

        if (reversed != null ? !reversed.equals(that.reversed) : that.reversed != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return reversed != null ? reversed.hashCode() : 0;
    }
}
