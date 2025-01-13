/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.mom.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import net.thevpc.scholar.hadrumaths.util.config.Configuration;
import net.thevpc.scholar.hadruwaves.Boundary;

/**
 *
 * @author vpc
 */
public class MomProjectLayers implements MomProjectItem,Serializable,Cloneable {

    public String bottomConductivityExpression;
    public String topConductivityExpression;
    public String bottomThicknessExpression;
    public String topThicknessExpression;
    public String bottomEpsrExpression;
    public String topEpsrExpression;
    public String maxExpression;
    public Boundary topLimit;
    public Boundary bottomLimit;
    private MomProject context;
    private List<MomProjectExtraLayer> extraLayers = new ArrayList<MomProjectExtraLayer>();

    public MomProjectLayers() {
        this.topConductivityExpression = "0";
        this.bottomConductivityExpression = "0";
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
        c.setString(key + ".topConductivity", topConductivityExpression);
        c.setString(key + ".bottomConductivity", bottomConductivityExpression);
        MomProjectFactory.INSTANCE.store(c, key + ".extraLayers", new MomProjectList(extraLayers));
    }

    public void load(Configuration c, String key) {
        bottomConductivityExpression = c.getString(key + ".bottomConductivity", "0");
        topConductivityExpression = c.getString(key + ".topConductivity", "0");
        bottomThicknessExpression = c.getString(key + ".bottomThickness", "1");
        topThicknessExpression = c.getString(key + ".topThickness", "1");
        bottomEpsrExpression = c.getString(key + ".bottomEpsr", "1");
        topEpsrExpression = c.getString(key + ".topEpsr", "1");
        maxExpression = c.getString(key + ".max", "1000");
        topLimit = Boundary.valueOf(c.getString(key + ".topLimit", Boundary.NOTHING.toString()));
        bottomLimit = Boundary.valueOf(c.getString(key + ".bottomLimit", Boundary.NOTHING.toString()));
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
        hash = 83 * hash + (this.bottomConductivityExpression != null ? this.bottomConductivityExpression.hashCode() : 0);
        hash = 83 * hash + (this.topConductivityExpression != null ? this.topConductivityExpression.hashCode() : 0);
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
        if (this.topConductivityExpression != other.topConductivityExpression && (this.topConductivityExpression == null || !this.topConductivityExpression.equals(other.topConductivityExpression))) {
            return false;
        }
        if (this.bottomConductivityExpression != other.bottomConductivityExpression && (this.bottomConductivityExpression == null || !this.bottomConductivityExpression.equals(other.bottomConductivityExpression))) {
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

    public double getTopConductivity() {
        return getContext().evaluateDimension(topConductivityExpression);
    }
    public double getBottomConductivity() {
        return getContext().evaluateDimension(bottomConductivityExpression);
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

    public Boundary getTopLimit() {
        return topLimit;
    }

    public Boundary getBottomLimit() {
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

    public void setTopLimit(Boundary topLimit) {
        this.topLimit = topLimit;
    }

    public void setBottomLimit(Boundary bottomLimit) {
        this.bottomLimit = bottomLimit;
    }

    public String getBottomConductivityExpression() {
        return bottomConductivityExpression;
    }

    public void setBottomConductivityExpression(String bottomConductivityExpression) {
        this.bottomConductivityExpression = bottomConductivityExpression;
    }

    public String getTopConductivityExpression() {
        return topConductivityExpression;
    }

    public void setTopConductivityExpression(String topConductivityExpression) {
        this.topConductivityExpression = topConductivityExpression;
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
