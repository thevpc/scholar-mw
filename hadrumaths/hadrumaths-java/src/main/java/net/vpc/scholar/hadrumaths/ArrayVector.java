package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;
import net.vpc.scholar.hadrumaths.symbolic.Param;
import net.vpc.scholar.hadrumaths.util.PlatformUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by vpc on 5/7/14.
 */
public class ArrayVector<T> extends AbstractVector<T> {

    private static final long serialVersionUID = 1L;

    private final ArrayList<T> values;
    private final TypeName<T> componentType;

    public ArrayVector(TypeName<T> componentType, boolean row, T[] values) {
        this(componentType, row, values.length);
        appendAll(Arrays.asList(values));
    }

    //    public ArrayTList(Class<T> componentType) {
//
//    }
    public ArrayVector(TypeName<T> componentType, boolean row, int initialSize) {
        super(row);
        this.componentType = componentType;
        if (componentType == null) {
            throw new NullPointerException("Null Component type");
        }
        values = new ArrayList<T>(initialSize);
    }

    public ArrayVector(TypeName<T> componentType, boolean row, VectorModel<T> values) {
        this(componentType, row, values.size());
        int s = values.size();
        for (int i = 0; i < s; i++) {
            append(values.get(i));
        }
    }

    @Override
    public <P extends T> P[] toArray(P[] a) {
        return values.toArray(a);
    }

    public Vector<T> setParam(String name, Object value) {
        Vector<T> other = Maths.vector(getComponentType());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (T e : this) {
            other.append(cs.setParam(e, name, value));
        }
        return other;
    }

    public Vector<T> setParam(Param paramExpr, Object value) {
        return setParam(paramExpr.getName(), value);
    }

    @Override
    public TypeName<T> getComponentType() {
        return componentType;
    }

    @Override
    public T get(int index) {
        return values.get(index);
    }

    //    public Expr[] toExprArray() {
//        return values.toArray(new Expr[values.size()]);
//    }
//
//    public List<Complex> toComplexList() {
//        if(!isComplex()){
//            throw new ClassCastException();
//        }
//        return new ArrayList(values);
//    }
//
//    public Complex[] toComplexArray() {
//        Complex[] cc=new Complex[values.size()];
//        for (int i = 0; i < cc.length; i++) {
//            cc[i]=values.get(i).toComplex();
//        }
//        return cc;
//    }
//
//    public List<Double> toDoubleList() {
//        if(!isDouble()){
//            throw new ClassCastException();
//        }
//        List<Double> dval=new ArrayList<>(values.size());
//        for (Expr value : values) {
//            dval.add(value.toDouble());
//        }
//        return dval;
//    }
//
//    public double[] toDoubleArray() {
//        if(!isDouble()){
//            throw new ClassCastException();
//        }
//        return ArrayUtils.exprListToDoubleArray((List<Expr>) values);
//    }
//
//    public TList<T> rewrite(ExpressionRewriter r) {
//        TList<T> next = newInstance();
//        for (Expr e : this) {
//            next.append((T) r.rewriteOrSame(e));
//        }
//        return next;
//    }
    public int size() {
        return values.size();
    }

    @Override
    public Vector<T> concat(Vector<T> e) {
        ArrayVector<T> v = new ArrayVector<T>(componentType,
                isRow(),
                size() + (e == null ? 0 : e.size())
        );
        v.appendAll(this);
        if (e != null) {
            v.appendAll(e);
        }
        return v;
    }

    @Override
    public Vector<T> set(int index, T e) {
        values.set(index, e);
        return this;
    }

    @Override
    public Vector<T> removeAt(int index) {
        values.remove(index);
        return this;
    }

    @Override
    public Vector<T> appendAt(int index, T e) {
        values.add(index,e);
        return this;
    }

    public Vector<T> appendAll(Vector<T> e) {
        for (int i = 0; i < e.size(); i++) {
            values.add(e.get(i));
        }
        return this;
    }

    public Vector<T> append(T e) {
        values.add(e);
        return this;
    }

    public Vector<T> appendAll(Collection<? extends T> e) {
        values.addAll(e);
        return this;
    }

    public Vector<T> copy() {
        Vector<T> other = Maths.vector(getComponentType());
        other.appendAll(this);
        return other;
    }

    @Override
    public Vector<T> sort() {
        Object[] vals = values.toArray();
        Arrays.sort(vals);
        return new ArrayVector(componentType, isRow(), vals);
    }

    @Override
    public Vector<T> removeDuplicates() {
        Object[] vals = values.toArray();
        Object[] vals2 = sort().toArray();
        Object[] vals3 = new Object[vals.length];
        int x = 0;
        for (int i = 0; i < vals2.length; i++) {
            Object val = vals2[i];
            if (x == 0 || !PlatformUtils.equals(vals3[x - 1], val)) {
                vals3[x] = val;
                x++;
            }
        }
        return new ArrayVector(componentType, isRow(), vals3);
    }

    public Vector<T> append(int index, T e) {
        values.add(index, e);
        return this;
    }

    public Vector<T> removeLast() {
        values.remove(values.size() - 1);
        return this;
    }

    public Vector<T> removeFirst() {
        values.remove(0);
        return this;
    }

    public Vector<T> remove(T e) {
        values.remove(e);
        return this;
    }

//    @Override
//    public String toString() {
//        return values.toString();
//    }

    public void remove(int index) {
        values.remove(index);
    }

    public Vector<T> map(VectorOp<T> op) {
        return this.eval(op);
    }

    public void trimToSize() {
        values.trimToSize();
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Vector<T> toReadOnly() {
        return new ReadOnlyVector<>(getComponentType(),isRow(),this);
    }

    @Override
    public Vector<T> toMutable() {
        return this;
    }
}
