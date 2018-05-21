/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadruwaves.mom.project.areamaterial;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadruwaves.mom.project.AbstractMomProjectItem;
import net.vpc.scholar.hadruwaves.mom.project.common.VarUnit;
import net.vpc.scholar.hadrumaths.util.config.Configuration;

/**
 *
 * @author vpc
 */
public class SurfaceImpedanceMaterial extends AbstractMomProjectItem implements AreaMaterial{
    private String valueExpression="0";

    public SurfaceImpedanceMaterial() {
    }

    public String getId() {
        return "SurfaceImpedanceMaterial";
    }

    public void load(Configuration conf, String key) {
        valueExpression = String.valueOf(conf.getObject(key + ".value", "0"));
    }

    public Complex getValue() {
        return (Complex)getContext().evaluate(valueExpression,VarUnit.NUMBER);
    }

    public void store(Configuration c, String key) {
        c.setString(key + ".value", valueExpression);
    }
    
    public String getName() {
        return "Surface Impedance";
    }
    
     @Override
    public void recompile() {
    }

    public String getValueExpression() {
        return valueExpression;
    }

    public void setValueExpression(String valueExpression) {
        this.valueExpression = valueExpression;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.valueExpression != null ? this.valueExpression.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SurfaceImpedanceMaterial other = (SurfaceImpedanceMaterial) obj;
        if (this.valueExpression != other.valueExpression && (this.valueExpression == null || !this.valueExpression.equals(other.valueExpression))) {
            return false;
        }
        return true;
    }
    
 
}
