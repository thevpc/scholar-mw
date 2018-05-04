package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

public class ValuesPlotModel implements PlotModel {
    String DATA_PROPERTY = "DATA_PROPERTY";
//    String PLOT_NAME_PROPERTY = "PLOT_NAME_PROPERTY";

    private static final long serialVersionUID = -1010101010101001064L;
    private String name="";
    private String title="";
    private String xtitle="";
    private String ytitle="";
    private String[] ytitles=new String[0];
    private boolean[] yvisible=new boolean[0];
    private String ztitle="";
    private double[][] x=new double[0][0];
    private double[][] y=new double[0][0];
    private Complex[][] z=new Complex[0][0];
    private ComplexAsDouble converter;
    private PlotType plotType=PlotType.CURVE;
    private PropertyChangeSupport changeSupport;
    private Map<String,Object> properties =new HashMap<String, Object>();
    private Set<ExternalLibrary> preferredLibraries= EnumSet.allOf(ExternalLibrary.class);
    private Set<ExternalLibrary> enabledLibraries=EnumSet.allOf(ExternalLibrary.class);
    private DoubleFormatter xformat =null;
    private DoubleFormatter yformat =null;
    private DoubleFormatter zformat =null;

    /**
     * surface
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
    public ValuesPlotModel(String title, String xtitle, String ytitle, String ztitle, double[] x, double[] y, Complex[][] z, ComplexAsDouble converter, PlotType plotType, Map<String,Object> properties) {
        this(title, xtitle, ytitle, ztitle, null, new double[][]{x}, new double[][]{y}, z, converter, plotType,properties);
    }

    /**
     * courbes
     * @param title
     * @param xtitle
     * @param ztitle
     * @param yTitles
     * @param x
     * @param z
     * @param converter
     */
    public ValuesPlotModel(String title, String xtitle, String ztitle, String[] yTitles, double[][] x, Complex[][] z, ComplexAsDouble converter, PlotType plotType, Map<String,Object> properties) {
        this(title, xtitle, null, ztitle, yTitles, x, null, z, converter, plotType,properties);
    }
    public ValuesPlotModel(String title, String xtitle, String ytitle, String ztitle, String[] ytitles, double[][] x, double[][] y, double[][] z, PlotType plotType, Map<String,Object> properties) {
        this(title, xtitle, ytitle, ztitle, ytitles, x, y, toComplex(z), ComplexAsDouble.REAL, plotType,properties);
    }

    public ValuesPlotModel(String title, String xtitle, String ytitle, String ztitle, String[] ytitles, double[][] x, double[][] y, Complex[][] z, ComplexAsDouble converter, PlotType plotType, Map<String,Object> properties) {
        changeSupport = new PropertyChangeSupport(this);
        this.title = title;
        this.name = title;
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
        this.converter = converter;
        this.plotType = plotType;
        if(properties!=null){
            for (Map.Entry<String, Object> e : properties.entrySet()) {
                if(e.getValue()!=null){
                    setProperty(e.getKey(),e.getValue());
                }
            }
        }
        check();
    }

    public DoubleFormatter getXformat() {
        return xformat;
    }

    public ValuesPlotModel setXformat(DoubleFormatter xformat) {
        this.xformat = xformat;
        return this;
    }

    public DoubleFormatter getYformat() {
        return yformat;
    }

    public ValuesPlotModel setYformat(DoubleFormatter yformat) {
        this.yformat = yformat;
        return this;
    }

    public DoubleFormatter getZformat() {
        return zformat;
    }

    public ValuesPlotModel setZformat(DoubleFormatter zformat) {
        this.zformat = zformat;
        return this;
    }

    private void check(){
    }

    private int getValidYVisibleCount(){
        int s=0;
        if(ytitles!=null){
            s=Math.max(s,ytitles.length);
        }
        if(y!=null){
            s=Math.max(s,y.length);
        }
        if(z!=null){
            s=Math.max(s,z.length);
        }
        return s;
    }

    private void ensureSize_yvisible(){
        ensureSize_yvisible(getValidYVisibleCount());
    }

