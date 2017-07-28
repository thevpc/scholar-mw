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
public class ArrayExprList extends AbstractExprList implements ExprList {

    private ArrayList<Expr> values;

    public ArrayExprList() {
        values = new ArrayList<Expr>();
    }

    public ArrayExprList(int initialCapacity) {
        values = new ArrayList<Expr>(initialCapacity);
    }

    public ArrayExprList(Expr... elements) {
        values = new ArrayList<Expr>(Arrays.asList(elements));
    }

    public ArrayExprList(Complex... elements) {
        values = new ArrayList<Expr>(Arrays.asList(elements));
    }

    public ArrayExprList(double... elements) {
        values = new ArrayList<Expr>(elements.length);
        for (double element : elements) {
            values.add(Complex.valueOf(element));
        }
    }

    public ArrayExprList(Collection<? extends Expr> elements) {
        values = new ArrayList<Expr>(elements);
    }

    public ArrayExprList(ExprList elements) {
        values = new ArrayList<Expr>();
        values.addAll(elements.toList().values);
    }

    @Override
    public <T extends Expr> T[] toArray(T[] a) {
        return values.toArray(a);
    }

    public void add(Expr e) {
        values.add(e);
    }

    public void addAll(Collection<? extends Expr> e) {
        values.addAll(e);
    }

    public void addAll(ExprList e) {
        for (int i = 0; i < e.length(); i++) {
            values.add(e.get(i));
        }
    }



    public void add(int index, Expr e) {
        values.add(index, e);
    }

    @Override
    public void set(int index, Expr e) {
        values.set(index, e);
    }

    public ArrayExprList removeLast() {
        values.remove(values.size() - 1);
        return this;
    }

    public ArrayExprList removeFirst() {
        values.remove(0);
        return this;
    }

    public ArrayExprList remove(Expr e) {
        values.remove(e);
        return this;
    }

    public void remove(int index) {
        values.remove(index);
    }

    @Override
    public Expr get(int index) {
        return values.get(index);
    }

    public ArrayExprList simplify() {
        ExpressionRewriter o = Maths.Config.getComputationOptimizer();
        return rewrite(o);
    }

    public List<Expr> toExprJList() {
        return new ArrayList<>(values);
    }

    public Expr[] toExprArray() {
        return values.toArray(new Expr[values.size()]);
    }

    public List<Complex> toComplexList() {
        if(!isComplex()){
            throw new ClassCastException();
        }
        return new ArrayList(values);
    }

    public Complex[] toComplexArray() {
        Complex[] cc=new Complex[values.size()];
        for (int i = 0; i < cc.length; i++) {
            cc[i]=values.get(i).toComplex();
        }
        return cc;
    }

    public List<Double> toDoubleList() {
        if(!isDouble()){
            throw new ClassCastException();
        }
        List<Double> dval=new ArrayList<>(values.size());
        for (Expr value : values) {
            dval.add(value.toDouble());
        }
        return dval;
    }

    public double[] toDoubleArray() {
        if(!isDouble()){
            throw new ClassCastException();
        }
        return ArrayUtils.exprListToDoubleArray(values);
    }

    @Override
    public ArrayExprList toList() {
        return this;
    }

    public ArrayExprList rewrite(ExpressionRewriter r) {
        ArrayExprList next = new ArrayExprList(size());
        for (Expr e : this) {
            next.add(r.rewriteOrSame(e));
        }
        return next;
    }

    public int length() {
        return values.size();
    }

    public ArrayExprList map(ElementOp op) {
        return transform(op);
    }

    public ArrayExprList transform(ElementOp op) {
        ArrayExprList other = new ArrayExprList();
        int index = 0;
        for (Expr e : this) {
            other.add(op.eval(index, e));
            index++;
        }
        return other;
    }

    public ArrayExprList setParam(String name, double value) {
        ArrayExprList other = new ArrayExprList();
        for (Expr e : this) {
            other.add(e.setParam(name, value));
        }
        return other;
    }

    public ArrayExprList setParam(String name, Expr value) {
        ArrayExprList other = new ArrayExprList();
        for (Expr e : this) {
            other.add(e.setParam(name, value));
        }
        return other;
    }

    public ArrayExprList setParam(ParamExpr paramExpr, double value) {
        ArrayExprList other = new ArrayExprList();
        for (Expr e : this) {
            other.add(e.setParam(paramExpr, value));
        }
        return other;
    }

    public ArrayExprList setParam(ParamExpr paramExpr, Expr value) {
        ArrayExprList other = new ArrayExprList();
        for (Expr e : this) {
            other.add(e.setParam(paramExpr, value));
        }
        return other;
    }


    public ArrayExprList copy() {
        return new ArrayExprList(this);
    }

    public Expr sum() {
        return new Plus(this);
    }

    public Expr mul() {
        return new Mul(this);
    }

    @Override
    public String toString() {
        return values.toString();
    }
}
