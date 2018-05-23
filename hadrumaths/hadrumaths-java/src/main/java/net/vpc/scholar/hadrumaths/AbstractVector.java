package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeReference;
import net.vpc.scholar.hadrumaths.symbolic.TParam;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.io.File;
import java.io.PrintStream;

/**
 * Created by vpc on 4/11/16.
 */
public abstract class AbstractVector extends AbstractTVector<Complex> implements Vector {
    private static final long serialVersionUID = 1L;

    public AbstractVector(boolean row) {
        super(row);
    }


    @Override
    public Matrix toMatrix() {
        if (isRow()) {
            return new MatrixFromRowVector(this);
        }
        return new MatrixFromColumnVector(this);
    }

    @Override
    public Vector transpose() {
        return new TransposedVector(this);
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
    public double[] toDoubleArray() {
        double[] all = new double[size()];
        int size = size();
        for (int i = 0; i < size; i++) {
            all[i] = get(i).toDouble();
        }
        return all;
    }

    @Override
    public Complex scalarProduct(TMatrix<Complex> v) {
        return scalarProduct(v.toVector());
    }

    public Complex scalarProduct(TVector<Complex> other) {
        int max = Math.max(size(), other.size());
        MutableComplex d = new MutableComplex();
        for (int i = 0; i < max; i++) {
            d.add(get(i).mul(other.get(i)));
        }
        return d.toComplex();
    }

    public Complex scalarProductAll(TVector<Complex>... other) {
        int currSize = size();
        for (TVector<Complex> v : other) {
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
            for (TVector<Complex> v : other) {
                el.mul(v.get(i));
            }
            d.add(el);
        }
        return d.toComplex();
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
    public void update(int i, Complex complex) {
        set(i, complex);
    }

    @Override
    public Vector dotmul(TVector<Complex> other) {
        Complex[] all = new Complex[Math.max(size(), other.size())];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).mul(other.get(i));
        }
        return new ArrayVector(all, isRow());
    }

