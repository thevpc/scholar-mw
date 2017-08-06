package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.TVector;
import net.vpc.scholar.hadrumaths.TVectorCell;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public class SimplifiedSeq<T extends Expr> implements TVectorCell<Expr> {
    private TVector<T> sequence;

    public SimplifiedSeq(TVector<T> sequence) {
        this.sequence = sequence;
    }

    @Override
    public Expr get(int row) {
        return Maths.simplify(sequence.get(row));
    }

    @Override
    public String toString() {
        return "SimplifiedSeq{" +
                "seq=" + sequence +
                '}';
    }
}
