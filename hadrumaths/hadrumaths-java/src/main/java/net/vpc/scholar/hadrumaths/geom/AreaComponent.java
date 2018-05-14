package net.vpc.scholar.hadrumaths.geom;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.DomainScaleTool;
import net.vpc.scholar.hadrumaths.Maths;

import javax.swing.*;
import java.awt.geom.Area;
import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
* @creationtime 22 mai 2007 22:46:15
*/
public final class AreaComponent extends JComponent {

    
    Area[] a;

    public AreaComponent(Area... a) {
        this.a = a;
        setPreferredSize(new Dimension(
                (int) 600,
                (int) 600
        ));
    }

    public static void showDialog(Area... a) {
        showDialog(null,a);
    }
    public static void showDialog(Geometry... a) {
        showDialog(null,a);
    }
    public static void showDialog(String smg,Area... a) {
        new AreaComponent(a).showDialog(smg);
    }
    public static void showDialog(String smg,Geometry... a) {
        Domain d=a[0].getDomain();
        for (int i = 1; i < a.length; i++) {
            d=d.expand(a[i].getDomain());
        }
        DomainScaleTool t=DomainScaleTool.create(d,Domain.forPoints(0,0,600,600));
        Area[] aa=new Area[a.length];
        for (int i = 0; i < aa.length; i++) {
            aa[i]=new Area(t.rescale(a[i].getPath()));
        }
        new AreaComponent(aa).showDialog(smg);
    }

    public void showDialog(String smg) {
        JOptionPane.showMessageDialog(null, this, smg==null? "Debug...":smg, JOptionPane.PLAIN_MESSAGE);
    }

    private Color tr(Color c){
        float r=c.getRed()/255.0f;
        float g=c.getGreen()/255.0f;
        float b=c.getBlue()/255.0f;
        float a=0.5f;
        return new Color(r,g,b,a);
    }
    private static Color[] COLORS=new Color[]{
            Color.GRAY,
            Color.CYAN,
            Color.BLUE,
            Color.RED,
            Color.GREEN,
            Color.MAGENTA,
            Color.YELLOW,
            Color.CYAN,
            Color.ORANGE,
            Color.PINK
    };

    private Color createColor(int index){
        return tr(COLORS[Maths.abs(index)%COLORS.length]);
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(Color.BLACK);
        g2.drawRect(0, 0, getWidth(), getHeight());
        super.paint(g);
        for (int i = 0; i < a.length; i++) {
            g2.setColor(createColor(i));
            Area area = a[i];
            g2.fill(area);
        }
        g2.setColor(Color.RED);
        for (Area area : a) {
            g2.draw(area);
        }
    }
}
