package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.ArrayExprList;
import net.vpc.scholar.hadrumaths.Expr;

import java.util.Collection;

public abstract class AbstractUpdatableExprSequence extends AbstractExprList implements Cloneable {
    private ExprList delegate;

    @Override
    public final int length() {
        if (delegate != null) {
            return delegate.length();
        }
        return lengthImpl();
    }

    protected abstract int lengthImpl() ;
    protected abstract Expr getImpl(int index) ;

    @Override
    public final Expr get(int index) {
        if (delegate != null) {
            return delegate.get(index);
        }
        return getImpl(index);
    }

    @Override
    public void addAll(ExprList e) {
        if (delegate == null) {
            delegate = new ArrayExprList();
        }
        delegate.addAll(e);
    }

    @Override
    public void add(Expr e) {
        if (delegate == null) {
            delegate = new ArrayExprList();
        }
        delegate.add(e);
    }

    @Override
    public void addAll(Collection<? extends Expr> e) {
        if (delegate == null) {
            delegate = new ArrayExprList();
        }
        delegate.addAll(e);
    }

    @Override
    public ExprList copy() {
        if (delegate != null) {
            return delegate.copy();
        }
        try {
            return (ExprList) clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException("Unsupported Clone");
        }
    }
}
