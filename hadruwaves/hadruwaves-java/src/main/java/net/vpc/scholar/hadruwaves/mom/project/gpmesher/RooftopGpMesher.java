/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.project.gpmesher;

import net.vpc.scholar.hadrumaths.symbolic.double2double.RooftopType;
import net.vpc.scholar.hadrumaths.meshalgo.MeshAlgo;
import net.vpc.scholar.hadrumaths.meshalgo.rect.GridPrecision;
import net.vpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.GpPattern;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.Rooftop2DPattern;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.Rooftop2DSimplePattern;
import net.vpc.scholar.hadrumaths.util.config.Configuration;

/**
 *
 * @author vpc
 */
public class RooftopGpMesher extends RectGpMesher {

    private RooftopType rooftopType = RooftopType.FULL;
    private String gridXExpression;
    private String gridYExpression;
    private boolean cross;

    public RooftopGpMesher() {

    }

    public String getId() {
        return "RooftopGpMesher";
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

    public RooftopType getRooftopType() {
        return rooftopType;
    }

    public void setRooftopType(RooftopType rooftopType) {
        this.rooftopType = rooftopType;
    }

    @Override
    public void load(Configuration conf, String key) {
        super.load(conf, key);
        cross = conf.getBoolean(key + ".cross", false);
        gridXExpression = (conf.getString(key + ".gridX", "1"));
        gridYExpression = (conf.getString(key + ".gridY", "1"));
        rooftopType = RooftopType.valueOf(conf.getString(key + ".rooftopType", "FULL"));
    }

    @Override
    public void store(Configuration c, String key) {
        super.store(c, key);
        c.setString(key + ".rooftopType", rooftopType.toString());
        c.setString(key + ".gridX", gridXExpression);
        c.setString(key + ".gridY", gridYExpression);
        c.setBoolean(key + ".cross", cross);
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
        GpPattern gpp = null;
        if (gridXX == 0 && gridYY == 0 && cross == false && !RooftopType.FULL.equals(rooftopType)) {
            gpp = new Rooftop2DSimplePattern(rooftopType);
        } else if (cross) {
            gpp = new Rooftop2DPattern(true, true);
        } else {
            gpp = new Rooftop2DPattern(false, false);
        }
        return gpp;
    }

    public boolean isCross() {
        return cross;
    }

    public void setCross(boolean cross) {
        this.cross = cross;
    }
    

}
