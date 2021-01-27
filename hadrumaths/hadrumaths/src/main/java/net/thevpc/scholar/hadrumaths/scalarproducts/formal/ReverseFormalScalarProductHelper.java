package net.thevpc.scholar.hadrumaths.scalarproducts.formal;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 10 juin 2007 13:48:04
 */
public class ReverseFormalScalarProductHelper implements FormalScalarProductHelper {
    private final FormalScalarProductHelper reversed;

    public ReverseFormalScalarProductHelper(FormalScalarProductHelper reversed) {
        this.reversed = reversed;
    }

    public double eval(Domain domain, DoubleToDouble f1, DoubleToDouble f2, FormalScalarProductOperator sp) {
        return reversed.eval(domain, f2, f1, sp);
    }

    public FormalScalarProductHelper getReversed() {
        return reversed;
    }

    @Override
    public int hashCode() {
        return reversed != null ? reversed.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReverseFormalScalarProductHelper)) return false;

        ReverseFormalScalarProductHelper that = (ReverseFormalScalarProductHelper) o;

        return reversed != null ? reversed.equals(that.reversed) : that.reversed == null;
    }
}
