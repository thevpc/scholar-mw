package net.vpc.scholar.hadruwaves.mom.sources.modal;

import net.vpc.scholar.hadrumaths.util.dump.Dumper;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadruwaves.mom.str.ModeInfoComparator;

/**
 * Created by vpc on 8/18/16.
 */
public class PropagatingIndexModeComparator implements ModeInfoComparator {
    private UserModalSources sources;

    public PropagatingIndexModeComparator(UserModalSources sources) {
        this.sources = sources;
    }

    public int compare(ModeInfo o1, ModeInfo o2) {
        int idx1 = sources.getPropagatingIndex(o1);
        int idx2 = sources.getPropagatingIndex(o2);
        if (idx1 >= 0 && idx2 >= 0) {
            if (idx1 > idx2) {
                return 1;
            } else if (idx1 < idx2) {
                return -1;
            } else {
                return 0;
            }
        } else if (idx1 >= 0 && idx2 < 0) {
            return -1;
        } else if (idx1 < 0 && idx2 >= 0) {
            return 1;
        }
        double f1 = o1.cutOffFrequency;
        double f2 = o2.cutOffFrequency;
        double i1 = o1.initialIndex;
        double i2 = o2.initialIndex;
        if (f1 > f2) {
            return 1;
        } else if (f1 < f2) {
            return -1;
        } else if (i1 > i2) {
            return 1;
        } else if (i1 < i2) {
            return -1;
        } else {
            return 0;
        }
    }
    public String dump() {
        return getDumpStringHelper().toString();
    }

    public Dumper getDumpStringHelper() {
        Dumper h=new Dumper(this,Dumper.Type.SIMPLE);
        h.add("sources", sources);
        return h;
    }

    @Override
    public String toString() {
        return dump();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PropagatingIndexModeComparator that = (PropagatingIndexModeComparator) o;

        return sources != null ? sources.equals(that.sources) : that.sources == null;
    }

    @Override
    public int hashCode() {
        return sources != null ? sources.hashCode() : 0;
    }
}
