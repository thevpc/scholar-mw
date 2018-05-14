package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.util.Arrays;
import java.util.List;

//import net.vpc.scholar.math.functions.dfxy.DDxToDDxy;

/**
 * User: taha Date: 2 juil. 2003 Time: 10:39:39
 */
public class DCxy extends AbstractDoubleToComplex implements Cloneable {

    private static final long serialVersionUID = 1L;
    private static final DCxy ZERO = new DCxy(FunctionFactory.DZEROXY);
    protected DoubleToDouble real;
    protected DoubleToDouble imag;
    protected Domain domain;
    protected int zStatus = 0;
    private static final int Z_NONE = 0;
    private static final int Z_R = 1;
    private static final int Z_I = 2;
    private static final int Z_RI = 3;
    //    public static final CFunctionXY ZERO =new CFunctionXY(DCstFunctionXY.ZERO,DCstFunctionXY.ZERO).setName("0");

//    public DCxy(IDDx real) {
//        this(new DDxToDDxy(real));
//    }

    public DCxy(DoubleToDouble real) {
        this(real.getDomain(), real, FunctionFactory.DZEROXY);
//        name = (real.getName());
    }

    public DCxy(DoubleToDouble real, DoubleToDouble imag) {
        this(real.getDomain(), real, imag);
//        name = (real.getName());
    }

    protected DCxy(Domain domain, DoubleToDouble real, DoubleToDouble imag) {
        this.domain = domain;
        this.real = real == null ? FunctionFactory.DZEROXY : real.toDD();
        this.imag = imag == null ? FunctionFactory.DZEROXY : imag.toDD();
        if (!this.real.isZero()) {
            zStatus |= 1;
        }
        if (!this.imag.isZero()) {
            zStatus |= 2;
        }
//        name = (this.real.getName() + "+i." + this.imag.getName());
    }

//    @Override
//    public Complex computeComplex(double x, BooleanMarker defined) {
//        Out<Range> ranges = new Out<>();
//        Complex complex = computeComplex(new double[]{x}, null, ranges)[0];
//        defined.set(ranges.get().getDefined1().get(0));
//        return complex;
//    }

    //    /**
//     * &lt;this,other&gt;
//     *
//     * @param other
//     * @return
//     */
//    public Complex leftScalarProduct(DomainXY domain, CFunctionXY other) {
//        return
//                new Complex(this.real.leftScalarProduct(domain, other.real) + this.imag.leftScalarProduct(domain, other.imag),
//                        this.real.leftScalarProduct(domain, other.imag) - this.imag.leftScalarProduct(domain, other.real));
//    }
//
//    /**
//     * &lt;other,this&gt;
//     *
//     * @param other
//     * @return
//     */
//    public Complex rightScalarProduct(DomainXY domain, CFunctionXY other) {
//        return other.leftScalarProduct(domain, this);
//    }
//
//
//    public Complex scalarProduct(CFunctionXY other) {
//        return ScalarProductFactory.scalarProduct(this, other);
//    }
//
//    public Complex scalarProduct(CFunctionXY other, DomainXY do0) {
//        return ScalarProductFactory.scalarProduct(this, other, do0);
//    }
//    public DCxy(Domain domain) {
//        this.domain = domain;
//    }

    public static DCxy valueOf(DoubleToDouble real) {
        if (real == null || real.isZero()) {
            return ZERO;
        }
        return new DCxy(real);
    }

    public static DCxy valueOf(DoubleToDouble real, DoubleToDouble imag) {
        if ((real == null || real.isZero()) && (imag == null || imag.isZero())) {
            return ZERO;
        }
        return new DCxy(real, imag);
    }

    public boolean isInvariantImpl(Axis axis) {
        return real.isInvariant(axis) && imag.isInvariant(axis);
    }

