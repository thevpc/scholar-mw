package net.thevpc.scholar.hadruplot.model;

import net.thevpc.common.util.DoubleFormat;

import java.util.*;
import java.util.function.ToDoubleFunction;
import net.thevpc.scholar.hadruplot.LibraryPlotType;
import net.thevpc.scholar.hadruplot.PlotDoubleConverter;
import net.thevpc.scholar.hadruplot.PlotModelType;
import net.thevpc.scholar.hadruplot.PlotType;

public class ValuesPlotModel extends BasePlotModel implements PlotModel1D, PlotModel2D {

    String DATA_PROPERTY = "DATA_PROPERTY";
//    String PLOT_NAME_PROPERTY = "PLOT_NAME_PROPERTY";

    private static final long serialVersionUID = 1L;
    private boolean d1 = false;
    private boolean d1column = false;
    private String xtitle = "";
    private String ytitle = "";
    private String[] ytitles = new String[0];
    private boolean[] yvisible = new boolean[0];
    private String ztitle = "";
    private double[][] x = new double[0][0];
    private double[][] y = new double[0][0];
    private Object[][] z = new Object[0][0];
    private DoubleFormat xformat = null;
    private DoubleFormat yformat = null;
    private DoubleFormat zformat = null;

    public ValuesPlotModel(String title, String xtitle, String ytitle, String[] ytitles, double[] x, Object[] y, boolean column, ToDoubleFunction<Object> converter, LibraryPlotType plotType, Map<String, Object> properties) {
        this.d1 = true;
        this.d1column = column;
        init(title, xtitle, ytitle, ztitle, ytitles, new double[][]{x}, new double[0][], new Object[][]{y}, converter, plotType, properties);
    }

    /**
     * surface
     *
     * @param title
     * @param xtitle
     * @param ytitle
     * @param ztitle
     * @param x
     * @param y
     * @param z
     * @param converter
     * @param plotType
     */
    public ValuesPlotModel(String title, String xtitle, String ytitle, String ztitle, double[] x, double[] y, Object[][] z, ToDoubleFunction<Object> converter, LibraryPlotType plotType, Map<String, Object> properties) {
        init(title, xtitle, ytitle, ztitle, null, new double[][]{x}, new double[][]{y}, z, converter, plotType, properties);
    }

    /**
     * courbes
     *
     * @param title
     * @param xtitle
     * @param ztitle
     * @param yTitles
     * @param x
     * @param z
     * @param converter
     */
    public ValuesPlotModel(String title, String xtitle, String ztitle, String[] yTitles, double[][] x, Object[][] z, ToDoubleFunction<Object> converter, LibraryPlotType plotType, Map<String, Object> properties) {
        init(title, xtitle, null, ztitle, yTitles, x, null, z, converter, plotType, properties);
    }

    public ValuesPlotModel(String title, String xtitle, String ytitle, String ztitle, String[] ytitles, double[][] x, double[][] y, double[][] z, LibraryPlotType plotType, Map<String, Object> properties) {
        init(title, xtitle, ytitle, ztitle, ytitles, x, y,
                PlotDoubleConverter.toComplex(PlotDoubleConverter.REAL, z),
                 PlotDoubleConverter.REAL, plotType, properties);
    }

    public ValuesPlotModel(String title, String xtitle, String ytitle, String ztitle, String[] ytitles, double[][] x, double[][] y, Object[][] z, ToDoubleFunction<Object> converter, LibraryPlotType plotType, Map<String, Object> properties) {
        init(title, xtitle, ytitle, ztitle, ytitles, x, y, z, converter, plotType, properties);
    }

    public double[] get1dX() {
        return x[0];
    }

    public Object[] get1dY() {
        return z[0];
    }

    private void init(String title, String xtitle, String ytitle, String ztitle, String[] ytitles, double[][] x, double[][] y, Object[][] z, ToDoubleFunction<Object> converter, LibraryPlotType plotType, Map<String, Object> properties) {
        setTitle(title);
        setName(title);
        this.xtitle = xtitle;
        this.ytitle = ytitle;
        this.ytitles = ytitles;

        this.ztitle = ztitle;
        this.x = x;
//        if(x!=null && x[0]==null){
//            System.out.println("how x[0]==null?");
//        }
        this.y = y;
        setZ(z);

        ensureSize_yvisible();
        setConverter(converter);
        setPlotType(plotType);
        setProperties(properties);
        check();
    }

    @Override
    public boolean is1dColumn() {
        return d1column;
    }

    @Override
    public PlotModel2D toPlotModel2D() {
        return this;
    }

    @Override
    public Set<PlotModelType> getModelTypes() {
        EnumSet<PlotModelType> a = EnumSet.of(PlotModelType.NUMBER_2);
        if (d1) {
            a.add(PlotModelType.NUMBER_1);
        }
        return a;
    }

    public DoubleFormat getXformat() {
        return xformat;
    }

    public ValuesPlotModel setXformat(DoubleFormat xformat) {
        this.xformat = xformat;
        return this;
    }

    public DoubleFormat getYformat() {
        return yformat;
    }

    public ValuesPlotModel setYformat(DoubleFormat yformat) {
        this.yformat = yformat;
        return this;
    }

    public DoubleFormat getZformat() {
        return zformat;
    }

    public ValuesPlotModel setZformat(DoubleFormat zformat) {
        this.zformat = zformat;
        return this;
    }

    private void check() {
    }

