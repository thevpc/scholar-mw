package net.vpc.scholar.hadruwaves.studio.standalone.v1.editors;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.util.config.Configuration;
import net.vpc.scholar.hadruwaves.mom.project.MomProject;
import net.vpc.scholar.hadruwaves.mom.project.common.ExpressionAware;


/**
 * Created by IntelliJ IDEA.
 * User: Taha
 * Date: 3 aout 2003
 * Time: 01:17:19
 * 
 */
public class PlotConfigData implements ExpressionAware {

    private MomProject structureContext;
    public boolean plotFx;
    public boolean plotFy;
    public boolean leadingX;
    public boolean leadingY;
    public double xMin;
    public double xMax;
    public double xPoint;
    public int xCount;
    public double yMin;
    public double yMax;
    public double yPoint;
    public int yCount;
    public double zMin;
    public double zMax;
    public double zPoint;
    public int zCount;
//    public double freqMin;
//    public double freqMax;
//    public int freqCount;
    public double iterMin;
    public double iterMax;
    public int iterCount;
    public double relativeErr;
    public String index1;
    public String index2;
    public String xMinExpression;
    public String xPointExpression;
    public String xMaxExpression;
    public String xCountExpression;
    public String yMinExpression;
    public String yPointExpression;
    public String yMaxExpression;
    public String yCountExpression;
    public String zMinExpression;
    public String zPointExpression;
    public String zMaxExpression;
    public String zCountExpression;
//    public String freqMinExpression;
//    public String freqMaxExpression;
//    public String freqCountExpression;
    public String iterName;
    public String iterMinExpression;
    public String iterMaxExpression;
    public String iterCountExpression;
    public String relativeErrExpression;

    public double[] getX() {
        if (isSurface() || isXLeading()) {
            return Maths.dtimes(xMin, xMax, xCount);
        } else {
            return new double[]{xPoint};
        }
    }

    public double[] getY() {
        if (isSurface() || isYLeading()) {
            return Maths.dtimes(yMin, yMax, yCount);
        } else {
            return new double[]{yPoint};
        }
    }

    public double[] getZ() {
        return Maths.dtimes(zMin, zMax, zCount);
    }

    public String getIteratorName() {
        return iterName;
    }
    
    public double[] getIteratorValues() {
        return Maths.dtimes(iterMin, iterMax, iterCount);
    }

    public boolean isSurface() {
        return !leadingX && !leadingY //&& (xMin != xMax && yMin != yMax && xCount > 1 && yCount > 1)
                ;
    }

    public boolean isXLeading() {
        return leadingX //|| (xMin != xMax && xCount > 1 && (yMin == yMax || yCount == 1))
                ;
    }

    public boolean isYLeading() {
        return leadingY //|| (yMin != yMax && yCount > 1 && (xMin == xMax || xCount == 1))
                ;
    }

    public void loadConfig(Configuration configuration, String root) {
        if (root == null) {
            root = "";
        } else {
            root = root + ".";
        }
        plotFx = configuration.getBoolean(root + "plotFx", true);
        plotFy = !plotFx;
        leadingX = configuration.getBoolean(root + "leadingX", false);
        leadingY = configuration.getBoolean(root + "leadingY", false);
        xMinExpression = String.valueOf(configuration.getObject(root + "plotXmin", "0"));
        xPointExpression = String.valueOf(configuration.getObject(root + "plotX", xMinExpression));
        xMaxExpression = String.valueOf(configuration.getObject(root + "plotXmax", "0"));
        xCountExpression = String.valueOf(configuration.getObject(root + "plotXcount", "1"));

        yMinExpression = String.valueOf(configuration.getObject(root + "plotYmin", "0"));
        yPointExpression = String.valueOf(configuration.getObject(root + "plotY", yMinExpression));
        yMaxExpression = String.valueOf(configuration.getObject(root + "plotYmax", "0"));
        yCountExpression = String.valueOf(configuration.getObject(root + "plotYcount", "1"));

        zMinExpression = String.valueOf(configuration.getObject(root + "plotZmin", "0"));
        zPointExpression = String.valueOf(configuration.getObject(root + "plotZ", zMinExpression));
        zMaxExpression = String.valueOf(configuration.getObject(root + "plotZmax", "0"));
        zCountExpression = String.valueOf(configuration.getObject(root + "plotZcount", "1"));

//        freqMinExpression = String.valueOf(configuration.getObject(root + "plotFreqMin", "0"));
//        freqMaxExpression = String.valueOf(configuration.getObject(root + "plotFreqMax", "0"));
//        freqCountExpression = String.valueOf(configuration.getObject(root + "plotFreqCount", "1"));

        iterName = String.valueOf(configuration.getObject(root + "plotIterName", "ITERATOR"));
        iterMinExpression = String.valueOf(configuration.getObject(root + "plotIterMin", "0"));
        iterMaxExpression = String.valueOf(configuration.getObject(root + "plotIterMax", "0"));
        iterCountExpression = String.valueOf(configuration.getObject(root + "plotIterCount", "1"));

        relativeErrExpression = String.valueOf(configuration.getObject(root + "relativeErr", "0.1"));
    }