    public Complex[][] computeComplex(double[] x, double[] y, Domain d0) {
        return computeComplex(x, y, d0, null);
    }

    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        d0 = domain.intersect(d0);
        Range abcd = (d0 == null ? domain : domain.intersect(d0)).range(x, y);
        if (abcd != null) {
            switch (zStatus) {
                case Z_NONE: {
                    Complex[][] c = ArrayUtils.fillArray2Complex(x.length, y.length, Complex.ZERO);
                    if (ranges != null) {
                        abcd.setDefined2(x.length,y.length);
                        abcd.getDefined2().setAll();
                        ranges.set(abcd);
                        ranges.set(null);
                    }
                    return c;
                }
                case Z_R: {
                    Out<Range> rranges = new Out<>();
                    double[][] r = real.computeDouble(x, y, d0, rranges);
                    BooleanArray2 rdefined3 = rranges.get() == null ? null : rranges.get().getDefined2();
                    BooleanArray2 def3 = abcd.setDefined2(x.length, y.length);

                    if (rdefined3 != null) {
                        def3.addFrom(rdefined3, rranges.get());
                    }
                    Complex[][] c = new Complex[y.length][x.length];
                    for (int j = abcd.ymin; j <= abcd.ymax; j++) {
                        for (int k = abcd.xmin; k <= abcd.xmax; k++) {
                            if ((rdefined3 == null || !rdefined3.get(j, k))) {
                                c[j][k] = Complex.ZERO;
                            } else {
                                double vr = (rdefined3 == null || !rdefined3.get(j, k)) ? 0 : r[j][k];
                                c[j][k] = Complex.valueOf(vr);
                            }
                        }
                    }
                    ArrayUtils.fillArray2ZeroComplex(c, abcd);
                    if (ranges != null) {
                        ranges.set(abcd);
                    }
                    return c;
                }
                case Z_I: {
                    Out<Range> iranges = new Out<>();
                    double[][] i = imag.computeDouble(x, y, d0, iranges);
                    BooleanArray2 idefined3 = iranges.get() == null ? null : iranges.get().getDefined2();
                    BooleanArray2 def3 = abcd.setDefined2(x.length, y.length);

                    if (idefined3 != null) {
                        def3.addFrom(idefined3, iranges.get());
                    }
                    Complex[][] c = new Complex[y.length][x.length];
                    for (int j = abcd.ymin; j <= abcd.ymax; j++) {
                        for (int k = abcd.xmin; k <= abcd.xmax; k++) {
                            if ((idefined3 == null || !idefined3.get(j, k))) {
                                c[j][k] = Complex.ZERO;
                            } else {
                                double vi = (idefined3 == null || !idefined3.get(j, k)) ? 0 : i[j][k];
                                c[j][k] = Complex.I(vi);
                            }
                        }
                    }
                    ArrayUtils.fillArray2ZeroComplex(c, abcd);
                    if (ranges != null) {
                        ranges.set(abcd);
                    }
                    return c;
                }
                case Z_RI: {
                    Out<Range> iranges = new Out<>();
                    Out<Range> rranges = new Out<>();
                    double[][] i = imag.computeDouble(x, y, d0, iranges);
                    double[][] r = real.computeDouble(x, y, d0, rranges);
                    BooleanArray2 idefined3 = iranges.get() == null ? null : iranges.get().getDefined2();
                    BooleanArray2 rdefined3 = rranges.get() == null ? null : rranges.get().getDefined2();
                    BooleanArray2 def3 = abcd.setDefined2(x.length, y.length);

                    if (idefined3 != null) {
                        def3.addFrom(idefined3, iranges.get());
                    }
                    if (rdefined3 != null) {
                        def3.addFrom(rdefined3, rranges.get());
                    }
                    Complex[][] c = new Complex[y.length][x.length];
                    for (int j = abcd.ymin; j <= abcd.ymax; j++) {
                        for (int k = abcd.xmin; k <= abcd.xmax; k++) {
                            if ((idefined3 == null || !idefined3.get(j, k)) && (rdefined3 == null || !rdefined3.get(j, k))) {
                                c[j][k] = Complex.ZERO;
                            } else {
                                double vr = (rdefined3 == null || !rdefined3.get(j, k)) ? 0 : r[j][k];
                                double vi = (idefined3 == null || !idefined3.get(j, k)) ? 0 : i[j][k];
                                c[j][k] = Complex.valueOf(vr, vi);
                            }
                        }
                    }
                    ArrayUtils.fillArray2ZeroComplex(c, abcd);
                    if (ranges != null) {
                        ranges.set(abcd);
                    }
                    return c;
                }
            }
            throw new IllegalArgumentException("Unsupported");
        } else {
            Complex[][] c = ArrayUtils.fillArray2Complex(x.length, y.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return c;
        }
    }

    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
        d0 = domain.intersect(d0);
        Range abcd = (d0 == null ? domain : domain.intersect(d0)).range(x);
        if (abcd != null) {
            switch (zStatus) {
                case Z_NONE:{
                    Complex[] c = ArrayUtils.fillArray1Complex(x.length, Complex.ZERO);
                    if (ranges != null) {
                        abcd.setDefined1(x.length);
                        abcd.getDefined1().setAll();
                        ranges.set(abcd);
                    }
                    return c;
                }
                case Z_R:{
                    Out<Range> rranges = new Out<>();
                    double[] r = real.computeDouble(x, d0, rranges);
                    BooleanArray1 rdefined3 = rranges.get() == null ? null : rranges.get().getDefined1();
                    BooleanArray1 def3 = abcd.setDefined1(x.length);

                    if (rdefined3 != null) {
                        def3.addFrom(rdefined3, rranges.get());
                    }
                    Complex[] c = new Complex[x.length];
                    for (int k = abcd.xmin; k <= abcd.xmax; k++) {
                        if ( (rdefined3 == null || !rdefined3.get(k))) {
                            c[k] = Complex.ZERO;
                        } else {
                            double vr = (rdefined3 == null || !rdefined3.get(k)) ? 0 : r[k];
                            c[k] = Complex.valueOf(vr);
                        }
                    }
                    ArrayUtils.fillArray1ZeroComplex(c, abcd);
                    if (ranges != null) {
                        ranges.set(abcd);
                    }
                    return c;
                }
                case Z_I:{
                    Out<Range> iranges = new Out<>();
                    double[] i = imag.computeDouble(x, d0, iranges);
                    BooleanArray1 idefined3 = iranges.get() == null ? null : iranges.get().getDefined1();
                    BooleanArray1 def3 = abcd.setDefined1(x.length);

                    if (idefined3 != null) {
                        def3.addFrom(idefined3, iranges.get());
                    }
                    Complex[] c = new Complex[x.length];
                    for (int k = abcd.xmin; k <= abcd.xmax; k++) {
                        if ((idefined3 == null || !idefined3.get(k)) ) {
                            c[k] = Complex.ZERO;
                        } else {
                            double vi = (idefined3 == null || !idefined3.get(k)) ? 0 : i[k];
                            c[k] = Complex.I(vi);
                        }
                    }
                    ArrayUtils.fillArray1ZeroComplex(c, abcd);
                    if (ranges != null) {
                        ranges.set(abcd);
                    }
                    return c;
                }
                case Z_RI:{
                    Out<Range> iranges = new Out<>();
                    Out<Range> rranges = new Out<>();
                    double[] i = imag.computeDouble(x, d0, iranges);
                    double[] r = real.computeDouble(x, d0, rranges);
                    BooleanArray1 idefined3 = iranges.get() == null ? null : iranges.get().getDefined1();
                    BooleanArray1 rdefined3 = rranges.get() == null ? null : rranges.get().getDefined1();
                    BooleanArray1 def3 = abcd.setDefined1(x.length);

                    if (idefined3 != null) {
                        def3.addFrom(idefined3, iranges.get());
                    }
                    if (rdefined3 != null) {
                        def3.addFrom(rdefined3, rranges.get());
                    }
                    Complex[] c = new Complex[x.length];
                    for (int k = abcd.xmin; k <= abcd.xmax; k++) {
                        if ((idefined3 == null || !idefined3.get(k)) && (rdefined3 == null || !rdefined3.get(k))) {
                            c[k] = Complex.ZERO;
                        } else {
                            double vr = (rdefined3 == null || !rdefined3.get(k)) ? 0 : r[k];
                            double vi = (idefined3 == null || !idefined3.get(k)) ? 0 : i[k];
                            c[k] = Complex.valueOf(vr, vi);
                        }
                    }
                    ArrayUtils.fillArray1ZeroComplex(c, abcd);
                    if (ranges != null) {
                        ranges.set(abcd);
                    }
                    return c;
                }
            }
            throw new IllegalArgumentException("Unsupported");
        } else {
            Complex[] c = ArrayUtils.fillArray1Complex(x.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return c;
        }
    }

    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        d0 = domain.intersect(d0);
        Range abcd = (d0 == null ? domain : domain.intersect(d0)).range(x, y, z);
        if (abcd != null) {
            switch (zStatus) {
                case Z_NONE: {
                    Complex[][][] c = ArrayUtils.fillArray3Complex(x.length, y.length, z.length, Complex.ZERO);
                    if (ranges != null) {
                        abcd.setDefined3(x.length, y.length, z.length);
                        abcd.getDefined3().setAll();
                        ranges.set(abcd);
                    }
                    return c;
                }
                case Z_R: {
                    Out<Range> rranges = new Out<>();
                    double[][][] r = real.computeDouble(x, y, z, d0, rranges);
                    BooleanArray3 rdefined3 = rranges.get() == null ? null : rranges.get().getDefined3();
                    BooleanArray3 def3 = abcd.setDefined3(x.length, y.length, z.length);

                    if (rdefined3 != null) {
                        def3.addFrom(rdefined3, rranges.get());
                    }
                    Complex[][][] c = new Complex[z.length][y.length][x.length];
                    for (int t = abcd.zmin; t <= abcd.zmax; t++) {
                        for (int j = abcd.ymin; j <= abcd.ymax; j++) {
                            for (int k = abcd.xmin; k <= abcd.xmax; k++) {
                                if ((rdefined3 == null || !rdefined3.get(t, j, k))) {
                                    c[t][j][k] = Complex.ZERO;
                                } else {
                                    double vr = (rdefined3 == null || !rdefined3.get(t, j, k)) ? 0 : r[t][j][k];
                                    c[t][j][k] = Complex.valueOf(vr);
                                }
                            }
                        }
                    }
                    ArrayUtils.fillArray3ZeroComplex(c, abcd);
                    if (ranges != null) {
                        ranges.set(abcd);
                    }
                    return c;
                }
                case Z_I: {
                    Out<Range> iranges = new Out<>();
                    double[][][] i = imag.computeDouble(x, y, z, d0, iranges);
                    BooleanArray3 idefined3 = iranges.get() == null ? null : iranges.get().getDefined3();
                    BooleanArray3 def3 = abcd.setDefined3(x.length, y.length, z.length);

                    if (idefined3 != null) {
                        def3.addFrom(idefined3, iranges.get());
                    }
                    Complex[][][] c = new Complex[z.length][y.length][x.length];
                    for (int t = abcd.zmin; t <= abcd.zmax; t++) {
                        for (int j = abcd.ymin; j <= abcd.ymax; j++) {
                            for (int k = abcd.xmin; k <= abcd.xmax; k++) {
                                if ((idefined3 == null || !idefined3.get(t, j, k))) {
                                    c[t][j][k] = Complex.ZERO;
                                } else {
                                    double vi = (idefined3 == null || !idefined3.get(t, j, k)) ? 0 : i[t][j][k];
                                    c[t][j][k] = Complex.I(vi);
                                }
                            }
                        }
                    }
                    ArrayUtils.fillArray3ZeroComplex(c, abcd);
                    if (ranges != null) {
                        ranges.set(abcd);
                    }
                    return c;
                }
                case Z_RI: {
                    Out<Range> iranges = new Out<>();
                    Out<Range> rranges = new Out<>();
                    double[][][] i = imag.computeDouble(x, y, z, d0, iranges);
                    double[][][] r = real.computeDouble(x, y, z, d0, rranges);
                    BooleanArray3 idefined3 = iranges.get() == null ? null : iranges.get().getDefined3();
                    BooleanArray3 rdefined3 = rranges.get() == null ? null : rranges.get().getDefined3();
                    BooleanArray3 def3 = abcd.setDefined3(x.length, y.length, z.length);

                    if (idefined3 != null) {
                        def3.addFrom(idefined3, iranges.get());
                    }
                    if (rdefined3 != null) {
                        def3.addFrom(rdefined3, rranges.get());
                    }
                    Complex[][][] c = new Complex[z.length][y.length][x.length];
                    for (int t = abcd.zmin; t <= abcd.zmax; t++) {
                        for (int j = abcd.ymin; j <= abcd.ymax; j++) {
                            for (int k = abcd.xmin; k <= abcd.xmax; k++) {
                                if ((idefined3 == null || !idefined3.get(t, j, k)) && (rdefined3 == null || !rdefined3.get(t, j, k))) {
                                    c[t][j][k] = Complex.ZERO;
                                } else {
                                    double vr = (rdefined3 == null || !rdefined3.get(t, j, k)) ? 0 : r[t][j][k];
                                    double vi = (idefined3 == null || !idefined3.get(t, j, k)) ? 0 : i[t][j][k];
                                    c[t][j][k] = Complex.valueOf(vr, vi);
                                }
                            }
                        }
                    }
                    ArrayUtils.fillArray3ZeroComplex(c, abcd);
                    if (ranges != null) {
                        ranges.set(abcd);
                    }
                    return c;
                }
            }
            throw new IllegalArgumentException("Unsupported");
        } else {
            Complex[][][] c = ArrayUtils.fillArray3Complex(x.length, y.length, z.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return c;
        }
    }

