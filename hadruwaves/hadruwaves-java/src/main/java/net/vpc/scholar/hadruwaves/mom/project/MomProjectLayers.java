/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import net.vpc.scholar.hadruwaves.mom.BoxLimit;
import net.vpc.scholar.hadrumaths.util.Configuration;

/**
 *
 * @author vpc
 */
public class MomProjectLayers implements MomProjectItem,Serializable,Cloneable {

    public String bottomThicknessExpression;
    public String topThicknessExpression;
    public String bottomEpsrExpression;
    public String topEpsrExpression;
    public String maxExpression;
    public BoxLimit topLimit;
    public BoxLimit bottomLimit;
    private MomProject context;
    private List<MomProjectExtraLayer> extraLayers = new ArrayList<MomProjectExtraLayer>();

    public MomProjectLayers() {
        this.bottomThicknessExpression = "0";
        this.topThicknessExpression = "0";
        this.bottomEpsrExpression = "0";
        this.topEpsrExpression = "0";
        this.maxExpression = "1";
    }

    public void store(Configuration c, String key) {
        c.setString(key + ".bottomThickness", bottomThicknessExpression);
        c.setString(key + ".topThickness", topThicknessExpression);
        c.setString(key + ".bottomEpsr", bottomEpsrExpression);
        c.setString(key + ".topEpsr", topEpsrExpression);
        c.setString(key + ".max", maxExpression);
        c.setString(key + ".topLimit", topLimit.toString());
        c.setString(key + ".bottomLimit", bottomLimit.toString());
        MomProjectFactory.INSTANCE.store(c, key + ".extraLayers", new MomProjectList(extraLayers));
    }

