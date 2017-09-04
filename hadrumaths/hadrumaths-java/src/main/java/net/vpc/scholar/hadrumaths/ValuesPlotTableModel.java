package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.plot.ComplexAsDouble;
import net.vpc.scholar.hadrumaths.plot.PlotModelProvider;
import net.vpc.scholar.hadrumaths.plot.ValuesPlotModel;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ValuesPlotTableModel extends AbstractTableModel {
    private ValuesPlotModel model;
    private int rows=0;
    private int cols=0;

    public ValuesPlotTableModel(PlotModelProvider modelProvider) {
        this.model = (ValuesPlotModel) modelProvider.getModel();
        model.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                fireTableDataChanged();
            }
        });
        this.rows = model.getZ().length;
        this.cols = 0;
        for (int i = 0; i < rows; i++) {
            cols=Math.max(model.getZ()[i].length,cols);
        }
    }

    public int getRowCount() {
        return model.getZ().length;
    }

    public int getColumnCount() {
        return cols;
    }

    public String getColumnName(int columnIndex) {
        return String.valueOf(columnIndex+1);
    }

    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
//            switch (columnIndex) {
//                case 0: {
//                    //return "Index";
//                    return false;
//                }
//                case 1: {
//                    //return "Title";
//                    return true;
//                }
//                case 2: {
//                    //return "NodeType";
//                    return true;
//                }
//                case 3: {
//                    //return "LineType";
//                    return true;
//                }
//                case 4: {
//                    //return "Color";
//                    return true;
//                }
//                case 5: {
//                    //return "xmultiplier";
//                    return true;
//                }
//                case 6: {
//                    //return "ymultiplier";
//                    return true;
//                }
//                case 7: {
//                    //return "Visible";
//                    return true;
//                }
//            }
//            return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Complex[][] z = model.getZ();
        int c=columnIndex;
        Complex vv=(c<z[rowIndex].length)?z[rowIndex][c]:Complex.NaN;
        switch (model.getZDoubleFunction()){
            case COMPLEX:{
                return String.valueOf(vv);
            }
        }
        return String.valueOf(Maths.toDouble(vv,model.getZDoubleFunction()));
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
//            switch (columnIndex) {
//                case 0: {
//                    //return "Index";
//                    lines[rowIndex].index = (Integer) aValue;
//                    break;
//                }
//                case 1: {
//                    //return "Title";
//                    lines[rowIndex].title = (String) aValue;
//                    break;
//                }
//                case 2: {
//                    //return "NodeType";
//                    lines[rowIndex].nodeType = (Integer) aValue;
//                    break;
//                }
//                case 3: {
//                    //return "LineType";
//                    lines[rowIndex].lineType = (Integer) aValue;
//                    break;
//                }
//                case 4: {
//                    //return "Color";
//                    lines[rowIndex].color = (Color) aValue;
//                    break;
//                }
//                case 5: {
//                    //return "xmultiplier";
//                    lines[rowIndex].ymultiplier = ((Number) aValue).doubleValue();
//                    break;
//                }
//                case 6: {
//                    //return "ymultiplier";
//                    lines[rowIndex].ymultiplier = ((Number) aValue).doubleValue();
//                    break;
//                }
//                case 7: {
//                    //return "Visible";
//                    lines[rowIndex].visible = (Boolean) aValue;
//                    break;
//                }
//            }
    }

}