    public Complex[] computeComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, x, y, d0, ranges);
    }

    public Complex[] computeComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, x, y, d0, ranges);
    }

    public boolean isDDImpl() {
        return imag.isZero();
    }

    public DoubleToDouble toDD() {
        if (imag.isZero()) {
            return real;
        }
        throw new UnsupportedOperationException("[" + getClass().getName() + "]" + "Not supported yet.");
    }

//    public boolean isDDx() {
//        return imag.isZero() && real.isDDx();
//    }

//    public IDDx toDDx() {
//        if (isDDx()) {
//            real.toDDx();
//        }
//        throw new UnsupportedOperationException("Not supported yet.");
//    }

    public DoubleToComplex add(DoubleToComplex other) {
        return Maths.sum(this, other).toDC();
//        return new DCxy(real.add(other.real), imag.add(other.imag));
    }

    //    public DCxy simplify() {
//        return new DCxy(real.simplify(), imag.simplify());
//    }
//    public DCxy multiply(DCxy other) {
//        return new DCxy(
//                new DDxyProduct(real, other.real).add(new DDxyProduct(imag.multiply(-1, null), other.imag)),
//                new DDxyProduct(real, other.imag).add(new DDxyProduct(imag, other.real))
//        );
//    }

    public Domain getDomainImpl() {
        return domain;
    }

    public Domain intersect(DCxy other) {
        return domain.intersect(other.domain);
    }

    public Domain intersect(DCxy other, Domain domain) {
//        return Domain.intersect(domain, other.domain, domain);
        return domain.intersect(other.domain);
    }

    public DoubleToDouble getRealDD() {
        return real;
    }

    public DoubleToDouble getImagDD() {
        return imag;
    }

    public DoubleToComplex conj() {
        return Maths.complex(real, Maths.mul(Maths.expr(-1, Domain.FULL(getDomainDimension())), imag).toDD());
    }

