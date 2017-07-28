package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.ArrayExprList;
import net.vpc.scholar.hadrumaths.Expr;

import java.util.Collection;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class PreloadedSequence extends AbstractExprList {
    final ArrayExprList cache;

    public PreloadedSequence(int size, ExprSeqCellIterator it) {
        cache = new ArrayExprList();
        for (int i = 0; i < size; i++) {
            cache.add(it.get(i));
        }
    }

    @Override
    public int length() {
        return cache.size();
    }

    @Override
    public Expr get(int index) {
        return cache.get(index);
    }

    @Override
    public void set(int index, Expr e) {
        cache.set(index, e);
    }

    @Override
    public void addAll(ExprList e) {
        cache.addAll(e);
    }

    @Override
    public void add(Expr e) {
        cache.add(e);
    }

    @Override
    public void addAll(Collection<? extends Expr> e) {
        cache.addAll(e);
    }

    @Override
    public String toString() {
        return "PreloadedSequence{" +
                "cache=" + cache +
                '}';
    }
}
