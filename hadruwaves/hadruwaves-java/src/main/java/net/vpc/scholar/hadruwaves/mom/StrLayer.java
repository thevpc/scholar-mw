/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.vpc.common.tson.*;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.HSerializable;
import net.vpc.scholar.hadruwaves.Physics;
import net.vpc.scholar.hadruwaves.util.Impedance;

/**
 *
 * @author vpc
 */
public class StrLayer implements HSerializable, Cloneable {

    public static final StrLayer[] NO_LAYERS = new StrLayer[0];
    private final double width;
    private final Impedance impedance;
    private final String name;

    public StrLayer(double width, Complex impedance) {
        this(width,impedance,null);
    }

    public StrLayer(double width, Complex impedance,String name) {
        this.width = width;
        this.impedance = Physics.impedance(impedance);
        this.name = name==null?"None":name;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonFunctionBuilder sb = Tson.function(getClass().getSimpleName());
        sb.add(Tson.pair("width", context.elem(width)));
        sb.add(Tson.pair("impedance", context.elem(impedance)));
        return sb.build();
    }
//    public String dump() {
//        Dumper h = new Dumper(this, Dumper.Type.SIMPLE);
//        h.add("width", width);
//        h.add("impedance", impedance);
//        return h.toString();
//    }

    @Override
    public StrLayer clone() {
        try {
            return (StrLayer) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(StrLayer.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalArgumentException();
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StrLayer strLayer = (StrLayer) o;
        return Double.compare(strLayer.width, width) == 0 &&
                Objects.equals(impedance, strLayer.impedance) &&
                Objects.equals(name, strLayer.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, impedance, name);
    }

    public String getName() {
        return name;
    }

    public Impedance getImpedance() {
        return impedance;
    }

    public double getWidth() {
        return width;
    }
    
}
