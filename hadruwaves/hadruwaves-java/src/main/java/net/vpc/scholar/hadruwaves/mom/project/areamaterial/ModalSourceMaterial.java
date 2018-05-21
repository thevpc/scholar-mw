/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadruwaves.mom.project.areamaterial;

import java.util.ArrayList;
import net.vpc.scholar.hadruwaves.ModeType;
import net.vpc.scholar.hadruwaves.ModeIndex;
import net.vpc.scholar.hadruwaves.mom.project.AbstractMomProjectItem;
import net.vpc.scholar.hadrumaths.util.config.Configuration;

/**
 *
 * @author vpc
 */
public class ModalSourceMaterial extends AbstractMomProjectItem implements AreaMaterial{
    private String sourceCountExpression = "1";
    private String namedModesExpression = "";

    public ModalSourceMaterial() {
    }

    public void load(Configuration conf, String key) {
        sourceCountExpression = conf.getString(key + ".sourceCount", "1");
        namedModesExpression = conf.getString(key + ".namedModes", "");
    }

    public void store(Configuration c, String key) {
        c.setString(key + ".sourceCount", sourceCountExpression);
        c.setString(key + ".namedModes", namedModesExpression);
    }

    public String getId() {
        return "ModalSourceMaterial";
    }

    public String getName() {
        return "Modal Source";
    }

    public int getSourcesCount() {
        return getContext().evaluateInt(sourceCountExpression);
    }

    public ModeIndex[] getNamedModes() {
        //TEM,TM(0,1),TE(0,14)
        String[] all = (namedModesExpression==null?"":namedModesExpression).split(";");
        ArrayList<ModeIndex> modes = new ArrayList<ModeIndex>();
        String err = "Named modes shoud follow this example : TEM;TE(1,0);TM(14,1)";
        for (String string : all) {
            string = string.trim();
            if (string.length() > 0) {
                if (string.equals("TEM")) {
                    modes.add(ModeIndex.mode(ModeType.TEM, 0, 0));
                } else if (string.startsWith("TE(") && string.endsWith(")") && string.length() >= 7) {
                    string = string.substring(3, string.length() - 1);
                    int v = string.indexOf(',');
                    if (v > 0) {
                        modes.add(ModeIndex.mode(ModeType.TE, Integer.parseInt(string.substring(0, v)), Integer.parseInt(string.substring(v + 1))));
                    } else {
                        throw new IllegalArgumentException(err);
                    }
                } else if (string.startsWith("TM(") && string.endsWith(")") && string.length() >= 7) {
                    string = string.substring(3, string.length() - 1);
                    int v = string.indexOf(',');
                    if (v > 0) {
                        modes.add(ModeIndex.mode(ModeType.TM, Integer.parseInt(string.substring(0, v)), Integer.parseInt(string.substring(v + 1))));
                    } else {
                        throw new IllegalArgumentException(err);
                    }
                } else {
                    throw new IllegalArgumentException(err);
                }
            }
        }
        return modes.toArray(new ModeIndex[modes.size()]);
    }

    public String getNamedModesExpression() {
        return namedModesExpression;
    }

    public void setNamedModesExpression(String namedModesExpression) {
        this.namedModesExpression = namedModesExpression;
    }

    public String getSourceCountExpression() {
        return sourceCountExpression;
    }

    public void setSourceCountExpression(String sourceCountExpression) {
        this.sourceCountExpression = sourceCountExpression;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ModalSourceMaterial other = (ModalSourceMaterial) obj;
        if (this.sourceCountExpression != other.sourceCountExpression && (this.sourceCountExpression == null || !this.sourceCountExpression.equals(other.sourceCountExpression))) {
            return false;
        }
        if (this.namedModesExpression != other.namedModesExpression && (this.namedModesExpression == null || !this.namedModesExpression.equals(other.namedModesExpression))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.sourceCountExpression != null ? this.sourceCountExpression.hashCode() : 0);
        hash = 97 * hash + (this.namedModesExpression != null ? this.namedModesExpression.hashCode() : 0);
        return hash;
    }
    
}
