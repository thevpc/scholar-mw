package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Expr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class CachedSequence extends AbstractExprList {
    private int size;
    private List<Expr> cache = new ArrayList<>();
    private ExprSeqCellIterator it;
    private boolean updated = false;

    public CachedSequence(int size, Expr[] cache, ExprSeqCellIterator it) {
        this.size = size;
        this.cache.addAll(Arrays.asList(cache));
        this.it = it;
    }

    @Override
    public int length() {
        return size;
    }

    @Override
    public Expr get(int index) {
        if (index < size && !updated) {
            Expr old = cache.get(index);
            if (old == null) {
                old = it.get(index);
                cache.set(index, old);
            }
            return old;
        } else {
            return cache.get(index);
        }
    }

    protected void setUpdated() {
        if (!updated) {
            updated = true;
            for (int i = 0; i < size; i++) {
                if (cache.get(i) == null) {
                    cache.set(i, (it.get(i)));
                }
            }
        }
    }

    @Override
    public void set(int index, Expr e) {
        cache.set(index, e);
    }

    @Override
    public void addAll(ExprList e) {
        setUpdated();
        cache.addAll(e.toExprJList());
    }

    @Override
    public void add(Expr e) {
        setUpdated();
        cache.add(e);
    }

    @Override
    public void addAll(Collection<? extends Expr> e) {
        setUpdated();
        cache.addAll(e);
    }

    @Override
    public ExprList copy() {
        try {
            if(true){
                throw new IllegalArgumentException("How to copy this?");
            }
            return (ExprList) clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException("Unsupported Clone");
        }
    }

    @Override
    public String toString() {
        return "CachedSequence{" +
                "items=" + it +
                ", size=" + size +
                '}';
    }
}
