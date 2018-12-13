package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeReference;
import net.vpc.scholar.hadrumaths.symbolic.TParam;

import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * Created by vpc on 2/14/15.
 */
public abstract class AbstractTList<T> extends AbstractTVector<T> implements TList<T> {
    private static final long serialVersionUID = 1L;
    protected int modCount;

    public AbstractTList(boolean row) {
        super(row);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int i = 0;
            int expectedModCount = modCount;

            @Override
            public boolean hasNext() {
                return i < size();
            }

            @Override
            public T next() {
                checkForCoModification();
                T v = get(i);
                i++;
                return v;
            }

            final void checkForCoModification() {
                if (modCount != expectedModCount)
                    throw new ConcurrentModificationException();
            }

        };
    }


    @Override
    public TVector<T> concat(TVector<T> e) {
        ArrayTList<T> v=new ArrayTList<T>(getComponentType(),isRow(),size()+(e==null?0:e.size()));
        v.appendAll(this);
        if(e!=null) {
            v.appendAll(e);
        }
        return v;
    }


    @Override
    public TList<T> eval(ElementOp<T> op) {
        return newReadOnlyInstanceFromModel(getComponentType(), isRow(), new TVectorModel<T>() {
            @Override
            public T get(int index) {
                T t = AbstractTList.this.get(index);
                return op.eval(index, t);
            }

            @Override
            public int size() {
                return AbstractTList.this.size();
            }
        });
    }

    @Override
    public <R> TList<R> transform(TypeReference<R> toType, TTransform<T, R> op) {
        return newReadOnlyInstanceFromModel(
                toType, isRow(), new TVectorModel<R>() {
                    @Override
                    public R get(int index) {
                        T t = AbstractTList.this.get(index);
                        return op.transform(index, t);
                    }

                    @Override
                    public int size() {
                        return AbstractTList.this.size();
                    }
                }
        );
    }

    @Override
    public void set(int index, T e) {
        throw new IllegalArgumentException("Unmodifiable List : " + getClass().getName());
    }

    @Override
    public void appendAll(TVector<T> e) {
        throw new IllegalArgumentException("Unmodifiable List : " + getClass().getName());
    }

    @Override
    public void append(T e) {
        throw new IllegalArgumentException("Unmodifiable List : " + getClass().getName());
    }

    @Override
    public TList<T> transpose() {
        return new TTransposedList<>(this);
    }

    @Override
    public void appendAll(Collection<? extends T> e) {
        throw new IllegalArgumentException("Unmodifiable List : " + getClass().getName());
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
        return Maths.copyOf(this);
    }

    @Override
    public TList<T> setParam(String name, Object value) {
        return (TList<T>) (super.setParam(name, value));
    }

    @Override
    public TList<T> setParam(TParam param, Object value) {
        return (TList<T>) (super.setParam(param, value));
    }


    @Override
    public TList<T> db2() {
        return (TList<T>) super.db2();
    }

    @Override
    public TList<T> db() {
        return (TList<T>) super.db();
    }

    @Override
    public TList<T> sin() {
        return (TList<T>) super.sin();
    }

    @Override
    public <R> TList<R> to(TypeReference<R> other) {
        if (other.equals(getComponentType())) {
            return (TList<R>) this;
        }
        return newReadOnlyInstanceFromModel(
                other, isRow(), new TVectorModel<R>() {
                    @Override
                    public R get(int index) {
                        T t = AbstractTList.this.get(index);
                        return (R) t;
                    }

                    @Override
                    public int size() {
                        return AbstractTList.this.size();
                    }
                }
        );
    }

    @Override
    public TList<T> scalarProduct(T other) {
        return (TList<T>) super.scalarProduct(other);
    }

    @Override
    public TList<T> rscalarProduct(T other) {
        return (TList<T>) super.rscalarProduct(other);
    }

    @Override
    public TList<T> vscalarProduct(TVector<T>... other) {
        return (TList<T>) super.vscalarProduct(other);
    }

    @Override
    public TList<T> dotmul(TVector<T> other) {
        return (TList<T>) super.dotmul(other);
    }

    @Override
    public TList<T> sqr() {
        return (TList<T>) super.sqr();
    }

    @Override
    public TList<T> dotdiv(TVector<T> other) {
        return (TList<T>) super.dotdiv(other);
    }

    @Override
    public TList<T> dotpow(TVector<T> other) {
        return (TList<T>) super.dotpow(other);
    }

    @Override
    public TList<T> inv() {
        return (TList<T>) super.inv();
    }

    @Override
    public TList<T> add(TVector<T> other) {
        return (TList<T>) super.add(other);
    }

    @Override
    public TList<T> add(T other) {
        return (TList<T>) super.add(other);
    }

    @Override
    public TList<T> mul(T other) {
        return (TList<T>) super.mul(other);
    }

    @Override
    public TList<T> sub(T other) {
        return (TList<T>) super.sub(other);
    }

    @Override
    public TList<T> div(T other) {
        return (TList<T>) super.div(other);
    }

    @Override
    public TList<T> dotpow(T other) {
        return (TList<T>) super.dotpow(other);
    }

    @Override
    public TList<T> sub(TVector<T> other) {
        return (TList<T>) super.sub(other);
    }

    @Override
    public TList<T> conj() {
        return (TList<T>) super.conj();
    }

    @Override
    public TList<T> cos() {
        return (TList<T>) super.cos();
    }

    @Override
    public TList<T> cosh() {
        return (TList<T>) super.cosh();
    }

    @Override
    public TList<T> sinh() {
        return (TList<T>) super.sinh();
    }

    @Override
    public TList<T> tan() {
        return (TList<T>) super.tan();
    }

    @Override
    public TList<T> tanh() {
        return (TList<T>) super.tanh();
    }

    @Override
    public TList<T> cotan() {
        return (TList<T>) super.cotan();
    }

    @Override
    public TList<T> cotanh() {
        return (TList<T>) super.cotanh();
    }

    @Override
    public TList<T> getReal() {
        return (TList<T>) super.getReal();
    }

    @Override
    public TList<T> real() {
        return (TList<T>) super.real();
    }

    @Override
    public TList<T> getImag() {
        return (TList<T>) super.getImag();
    }

    @Override
    public TList<T> imag() {
        return (TList<T>) super.imag();
    }

    @Override
    public TList<T> abs() {
        return (TList<T>) super.abs();
    }

    @Override
    public TList<T> abssqr() {
        return (TList<T>) super.abssqr();
    }

    @Override
    public TList<T> log() {
        return (TList<T>) super.log();
    }

    @Override
    public TList<T> log10() {
        return (TList<T>) super.log10();
    }

    @Override
    public TList<T> exp() {
        return (TList<T>) super.exp();
    }

    @Override
    public TList<T> sqrt() {
        return (TList<T>) super.sqrt();
    }

    @Override
    public TList<T> acosh() {
        return (TList<T>) super.acosh();
    }

    @Override
    public TList<T> acos() {
        return (TList<T>) super.acos();
    }

    @Override
    public TList<T> asinh() {
        return (TList<T>) super.asinh();
    }

    @Override
    public TList<T> asin() {
        return (TList<T>) super.asin();
    }

    @Override
    public TList<T> atan() {
        return (TList<T>) super.atan();
    }

    @Override
    public TList<T> acotan() {
        return (TList<T>) super.acotan();
    }

    protected TList<T> newInstanceFromValues(boolean row, T[] all) {
        TList<T> ts = Maths.list(getComponentType(), row, all.length);
        ts.appendAll(Arrays.asList(all));
        return ts;
    }

    @Override
    protected <R> TList<R> newReadOnlyInstanceFromModel(TypeReference<R> type, boolean row, TVectorModel<R> model) {
        return Maths.listro(type, row, model);
    }

    @Override
    public TList<T> sublist(int fromIndex, int toIndex) {
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        int size = size();
        if (toIndex >= size) {
            toIndex = size - 1;
        }
        if (toIndex < fromIndex) {
            toIndex = fromIndex;
        }
        int finalToIndex = toIndex;
        int finalFromIndex = fromIndex;
        return newReadOnlyInstanceFromModel(getComponentType(), isRow(), new TVectorModel<T>() {
            @Override
            public int size() {
                return finalToIndex - finalFromIndex;
            }

            @Override
            public T get(int index) {
                return AbstractTList.this.get(finalFromIndex + index);
            }
        });
    }

    @Override
    public TVector<T> concat(T e) {
        ArrayTList<T> e1 = new ArrayTList<T>(getComponentType(),isRow(),1);
        e1.append(e);
        return concat(e1);
    }
}
