package net.vpc.scholar.hadrumaths.util;

import net.vpc.scholar.hadrumaths.util.swingext.SimpleRowHeaderRenderer;

import javax.swing.*;
import javax.swing.table.TableModel;

public class JTableHelper {
    JTable table;
    JScrollPane pane;

    public static JTableHelper prepareIndexedTable(TableModel model){
        JTableHelper t=new JTableHelper();
        t.table=new JTable(model);
        t.pane=new JScrollPane(t.table);
        SimpleRowHeaderRenderer r = new SimpleRowHeaderRenderer(t.table);
        r.install();
        return t;
    }

    public JTable getTable() {
        return table;
    }

    public JScrollPane getPane() {
        return pane;
    }
}
