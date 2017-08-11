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
public class ExprArrayList extends ArrayTList<Expr> implements ExprList{


    public ExprArrayList(boolean row, int initialSize) {
        super(Maths.$EXPR,row,initialSize);
    }

    public ExprArrayList(boolean row, Expr... elements) {
        this(row,elements.length);
        appendAll(Arrays.asList(elements));
    }

    public ExprArrayList(boolean row, Complex... elements) {
        this(row,elements.length);
        appendAll(Arrays.asList(elements));
    }

    public ExprArrayList(boolean row, double... elements) {
        this(row,elements.length);
        for (double element : elements) {
            append(Complex.valueOf(element));
        }
    }

    public ExprArrayList(boolean row, Collection<? extends Expr> elements) {
        this(row,elements.size());
        appendAll(elements);
    }

    public <P extends Expr> ExprArrayList(boolean row, TVector<P> elements) {
        this(row,elements.size());
        appendAll(elements.toJList());
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
        for (Expr t : this) {
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

    public TList<Expr> rewrite(ExpressionRewriter r) {
        TList<Expr> next = (TList<Expr>) Maths.elist();
        for (Expr e : this) {
            next.append((Expr) r.rewriteOrSame(e));
        }
        return next;
    }



    public Expr sum() {
        return (new Plus(this));
    }

    public Expr prod() {
        return new Mul(this);
    }

}
