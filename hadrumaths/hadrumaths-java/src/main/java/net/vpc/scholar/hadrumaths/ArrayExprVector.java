package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;
import net.vpc.scholar.hadrumaths.util.InflatedExprTitleResolver;
import net.vpc.scholar.hadrumaths.util.StringPatternInflatedExprTitleResolver;

import java.util.*;

/**
 * Created by vpc on 5/7/14.
 */
public class ArrayExprVector extends ArrayTVector<Expr> implements ExprVector {
    private static final long serialVersionUID = 1L;


    public ArrayExprVector(boolean row, int initialSize) {
        super(MathsBase.$EXPR, row, initialSize);
    }

    public ArrayExprVector(boolean row, Expr... elements) {
        this(row, elements.length);
        appendAll(Arrays.asList(elements));
    }

    public ArrayExprVector(boolean row, Complex... elements) {
        this(row, elements.length);
        appendAll(Arrays.asList(elements));
    }

    public ArrayExprVector(boolean row, double... elements) {
        this(row, elements.length);
        for (double element : elements) {
            append(Complex.valueOf(element));
        }
    }

    public ArrayExprVector(boolean row, Collection<? extends Expr> elements) {
        this(row, elements.size());
        appendAll(elements);
    }

    public <P extends Expr> ArrayExprVector(boolean row, TVector<P> elements) {
        this(row, elements.size());
        appendAll(elements.toJList());
    }

    public <P extends Expr> ArrayExprVector(boolean row, int count, TVectorCell<P> elements) {
        this(row, count);
        for (int i = 0; i < count; i++) {
            append(elements.get(i));
        }
    }

    public Expr[] toExprArray() {
        List<Expr> y = toExprJList();
        return y.toArray(new Expr[0]);
    }

