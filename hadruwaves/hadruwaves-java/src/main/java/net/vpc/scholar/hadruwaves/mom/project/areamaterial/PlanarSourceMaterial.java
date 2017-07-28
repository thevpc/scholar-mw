/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.project.areamaterial;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadruwaves.mom.project.AbstractMomProjectItem;
import net.vpc.scholar.hadrumaths.util.Configuration;

/**
 *
 * @author vpc
 */
public class PlanarSourceMaterial extends AbstractMomProjectItem implements AreaMaterial {

    private String caracteristicImpedanceExpression="50";
    private String xvalueExpression="1";
    private String yvalueExpression="1";
    private Axis polarization;

    public PlanarSourceMaterial() {
    }

    public void load(Configuration conf, String key) {
        caracteristicImpedanceExpression = String.valueOf(conf.getObject(key + ".caracteristicImpedance", "50"));
        xvalueExpression = String.valueOf(conf.getObject(key + ".xvalueExpression", "1"));
        yvalueExpression = String.valueOf(conf.getObject(key + ".yvalueExpression", "1"));
        polarization = conf.getString(key + ".polarization")==null?null:Axis.valueOf(conf.getString(key + ".polarization"));
        //
    }

    public void store(Configuration c, String key) {
        c.setString(key + ".caracteristicImpedance", caracteristicImpedanceExpression);
        c.setString(key + ".xvalue", xvalueExpression);
        c.setString(key + ".yvalue", yvalueExpression);
        c.setString(key + ".polarization", polarization==null?null:polarization.toString());
    }

    public String getId() {
        return "PlanarSourceMaterial";
    }

    public String getName() {
        return "Planar Source";
    }

    public String getCaracteristicImpedanceExpression() {
        return caracteristicImpedanceExpression;
    }

    public void setCaracteristicImpedanceExpression(String caracteristicImpedanceExpression) {
        this.caracteristicImpedanceExpression = caracteristicImpedanceExpression;
    }
    


      
    public Complex getCaracteristicImpedance() {
        return getContext().evaluateComplex(caracteristicImpedanceExpression);
    }
    public double getXValue() {
        return getContext().evaluateDouble(xvalueExpression);
    }
    public double getYValue() {
        return getContext().evaluateDouble(yvalueExpression);
    }

    public Axis getPolarization() {
        return polarization;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PlanarSourceMaterial other = (PlanarSourceMaterial) obj;
        if (this.caracteristicImpedanceExpression != other.caracteristicImpedanceExpression && (this.caracteristicImpedanceExpression == null || !this.caracteristicImpedanceExpression.equals(other.caracteristicImpedanceExpression))) {
            return false;
        }
        if (this.xvalueExpression != other.xvalueExpression && (this.xvalueExpression == null || !this.xvalueExpression.equals(other.xvalueExpression))) {
            return false;
        }
        if (this.yvalueExpression != other.yvalueExpression && (this.yvalueExpression == null || !this.yvalueExpression.equals(other.yvalueExpression))) {
            return false;
        }
        if (this.polarization != other.polarization) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (this.caracteristicImpedanceExpression != null ? this.caracteristicImpedanceExpression.hashCode() : 0);
        hash = 29 * hash + (this.xvalueExpression != null ? this.xvalueExpression.hashCode() : 0);
        hash = 29 * hash + (this.yvalueExpression != null ? this.yvalueExpression.hashCode() : 0);
        hash = 29 * hash + (this.polarization != null ? this.polarization.hashCode() : 0);
        return hash;
    }


    
    public void setPolarization(Axis polarization) {
        this.polarization = polarization;
    }

    public String getXvalueExpression() {
        return xvalueExpression;
    }

    public void setXvalueExpression(String xvalueExpression) {
        this.xvalueExpression = xvalueExpression;
    }

    public String getYvalueExpression() {
        return yvalueExpression;
    }

    public void setYvalueExpression(String yvalueExpression) {
        this.yvalueExpression = yvalueExpression;
    }
}
