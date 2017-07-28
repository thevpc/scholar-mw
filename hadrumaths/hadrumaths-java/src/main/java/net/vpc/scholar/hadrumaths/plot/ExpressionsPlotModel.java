package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.ExternalLibrary;
import net.vpc.scholar.hadrumaths.Samples;

import java.util.Map;
import java.util.Set;

/**
 * Created by vpc on 6/4/17.
 */
public class ExpressionsPlotModel implements PlotModel {
    private String title;
    private Samples samples;
    private Domain domain;
    private int xprec=-1;
    private int yprec=-1;
    private ComplexAsDouble complexAsDouble=ComplexAsDouble.ABS;
    private Expr[] expressions =new Expr[0];
    private ExpressionsPlotPanel.ShowType showType= ExpressionsPlotPanel.ShowType.CURVE_FX;
    private Map<String, Object> properties;
    private Set<ExternalLibrary> preferredLibraries;
    private CellPosition[] selectedAxis;

    public ExpressionsPlotModel() {
    }

    public String getTitle() {
        return title;
    }

    public ExpressionsPlotModel setTitle(String title) {
        this.title = title;
        return this;
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

    public ComplexAsDouble getComplexAsDouble() {
        return complexAsDouble;
    }

    public ExpressionsPlotModel setComplexAsDouble(ComplexAsDouble complexAsDouble) {
        this.complexAsDouble = complexAsDouble;
        return this;
    }

    public Expr[] getExpressions() {
        return expressions;
    }

    public ExpressionsPlotModel setExpressions(Expr[] expressions) {
        this.expressions = expressions;
        return this;
    }

    public ExpressionsPlotPanel.ShowType getShowType() {
        return showType;
    }

    public ExpressionsPlotModel setShowType(ExpressionsPlotPanel.ShowType showType) {
        this.showType = showType;
        return this;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public ExpressionsPlotModel setProperties(Map<String, Object> properties) {
        this.properties = properties;
        return this;
    }

    public Set<ExternalLibrary> getPreferredLibraries() {
        return preferredLibraries;
    }

    public ExpressionsPlotModel setPreferredLibraries(Set<ExternalLibrary> preferredLibraries) {
        this.preferredLibraries = preferredLibraries;
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
