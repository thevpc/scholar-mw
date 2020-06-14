package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.locks.components;

import net.vpc.scholar.hadrumaths.plot.util.PrivateSwingUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.lang.reflect.Field;

class LockDefaultTableCellRenderer extends DefaultTableCellRenderer {

    public Component getTableCellRendererComponent(JTable table,
                                                   Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Color bg = getDefaultBG(table, value, isSelected, hasFocus, row, column);
        Color fg = getDefaultFG(table, value, isSelected, hasFocus, row, column);
        if (value instanceof Number && ((Number) value).intValue() > 1) {
            if (isSelected) {
                bg = (new Color(230, 135, 230));
//                        fg = (Color.YELLOW);
            } else {
                bg = (new Color(230, 135, 121));
//                        fg = (Color.BLACK);
            }
        }
        super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, column);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);
        setBackground(bg);
        setForeground(fg);
        return this;
    }

    private Color getDefaultBG(JTable table,
                               Object value, boolean isSelected, boolean hasFocus,
                               int row, int column) {
        Color bg = null;

        JTable.DropLocation dropLocation = table.getDropLocation();
        if (dropLocation != null
                && !dropLocation.isInsertRow()
                && !dropLocation.isInsertColumn()
                && dropLocation.getRow() == row
                && dropLocation.getColumn() == column) {

            bg = PrivateSwingUtils.getDefaultColor(this, ui, "Table.dropCellBackground");

            isSelected = true;
        }

        if (isSelected) {
            bg = (bg == null ? table.getSelectionBackground() : bg);
        } else {
            Color unselectedBackground = getUnselectedBackground();
            Color background = unselectedBackground != null
                    ? unselectedBackground
                    : table.getBackground();
            if (background == null || background instanceof javax.swing.plaf.UIResource) {
                Color alternateColor = PrivateSwingUtils.getDefaultColor(this, ui, "Table.alternateRowColor");
                if (alternateColor != null && row % 2 != 0) {
                    background = alternateColor;
                }
            }
            if (background != null) {
                bg = (background);
            }
        }


        if (hasFocus) {
            if (!isSelected && table.isCellEditable(row, column)) {
                Color col;
                col = PrivateSwingUtils.getDefaultColor(this, ui, "Table.focusCellBackground");
                if (col != null) {
                    bg = (col);
                }
            }
        }
        return bg;
    }

    private Color getDefaultFG(JTable table,
                               Object value, boolean isSelected, boolean hasFocus,
                               int row, int column) {
        Color fg = null;

        JTable.DropLocation dropLocation = table.getDropLocation();
        if (dropLocation != null
                && !dropLocation.isInsertRow()
                && !dropLocation.isInsertColumn()
                && dropLocation.getRow() == row
                && dropLocation.getColumn() == column) {

            fg = PrivateSwingUtils.getDefaultColor(this, ui, "Table.dropCellForeground");

            isSelected = true;
        }

        if (isSelected) {
            fg = (fg == null ? table.getSelectionForeground()
                    : fg);
        } else {
            Color unselectedForeground = getUnselectedForeground();
            fg = (unselectedForeground != null
                    ? unselectedForeground
                    : table.getForeground());
        }


        if (hasFocus) {
            if (!isSelected && table.isCellEditable(row, column)) {
                Color col;
                col = PrivateSwingUtils.getDefaultColor(this, ui, "Table.focusCellForeground");
                if (col != null) {
                    fg = (col);
                }
            }
        } else {
        }
        return fg;
    }

    private Color getUnselectedBackground() {
        try {
            Field unselectedBackgroundField = getClass().getSuperclass().getDeclaredField("unselectedBackground");
            unselectedBackgroundField.setAccessible(true);
            return (Color) unselectedBackgroundField.get(this);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unexpected");
        }
    }

    private Color getUnselectedForeground() {
        try {
            Field unselectedForegroundField = getClass().getSuperclass().getDeclaredField("unselectedForeground");
            unselectedForegroundField.setAccessible(true);
            return (Color) unselectedForegroundField.get(this);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unexpected");
        }
    }
}
