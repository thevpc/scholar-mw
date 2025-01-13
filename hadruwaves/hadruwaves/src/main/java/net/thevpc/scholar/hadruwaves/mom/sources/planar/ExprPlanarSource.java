/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.mom.sources.planar;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectBuilder;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.geom.Geometry;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadruwaves.mom.sources.PlanarSource;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author vpc
 */
public class ExprPlanarSource implements PlanarSource, Cloneable {

    private DoubleToVector fct;
    private Complex characteristicImpedance;
    private Geometry geometry;

    public ExprPlanarSource(Expr fct, Complex characteristicImpedance,Geometry geometry) {
        this.fct = fct.toDV();
        if(geometry==null){
            geometry=fct.getDomain().toGeometry();
        }
        if(characteristicImpedance==null){
            characteristicImpedance=Complex.of(50);
        }
        this.geometry = geometry;
        this.characteristicImpedance = characteristicImpedance;
    }

    @Override
    public Geometry getGeometry() {
        return geometry;
    }

    public Complex getCharacteristicImpedance() {
        return characteristicImpedance;
    }

    public DoubleToVector getFunction() {
        return fct;
    }


    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder h = Tson.ofObj(getClass().getSimpleName());
        h.add("fct", context.elem(fct));
        h.add("z0", context.elem(characteristicImpedance));
        return h.build();
    }

    @Override
    public PlanarSource clone() {
        try {
            ExprPlanarSource d = (ExprPlanarSource) super.clone();
//            d.fct = (DoubleToVector) d.fct.clone();
            return d;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(ExprPlanarSource.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public DoubleToVector getFct() {
        return fct;
    }

    public ExprPlanarSource setFct(DoubleToVector fct) {
        this.fct = fct;
        return this;
    }

    public ExprPlanarSource setCharacteristicImpedance(Complex characteristicImpedance) {
        this.characteristicImpedance = characteristicImpedance;
        return this;
    }
}