//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        if (!real.isZero()) {
//            sb.append(real.toString());
//        }
//        if (!imag.isZero()) {
//            String s = imag.toString();
//            if (!s.startsWith("-")) {
//                sb.append("+");
//            }
//            sb.append(s);
//            sb.append("*i");
//        }
//        return sb.toString();
//    }


    //    public boolean isInvariant(Axis axis) {
//        return real.isInvariant(axis) && real.isInvariant(axis);
//    }
//
//    public boolean isSymmetric(AxisXY axis) {
//        return real.isSymmetric(axis) && real.isSymmetric(axis);
//    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof DCxy)) {
            return false;
        }
        DCxy c = (DCxy) obj;
        return c.imag.equals(imag) && c.real.equals(real);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.real != null ? this.real.hashCode() : 0);
        hash = 97 * hash + (this.imag != null ? this.imag.hashCode() : 0);
        hash = 97 * hash + (this.domain != null ? this.domain.hashCode() : 0);
        return hash;
    }

    public boolean isZeroImpl() {
        return real.isZero() && imag.isZero();
    }

    public boolean isNaNImpl() {
        return real.isNaN() || imag.isNaN();
    }

    public boolean isInifnite() {
        return real.isInfinite() || imag.isInfinite();
    }

    @Override
    public DoubleToComplex clone() {
        DCxy x = (DCxy) super.clone();
        x.real = (DoubleToDouble) x.real.clone();
        x.imag = (DoubleToDouble) x.imag.clone();
        return x;
    }

    public boolean isInfiniteImpl() {
        return getRealDD().isInfinite() || getImagDD().isInfinite();
    }

    public List<Expr> getSubExpressions() {
        return Arrays.asList(new Expr[]{real, imag});
    }


    @Override
    public boolean isDoubleImpl() {
        return false;
    }

    @Override
    public boolean isComplexImpl() {
        return isZero() || (getDomain().isFull() && isInvariant(Axis.X) && isInvariant(Axis.Y) && isInvariant(Axis.Z));
    }

    @Override
    public boolean isMatrixImpl() {
        return false;
    }

    @Override
    public Complex toComplex() {
        return Complex.valueOf(
                real.toDouble(),
                imag.toDouble()
        );
    }

    @Override
    public double toDouble() {
        if (!imag.isZero()) {
            throw new ClassCastException();
        }
        return real.toDouble();
    }

    @Override
    public Matrix toMatrix() {
        throw new ClassCastException();
    }

    @Override
    public boolean hasParamsImpl() {
        return real.hasParams() || imag.hasParams();
    }

    @Override
    public Expr setParam(String name, double value) {
        return setParam(name, DoubleValue.valueOf(value, Domain.FULL(getDomainDimension())));
    }

    @Override
    public Expr setParam(String name, Expr value) {
        DoubleToDouble real = this.real.setParam(name, value).toDD();
        DoubleToDouble imag = this.imag.setParam(name, value).toDD();
        if (!real.equals(this.real) || !(imag.equals(this.imag))) {
            Expr e = DCxy.valueOf(real, imag);
            e = Any.copyProperties(this, e);
            return Any.updateTitleVars(e, name, value);
        }
        return this;
    }

    @Override
    public Expr composeX(Expr xreplacement) {
        DoubleToDouble real = this.real.composeX(xreplacement).toDD();
        DoubleToDouble imag = this.imag.composeX(xreplacement).toDD();
        if (!real.equals(this.real) || !(imag.equals(this.imag))) {
            Expr e = DCxy.valueOf(real, imag);
            e = Any.copyProperties(this, e);
            return e;
        }
        return this;
    }

    @Override
    public Expr composeY(Expr yreplacement) {
        DoubleToDouble real = this.real.composeY(yreplacement).toDD();
        DoubleToDouble imag = this.imag.composeY(yreplacement).toDD();
        if (!real.equals(this.real) || !(imag.equals(this.imag))) {
            Expr e = DCxy.valueOf(real, imag);
            e = Any.copyProperties(this, e);
            return e;
        }
        return this;
    }

    @Override
    public boolean isScalarExprImpl() {
        return true;
    }

