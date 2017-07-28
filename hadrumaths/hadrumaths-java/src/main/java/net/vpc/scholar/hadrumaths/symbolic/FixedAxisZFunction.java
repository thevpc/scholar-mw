package net.vpc.scholar.hadrumaths.symbolic;

/**
* Created by vpc on 8/21/14.
*/
public class FixedAxisZFunction extends AbstractDoubleToDouble implements Cloneable{
    private double z;
    private DoubleToDouble base;
    public FixedAxisZFunction(DoubleToDouble base, double z) {
        super(base.getDomain().toDomainXY());
        this.base=base;
        this.z=z;
    }

    @Override
    protected double computeDouble0(double x) {
        return base.computeDouble(x);
    }

    @Override
    protected double computeDouble0(double x, double y) {
        return base.computeDouble(x, y, z);
    }

    @Override
    protected double computeDouble0(double x, double y, double z0) {
        return base.computeDouble(x, y, z);
    }

    @Override
    public String toString() {
        return "FixedAxisZFunction{" +
                "z=" + z +
                ", base=" + base +
                '}';
    }

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
