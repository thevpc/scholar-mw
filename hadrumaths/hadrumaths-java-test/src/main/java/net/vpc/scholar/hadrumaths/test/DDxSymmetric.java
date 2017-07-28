//package net.vpc.scholar.math.functions.dfx;
//
//import java.util.Collections;
//import java.util.List;
//
//import net.vpc.scholar.math.AbstractExprPropertyAware;
//import net.vpc.scholar.math.Axis;
//import net.vpc.scholar.math.Expr;
//import net.vpc.scholar.math.functions.Domain;
//
//@Deprecated
//public class DDxSymmetric extends DDx {
//
//    private static final long serialVersionUID = -1010101010101001026L;
//    private DDx base;
//
//    public DDxSymmetric(DDx base) {
//        super(base.getDomain());
//        this.base = base;
//    }
//
//    public boolean isZero() {
//        return base.isZero();
//    }
//
//    public boolean isNaN() {
//        return base.isNaN();
//    }
//
//    public boolean isInfinite() {
//        return base.isInfinite();
//    }
//
//    public double computeDouble(double x) {
//        return base.computeDouble(domain.xmax - (x - domain.xmin));
//    }
//
//    public DDx mul(double factor, Domain newDomain) {
//        return new DDxSymmetric(base.mul(factor, newDomain));
//    }
//
//    public DDx toXOpposite() {
//        return new DDxSymmetric(base.toXOpposite());
//    }
//
//    public DDx toSymmetric() {
//        return base;
//    }
//
//    public DDx translate(double xDelta, double yDelta) {
//        return new DDxSymmetric(base.translate(xDelta, yDelta));
//    }
//
//    public DDx simplify() {
//        return new DDxSymmetric(base.simplify());
//    }
//
//    public DDx getBase() {
//        return base;
//    }
//
//    public boolean isInvariant(Axis axis) {
//        switch (axis) {
//            case X: {
//                return base.isInvariant(axis);
//            }
//        }
//        return true;
//    }
//
//    public List<Expr> getSubExpressions() {
//        return Collections.EMPTY_LIST;
//    }
//
//    @Override
//    public Expr setParam(String name, Expr value) {
//        Expr updated = base.setParam(name, value);
//        if(updated!=base){
//            DDxSymmetric e = new DDxSymmetric((DDx) updated);
//            AbstractExprPropertyAware.copyProperties(this, e);
//            return e;
//        }
//        return this;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof DDxSymmetric)) return false;
//        if (!super.equals(o)) return false;
//
//        DDxSymmetric that = (DDxSymmetric) o;
//
//        if (base != null ? !base.equals(that.base) : that.base != null) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (base != null ? base.hashCode() : 0);
//        return result;
//    }
//
//    @Override
//    public boolean isDoubleExpr() {
//        return true;
//    }
//}
