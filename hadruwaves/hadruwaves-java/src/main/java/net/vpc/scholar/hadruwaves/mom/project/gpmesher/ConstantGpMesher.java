/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.project.gpmesher;

import net.vpc.scholar.hadrumaths.meshalgo.MeshAlgo;
import net.vpc.scholar.hadrumaths.meshalgo.rect.GridPrecision;
import net.vpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.EchelonPattern;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.GpPattern;
import net.vpc.scholar.hadruwaves.mom.GpPatternFactory;
import net.vpc.scholar.hadrumaths.util.config.Configuration;

/**
 *
 * @author vpc
 */
public class ConstantGpMesher extends RectGpMesher {

    private String gridXExpression;
    private String gridYExpression;
    private String valueExpression;

    public ConstantGpMesher() {

    }

    public String getId() {
        return "ConstantGpMesher";
    }

    public String getGridXExpression() {
        return gridXExpression;
    }

    public void setGridXExpression(String gridXExpression) {
        this.gridXExpression = gridXExpression;
    }

    public String getGridYExpression() {
        return gridYExpression;
    }

    public void setGridYExpression(String gridYExpression) {
        this.gridYExpression = gridYExpression;
    }

    public String getValueExpression() {
        return valueExpression;
    }

    public void setValueExpression(String valueExpression) {
        this.valueExpression = valueExpression;
    }
    

    @Override
    public void load(Configuration conf, String key) {
        gridXExpression = (conf.getString(key + ".gridX", "1"));
        gridYExpression = (conf.getString(key + ".gridY", "1"));
        valueExpression = (conf.getString(key + ".value", "0"));
    }

    @Override
    public void store(Configuration c, String key) {
        c.setString(key + ".gridX", gridXExpression);
        c.setString(key + ".gridY", gridYExpression);
        c.setString(key + ".value", valueExpression);
    }

    public MeshAlgo getMeshAlgo() {
        int gridX = getContext().evaluateInt(gridXExpression);
        int gridY = getContext().evaluateInt(gridYExpression);
        if (gridX < 1) {
            gridX = 1;
        }

        if (gridY < 1) {
            gridY = 1;
        }

        int gridXX = (int) (Math.log(gridX) / Math.log(2));
        int gridYY = (int) (Math.log(gridY) / Math.log(2));

        return new MeshAlgoRect(GridPrecision.ofXY(gridXX, gridXX, gridYY, gridYY));

    }

    public GpPattern getPattern() {
        double value = getContext().evaluateDouble(valueExpression);
        return value==0? GpPatternFactory.ECHELON:new EchelonPattern(1,1,value);
    }
    

}
