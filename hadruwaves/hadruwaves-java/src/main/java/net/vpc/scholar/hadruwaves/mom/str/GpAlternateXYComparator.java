package net.vpc.scholar.hadruwaves.mom.str;

import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;

import java.io.Serializable;

public class GpAlternateXYComparator implements TestFunctionsComparator, Serializable {
    public GpAlternateXYComparator() {
    }

    public int compare(DoubleToVector o1, DoubleToVector o2) {
        String axis1=(String) o1.getProperty("Axis");
        Integer axisIndex1=(Integer) o1.getProperties().get("AxisIndex");
        String axis2=(String) o2.getProperties().get("Axis");
        Integer axisIndex2=(Integer) o2.getProperties().get("AxisIndex");
        int r;

        r=axisIndex1.compareTo(axisIndex2);
        if(r!=0){
            return r;
        }

        r= axis1.compareTo(axis2);
        if(r!=0){
            return r;
        }
        return 0;
    }

    @Override
    public String toString() {
        return dump();
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.function(getClass().getSimpleName()).build();
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
