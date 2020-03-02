package net.vpc.scholar.hadruwaves.studio.standalone.v1.editors;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * User: vpc
 * Date: 22 juin 2005
 * Time: 22:15:43
 */
class ColoredLosangeIcon implements Icon {
    Color color;

    public ColoredLosangeIcon(Color color) {
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
        g.drawPolygon(
                new int[]{
                    x,
                    x+(xl/2),
                    x+(xl),
                    x,
                },
                new int[]{
                    y+(yl/2),
                    y,
                    y+(yl/2),
                    y+(yl),
                },
                4);
        g.setColor(color);
        g.fillPolygon(
                new int[]{
                    x,
                    x+(xl/2),
                    x+(xl),
                    x,
                },
                new int[]{
                    y+(yl/2),
                    y,
                    y+(yl/2),
                    y+(yl),
                },
                4);
    }
}
