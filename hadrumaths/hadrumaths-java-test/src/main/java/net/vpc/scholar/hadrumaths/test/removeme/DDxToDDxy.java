//package net.vpc.scholar.math.functions.dfxy;
//
//import java.util.Arrays;
//import java.util.List;
//
//import net.vpc.scholar.math.*;
//import net.vpc.scholar.math.AbstractExprPropertyAware;
//import net.vpc.scholar.math.Expr;
//import net.vpc.scholar.math.functions.Domain;
//import net.vpc.scholar.math.functions.Range;
//
///**
// * Created by IntelliJ IDEA. User: vpc Date: 31 juil. 2005 Time: 18:03:00 To
// * change this template use File | Settings | File Templates.
// */
//public class DDxToDDxy extends DDxy {
//
//private static final long serialVersionUID = 1L;
//    private IDDx base;
//
//    public DDxToDDxy(IDDx base) {
//        this(base, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
//    }
//
//    public DDxToDDxy(IDDx base, double ymin, double ymax) {
//        super(Domain.forBounds(base.getDomain().xmin, base.getDomain().xmax, ymin, ymax));
//        this.base = base;
//    }
//
//    public boolean isDDx() {
//        return true;
//    }
//
//    public IDDx toDDx() {
//        return base;
//    }
//
//    //TODO isZero, isInifinite, isNaN
//    @Override
//    public boolean isZero() {
//        return false;
//    }
//
//    @Override
//    public boolean isInvariant(Axis axis) {
//        return false;
//    }
//
//    @Override
//    public double computeDouble(double x, double y) {
//        return base.computeDouble(x);
//    }
//
////    @Override
////    public DDxy multiply(double factor,DomainXY newDomain) {
////        return new DDxToDDxy(base.multiply(factor,newDomain==null?null: new DomainX(newDomain.xmin,newDomain.xmax)),domain.ymin,domain.ymax);
////    }
//    @Override
//    public DDxy toXOpposite() {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public DDxy toYOpposite() {
//        throw new UnsupportedOperationException();
//    }
//
////    @Override
////    public DDxy getSymmetricX() {
////        return new DDxToDDxy(base.toSymmetric(), getDomain().ymin, getDomain().ymax);
////    }
//    @Override
//    public DDxy getSymmetricY() {
//        throw new IllegalArgumentException("Not implemented");
//    }
//
////    @Override
////    public DDxy translate(double deltaX, double deltaY) {
////        return new DDxToDDxy(base.translate(deltaX, deltaY), getDomain().ymin, getDomain().ymax);
////    }
//    public IDDx getBaseFunction() {
//        return base;
//    }
//
//
//    public List<Expr> getSubExpressions() {
//        return Arrays.asList(new Expr[]{base});
//    }
//
//    @Override
//    public Expr setParam(String name, Expr value) {
//        IDDx base=this.base.setParam(name,value).toDDx();
//        if(base!=this.base){
//            DDxToDDxy e = new DDxToDDxy(base);
//            AbstractExprPropertyAware.copyProperties(this, e);
//            return e;
//        }
//        return this;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof DDxToDDxy)) return false;
//        if (!super.equals(o)) return false;
//
//        DDxToDDxy dDxToDDxy = (DDxToDDxy) o;
//
//        if (base != null ? !base.equals(dDxToDDxy.base) : dDxToDDxy.base != null) return false;
//        if (name != null ? !name.equals(dDxToDDxy.name) : dDxToDDxy.name != null) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (base != null ? base.hashCode() : 0);
//        result = 31 * result + (name != null ? name.hashCode() : 0);
//        return result;
//    }
//
//    @Override
//    public int getDomainDimension() {
//        return 1;
//    }
//
//    @Override
//    public double computeDouble0(double x) {
//        return base.computeDouble(x);
//    }
//
//    @Override
//    public double computeDouble0(double x,double y) {
//        return base.computeDouble(x);
//    }
//
//    @Override
//    public double computeDouble0(double x,double y,double z) {
//        return base.computeDouble(x);
//    }
//
//
//}