    private int getValidYVisibleCount() {
        int s = 0;
        if (ytitles != null) {
            s = Math.max(s, ytitles.length);
        }
        if (y != null) {
            s = Math.max(s, y.length);
        }
        if (z != null) {
            s = Math.max(s, z.length);
        }
        return s;
    }

    private void ensureSize_yvisible() {
        ensureSize_yvisible(getValidYVisibleCount());
    }

    private void ensureSize_yvisible(int size) {
        if (yvisible == null) {
            yvisible = new boolean[size];
            for (int i = 0; i < size; i++) {
                yvisible[i] = true;
            }
        } else {
            if (yvisible.length < size) {
                boolean[] yvisible2 = new boolean[size];
                int old = Math.min(size, yvisible.length);
                System.arraycopy(yvisible, 0, yvisible2, 0, old);
                for (int i = old; i < yvisible2.length; i++) {
                    yvisible2[i] = true;
                }
                yvisible = yvisible2;
            }
        }
    }

    public ValuesPlotModel() {

    }

    public String getXtitle() {
        return xtitle;
    }

    public void setxTitle(String xTitle) {
        Object old = this.xtitle;
        this.xtitle = xTitle;
        firePropertyChange("xtitle", old, this.xtitle);
    }

    public String getYtitle() {
        return ytitle;
    }

    public void setyTitle(String yTitle) {
        Object old = this.ytitle;
        this.ytitle = yTitle;
        firePropertyChange("ytitle", old, this.ytitle);
    }

    public String getZtitle() {
        return ztitle;
    }

    public void setzTitle(String zTitle) {
        Object old = this.ztitle;
        this.ztitle = zTitle;
        firePropertyChange("ztitle", old, this.ztitle);
    }

    public double[] getX(int index) {
        return x[index];
    }

    public double[] getY(int index) {
        return y[index];
    }

    public Object[] getZ(int index) {
        return z[index];
    }

    public double[][] getX() {
        return x;
    }

    public void setXVector(double[] x) {
        setX(new double[][]{x});
    }

    public void setYVector(double[] y) {
        setY(new double[][]{y});
    }

    public void setX(double[][] x) {
        Object old = this.x;
        this.x = x;
        firePropertyChange("x", old, this.x);
    }

    public double[][] getY() {
        return y;
    }

    public void setY(double[][] y) {
        Object old = this.y;
        this.y = y;
        firePropertyChange("y", old, this.y);
    }

    public Object[][] getZ() {
        return z;
    }

    public void setZ(Object[][] z) {
        Object old = this.z;
        this.z = z;
        check();
        firePropertyChange("z", old, this.z);
    }

    public String getYtitle(int index) {
        if (ytitles != null) {
            if (index >= 0 && index < ytitles.length) {
                return ytitles[index];
            }
        }
        return null;
    }

    public String[] getYtitles() {
        return ytitles;
    }

    public void setYtitles(String[] ytitles) {
        Object old = this.ytitles;
        this.ytitles = ytitles;
        ensureSize_yvisible();
        firePropertyChange("ytitles", old, this.ytitles);
    }

    public void addX(double[] xx) {
        Object old = this.x;
        if (x == null) {
            x = new double[][]{xx};
        } else {
            double[][] x2 = new double[x.length + 1][];
            for (int i = 0; i < x.length; i++) {
                x2[i] = x[i];
            }
            x2[x2.length - 1] = xx;
            x = x2;
        }
        firePropertyChange("x", old, this.x);
    }

    public void addY(double[] yy) {
        Object old = this.y;
        if (y == null) {
            y = new double[][]{yy};
        } else {
            double[][] y2 = new double[y.length + 1][];
            for (int i = 0; i < y.length; i++) {
                y2[i] = y[i];
            }
            y2[y2.length - 1] = yy;
            y = y2;
        }
        firePropertyChange("y", old, this.y);
    }

    public void addZ(Object[] zz) {
        Object[][] z0 = z;
        if (z0 == null) {
            z0 = new Object[][]{zz};
        } else {
            Object[][] z2 = new Object[z.length + 1][];
            for (int i = 0; i < z.length; i++) {
                z2[i] = z[i];
            }
            z2[z2.length - 1] = zz;
            z0 = z2;
        }
        setZ(z0);
    }

    public void addYTitle(String nextYTitle) {
        Object old = this.ytitle;
        if (ytitles == null) {
            ytitles = new String[]{nextYTitle};
            ensureSize_yvisible();
        } else {
            String[] yt2 = new String[ytitles.length + 1];
            for (int i = 0; i < ytitles.length; i++) {
                yt2[i] = ytitles[i];
            }
            yt2[yt2.length - 1] = nextYTitle;
            ytitles = yt2;
            ensureSize_yvisible();
        }
        firePropertyChange("ytitle", old, this.ytitle);
    }

    

    public boolean getYVisible(int index) {
        ensureSize_yvisible(Math.max(index + 1, getValidYVisibleCount()));
        return yvisible[index];
    }

    public void setYVisible(int index, boolean visible) {
        if (yvisible[index] != visible) {
            yvisible[index] = visible;
            firePropertyChange("yvisible", Boolean.FALSE, Boolean.TRUE);
            firePropertyChange("yvisible[" + index + "]", !visible, visible);
//            changeSupport.firePropertyChange(DATA_PROPERTY, Boolean.FALSE, Boolean.TRUE);
        }
    }

    public double[][] getZd() {
        return PlotDoubleConverter.toDouble(getConverter(), getZ());
    }

    public void modelUpdated() {
        firePropertyChange("modelUpdated", false, true);
    }
}
