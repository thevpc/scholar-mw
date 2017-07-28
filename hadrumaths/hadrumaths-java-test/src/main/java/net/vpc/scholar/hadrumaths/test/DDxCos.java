//package net.vpc.scholar.math.functions.dfx;
//
//import net.vpc.scholar.math.Axis;
//import net.vpc.scholar.math.Expr;
//import net.vpc.scholar.math.Out;
//import net.vpc.scholar.math.functions.Domain;
//import net.vpc.scholar.math.functions.Range;
//import net.vpc.scholar.math.transform.*;
//
//import java.util.Collections;
//import java.util.List;
//
//public class DDxCos extends DDx {
//
//    static {
//        ExpressionTransformFactory.setExpressionTransformer(DDxCos.class, DomainXYMultiplierTransform.class, new ExpressionTransformer() {
//
//            public Expr transform(Expr expression, ExpressionTransform transform) {
//                DDxCos e = (DDxCos) expression;
//                DomainXYMultiplierTransform t = (DomainXYMultiplierTransform) transform;
//                return new DDxCos(
//                        t.getValue() == null ? e.domain : e.domain.intersect(t.getValue().getDomainX()),
//                        e.amp, e.a, e.b
//                );
//            }
//        });
//        ExpressionTransformFactory.setExpressionTransformer(DDxCos.class, DomainXYMultiplierTransform.class, new ExpressionTransformer() {
//
//            public Expr transform(Expr expression, ExpressionTransform transform) {
//                DDxCos e = (DDxCos) expression;
//                DoubleMultiplierTransform t = (DoubleMultiplierTransform) transform;
//                return new DDxCos(
//                        e.domain,
//                        e.amp * t.getValue(), e.a, e.b
//                );
//            }
//        });
//    }
//
//    private static final long serialVersionUID = -1010101010101001041L;
//    private double amp;
//    private double a;
//    private double b;
//
//    public DDxCos(Domain domain, double amp, double a, double b) {
//        super(domain);
//        this.amp = amp;
//        this.a = a;
//        this.b = b;
//    }
//
//    public double computeDouble(double x) {
//        if (domain.contains(x)) {
//            return amp * Math.cos(a * x + b);
//        }
//        return 0;
//    }
//
//    public boolean isInvariant(Axis axis) {
//        switch (axis) {
//            case X: {
//                return amp == 0 || a == 0;
//            }
//        }
//        return true;
//    }
//
//    public boolean isInfinite() {
//        return Double.isInfinite(amp);
//    }
//
//    public boolean isNaN() {
//        return Double.isNaN(amp) || Double.isNaN(a) || Double.isNaN(b) || Double.isInfinite(a) || Double.isInfinite(b);
//    }
//
//    public boolean isZero() {
//        return amp == 0 || (a == 0 && Math.cos(b) == 0);
//    }
//
//    @Override
//    public double[] computeDouble(double[] x, Domain d0, Out<Range> range) {
//        //todo taha
//
//        double[] y = new double[x.length];
//        int[] ab = (d0 == null ? domain : domain.intersect(d0)).rangeArray(x);
//
//        if (ab != null) {
//            int a = ab[0];
//            int b = ab[1];
//            for (int i = a; i <= b; i++) {
//                double xx = x[i];
//                y[i] = Math.cos(this.a * xx + this.b);
//            }
//            if (range != null) {
//                range.set(Range.forBounds(a, b));
//            }
//        }
//        return y;
//    }
//
//    public DDx transformLinear(double a0x, double b0) {
//        DDxLinear l = (DDxLinear) new DDxLinear(domain, a, b).transformLinear(a0x, b0);
//        return new DDxCos(
//                domain.transformLinear(a0x, b0),
//                amp,
//                l.getA(),
//                l.getB()
//        );
//    }
//
//    public double getA() {
//        return a;
//    }
//
//    public double getB() {
//        return b;
//    }
//
//    public double getAmp() {
//        return amp;
//    }
//
//    public List<Expr> getSubExpressions() {
//        return Collections.EMPTY_LIST;
//    }
//
//    @Override
//    public Expr setParam(String name, Expr value) {
//        return this;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof DDxCos)) return false;
//        if (!super.equals(o)) return false;
//
//        DDxCos dDxCos = (DDxCos) o;
//
//        if (Double.compare(dDxCos.a, a) != 0) return false;
//        if (Double.compare(dDxCos.amp, amp) != 0) return false;
//        if (Double.compare(dDxCos.b, b) != 0) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        long temp;
//        temp = Double.doubleToLongBits(amp);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        temp = Double.doubleToLongBits(a);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        temp = Double.doubleToLongBits(b);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        return result;
//    }
//
//    @Override
//    public boolean isDoubleExpr() {
//        return false;
//    }
//}