    public Vector sqr() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).sqr();
        }
        return new ArrayVector(all, isRow());
    }

    @Override
    public Vector dotdiv(TVector<Complex> other) {
        Complex[] all = new Complex[Math.max(size(), other.size())];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).div(other.get(i));
        }
        return new ArrayVector(all, isRow());
    }

    @Override
    public Vector dotpow(TVector<Complex> other) {
        Complex[] all = new Complex[Math.max(size(), other.size())];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).pow(other.get(i));
        }
        return new ArrayVector(all, isRow());
    }


    @Override
    public Vector inv() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).inv();
        }
        return new ArrayVector(all, isRow());
    }

    @Override
    public Vector add(TVector<Complex> other) {
        Complex[] all = new Complex[Math.max(size(), other.size())];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).add(other.get(i));
        }
        return new ArrayVector(all, isRow());
    }

    @Override
    public Vector add(Complex other) {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).add(other);
        }
        return new ArrayVector(all, isRow());
    }

    @Override
    public Vector mul(Complex other) {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).mul(other);
        }
        return new ArrayVector(all, isRow());
    }

    @Override
    public Vector sub(Complex other) {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).sub(other);
        }
        return new ArrayVector(all, isRow());
    }

    @Override
    public Vector div(Complex other) {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).div(other);
        }
        return new ArrayVector(all, isRow());
    }

    @Override
    public Vector dotpow(Complex other) {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).pow(other);
        }
        return new ArrayVector(all, isRow());
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

    @Override
    public Complex avg() {
        int size = size();
        if (size == 0) {
            return Complex.ZERO;
        }
        MutableComplex c = MutableComplex.forComplex(get(0));
        for (int i = 1; i < size; i++) {
            c.add(get(i));
        }
        c.div(size);
        return c.toComplex();
    }

    @Override
    public Vector sub(TVector<Complex> other) {
        Complex[] all = new Complex[Math.max(size(), other.size())];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).sub(other.get(i));
        }
        return new ArrayVector(all, isRow());
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

    @Override
    public double getDistance(Normalizable other) {
        if (other instanceof Vector) {
            return this.sub((Vector) other).norm() / other.norm();
        } else {
            Normalizable o = other;
            return (this.norm() - o.norm()) / o.norm();
        }
    }

    @Override
    public String toString() {
        return toMatrix().toString();
    }

    public Vector conj() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).conj();
        }
        return new ArrayVector(all, isRow());
    }

    public Vector cos() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).cos();
        }
        return new ArrayVector(all, isRow());
    }

    public Vector cosh() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).cosh();
        }
        return new ArrayVector(all, isRow());
    }

    public Vector sin() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).sin();
        }
        return new ArrayVector(all, isRow());
    }

    public Vector sinh() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).sinh();
        }
        return new ArrayVector(all, isRow());
    }

    public Vector tan() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).tan();
        }
        return new ArrayVector(all, isRow());
    }

    public Vector tanh() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).tanh();
        }
        return new ArrayVector(all, isRow());
    }

    public Vector cotan() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).cotan();
        }
        return new ArrayVector(all, isRow());
    }

    public Vector cotanh() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).cotanh();
        }
        return new ArrayVector(all, isRow());
    }

    public Vector getReal() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = Complex.valueOf(get(i).getReal());
        }
        return new ArrayVector(all, isRow());
    }

    public Vector real() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = Complex.valueOf(get(i).getReal());
        }
        return new ArrayVector(all, isRow());
    }

    public Vector getImag() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = Complex.valueOf(get(i).getImag());
        }
        return new ArrayVector(all, isRow());
    }

    public Vector imag() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = Complex.valueOf(get(i).getImag());
        }
        return new ArrayVector(all, isRow());
    }

    public Vector db() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = (get(i).db());
        }
        return new ArrayVector(all, isRow());
    }

    public Vector db2() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = (get(i).db2());
        }
        return new ArrayVector(all, isRow());
    }

    public Vector abs() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = Complex.valueOf(get(i).absdbl());
        }
        return new ArrayVector(all, isRow());
    }

    public Vector abssqr() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = Complex.valueOf(get(i).absdblsqr());
        }
        return new ArrayVector(all, isRow());
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

    public Vector log() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).log();
        }
        return new ArrayVector(all, isRow());
    }

    public Vector log10() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).log10();
        }
        return new ArrayVector(all, isRow());
    }

    public Vector exp() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).exp();
        }
        return new ArrayVector(all, isRow());
    }

    public Vector sqrt() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).sqrt();
        }
        return new ArrayVector(all, isRow());
    }

    public Vector acosh() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).acosh();
        }
        return new ArrayVector(all, isRow());
    }

    public Vector acos() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).acos();
        }
        return new ArrayVector(all, isRow());
    }

    public Vector asinh() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).asinh();
        }
        return new ArrayVector(all, isRow());
    }

    public Vector asin() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).asin();
        }
        return new ArrayVector(all, isRow());
    }

    public Vector atan() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).atan();
        }
        return new ArrayVector(all, isRow());
    }

    public Vector acotan() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).acotan();
        }
        return new ArrayVector(all, isRow());
    }

    public Complex toComplex() {
        if (isComplex()) {
            return get(0);
        }
        throw new ClassCastException();
    }

    public boolean isComplex() {
        return size() == 1;
    }

    @Override
    public boolean isDouble() {
        return isComplex() && toComplex().isDouble();
    }


    @Override
    public Vector scalarProductToVector(Vector... other) {
        return Maths.columnVector(other.length, new VectorCell() {
            @Override
            public Complex get(int index) {
                return scalarProduct(other[index]);
            }
        });
    }


    @Override
    public Vector vscalarProduct(TVector<Complex>... other) {
        return Maths.columnVector(other.length, new VectorCell() {
            @Override
            public Complex get(int index) {
                return scalarProduct(other[index]);
            }
        });
    }

    @Override
    public Vector scalarProduct(Complex other) {
        if (isRow()) {
            return Maths.rowVector(size(),
                    new VectorCell() {
                        @Override
                        public Complex get(int index) {
                            return getComponentVectorSpace().scalarProduct(get(index), other);
                        }
                    });
        }
        return Maths.columnVector(size(),
                new VectorCell() {
                    @Override
                    public Complex get(int index) {
                        return getComponentVectorSpace().scalarProduct(get(index), other);
                    }
                });
    }

    @Override
    public Vector rscalarProduct(Complex other) {
        if (isRow()) {
            return Maths.rowVector(size(),
                    new VectorCell() {
                        @Override
                        public Complex get(int index) {
                            return getComponentVectorSpace().scalarProduct(other, get(index));
                        }
                    });
        }
        return Maths.columnVector(size(),
                new VectorCell() {
                    @Override
                    public Complex get(int index) {
                        return getComponentVectorSpace().scalarProduct(other, get(index));
                    }
                });
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
    public Vector eval(ElementOp<Complex> op) {
        return new ReadOnlyVector(new TVectorModel<Complex>() {
            @Override
            public Complex get(int index) {
                return op.eval(index, AbstractVector.this.get(index));
            }

            @Override
            public int size() {
                return AbstractVector.this.size();
            }
        }, isRow());
    }

    @Override
    public TypeReference<Complex> getComponentType() {
        return Maths.$COMPLEX;
    }

    @Override
    public VectorSpace<Complex> getComponentVectorSpace() {
        return Maths.COMPLEX_VECTOR_SPACE;
    }

    public Vector setParam(String name, Object value) {
        return eval(new ElementOp<Complex>() {
            @Override
            public Complex eval(int index, Complex e) {
                VectorSpace<Complex> cs = getComponentVectorSpace();
                return cs.setParam(e, name, value);
            }
        });
    }

    public Vector setParam(TParam param, Object value) {
        return eval(new ElementOp<Complex>() {
            @Override
            public Complex eval(int index, Complex e) {
                VectorSpace<Complex> cs = getComponentVectorSpace();
                return cs.setParam(e, param.getParamName(), value);
            }
        });
    }

    public <R> TVector<R> transform(TypeReference<R> toType, TTransform<Complex, R> op) {
        return new ReadOnlyTVector<R>(
                toType, isRow(), new TVectorModel<R>() {
            @Override
            public R get(int index) {
                Complex t = AbstractVector.this.get(index);
                return op.transform(index, t);
            }

            @Override
            public int size() {
                return AbstractVector.this.size();
            }
        }
        );
    }

    @Override
    public <R> boolean isConvertibleTo(TypeReference<R> other) {
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
}
