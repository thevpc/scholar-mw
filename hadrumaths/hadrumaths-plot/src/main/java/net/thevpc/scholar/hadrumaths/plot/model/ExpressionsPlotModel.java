package net.thevpc.scholar.hadrumaths.plot.model;

import net.thevpc.scholar.hadruplot.model.BasePlotModel;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.util.ArrayUtils;
import net.thevpc.scholar.hadruplot.*;


/**
 * Created by vpc on 6/4/17.
 */
public class ExpressionsPlotModel extends BasePlotModel {

    private PlotSamples samples;
    private PlotDomain domain;
    private int xprec = -1;
    private int yprec = -1;
    private Expr[] expressions = ArrayUtils.EMPTY_EXPR_ARRAY;
    private boolean constX = false;
    private CellPosition[] selectedAxis;

    public ExpressionsPlotModel() {
    }

    public PlotDomain getDomain() {
        return domain;
    }

    public ExpressionsPlotModel setDomain(PlotDomain domain) {
        this.domain = domain;
        return this;
    }

    public PlotSamples getSamples() {
        return samples;
    }

    public ExpressionsPlotModel setSamples(PlotSamples samples) {
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

    public CellPosition[] getSelectedAxis() {
        return selectedAxis;
    }

    public ExpressionsPlotModel setSelectedAxis(CellPosition[] selectedAxis) {
        this.selectedAxis = selectedAxis;
        return this;
    }
}
