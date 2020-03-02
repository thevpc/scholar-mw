package net.vpc.scholar.hadruwaves.mom.str;

import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectBuilder;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;

import java.io.Serializable;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;

public class GpDomainComparator implements TestFunctionsComparator, Serializable {
    private boolean compareXFirst;
    public GpDomainComparator() {
        this(true);
    }
    public GpDomainComparator(boolean compareXFirst) {
        this.compareXFirst=compareXFirst;
    }

    public int compare(DoubleToVector o1, DoubleToVector o2) {
        Domain index1 = lookupFirstDomain(o1);
        Domain index2 = lookupFirstDomain(o2);
        return compare(index1,index2);
    }

    private Domain lookupFirstDomain(DoubleToVector f) {
        DoubleToDouble[] all={f.getComponent(Axis.X).toDC().getRealDD(),f.getComponent(Axis.Y).toDC().getRealDD(),f.getComponent(Axis.X).toDC().getImagDD(),f.getComponent(Axis.Y).toDC().getImagDD()};
        Domain o=null;
        for (DoubleToDouble dFunctionXY : all) {
            if(o==null || compare(o,dFunctionXY.getDomain())>0){
                o=dFunctionXY.getDomain();
            }
        }
        return o;
    }

    private int compare(Domain domain1, Domain domain2) {
        int r;
        if(compareXFirst){
            r=compareX(domain1,domain2);
            if(r!=0){
                return r;
            }
            return compareY(domain1,domain2);
        }else{
            r=compareY(domain1,domain2);
            if(r!=0){
                return r;
            }
            return compareX(domain1,domain2);
        }
    }
    private int compareX(Domain domain1, Domain domain2) {
        if (domain1.xmin() < domain2.xmin()) {
            return -1;
        } else if (domain1.xmin() > domain2.xmin()) {
            return 1;
        }
        if (domain1.xmax() < domain2.xmax()) {
            return -1;
        } else if (domain1.xmax() > domain2.xmax()) {
            return 1;
        }
        return 0;
    }

    private int compareY(Domain domain1, Domain domain2) {
        if (domain1.ymin() < domain2.ymin()) {
            return -1;
        } else if (domain1.ymin() > domain2.ymin()) {
            return 1;
        }
        if (domain1.ymax() < domain2.ymax()) {
            return -1;
        } else if (domain1.ymax() > domain2.ymax()) {
            return 1;
        }
        return 0;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder h = Tson.obj(getClass().getSimpleName());
        h.add("compareXFirst", context.elem(compareXFirst));
        return h.build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GpDomainComparator that = (GpDomainComparator) o;

        return compareXFirst == that.compareXFirst;
    }

    @Override
    public int hashCode() {
        return (compareXFirst ? 1 : 0);
    }
}