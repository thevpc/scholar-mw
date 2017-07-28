package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

import java.util.Collection;
import java.util.List;

/**
 * Created by vpc on 5/30/14.
 */
public interface ExprList extends Iterable<Expr>, ExprSeqCellIterator {

    boolean isComplex();

    boolean isDouble();

    ArrayExprList toList();

    Expr[] toArray();

    <T extends Expr> T[] toArray(T[] a);

    int length();

    int size();

    Expr get(int index);

    /**
     * same as index, provided for scala compatibility
     *
     * @param index index
     * @return get(index)
     */
    Expr apply(int index);

    void set(int index, Expr e);

    /**
     * create a preloaded sequence (all element are pre evaluated)
     *
     * @return
     */
    ExprList preload();

    /**
     * create a cached instance of this expression sequence
     *
     * @return
     */
    ExprList withCache();

    /**
     * create a sequence of simplified elements
     *
     * @return
     */
    ExprList simplify();

    Expr scalarProduct(Matrix v);

    Expr scalarProduct(Vector v);

    Expr scalarProduct(ExprList v);

    Expr scalarProduct(Expr v);

    ExprList transform(ElementOp op);


    List<Expr> toExprJList();

    Expr[] toExprArray();

    List<Complex> toComplexList();

    Complex[] toComplexArray();

    List<Double> toDoubleList();

    double[] toDoubleArray();

    void addAll(ExprList e);

    void add(Expr e);

    void addAll(Collection<? extends Expr> e);

    ExprList copy();

    Expr scalarProductAll(ExprList... other);
}
