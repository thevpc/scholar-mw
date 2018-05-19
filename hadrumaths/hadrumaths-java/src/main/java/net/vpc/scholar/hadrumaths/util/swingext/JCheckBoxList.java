package net.vpc.scholar.hadrumaths.util.swingext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 10 mai 2007 22:26:14
 */
public class JCheckBoxList extends JPanel {
    private Box verticalBox;
    Vector<JCheckBox> list = new Vector<JCheckBox>();

    public JCheckBoxList() {
        verticalBox = Box.createVerticalBox();

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        JButton all = new JButton("select All");
        all.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox jCheckBox : list) {
                    jCheckBox.setSelected(true);
                }
            }
        });
        JButton none = new JButton("select None");
        none.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox jCheckBox : list) {
                    jCheckBox.setSelected(false);
                }
            }
        });
        toolBar.add(all);
        toolBar.add(none);
        JScrollPane jsp = new JScrollPane(verticalBox);
        jsp.setPreferredSize(new Dimension(400, 400));
        this.setLayout(new BorderLayout());
        this.add(toolBar, BorderLayout.PAGE_START);
        this.add(jsp, BorderLayout.CENTER);

    }

    public JCheckBox addItem(String name) {
        return addItem(name, name);
    }

    public JCheckBox addItem(String name, String title) {
        JCheckBox c = new JCheckBox();
        c.setName(name);
        c.setText(title);
        addItem(c);
        return c;
    }

    public JCheckBox getItem(int index) {
        return list.get(index);
    }

    public void addItem(JCheckBox box) {
        verticalBox.add(box);
        list.add(box);
    }
}
