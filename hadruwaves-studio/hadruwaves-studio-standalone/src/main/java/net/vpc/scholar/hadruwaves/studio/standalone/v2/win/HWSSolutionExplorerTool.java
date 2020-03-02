package net.vpc.scholar.hadruwaves.studio.standalone.v2.win;

import net.vpc.common.prpbind.PValue;
import net.vpc.common.prpbind.WritablePIndexedNode;
import net.vpc.common.prpbind.WritablePNamedNode;
import net.vpc.common.prpbind.impl.PropsHelper;
import net.vpc.common.strings.StringUtils;
import net.vpc.common.swings.app.impl.swing.LazyTree;
import net.vpc.common.swings.app.impl.swing.LazyTreeBackend;
import net.vpc.common.swings.app.impl.swing.LazyTreeNode;
import net.vpc.scholar.hadruwaves.Material;
import net.vpc.scholar.hadruwaves.project.HWProject;
import net.vpc.scholar.hadruwaves.project.HWSolution;
import net.vpc.scholar.hadruwaves.project.HWSolutionElement;
import net.vpc.scholar.hadruwaves.project.HWSolutionFolder;
import net.vpc.scholar.hadruwaves.project.scene.*;
import net.vpc.scholar.hadruwaves.project.scene.elem.Element3DPolygonTemplate;
import net.vpc.scholar.hadruwaves.studio.standalone.v2.HWUtils;
import net.vpc.scholar.hadruwaves.studio.standalone.v2.HadruwavesStudioV2;
import org.jdesktop.swingx.JXTree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class HWSSolutionExplorerTool extends JPanel {
    AbstractAction selectProjectAction = null;
    AbstractAction showProperties = null;
    AbstractAction enableAction = null;
    AbstractAction disableAction = null;
    AbstractAction newFolderAction = null;
    AbstractAction renameAction = null;
    AbstractAction removeAction = null;

    private HadruwavesStudioV2 application;
    private JXTree tree;
    private JPopupMenu popUpMenu;

    public HWSSolutionExplorerTool(HadruwavesStudioV2 application) {
        super(new BorderLayout());
        this.application = application;
        tree = (JXTree) LazyTree.of(new JXTree(), new SolutionLazyTreeBackend(application));


//        tree.setModel(new DefaultTreeModel(new SolutionLoadableMutableTreeNode(application.proc().solution())));
        tree.setCellRenderer(new DefaultAppTreeCellRenderer(application) {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                Component u = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                Object o = LazyTree.resolveLazyTreeNodeValue(value);
                if (o instanceof PValue) {
                    o = ((PValue) o).get();
                }
                if (o instanceof HWSolution) {
                    super.setIcon(HWUtils.iconFor("solution"));
                    setText(((HWSolution) o).name().get());
                }
                if (o instanceof Material) {
                    setText(((Material) o).getName());
                }
                if (o instanceof HWMaterialTemplate) {
                    setText(((HWMaterialTemplate) o).name().get());
                }
                if (o instanceof HWMaterialTemplate || o instanceof Material) {
                    super.setIcon(HWUtils.iconFor("material"));
                } else if (o instanceof HWProject) {
                    if (application.activeProject() == o) {
                        super.setForeground(getActivatedColor(sel));
                    }
                    super.setIcon(HWUtils.iconFor("project"));
                } else if (o instanceof StructureNodePort) {
                    super.setIcon(HWUtils.iconFor("port"));
                } else if (o instanceof MaterialNode) {
                    Element3DTemplate e = ((MaterialNode) o).geometry().get();
                    if (e instanceof Element3DPolygonTemplate) {
                        super.setIcon(HWUtils.iconFor("elem2d"));
                    } else {
                        super.setIcon(HWUtils.iconFor("elem3d"));
                    }
                }
                if (o instanceof StructureNode) {
                    if (!((StructureNode) o).enabled().get()) {
                        super.setForeground(getDisabledColor(sel));
                    }
                }
                if (o instanceof HWSolutionElement) {
                    HWSolutionElement se = (HWSolutionElement) o;
                    setText(se.name().get());
                }
                return u;
            }
        });
        MouseListener ml = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int selRow = tree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                if (selRow != -1) {
                    if (e.getClickCount() == 2) {
                        DefaultMutableTreeNode n = (DefaultMutableTreeNode) selPath.getLastPathComponent();
                        Object a = n.getUserObject();
                        if (a instanceof WritablePIndexedNode) {
                            HWSolutionElement se = (HWSolutionElement) ((WritablePIndexedNode) a).get();
                            if (se instanceof HWProject) {
                                application.proc().solution().get().activeProject().set((HWProject) se);
                            }
                        }
                    }
                }
                if (e.isPopupTrigger()) {
                    if (selPath != null) {
                        tree.getSelectionModel().setSelectionPath(selPath);
                    } else {
                        tree.getSelectionModel().setSelectionPath(null);
                    }
                    preparePopupBeforeShowing();
                    try {
                        popUpMenu.show(tree, e.getX(), e.getY());
                    }catch (Exception ex){
                        System.err.println(ex);
                    }
                }

            }
        };
        tree.addMouseListener(ml);
        popUpMenu = createPopUpMenu();
