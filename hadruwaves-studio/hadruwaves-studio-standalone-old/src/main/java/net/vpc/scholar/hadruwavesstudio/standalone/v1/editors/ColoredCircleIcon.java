package net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * User: vpc
 * Date: 22 juin 2005
 * Time: 22:15:56
 */
class ColoredCircleIcon implements Icon {
    Color color;

    public ColoredCircleIcon(Color color) {
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
        g.drawOval(x, y, getIconWidth(), getIconHeight());
        g.setColor(color);
        g.fillOval(x, y, getIconWidth(), getIconHeight());
    }
}
