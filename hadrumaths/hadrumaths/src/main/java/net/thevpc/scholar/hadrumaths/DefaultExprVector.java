package net.thevpc.scholar.hadrumaths;

import net.thevpc.common.util.TypeName;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleParamValues;
import net.thevpc.scholar.hadrumaths.symbolic.Param;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.DoubleParam;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Mul;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Plus;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.thevpc.scholar.hadrumaths.util.ArrayUtils;
import net.thevpc.scholar.hadrumaths.util.InflatedExprTitleResolver;
import net.thevpc.scholar.hadrumaths.util.StringPatternInflatedExprTitleResolver;

import java.util.*;

/**
 * Created by vpc on 5/7/14.
 */
public class DefaultExprVector extends AbstractVector<Expr> implements ExprVector {
    private static final long serialVersionUID = 1L;
    private Vector<Expr> delegate;

    private DefaultExprVector(Vector<Expr> delegate) {
        super(delegate.isRow());
        this.delegate = delegate;
    }

    public static ExprVector of(boolean row, int initialSize) {
        return new DefaultExprVector(new ArrayVector<Expr>(Maths.$EXPR,row,initialSize));
    }

    public static ExprVector of(boolean row, Complex... elements) {
        return new DefaultExprVector(new ArrayVector<Expr>(Maths.$EXPR,row,elements));
    }

    public static ExprVector of(boolean row, Expr... elements) {
        return new DefaultExprVector(new ArrayVector<Expr>(Maths.$EXPR,row,elements));
    }

    public static ExprVector of(boolean row, double... elements) {
        return new DefaultExprVector(new ArrayVector<Expr>(Maths.$EXPR,row,ArrayUtils.toComplex(elements)));
    }

    public static ExprVector of(boolean row, Collection<? extends Expr> elements) {
        return new DefaultExprVector(new ArrayVector<Expr>(Maths.$EXPR,row,elements.toArray(new Expr[0])));
    }

    public static <P extends Expr>  ExprVector of(boolean row, Vector<P> elements) {
        return new DefaultExprVector(new ArrayVector<Expr>(Maths.$EXPR,row,elements.toArray()));
    }

    public static <P extends Expr>  ExprVector of(boolean row, VectorModel<P> elements) {
        int count = elements.size();
        DefaultExprVector d = new DefaultExprVector(new ArrayVector<Expr>(Maths.$EXPR, row, count));
        for (int i = 0; i < count; i++) {
            d.append(elements.get(i));
        }
        return d;
    }

    public static <P extends Expr> ExprVector of(boolean row, int count, VectorCell<P> elements) {
        DefaultExprVector d = new DefaultExprVector(new ArrayVector<Expr>(Maths.$EXPR, row, count));
        for (int i = 0; i < count; i++) {
            d.append(elements.get(i));
        }
        return d;
    }


    public static ExprVector Column() {
        return of(new ArrayVector<Expr>(Maths.$EXPR, false, 0));
    }

    public static ExprVector Column(int initialSize) {
        return of(new ArrayVector<Expr>(Maths.$EXPR, false, initialSize));
    }

    public static ExprVector Row() {
        return of(new ArrayVector<Expr>(Maths.$EXPR, true, 0));
    }

    public static ExprVector Row(int initialSize) {
        return of(new ArrayVector<Expr>(Maths.$EXPR, true, initialSize));
    }

    public static ExprVector of(Vector<Expr> other) {
        if (other instanceof ExprVector) {
            return (ExprVector) other;
        }
        return new DefaultExprVector(other);
    }

    public Complex[] toComplexArray() {
        List<Complex> complexes = toComplexList();
        return complexes.toArray(new Complex[0]);
    }

    public List<Complex> toComplexList() {
        if (!isComplex()) {
            throw new ClassCastException();
        }
        List<Complex> y = new ArrayList<>();
        for (Expr expr : toExprList()) {
            y.add(expr.toComplex());
        }
        return y;
    }

    private List<Expr> toExprList() {
        List<Expr> e = new ArrayList<>();
        for (Expr t : this) {
            e.add(t);
        }
        return e;
    }

    public List<Double> toDoubleList() {
        if (!isDouble()) {
            throw new ClassCastException();
        }
        List<Double> y = new ArrayList<>();
        for (Expr expr : toExprList()) {
            y.add(expr.toDouble());
        }
        return y;
    }

