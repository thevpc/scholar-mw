package net.vpc.scholar.hadruwaves.studio.standalone.v1.editors;

//package org.vpc.momlib.application.editors;
//
//import org.vpc.lib.application.Application;
//import org.vpc.lib.swing.Swings;
//import org.vpc.lib.util.ConstraintsException;
//import org.vpc.lib.util.Resources;
//import org.vpc.momlib.planar.common.Area;
//
//import javax.swing.*;
//import javax.swing.table.AbstractTableModel;
//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.TableColumn;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.util.Arrays;
//import java.util.Vector;
//
///**
// * Created by IntelliJ IDEA.
// * User: TAHA
// * Date: 11 mars 2004
// * Time: 21:15:55
// * To change this template use File | Settings | File Templates.
// */
//public class AreaListEditor extends JPanel {
//    private static final long serialVersionUID=111111111113L;
////    MutableList areasList;
//    JTable areasTable;
//    StructureEditor structureEditor;
//    String category;
//
//    public AreaListEditor(StructureEditor structureEditor, String category) {
//        super(new BorderLayout());
//        this.structureEditor = structureEditor;
//        this.category = category;
//        areasTable = new JTable(new TModel());
//        TableColumn checkColumn = areasTable.getColumn(getTModel().getColumnName(0));
//        checkColumn.setPreferredWidth(30);
//        TableColumn areaColumn = areasTable.getColumn(getTModel().getColumnName(1));
//        areaColumn.setCellRenderer(new DefaultTableCellRenderer() {
//            private static final long serialVersionUID=111111111111L;
//            public void setValue(Object value) {
//                if (value instanceof Area) {
//                    Area a = (Area) value;
//                    setText(a.getName());
//                    setIcon(new ColoredIcon(a.getColor()));
//                } else {
//                    super.setValue(value);
//                }
//            }
//        });
//        areasTable.setPreferredScrollableViewportSize(new Dimension(100, 100));
//        areasTable.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent e) {
//                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
//                    int[] rc = Swings.getRowAndColumn(areasTable, e.getPoint());
//                    if (rc != null) {
//                        if (rc[1] == 0) {
//                            Area area = (Area) getTModel().getArea(rc[0]);
//                            area.setEnabled(!area.isEnabled());
//                            getTModel().updateArea(rc[0]);
//                            firePropertyChange("AreaListEditor.AreaChanged", null, area);
//                        } else {
//                            editArea();
//                        }
//                    }
//                }
//            }
//        });
//
////        areasList = new MutableList();
////        areasList.addMouseListener(new MouseAdapter() {
////            public void mouseClicked(MouseEvent e) {
////                if(e.getClickCount()==2 && SwingUtilities.isLeftMouseButton(e)){
////                    int i=areasList.locationToIndex(e.getPoint());
////                    if(i>=0){
////                        Area area=(Area)( ((Area) areasList.getSelectedValue()).clone());
////                        area.setEnabled(!area.isEnabled());
////                        areasList.setElementAt(area,i);
////                        areasList.repaint();
////                        firePropertyChange("AreaListEditor.AreaChanged",null,area);
////                    }
////                }
////            }
////        });
////        areasList.setCellRenderer(new DefaultListCellRenderer(){
////            public Component getListCellRendererComponent(
////                    JList list,
////                    Object value,
////                    int index,
////                    boolean isSelected,
////                    boolean cellHasFocus) {
////                Area a=(Area) value;
////                if(a==null){
////                    super.getListCellRendererComponent(list, "...NULL...", index, isSelected, cellHasFocus);
////                    setForeground(Color.RED);
////                }else{
////                    super.getListCellRendererComponent(list, a.getName()+ " ("+a.getFunctionMax()+")", index, isSelected, cellHasFocus);
////                    if(a.isEnabled()){
////                        setForeground(Color.BLUE);
////                    }else{
////                        setForeground(Color.GRAY);
////                    }
////                }
////                return this;
////            }
////        });
//        areasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        add(new JScrollPane(areasTable));
//
//        JToolBar jToolBar = new JToolBar(JToolBar.HORIZONTAL);
//        jToolBar.add(Box.createGlue());
//        JButton addAreaButton = new JButton(Resources.loadImageIcon("/images/net/vpc/swing/AddListItem.gif"));
//        addAreaButton.setToolTipText(Application.getResources().get("addMetal"));
//        addAreaButton.addActionListener(new ActionListener() {
//            public void actionPerformedImpl(ActionEvent e) {
//                addArea();
//            }
//        });
//        JButton removeAreaButton = new JButton(Resources.loadImageIcon("/images/net/vpc/swing/Delete.gif"));
//        removeAreaButton.setToolTipText(Application.getResources().get("removeMetal"));
//        removeAreaButton.addActionListener(new ActionListener() {
//            public void actionPerformedImpl(ActionEvent e) {
//                removeArea();
//            }
//        });
//        JButton editAreaButton = new JButton(Resources.loadImageIcon("/images/net/vpc/swing/Editor.gif"));
//        editAreaButton.setToolTipText(Application.getResources().get("editMetal"));
//        editAreaButton.addActionListener(new ActionListener() {
//            public void actionPerformedImpl(ActionEvent e) {
//                editArea();
//            }
//        });
//        jToolBar.add(addAreaButton);
//        jToolBar.add(removeAreaButton);
//        jToolBar.add(editAreaButton);
//        jToolBar.setFloatable(false);
//        add(jToolBar, BorderLayout.SOUTH);
//    }
//
//    public void addArea(Area a) {
//        getTModel().addArea(a);
//        firePropertyChange("AreaListEditor.AreaAdded", null, a);
//    }
//
//    public void addArea() {
//        AreaEditor areaEditor = new AreaEditor(structureEditor, category);
//        int i = areasTable.getSelectedRow();
//        if (i >= 0) {
//            Area area = getTModel().getArea(i);
//            areaEditor.setArea(area);
//            areaEditor.clearTitle();
//        }
//        while (true) {
//            int btn = JOptionPane.showConfirmDialog(this, areaEditor, Application.getResources().get("addMetal"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
//            if (btn == JOptionPane.OK_OPTION) {
//                try {
//                    Area a = areaEditor.getArea();
//                    getTModel().addArea(a);
//                    firePropertyChange("AreaListEditor.AreaAdded", null, a);
//                    return;
//                } catch (Exception e) {
//                    JOptionPane.showMessageDialog(this, e.getMessage(), Swings.getResources().get("error"), JOptionPane.ERROR_MESSAGE);
//                    e.printStackTrace();  //To change body of catch statement use Options | File Templates.
//                }
//            } else {
//                return;
//            }
//        }
//    }
//
//    public void removeArea() {
//        int i = areasTable.getSelectedRow();
//        if (i >= 0) {
//            Area area = getTModel().getArea(i);
//            getTModel().removeArea(i);
//            if (i < areasTable.getRowCount()) {
//                areasTable.getSelectionModel().setLeadSelectionIndex(i);
//                areasTable.getSelectionModel().setSelectionInterval(i, i);
//            }
//            firePropertyChange("AreaListEditor.AreaRemoved", null, area);
//        }
//    }
//
//    public int setSelectedArea(Area area) {
//        int x = getTModel().indexOfArea(area);
//        if (x >= 0) {
//            areasTable.getSelectionModel().setLeadSelectionIndex(x);
//            areasTable.getSelectionModel().setSelectionInterval(x, x);
//        }
//        return x;
//    }
//
//    public void editArea() {
//        int i = areasTable.getSelectedRow();
//        if (i < 0) {
//            return;
//        }
//        AreaEditor areaEditor = new AreaEditor(structureEditor, category);
//        Area area = getTModel().getArea(i);
//        areaEditor.setArea(area);
//        while (true) {
//            int btn = JOptionPane.showConfirmDialog(this, areaEditor, Application.getResources().get("editMetal"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
//            if (btn == JOptionPane.OK_OPTION) {
//                try {
//                    area = areaEditor.getArea();
//                    getTModel().updateArea(i, area);
////                    areasList.remove(i);
////                    areasList.add(i,area);
//                    firePropertyChange("AreaListEditor.AreaChanged", null, area);
//                    return;
//                } catch (ConstraintsException e) {
//                    JOptionPane.showMessageDialog(this, e.getMessage(), Swings.getResources().get("error"), JOptionPane.ERROR_MESSAGE);
//                    e.printStackTrace();  //To change body of catch statement use Options | File Templates.
//                } catch (NumberFormatException e) {
//                    JOptionPane.showMessageDialog(this, e.getMessage(), Swings.getResources().get("error"), JOptionPane.ERROR_MESSAGE);
//                    e.printStackTrace();  //To change body of catch statement use Options | File Templates.
//                    e.printStackTrace();  //To change body of catch statement use Options | File Templates.
//                }
//            } else {
//                return;
//            }
//        }
//    }
//
//    public Area[] getAreas() {
//        return ((TModel) areasTable.getModel()).getAllAreas();
////        Object[] o=areasList.getElements();
////        Area[] a=new Area[o.length];
////        System.arraycopy(o,0,a,0,o.length);
////        return a;
//    }
//
//    public TModel getTModel() {
//        return ((TModel) areasTable.getModel());
//    }
//
//    public void setAreas(Area[] areas) {
//        ((TModel) areasTable.getModel()).setAllAreas(areas);
////        areasList.removeAll();
////        areasList.setElements(areas);
//        firePropertyChange("AreaListEditor.AllAreasChanged", null, areas);
//    }
//
//    private class TModel extends AbstractTableModel {
//        private static final long serialVersionUID=111111111112L;
//        Vector<Area> areasVector = new Vector<Area>();
//
//        public void addArea(Area a) {
//            a.setStructure(structureEditor.getCurrentStructure());
//            areasVector.add(a);
//            this.fireTableRowsInserted(areasVector.size() - 1, areasVector.size() - 1);
//        }
//
//        public int indexOfArea(Area area) {
//            return areasVector.indexOf(area);
//        }
//
//        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
////            if(columnIndex==0){
////                getArea(rowIndex).setEnabled(((Boolean)aValue).booleanValue());
////            }
//        }
//
//        public void removeArea(int index) {
//
//            Area a=areasVector.remove(index);
//            if(a!=null){
//                a.setStructure(null);
//            }
//            this.fireTableRowsDeleted(index, index);
//        }
//
//        public void updateArea(int index) {
//            this.fireTableRowsUpdated(index, index);
//        }
//
//        public void updateArea(int index, Area newValue) {
//            areasVector.set(index, newValue);
//            this.fireTableRowsUpdated(index, index);
//        }
//
//        public void setAllAreas(Area[] newAreas) {
//            int l = areasVector.size();
//            if (l > 0) {
//                areasVector.clear();
//                this.fireTableRowsDeleted(0, l - 1);
//            }
//            areasVector.addAll(Arrays.asList(newAreas));
//            this.fireTableRowsInserted(0, areasVector.size() - 1);
//        }
//
//        public Area[] getAllAreas() {
//            return (Area[]) areasVector.toArray(new Area[areasVector.size()]);
//        }
//
//        public boolean isCellEditable(int rowIndex, int columnIndex) {
//            return false;//columnIndex==0;
//        }
//
//        public int getColumnCount() {
//            return 3;
//        }
//
//        public int getRowCount() {
//            return areasVector.size();
//        }
//
//        public Object getValueAt(int rowIndex, int columnIndex) {
//            Area a = getArea(rowIndex);
//            switch (columnIndex) {
//                case 0:
//                    return a.isEnabled() ? Boolean.TRUE : Boolean.FALSE;
//                case 1:
//                    return a;
//                case 2:
//                    {
//                        try {
//                            return new Integer(a.getFunctionMax());
//                        } catch (Exception e) {
//                            System.out.println(e);
//                        }
//                    }
//            }
//            return null;
//        }
//
//        public Class getColumnClass(int columnIndex) {
//            switch (columnIndex) {
//                case 0:
//                    return Boolean.class;
//                case 1:
//                    return Area.class;
//                case 2:
//                    return Integer.class;
//            }
//            return Object.class;
//        }
//
//        public String getColumnName(int column) {
//            switch (column) {
//                case 0:
//                    return Application.getResources().get("activated");
//                case 1:
//                    return Application.getResources().get("label");
//                case 2:
//                    return Application.getResources().get("functions");
//            }
//            return "";
//        }
//
//        public Area getArea(int index) {
//            return (Area) areasVector.get(index);
//        }
//    }
//
//    class ColoredIcon implements Icon {
//        Color color;
//
//        public ColoredIcon(Color color) {
//            this.color = color;
//        }
//
//        public int getIconHeight() {
//            return 8;
//        }
//
//        public int getIconWidth() {
//            return 8;
//        }
//
//        public void paintIcon(Component c, Graphics g, int x, int y) {
//            g.setColor(Color.BLACK);
//            g.drawRect(x, y, getIconWidth(), getIconHeight());
//            g.setColor(color);
//            g.fillRect(x, y, getIconWidth(), getIconHeight());
//        }
//    }
//}
