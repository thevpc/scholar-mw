package net.thevpc.scholar.hadruplot.libraries.calc3d.elements;

import net.thevpc.common.swing.color.ColorUtils;
import net.thevpc.scholar.hadruplot.libraries.calc3d.log.Logger;
import net.thevpc.scholar.hadruplot.libraries.calc3d.math.MathUtils;
import net.thevpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.mathparser.Calculable;
import net.thevpc.scholar.hadruplot.libraries.calc3d.mathparser.ExpressionBuilder;
import net.thevpc.scholar.hadruplot.libraries.calc3d.geometry3d.*;


public class Element3DCurve extends Element3D {

    private static Logger LOG = Logger.getLogger(Element3DCurve.class.getName());
    private transient Calculable calc_x, calc_y, calc_z;
    private String exprX = "", exprY = "", exprZ = "";

    /**
     * minimum and maximum values of parametric variable t
     */
    private double min_t = -1, max_t = 1;

    /**
     * No of segments of the curve
     */
    private int numSegments = 80;

    public Element3DCurve() {
        prefs().setSplittable(false);
    }

    public double getMin_t() {
        return min_t;
    }

    public void setMin_t(double min_t) {
        this.min_t = min_t;
    }

    public double getMax_t() {
        return max_t;
    }

    public void setMax_t(double max_t) {
        this.max_t = max_t;
    }

    public PrimitiveElement3DCollection CreateCurve(Clip clip) {
        try {
            calc_x = new ExpressionBuilder(exprX).withVariableNames("t")
                    .build();
            calc_y = new ExpressionBuilder(exprY).withVariableNames("t")
                    .build();
            calc_z = new ExpressionBuilder(exprZ).withVariableNames("t")
                    .build();
        } catch (Exception e) {
            LOG.error(e);
            return null;
        }

        PrimitiveElement3DCollection curve3D = new PrimitiveElement3DCollection();

        Vector3D v1, v2;
        double x, y, z;
        double t;
        //sets first point
        x = fx(min_t);
        y = fy(min_t);
        z = fz(min_t);

        for (int i = 1; i < numSegments - 1; i++) {
            t = min_t + (max_t - min_t) * i / numSegments;
            //set first Point
            x = fx(t);
            if (!MathUtils.isValidNumber(x)) continue;
            y = fy(t);
            if (!MathUtils.isValidNumber(y)) continue;
            z = fz(t);
            if (!MathUtils.isValidNumber(z)) continue;

            v1 = new Vector3D(x, y, z);
            //set second point
            t = min_t + (max_t - min_t) * (i + 1) / numSegments;
            x = fx(t);
            y = fy(t);
            z = fz(t);
            v2 = new Vector3D(x, y, z);
            PrimitiveElement3D ec = (i % 10 == 0) ? new PrimitiveElement3DArrow(v1, v2) : new PrimitiveElement3DCurve(v1, v2);

            if (T != null) ec.transform(T);
            if (null != clip) ec = clip.getClippedElement(ec);
            if (null != ec) {
                ec.prefs().setCurveWidth(prefs().getCurveWidth());
                ec.prefs().setFillColor(prefs().getFillColor());
                ec.prefs().setLineColor(prefs().getLineColor());
                ec.prefs().setFillColor(ColorUtils.blendColors(prefs().getFillColor(), prefs().getBackColor(), (t - min_t) / (max_t - min_t)));
                ec.prefs().setSplittable(prefs().isSplittable());
                ec.prefs().setDashed(prefs().isDashed());
                curve3D.addElement(ec);
            }
        }
        primitiveElement3D = curve3D;
        return curve3D;

    }

    public int getNumSegments() {
        return numSegments;
    }

    public void setNumSegments(int numSegments) {
        if (numSegments > 1) this.numSegments = numSegments;
        else
            LOG.error("illegal number of segments (" + numSegments + ")" + "expected more than 1");
    }

    public String getExprX() {
        return exprX;
    }

    public void setExprX(String exprX) {
        this.exprX = exprX;
    }

    public String getExprY() {
        return exprY;
    }

    public void setExprY(String exprY) {
        this.exprY = exprY;
    }

    public String getExprZ() {
        return exprZ;
    }

    public void setExprZ(String exprZ) {
        this.exprZ = exprZ;
    }

    double fx(double t) {
        calc_x.setVariable("t", t);
        return getSceneManager().getSettings().inverseMapX(calc_x.calculate());
    }

    double fy(double t) {
        calc_y.setVariable("t", t);
        return getSceneManager().getSettings().inverseMapZ(calc_y.calculate());
    }

    double fz(double t) {
        calc_z.setVariable("t", t);
        return getSceneManager().getSettings().inverseMapZ(calc_z.calculate());
    }

    @Override
    public String getDefinition() {
        return "<br>x = " + exprX + "<br>" + "y = " + exprY + "<br>" + "z = " + exprZ +
                "<br>" + " <br> <b>t-range: </b> &nbsp [" + min_t + " , " + max_t + "]";

    }

    @Override
    public PrimitiveElement3D createElement() {
        primitiveElement3D = CreateCurve(null);
        primitiveElement3D.prefs().setFillColor(prefs().getFillColor());
        primitiveElement3D.prefs().setLineColor(prefs().getLineColor());
        primitiveElement3D.prefs().setCurveWidth(prefs().getCurveWidth());
        primitiveElement3D.prefs().setDashed(prefs().isDashed());
        return primitiveElement3D;
    }

    @Override
    public PrimitiveElement3D createElement(Clip clip) {
        primitiveElement3D = CreateCurve(clip);
        if (null == primitiveElement3D) return null;
        primitiveElement3D.prefs().setFillColor(prefs().getFillColor());
        primitiveElement3D.prefs().setLineColor(prefs().getLineColor());
        primitiveElement3D.prefs().setCurveWidth(prefs().getCurveWidth());
        primitiveElement3D.prefs().setDashed(prefs().isDashed());
        return primitiveElement3D;
    }
}