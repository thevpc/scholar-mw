package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.Expr;

import javax.swing.table.AbstractTableModel;
import java.util.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 18 oct. 2006 09:57:45
 */
public class FunctionsXYTableModel extends AbstractTableModel {
    private static final long serialVersionUID = -1010101010101001063L;
    private Expr[] base;
    private String[] titles;
    private boolean[] selected;
//    private PropertyChangeSupport support;

    public FunctionsXYTableModel(Expr[] base) {
        setExpressions(base);
    }
    public void setExpressions(Expr[] base){
        this.base = base;
        TreeSet<String> seenTitles = new TreeSet<String>();
        ArrayList<String> orderedTitles = new ArrayList<String>();
        orderedTitles.add("Name");
        seenTitles.add("Name");
        for (Expr f : base) {
            if(f.hasProperties()) {
                Map<String, Object> values = f.getProperties();
                if (values != null) {
                    for (String t : values.keySet()) {
                        if (!seenTitles.contains(t)) {
                            seenTitles.add(t);
                            orderedTitles.add(t);
                        }
                    }
                }
            }
        }
        orderedTitles.add("Selected");
        seenTitles.add("Selected");
        titles = orderedTitles.toArray(new String[orderedTitles.size()]);
        selected = new boolean[base.length];
        int max = Math.min(50, base.length);
        for (int i = 0; i < max; i++) {
            selected[i] = true;
        }
        fireTableDataChanged();
//        support=new PropertyChangeSupport(this);
    }

    @Override
    public int getColumnCount() {
        return titles == null ? 1 : titles.length;
    }

    @Override
    public int getRowCount() {
        return base == null ? 0 : base.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String title=titles[columnIndex];
        Expr expr = base[rowIndex];
        Map<String, Object> values = expr.hasProperties()?expr.getProperties():null;
        Object value=values == null ? null : values.get(title);
        if(value==null){
            if("Name".equals(title)){
                return expr.getName();
            }else if("Selected".equals(title)){
                return selected[rowIndex];
            }
        }
        return values==null?null:values.get(title);
    }

    @Override
    public String getColumnName(int column) {
        return titles == null ? super.getColumnName(column):titles[column];
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        String title=titles[columnIndex];
        return "Selected".equals(title);
    }


    @Override
    public Class<?> getColumnClass(int columnIndex) {
        String title=titles[columnIndex];
        if("Name".equals(title)){
            return String.class;
        }else if("Selected".equals(title)){
            return Boolean.class;
        }
        return Object.class;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        String title=titles[columnIndex];
        if("Selected".equals(title)){
            selected[rowIndex] = (Boolean) aValue;
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    public void setAllSelectedByCell(int c,int r,boolean value) {
        String title=titles[c];
        if("Name".equals(title)){
            return ;
        }else if("Selected".equals(title)){
            return ;
        }
        Object v = base[r].getProperty(titles[c]);
        for (int i = 0; i < selected.length; i++) {
            Object v2 = base[i].getProperty(titles[c]);
            if(v==v2 || (v!=null && v2!=null  && v.equals(v2))){
                selected[i]=value;
            }
        }
        fireTableDataChanged();
    }

    public void setAllSelected(boolean value) {
        for (int i = 0; i < selected.length; i++) {
            selected[i]=value;
        }
        fireTableDataChanged();
    }

    public Expr[] getSelectedFunctions() {
        ArrayList<Expr> selectedFunctions = new ArrayList<Expr>();
        for (int i = 0; i < base.length; i++) {
            if (selected[i]) {
                selectedFunctions.add(base[i]);
            }
        }
        return selectedFunctions.toArray(new Expr[selectedFunctions.size()]);
    }

    public int[] getSelectedFunctionsIndexes() {
        ArrayList<Integer> selectedFunctions = new ArrayList<Integer>();
        for (int i = 0; i < base.length; i++) {
            if (selected[i]) {
                selectedFunctions.add(i);
            }
        }
        int[] ret=new int[selectedFunctions.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i]=selectedFunctions.get(i);
        }
        return ret;
    }
}
