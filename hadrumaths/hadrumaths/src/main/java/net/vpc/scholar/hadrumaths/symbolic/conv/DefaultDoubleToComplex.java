package net.vpc.scholar.hadrumaths.symbolic.conv;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Plus;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Minus;
import net.vpc.scholar.hadrumaths.util.internal.NonStateField;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.ExprDefaults;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;

import java.util.Arrays;
import java.util.List;

//import net.vpc.scholar.math.functions.dfxy.DDxToDDxy;

/**
 * User: taha Date: 2 juil. 2003 Time: 10:39:39
 */
public class DefaultDoubleToComplex implements DoubleToComplex {

    private static final long serialVersionUID = 1L;
    private static final DefaultDoubleToComplex ZERO = new DefaultDoubleToComplex(Maths.DZEROXY);
    private static final int Z_NONE = 0;
    private static final int Z_R = 1;
    private static final int Z_I = 2;
    private static final int Z_RI = 3;

    static {
        FormatFactory.register(DefaultDoubleToComplex.class, new ObjectFormat() {
            @Override
            public void format(Object o, ObjectFormatContext context) {
                context.append("(");
                DefaultDoubleToComplex dc = (DefaultDoubleToComplex) o;
                context.format(dc.real);
//                context.append(" + î *");
                if (!dc.imag.isZero()) {
                    context.append("+î*");
                    context.format(dc.imag, context.getParams().add(FormatFactory.REQUIRED_PARS));
                }
                context.append(")");
            }
        });
    }

    protected DoubleToDouble real;
    protected DoubleToDouble imag;
    protected Domain domain;
    //    public static final CFunctionXY ZERO =new CFunctionXY(DCstFunctionXY.ZERO,DCstFunctionXY.ZERO).setName("0");

    //    public DefaultDoubleToComplex(IDDx real) {
//        this(new DDxToDDxy(real));
//    }
    @NonStateField
    protected int zStatus = 0;
    @NonStateField
    protected int eagerHash = 0;

    public DefaultDoubleToComplex(DoubleToDouble real) {
        this(real.getDomain(), real, Maths.DZEROX);
//        name = (real.getName());
    }

    protected DefaultDoubleToComplex(Domain domain, DoubleToDouble real, DoubleToDouble imag) {
        this.domain = domain;
        this.real = real.toDD();
        this.imag = imag.toDD();
        if (!this.real.isZero()) {
            zStatus |= 1;
        }
        if (!this.imag.isZero()) {
            zStatus |= 2;
        }
//        name = (this.real.getName() + "+i." + this.imag.getName());
    }

    public DefaultDoubleToComplex(DoubleToDouble real, DoubleToDouble imag) {
        this(
                real.getDomain().intersect(imag.getDomain()),
                real,
                imag
        );
//        name = (real.getName());
    }

    public static DefaultDoubleToComplex of(DoubleToDouble real) {
        if (real == null || real.isZero()) {
            return ZERO;
        }
        return new DefaultDoubleToComplex(real);
    }

    public boolean isInvariant(Axis axis) {
        return real.isInvariant(axis) && imag.isInvariant(axis);
    }

    @Override
    public Complex toComplex() {
        return Complex.of(
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
    public Expr narrow(ExprType other) {
        switch (other) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                if (imag.isZero()) {
                    return real.narrow(other);
                }
                break;
            }
        }
        return ExprDefaults.narrow(this, other);
    }

    public boolean isZero() {
        return real.isZero() && imag.isZero();
    }

    public List<Expr> getChildren() {
        return Arrays.asList(new Expr[]{real, imag});
    }

    public boolean isInfinite() {
        return getRealDD().isInfinite() || getImagDD().isInfinite();
    }

    @Override
    public Expr compose(Expr xreplacement, Expr yreplacement, Expr zreplacement) {
        DoubleToDouble real = this.real.compose(xreplacement, yreplacement, zreplacement).toDD();
        DoubleToDouble imag = this.imag.compose(xreplacement, yreplacement, zreplacement).toDD();
        if (!real.equals(this.real) || !(imag.equals(this.imag))) {
            Expr e = DefaultDoubleToComplex.of(real, imag);
            e = ExprDefaults.copyProperties(this, e);
            return e;
        }
        return this;
    }

    public static DefaultDoubleToComplex of(DoubleToDouble real, DoubleToDouble imag) {
        if ((real == null || real.isZero()) && (imag == null || imag.isZero())) {
            return ZERO;
        }
        return new DefaultDoubleToComplex(real, imag);
    }

    public DoubleToComplex conj() {
        return Maths.complex(real, Maths.mul(Maths.expr(-1, Domain.FULL(getDomain().getDimension())), imag).toDD());
    }

    public Domain getDomain() {
        return domain;
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return new DefaultDoubleToComplex(subExpressions[0].toDD(), subExpressions[1].toDD());
    }