    public double[] toDoubleArray() {
        if (!isDouble()) {
            throw new ClassCastException();
        }
        return ArrayUtils.exprListToDoubleArray(toExprList());
    }

    public ExprVector rewrite(ExpressionRewriter r) {
        ExprVector next = Maths.evector();
        for (Expr e : this) {
            next.append(r.rewriteOrSame(e, null));
        }
        return next;
    }

    public ExprVector setParams(DoubleParamValues p) {
        return inflate(p);
    }

    public ExprVector inflate(DoubleParamValues d) {
        return inflate(d, (InflatedExprTitleResolver) null);
    }

    public ExprVector inflate(DoubleParamValues d, InflatedExprTitleResolver title) {
        if (title == null || title == StringPatternInflatedExprTitleResolver.DEFAULT) {
            double[][] values = d.getParamValues();
            ExprVector elist = isRow()?Row(size() * values.length):Column(size() * values.length);
            DoubleParam[] paramsAll = d.getParams();
            int pc = paramsAll.length;
            for (Expr item : this) {
                for (double[] value : values) {
                    Expr currItem = item;
                    for (int i = 0; i < pc; i++) {
                        currItem = currItem.setParam(paramsAll[i], value[i]);
                    }
                    elist.append(currItem);
                }
            }
            return elist;
        } else {
            double[][] values = d.getParamValues();
            ExprVector elist = isRow()?Row(size() * values.length):Column(size() * values.length);
            DoubleParam[] paramsAll = d.getParams();
            int pc = paramsAll.length;
            Map<String, Object> tileVars = new HashMap<>();
            int index = 0;
            for (Expr item : this) {
                for (double[] value : values) {
                    Expr currItem = item;
                    tileVars.clear();
                    for (int i = 0; i < pc; i++) {
                        currItem = currItem.setParam(paramsAll[i], value[i]);
                        if (Maths.isInt(value[i])) {
                            tileVars.put(paramsAll[i].getName(), String.valueOf((int) value[i]));
                        } else {
                            tileVars.put(paramsAll[i].getName(), String.valueOf(value[i]));
                        }
                    }
                    currItem = currItem.setTitle(title.resolveTitle(tileVars, index));
                    elist.append(currItem);
                }
                index++;
            }
            return elist;
        }
    }

    public ExprVector inflate(DoubleParamValues d, String title) {
        return inflate(d, title == null ? StringPatternInflatedExprTitleResolver.DEFAULT : new StringPatternInflatedExprTitleResolver(title));
    }

    @Override
    public ExprVector normalize() {
        return transform((index, e) -> e.normalize());
    }

    @Override
    public ExprVector simplify() {
        return transform((index, e) -> e.simplify());
    }

    @Override
    public ExprVector simplify(SimplifyOptions options) {
        return transform((index, e) -> e.simplify(options));
    }

    protected ExprVector wrap(Vector<Expr> other) {
        if (other == delegate) {
            return this;
        }
        return of(other);
    }

    @Override
    public ExprVector eval(VectorOp<Expr> op) {
        return wrap(super.eval(op));
    }

    @Override
    public ExprVector copy(CopyStrategy strategy) {
        return wrap(delegate.copy(strategy));
    }

    @Override
    public ExprVector copy() {
        return wrap(super.copy());
    }

    @Override
    public ExprVector setParam(Param paramExpr, Object value) {
        return wrap(super.setParam(paramExpr, value));
    }

    @Override
    public ExprVector setParam(String name, Object value) {
        return wrap(super.setParam(name, value));
    }

    ////////////////////////////////////////////////////////////////////////////////

    @Override
    public ExprVector transpose() {
        return wrap(super.transpose());
    }

    @Override
    public ExprVector scalarProduct(Expr other) {
        return wrap(super.scalarProduct(other));
    }

    @Override
    public ExprVector rscalarProduct(Expr other) {
        return wrap(super.rscalarProduct(other));
    }

    @Override
    public ExprVector vscalarProduct(Vector<Expr>... other) {
        return wrap(super.vscalarProduct(other));
    }

    @Override
    public ExprVector dotmul(Vector<Expr> other) {
        return wrap(super.dotmul(other));
    }

