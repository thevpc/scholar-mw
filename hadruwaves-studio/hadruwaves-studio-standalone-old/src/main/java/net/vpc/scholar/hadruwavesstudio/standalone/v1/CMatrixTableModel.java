package net.thevpc.scholar.hadruwavesstudio.standalone.v1;


import javax.swing.table.AbstractTableModel;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;



/**
 * User: taha
 * Date: 31 aout 2003
 * Time: 10:31:44
 */
public class CMatrixTableModel extends AbstractTableModel {
    private ComplexMatrix matrix;

    public CMatrixTableModel(ComplexMatrix matrix) {
        this.matrix = matrix;
    }

    public int getRowCount() {
        return matrix.getRowCount();
    }

    public int getColumnCount() {
        return matrix.getColumnCount()+ 1;
    }

    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                {
                    return "Num";
                }
            case 1:
                {
                    return String.valueOf(columnIndex - 1);
                }
        }
        return null;
    }

    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                {
                    return Integer.class;
                }
            default :
                {
                    return String.class;
                }
        }
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                {
                    return new Integer(rowIndex + 1);
                }
            default:
                {
                    return matrix.get(rowIndex, columnIndex - 1).toString();
                }
        }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        try {
            matrix.set(rowIndex, columnIndex - 1,Complex.of((String) aValue));
        } catch (Exception e) {
            e.printStackTrace();
        }
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public ComplexMatrix getMatrix() {
        return matrix;
    }

    public void setMatrix(ComplexMatrix matrix) {
        this.matrix = matrix;
    }
}
