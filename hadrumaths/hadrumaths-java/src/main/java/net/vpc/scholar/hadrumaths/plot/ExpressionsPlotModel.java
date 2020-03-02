package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;
import net.vpc.scholar.hadruplot.*;

import java.util.Map;

/**
 * Created by vpc on 6/4/17.
 */
public class ExpressionsPlotModel extends BasePlotModel {
    private Samples samples;
    private Domain domain;
    private int xprec = -1;
    private int yprec = -1;
    private PlotDoubleConverter toDoubleConverter = PlotDoubleConverter.ABS;
    private Expr[] expressions = ArrayUtils.EMPTY_EXPR_ARRAY;
    private PlotType plotType = PlotType.CURVE;
    private boolean constX = false;
    private Map<String, Object> properties;
    private String libraries;
    private CellPosition[] selectedAxis;

    public ExpressionsPlotModel() {
    }

    public Domain getDomain() {
        return domain;
    }

    public ExpressionsPlotModel setDomain(Domain domain) {
        this.domain = domain;
        return this;
    }

    public Samples getSamples() {
        return samples;
    }

    public ExpressionsPlotModel setSamples(Samples samples) {
        this.samples = samples;
        return this;
    }

    public int getXprec() {
        return xprec;
    }

    public ExpressionsPlotModel setXprec(int xprec) {
        this.xprec = xprec;
        return this;
    }

    public int getYprec() {
        return yprec;
    }

    public ExpressionsPlotModel setYprec(int yprec) {
        this.yprec = yprec;
        return this;
    }

    public PlotDoubleConverter getComplexAsDouble() {
        return toDoubleConverter;
    }

    public ExpressionsPlotModel setComplexAsDouble(PlotDoubleConverter toDoubleConverter) {
        this.toDoubleConverter = toDoubleConverter;
        return this;
    }

    public boolean isConstX() {
        return constX;
    }

    public ExpressionsPlotModel setConstX(boolean constX) {
        this.constX = constX;
        return this;
    }

    public Expr[] getExpressions() {
        return expressions;
    }

    public ExpressionsPlotModel setExpressions(Expr[] expressions) {
        this.expressions = expressions;
        return this;
    }

    public PlotType getPlotType() {
        return plotType;
    }

    public ExpressionsPlotModel setPlotType(PlotType plotType) {
        this.plotType = plotType;
        return this;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public ExpressionsPlotModel setProperties(Map<String, Object> properties) {
        this.properties = properties;
        return this;
    }

    public CellPosition[] getSelectedAxis() {
        return selectedAxis;
    }

    public ExpressionsPlotModel setSelectedAxis(CellPosition[] selectedAxis) {
        this.selectedAxis = selectedAxis;
        return this;
    }
}
