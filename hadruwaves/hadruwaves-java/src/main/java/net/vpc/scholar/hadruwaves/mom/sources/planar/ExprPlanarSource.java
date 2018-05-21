/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.sources.planar;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.dump.Dumper;
import net.vpc.scholar.hadrumaths.dump.Dumpable;
import net.vpc.scholar.hadruwaves.mom.sources.PlanarSource;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author vpc
 */
public class ExprPlanarSource implements PlanarSource, Dumpable, Cloneable {

    private DoubleToVector fct;
    private Complex characteristicImpedance;

    public ExprPlanarSource(Expr fct, Complex characteristicImpedance) {
        this.fct = fct.toDV();
        this.characteristicImpedance = characteristicImpedance;
    }


    public Complex getCharacteristicImpedance() {
        return characteristicImpedance;
    }

    public DoubleToVector getFunction() {
        return fct;
    }

  
    public Dumper getDumpHelper() {
        return new Dumper(this)
                .add("fct", fct)
                .add("characteristicImpedance", characteristicImpedance)
                ;
    }

    public String dump() {
        return getDumpHelper().toString();
    }

    @Override
    public PlanarSource clone() {
        try {
            ExprPlanarSource d = (ExprPlanarSource) super.clone();
            d.fct = (DoubleToVector) d.fct.clone();
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
