package net.vpc.scholar.hadruwaves.studio.standalone.editors;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * User: vpc
 * Date: 22 juin 2005
 * Time: 22:15:43
 */
class ColoredRect3DIcon implements Icon {
    Color color;

    public ColoredRect3DIcon(Color color) {
        this.color = color;
    }

    public int getIconHeight() {
        return 8;
    }

    public int getIconWidth() {
        return 8;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(Color.BLACK);
        int xl=getIconWidth();
        int yl=getIconHeight();
        g.draw3DRect(x,y,xl,yl,true);
        g.setColor(color);
        g.fill3DRect(x,y,xl,yl,true);
    }
}
