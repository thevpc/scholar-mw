/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.mom.project;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.util.config.Configuration;

/**
 *
 * @author vpc
 */
public class StrLayerConfig {

    public double minZ;
    public double width;
    public Complex impedance;
    public String minZExpression;
    public String widthExpression;
    public String impedanceExpression;
    public String name = "None";
    private MomProject structureContext;

    public StrLayerConfig() {

    }

    public void evaluate() {
        MomProject c = getStructureContext();
        if (c != null) {
            minZ = c.evaluateDimension(minZExpression);
            width = c.evaluateDimension(widthExpression);
            impedance = c.evaluateComplex(impedanceExpression);
        }
    }

    public void store(Configuration c, String key) {
        c.setString(key + ".minZ", minZExpression);
        c.setString(key + ".width", widthExpression);
        c.setString(key + ".impedance", impedanceExpression);
        c.setString(key + ".name", name);
    }

    public void load(Configuration c, String key) {
        minZExpression = c.getString(key + ".minZ");
        widthExpression = c.getString(key + ".width");
        impedanceExpression = c.getString(key + ".impedance");
        name = c.getString(key + ".name");
    }

    public MomProject getStructureContext() {
        return structureContext;
    }

    public void setStructureContext(MomProject context) {
        this.structureContext = context;
    }
}