//        tree.setComponentPopupMenu(popUpMenu);

        JPanel pane = new JPanel(new BorderLayout());
//        pane.add(toolbar,BorderLayout.PAGE_START);
        tree.setBorder(null);
        tree.expandAll();
        JScrollPane jsp = new JScrollPane(tree);
        jsp.setBorder(null);
        pane.add(jsp, BorderLayout.CENTER);
        add(pane);
    }

    protected void setVisible(AbstractAction a, boolean visible) {
        a.putValue("visible", visible);
    }

    protected void preparePopupBeforeShowing() {
        HWProject sp = getSelectedProject();
        selectProjectAction.setEnabled(sp != null);
        setVisible(selectProjectAction, sp != null);

        enableAction.setEnabled(getEnablableItems().length > 0);
        setVisible(enableAction,enableAction.isEnabled());

        disableAction.setEnabled(getDisablableItems().length > 0);
        setVisible(disableAction,disableAction.isEnabled());

        removeAction.setEnabled(getRemovableItems().length > 0);
        setVisible(removeAction,removeAction.isEnabled());

        showProperties.setEnabled(getShowPropsItem()!=null);
        setVisible(showProperties,showProperties.isEnabled());

        renameAction.setEnabled(getRenamableItem()!=null);
        setVisible(renameAction,renameAction.isEnabled());

        newFolderAction.setEnabled(getNewFolderItems()!=null);
        setVisible(newFolderAction,newFolderAction.isEnabled());

    }

    private Object[] getSelectedItems() {
        java.util.List<Object> all = new ArrayList<>();
        TreePath[] paths = tree.getSelectionPaths();
        for (TreePath path : paths) {
            Object a = LazyTree.resolveLazyTreeNodeValue(path.getLastPathComponent());
            if (a instanceof PValue) {
                a = ((PValue) a).get();
            }
            if (a instanceof HWProject) {
                all.add(a);
            } else if (a instanceof HWSolution) {
                all.add(a);
            } else if (a instanceof HWSolutionElement) {
                all.add(a);
            } else if (a instanceof ProjectFolderNode) {
                all.add(a);
            } else if (a instanceof HWMaterialTemplate) {
                all.add(a);
            } else if (a instanceof Material) {
                all.add(a);
            } else if (a instanceof StructureNode) {
                all.add(a);
            }
        }
        return all.toArray(new Object[0]);
    }

    private HWProject getSelectedProject() {
        Object[] selectedItems = getSelectedItems();
        if (selectedItems.length == 1) {
            Object wn = selectedItems[0];
            if (wn instanceof HWProject) {
                return (HWProject) wn;
            }
        }
        return null;
    }

    public Object[] getEnablableItems() {
        java.util.List<StructureNode> all = new ArrayList();
        for (Object selectedItem : getSelectedItems()) {
            if (selectedItem instanceof StructureNode) {
                StructureNode o = (StructureNode) selectedItem;
                if (!o.enabled().get()) {
                    all.add(o);
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return all.toArray(new Object[0]);
    }

    public Object[] getDisablableItems() {
        java.util.List<StructureNode> all = new ArrayList();
        for (Object selectedItem : getSelectedItems()) {
            if (selectedItem instanceof StructureNode) {
                StructureNode o = (StructureNode) selectedItem;
                if (o.enabled().get()) {
                    all.add(o);
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return all.toArray(new Object[0]);
    }

    public Object[] getRemovableItems() {
        java.util.List<Object> all = new ArrayList();
        for (Object selectedItem : getSelectedItems()) {
            if (
                    (selectedItem instanceof StructureNode)
                    ||(selectedItem instanceof HWProject)
                    ||(selectedItem instanceof HWMaterialTemplate)
                    ||(selectedItem instanceof Material)
            ) {
                all.add(selectedItem);
            } else {
                break;
            }
        }
        return all.toArray(new Object[0]);
    }

    public Object getShowPropsItem() {
        java.util.List<Object> all = new ArrayList();
        for (Object selectedItem : getSelectedItems()) {
            if (
                    (selectedItem instanceof StructureNode)
                    ||(selectedItem instanceof HWProject)
                    ||(selectedItem instanceof HWMaterialTemplate)
                    ||(selectedItem instanceof Material)
            ) {
                all.add(selectedItem);
            } else {
                break;
            }
        }
        if(all.size()==1){
            return all.get(0);
        }
        return null;
    }

    public Object getRenamableItem() {
        java.util.List<Object> all = new ArrayList();
        for (Object selectedItem : getSelectedItems()) {
            if (
                    (selectedItem instanceof StructureNode)
                    ||(selectedItem instanceof HWSolutionElement)
                    ||(selectedItem instanceof HWSolution)
                    ||(selectedItem instanceof HWMaterialTemplate)
                    ||(selectedItem instanceof Material)
            ) {
                all.add(selectedItem);
            } else {
                break;
            }
        }
        if(all.size()==1){
            return all.get(0);
        }
        return null;
    }

    public Object getNewFolderItems() {
        java.util.List<Object> all = new ArrayList();
        for (Object selectedItem : getSelectedItems()) {
            if (
                    (selectedItem instanceof HWSolution)
                    || (selectedItem instanceof HWSolutionFolder)
            ) {
                all.add(selectedItem);
            } else {
                break;
            }
        }
        if(all.size()==1){
            return all.get(0);
        }
        return null;
    }

    private JPopupMenu createPopUpMenu() {
        selectProjectAction = HWUtils.action("selectProject", "Select Project", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HWProject sp = getSelectedProject();
                if (sp != null) {
                    application.proc().solution().get().activeProject().set(sp);
                    tree.repaint();
                }
            }
        });

        enableAction = HWUtils.action("enable", "Enable", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Object enablableItem : getEnablableItems()) {
                    if (enablableItem instanceof StructureNode) {
                        ((StructureNode) enablableItem).enabled().set(true);
                    }
                }
                tree.repaint();
            }
        });

        disableAction = HWUtils.action("disable", "Disable", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Object enablableItem : getDisablableItems()) {
                    if (enablableItem instanceof StructureNode) {
                        ((StructureNode) enablableItem).enabled().set(false);
                    }
                }
                tree.repaint();
            }
        });

        removeAction = HWUtils.action("remove", "Remove", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] removableItems = getRemovableItems();
                for (Object item : removableItems) {
                    //
                }
                tree.repaint();
            }
        });

        showProperties = HWUtils.action("properties", "Properties", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object removableItems = getShowPropsItem();
                tree.repaint();
            }
        });

        newFolderAction = HWUtils.action("NewFolder", "New Folder", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object removableItems = getNewFolderItems();
                tree.repaint();
            }
        });

        renameAction = HWUtils.action("RenameAction", "Rename", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object removableItems = getRenamableItem();
                tree.repaint();
            }
        });


        JPopupMenu menu = new JPopupMenu();

        JMenuItem m = new JMenuItem(selectProjectAction);
        selectProjectAction.addPropertyChangeListener(new VisibleHandlerListener(m));
        menu.add(m);

        m = new JMenuItem(newFolderAction);
        newFolderAction.addPropertyChangeListener(new VisibleHandlerListener(m));
        menu.add(m);

        m = new JMenuItem(renameAction);
        renameAction.addPropertyChangeListener(new VisibleHandlerListener(m));
        menu.add(m);

        m = new JMenuItem(enableAction);
        enableAction.addPropertyChangeListener(new VisibleHandlerListener(m));
        menu.add(m);

        m = new JMenuItem(disableAction);
        disableAction.addPropertyChangeListener(new VisibleHandlerListener(m));
        menu.add(m);

        m = new JMenuItem(removeAction);
        removeAction.addPropertyChangeListener(new VisibleHandlerListener(m));
        menu.add(m);

        m = new JMenuItem(showProperties);
        showProperties.addPropertyChangeListener(new VisibleHandlerListener(m));
        menu.add(m);

        return menu;
    }

    private static class ProjectFolderNode {
        private String path;
        private HWProject project;

        public ProjectFolderNode(String path, HWProject project) {
            this.path = path;
            this.project = project;
        }
    }

    private static class SolutionLazyTreeBackend implements LazyTreeBackend {
        HadruwavesStudioV2 application;

        public SolutionLazyTreeBackend(HadruwavesStudioV2 application) {
            this.application = application;
        }

        @Override
        public LazyTreeNode getRoot() {
            HWSolution solution = application.proc().solution().get();
            if (solution != null) {
                return new LazyTreeNode(solution.name().get(), solution, "/", true);
            }
            return new LazyTreeNode("No Solution is Open", "/", "/", true);
        }

        @Override
        public LazyTreeNode[] getChildren(LazyTreeNode parent) {
            Object o = parent.getValue();
            String parentPath = parent.getPath();
            if (o instanceof ProjectFolderNode) {
                ProjectFolderNode pfn = (ProjectFolderNode) o;
                switch (pfn.path) {
                    case "/Materials": {
                        HWProject p = pfn.project;
                        LinkedHashMap<String, LazyTreeNode> all = new LinkedHashMap<>();
                        all.put("Vacuum", new LazyTreeNode("Vacuum", Material.VACUUM, parentPath + "/Vacuum", false));
                        all.put("PEC", new LazyTreeNode("PEC", Material.PEC, parentPath + "/PEC", false));
                        all.put("PMC", new LazyTreeNode("PMC", Material.PMC, parentPath + "/PMC", false));
                        if (p != null) {
                            for (HWMaterialTemplate value : p.materials().values()) {
                                if (all.containsKey(value.name().get())) {
                                    all.put(
                                            value.name().get(),
                                            new LazyTreeNode(value.name().get(), value, parentPath + "/" + value.name().get(), false)
                                    );
                                }
                            }
                        }
                        return all.values().toArray(new LazyTreeNode[0]);
                    }
                    case "/Sources": {
                        HWProject p = pfn.project;
                        if (p != null) {
                            HWScene s = p.scene().get();
                            if (s != null) {
                                return s.findDeepComponents(x -> x instanceof StructureNodePort)
                                        .stream()
                                        .map(x -> new LazyTreeNode(x.getClass().getSimpleName(), x, parentPath + "/" + x.getClass().getSimpleName(), x instanceof StructureNodeGroup))
                                        .toArray(LazyTreeNode[]::new);
                            }
                        }
                        return new LazyTreeNode[0];
                    }
                    case "/Components": {
                        HWProject p = pfn.project;
                        if (p != null) {
                            HWScene s = p.scene().get();
                            if (s != null) {
                                return s.components().findAll(x -> !(x instanceof StructureNodePort))
                                        .stream()
                                        .map(x -> new LazyTreeNode(StringUtils.isBlank(x.name().get()) ? x.getClass().getSimpleName() : x.name().get(),
                                                x, parentPath + "/" + x.getClass().getSimpleName(),
                                                x instanceof StructureNodeGroup)
                                        )
                                        .toArray(LazyTreeNode[]::new);
                            }
                        }
                        return new LazyTreeNode[0];
                    }
                }
            }
            if (o instanceof StructureNodeGroup) {
                return ((StructureNodeGroup) o).children()
                        .stream()
                        .map(x -> new LazyTreeNode(x.getClass().getSimpleName(), x, parentPath + "/" + x.getClass().getSimpleName(), x instanceof StructureNodeGroup))
                        .toArray(LazyTreeNode[]::new);
            }
            if (o instanceof HWSolution) {
                //WritablePIndexedNode<HWSolutionElement> child :
                return ((HWSolution) o).children()
                        .values()
                        .stream()
                        .map(x -> {
                            HWSolutionElement e = x.get();
                            return new LazyTreeNode(e.name().get(), x, PropsHelper.buildPath(parentPath, e.name().get()), true);
                        })
                        .toArray(LazyTreeNode[]::new);
            } else if (o instanceof WritablePIndexedNode) {
                WritablePIndexedNode<Object> node = (WritablePIndexedNode) o;
                Object v = node.get();
                if (v instanceof HWProject) {
                    HWProject hwp = (HWProject) v;
                    return new LazyTreeNode[]{
                            new LazyTreeNode("Components", new ProjectFolderNode("/Components", hwp), parentPath + "/Components", true),
                            new LazyTreeNode("Sources", new ProjectFolderNode("/Sources", hwp), parentPath + "/Sources", true),
                            new LazyTreeNode("Materials", new ProjectFolderNode("/Materials", hwp), parentPath + "/Materials", true),
                    };
                }


                return node.children()
                        .stream()
                        .map(x -> new LazyTreeNode(x.getClass().getSimpleName(), x, parentPath + "/" + x.getClass().getSimpleName(), true))
                        .toArray(LazyTreeNode[]::new);
            } else if (o instanceof WritablePNamedNode) {
                WritablePNamedNode<Object> node = (WritablePNamedNode) o;
                Object v = node.get();
                if (v instanceof HWProject) {
                    HWProject hwp = (HWProject) v;
                    return new LazyTreeNode[]{
                            new LazyTreeNode("Components", new ProjectFolderNode("/Components", hwp), parentPath + "/Components", true),
                            new LazyTreeNode("Sources", new ProjectFolderNode("/Sources", hwp), parentPath + "/Sources", true),
                            new LazyTreeNode("Materials", new ProjectFolderNode("/Materials", hwp), parentPath + "/Materials", true),
                    };
                }


                return node.children()
                        .stream()
                        .map(x -> {
                            return new LazyTreeNode(x.getKey(), x.getValue(), parentPath + "/" + x.getClass().getSimpleName(), true);
                        })
                        .toArray(LazyTreeNode[]::new);
            }
            return new LazyTreeNode[0];
        }
    }

    private static class VisibleHandlerListener implements PropertyChangeListener {
        private final JMenuItem m;

        public VisibleHandlerListener(JMenuItem m) {
            this.m = m;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("visible")) {
                Object v = evt.getNewValue();
                if (v != null) {
                    m.setVisible((Boolean) v);
                }
            }
        }
    }