    public List<Complex> toComplexList() {
        if (!isComplex()) {
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
        return complexes.toArray(new Complex[0]);
    }

    public List<Double> toDoubleList() {
        if (!isDouble()) {
            throw new ClassCastException();
        }
        List<Double> y = new ArrayList<>();
        for (Expr expr : toExprJList()) {
            y.add(expr.toDouble());
        }
        return y;
    }

    private List<Expr> toExprJList() {
        List<Expr> e = new ArrayList<>();
        for (Expr t : this) {
            e.add(t);
        }
        return e;
    }

    public double[] toDoubleArray() {
        if (!isDouble()) {
            throw new ClassCastException();
        }
        return ArrayUtils.exprListToDoubleArray((List<Expr>) toExprJList());
    }

    public TVector<Expr> rewrite(ExpressionRewriter r) {
        TVector<Expr> next = (TVector<Expr>) MathsBase.elist();
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

    @Override
    public ExprVector concat(TVector<Expr> e) {
        ArrayExprVector v = new ArrayExprVector(isRow(), size() + (e == null ? 0 : e.size()));
        v.appendAll(this);
        if (e != null) {
            v.appendAll(e);
        }
        return v;
    }

    public ExprVector setParams(DoubleParamValues p) {
        return inflate(p);
    }


    public ExprVector inflate(DoubleParamValues d, String title) {
        return inflate(d,title==null?StringPatternInflatedExprTitleResolver.DEFAULT:new StringPatternInflatedExprTitleResolver(title));
    }
    public ExprVector inflate(DoubleParamValues d) {
        return inflate(d,(InflatedExprTitleResolver)null);
    }

    public ExprVector inflate(DoubleParamValues d,InflatedExprTitleResolver title) {
        if(title==null ||title==StringPatternInflatedExprTitleResolver.DEFAULT){
            double[][] values = d.getParamValues();
            ExprVector elist = new ArrayExprVector(isRow(), size() * values.length);
            DoubleParam[] paramsAll = d.getParams();
            int pc = paramsAll.length;
            for (Expr item : this) {
                for (double[] value : values) {
                    Expr currItem=item;
                    for (int i = 0; i < pc; i++) {
                        currItem = currItem.setParam(paramsAll[i], value[i]);
                    }
                    elist.append(currItem);
                }
            }
            return elist;
        }else{
            double[][] values = d.getParamValues();
            ExprVector elist = new ArrayExprVector(isRow(), size() * values.length);
            DoubleParam[] paramsAll = d.getParams();
            int pc = paramsAll.length;
            Map<String,Object> tileVars=new HashMap<>();
            int index=0;
            for (Expr item : this) {
                for (double[] value : values) {
                    Expr currItem=item;
                    tileVars.clear();
                    for (int i = 0; i < pc; i++) {
                        currItem = currItem.setParam(paramsAll[i], value[i]);
                        if(MathsBase.isInt(value[i])){
                            tileVars.put(paramsAll[i].getParamName(),String.valueOf((int)value[i]));
                        }else{
                            tileVars.put(paramsAll[i].getParamName(),String.valueOf(value[i]));
                        }
                    }
                    currItem=currItem.setTitle(title.resolveTitle(tileVars,index));
                    elist.append(currItem);
                }
                index++;
            }
            return elist;
        }
    }

    @Override
    public ExprVector transform(TTransform<Expr, Expr> op) {
        return ensureExprList(super.transform(op));
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

    ////////////////////////////////////////////////////////////////////////////////

    protected ExprVector ensureExprList(TVector<Expr> other) {
        if (other instanceof ExprVector) {
            return (ExprVector) other;
        }
        return new ArrayExprVector(other.isRow(), other);
    }

    @Override
    public ExprVector map(ElementOp<Expr> op) {
        return ensureExprList(super.map(op));
    }

    @Override
    public ExprVector setParam(String name, Object value) {
        return ensureExprList(super.setParam(name, value));
    }

    @Override
    public ExprVector setParam(TParam paramExpr, Object value) {
        return ensureExprList(super.setParam(paramExpr, value));
    }

    @Override
    public ExprVector copy() {
        return ensureExprList(super.copy());
    }

    @Override
    public ExprVector removeDuplicates() {
        return ensureExprList(super.removeDuplicates());
    }

    @Override
    public ExprVector eval(ElementOp<Expr> op) {
        return ensureExprList(super.eval(op));
    }

    @Override
    public ExprVector transpose() {
        return ensureExprList(super.transpose());
    }

    @Override
    public ExprVector db2() {
        return ensureExprList(super.db2());
    }

    @Override
    public ExprVector db() {
        return ensureExprList(super.db());
    }

    @Override
    public ExprVector sin() {
        return ensureExprList(super.sin());
    }

    @Override
    public ExprVector scalarProduct(Expr other) {
        return ensureExprList(super.scalarProduct(other));
    }

    @Override
    public ExprVector rscalarProduct(Expr other) {
        return ensureExprList(super.rscalarProduct(other));
    }

    @Override
    public ExprVector vscalarProduct(TVector<Expr>... other) {
        return ensureExprList(super.vscalarProduct(other));
    }

    @Override
    public ExprVector dotmul(TVector<Expr> other) {
        return ensureExprList(super.dotmul(other));
    }

    @Override
    public ExprVector sqr() {
        return ensureExprList(super.sqr());
    }

    @Override
    public ExprVector dotdiv(TVector<Expr> other) {
        return ensureExprList(super.dotdiv(other));
    }

    @Override
    public ExprVector dotpow(TVector<Expr> other) {
        return ensureExprList(super.dotpow(other));
    }

    @Override
    public ExprVector inv() {
        return ensureExprList(super.inv());
    }

    @Override
    public ExprVector add(TVector<Expr> other) {
        return ensureExprList(super.add(other));
    }

    @Override
    public ExprVector add(Expr other) {
        return ensureExprList(super.add(other));
    }

    @Override
    public ExprVector mul(Expr other) {
        return ensureExprList(super.mul(other));
    }

    @Override
    public ExprVector sub(Expr other) {
        return ensureExprList(super.sub(other));
    }

    @Override
    public ExprVector div(Expr other) {
        return ensureExprList(super.div(other));
    }

    @Override
    public ExprVector rem(Expr other) {
        return ensureExprList(super.rem(other));
    }

    @Override
    public ExprVector dotpow(Expr other) {
        return ensureExprList(super.dotpow(other));
    }

    @Override
    public ExprVector sub(TVector<Expr> other) {
        return ensureExprList(super.sub(other));
    }

    @Override
    public ExprVector conj() {
        return ensureExprList(super.conj());
    }

    @Override
    public ExprVector cos() {
        return ensureExprList(super.cos());
    }

    @Override
    public ExprVector cosh() {
        return ensureExprList(super.cosh());
    }

    @Override
    public ExprVector sinh() {
        return ensureExprList(super.sinh());
    }

    @Override
    public ExprVector tan() {
        return ensureExprList(super.tan());
    }

    @Override
    public ExprVector tanh() {
        return ensureExprList(super.tanh());
    }

    @Override
    public ExprVector cotan() {
        return ensureExprList(super.cotan());
    }

    @Override
    public ExprVector cotanh() {
        return ensureExprList(super.cotanh());
    }

    @Override
    public ExprVector getReal() {
        return ensureExprList(super.getReal());
    }

    @Override
    public ExprVector real() {
        return ensureExprList(super.real());
    }

    @Override
    public ExprVector getImag() {
        return ensureExprList(super.getImag());
    }

    @Override
    public ExprVector imag() {
        return ensureExprList(super.imag());
    }

    @Override
    public ExprVector abs() {
        return ensureExprList(super.abs());
    }

    @Override
    public ExprVector abssqr() {
        return ensureExprList(super.abssqr());
    }

    @Override
    public ExprVector log() {
        return ensureExprList(super.log());
    }

    @Override
    public ExprVector log10() {
        return ensureExprList(super.log10());
    }

    @Override
    public ExprVector exp() {
        return ensureExprList(super.exp());
    }

    @Override
    public ExprVector sqrt() {
        return ensureExprList(super.sqrt());
    }

    @Override
    public ExprVector acosh() {
        return ensureExprList(super.acosh());
    }

    @Override
    public ExprVector acos() {
        return ensureExprList(super.acos());
    }

    @Override
    public ExprVector asinh() {
        return ensureExprList(super.asinh());
    }

    @Override
    public ExprVector asin() {
        return ensureExprList(super.asin());
    }

    @Override
    public ExprVector atan() {
        return ensureExprList(super.atan());
    }

    @Override
    public ExprVector acotan() {
        return ensureExprList(super.acotan());
    }

    @Override
    public ExprVector sublist(int fromIndex, int toIndex) {
        return ensureExprList(super.sublist(fromIndex, toIndex));
    }

    @Override
    public ExprVector concat(Expr e) {
        return ensureExprList(super.concat(e));
    }

    @Override
    public ExprVector rem(TVector<Expr> other) {
        return ensureExprList(super.rem(other));
    }

    @Override
    public ExprVector pow(TVector<Expr> other) {
        return ensureExprList(super.pow(other));
    }

    @Override
    public ExprVector sqrt(int n) {
        return ensureExprList(super.sqrt(n));
    }

    @Override
    public ExprVector pow(double n) {
        return ensureExprList(super.pow(n));
    }

    @Override
    public ExprVector neg() {
        return ensureExprList(super.neg());
    }

    @Override
    public ExprVector arg() {
        return ensureExprList(super.arg());
    }

    @Override
    public ExprVector sincard() {
        return ensureExprList(super.sincard());
    }

    @Override
    public ExprVector sort() {
        return ensureExprList(super.sort());
    }

    //////
    @Override
    public ExprVector append(Expr e) {
        return (ExprVector) super.append(e);
    }

    @Override
    public ExprVector appendAll(Collection<? extends Expr> e) {
        return (ExprVector) super.appendAll(e);
    }

    @Override
    public ExprVector appendAll(TVector<Expr> e) {
        return (ExprVector) super.appendAll(e);
    }

    @Override
    public ExprVector append(int index, Expr e) {
        return (ExprVector) super.append(index, e);
    }

    @Override
    public ExprVector set(int index, Expr e) {
        return (ExprVector) super.set(index, e);
    }

    @Override
    public TVector<Expr> removeLast() {
        return super.removeLast();
    }

    @Override
    public TVector<Expr> removeFirst() {
        return super.removeFirst();
    }

    @Override
    public TVector<Expr> remove(Expr e) {
        return super.remove(e);
    }


    @Override
    public ExprMatrix toMatrix() {
        if (isRow()) {
            return new ExprMatrixFromRowVector(this);
        }
        return new ExprMatrixFromColumnVector(this);
    }

    @Override
    public ExprVector update(int i, Expr complex) {
        return ensureExprList(super.update(i, complex));
    }

    @Override
    public ExprVector set(Enum i, Expr value) {
        return ensureExprList(super.set(i, value));
    }

    @Override
    public ExprVector update(Enum i, Expr value) {
        return ensureExprList(super.update(i, value));
    }

    @Override
    public ExprVector filter(TFilter<Expr> filter) {
        return ensureExprList(super.filter(filter));
    }

    @Override
    protected TVector<Expr> newInstanceFromValues(boolean row, Expr[] all) {
        return super.newInstanceFromValues(row, all);
    }
}