//    @Override
//    public boolean isDoubleExprImpl() {
//        return false;
//    }

    @Override
    public boolean isDoubleTyped() {
        return getImagDD().isZero();
    }

    @Override
    public Complex computeComplex(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            BooleanRef ro = BooleanMarker.ref();
            BooleanRef ri = BooleanMarker.ref();
            double r = 0;
            double i = 0;
            switch (zStatus) {
                case Z_NONE: {
                    defined.set();
                    break;
                }
                case Z_R: {
                    r = real.computeDouble(x, y, z, ro);
                    if (ro.get()) {
                        defined.set();
                    }
                    return Complex.valueOf(r);
                }
                case Z_I: {
                    i = imag.computeDouble(x, y, z, ri);
                    if (ri.get()) {
                        defined.set();
                    }
                    return Complex.I(i);
                }
                case Z_RI: {
                    r = real.computeDouble(x, y, z, ro);
                    i = imag.computeDouble(x, y, z, ri);
                    if (ri.get() || ri.get()) {
                        defined.set();
                    }
                    return Complex.valueOf(r, i);
                }
            }
        }
        return Complex.ZERO;
    }

    public Complex computeComplex(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            BooleanRef ro = BooleanMarker.ref();
            BooleanRef ri = BooleanMarker.ref();
            double r = 0;
            double i = 0;
            switch (zStatus) {
                case Z_NONE: {
                    defined.set();
                    break;
                }
                case Z_R: {
                    r = real.computeDouble(x, y, ro);
                    if (ro.get()) {
                        defined.set();
                    }
                    return Complex.valueOf(r);
                }
                case Z_I: {
                    i = imag.computeDouble(x, y, ri);
                    if (ri.get()) {
                        defined.set();
                    }
                    return Complex.I(i);
                }
                case Z_RI: {
                    r = real.computeDouble(x, y, ro);
                    i = imag.computeDouble(x, y, ri);
                    if (ri.get() || ri.get()) {
                        defined.set();
                    }
                    return Complex.valueOf(r, i);
                }
            }
        }
        return Complex.ZERO;
    }

    @Override
    public Complex computeComplex(double x, BooleanMarker defined) {
        if (contains(x)) {
            BooleanRef ro = BooleanMarker.ref();
            BooleanRef ri = BooleanMarker.ref();
            double r = 0;
            double i = 0;
            switch (zStatus) {
                case Z_NONE: {
                    defined.set();
                    break;
                }
                case Z_R: {
                    r = real.computeDouble(x, ro);
                    if (ro.get()) {
                        defined.set();
                    }
                    return Complex.valueOf(r);
                }
                case Z_I: {
                    i = imag.computeDouble(x, ri);
                    if (ri.get()) {
                        defined.set();
                    }
                    return Complex.I(i);
                }
                case Z_RI: {
                    r = real.computeDouble(x, ro);
                    i = imag.computeDouble(x, ri);
                    if (ri.get() || ri.get()) {
                        defined.set();
                    }
                    return Complex.valueOf(r, i);
                }
            }
        }
        return Complex.ZERO;
    }

//    @Override
//    public double computeDouble(double x, double y, double z) {
//        return computeComplexArg(x,y,z).toDouble();
//    }


}
