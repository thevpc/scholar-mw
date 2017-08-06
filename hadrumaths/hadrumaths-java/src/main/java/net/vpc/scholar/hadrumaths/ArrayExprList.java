package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by vpc on 5/7/14.
 */
public class ArrayExprList<T extends Expr> extends ArrayTList<T> {


    public ArrayExprList(Class<T> componentType,boolean row,int initialSize) {
        super(componentType,row,initialSize);
    }

    public ArrayExprList(Class<T> componentType,boolean row,Expr... elements) {
        this(componentType,row,elements.length);
        appendAll((Collection<? extends T>) Arrays.asList(elements));
    }

    public ArrayExprList(Class<T> componentType,boolean row,Complex... elements) {
        this(componentType,row,elements.length);
        appendAll((Collection<? extends T>) Arrays.asList(elements));
    }

    public ArrayExprList(Class<T> componentType,boolean row,double... elements) {
        this(componentType,row,elements.length);
        for (double element : elements) {
            append((T) Complex.valueOf(element));
        }
    }

    public ArrayExprList(Class<T> componentType,boolean row,Collection<? extends Expr> elements) {
        this(componentType,row,elements.size());
        appendAll((Collection<? extends T>) elements);
    }

    public <P extends Expr> ArrayExprList(Class<T> componentType,boolean row,TVector<P> elements) {
        this(componentType,row,elements.size());
        appendAll((Collection) elements);
    }

    public Expr[] toExprArray() {
        List<Expr> y = toExprJList();
        return y.toArray(new Expr[y.size()]);
    }

    public List<Complex> toComplexList() {
        if(!isComplex()){
            throw new ClassCastException();
        }
        List<Complex> y = new ArrayList<>();
        for (Expr expr : toExprJList()) {
            y.add(expr.toComplex());
        }
        return y;
    }

    public Complex[] toComplexArray() {
        List<Complex> complexes = toComplexList();
        return complexes.toArray(new Complex[complexes.size()]);
    }

    public List<Double> toDoubleList() {
        if(!isDouble()){
            throw new ClassCastException();
        }
        List<Double> y = new ArrayList<>();
        for (Expr expr : toExprJList()) {
            y.add(expr.toDouble());
        }
        return y;
    }

    private List<Expr> toExprJList() {
        List<Expr> e=new ArrayList<>();
        for (T t : this) {
            e.add(t);
        }
        return e;
    }

    public double[] toDoubleArray() {
        if(!isDouble()){
            throw new ClassCastException();
        }
        return ArrayUtils.exprListToDoubleArray((List<Expr>) toExprJList());
    }

    public TList<T> rewrite(ExpressionRewriter r) {
        TList<T> next = (TList<T>) Maths.exprList();
        for (Expr e : this) {
            next.append((T) r.rewriteOrSame(e));
        }
        return next;
    }



    public T sum() {
        return (T) (new Plus((TVector<Expr>) this));
    }

    public T prod() {
        return (T) new Mul((TVector<Expr>) this);
    }

}
