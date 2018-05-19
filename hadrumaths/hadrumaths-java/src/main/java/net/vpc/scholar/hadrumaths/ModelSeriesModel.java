package net.vpc.scholar.hadrumaths;

import javax.swing.table.AbstractTableModel;
import java.awt.*;

class ModelSeriesModel extends AbstractTableModel {
    private ModelSeriesItem[] lines;

    ModelSeriesModel(ModelSeriesItem[] lines) {
        this.lines = lines;
    }

    public int getRowCount() {
        return lines.length;
    }

    public ModelSeriesItem[] getLines() {
        return lines;
    }

    public int getColumnCount() {
        return 7;
    }

    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
//            case 0: {
//                return "Index";
//            }
            case 0: {
                return "Visible";
            }
            case 1: {
                return "Title";
            }
            case 2: {
                return "NodeType";
            }
            case 3: {
                return "LineType";
            }
            case 4: {
                return "Color";
            }
            case 5: {
                return "xfactor";
            }
            case 6: {
                return "yfactor";
            }
        }
        return null;
    }

    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
//            case 0: {
//                //return "Index";
//                return Integer.class;
//            }
            case 0: {
                //return "Visible";
                return Boolean.class;
            }
            case 1: {
                //return "Title";
                return String.class;
            }
            case 2: {
                //return "NodeType";
                return Integer.class;
            }
            case 3: {
                //return "LineType";
                return Integer.class;
            }
            case 4: {
                //return "Color";
                return Color.class;
            }
            case 5: {
                //return "xmultiplier";
                return Double.class;
            }
            case 6: {
                //return "ymultiplier";
                return Double.class;
            }
        }
        return null;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
//            case 0: {
//                //return "Index";
//                return false;
//            }
            case 0: {
                //return "Visible";
                return true;
            }
            case 1: {
                //return "Title";
                return true;
            }
            case 2: {
                //return "NodeType";
                return true;
            }
            case 3: {
                //return "LineType";
                return true;
            }
            case 4: {
                //return "Color";
                return true;
            }
            case 5: {
                //return "xmultiplier";
                return true;
            }
            case 6: {
                //return "ymultiplier";
                return true;
            }
        }
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
//            case 0: {
//                //return "Index";
//                return lines[rowIndex].getIndex();
//            }
            case 0: {
                //return "Visible";
                return lines[rowIndex].isVisible();
            }
            case 1: {
                //return "Title";
                return lines[rowIndex].getTitle();
            }
            case 2: {
                //return "NodeType";
                return lines[rowIndex].getNodeType();
            }
            case 3: {
                //return "LineType";
                return lines[rowIndex].getLineType();
            }
            case 4: {
                //return "Color";
                return lines[rowIndex].getColor();
            }
            case 5: {
                //return "xmultiplier";
                return lines[rowIndex].getXmultiplier();
            }
            case 6: {
                //return "ymultiplier";
                return lines[rowIndex].getYmultiplier();
            }
        }
        return null;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        switch (columnIndex) {
//            case 0: {
//                //return "Index";
//                lines[rowIndex].setIndex((Integer) aValue);
//                break;
//            }
            case 0: {
                //return "Visible";
                lines[rowIndex].setVisible((Boolean) aValue);
                fireTableCellUpdated(rowIndex, columnIndex);
                break;
            }
            case 1: {
                //return "Title";
                lines[rowIndex].setTitle((String) aValue);
                fireTableCellUpdated(rowIndex, columnIndex);
                break;
            }
            case 2: {
                //return "NodeType";
                lines[rowIndex].setNodeType((Integer) aValue);
                fireTableCellUpdated(rowIndex, columnIndex);
                break;
            }
            case 3: {
                //return "LineType";
                lines[rowIndex].setLineType((Integer) aValue);
                fireTableCellUpdated(rowIndex, columnIndex);
                break;
            }
            case 4: {
                //return "Color";
                lines[rowIndex].setColor((Color) aValue);
                fireTableCellUpdated(rowIndex, columnIndex);
                break;
            }
            case 5: {
                //return "xmultiplier";
                lines[rowIndex].setYmultiplier(((Number) aValue).doubleValue());
                fireTableCellUpdated(rowIndex, columnIndex);
                break;
            }
            case 6: {
                //return "ymultiplier";
                lines[rowIndex].setYmultiplier(((Number) aValue).doubleValue());
                fireTableCellUpdated(rowIndex, columnIndex);
                break;
            }
        }
    }

    public void setSelectAll() {
        for (int i = 0; i < lines.length; i++) {
            ModelSeriesItem line = lines[i];
            if (!line.isVisible()) {
                line.setVisible(true);
                fireTableCellUpdated(i, 0);
            }
        }
    }

    public void setSelectNone() {
        for (int i = 0; i < lines.length; i++) {
            ModelSeriesItem line = lines[i];
            if (line.isVisible()) {
                line.setVisible(false);
                fireTableCellUpdated(i, 0);
            }
        }
    }

}