    public void saveConfig(Configuration configuration, String root) {
        if (root == null) {
            root = "";
        } else {
            root = root + ".";
        }
        configuration.setBoolean(root + "plotFx", plotFx);
        configuration.setBoolean(root + "plotFy", plotFy);
        configuration.setBoolean(root + "leadingX", leadingX);
        configuration.setBoolean(root + "leadingY", leadingY);
        configuration.setString(root + "plotXmin", xMinExpression);
        configuration.setString(root + "plotX", xPointExpression);
        configuration.setString(root + "plotXmax", xMaxExpression);
        configuration.setString(root + "plotXcount", xCountExpression);
        configuration.setString(root + "plotYmin", yMinExpression);
        configuration.setString(root + "plotY", yPointExpression);
        configuration.setString(root + "plotYmax", yMaxExpression);
        configuration.setString(root + "plotYcount", yCountExpression);
        configuration.setString(root + "plotZmin", zMinExpression);
        configuration.setString(root + "plotZ", zPointExpression);
        configuration.setString(root + "plotZmax", zMaxExpression);
        configuration.setString(root + "plotZcount", zCountExpression);
//        configuration.setString(root + "plotFreqMin", freqMinExpression);
//        configuration.setString(root + "plotFreqMax", freqMaxExpression);
//        configuration.setString(root + "plotFreqCount", freqCountExpression);
        configuration.setString(root + "plotIterName", iterName);
        configuration.setString(root + "plotIterMin", iterMinExpression);
        configuration.setString(root + "plotIterMax", iterMaxExpression);
        configuration.setString(root + "plotIterCount", iterCountExpression);
        configuration.setString(root + "relativeErr", relativeErrExpression == null ? "0.1" : relativeErrExpression);
    }

    public void recompile() {
        MomProject c = getContext();
        xMin = c.evaluateDimension(xMinExpression);
        xPoint = c.evaluateDimension(xPointExpression);
        xMax = c.evaluateDimension(xMaxExpression);
        xCount = c.evaluateInt(xCountExpression);

        yMin = c.evaluateDimension(yMinExpression);
        yPoint = c.evaluateDimension(yPointExpression);
        yMax = c.evaluateDimension(yMaxExpression);
        yCount = c.evaluateInt(yCountExpression);

        zMin = c.evaluateDimension(zMinExpression);
        zPoint = c.evaluateDimension(zPointExpression);
        zMax = c.evaluateDimension(zMaxExpression);
        zCount = c.evaluateInt(zCountExpression);

//        freqMin = c.evaluateFrequence(freqMinExpression);
//        freqMax = c.evaluateFrequence(freqMaxExpression);
//        freqCount = c.evaluateInt(freqCountExpression);
        iterMin = c.evaluateDouble(iterMinExpression);
        iterMax = c.evaluateDouble(iterMaxExpression);
        iterCount = c.evaluateInt(iterCountExpression);
        relativeErr = c.evaluateDouble(relativeErrExpression);
    }

    public MomProject getContext() {
        return structureContext;
    }

    public void setContext(MomProject structureContext) {
        this.structureContext = structureContext;
    }
}
