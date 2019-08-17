package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;
import net.vpc.scholar.hadrumaths.symbolic.TParam;
import net.vpc.scholar.hadrumaths.util.PlatformUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by vpc on 5/7/14.
 */
public class ArrayTList<T> extends AbstractTList<T> {

    private static final long serialVersionUID = 1L;

    private ArrayList<T> values;
    private TypeName<T> componentType;

    //    public ArrayTList(Class<T> componentType) {
//
//    }
    public ArrayTList(TypeName<T> componentType, boolean row, int initialSize) {
        super(row);
        this.componentType = componentType;
        values = new ArrayList<T>(initialSize);
    }

    public ArrayTList(TypeName<T> componentType, boolean row, T[] values) {
        this(componentType, row, values.length);
        appendAll(Arrays.asList(values));
    }
    public ArrayTList(TypeName<T> componentType, boolean row, TVectorModel<T> values) {
        this(componentType, row, values.size());
        int s=values.size();
        for (int i = 0; i < s; i++) {
            append(values.get(i));
        }
    }

    @Override
    public TypeName<T> getComponentType() {
        return componentType;
    }

    @Override
    public <P extends T> P[] toArray(P[] a) {
        return values.toArray(a);
    }

    public void append(T e) {
        values.add(e);
    }

    public void appendAll(Collection<? extends T> e) {
        values.addAll(e);
    }

    public void appendAll(TVector<T> e) {
        for (int i = 0; i < e.size(); i++) {
            values.add(e.get(i));
        }
    }

    @Override
    public TVector<T> concat(TVector<T> e) {
        ArrayTList<T> v=new ArrayTList<T>(componentType,
                isRow(),
                size()+(e==null?0:e.size())
        );
        v.appendAll(this);
        if(e!=null) {
            v.appendAll(e);
        }
        return v;
    }

    public void append(int index, T e) {
        values.add(index, e);
    }

    @Override
    public void set(int index, T e) {
        values.set(index, e);
    }

    public TList<T> removeLast() {
        values.remove(values.size() - 1);
        return this;
    }

    public TList<T> removeFirst() {
        values.remove(0);
        return this;
    }

    public TList<T> remove(T e) {
        values.remove(e);
        return this;
    }

    public void remove(int index) {
        values.remove(index);
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

    public TList<T> map(ElementOp<T> op) {
        return this.eval(op);
    }

    public TList<T> setParam(String name, Object value) {
        TList<T> other = Maths.list(getComponentType());
        VectorSpace<T> cs = getComponentVectorSpace();
        for (T e : this) {
            other.append((T) cs.setParam(e, name, value));
        }
        return other;
    }

    public TList<T> setParam(TParam paramExpr, Object value) {
        return setParam(paramExpr.getParamName(), value);
    }

    public TList<T> copy() {
        TList<T> other = Maths.list(getComponentType());
        other.appendAll(this);
        return other;
    }

    @Override
    public String toString() {
        return values.toString();
    }

    public void trimToSize() {
        values.trimToSize();
    }

    @Override
    public TList<T> sort() {
        Object[] vals = values.toArray();
        Arrays.sort(vals);
        return new ArrayTList(componentType, isRow(), vals);
    }

    @Override
    public TList<T> removeDuplicates() {
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
        return new ArrayTList(componentType, isRow(), vals3);
    }

}