    @Override
    public ExprVector dotdiv(Vector<Expr> other) {
        return wrap(super.dotdiv(other));
    }

//    @Override
//    public ExprVector appendAt(int index, Expr e) {
//        return (ExprVector) super.append(index, e);
//    }
//
////    @Override
//    public ExprVector removeLast() {
//        return super.removeLast();
//    }
//
//    @Override
//    public ExprVector removeFirst() {
//        return super.removeFirst();
//    }
//
//    @Override
//    public ExprVector remove(Expr e) {
//        return super.remove(e);
//    }
//
//    @Override
//    public ExprVector map(VectorOp<Expr> op) {
//        return ensureExprVector(super.map(op));
//    }

    @Override
    public ExprVector dotpow(Vector<Expr> other) {
        return wrap(super.dotpow(other));
    }

    @Override
    public ExprVector add(Vector<Expr> other) {
        return wrap(super.add(other));
    }

    @Override
    public ExprVector sub(Vector<Expr> other) {
        return wrap(super.sub(other));
    }

    @Override
    public ExprVector add(Expr other) {
        return wrap(super.add(other));
    }

    @Override
    public ExprVector sub(Expr other) {
        return wrap(super.sub(other));
    }

    @Override
    public ExprVector mul(Expr other) {
        return wrap(super.mul(other));
    }

    @Override
    public ExprVector div(Expr other) {
        return wrap(super.div(other));
    }

    @Override
    public ExprVector rem(Expr other) {
        return wrap(super.rem(other));
    }

    @Override
    public ExprVector dotpow(Expr other) {
        return wrap(super.dotpow(other));
    }

    @Override
    public ExprVector conj() {
        return wrap(super.conj());
    }

    @Override
    public ExprVector cos() {
        return wrap(super.cos());
    }

    @Override
    public ExprVector cosh() {
        return wrap(super.cosh());
    }

    @Override
    public ExprVector sin() {
        return wrap(super.sin());
    }

    @Override
    public ExprVector sinh() {
        return wrap(super.sinh());
    }

    @Override
    public ExprVector tan() {
        return wrap(super.tan());
    }

    @Override
    public ExprVector tanh() {
        return wrap(super.tanh());
    }

    @Override
    public ExprVector cotan() {
        return wrap(super.cotan());
    }

    @Override
    public ExprVector cotanh() {
        return wrap(super.cotanh());
    }

    @Override
    public ExprVector getReal() {
        return wrap(super.getReal());
    }

    @Override
    public ExprVector real() {
        return wrap(super.real());
    }

    @Override
    public ExprVector getImag() {
        return wrap(super.getImag());
    }

    @Override
    public ExprVector imag() {
        return wrap(super.imag());
    }

    @Override
    public ExprVector db() {
        return wrap(super.db());
    }

    @Override
    public ExprVector db2() {
        return wrap(super.db2());
    }

    @Override
    public ExprVector abs() {
        return wrap(super.abs());
    }

    @Override
    public ExprVector abssqr() {
        return wrap(super.abssqr());
    }

    @Override
    public ExprVector log() {
        return wrap(super.log());
    }

    @Override
    public ExprVector log10() {
        return wrap(super.log10());
    }

    @Override
    public ExprVector exp() {
        return wrap(super.exp());
    }

    @Override
    public ExprVector sqrt() {
        return wrap(super.sqrt());
    }

    @Override
    public ExprVector sqr() {
        return wrap(super.sqr());
    }

    @Override
    public ExprVector acosh() {
        return wrap(super.acosh());
    }

    @Override
    public ExprVector acos() {
        return wrap(super.acos());
    }

    @Override
    public ExprVector asinh() {
        return wrap(super.asinh());
    }

    @Override
    public ExprVector asin() {
        return wrap(super.asin());
    }

    @Override
    public ExprVector atan() {
        return wrap(super.atan());
    }

    @Override
    public ExprVector acotan() {
        return wrap(super.acotan());
    }

    @Override
    public ExprVector filter(VectorFilter<Expr> filter) {
        return wrap(super.filter(filter));
    }

    @Override
    public ExprVector transform(VectorTransform<Expr, Expr> op) {
        return wrap(super.transform(op));
    }

    @Override
    public ExprVector removeFirst() {
        return wrap(delegate.removeFirst());
    }

    @Override
    public ExprVector removeLast() {
        return wrap(delegate.removeLast());
    }

