package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Maths;
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
    private final double electricConductivity;

    public BoxSpace(BoxLimit limit, double epsr, double width,double electricConductivity) {
        if(limit==null){
            throw new IllegalArgumentException("Null Box Limit");
        }
        this.limit = limit;
        this.epsr = epsr;
        this.width = width;
        this.electricConductivity = electricConductivity;
    }

    public BoxSpace setLimit(BoxLimit limit) {
        return new BoxSpace(limit,epsr,width,electricConductivity);
    }

    public BoxSpace setWidth(double width) {
        return new BoxSpace(limit,epsr,width,electricConductivity);
    }

    public BoxSpace setEpsr(double epsr) {
        return new BoxSpace(limit,epsr,width,electricConductivity);
    }

    public BoxSpace setElectricConductivity(double electricConductivity) {
        return new BoxSpace(limit,epsr,width,electricConductivity);
    }

    public String dump() {
        Dumper h=new Dumper(getClass().getSimpleName(),Dumper.Type.SIMPLE);
        h.add("limit",limit);
        if (!limit.equals(BoxLimit.NOTHING) && !limit.equals(BoxLimit.MATCHED_LOAD)) {
            h.add("width", width);
        }

        if (!limit.equals(BoxLimit.NOTHING)) {
            h.add("epsr",epsr);
            if(electricConductivity !=0){
                h.add("electricConductivity", electricConductivity);
            }
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

    public Complex getEps(double freq) {
        if(electricConductivity ==0){
            return Complex.valueOf(epsr* Maths.EPS0);
        }else{
            return Complex.valueOf(epsr* Maths.EPS0, electricConductivity /(2*Math.PI*freq));
        }
    }

    public double getEpsr() {
        return epsr;
    }

    public double getElectricConductivity() {
        return electricConductivity;
    }
}
