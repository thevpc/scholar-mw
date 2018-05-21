package net.vpc.scholar.hadruwaves.studio.standalone.editors;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
import net.vpc.lib.pheromone.application.Application;
import net.vpc.lib.pheromone.application.swing.DataTypeEditor;
import net.vpc.lib.pheromone.application.swing.ECTextField;
import net.vpc.lib.pheromone.application.swing.EditComponentsPanel;
import net.vpc.lib.pheromone.application.swing.Swings;
import net.vpc.lib.pheromone.ariana.util.Resources;
import net.vpc.scholar.hadruwaves.studio.standalone.ECExpressionField;
import net.vpc.scholar.hadruwaves.studio.standalone.TMWLabApplication;
import net.vpc.scholar.hadruwaves.mom.project.MomProjectExtraLayer;
import net.vpc.upa.types.ConstraintsException;

/**
 * Created by IntelliJ IDEA.
 * User: TAHA
 * Date: 11 mars 2004
 * Time: 21:15:55
 * To change this template use File | Settings | File Templates.
 */
public class LayersList extends JPanel {
//    MutableList areasList;
    JTable layersTable;
    MomProjectEditor editor;
    public LayersList(MomProjectEditor editor) {
        this(editor,null);
    }
    public LayersList(MomProjectEditor editor,Collection<MomProjectExtraLayer> couches) {
        super(new BorderLayout());
        this.editor=editor;
        layersTable = new JTable(new TModel(couches));
        TableColumn checkColumn = layersTable.getColumn(getTModel().getColumnName(0));
        checkColumn.setPreferredWidth(30);
//        TableColumn areaColumn = varsTable.getColumn(getTModel().getColumnName(1));
        layersTable.setPreferredScrollableViewportSize(new Dimension(100, 100));
        layersTable.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    int[] rc = Swings.getRowAndColumn(layersTable, e.getPoint());
                    if (rc != null) {
                        editLayer();
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
        layersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(layersTable));

        JToolBar jToolBar = new JToolBar(JToolBar.HORIZONTAL);
        jToolBar.add(Box.createGlue());
        JButton addVariableExpressionButton = new JButton(Resources.loadImageIcon("/images/net/vpc/swing/AddListItem.gif"));
        addVariableExpressionButton.setToolTipText(editor.getResources().get("addVariable"));
        addVariableExpressionButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addLayer();
            }
        });
        JButton removeVariableExpressionButton = new JButton(Resources.loadImageIcon("/images/net/vpc/swing/Delete.gif"));
        removeVariableExpressionButton.setToolTipText(editor.getResources().get("removeVariable"));
        removeVariableExpressionButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                removeLayer();
            }
        });
        JButton editVariableExpressionButton = new JButton(Resources.loadImageIcon("/images/net/vpc/swing/Editor.gif"));
        editVariableExpressionButton.setToolTipText(editor.getResources().get("editVariable"));
        editVariableExpressionButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                editLayer();
            }
        });
        jToolBar.add(addVariableExpressionButton);
        jToolBar.add(removeVariableExpressionButton);
        jToolBar.add(editVariableExpressionButton);
        jToolBar.setFloatable(false);
        add(jToolBar, BorderLayout.SOUTH);
    }

    public void addLayer(MomProjectExtraLayer a) {
        getTModel().addLayer(a);
        firePropertyChange("VariableExpressionList.VariableExpressionAdded", null, a);
    }

    public void addLayer() {
        ProjectLayerEditor areaEditor = new ProjectLayerEditor(editor);
        int i = layersTable.getSelectedRow();
        if (i >= 0) {
            MomProjectExtraLayer var = getTModel().getLayerAt(i);
        }
        while (true) {
            int btn = JOptionPane.showConfirmDialog(this, areaEditor, editor.getResources().get("addVariable"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (btn == JOptionPane.OK_OPTION) {
                try {
                    MomProjectExtraLayer a = areaEditor.getLayer();
                    a.setContext(editor.getProject0());
                    getTModel().addLayer(a);
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

    public void addLayerAddedListener(PropertyChangeListener listener) {
        addPropertyChangeListener("VariableExpressionList.VariableExpressionAdded", listener);
    }

    public void removeLayer() {
        int i = layersTable.getSelectedRow();
        if (i >= 0) {
            MomProjectExtraLayer area = getTModel().getLayerAt(i);
            getTModel().removeCouche(i);
            if (i < layersTable.getRowCount()) {
                layersTable.getSelectionModel().setLeadSelectionIndex(i);
                layersTable.getSelectionModel().setSelectionInterval(i, i);
            }
            firePropertyChange("VariableExpressionList.VariableExpressionRemoved", null, area);
        }
    }

    public void addLayerRemovedListener(PropertyChangeListener listener) {
        addPropertyChangeListener("VariableExpressionList.VariableExpressionRemoved", listener);
    }

    public int setSelectedVariableExpression(MomProjectExtraLayer area) {
        int x = getTModel().indexOfLayer(area);
        if (x >= 0) {
            layersTable.getSelectionModel().setLeadSelectionIndex(x);
            layersTable.getSelectionModel().setSelectionInterval(x, x);
        }
        return x;
    }

    public void editLayer() {
        int i = layersTable.getSelectedRow();
        if (i < 0) {
            return;
        }
        ProjectLayerEditor alyerEditor = new ProjectLayerEditor(editor);
        MomProjectExtraLayer area = getTModel().getLayerAt(i);
        alyerEditor.setLayer(area);
        while (true) {
            int btn = JOptionPane.showConfirmDialog(this, alyerEditor, editor.getResources().get("editVariable"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (btn == JOptionPane.OK_OPTION) {
                try {
                    area = alyerEditor.getLayer();
                    area.setContext(editor.getProject0());
                    getTModel().updateLayer(i, area);
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

    public MomProjectExtraLayer[] getLayers() {
        return ((TModel) layersTable.getModel()).getAllLayers();
//        Object[] o=areasList.getElements();
//        VariableExpression[] a=new VariableExpression[o.length];
//        System.arraycopy(o,0,a,0,o.length);
//        return a;
    }

    TModel getTModel() {
        return ((TModel) layersTable.getModel());
    }

    public void setLayers(MomProjectExtraLayer[] areas) {
        ((TModel) layersTable.getModel()).setLayers(areas);
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

    private class TModel extends AbstractTableModel {

        Vector<MomProjectExtraLayer> couches;

        public TModel(Collection<MomProjectExtraLayer> couches) {
            this.couches = couches==null?new Vector<MomProjectExtraLayer>() : new Vector<MomProjectExtraLayer>(couches);
        }

        
        public void setLayers(MomProjectExtraLayer[] a) {
            couches.clear();
            couches.addAll(Arrays.asList(a));
            fireTableDataChanged();
        }
        
        public void addLayer(MomProjectExtraLayer a) {
            couches.add(a);
            Collections.sort(couches);
            this.fireTableDataChanged();
        }

        public int indexOfLayer(MomProjectExtraLayer area) {
            return couches.indexOf(area);
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
//            if(columnIndex==0){
//                getVariableExpression(rowIndex).setEnabled(((Boolean)aValue).booleanValue());
//            }
        }

        public void removeCouche(int index) {
            couches.remove(index);
            this.fireTableRowsDeleted(index, index);
        }

//        public void updateVariableExpression(int index) {
//            couches.remove(index);
//            addCouche()
//            structureEditor.getStructure().getStructureContext().setExpression(areasVector.get(index));
//            this.fireTableRowsUpdated(index, index);
//        }
        public void updateLayer(int index, MomProjectExtraLayer newValue) {
            couches.remove(index);
            addLayer(newValue);
        //this.fireTableRowsUpdated(index, index);
        }

        public void setAllVariableExpressions(Collection<MomProjectExtraLayer> vals) {
            couches.clear();
            couches.addAll(vals);
            Collections.sort(couches);
            this.fireTableDataChanged();
        }

        public MomProjectExtraLayer[] getAllLayers() {
            return (MomProjectExtraLayer[]) couches.toArray(new MomProjectExtraLayer[couches.size()]);
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;//columnIndex==0;
        }

        public int getColumnCount() {
            return 4;
        }

        public int getRowCount() {
            return couches.size();
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            MomProjectExtraLayer a = getLayerAt(rowIndex);
            switch (columnIndex) {
                case 0:
                    return "<HTML><B>" + a.getName() + "</B></HTML>";
                case 1:
                    return a.getImpedanceExpression();
                case 2: {
                    return a.getMinZExpression();
                }
                case 3: {
                    return a.getWidthExpression();
                }
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
            }
            return Object.class;
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return editor.getResources().get("label");
                case 1:
                    return "Impedance";
                case 2:
                    return "Z Position";
                case 3:
                    return "Width";
            }
            return "";
        }

        public MomProjectExtraLayer getLayerAt(int index) {
            return couches.get(index);
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
    public static class ProjectLayerEditor extends JPanel {

        private ECTextField nameField;
        private ECExpressionField impedanceField;
        private ECExpressionField z0Field;
        private ECExpressionField width;

        public ProjectLayerEditor(MomProjectEditor editor) {
            super(new BorderLayout());
            nameField = new ECTextField("name", false);
            nameField.getHelper().setDescription(editor.getResources().get("label"));

            impedanceField = new ECExpressionField("value", false);
            impedanceField.getHelper().setDescription("Impedance");

            z0Field = new ECExpressionField("z0Field", false);
            z0Field.getHelper().setDescription("Z Position");

            width = new ECExpressionField("width", false);
            width.getHelper().setDescription("Width");
            add(new EditComponentsPanel(new DataTypeEditor[]{nameField,impedanceField,z0Field,width}, 1, false));
        }

        public MomProjectExtraLayer getLayer() {
            return new MomProjectExtraLayer(this.nameField.getString(),
                    this.impedanceField.getString(),
                    this.z0Field.getString(),
                    this.width.getString());

        }

        public void setLayer(MomProjectExtraLayer ll) {
            this.nameField.getHelper().setObject(ll.getName());
            this.impedanceField.getHelper().setObject(ll.getImpedanceExpression());
            this.z0Field.getHelper().setObject(ll.getMinZExpression());
            this.width.getHelper().setObject(ll.getWidthExpression());
        }
    }
}
