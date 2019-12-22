package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;
import net.vpc.scholar.hadrumaths.symbolic.TParam;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.io.File;
import java.io.PrintStream;

/**
 * Created by vpc on 4/11/16.
 */
public abstract class AbstractComplexVector extends AbstractTVector<Complex> implements ComplexVector {
    private static final long serialVersionUID = 1L;

    public AbstractComplexVector(boolean row) {
        super(row);
    }


    @Override
    public ComplexMatrix toMatrix() {
        if (isRow()) {
            return new ComplexMatrixFromRowVector(this);
        }
        return new ComplexMatrixFromColumnVector(this);
    }

    @Override
    public ComplexVector transpose() {
        return new TransposedComplexVector(this);
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
    public ComplexVector update(int i, Complex complex) {
        return (ComplexVector) super.update(i, complex);
    }

    @Override
    public ComplexVector set(int i, Complex complex) {
        return (ComplexVector) super.set(i, complex);
    }

    @Override
    public TVector<Complex> set(Enum i, Complex value) {
        return (ComplexVector) super.set(i, value);
    }

    @Override
    public ComplexVector dotmul(TVector<Complex> other) {
        Complex[] all = new Complex[Math.max(size(), other.size())];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).mul(other.get(i));
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector sqr() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).sqr();
        }
        return new ArrayComplexVector(all, isRow());
    }

    @Override
    public ComplexVector dotdiv(TVector<Complex> other) {
        Complex[] all = new Complex[Math.max(size(), other.size())];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).div(other.get(i));
        }
        return new ArrayComplexVector(all, isRow());
    }

    @Override
    public ComplexVector dotpow(TVector<Complex> other) {
        Complex[] all = new Complex[Math.max(size(), other.size())];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).pow(other.get(i));
        }
        return new ArrayComplexVector(all, isRow());
    }
    @Override
    public ComplexVector pow(TVector<Complex> other) {
        Complex n = other.toComplex();
        Complex[] all = new Complex[size()];
        VectorSpace<Complex> cs = getComponentVectorSpace();
        for (int i = 0; i < all.length; i++) {
            all[i] = cs.pow(get(i), n);
        }
        return new ArrayComplexVector(all,isRow());
    }

    @Override
    public ComplexVector inv() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).inv();
        }
        return new ArrayComplexVector(all, isRow());
    }

    @Override
    public ComplexVector add(TVector<Complex> other) {
        Complex[] all = new Complex[Math.max(size(), other.size())];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).add(other.get(i));
        }
        return new ArrayComplexVector(all, isRow());
    }

    @Override
    public ComplexVector add(Complex other) {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).add(other);
        }
        return new ArrayComplexVector(all, isRow());
    }

    @Override
    public ComplexVector mul(Complex other) {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).mul(other);
        }
        return new ArrayComplexVector(all, isRow());
    }

    @Override
    public ComplexVector sub(Complex other) {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).sub(other);
        }
        return new ArrayComplexVector(all, isRow());
    }

    @Override
    public ComplexVector div(Complex other) {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).div(other);
        }
        return new ArrayComplexVector(all, isRow());
    }
    @Override
    public ComplexVector rem(Complex other) {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).rem(other);
        }
        return new ArrayComplexVector(all, isRow());
    }

    @Override
    public ComplexVector dotpow(Complex other) {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).pow(other);
        }
        return new ArrayComplexVector(all, isRow());
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
    public ComplexVector sub(TVector<Complex> other) {
        Complex[] all = new Complex[Math.max(size(), other.size())];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).sub(other.get(i));
        }
        return new ArrayComplexVector(all, isRow());
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
        if (other instanceof ComplexVector) {
            return this.sub((ComplexVector) other).norm() / other.norm();
        } else {
            Normalizable o = other;
            return (this.norm() - o.norm()) / o.norm();
        }
    }

    @Override
    public String toString() {
        return toMatrix().toString();
    }

    public ComplexVector conj() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).conj();
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector cos() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).cos();
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector cosh() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).cosh();
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector sin() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).sin();
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector sinh() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).sinh();
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector tan() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).tan();
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector tanh() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).tanh();
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector cotan() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).cotan();
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector cotanh() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).cotanh();
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector getReal() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = Complex.valueOf(get(i).getReal());
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector real() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = Complex.valueOf(get(i).getReal());
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector getImag() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = Complex.valueOf(get(i).getImag());
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector imag() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = Complex.valueOf(get(i).getImag());
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector db() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = (get(i).db());
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector db2() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = (get(i).db2());
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector abs() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = Complex.valueOf(get(i).absdbl());
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector abssqr() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = Complex.valueOf(get(i).absdblsqr());
        }
        return new ArrayComplexVector(all, isRow());
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

    public ComplexVector log() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).log();
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector log10() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).log10();
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector neg() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).neg();
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector exp() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).exp();
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector sqrt() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).sqrt();
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector acosh() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).acosh();
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector acos() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).acos();
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector asinh() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).asinh();
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector asin() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).asin();
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector atan() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).atan();
        }
        return new ArrayComplexVector(all, isRow());
    }

    public ComplexVector acotan() {
        Complex[] all = new Complex[size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = get(i).acotan();
        }
        return new ArrayComplexVector(all, isRow());
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
    public ComplexVector scalarProductToVector(ComplexVector... other) {
        return MathsBase.columnVector(other.length, new TVectorCell<Complex>() {
            @Override
            public Complex get(int index) {
                return scalarProduct(other[index]);
            }
        });
    }


    @Override
    public ComplexVector vscalarProduct(TVector<Complex>... other) {
        return MathsBase.columnVector(other.length, new TVectorCell<Complex>() {
            @Override
            public Complex get(int index) {
                return scalarProduct(other[index]);
            }
        });
    }

    @Override
    public ComplexVector scalarProduct(Complex other) {
        if (isRow()) {
            return MathsBase.rowVector(size(),
                    new TVectorCell<Complex>() {
                        @Override
                        public Complex get(int index) {
                            return getComponentVectorSpace().scalarProduct(get(index), other);
                        }
                    });
        }
        return MathsBase.columnVector(size(),
                new TVectorCell<Complex>() {
                    @Override
                    public Complex get(int index) {
                        return getComponentVectorSpace().scalarProduct(get(index), other);
                    }
                });
    }

    @Override
    public ComplexVector rscalarProduct(Complex other) {
        if (isRow()) {
            return MathsBase.rowVector(size(),
                    new TVectorCell<Complex>() {
                        @Override
                        public Complex get(int index) {
                            return getComponentVectorSpace().scalarProduct(other, get(index));
                        }
                    });
        }
        return MathsBase.columnVector(size(),
                new TVectorCell<Complex>() {
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
    public ComplexVector eval(ElementOp<Complex> op) {
        return new ReadOnlyComplexVector(new TVectorModel<Complex>() {
            @Override
            public Complex get(int index) {
                return op.eval(index, AbstractComplexVector.this.get(index));
            }

            @Override
            public int size() {
                return AbstractComplexVector.this.size();
            }
        }, isRow());
    }

    @Override
    public TypeName<Complex> getComponentType() {
        return MathsBase.$COMPLEX;
    }

    @Override
    public VectorSpace<Complex> getComponentVectorSpace() {
        return MathsBase.COMPLEX_VECTOR_SPACE;
    }

    public ComplexVector setParam(String name, Object value) {
        return eval(new ElementOp<Complex>() {
            @Override
            public Complex eval(int index, Complex e) {
                VectorSpace<Complex> cs = getComponentVectorSpace();
                return cs.setParam(e, name, value);
            }
        });
    }

    public ComplexVector setParam(TParam param, Object value) {
        return eval(new ElementOp<Complex>() {
            @Override
            public Complex eval(int index, Complex e) {
                VectorSpace<Complex> cs = getComponentVectorSpace();
                return cs.setParam(e, param.getParamName(), value);
            }
        });
    }

    public <R> TVector<R> transform(TypeName<R> toType, TTransform<Complex, R> op) {
        return new ReadOnlyTList<R>(
                toType, isRow(), new TVectorModel<R>() {
            @Override
            public R get(int index) {
                Complex t = AbstractComplexVector.this.get(index);
                return op.transform(index, t);
            }

            @Override
            public int size() {
                return AbstractComplexVector.this.size();
            }
        }
        );
    }

    @Override
    public <R> boolean isConvertibleTo(TypeName<R> other) {
        if (
                MathsBase.$COMPLEX.equals(other)
                        || MathsBase.$EXPR.equals(other)
                ) {
            return true;
        }
        if (other.isAssignableFrom(getComponentType())) {
            return true;
        }
        VectorSpace<Complex> vs = MathsBase.getVectorSpace(getComponentType());
        for (Complex t : this) {
            if (!vs.is(t, other)) {
                return false;
            }
        }
        return true;
    }

//    @Override
//    public TVector<Complex> concat(Complex e) {
//        ArrayVector v=new ArrayVector(this);
//        v.add(e);
//        return v;
//    }
//
//    @Override
//    public TVector<Complex> concat(TVector<Complex> e) {
//        ArrayVector v=new ArrayVector(this);
//        v.add(e);
//        return v;
//    }
}
