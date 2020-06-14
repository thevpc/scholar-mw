//package net.vpc.scholar.hadrumaths.symbolic.double2double;
//
//import net.vpc.scholar.hadrumaths.BooleanMarker;
//import net.vpc.scholar.hadrumaths.Domain;
//import net.vpc.scholar.hadrumaths.Expr;
//import net.vpc.scholar.hadrumaths.FormatFactory;
//import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
//import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
//import net.vpc.scholar.hadrumaths.format.impl.AbstractObjectFormat;
//import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
//
//import java.util.Arrays;
//import java.util.List;
//
///**
// * Created by vpc on 8/21/14.
// */
//public class FixedAxisZFunction extends AbstractDoubleToDouble {
//    private static final long serialVersionUID = 1L;
//
//    static {
//        FormatFactory.register(FixedAxisZFunction.class, new AbstractObjectFormat<FixedAxisZFunction>() {
//            @Override
//            public void format(StringBuilder sb, FixedAxisZFunction o, ObjectFormatParamSet format, ObjectFormatContext context) {
//                sb.append("FixedAxisZ(");
//                FormatFactory.format(sb, o.z, format.remove(FormatFactory.REQUIRED_PARS));
//                sb.append(", ");
//                FormatFactory.format(sb, o.base, format.remove(FormatFactory.REQUIRED_PARS));
//                sb.append(")");
//            }
//        });
//    }
//
//    private final double z;
//    private final DoubleToDouble base;
//    private final Domain domain;
//
//    public FixedAxisZFunction(DoubleToDouble base, double z) {
//        this.base = base;
//        this.z = z;
//        this.domain = base.getDomain().toDomainXY();
//    }
//
//
//    @Override
//    public Domain getDomain() {
//        return this.domain;
//    }
//
//    @Override
//    public List<Expr> getChildren() {
//        return Arrays.asList(base);
//    }
//
//    @Override
//    public boolean isZero() {
//        return base.isZero();
//    }
//
//    @Override
//    public Expr setParam(String name, Expr value) {
//        return new FixedAxisZFunction(base.setParam(name, value).toDD(), z);
//    }
//
//    @Override
//    public Expr newInstance(Expr... subExpressions) {
//        return new FixedAxisZFunction(subExpressions[0].toDD(), z);
//    }
//
//    @Override
//    public double computeDouble(double x, BooleanMarker defined) {
//        return base.computeDouble(x, defined);
//    }
//
//    @Override
//    public double computeDouble(double x, double y, BooleanMarker defined) {
//        return base.computeDouble(x, y, z, defined);
//    }
//
////    @Override
////    public String toString() {
////        return "FixedAxisZ(" +
////                "," + z +
////                ", " + base +
////                ')';
////    }
//
//    @Override
//    public double computeDouble(double x, double y, double z0, BooleanMarker defined) {
//        return base.computeDouble(x, y, z, defined);
//    }
//
//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        long temp;
//        temp = Double.doubleToLongBits(z);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        result = 31 * result + (base != null ? base.hashCode() : 0);
//        return result;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof FixedAxisZFunction)) return false;
//        if (!super.equals(o)) return false;
//
//        FixedAxisZFunction that = (FixedAxisZFunction) o;
//
//        if (Double.compare(that.z, z) != 0) return false;
//        return base != null ? base.equals(that.base) : that.base == null;
//    }
//}
