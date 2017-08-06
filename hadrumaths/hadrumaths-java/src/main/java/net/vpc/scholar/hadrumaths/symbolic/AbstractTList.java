package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

import java.util.Collection;

/**
 * Created by vpc on 2/14/15.
 */
public abstract class AbstractTList<T> extends AbstractTVector<T> implements TList<T> {
    private Class<T> componentType;

    public AbstractTList(Class<T> componentType) {
        super(false);
        this.componentType = componentType;
    }

    @Override
    public TList<T> eval(ElementOp<T> op) {
        return new UpdatableExprSequence<T>(getComponentType(),super.eval(op));
    }

    @Override
    public <R> TList<R> transform(Class<R> toType, TTransform<T, R> op) {
        return new UpdatableExprSequence<R>(toType,super.transform(toType,op));
    }

    @Override
    public void set(int index, T e) {
        throw new IllegalArgumentException("Unmodifiable");
    }

    @Override
    public void appendAll(TVector<T> e) {
        throw new IllegalArgumentException("Unmodifiable");
    }

    @Override
    public void append(T e) {
        throw new IllegalArgumentException("Unmodifiable");
    }

    @Override
    public void appendAll(Collection<? extends T> e) {
        throw new IllegalArgumentException("Unmodifiable");
    }

//    public List<T> toExprJList() {
//        List<T> dval = new ArrayList<>(length());
//        for (T value : this) {
//            dval.add(value);
//        }
//        return dval;
//    }
//
//    public Expr[] toExprArray() {
//        Expr[] all = new Expr[length()];
//        for (int i = 0; i < all.length; i++) {
//            all[i] = get(i);
//        }
//        return all;
//    }
//
//    public List<Complex> toComplexList() {
//        List<Complex> dval = new ArrayList<>(length());
//        for (Expr value : this) {
//            dval.add(value.toComplex());
//        }
//        return dval;
//    }
//
//    public Complex[] toComplexArray() {
//        Complex[] all = new Complex[length()];
//        for (int i = 0; i < all.length; i++) {
//            all[i] = get(i).toComplex();
//        }
//        return all;
//    }
//
//    public List<Double> toDoubleList() {
//        List<Double> dval = new ArrayList<>(length());
//        for (Expr value : this) {
//            dval.add(value.toDouble());
//        }
//        return dval;
//    }
//
//    public double[] toDoubleArray() {
//        double[] all = new double[length()];
//        for (int i = 0; i < all.length; i++) {
//            all[i] = get(i).toDouble();
//        }
//        return all;
//    }

    @Override
    public TList<T> copy() {
        try {
            return (TList<T>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException("Not Clonable");
        }
    }

    @Override
    public final int length() {
        return size();
    }

    @Override
    public Class<T> getComponentType() {
        return componentType;
    }

    @Override
    public TList<T> setParam(String name, Object value) {
        return Maths.list(super.setParam(name, value));
    }

    @Override
    public TList<T> setParam(TParam param, Object value) {
        return Maths.list(super.setParam(param, value));
    }
}
