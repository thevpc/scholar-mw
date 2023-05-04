package net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * User: vpc
 * Date: 22 juin 2005
 * Time: 22:16:10
 */
class ColoredRectIcon implements Icon {
    Color color;

    public ColoredRectIcon(Color color) {
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
        g.drawRect(x, y, getIconWidth(), getIconHeight());
        g.setColor(color);
        g.fillRect(x, y, getIconWidth(), getIconHeight());
    }
}
