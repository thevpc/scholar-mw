package net.thevpc.scholar.hadruwavesstudio.standalone.v1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import net.vpc.lib.pheromone.application.swing.Swings;
import net.vpc.lib.pheromone.ariana.util.Resources;

import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.MomProjectEditor;
import net.thevpc.scholar.hadruwaves.mom.project.MomProject;
import net.thevpc.scholar.hadruwaves.mom.project.common.VariableExpression;
import net.vpc.upa.types.ConstraintsException;

/**
 * Created by IntelliJ IDEA. User: TAHA Date: 11 mars 2004 Time: 21:15:55 To
 * change this template use File | Settings | File Templates.
 */
public class VarList extends JPanel {
//    MutableList areasList;

    JTable varsTable;
    MomProjectEditor structureEditor;

    public VarList(MomProjectEditor structureEditor) {
        super(new BorderLayout());
        this.structureEditor = structureEditor;
        varsTable = new JTable(new TModel());
        TableColumn checkColumn = varsTable.getColumn(getTModel().getColumnName(0));
        checkColumn.setPreferredWidth(30);
//        TableColumn areaColumn = varsTable.getColumn(getTModel().getColumnName(1));
        varsTable.setPreferredScrollableViewportSize(new Dimension(100, 100));
        varsTable.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    int[] rc = Swings.getRowAndColumn(varsTable, e.getPoint());
                    if (rc != null) {
                        editVariableExpression();
                    }
                }
            }
        });