    public DoubleToDouble getRealDD() {
        return real;
    }

    public DoubleToDouble getImagDD() {
        return imag;
    }

    @Override
    public Complex evalComplex(double x, BooleanMarker defined) {
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
                    r = real.evalDouble(x, ro);
                    if (ro.get()) {
                        defined.set();
                        return Complex.of(r);
                    }
                    break;
                }
                case Z_I: {
                    i = imag.evalDouble(x, ri);
                    if (ri.get()) {
                        defined.set();
                        return Complex.I(i);
                    }
                    break;
                }
                case Z_RI: {
                    r = real.evalDouble(x, ro);
                    i = imag.evalDouble(x, ri);
                    if (ri.get() || ri.get()) {
                        defined.set();
                        return Complex.of(r, i);
                    }
                    break;
                }
            }
        }
        return Complex.ZERO;
    }

    public Complex evalComplex(double x, double y, BooleanMarker defined) {
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
                    r = real.evalDouble(x, y, ro);
                    if (ro.get()) {
                        defined.set();
                        return Complex.of(r);
                    }
                    break;
                }
                case Z_I: {
                    i = imag.evalDouble(x, y, ri);
                    if (ri.get()) {
                        defined.set();
                        return Complex.I(i);
                    }
                    break;
                }
                case Z_RI: {
                    r = real.evalDouble(x, y, ro);
                    i = imag.evalDouble(x, y, ri);
                    if (ri.get() || ro.get()) {
                        defined.set();
                        return Complex.of(r, i);
                    }
                    break;
                }
            }
        }
        return Complex.ZERO;
    }

    @Override
    public Complex evalComplex(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            BooleanRef ro = BooleanMarker.ref();
            BooleanRef ri = BooleanMarker.ref();
            double r;
            double i;
            switch (zStatus) {
                case Z_NONE: {
                    defined.set();
                    break;
                }
                case Z_R: {
                    r = real.evalDouble(x, y, z, ro);
                    if (ro.get()) {
                        defined.set();
                        return Complex.of(r);
                    }
                    break;
                }
                case Z_I: {
                    i = imag.evalDouble(x, y, z, ri);
                    if (ri.get()) {
                        defined.set();
                        return Complex.I(i);
                    }
                    break;
                }
                case Z_RI: {
                    r = real.evalDouble(x, y, z, ro);
                    i = imag.evalDouble(x, y, z, ri);
                    if (ri.get() || ri.get()) {
                        defined.set();
                        return Complex.of(r, i);
                    }
                    break;
                }
            }
        }
        return Complex.ZERO;
    }

    @Override
    public ExprType getNarrowType() {
        if (imag.isZero()) {
            return real.getNarrowType();
        }
        return ExprType.DOUBLE_COMPLEX;
    }

    @Override
    public DoubleToComplex toDC() {
        return this;
    }

    public DoubleToDouble toDD() {
        if (imag.isZero()) {
            return real;
        }
        throw new UnsupportedOperationException("[" + getClass().getName() + "]" + "Not supported yet.");
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
    }

    public DoubleToComplex add(DoubleToComplex other) {
        return ExprDefaults.add(this, other).toDC();
//        return new DefaultDoubleToComplex(real.add(other.real), imag.add(other.imag));
    }

    public Domain intersect(DefaultDoubleToComplex other) {
        return domain.intersect(other.domain);
    }

    public Domain intersect(DefaultDoubleToComplex other, Domain domain) {
//        return Domain.intersect(domain, other.domain, domain);
        return domain.intersect(other.domain);
    }

//    @Override
//    public ComplexMatrix toMatrix() {
//        throw new ClassCastException();
//    }

    @Override
    public int hashCode() {
        if(eagerHash==0) {
            int hash = 292480294;
            hash = 97 * hash + this.real.hashCode();
            hash = 97 * hash + this.imag.hashCode();
            hash = 97 * hash + (this.domain != null ? this.domain.hashCode() : 0);
            eagerHash= hash;
        }
        return eagerHash;
    }

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
        if (obj == null || !(obj instanceof DefaultDoubleToComplex)) {
            return false;
        }
        DefaultDoubleToComplex c = (DefaultDoubleToComplex) obj;
        return c.imag.equals(imag) && c.real.equals(real);
    }

    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }

    @Override
    public String toLatex() {
        if(imag.isZero()){
            return real.toLatex();
        }
        if(real.isZero()){
            String s = imag.toLatex();
            if(s.startsWith("-") || imag instanceof Plus || imag instanceof Minus){
                s="\\left({"+s+"}\\right)";
            }
            return "î*"+s;
        }
        StringBuilder sb=new StringBuilder();
        sb.append(real.toLatex());
        String s = imag.toLatex();
        if(s.startsWith("-") || imag instanceof Plus || imag instanceof Minus){
            s="\\left({"+s+"}\\right)";
        }
        sb.append("+").append(s);
        return sb.toString();
    }

}