    @Override
    public ExprVector sublist(int fromIndex, int toIndex) {
        return wrap(super.sublist(fromIndex, toIndex));
    }

    @Override
    public ExprVector sort() {
        return wrap(super.sort());
    }

    @Override
    public ExprVector removeDuplicates() {
        return wrap(super.removeDuplicates());
    }

    @Override
    public ExprVector concat(Expr e) {
        return wrap(super.concat(e));
    }

    @Override
    public ExprVector concat(Vector<Expr> e) {
        return wrap(delegate.concat(e));
    }

    @Override
    public ExprMatrix toMatrix() {
        if (isRow()) {
            return new ExprMatrixFromRowVector(this);
        }
        return new ExprMatrixFromColumnVector(this);
    }

    @Override
    public ExprVector set(int index, Expr e) {
        return (ExprVector) super.set(index, e);
    }

    @Override
    public ExprVector set(Enum i, Expr value) {
        return wrap(super.set(i, value));
    }

    @Override
    public ExprVector update(int i, Expr complex) {
        return wrap(super.update(i, complex));
    }

    @Override
    public ExprVector update(Enum i, Expr value) {
        return wrap(super.update(i, value));
    }

    @Override
    public ExprVector rem(Vector<Expr> other) {
        return wrap(super.rem(other));
    }

    @Override
    public ExprVector inv() {
        return wrap(super.inv());
    }

    public Expr sum() {
        return (Plus.of(this.toExprArray()));
    }

    public Expr prod() {
        return Mul.of(toExprArray());
    }

    @Override
    public ExprVector neg() {
        return wrap(super.neg());
    }

    @Override
    public ExprVector sincard() {
        return wrap(super.sincard());
    }

    @Override
    public ExprVector sqrt(int n) {
        return wrap(super.sqrt(n));
    }

    @Override
    public ExprVector pow(Vector<Expr> other) {
        return wrap(super.pow(other));
    }

    @Override
    public ExprVector pow(double n) {
        return wrap(super.pow(n));
    }

    @Override
    public ExprVector pow(Complex n) {
        return wrap(super.pow(n));
    }

    @Override
    public ExprVector arg() {
        return wrap(super.arg());
    }

    @Override
    public ExprVector mul(Vector<Expr> other) {
        return wrap(delegate.mul(other));
    }

    @Override
    protected ExprVector toVector(Expr expr) {
        return wrap(super.toVector(expr));
    }

    @Override
    protected ExprVector toVector(Expr expr, boolean row) {
        return wrap(super.toVector(expr, row));
    }

    @Override
    protected ExprVector newInstanceFromValues(boolean row, Expr[] all) {
        return wrap(super.newInstanceFromValues(row, all));
    }

    @Override
    protected ExprVector newInstanceFromValues(Expr[] all) {
        return wrap(super.newInstanceFromValues(all));
    }

    @Override
    public ExprVector appendAll(VectorModel<Expr> e) {
        return wrap(delegate.appendAll(e));
    }

    public Expr[] toExprArray() {
        List<Expr> y = toExprList();
        return y.toArray(new Expr[0]);
    }

    @Override
    public boolean isMutable() {
        return delegate.isMutable();
    }

    @Override
    public ExprVector toReadOnly() {
        if (isReadOnly()) {
            return this;
        }
        return of(delegate.toReadOnly());
    }

    @Override
    public ExprVector toMutable() {
        if (isMutable()) {
            return this;
        }
        return of(delegate.toMutable());
    }

    @Override
    public ExprVector removeAt(int index) {
        return of(delegate.removeAt(index));
    }

    @Override
    public ExprVector appendAt(int index, Expr e) {
        return of(delegate.appendAt(index, e));
    }

    //////
    @Override
    public ExprVector append(Expr e) {
        return wrap(delegate.append(e));
    }

    @Override
    public ExprVector appendAll(Collection<? extends Expr> e) {
        return wrap(delegate.appendAll(e));
    }

    @Override
    public ExprVector appendAll(Vector<Expr> e) {
        return wrap(delegate.appendAll(e));
    }

    @Override
    public TypeName<Expr> getComponentType() {
        return Maths.$EXPR;
    }

    @Override
    public Expr get(int i) {
        return delegate.get(i);
    }

    @Override
    public int size() {
        return delegate.size();
    }

}
