package net.thevpc.scholar.hadrumaths;

import net.thevpc.common.util.TypeName;
import net.thevpc.scholar.hadrumaths.symbolic.Param;
import net.thevpc.scholar.hadrumaths.util.ArrayUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by vpc on 4/11/16.
 */
public class DefaultComplexVector extends AbstractVector<Complex> implements ComplexVector {
    private static final long serialVersionUID = 1L;
    protected Vector<Complex> delegate;

    public static ComplexVector of(Vector<Complex> o){
        if(o instanceof ComplexVector){
            return (ComplexVector) o;
        }
        return new DefaultComplexVector(o);
    }

    private DefaultComplexVector(Vector<Complex> delegate) {
        super(delegate.isRow());
        this.delegate = delegate;
    }

    public DefaultComplexVector(File file) throws IOException {
        this(new ArrayVector<Complex>(Maths.$COMPLEX, false, 0));
        read(file);
    }

    public DefaultComplexVector(boolean row, Complex[] elementData) {
        this(new ArrayVector<Complex>(Maths.$COMPLEX, row, elementData.length));
        appendAll(Arrays.asList(elementData));
    }

    public DefaultComplexVector(boolean row, VectorModel<Complex> model) {
        this(new ArrayVector<Complex>(Maths.$COMPLEX, row, model.size()));
        appendAll(model);
    }

    public static final ComplexVector Row(Complex[] elements) {
        return new DefaultComplexVector(true, elements);
    }

    public static final ComplexVector Column(VectorModel<Complex> elements) {
        return Column(elements.size(), elements);
    }

