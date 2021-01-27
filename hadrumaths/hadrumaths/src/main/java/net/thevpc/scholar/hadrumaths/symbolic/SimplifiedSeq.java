package net.thevpc.scholar.hadrumaths.symbolic;

import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Vector;
import net.thevpc.scholar.hadrumaths.VectorCell;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public class SimplifiedSeq<T extends Expr> implements VectorCell<Expr> {
    private static final long serialVersionUID = 1L;
    private final Vector<T> sequence;

    public SimplifiedSeq(Vector<T> sequence) {
        this.sequence = sequence;
    }

    @Override
    public Expr get(int row) {
        return sequence.get(row).simplify();
    }

    @Override
    public String toString() {
        return "SimplifiedSeq{" +
                "seq=" + sequence +
                '}';
    }
}