//        areasList = new MutableList();
//        areasList.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent e) {
//                if(e.getClickCount()==2 && SwingUtilities.isLeftMouseButton(e)){
//                    int i=areasList.locationToIndex(e.getPoint());
//                    if(i>=0){
//                        VariableExpression area=(VariableExpression)( ((VariableExpression) areasList.getSelectedValue()).clone());
//                        area.setEnabled(!area.isEnabled());
//                        areasList.setElementAt(area,i);
//                        areasList.repaint();
//                        firePropertyChange("VariableExpressionList.VariableExpressionChanged",null,area);
//                    }
//                }
//            }
//        });
//        areasList.setCellRenderer(new DefaultListCellRenderer(){
//            public Component getListCellRendererComponent(
//                    JList list,
//                    Object value,
//                    int index,
//                    boolean isSelected,
//                    boolean cellHasFocus) {
//                VariableExpression a=(VariableExpression) value;
//                if(a==null){
//                    super.getListCellRendererComponent(list, "...NULL...", index, isSelected, cellHasFocus);
//                    setForeground(Color.RED);
//                }else{
//                    super.getListCellRendererComponent(list, a.getName()+ " ("+a.getFunctionMax()+")", index, isSelected, cellHasFocus);
//                    if(a.isEnabled()){
//                        setForeground(Color.BLUE);
//                    }else{
//                        setForeground(Color.GRAY);
//                    }
//                }
//                return this;
//            }
//        });
        varsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(varsTable));

        JToolBar jToolBar = new JToolBar(JToolBar.HORIZONTAL);
        jToolBar.add(Box.createGlue());
        JButton addVariableExpressionButton = new JButton(Resources.loadImageIcon("/images/net/vpc/swing/AddListItem.gif"));
        addVariableExpressionButton.setToolTipText(structureEditor.getResources().get("addVariable"));
        addVariableExpressionButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addVariableExpression();
            }
        });
        JButton removeVariableExpressionButton = new JButton(Resources.loadImageIcon("/images/net/vpc/swing/Delete.gif"));
        removeVariableExpressionButton.setToolTipText(structureEditor.getResources().get("removeVariable"));
        removeVariableExpressionButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                removeVariableExpression();
            }
        });
        JButton editVariableExpressionButton = new JButton(Resources.loadImageIcon("/images/net/vpc/swing/Editor.gif"));
        editVariableExpressionButton.setToolTipText(structureEditor.getResources().get("editVariable"));
        editVariableExpressionButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                editVariableExpression();
            }
        });
        jToolBar.add(addVariableExpressionButton);
        jToolBar.add(removeVariableExpressionButton);
        jToolBar.add(editVariableExpressionButton);
        jToolBar.setFloatable(false);
        add(jToolBar, BorderLayout.SOUTH);
    }

    public void addVariableExpression(VariableExpression a) {
        getTModel().addVariableExpression(a);
        firePropertyChange("VariableExpressionList.VariableExpressionAdded", null, a);
    }

    public void addVariableExpression() {
        VariableExpressionEditor areaEditor = new VariableExpressionEditor(structureEditor);
        int i = varsTable.getSelectedRow();
        if (i >= 0) {
            VariableExpression var = getTModel().getVariableExpression(i);
            areaEditor.setVarName(var.getName());
            areaEditor.setVarExpression(var.getExpression());
        }
        while (true) {
            int btn = JOptionPane.showConfirmDialog(this, areaEditor, getResources().get("addVariable"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (btn == JOptionPane.OK_OPTION) {
                try {
                    VariableExpression a = areaEditor.getVariableExpression();
                    getTModel().addVariableExpression(a);
                    firePropertyChange("VariableExpressionList.VariableExpressionAdded", null, a);
                    return;
                } catch (ConstraintsException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), Swings.getResources().get("error"), JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), Swings.getResources().get("error"), JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            } else {
                return;
            }
        }
    }

    public void addVariableExpressionAddedListener(PropertyChangeListener listener) {
        addPropertyChangeListener("VariableExpressionList.VariableExpressionAdded", listener);
    }

    public void removeVariableExpression() {
        int i = varsTable.getSelectedRow();
        if (i >= 0) {
            VariableExpression area = getTModel().getVariableExpression(i);
            getTModel().removeVariableExpression(i);
            if (i < varsTable.getRowCount()) {
                varsTable.getSelectionModel().setLeadSelectionIndex(i);
                varsTable.getSelectionModel().setSelectionInterval(i, i);
            }
            firePropertyChange("VariableExpressionList.VariableExpressionRemoved", null, area);
        }
    }

    public void addVariableExpressionRemovedListener(PropertyChangeListener listener) {
        addPropertyChangeListener("VariableExpressionList.VariableExpressionRemoved", listener);
    }

    public int setSelectedVariableExpression(VariableExpression area) {
        int x = getTModel().indexOfVariableExpression(area);
        if (x >= 0) {
            varsTable.getSelectionModel().setLeadSelectionIndex(x);
            varsTable.getSelectionModel().setSelectionInterval(x, x);
        }
        return x;
    }

    public void editVariableExpression() {
        int i = varsTable.getSelectedRow();
        if (i < 0) {
            return;
        }
        VariableExpressionEditor areaEditor = new VariableExpressionEditor(structureEditor);
        VariableExpression area = getTModel().getVariableExpression(i);
        areaEditor.setVariableExpression(area);
        while (true) {
            int btn = JOptionPane.showConfirmDialog(this, areaEditor, getResources().get("editVariable"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (btn == JOptionPane.OK_OPTION) {
                try {
                    area = areaEditor.getVariableExpression();
                    getTModel().updateVariableExpression(i, area);
//                    areasList.remove(i);
//                    areasList.add(i,area);
                    firePropertyChange("VariableExpressionList.VariableExpressionChanged", null, area);
                    return;
                } catch (ConstraintsException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), Swings.getResources().get("error"), JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();  //To change body of catch statement use Options | File Templates.
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), Swings.getResources().get("error"), JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();  //To change body of catch statement use Options | File Templates.
                    e.printStackTrace();  //To change body of catch statement use Options | File Templates.
                }
            } else {
                return;
            }
        }
    }

    public void addVariableExpressionChangedListener(PropertyChangeListener listener) {
        addPropertyChangeListener("VariableExpressionList.VariableExpressionChanged", listener);
    }

    public VariableExpression[] getVariableExpressions() {
        return ((TModel) varsTable.getModel()).getAllVariableExpressions();
//        Object[] o=areasList.getElements();
//        VariableExpression[] a=new VariableExpression[o.length];
//        System.arraycopy(o,0,a,0,o.length);
//        return a;
    }

    public TModel getTModel() {
        return ((TModel) varsTable.getModel());
    }

    public void setVariableExpressions(VariableExpression[] areas) {
        ((TModel) varsTable.getModel()).setAllVariableExpressions(areas);
//        areasList.removeAll();
//        areasList.setElements(areas);
        firePropertyChange("VariableExpressionList.AllVariableExpressionsChanged", null, areas);
    }

    public void addAllVariableExpressionsChangedListener(PropertyChangeListener listener) {
        addPropertyChangeListener("VariableExpressionList.AllVariableExpressionsChanged", listener);
    }

    public void addVariableListener(PropertyChangeListener listener) {
        addPropertyChangeListener("VariableExpressionList.VariableExpressionAdded", listener);
        addPropertyChangeListener("VariableExpressionList.VariableExpressionRemoved", listener);
        addPropertyChangeListener("VariableExpressionList.VariableExpressionChanged", listener);
        addPropertyChangeListener("VariableExpressionList.AllVariableExpressionsChanged", listener);
    }

    protected Resources getResources() {
        return structureEditor.getResources();
    }

    private class TModel extends AbstractTableModel {

        Vector<VariableExpression> areasVector = new Vector<VariableExpression>();

        public void addVariableExpression(VariableExpression a) {
            for (Iterator i = areasVector.iterator(); i.hasNext();) {
                VariableExpression variableExpression = (VariableExpression) i.next();
                if (variableExpression.getName().equals(a.getName())) {
                    throw new IllegalArgumentException("Variable declared");
                }
            }
            areasVector.add(a);
            VariableExpression[] v = areasVector.toArray(new VariableExpression[areasVector.size()]);
            Arrays.sort(v);
            areasVector.clear();
            areasVector.addAll(Arrays.asList(v));
            structureEditor.getProject().setExpression(a);
//            this.fireTableRowsInserted(areasVector.size() - 1, areasVector.size() - 1);
            this.fireTableDataChanged();
        }

        public int indexOfVariableExpression(VariableExpression area) {
            return areasVector.indexOf(area);
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
//            if(columnIndex==0){
//                getVariableExpression(rowIndex).setEnabled(((Boolean)aValue).booleanValue());
//            }
        }

        public void removeVariableExpression(int index) {
            structureEditor.getProject().unsetExpression(areasVector.get(index).getName());
            areasVector.remove(index);
            this.fireTableRowsDeleted(index, index);
        }

        public void updateVariableExpression(int index) {
            structureEditor.getProject().setExpression(areasVector.get(index));
            this.fireTableRowsUpdated(index, index);
        }

        public void updateVariableExpression(int index, VariableExpression newValue) {
            areasVector.set(index, newValue);
            structureEditor.getProject().setExpression(areasVector.get(index));
            this.fireTableRowsUpdated(index, index);
        }

        public void setAllVariableExpressions(VariableExpression[] newVariableExpressions) {
            int l = areasVector.size();
            if (l > 0) {
                areasVector.clear();
                this.fireTableRowsDeleted(0, l - 1);
            }
            Arrays.sort(newVariableExpressions);
            areasVector.addAll(Arrays.asList(newVariableExpressions));
            this.fireTableRowsInserted(0, areasVector.size() - 1);
        }

        public VariableExpression[] getAllVariableExpressions() {
            return (VariableExpression[]) areasVector.toArray(new VariableExpression[areasVector.size()]);
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;//columnIndex==0;
        }

        public int getColumnCount() {
            return 5;
        }

        public int getRowCount() {
            return areasVector.size();
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            VariableExpression a = getVariableExpression(rowIndex);
            switch (columnIndex) {
                case 0:
                    return "<HTML><B>" + a.getName() + "</B></HTML>";
                case 1:
                    return a.getExpression();
                case 2: {
                    switch (a.getUnit()) {
                        case LEN: {
                            double dd = structureEditor.getProject0().getDimensionUnit();
                            return ((dd == 1) ? "m" : (dd == 0.1) ? "dm" : (dd == 0.01) ? "cm" : (dd == 0.001) ? "mm" : (dd == 1E-4) ? "1E-4m" : (dd == 1E-5) ? "1E-5m" : (dd == 1E-6) ? "mirco-m" : (dd + "m")) + (" (Dimension)");
                        }
                        case LAMBDA: {
                            return ("lambda (Dimension)");
                        }
                        case FREQ: {
                            double dd = structureEditor.getProject0().getFrequencyUnit();
                            return ((dd == 1) ? "Hz" : (dd == 1000) ? "KHz" : (dd == 1E6) ? "MHz" : (dd == 1E9) ? "GHz" : (dd + "Hz")) + (" (Frequence)");
                        }
                        default: {
                            return a.getUnit().toString();
                        }
                    }
                }
                case 3: {
                    try {
                        VariableExpression[] variableExpressions = getAllVariableExpressions();
                        MomProject structureContext = new MomProject();
                        structureContext.setDimensionUnit(structureEditor.getProject0().getDimensionUnit());
                        structureContext.setFrequencyUnit(structureEditor.getProject0().getFrequencyUnit());
                        for (VariableExpression variableExpression : variableExpressions) {
                            structureContext.setExpression(variableExpression);
                        }
                        return structureContext.evaluate(a.getName(), a.getUnit()).toString();
                    } catch (Exception e) {
                        return "<HTML><FONT COLOR=RED>ERREUR</FONT><HTML>";
                    }
                }
                case 4:
                    return a.getDesc();
            }
            return null;
        }

        @Override
        public Class getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return String.class;
                case 1:
                    return String.class;
                case 2:
                    return String.class;
                case 3:
                    return String.class;
                case 4:
                    return String.class;
            }
            return Object.class;
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return getResources().get("label");
                case 1:
                    return "Expression";
                case 3:
                    return "Valeur";
                case 2:
                    return "Unite";
                case 4:
                    return "Desc";
            }
            return "";
        }

        public VariableExpression getVariableExpression(int index) {
            return areasVector.get(index);
        }
    }
//    public void storeConfiguration(Configuration configuration,String key) throws IOException{
//        VariableExpression[]  variableExpressions= getVariableExpressions();
//        for(int i=0;i<variableExpressions.length;i++){
//            configuration.setString(
//                    key+".vars."+variableExpressions[i].getName(),
//                    variableExpressions[i].getExpression());
//        }
//    }
//
//    public void loadConfiguration(Configuration configuration,String key) throws IOException{
//        String[] ch=configuration.getChildrenKeys(key+".vars",false);
//        getTModel().areasVector.clear();
//        for (int i = 0; i < ch.length; i++) {
//            getTModel().areasVector.add(new VariableExpression(ch[i].substring(key.length()+1),configuration.getString(ch[i])));
//        }
//        getTModel().fireTableDataChanged();
//    }
}