    public static final ComplexVector Column(int rows, VectorCell<Complex> elements) {
        Complex[] arr = new Complex[rows];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = elements.get(i);
        }
        return new DefaultComplexVector(false, arr);
    }

    public static final ComplexVector Row(VectorModel<Complex> elements) {
        return Row(elements.size(), elements);
    }

    public static final ComplexVector Row(int rows, VectorCell<Complex> elements) {
        Complex[] arr = new Complex[rows];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = elements.get(i);
        }
        return new DefaultComplexVector(true, arr);
    }

    public static final ComplexVector Column() {
        return new DefaultComplexVector(false, new Complex[0]);
    }

    public static final ComplexVector Row() {
        return new DefaultComplexVector(true, new Complex[0]);
    }

    public static final ComplexVector Column(Complex[] elements) {
        return new DefaultComplexVector(false, elements);
    }


    @Override
    public boolean isMutable() {
        return delegate.isMutable();
    }

    @Override
    public ComplexVector toReadOnly() {
        if(delegate.isReadOnly()){
            return this;
        }
        return new DefaultComplexVector(delegate.toReadOnly());
    }

    @Override
    public ComplexVector toMutable() {
        if(delegate.isMutable()){
            return this;
        }
        return new DefaultComplexVector(delegate.toMutable());
    }

    @Override
    public ComplexVector removeAt(int index) {
        return wrap(delegate.removeAt(index));
    }

    private ComplexVector wrap(Vector<Complex> other) {
        if(other==delegate){
            return this;
        }
        return of(other);
    }

    @Override
    public ComplexVector appendAt(int index, Complex e) {
        return wrap(delegate.appendAt(index, e));
    }

    @Override
    public ComplexVector append(Complex e) {
        return wrap(delegate.append(e));
    }

    @Override
    public ComplexVector appendAll(Collection<? extends Complex> e) {
        return wrap(delegate.appendAll(e));
    }

    @Override
    public ComplexVector appendAll(Vector<Complex> e) {
        return wrap(delegate.appendAll(e));
    }

    @Override
    public TypeName<Complex> getComponentType() {
        return Maths.$COMPLEX;
    }

    @Override
    public Complex get(int i) {
        return delegate.get(i);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public double[] toDoubleArray() {
        double[] all = new double[size()];
        int size = size();
        for (int i = 0; i < size; i++) {
            all[i] = get(i).toDouble();
        }
        return all;
    }

    @Override
    public ComplexVector scalarProductToVector(ComplexVector... other) {
        return Maths.columnVector(other.length, new VectorCell<Complex>() {
            @Override
            public Complex get(int index) {
                return scalarProduct(other[index]);
            }
        });
    }

    public double[] dabs() {
        double[] all = new double[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = (get(i).absdbl());
        }
        return all;
    }

    public double[] dabsSqr() {
        double[] all = new double[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = (get(i).absdblsqr());
        }
        return (all);
    }

    @Override
    public ComplexVector eval(VectorOp<Complex> op) {
        return of(new ReadOnlyVector<Complex>(
                Maths.$COMPLEX,isRow(),new VectorModel<Complex>() {
            @Override
            public Complex get(int index) {
                return op.eval(index, DefaultComplexVector.this.get(index));
            }

            @Override
            public int size() {
                return DefaultComplexVector.this.size();
            }
        }
        ));
    }

    public <R> Vector<R> transform(TypeName<R> toType, VectorTransform<Complex, R> op) {
        return new ReadOnlyVector<R>(
                toType, isRow(), new VectorModel<R>() {
            @Override
            public R get(int index) {
                Complex t = DefaultComplexVector.this.get(index);
                return op.transform(index, t);
            }

            @Override
            public int size() {
                return DefaultComplexVector.this.size();
            }
        }
        );
    }

    public ComplexVector setParam(Param param, Object value) {
        return eval(new VectorOp<Complex>() {
            @Override
            public Complex eval(int index, Complex e) {
                VectorSpace<Complex> cs = getComponentVectorSpace();
                return cs.setParam(e, param.getName(), value);
            }
        });
    }

    public ComplexVector setParam(String name, Object value) {
        return eval(new VectorOp<Complex>() {
            @Override
            public Complex eval(int index, Complex e) {
                VectorSpace<Complex> cs = getComponentVectorSpace();
                return cs.setParam(e, name, value);
            }
        });
    }

    @Override
    public ComplexVector transpose() {
        return new DefaultComplexVector(delegate.transpose());
    }

    @Override
    public ComplexVector scalarProduct(Complex other) {
        if (isRow()) {
            return Maths.rowVector(size(),
                    new VectorCell<Complex>() {
                        @Override
                        public Complex get(int index) {
                            return getComponentVectorSpace().scalarProduct(get(index), other);
                        }
                    });
        }
        return Maths.columnVector(size(),
                new VectorCell<Complex>() {
                    @Override
                    public Complex get(int index) {
                        return getComponentVectorSpace().scalarProduct(get(index), other);
                    }
                });
    }

    @Override
    public ComplexVector rscalarProduct(Complex other) {
        if (isRow()) {
            return Maths.rowVector(size(),
                    new VectorCell<Complex>() {
                        @Override
                        public Complex get(int index) {
                            return getComponentVectorSpace().scalarProduct(other, get(index));
                        }
                    });
        }
        return Maths.columnVector(size(),
                new VectorCell<Complex>() {
                    @Override
                    public Complex get(int index) {
                        return getComponentVectorSpace().scalarProduct(other, get(index));
                    }
                });
    }

    @Override
    public ComplexVector vscalarProduct(Vector<Complex>... other) {
        return Maths.columnVector(other.length, new VectorCell<Complex>() {
            @Override
            public Complex get(int index) {
                return scalarProduct(other[index]);
            }
        });
    }

    @Override
    public ComplexVector dotmul(Vector<Complex> other) {
        Complex[] all = new Complex[Math.max(size(), other.size())];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).mul(other.get(i));
        }
        return new DefaultComplexVector(isRow(), all);
    }

    @Override
    public ComplexVector dotdiv(Vector<Complex> other) {
        Complex[] all = new Complex[Math.max(size(), other.size())];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).div(other.get(i));
        }
        return new DefaultComplexVector(isRow(), all);
    }

    @Override
    public ComplexVector dotpow(Vector<Complex> other) {
        Complex[] all = new Complex[Math.max(size(), other.size())];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).pow(other.get(i));
        }
        return new DefaultComplexVector(isRow(), all);
    }

    @Override
    public ComplexVector add(Vector<Complex> other) {
        Complex[] all = new Complex[Math.max(size(), other.size())];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).plus(other.get(i));
        }
        return new DefaultComplexVector(isRow(), all);
    }

    @Override
    public ComplexVector sub(Vector<Complex> other) {
        Complex[] all = new Complex[Math.max(size(), other.size())];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).minus(other.get(i));
        }
        return new DefaultComplexVector(isRow(), all);
    }

    @Override
    public ComplexVector add(Complex other) {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).plus(other);
        }
        return new DefaultComplexVector(isRow(), all);
    }

    @Override
    public ComplexVector sub(Complex other) {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).minus(other);
        }
        return new DefaultComplexVector(isRow(), all);
    }

    @Override
    public ComplexVector mul(Complex other) {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).mul(other);
        }
        return new DefaultComplexVector(isRow(), all);
    }

    @Override
    public ComplexVector div(Complex other) {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).div(other);
        }
        return new DefaultComplexVector(isRow(), all);
    }

    @Override
    public ComplexVector rem(Complex other) {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).rem(other);
        }
        return new DefaultComplexVector(isRow(), all);
    }

    @Override
    public ComplexVector dotpow(Complex other) {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).pow(other);
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector conj() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).conj();
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector cos() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).cos();
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector cosh() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).cosh();
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector sin() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).sin();
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector sinh() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).sinh();
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector tan() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).tan();
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector tanh() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).tanh();
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector cotan() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).cotan();
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector cotanh() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).cotanh();
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector getReal() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = Complex.of(get(i).getReal());
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector real() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = Complex.of(get(i).getReal());
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector getImag() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = Complex.of(get(i).getImag());
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector imag() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = Complex.of(get(i).getImag());
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector db() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = (get(i).db());
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector db2() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = (get(i).db2());
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector abs() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = Complex.of(get(i).absdbl());
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector abssqr() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = Complex.of(get(i).absdblsqr());
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector log() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).log();
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector log10() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).log10();
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector exp() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).exp();
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector sqrt() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).sqrt();
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector sqr() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).sqr();
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector acosh() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).acosh();
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector acos() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).acos();
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector asinh() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).asinh();
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector asin() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).asin();
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector atan() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).atan();
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector acotan() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).acotan();
        }
        return new DefaultComplexVector(isRow(), all);
    }

    @Override
    public VectorSpace<Complex> getComponentVectorSpace() {
        return Maths.COMPLEX_VECTOR_SPACE;
    }

    public boolean isComplex() {
        return size() == 1;
    }

    public Complex toComplex() {
        if (isComplex()) {
            return get(0);
        }
        throw new ClassCastException();
    }

    @Override
    public ComplexMatrix toMatrix() {
        if(delegate instanceof ArrayVector){
            if (rowType) {
                return Maths.rowMatrix(toArray());
            } else {
                return Maths.columnMatrix(toArray());
            }
        }
        if (isRow()) {
            return new ComplexMatrixFromRowVector(this);
        }
        return new ComplexMatrixFromColumnVector(this);
    }

    @Override
    public ComplexVector set(int i, Complex complex) {
        delegate.set(i, complex);
        return this;
    }

    @Override
    public ComplexVector set(Enum i, Complex value) {
        delegate.set(i, value);
        return this;
    }

    @Override
    public ComplexVector update(int i, Complex complex) {
        delegate.update(i, complex);
        return this;
    }

    @Override
    public Complex[] toArray() {
        Complex[] all = new Complex[size()];
        int size = size();
        for (int i = 0; i < size; i++) {
            all[i] = get(i);
        }
        return all;
    }

    @Override
    public <P extends Complex> P[] toArray(P[] a) {
        int size = size();
        if (a.length < size) {
            a = ArrayUtils.newArray(a.getClass().getComponentType(), size);
        }
        for (int i = 0; i < size; i++) {
            a[i] = (P) get(i);
        }
        return a;
    }

    @Override
    public void store(String file) {
        toMatrix().store(file);
    }

    public void store(File file) {
        toMatrix().store(file);
    }

    public void store(PrintStream stream) {
        toMatrix().store(stream);
    }

    public void store(PrintStream stream, String commentsChar, String varName) {
        toMatrix().store(stream, commentsChar, varName);
    }

    @Override
    public Complex scalarProduct(Matrix<Complex> v) {
        return scalarProduct(v.toVector());
    }

    public Complex scalarProduct(Vector<Complex> other) {
        int max = Math.max(size(), other.size());
        MutableComplex d = new MutableComplex();
        for (int i = 0; i < max; i++) {
            d.add(get(i).mul(other.get(i)));
        }
        return d.toComplex();
    }

    public Complex scalarProductAll(Vector<Complex>... other) {
        int currSize = size();
        for (Vector<Complex> v : other) {
            int size = v.size();
            if (size > currSize) {
                currSize = size;
            }
            if (size != currSize) {
                throw new IllegalArgumentException("Unable to scalar product vectors of distinct sizes " + currSize + "<>" + size);
            }
            if (!acceptsType(v.getComponentType())) {
                throw new IllegalArgumentException("Unexpected Type " + getComponentType() + "<>" + v.getComponentType());
            }
        }
        MutableComplex d = new MutableComplex();
        for (int i = 0; i < currSize; i++) {
            MutableComplex el = new MutableComplex(get(i));
            for (Vector<Complex> v : other) {
                el.mul(v.get(i));
            }
            d.add(el);
        }
        return d.toComplex();
    }

    @Override
    public <R> boolean isConvertibleTo(TypeName<R> other) {
        if (
                Maths.$COMPLEX.equals(other)
                        || Maths.$EXPR.equals(other)
        ) {
            return true;
        }
        if (other.isAssignableFrom(getComponentType())) {
            return true;
        }
        VectorSpace<Complex> vs = Maths.getVectorSpace(getComponentType());
        for (Complex t : this) {
            if (!vs.is(t, other)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ComplexVector inv() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).inv();
        }
        return new DefaultComplexVector(isRow(), all);
    }

    @Override
    public Complex sum() {
        int size = size();
        if (size == 0) {
            return Complex.ZERO;
        }
        MutableComplex c = new MutableComplex(get(0));
        for (int i = 1; i < size; i++) {
            c.add(get(i));
        }
        return c.toComplex();
    }

    @Override
    public Complex prod() {
        int size = size();
        if (size == 0) {
            return Complex.ONE;
        }
        MutableComplex c = new MutableComplex(get(0));
        for (int i = 1; i < size; i++) {
            c.mul(get(i));
        }
        return c.toComplex();
    }

    @Override
    public Complex avg() {
        int size = size();
        if (size == 0) {
            return Complex.ZERO;
        }
        MutableComplex c = MutableComplex.of(get(0));
        for (int i = 1; i < size; i++) {
            c.add(get(i));
        }
        c.div(size);
        return c.toComplex();
    }

    @Override
    public double maxAbs() {
        int size = size();
        if (size == 0) {
            return Double.NaN;
        }
        double c = get(0).absdbl();
        for (int i = 1; i < size; i++) {
            double d = get(i).absdbl();
            c = Math.max(c, d);
        }
        return c;
    }

    @Override
    public double minAbs() {
        int size = size();
        if (size == 0) {
            return Double.NaN;
        }
        double c = get(0).absdbl();
        for (int i = 1; i < size; i++) {
            double d = get(i).absdbl();
            c = Math.min(c, d);
        }
        return c;
    }

    public ComplexVector neg() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).neg();
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector sincard() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).sincard();
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector sqrt(int n) {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).sqrt(n);
        }
        return new DefaultComplexVector(isRow(), all);
    }

    @Override
    public ComplexVector pow(Vector<Complex> other) {
        Complex n = other.toComplex();
        Complex[] all = new Complex[size()];
        VectorSpace<Complex> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.pow(get(i), n);
        }
        return new DefaultComplexVector(isRow(), all);
    }

    public ComplexVector arg() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).arg();
        }
        return new DefaultComplexVector(isRow(), all);
    }

    @Override
    public boolean isDouble() {
        return isComplex() && toComplex().isReal();
    }

    @Override
    public ComplexVector mul(Vector<Complex> other) {
        Vector<Complex> mul = super.mul(other);
        return wrap(mul);
    }

    @Override
    public double getDistance(Normalizable other) {
        if (other instanceof ComplexVector) {
            return this.sub((ComplexVector) other).norm() / other.norm();
        } else {
            Normalizable o = other;
            return (this.norm() - o.norm()) / o.norm();
        }
    }

    @Override
    public double norm() {
        MutableComplex d = MutableComplex.Zero();
        for (int i = 0; i < size(); i++) {
            Complex c = get(i);
            d.add(c.absdblsqr());
        }
        return d.sqrtDouble();
    }


    public Complex scalarProduct(ComplexVector other) {
        int max = Math.max(size(), other.size());
        MutableComplex d = new MutableComplex();
        for (int i = 0; i < max; i++) {
            d.add(get(i).mul(other.get(i)));
        }
        return d.toComplex();
    }

    public Complex scalarProductAll(ComplexVector... other) {
        int max = size();
        for (ComplexVector v : other) {
            int size = v.size();
            if (size > max) {
                max = size;
            }
        }
        MutableComplex d = new MutableComplex();
        for (int i = 0; i < max; i++) {
            MutableComplex c = new MutableComplex(1, 0);
            c.mul(get(i));
            for (ComplexVector v : other) {
                c.mul(v.get(i));
            }
            d.add(c.toComplex());
        }
        return d.toComplex();
    }


    public void read(File file) throws IOException {
        ComplexMatrix m = Maths.loadMatrix(file);

        Complex[] elementData = m.getColumnCount() == 0 ? m.getRow(0).toArray() : m.getColumn(0).toArray();
        if (elementData.length > 0) {
            if (size() == 0) {
                rowType = m.getColumnCount() == 0;
            }
            appendAll(Arrays.asList(elementData));
        }
    }

    public void read(BufferedReader reader) throws IOException {
        ComplexMatrix m = Maths.zerosMatrix(1, 1);
        m.read(reader);
        Complex[] elementData = m.getColumnCount() == 0 ? m.getRow(0).toArray() : m.getColumn(0).toArray();
        if (elementData.length > 0) {
            if (size() == 0) {
                rowType = m.getColumnCount() == 0;
            }
            appendAll(Arrays.asList(elementData));
        }
    }

    @Override
    public ComplexVector copy() {
        return wrap(super.copy());
    }

    @Override
    public ComplexVector copy(CopyStrategy strategy) {
        return wrap(super.copy(strategy));
    }

    @Override
    public ComplexVector transform(VectorTransform<Complex, Complex> op) {
        return wrap(super.transform(op));
    }

    @Override
    public ComplexVector removeFirst() {
        return wrap(super.removeFirst());
    }

    @Override
    public ComplexVector removeLast() {
        return wrap(super.removeLast());
    }

    @Override
    public ComplexVector sublist(int fromIndex, int toIndex) {
        return wrap(super.sublist(fromIndex, toIndex));
    }

    @Override
    public ComplexVector sort() {
        return wrap(super.sort());
    }

    @Override
    public ComplexVector removeDuplicates() {
        return wrap(super.removeDuplicates());
    }

    @Override
    public ComplexVector concat(Complex e) {
        return wrap(super.concat(e));
    }

    @Override
    public ComplexVector concat(Vector<Complex> e) {
        return wrap(super.concat(e));
    }

    @Override
    public ComplexVector update(Enum i, Complex value) {
        return wrap(super.update(i, value));
    }

    @Override
    public ComplexVector rem(Vector<Complex> other) {
        return wrap(super.rem(other));
    }

    @Override
    public ComplexVector pow(double n) {
        return wrap(super.pow(n));
    }

    @Override
    public ComplexVector pow(Complex n) {
        return wrap(super.pow(n));
    }

    @Override
    protected ComplexVector toVector(Complex complex) {
        return wrap(super.toVector(complex));
    }

    @Override
    protected ComplexVector toVector(Complex complex, boolean row) {
        return wrap(super.toVector(complex, row));
    }

    @Override
    public ComplexVector filter(VectorFilter<Complex> filter) {
        return wrap(super.filter(filter));
    }

    @Override
    public ComplexVector appendAll(VectorModel<Complex> e) {
        return wrap(delegate.appendAll(e));
    }

    //    @Override
//    public ComplexVector concat(Complex e) {
//        ArrayVector v=new ArrayVector(this);
//        v.add(e);
//        return v;
//    }
//
//    @Override
//    public ComplexVector concat(Vector<Complex> e) {
//        ArrayVector v=new ArrayVector(this);
//        v.add(e);
//        return v;
//    }
}
