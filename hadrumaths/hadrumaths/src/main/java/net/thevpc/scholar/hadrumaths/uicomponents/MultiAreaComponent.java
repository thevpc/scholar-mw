package net.thevpc.scholar.hadrumaths.uicomponents;

import net.thevpc.nuts.util.NBlankable;
import net.thevpc.nuts.util.NStringUtils;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.DomainScaleTool;
import net.thevpc.scholar.hadrumaths.geom.HGeometry;

import javax.swing.*;
import java.awt.*;
import java.awt.Dimension;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.util.Arrays;

public class MultiAreaComponent extends JPanel {

    public MultiAreaComponent(HGeometry... a) {
        this(null, a);
    }

    public MultiAreaComponent(String[] labels, HGeometry... a) {
        super(new BorderLayout());
        Domain d = a[0].getDomain();
        for (int i = 1; i < a.length; i++) d = d.expand(a[i].getDomain());
        DomainScaleTool t = DomainScaleTool.create(d, Domain.ofPoints(0, 0, 600, 600));
        String[] labels2 = new String[a.length];
        Area[] aa = new Area[a.length];
        for (int i = 0; i < aa.length; i++) {
            aa[i] = new Area(t.rescale(a[i].getPath()));
            int points = -1;
            if (aa[i].isPolygonal()) {
                points = countPoints(aa[i]);
            }
            if (labels != null && labels.length > i && !NBlankable.isBlank(labels[i])) {
                labels2[i] = labels[i];
            } else {
                labels2[i] = "Shape" + (points > 0 ? ("[" + points + "]") : "") + " " + i + " " + a[i].getDomain();
            }
        }
        init(labels2, aa);
    }


    public MultiAreaComponent(String[] labels, Area... a) {
        super(new BorderLayout());
        init(labels, a);
    }

    protected void init(String[] labels, Area... a) {
        AreaComponent canvas = new AreaComponent(labels, a);

        // build list model
        DefaultListModel<String> model = new DefaultListModel<>();
        for (int i = 0; i < a.length; i++) {
            Area aa = a[i];
            int points = -1;
            if (aa.isPolygonal()) {
                points = countPoints(aa);
            }
            model.addElement(labels != null && i < labels.length ? labels[i] : "Shape" + (points > 0 ? ("[" + points + "]") : "") + " " + i);
        }
        JList<String> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setVisibleRowCount(Math.min(a.length, 20));
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> l, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(l, value, index, isSelected, cellHasFocus);
                // color swatch
                Color c = canvas.createColor(index);
                lbl.setIcon(new Icon() {
                    public void paintIcon(Component comp, Graphics g, int x, int y) {
                        g.setColor(c);
                        g.fillRect(x, y, 12, 12);
                        g.setColor(Color.DARK_GRAY);
                        g.drawRect(x, y, 12, 12);
                    }

                    public int getIconWidth() {
                        return 14;
                    }

                    public int getIconHeight() {
                        return 14;
                    }
                });
                return lbl;
            }
        });
        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                canvas.selected.clear();
                canvas.selected.addAll(Arrays.stream(list.getSelectedIndices())
                        .boxed().collect(java.util.stream.Collectors.toList()));
                canvas.repaint();
            }
        });

        JButton selectAll = new JButton("All");
        JButton selectNone = new JButton("None");
        selectAll.addActionListener(e -> {
            list.clearSelection();
            canvas.selected.clear();
            canvas.repaint();
        });
        selectNone.addActionListener(e -> {
            list.setSelectionInterval(0, a.length - 1);
            canvas.selected.clear();
            for (int i = 0; i < a.length; i++) canvas.selected.add(i);
            canvas.repaint();
        });

        JPanel buttons = new JPanel(new GridLayout(1, 2, 2, 2));
        buttons.add(selectAll);
        buttons.add(selectNone);

        JPanel sidebar = new JPanel(new BorderLayout(4, 4));
        sidebar.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        sidebar.add(new JLabel("Shapes (" + a.length + ")"), BorderLayout.NORTH);
        sidebar.add(new JScrollPane(list), BorderLayout.CENTER);
        sidebar.add(buttons, BorderLayout.SOUTH);
        sidebar.setPreferredSize(new Dimension(160, 600));

        this.add(canvas, BorderLayout.CENTER);
        this.add(sidebar, BorderLayout.EAST);
    }

    public void showDialog(String msg) {
        JOptionPane.showMessageDialog(null, this,
                NStringUtils.firstNonBlank(msg, "Debug..."), JOptionPane.PLAIN_MESSAGE);
    }


    public static int countPoints(Area area) {
        int count = 0;
        PathIterator pi = area.getPathIterator(null);
        double[] coords = new double[6];
        while (!pi.isDone()) {
            int type = pi.currentSegment(coords);
            if (type == PathIterator.SEG_MOVETO || type == PathIterator.SEG_LINETO) {
                count++;
            }
            // SEG_CLOSE doesn't add a new point (it repeats the first)
            pi.next();
        }
        return count;
    }

}
