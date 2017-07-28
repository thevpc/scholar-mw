package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.util.dump.Dumpable;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 20:45:41
 */
public final class BoxSpace implements Dumpable {
    private final BoxLimit limit;
    private final double width;
    private final double epsr;

    public BoxSpace(BoxLimit limit, double epsr, double width) {
        this.limit = limit;
        this.epsr = epsr;
        this.width = width;
    }

    

    public String dump() {
        Dumper h=new Dumper(getClass().getSimpleName(),Dumper.Type.SIMPLE);
        h.add("limit",limit);
        if (!limit.equals(BoxLimit.NOTHING) && !limit.equals(BoxLimit.MATCHED_LOAD)) {
            h.add("width", width);
        }

        if (!limit.equals(BoxLimit.NOTHING)) {
            h.add("epsr",epsr);
        }
        return h.toString();
    }

    @Override
    public String toString() {
        return dump();
    }

    public BoxLimit getLimit() {
        return limit;
    }

    public double getWidth() {
        return width;
    }

    public double getEpsr() {
        return epsr;
    }
}
