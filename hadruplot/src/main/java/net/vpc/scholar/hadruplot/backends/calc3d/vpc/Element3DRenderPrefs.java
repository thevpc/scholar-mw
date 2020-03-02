package net.vpc.scholar.hadruplot.backends.calc3d.vpc;

import java.awt.*;

public class Element3DRenderPrefs implements Cloneable{
    protected Color fillColor = Color.white, lineColor = Color.black;
    protected Color backColor = Color.gray;
    protected int curveWidth = 1;
    /**
     * if line is drawn in dASHED MODE
     */
    protected boolean dashed;
    /**
     * if primitiveElement3D is to be splitted while solving BSP Tree
     */

    protected boolean splittable = true;
    /***/
    protected int fillmode = 0;
    /**
     * True if Object is visible
     */
    private boolean visible = true;

    /**
     * returns if this primitiveElement3D is to be rendered or not
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * sets flag if primitiveElement3D is visible on screen(renderable)
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public int getCurveWidth() {
        return curveWidth;
    }

    public void setCurveWidth(int curveWidth) {
        if (curveWidth >= 0) this.curveWidth = curveWidth;
    }

    /**
     * Returns if line rendering pattern of primitiveElement3D is dahsed/solid
     */
    public boolean isDashed() {
        return dashed;
    }

    /**
     * Sets the primitiveElement3D line rendering pattern to be dashed/solid
     */
    public void setDashed(boolean isdashed) {
        this.dashed = isdashed;
    }

    public int getFillmode() {
        return fillmode;
    }

    public void setFillmode(int fillmode) {
        if ((fillmode > 2) || (fillmode < 0)) return;
        this.fillmode = fillmode;
    }

    /**
     * @return the backColor
     */
    public Color getBackColor() {
        return backColor;
    }

    /**
     * @param backColor the backColor to set
     */
    public void setBackColor(Color backColor) {
        this.backColor = backColor;
    }

    /**
     * @return the splittable
     */
    public boolean isSplittable() {
        return splittable;
    }

    /**
     * @param splittable the splittable to set
     */
    public void setSplittable(boolean splittable) {
        this.splittable = splittable;
    }

    public void copyFrom(Element3DRenderPrefs other){
        if(other!=null){
            this.setFillColor(other.getFillColor());
            this.setBackColor(other.getBackColor());
            this.setLineColor(other.getLineColor());
            this.setSplittable(other.isSplittable());
            this.setDashed(other.isDashed());
            this.setCurveWidth(other.getCurveWidth());
        }
    }
    public Element3DRenderPrefs copy(){
        return clone();
    }

    @Override
    protected Element3DRenderPrefs clone(){
        try {
            return (Element3DRenderPrefs) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException("Unexpected");
        }
    }
}
