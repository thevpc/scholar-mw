/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.mom.project;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.util.config.Configuration;

/**
 *
 * @author vpc
 */
public class MomProjectExtraLayer implements Cloneable, Comparable<MomProjectExtraLayer>, MomProjectItem {

    private String minZExpression;
    private String widthExpression;
    private String impedanceExpression;
    private String name;
    private MomProject context;

    public MomProjectExtraLayer() {
    }

    public MomProjectExtraLayer(String name,String impedanceExpression,String minZExpression, String widthExpression) {
        this.minZExpression = minZExpression;
        this.widthExpression = widthExpression;
        this.impedanceExpression = impedanceExpression;
        this.name = name;
        if(this.name==null){
            this.name="NONAME";
        }
    }
    

    
    @Override
    public MomProjectExtraLayer clone() {
        try {
            MomProjectExtraLayer lay=(MomProjectExtraLayer) super.clone();
            lay.context=null;
            return lay;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MomProjectExtraLayer.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalArgumentException();
        }
    }

    public int compareTo(MomProjectExtraLayer o) {
        double r = this.getMinZ() - o.getMinZ();
        if (r == 0) {
            r = this.getWidth() - o.getWidth();
            if (r == 0) {
                r = this.getImpedance().minus(o.getImpedance()).absdbl();
            }
        }
        return r<0?-1:r>0?1:0;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Complex getImpedance() {
        return getContext().evaluateComplex(impedanceExpression);
    }

    public double getMinZ() {
        return getContext().evaluateDimension(minZExpression);
    }

    public double getWidth() {
        return getContext().evaluateDimension(widthExpression);
    }

    public String getImpedanceExpression() {
        return impedanceExpression;
    }

    public void setImpedanceExpression(String impedanceExpression) {
        this.impedanceExpression = impedanceExpression;
    }

    public String getMinZExpression() {
        return minZExpression;
    }

    public void setMinZExpression(String minZExpression) {
        this.minZExpression = minZExpression;
    }

    public String getWidthExpression() {
        return widthExpression;
    }

    public void setWidthExpression(String widthExpression) {
        this.widthExpression = widthExpression;
    }

    public MomProject getContext() {
        return context;
    }

    public void recompile() {
        //
    }

    public void setContext(MomProject context) {
        this.context=context;
    }

    public MomProjectItem create() {
        return new MomProjectExtraLayer();
    }

    public String getId() {
        return "MomProjectExtraLayer";
    }

    public void load(Configuration conf, String key) {
        name=conf.getString(key+".name","None");
        minZExpression=conf.getString(key+".minZ","0");
        widthExpression=conf.getString(key+".width","0");
        impedanceExpression=conf.getString(key+".impedance","0");
    }

    public void store(Configuration conf, String key) {
        conf.setString(key+".name", name);
        conf.setString(key+".minZ", minZExpression);
        conf.setString(key+".width", widthExpression);
        conf.setString(key+".impedance", impedanceExpression);
    }
    
}
