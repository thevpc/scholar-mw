package net.vpc.scholar.hadrumaths.plot;

import java.awt.Color;

public class HSBColorPalette extends JColorPalette implements Cloneable{
    private static final long serialVersionUID = -1010101010101001029L;
    public static final JColorPalette GRAY_PALETTE = new HSBColorPalette(-1, 0, 0, 0, 0, 0, 100);
    public static final JColorPalette DEFAULT_PALETTE = new HSBColorPalette(-1, 260, 0, 100, 100, 20, 95);
    private Color[] cache;
    private int minH;
    private int maxH;
    private int minS;
    private int maxS;
    private int minB;
    private int maxB;


    public HSBColorPalette(int size, int minH, int maxH, int minS, int maxS, int minB, int maxB) {
        super(size,false);
        this.minH = minH;
        this.maxH = maxH;
        this.minS = minS;
        this.maxS = maxS;
        this.minB = minB;
        this.maxB = maxB;
    }

    public Color getColor(int index) {
        Color[] colors = getColors();
        if(colors!=null){
            return colors[index];
        }else{
            return createColor((index % getSize()) / ((float) getSize()));
        }
    }

    public Color[] getColors() {
        if(getSize()>0){
            if(cache==null){
                Color[] all=new Color[getSize()];
                for (int i = 0; i < all.length; i++) {
                    all[i]=createColor(((float)i)/all.length);
                }
                cache=all;
            }
        }
        return cache;
    }

    public Color getColor(float ratio) {
        Color[] colors = getColors();
        if(colors!=null){
            ratio=getRatioTransform(ratio);
            int s=colors.length;// getSize();
            int x=(int) (ratio*s);
            return colors[x<0?0:(x>=s)?(s-1):x];
        }else{
            return createColor(ratio);
        }
    }

    public Color createColor(float ratio) {
        ratio = getRatioTransform(ratio);
        return new Color(Color.HSBtoRGB((minH + (maxH - minH) * ratio) / 359f,
                (minS + (maxS - minS) * ratio) / 100f,
                (minB + (maxB - minB) * ratio) / 100f));
    }

    protected void setSize(int size) {
        super.setSize(size);
        cache=null;
    }
}
