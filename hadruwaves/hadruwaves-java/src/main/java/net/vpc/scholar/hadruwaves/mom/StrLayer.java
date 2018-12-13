/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.util.dump.Dumpable;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;

/**
 *
 * @author vpc
 */
public class StrLayer implements Dumpable, Cloneable, Comparable<StrLayer> {

    public static final StrLayer[] NO_LAYERS = new StrLayer[0];
    public double minZ;
    public double width;
    public Complex impedance;
    public String name="None";

    public StrLayer(double minZ, double width, Complex impedance) {
        this.minZ = minZ;
        this.width = width;
        this.impedance = impedance;
    }

    public String dump() {
        Dumper h = new Dumper(this, Dumper.Type.SIMPLE);
        h.add("minZ", minZ);
        h.add("width", width);
        h.add("impedance", impedance);
        return h.toString();
    }

    @Override
    public StrLayer clone() {
        try {
            return (StrLayer) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(StrLayer.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalArgumentException();
        }
    }

    public int compareTo(StrLayer o) {
        double r = this.minZ - o.minZ;
        if (r == 0) {
            r = this.width - o.width;
            if (r == 0) {
                r = this.impedance.sub(o.impedance).absdbl();
            }
        }
        return r<0?-1:r>0?1:0;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof StrLayer) &&  (compareTo((StrLayer)obj)==0);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.minZ) ^ (Double.doubleToLongBits(this.minZ) >>> 32));
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.width) ^ (Double.doubleToLongBits(this.width) >>> 32));
        hash = 17 * hash + (this.impedance != null ? this.impedance.hashCode() : 0);
        return hash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Complex getImpedance() {
        return impedance;
    }

    public double getMinZ() {
        return minZ;
    }

    public double getWidth() {
        return width;
    }
    
    

    
}
