/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.mom.project.gpmesher;

import net.thevpc.scholar.hadrumaths.meshalgo.MeshAlgo;
import net.thevpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.BoxModesPattern;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.GpPattern;
import net.thevpc.scholar.hadrumaths.util.config.Configuration;

/**
 *
 * @author vpc
 */
public class BoxModesGpMesher extends RectGpMesher {

    private String maxExpression;

    public BoxModesGpMesher() {

    }

    public String getId() {
        return "BoxModesGpMesher";
    }

    @Override
    public void load(Configuration conf, String key) {
        super.load(conf, key);
        maxExpression = (conf.getString(key + ".max", "1"));
    }

    @Override
    public void store(Configuration c, String key) {
        super.store(c, key);
        c.setString(key + ".max", maxExpression);
    }

    public MeshAlgo getMeshAlgo() {
        return MeshAlgoRect.RECT_ALGO_LOW_RESOLUTION;
    }

    public GpPattern getPattern() {
        int max = getContext().evaluateInt(maxExpression);
        if (max < 1) {
            max = 1;
        }
        BoxModesPattern b=new BoxModesPattern(max);
        b.setInheritSymmetry(false);
        b.setInheritInvariance(false);
        b.setAxisIndependent(getInvariance());
        //b.setAxisSymmetry(getSymmetry());
        return b;
    }

    public String getMaxExpression() {
        return maxExpression;
    }

    public void setMaxExpression(String maxExpression) {
        this.maxExpression = maxExpression;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BoxModesGpMesher other = (BoxModesGpMesher) obj;
        if (this.maxExpression != other.maxExpression && (this.maxExpression == null || !this.maxExpression.equals(other.maxExpression))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (this.maxExpression != null ? this.maxExpression.hashCode() : 0);
        return hash;
    }
    

}
