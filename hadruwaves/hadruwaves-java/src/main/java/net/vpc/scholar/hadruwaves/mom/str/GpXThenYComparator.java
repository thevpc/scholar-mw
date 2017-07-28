package net.vpc.scholar.hadruwaves.mom.str;

import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;

import java.io.Serializable;

public class GpXThenYComparator implements TestFunctionsComparator, Serializable {
    public GpXThenYComparator() {
    }

    public String dump() {
        Dumper d = new Dumper(this, Dumper.Type.SIMPLE);
        return d.toString();
    }

    public int compare(DoubleToVector o1, DoubleToVector o2) {
        String axis1 = (String) o1.getProperty("Axis");
        Integer axisIndex1 = (Integer) o1.getProperty("AxisIndex");
        String axis2 = (String) o2.getProperty("Axis");
        Integer axisIndex2 = (Integer) o2.getProperty("AxisIndex");
        int r;

        r = axis1.compareTo(axis2);
        if (r != 0) {
            return r;
        }

        r = axisIndex1.compareTo(axisIndex2);
        if (r != 0) {
            return r;
        }

        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }
}