package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.locks.components;

import net.thevpc.echo.Application;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

class LocksDefaultTableCellRenderer extends DefaultTableCellRenderer {
    private Application app;

    public LocksDefaultTableCellRenderer(Application app) {
        this.app = app;
    }

    public Component getTableCellRendererComponent(JTable table,
                                                   Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        if (value instanceof Date) {
            value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
        }
        Component c = super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, column);
//        if(column==3){
//            super.setIcon(
//                    app.iconSet().icon("Delete").get()
//            );
//        }
        return c;
    }
}