    public void load(Configuration c, String key) {
        bottomThicknessExpression = c.getString(key + ".bottomThickness", "1");
        topThicknessExpression = c.getString(key + ".topThickness", "1");
        bottomEpsrExpression = c.getString(key + ".bottomEpsr", "1");
        topEpsrExpression = c.getString(key + ".topEpsr", "1");
        maxExpression = c.getString(key + ".max", "1000");
        topLimit = BoxLimit.valueOf(c.getString(key + ".topLimit", BoxLimit.NOTHING.toString()));
        bottomLimit = BoxLimit.valueOf(c.getString(key + ".bottomLimit", BoxLimit.NOTHING.toString()));
        MomProjectList list = (MomProjectList) MomProjectFactory.INSTANCE.load(c, key + ".extraLayers");
        extraLayers = new ArrayList<MomProjectExtraLayer>();
        if (list != null) {
            for (Object object : list.getList()) {
                MomProjectExtraLayer ll=(MomProjectExtraLayer) object;
                ll.setContext(context);
                extraLayers.add(ll);
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.bottomThicknessExpression != null ? this.bottomThicknessExpression.hashCode() : 0);
        hash = 83 * hash + (this.topThicknessExpression != null ? this.topThicknessExpression.hashCode() : 0);
        hash = 83 * hash + (this.bottomEpsrExpression != null ? this.bottomEpsrExpression.hashCode() : 0);
        hash = 83 * hash + (this.topEpsrExpression != null ? this.topEpsrExpression.hashCode() : 0);
        hash = 83 * hash + (this.maxExpression != null ? this.maxExpression.hashCode() : 0);
        hash = 83 * hash + (this.topLimit != null ? this.topLimit.hashCode() : 0);
        hash = 83 * hash + (this.bottomLimit != null ? this.bottomLimit.hashCode() : 0);
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
        final MomProjectLayers other = (MomProjectLayers) obj;
        if (this.bottomThicknessExpression != other.bottomThicknessExpression && (this.bottomThicknessExpression == null || !this.bottomThicknessExpression.equals(other.bottomThicknessExpression))) {
            return false;
        }
        if (this.topThicknessExpression != other.topThicknessExpression && (this.topThicknessExpression == null || !this.topThicknessExpression.equals(other.topThicknessExpression))) {
            return false;
        }
        if (this.bottomEpsrExpression != other.bottomEpsrExpression && (this.bottomEpsrExpression == null || !this.bottomEpsrExpression.equals(other.bottomEpsrExpression))) {
            return false;
        }
        if (this.topEpsrExpression != other.topEpsrExpression && (this.topEpsrExpression == null || !this.topEpsrExpression.equals(other.topEpsrExpression))) {
            return false;
        }
        if (this.maxExpression != other.maxExpression && (this.maxExpression == null || !this.maxExpression.equals(other.maxExpression))) {
            return false;
        }
        if (this.topLimit != other.topLimit) {
            return false;
        }
        if (this.bottomLimit != other.bottomLimit) {
            return false;
        }
        return true;
    }

    public void recompile() {
    }

    public String getBottomThicknessExpression() {
        return bottomThicknessExpression;
    }

    public String getTopThicknessExpression() {
        return topThicknessExpression;
    }

    public String getBottomEpsrExpression() {
        return bottomEpsrExpression;
    }

    public String getTopEpsrExpression() {
        return topEpsrExpression;
    }

    public String getMaxExpression() {
        return maxExpression;
    }

    public double getBottomThickness() {
        return getContext().evaluateDimension(bottomThicknessExpression);
    }

    public double getTopThickness() {
        return getContext().evaluateDimension(topThicknessExpression);
    }

    public double getBottomEpsr() {
        return getContext().evaluateDouble(bottomEpsrExpression);
    }

    public double getTopEpsr() {
        return getContext().evaluateDouble(topEpsrExpression);
    }

    public BoxLimit getTopLimit() {
        return topLimit;
    }

    public BoxLimit getBottomLimit() {
        return bottomLimit;
    }

    ///////////////
    public void setBottomEpsrExpression(String bottomEpsrExpression) {
        this.bottomEpsrExpression = bottomEpsrExpression;
        recompile();
    }

    public void setBottomThicknessExpression(String bottomThicknessExpression) {
        this.bottomThicknessExpression = bottomThicknessExpression;
        recompile();
    }

    public void setMaxExpression(String maxExpression) {
        this.maxExpression = maxExpression;
        recompile();
    }

    public void setTopEpsrExpression(String topEpsrExpression) {
        this.topEpsrExpression = topEpsrExpression;
        recompile();
    }

    public void setTopThicknessExpression(String topThicknessExpression) {
        this.topThicknessExpression = topThicknessExpression;
        recompile();
    }

    public void setTopLimit(BoxLimit topLimit) {
        this.topLimit = topLimit;
    }

    public void setBottomLimit(BoxLimit bottomLimit) {
        this.bottomLimit = bottomLimit;
    }

    public MomProject getContext() {
        return context;
    }

    public void setContext(MomProject context) {
        this.context = context;
        for (MomProjectExtraLayer momProjectExtraLayer : extraLayers) {
            momProjectExtraLayer.setContext(context);
        }
    }

    public List<MomProjectExtraLayer> getExtraLayers() {
        return extraLayers == null ? new ArrayList<MomProjectExtraLayer>() : extraLayers;
    }

    public void setExtraLayers(List<MomProjectExtraLayer> layers) {
        this.extraLayers = (layers == null ? new ArrayList<MomProjectExtraLayer>() : layers);
    }

    public MomProjectItem create() {
        return new MomProjectLayers();
    }

    public String getId() {
        return "MomProjectLayers";
    }

    @Override
    protected MomProjectLayers clone() {
        MomProjectLayers o;
        try {
            o = (MomProjectLayers) super.clone();
            o.context=null;
            o.extraLayers=new ArrayList<MomProjectExtraLayer>();
            for (MomProjectExtraLayer layer : extraLayers) {
                o.extraLayers.add(layer.clone());
            }
            return o;
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
