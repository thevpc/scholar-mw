package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Expr;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class UnmodifiableSequence extends AbstractExprList implements Cloneable {
    private final int size;
    private final ExprSeqCellIterator it;

    public UnmodifiableSequence(int size, ExprSeqCellIterator it) {
        this.size = size;
        this.it = it;
    }

    @Override
    public int length() {
        return size;
    }

    @Override
    public Expr get(int index) {
        return it.get(index);
    }

    @Override
    public void set(int index, Expr e) {
        throw new IllegalArgumentException("Unmodifiable");
    }

    @Override
    public String toString() {
        return "UnmodifiableSequence{" +
                "size=" + size +
                ", items=" + it +
                '}';
    }

}