//    private static class SolutionLoadableMutableTreeNode extends LoadableMutableTreeNode {
//        public SolutionLoadableMutableTreeNode(Object o) {
//            setUserObject(o);
//            prepareChildren0();
//        }
//
//        @Override
//        public boolean isLeaf() {
//            if (userObject instanceof PValue) {
//                Object o = ((PValue) userObject).get();
//                if (o instanceof HWSolution) {
//                    return false;
//                }
//            } else if (userObject instanceof HWSolution) {
//                return false;
//            } else if (userObject instanceof WritablePIndexedNode) {
//                HWSolutionElement o = ((WritablePIndexedNode<HWSolutionElement>) userObject).get();
//                if (o instanceof HWSolutionFolder) {
//                    return false;
//                }
//                if (o instanceof HWProject) {
//                    return false;
//                }
//            }
//            return true;
//        }
//
//        @Override
//        protected void prepareChildren() {
//            Object userObject = getUserObject();
//            if (userObject instanceof PValue) {
//                Object o = ((PValue) userObject).get();
//                if (children == null) {
//                    children = new Vector();
//                }
//                for (WritablePIndexedNode<HWSolutionElement> child : ((HWSolution) o).children()) {
//                    children.add(new SolutionLoadableMutableTreeNode(child));
//                }
//            } else if (userObject instanceof HWSolution) {
//                if (children == null) {
//                    children = new Vector();
//                }
//                for (WritablePIndexedNode<HWSolutionElement> child : ((HWSolution) userObject).children()) {
//                    children.add(new SolutionLoadableMutableTreeNode(child));
//                }
//            } else if (userObject instanceof WritablePIndexedNode) {
//                if (children == null) {
//                    children = new Vector();
//                }
//                for (WritablePIndexedNode<HWSolutionElement> child : ((WritablePIndexedNode<HWSolutionElement>) userObject).children()) {
//                    children.add(new SolutionLoadableMutableTreeNode(child));
//                }
//            }
//        }
//    }

}
