package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by vpc on 2/14/15.
 */
public abstract class AbstractExprList implements ExprList {
    @Override
    public ArrayExprList toList() {
        int length = length();
        ArrayExprList list = new ArrayExprList(length);
        for (int i = 0; i < length; i++) {
            list.add(get(i));
        }
        return list;
    }

    @Override
    public boolean isComplex() {
        for (Expr expr : this) {
            if(!expr.isComplex()){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isDouble() {
        for (Expr expr : this) {
            if(!expr.isDouble()){
                return false;
            }
        }
        return true;
    }

    @Override
    public final int size() {
        return length();
    }

    @Override
    public final Expr apply(int index) {
        return get(index);
    }

    @Override
    public final Expr[] toArray() {
        return toArray(new Expr[size()]);
    }

    @Override
    public <T extends Expr> T[] toArray(T[] a) {
        return toList().toArray(a);
    }

    @Override
    public Iterator<Expr> iterator() {
        return new Iterator<Expr>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < length();
            }

            @Override
            public Expr next() {
                return get(i++);
            }
        };
    }

    @Override
    public ExprList preload() {
        return DefaultExprSequenceFactory.INSTANCE.newPreloadedSequence(length(), this);
    }

    @Override
    public ExprList withCache() {
        return DefaultExprSequenceFactory.INSTANCE.newCachedSequence(length(), this);
    }

    @Override
    public ExprList simplify() {
        return DefaultExprSequenceFactory.INSTANCE.newUnmodifiableSequence(length(), new SimplifiedSeq(this));
    }

    public Expr scalarProduct(Matrix v) {
        return scalarProduct(v.toVector());
    }

    public Expr scalarProduct(Vector v) {
        List<Expr> other = new ArrayList<Expr>();
        int max1 = v.size();
        int max2 = size();
        if(max1!=max2){
            throw new IllegalArgumentException("Invalid size "+max1+" <> "+max2);
        }
        int max = Math.max(max1, max2);
        for (int i = 0; i < max; i++) {
            other.add(Maths.mul(get(i), v.get(i)));
        }
        return new Plus(other);
    }

    public Expr scalarProduct(ExprList v) {
        List<Expr> other = new ArrayList<Expr>();
        int max = Math.max(v.size(), size());
        for (int i = 0; i < max; i++) {
            other.add(Maths.mul(get(i), v.get(i)));
        }
        return new Plus(other);
    }

    public Expr scalarProduct(Expr v) {
        List<Expr> other = new ArrayList<Expr>();
        int max = size();
        for (int i = 0; i < max; i++) {
            other.add(Maths.mul(get(i), v));
        }
        return new Plus(other);
    }

    public ExprList transform(ElementOp op) {
        ExprList other = new ArrayExprList();
        int index = 0;
        for (Expr e : this) {
            other.add(op.eval(index, e));
            index++;
        }
        return other;
    }

    @Override
    public void set(int index, Expr e) {
        throw new IllegalArgumentException("Unmodifiable");
    }

    @Override
    public void addAll(ExprList e) {
        throw new IllegalArgumentException("Unmodifiable");
    }

    @Override
    public void add(Expr e) {
        throw new IllegalArgumentException("Unmodifiable");
    }

    @Override
    public void addAll(Collection<? extends Expr> e) {
        throw new IllegalArgumentException("Unmodifiable");
    }

    public List<Expr> toExprJList() {
        List<Expr> dval=new ArrayList<>(length());
        for (Expr value : this) {
            dval.add(value);
        }
        return dval;
    }

    public Expr[] toExprArray() {
        Expr[] all=new Expr[length()];
        for (int i = 0; i < all.length; i++) {
            all[i]=get(i);
        }
        return all;
    }

    public List<Complex> toComplexList() {
        List<Complex> dval=new ArrayList<>(length());
        for (Expr value : this) {
            dval.add(value.toComplex());
        }
        return dval;
    }

    public Complex[] toComplexArray() {
        Complex[] all=new Complex[length()];
        for (int i = 0; i < all.length; i++) {
            all[i]=get(i).toComplex();
        }
        return all;
    }

    public List<Double> toDoubleList() {
        List<Double> dval=new ArrayList<>(length());
        for (Expr value : this) {
            dval.add(value.toDouble());
        }
        return dval;
    }

    public double[] toDoubleArray() {
        double[] all=new double[length()];
        for (int i = 0; i < all.length; i++) {
            all[i]=get(i).toDouble();
        }
        return all;
    }

    @Override
    public ExprList copy() {
        try {
            return (ExprList) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException("Not Clonable");
        }
    }

    public Expr scalarProductAll(ExprList... other) {
        int max = size();
        for (ExprList v : other) {
            int size = v.size();
            if (size > max) {
                max = size;
            }
        }
        List<Expr> d = new ArrayList<>(max);
        for (int i = 0; i < max; i++) {
            List<Expr> el = new ArrayList<>(max);
            el.add(get(i));
            for (ExprList v : other) {
                el.add(v.get(i));
            }
            d.add(Maths.mul(el.toArray(new Expr[el.size()])));
        }
        return Maths.add(d.toArray(new Expr[d.size()]));
    }

}
