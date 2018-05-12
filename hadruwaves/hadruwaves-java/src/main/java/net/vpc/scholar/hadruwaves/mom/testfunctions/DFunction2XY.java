package net.vpc.scholar.hadruwaves.mom.testfunctions;

import static java.lang.Math.abs;
import static java.lang.Math.round;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.DxySum;


public class DFunction2XY extends DxySum implements Cloneable{
    private static final long serialVersionUID = 1L;
    public double bandWidth;
    public double bandx;
    public DoubleToDouble band1;
    public DoubleToDouble band2;
    public Domain creationDomain;

    public DFunction2XY(Domain domain, double bandx, double bandWidth) {
        super();
        this.creationDomain = domain;
        this.bandx = bandx;
        this.bandWidth = bandWidth;
    }

    public double getBandWidth() {
        return bandWidth;
    }

    public double getBandx() {
        return bandx;
    }

    public void debugEssai(int i) {
        System.out.printf("Fonction %s<x=%s,w=%s> sur [%s,%s]\t:\t[%s,%s]+[%s,%s]\n",
                (i + 1),
                quotientAdisplay(getBandx(), bandWidth),
                bandWidth + "=" + quotientAdisplay(bandWidth, bandWidth),
                quotientAdisplay(getDomain().xmin(), bandWidth),
                quotientAdisplay(getDomain().xmax(), bandWidth),
                quotientAdisplay(band1.getDomain().xmin(), bandWidth),
                quotientAdisplay(band1.getDomain().xmax(), bandWidth),
                quotientAdisplay(band2.getDomain().xmin(), bandWidth),
                quotientAdisplay(band2.getDomain().xmax(), bandWidth));
    }
    public String quotientAdisplay(double d, double w) {
        int step = (int) (domain.xwidth() / w);
        return quotientAdisplayTry(d + domain.xwidth() / 2, domain.xwidth(), step + 300);
//        String stepStr = step == 1 ? "a" : "a/" + step;
//        double r = d / w;
//        if (r == 0.0) {
//            return "0";
//        } else if (r == 1.0) {
//            return stepStr;
//        } else if (((round(r) - r) / r) < 1E-4) {
//            return ((int) r) + stepStr;
//        } else {
//            return r + stepStr;
//        }
    }

    public static String quotientAdisplayTry(double value, double a, int maxIterations) {
        if (value == 0.0) {
            return "0";
        } else {
            for (int i = 1; i < maxIterations; i++) {
                double w = value * i / a;
                if (abs((round(w) - w) / w) < 1E-4) {
                    StringBuilder sb = new StringBuilder();
                    int wi = (int) w;
                    if (wi == 1) {
                        //nothing
                    } else if (wi == -1) {
                        sb.append("-");
                    } else {
                        sb.append(wi);
                    }
                    sb.append("a");
                    if (i > 1) {
                        sb.append("/").append(i);
                    }
                    return sb.toString();
                }
            }
            return value + "a";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DFunction2XY)) return false;
        if (!super.equals(o)) return false;

        DFunction2XY that = (DFunction2XY) o;

        if (Double.compare(that.bandWidth, bandWidth) != 0) return false;
        if (Double.compare(that.bandx, bandx) != 0) return false;
        if (band1 != null ? !band1.equals(that.band1) : that.band1 != null) return false;
        if (band2 != null ? !band2.equals(that.band2) : that.band2 != null) return false;
        if (creationDomain != null ? !creationDomain.equals(that.creationDomain) : that.creationDomain != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(bandWidth);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(bandx);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (band1 != null ? band1.hashCode() : 0);
        result = 31 * result + (band2 != null ? band2.hashCode() : 0);
        result = 31 * result + (creationDomain != null ? creationDomain.hashCode() : 0);
        return result;
    }
}