    private void ensureSize_yvisible(int size){
        if(yvisible==null){
            yvisible=new boolean[size];
            for (int i = 0; i < size; i++) {
                yvisible[i]=true;
            }
        }else {
            if(yvisible.length<size) {
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
        changeSupport=new PropertyChangeSupport(this);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Object old=this.title;
        this.title = title;
        firePropertyChange("title", old, this.title);
    }


    public void setName(String name) {
        Object old=this.name;
        this.name = name;
        firePropertyChange("name", old, this.name);
    }

    public String getXtitle() {
        return xtitle;
    }

    public void setxTitle(String xTitle) {
        Object old=this.xtitle;
        this.xtitle = xTitle;
        firePropertyChange("xtitle", old, this.xtitle);
    }

    public String getYtitle() {
        return ytitle;
    }

    public void setyTitle(String yTitle) {
        Object old=this.ytitle;
        this.ytitle = yTitle;
        firePropertyChange("ytitle", old, this.ytitle);
    }

    public String getZtitle() {
        return ztitle;
    }

    public void setzTitle(String zTitle) {
        Object old=this.ztitle;
        this.ztitle = zTitle;
        firePropertyChange("ztitle", old, this.ztitle);
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
        Object old=this.x;
        this.x = x;
        firePropertyChange("x", old, this.x);
    }

    public double[][] getY() {
        return y;
    }

    public void setY(double[][] y) {
        Object old=this.y;
        this.y = y;
        firePropertyChange("y", old, this.y);
    }

    public Complex[][] getZ() {
        return z;
    }

    public void setZ(Complex[][] z) {
        Object old=this.z;
        this.z = z;
        check();
        firePropertyChange("z", old, this.z);
    }

    public String[] getYtitles() {
        return ytitles;
    }

    public void setYtitles(String[] ytitles) {
        Object old=this.ytitles;
        this.ytitles = ytitles;
        ensureSize_yvisible();
        firePropertyChange("ytitles", old, this.ytitles);
    }

    public void addX(double[] xx) {
        Object old=this.x;
        if (x == null) {
            x = new double[][]{xx};
        } else {
            double[][] x2 = new double[x.length + 1][];
            for (int i = 0; i < x.length; i++) {
                x2[i] = x[i];
            }
            x2[x2.length - 1] = xx;
            x=x2;
        }
        firePropertyChange("x", old, this.x);
    }

    public void addY(double[] yy) {
        Object old=this.y;
        if (y == null) {
            y = new double[][]{yy};
        } else {
            double[][] y2 = new double[y.length + 1][];
            for (int i = 0; i < y.length; i++) {
                y2[i] = y[i];
            }
            y2[y2.length - 1] = yy;
            y=y2;
        }
        firePropertyChange("y", old, this.y);
    }

    public void addZ(Complex[] zz) {
        Complex[][] z0=z;
        if (z0 == null) {
            z0 = new Complex[][]{zz};
        } else {
            Complex[][] z2 = new Complex[z.length + 1][];
            for (int i = 0; i < z.length; i++) {
                z2[i] = z[i];
            }
            z2[z2.length - 1] = zz;
            z0=z2;
        }
        setZ(z0);
    }

    public void addYTitle(String nextYTitle) {
        Object old=this.ytitle;
        if (ytitles == null) {
            ytitles = new String[]{nextYTitle};
            ensureSize_yvisible();
        } else {
            String[] yt2 = new String[ytitles.length + 1];
            for (int i = 0; i < ytitles.length; i++) {
                yt2[i] = ytitles[i];
            }
            yt2[yt2.length - 1] = nextYTitle;
            ytitles=yt2;
            ensureSize_yvisible();
        }
        firePropertyChange("ytitle", old, this.ytitle);
    }

    public ComplexAsDouble getConverter() {
        return converter;
    }

    public void setConverter(ComplexAsDouble zDoubleFunction) {
        Object old=this.converter;
        this.converter = zDoubleFunction;
        firePropertyChange("converter", old, this.converter);
    }

    public PlotType getPlotType() {
        return plotType;
    }

    public void setPlotType(PlotType plotType) {
        Object old=this.plotType;
        this.plotType = plotType;
        firePropertyChange("plotType", old, this.plotType);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public Map<String,Object> getProperties(){
        return Collections.unmodifiableMap(properties);
    }

    public void setProperties(Map<String,Object> other){
        if(other!=null){
            for (Map.Entry<String, Object> e : other.entrySet()) {
                setProperty(e.getKey(),e.getValue());
            }
        }
    }
    public void removeProperty(String key){
        setProperty(key,null);
    }

    public Object getProperty(String key){
        return getProperty(key, null);
    }

    public Object getProperty(String key,Object defaultValue){
        if(properties.containsKey(key)){
            return properties.get(key);
        }
        return defaultValue;
    }

    public void setProperty(String key,Object value){
        if(value==null){
            Object old = properties.remove(key);
            if(old !=null) {
                firePropertyChange("property."+key, old, null);
            }
        }else {
            Object old = properties.put(key, value);
            firePropertyChange("property."+key, old, value);
        }
    }

    private void firePropertyChange(String name,Object oldValue,Object newValue){
        if(!Objects.equals(oldValue,newValue)){
            changeSupport.firePropertyChange(DATA_PROPERTY, Boolean.FALSE, Boolean.TRUE);
            changeSupport.firePropertyChange(name, oldValue, newValue);
        }
    }
    public void setProperty(int index,String key,Object value){
        String s="["+index+"]."+key;
        setProperty(s,value);
    }

    public Object getProperty(int index,String key){
        return getProperty(index, key, null);
    }

    public Object getProperty(int index,String key,Object defaultValue){
        String s="["+index+"]."+key;
        return getProperty(s, defaultValue);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(property, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(property, listener);
    }

    public boolean getYVisible(int index) {
        ensureSize_yvisible(Math.max(index+1,getValidYVisibleCount()));
        return yvisible[index];
    }

    public void setYVisible(int index,boolean visible) {
        if(yvisible[index]!=visible) {
            yvisible[index] = visible;
            changeSupport.firePropertyChange("yvisible", Boolean.FALSE, Boolean.TRUE);
            changeSupport.firePropertyChange("yvisible["+index+"]", !visible, visible);
            changeSupport.firePropertyChange(DATA_PROPERTY, Boolean.FALSE, Boolean.TRUE);
        }
    }

    public String getName() {
        return name;
    }

    public double[][] getZd() {
        return Maths.toDouble(getZ(), getConverter());
    }

    public void modelUpdated() {
        firePropertyChange("modelUpdated", false, true);
    }

    private static Complex[][] toComplex(double[][] d){
        Complex[][] x=new Complex[d.length][];
        for (int i = 0; i < d.length; i++) {
            double[] ds = d[i];
            Complex[] xx=new Complex[ds.length];
            x[i]=xx;
            for (int j = 0; j < xx.length; j++) {
                xx[j]=Complex.valueOf(ds[j]);
            }
        }
        return x;
    }

    public Set<ExternalLibrary> getPreferredLibraries() {
        return preferredLibraries;
    }

    public ValuesPlotModel setPreferredLibraries(Set<ExternalLibrary> preferredLibraries) {
        Object old=this.preferredLibraries;
        if (preferredLibraries == null) {
            preferredLibraries = EnumSet.allOf(ExternalLibrary.class);
        }

        this.preferredLibraries = preferredLibraries;

        firePropertyChange("preferredLibraries", old, this.preferredLibraries);

        return this;
    }

    public Set<ExternalLibrary> getEnabledLibraries() {
        return enabledLibraries;
    }

    public ValuesPlotModel setEnabledLibraries(Set<ExternalLibrary> enabledLibraries) {
        Object old=this.enabledLibraries;
        if (enabledLibraries == null) {
            enabledLibraries = EnumSet.allOf(ExternalLibrary.class);
        }
        this.enabledLibraries = enabledLibraries;
        firePropertyChange("enabledLibraries", old, this.enabledLibraries);
        return this;
    }
}
