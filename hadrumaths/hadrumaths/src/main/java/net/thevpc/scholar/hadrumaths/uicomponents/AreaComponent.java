package net.thevpc.scholar.hadrumaths.uicomponents;

import net.thevpc.nuts.util.NBlankable;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.DomainScaleTool;
import net.thevpc.scholar.hadrumaths.geom.HGeometry;
import net.thevpc.scholar.hadrumaths.util.JavaGeoUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 22 mai 2007 22:46:15
 */
public final class AreaComponent extends JComponent {

    private static final Color[] COLORS = new Color[]{
            Color.GRAY, Color.CYAN, Color.BLUE, Color.RED, Color.GREEN,
            Color.MAGENTA, Color.YELLOW, Color.CYAN, Color.ORANGE, Color.PINK
    };

    Area[] a;
    String[] labels;
    Set<Integer> selected = new LinkedHashSet<>(); // empty = show all

    public AreaComponent(Area... a) {
        this(null, a);
    }

    public AreaComponent(String[] labels, Area... a) {
        this.a = a;
        this.labels = labels;
        setPreferredSize(new java.awt.Dimension(600, 600));
    }

    public AreaComponent(HGeometry... a) {
        this(null, a);
    }

    public AreaComponent(String[] labels, HGeometry... a) {
        Domain d = a[0].getDomain();
        for (int i = 1; i < a.length; i++) d = d.expand(a[i].getDomain());
        DomainScaleTool t = DomainScaleTool.create(d, Domain.ofPoints(0, 0, 600, 600));
        String[] labels2 = new String[a.length];
        Area[] aa = new Area[a.length];
        for (int i = 0; i < aa.length; i++) {
            aa[i] = new Area(t.rescale(a[i].getPath()));
            int points = -1;
            if (aa[i].isPolygonal()) {
                points = JavaGeoUtils.countPoints(aa[i]);
            }
            if (labels != null && labels.length > i && !NBlankable.isBlank(labels[i])) {
                labels2[i] = labels[i];
            } else {
                labels2[i] = "Shape" + (points > 0 ? ("[" + points + "]") : "") + " " + i + " " + a[i].getDomain();
            }
        }
        this.labels = labels2;
        this.a = aa;
        setPreferredSize(new java.awt.Dimension(600, 600));
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(Color.BLACK);
        g2.drawRect(0, 0, getWidth(), getHeight());
        super.paint(g);
        for (int i = 0; i < a.length; i++) {
            if (!selected.isEmpty() && !selected.contains(i)) continue; // filtered
            g2.setColor(createColor(i));
            g2.fill(a[i]);
        }
        g2.setColor(Color.RED);
        for (int i = 0; i < a.length; i++) {
            if (!selected.isEmpty() && !selected.contains(i)) continue;
            g2.setColor(createColor(i).darker());
            g2.draw(a[i]);
        }

        for (int i = 0; i < a.length; i++) {
            if (!selected.isEmpty() && !selected.contains(i)) continue; // filtered
            // Draw points of the polygons
            if (a[i].isPolygonal()) {
                int points = JavaGeoUtils.countPoints(a[i]);
                if (points > 0) {
                    PathIterator pi = a[i].getPathIterator(null);
                    double[] coords = new double[6];
                    while (!pi.isDone()) {
                        int type = pi.currentSegment(coords);
                        if (type == PathIterator.SEG_MOVETO || type == PathIterator.SEG_LINETO) {
                            int x = (int) coords[0];
                            int y = (int) coords[1];
                            g2.setColor(createColor(i).darker().darker());
                            g2.fillOval(x - 2, y - 2, 4, 4);
                        }
                        pi.next();
                    }
                }
            }
        }
    }

    public Color createColor(int index) {
        return tr(COLORS[Math.abs(index) % COLORS.length]);
    }

    private Color tr(Color c) {
        return new Color(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, 0.5f);
    }
}