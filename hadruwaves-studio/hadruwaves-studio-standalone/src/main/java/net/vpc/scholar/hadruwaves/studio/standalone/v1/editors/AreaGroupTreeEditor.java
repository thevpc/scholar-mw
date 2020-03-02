package net.vpc.scholar.hadruwaves.studio.standalone.v1.editors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import net.vpc.common.swings.JDropDownButton;
import net.vpc.lib.pheromone.ariana.util.Resources;

import net.vpc.scholar.hadruwaves.mom.project.MomProject;
import net.vpc.scholar.hadruwaves.mom.project.MomProjectFactory;
import net.vpc.scholar.hadruwaves.mom.project.MomProjectItem;
import net.vpc.scholar.hadruwaves.mom.project.MomProjectListener;
import net.vpc.scholar.hadruwaves.mom.project.areamaterial.AreaMaterial;
import net.vpc.scholar.hadruwaves.mom.project.areamaterial.PecMaterial;
import net.vpc.scholar.hadruwaves.mom.project.areamaterial.PlanarSourceMaterial;
import net.vpc.scholar.hadruwaves.mom.project.areamaterial.SurfaceImpedanceMaterial;
import net.vpc.scholar.hadruwaves.mom.project.common.Area;
import net.vpc.scholar.hadruwaves.mom.project.common.AreaGroup;
import net.vpc.scholar.hadruwaves.mom.project.common.AreaZone;

/**
 * Created by IntelliJ IDEA.
 * User: TAHA
 * Date: 11 mars 2004
 * Time: 21:15:55
 * To change this template use File | Settings | File Templates.
 */
public class AreaGroupTreeEditor extends JPanel {

