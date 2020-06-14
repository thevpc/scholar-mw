package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.Expr;

import javax.swing.table.AbstractTableModel;
import java.util.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 18 oct. 2006 09:57:45
 */
public class FunctionsXYTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    public static int MAX_SELECTED_EXPRESSIONS = 1000;
    private Expr[] base;
    private String[] titles;
    //    private boolean[] selected;
    private int maxSelected = 200;
    private boolean exclusive = false;
    private final LinkedHashSet<Integer> selectionOrder = new LinkedHashSet<>();
//    private PropertyChangeSupport support;

    public FunctionsXYTableModel(Expr[] base) {
        setExpressions(base);
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExpressions(Expr[] base) {
        this.base = base;
        TreeSet<String> seenTitles = new TreeSet<String>();
        ArrayList<String> orderedTitles = new ArrayList<String>();
        orderedTitles.add("Name");
        orderedTitles.add("Expr");
        seenTitles.add("Name");
        for (Expr f : base) {
            if (f.hasProperties()) {
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
        titles = orderedTitles.toArray(new String[0]);
        int max = Math.min(50, base.length);
        selectionOrder.clear();
        for (int i = 0; i < max; i++) {
            setSelected(i, true, false);
        }
        fireTableDataChanged();
//        support=new PropertyChangeSupport(this);
    }

    private synchronized boolean setSelected(int index, boolean v, boolean fire) {
        if (index >= 0 && index < base.length) {
            boolean old = isSelected(index);
            if (old != v) {
                if (v) {
                    selectionOrder.add(index);
                } else {
                    selectionOrder.remove(index);
                }
                rebuildSelectionOrder();
                if (fire) {
                    fireTableDataChanged();
                }
                return true;
            }
        }
        return false;
    }

    private synchronized boolean isSelected(int index) {
        return selectionOrder.contains(index);
    }

    private synchronized void rebuildSelectionOrder() {
        int effMaxSelected=0;
        if(exclusive){
            effMaxSelected=1;
        }else{
            effMaxSelected=maxSelected;
        }
        if (effMaxSelected > 0 && selectionOrder.size() > effMaxSelected) {
            for (Iterator<Integer> iterator = selectionOrder.iterator(); iterator.hasNext(); ) {
                Integer p = iterator.next();
                iterator.remove();
                if (selectionOrder.size() <= effMaxSelected) {
                    break;
                }
            }
        }
    }

    public int getMaxSelected() {
        return maxSelected;
    }

    public FunctionsXYTableModel setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
        rebuildSelectionOrder();
        fireTableDataChanged();
        return this;
    }
    public void setMaxSelected(int maxSelected) {
        this.maxSelected = maxSelected;
        rebuildSelectionOrder();
        fireTableDataChanged();
    }


    @Override
    public int getRowCount() {
        return base == null ? 0 : base.length;
    }

    @Override
    public int getColumnCount() {
        return titles == null ? 1 : titles.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String title = titles[columnIndex];
        Expr expr = base[rowIndex];
        Map<String, Object> values = expr.hasProperties() ? expr.getProperties() : null;
        Object value = values == null ? null : values.get(title);
        if (value == null) {
            if ("Name".equals(title)) {
                return expr.getTitle();
            } else if ("Expr".equals(title)) {
                return expr.toString();
            } else if ("Selected".equals(title)) {
                return isSelected(rowIndex);
            }
        }
        return values == null ? null : values.get(title);
    }

    @Override
    public String getColumnName(int column) {
        return titles == null ? super.getColumnName(column) : titles[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        String title = titles[columnIndex];
        if ("Name".equals(title)) {
            return String.class;
        } else if ("Expr".equals(title)) {
            return String.class;
        } else if ("Selected".equals(title)) {
            return Boolean.class;
        }
        return Object.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        String title = titles[columnIndex];
        return "Selected".equals(title);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        String title = titles[columnIndex];
        if ("Selected".equals(title)) {
            setSelected(rowIndex, (Boolean) aValue, true);
        }
    }

    public void setAllSelectedByCell(int c, int r, boolean value) {
        String title = titles[c];
        if ("Name".equals(title)) {
            return;
        } else if ("Selected".equals(title)) {
            return;
        } else if ("Expr".equals(title)) {
            return;
        }
        Object v = base[r].getProperty(titles[c]);
        boolean change = false;
        for (int i = 0; i < base.length; i++) {
            Object v2 = base[i].getProperty(titles[c]);
            if (v == v2 || (v != null && v2 != null && v.equals(v2))) {
                change |= setSelected(i, value, false);
            }
        }
        fireTableDataChanged();
    }

    public void setAllSelected(boolean value) {
        boolean change = false;
        for (int i = 0; i < base.length; i++) {
            change |= setSelected(i, value, false);
        }
        fireTableDataChanged();
    }

    public Expr[] getSelectedFunctions() {
        ArrayList<Expr> selectedFunctions = new ArrayList<Expr>();
        for (int i = 0; i < base.length; i++) {
            if (isSelected(i)) {
                selectedFunctions.add(base[i]);
            }
        }
        return selectedFunctions.toArray(new Expr[0]);
    }

    public int[] getSelectedFunctionsIndexes() {
        ArrayList<Integer> selectedFunctions = new ArrayList<Integer>();
        for (int i = 0; i < base.length; i++) {
            if (isSelected(i)) {
                selectedFunctions.add(i);
            }
        }
        int[] ret = new int[selectedFunctions.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = selectedFunctions.get(i);
        }
        return ret;
    }
}
