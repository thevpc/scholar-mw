package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.BooleanMarker;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.format.impl.AbstractFormatter;

/**
 * Created by vpc on 8/21/14.
 */
public class FixedAxisZFunction extends AbstractDoubleToDouble implements Cloneable {
    private static final long serialVersionUID = 1L;

    static {
        FormatFactory.register(FixedAxisZFunction.class, new AbstractFormatter<FixedAxisZFunction>() {
            @Override
            public void format(StringBuilder sb, FixedAxisZFunction o, FormatParamSet format) {
                sb.append("FixedAxisZ(");
                FormatFactory.format(sb, o.z, format.remove(FormatFactory.REQUIRED_PARS));
                sb.append(", ");
                FormatFactory.format(sb, o.base, format.remove(FormatFactory.REQUIRED_PARS));
                sb.append(")");
            }
        });
    }

    private double z;
    private DoubleToDouble base;

    public FixedAxisZFunction(DoubleToDouble base, double z) {
        super(base.getDomain().toDomainXY());
        this.base = base;
        this.z = z;
    }

    @Override
    public boolean isZeroImpl() {
        return base.isZero();
    }

    @Override
    protected double computeDouble0(double x, BooleanMarker defined) {
        return base.computeDouble(x, defined);
    }

    @Override
    protected double computeDouble0(double x, double y, BooleanMarker defined) {
        return base.computeDouble(x, y, z, defined);
    }

    @Override
    protected double computeDouble0(double x, double y, double z0, BooleanMarker defined) {
        return base.computeDouble(x, y, z, defined);
    }

//    @Override
//    public String toString() {
//        return "FixedAxisZ(" +
//                "," + z +
//                ", " + base +
//                ')';
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FixedAxisZFunction)) return false;
        if (!super.equals(o)) return false;

        FixedAxisZFunction that = (FixedAxisZFunction) o;

        if (Double.compare(that.z, z) != 0) return false;
        if (base != null ? !base.equals(that.base) : that.base != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (base != null ? base.hashCode() : 0);
        return result;
    }
}
