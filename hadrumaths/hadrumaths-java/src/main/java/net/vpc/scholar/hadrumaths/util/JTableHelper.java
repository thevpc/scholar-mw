package net.vpc.scholar.hadrumaths.util;

import net.vpc.scholar.hadrumaths.util.swingext.SimpleRowHeaderRenderer;

import javax.swing.*;
import javax.swing.table.TableModel;

public class JTableHelper {
    JTable table;
    JScrollPane pane;

    public static JTableHelper prepareIndexedTable(TableModel model){
        JTableHelper t=new JTableHelper();
        t.table=new JTable(){
//            public boolean getScrollableTracksViewportWidth()
//            {
//                return getPreferredSize().width < getParent().getWidth();
//            }
        };
        t.table.setAutoCreateRowSorter(true);
        t.table.setModel(model);
        t.pane=new JScrollPane(t.table);
        t.table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
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