    private static final long serialVersionUID = 111111111123L;
//    MutableList areasList;
    JTree groupsTree;
    MomProjectEditor editor;
    MomProject structure;
    DefaultTreeModel newModel;
    DefaultMutableTreeNode root;
//    private TreeCellRenderer checksRenderer=null;
    private TreeCellRenderer labelsRenderer = new DefaultTreeCellRenderer() {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            value = ((DefaultMutableTreeNode) value).getUserObject();
            Icon icon = null;
            boolean checked = true;
            if (value instanceof Area) {
                Area a = ((Area) value);
                value = a.getName();
                AreaMaterial cat = a.getMaterial();
                icon =
                        PecMaterial.class.isInstance(value) ? new ColoredRectIcon(a.getColor()) : SurfaceImpedanceMaterial.class.isInstance(value) ? new ColoredLosangeIcon(a.getColor()) : PlanarSourceMaterial.class.isInstance(value) ? new ColoredCircleIcon(a.getColor()) : (Icon) null;
                checked = a.isEnabled();
            } else if (value instanceof AreaGroup) {
                value = "[" + ((AreaGroup) value).getName() + "]";
                icon = UIManager.getIcon("Tree.closedIcon");
            }
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            setIcon(icon);
            setForeground(checked ? Color.black : Color.gray);
//                check.setSelected(checked);
//                this.setSize(new Dimension(200,20));
            return this;
        }
    };
    MomProjectListener structureListener = new MomProjectListener() {

        public void propertyChange(PropertyChangeEvent evt) {
            String propertyName = evt.getPropertyName();
            if (MomProject.AREA_ADDED.equals(propertyName)) {
                DefaultMutableTreeNode node = getNode(((AreaZone) evt.getOldValue()).getParentGroup());
                if (node == null) {
                    node = root;
                }
                DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(evt.getOldValue());
                int childIndex = ((Integer) evt.getNewValue()).intValue();
                if (childIndex >= node.getChildCount()) {
                    node.add(newChild);
                } else {
                    node.insert(newChild, childIndex);
                }
            } else if (MomProject.AREA_REMOVED.equals(propertyName)) {
                DefaultMutableTreeNode parent = getNode(((AreaZone) evt.getNewValue()));
                DefaultMutableTreeNode node = null;
                if (parent != null) {
                    for (int i = 0; i < parent.getChildCount(); i++) {
                        DefaultMutableTreeNode childAt = (DefaultMutableTreeNode) parent.getChildAt(i);
                        if (childAt.getUserObject() == ((AreaZone) evt.getOldValue())) {
                            node = childAt;
                            break;
                        }
                    }
                    if (node != null) {
                        node.removeFromParent();
                    }
                }
            } else if (MomProject.AREA_UPDATED.equals(propertyName)) {
                DefaultMutableTreeNode parent = getNode(((AreaZone) evt.getNewValue()).getParentGroup());
                for (int i = 0; i < parent.getChildCount(); i++) {
                    DefaultMutableTreeNode childAt = (DefaultMutableTreeNode) parent.getChildAt(i);
                    if (childAt.getUserObject() == ((AreaZone) evt.getOldValue())) {
                        childAt.setUserObject(((AreaZone) evt.getNewValue()));
                    }
                }
//                nodeChanged(evt.getNewValue());
            } else if (MomProject.AREAGROUP_ADDED.equals(propertyName)) {
                getNode(((AreaZone) evt.getOldValue()).getParentGroup()).insert(
                        new DefaultMutableTreeNode(evt.getOldValue()), ((Integer) evt.getNewValue()).intValue());
            } else if (MomProject.AREAGROUP_REMOVED.equals(propertyName)) {
                DefaultMutableTreeNode parent = getNode(((AreaZone) evt.getNewValue()));
                DefaultMutableTreeNode node = null;
                if (parent != null) {
                    for (int i = 0; i < parent.getChildCount(); i++) {
                        DefaultMutableTreeNode childAt = (DefaultMutableTreeNode) parent.getChildAt(i);
                        if (childAt.getUserObject() == ((AreaZone) evt.getOldValue())) {
                            node = childAt;
                            break;
                        }
                    }
                    if (node != null) {
                        node.removeFromParent();
                    }
                }
            } else if (MomProject.AREAGROUP_UPDATED.equals(propertyName)) {
                DefaultMutableTreeNode parent = getNode(((AreaZone) evt.getNewValue()).getParentGroup());
                if (parent != null) {
                    for (int i = 0; i < parent.getChildCount(); i++) {
                        DefaultMutableTreeNode childAt = (DefaultMutableTreeNode) parent.getChildAt(i);
                        if (childAt.getUserObject() == ((AreaZone) evt.getOldValue())) {
                            childAt.setUserObject(((AreaZone) evt.getNewValue()));
                        }
                    }
                }
            }
            groupsTree.setModel(new DefaultTreeModel(root));
        }
    };

    public DefaultMutableTreeNode getNode(AreaZone o) {
        if (o == root.getUserObject()) {
            return root;
        } else if (o instanceof Area) {
            DefaultMutableTreeNode node = getNode(((Area) o).getParentGroup());
            if (node == null) {
                return null;
            }
            for (int i = 0; i < node.getChildCount(); i++) {
                DefaultMutableTreeNode childAt = (DefaultMutableTreeNode) node.getChildAt(i);
                if (childAt.getUserObject() == o) {
                    return childAt;
                }
            }
            return null;
        } else if (o instanceof AreaGroup) {
            DefaultMutableTreeNode node = getNode(((AreaGroup) o).getParentGroup());
            if (node == null) {
                return null;
            }
            for (int i = 0; i < node.getChildCount(); i++) {
                DefaultMutableTreeNode childAt = (DefaultMutableTreeNode) node.getChildAt(i);
                if (childAt.getUserObject() == o) {
                    return childAt;
                }
            }
            return null;
        }
        return null;
    }

    public AreaGroupTreeEditor(MomProjectEditor aStructureEditor) {
        super(new BorderLayout());
        this.editor = aStructureEditor;
        groupsTree = new JTree(newModel = new DefaultTreeModel(root = new DefaultMutableTreeNode("***")));
        groupsTree.setRootVisible(true);
        groupsTree.setCellRenderer(labelsRenderer);
        add(new JScrollPane(groupsTree));
        createPopupMenu();
//        checksRenderer=new CheckTreeCellRenderer(groupsTree){
//            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
//                Icon icon=null;
//                boolean checked=false;
//                if(value instanceof Area){
//                    Area a=((Area)value);
//                    value=a.getName();
//                    String cat=AreaManager.getAreaDesc(a.getType()).getCategory().getName();
//                    icon=
//                            cat.equals(AreaManager.DIELECTRIC_CATEGORY)?new ColoredRect3DIcon(a.getColor()) :
//                            cat.equals(AreaManager.ZS_CATEGORY)?new ColoredLosangeIcon(a.getColor()) :
//                            cat.equals(AreaManager.METAL_CATEGORY)?new ColoredRectIcon(a.getColor()) :
//                            cat.equals(AreaManager.SOURCE_CATEGORY)?new ColoredCircleIcon(a.getColor()) :
//                            (Icon)null;
//                    checked=a.isEnabled();
//                }else if(value instanceof AreaGroup){
//                    value="["+((AreaGroup)value).getName()+"]";
//                }
//                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
//                ((DefaultTreeCellRenderer)renderer).setIcon(icon);
//                check.setSelected(checked);
//                this.setSize(new Dimension(200,20));
//                return this;
//            }
//        };
        JToolBar jToolBar = new JToolBar(JToolBar.HORIZONTAL);
        jToolBar.add(Box.createGlue());
        JDropDownButton addAreaButton = new JDropDownButton(Resources.loadImageIcon("/images/net/vpc/swing/AddListItem.gif"));
        addAreaButton.setToolTipText(editor.getResources().get("addArea"));
        JMenuItem menutem = null;
        for (MomProjectItem momProjectItem : MomProjectFactory.INSTANCE.getAllByType(AreaMaterial.class)) {
            final AreaMaterial mat = (AreaMaterial) momProjectItem;
            menutem = new JMenuItem(editor.getResources().get("add" + mat.getId()));
            menutem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    editor.showAddArea((AreaMaterial) mat.create());
                }
            });
            addAreaButton.add(menutem);
        }


        JButton removeAreaButton = new JButton(Resources.loadImageIcon("/images/net/vpc/swing/Delete.gif"));
        removeAreaButton.setToolTipText(editor.getResources().get("removeArea"));
        removeAreaButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                TreePath[] selectionPaths = groupsTree.getSelectionPaths();
                if (selectionPaths != null) {
                    for (int i = 0; i < selectionPaths.length; i++) {
                        Object areaZone = (((DefaultMutableTreeNode) selectionPaths[i].getLastPathComponent()).getUserObject());
                        if (areaZone instanceof Area) {
                            editor.removeArea((Area) areaZone);
                        }
                    }
                }
            }
        });
        JButton editAreaButton = new JButton(Resources.loadImageIcon("/images/net/vpc/swing/Editor.gif"));
        editAreaButton.setToolTipText(editor.getResources().get("editArea"));
        editAreaButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                TreePath[] selectionPaths = groupsTree.getSelectionPaths();
                if (selectionPaths != null) {
                    for (int i = 0; i < selectionPaths.length; i++) {
                        Object o = (((DefaultMutableTreeNode) selectionPaths[i].getLastPathComponent()).getUserObject());
                        if (o instanceof AreaZone) {
                            editor.showEdit(((AreaZone) o));
                        }
                        return;
                    }
                }
            }
        });
        jToolBar.add(addAreaButton);
        jToolBar.add(removeAreaButton);
        jToolBar.add(editAreaButton);
        jToolBar.setFloatable(false);
        add(jToolBar, BorderLayout.SOUTH);
    }

    public void setProject(MomProject structure) {
        if (this.structure != null) {
            this.structure.removeStructureListener(structureListener);
        }
        this.structure = structure;
        this.structure.addProjectListener(structureListener);
        root.removeAllChildren();
        root.setUserObject(structure.getRootAreaGroup());
        rebuild(root);
    }

    private void rebuild(DefaultMutableTreeNode n) {
        Object userObject = n.getUserObject();
        if (userObject instanceof AreaGroup) {
            AreaGroup ag = (AreaGroup) userObject;
            int areaZoneSize = ag.getAreaZoneSize();
            for (int i = 0; i < areaZoneSize; i++) {
                DefaultMutableTreeNode p = new DefaultMutableTreeNode(ag.getAreaZone(i));
                n.add(p);
                rebuild(p);
            }
        }
    }

    public AreaGroup getRootAreaGroup() {
        return structure == null ? null : structure.getRootAreaGroup();
    }

    private void createPopupMenu() {
        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menutem = null;
        for (MomProjectItem momProjectItem : MomProjectFactory.INSTANCE.getAllByType(AreaMaterial.class)) {
            final AreaMaterial mat = (AreaMaterial) momProjectItem;
            menutem = new JMenuItem(editor.getResources().get("add" + mat.getId()));
            menutem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    editor.showAddArea((AreaMaterial) mat.create());
                }
            });
            popupMenu.add(menutem);
        }



        menutem = new JMenuItem(
                editor.getResources().get("removeArea"),
                Resources.loadImageIcon("/images/net/vpc/swing/Delete.gif"));
        menutem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                TreePath[] selectionPaths = groupsTree.getSelectionPaths();
                for (int i = 0; i < selectionPaths.length; i++) {
                    if (((((DefaultMutableTreeNode) selectionPaths[i].getLastPathComponent()).getUserObject())) instanceof Area) {
                        editor.removeArea(((Area) (((DefaultMutableTreeNode) selectionPaths[i].getLastPathComponent()).getUserObject())));
                    } else {
                        AreaGroup g = ((AreaGroup) (((DefaultMutableTreeNode) selectionPaths[i].getLastPathComponent()).getUserObject()));
                        g.remove();
                    }
                }
            }
        });
        popupMenu.add(menutem);
        menutem = new JMenuItem(
                editor.getResources().get("editArea"),
                Resources.loadImageIcon("/images/net/vpc/swing/Editor.gif"));
        menutem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                TreePath[] selectionPaths = groupsTree.getSelectionPaths();
                for (int i = 0; i < selectionPaths.length; i++) {
                    if (((((DefaultMutableTreeNode) selectionPaths[i].getLastPathComponent()).getUserObject())) instanceof AreaZone) {
                        editor.showEdit(((AreaZone) (((DefaultMutableTreeNode) selectionPaths[i].getLastPathComponent()).getUserObject())));
                        return;
                    }
                }
            }
        });
        popupMenu.add(menutem);

        menutem = new JMenuItem(
                editor.getResources().get("enableArea"),
                Resources.loadImageIcon("/images/net/vpc/swing/Editor.gif"));
        menutem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                TreePath[] selectionPaths = groupsTree.getSelectionPaths();
                for (int i = 0; i < selectionPaths.length; i++) {
                    if (((((DefaultMutableTreeNode) selectionPaths[i].getLastPathComponent()).getUserObject())) instanceof Area) {
                        Area area = ((Area) (((DefaultMutableTreeNode) selectionPaths[i].getLastPathComponent()).getUserObject()));
                        area.setEnabled(true);
                    }
                }
            }
        });
        popupMenu.add(menutem);
        menutem = new JMenuItem(
                editor.getResources().get("disableArea"),
                Resources.loadImageIcon("/images/net/vpc/swing/Editor.gif"));
        menutem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                TreePath[] selectionPaths = groupsTree.getSelectionPaths();
                for (int i = 0; i < selectionPaths.length; i++) {
                    if (((((DefaultMutableTreeNode) selectionPaths[i].getLastPathComponent()).getUserObject())) instanceof Area) {
                        Area area = ((Area) (((DefaultMutableTreeNode) selectionPaths[i].getLastPathComponent()).getUserObject()));
                        area.setEnabled(false);
                    }
                }
            }
        });

        popupMenu.add(menutem);
        menutem = new JMenuItem(
                editor.getResources().get("selectArea"),
                Resources.loadImageIcon("/images/net/vpc/swing/Editor.gif"));
        menutem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                TreePath[] selectionPaths = groupsTree.getSelectionPaths();
                for (int i = 0; i < selectionPaths.length; i++) {
                    AreaZone area = ((AreaZone) (((DefaultMutableTreeNode) selectionPaths[i].getLastPathComponent()).getUserObject()));
                    editor.getPlotEditor().selectZone(area);
                }
            }
        });

        popupMenu.add(menutem);
        groupsTree.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
                    popupMenu.setInvoker(groupsTree);
                    if (groupsTree != null) {
                        popupMenu.show(groupsTree, e.getX(), e.getY());
                    }

                }
            }
        });
    //groupsTree.setComponentPopupMenu(popupMenu);
    }
}
