package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.props;

import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.props.components.CustomJXTreeTableImpl;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.props.components.AppPropertiesNodeFolderTreeTableNode;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.props.components.AppPropertiesNodeItemTreeTableNode;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.components.DefaultNonEditableMutableTreeTableNode;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.vpc.common.props.PropertyEvent;
import net.vpc.common.props.PropertyListener;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.function.Predicate;
import net.vpc.common.app.AppPopupMenu;
import net.vpc.common.app.AppTools;
import net.vpc.common.app.swing.core.EmptyPropertiesTree;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.AbstractToolWindowPanel;
import net.vpc.common.app.AppPropertiesNode;
import net.vpc.common.app.AppPropertiesNodeFolder;
import net.vpc.common.app.AppPropertiesNodeItem;
import net.vpc.common.app.AppPropertiesTree;
import net.vpc.common.app.swing.core.swing.SwingAppFactory;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.props.actions.MoMNewFunctionGroupListAction;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.props.actions.MoMNewFunctionGroupMeshAction;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.props.actions.MoMNewFunctionGroupSeqAction;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.props.actions.RemoveFromPropsAction;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.props.components.PropsTreeCellRenderer;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.util.AppCompUtils;

public class HWSProjectPropertiesTool extends AbstractToolWindowPanel {

    private JXTreeTable tree;
    private AppPopupMenu popUpMenu;

    public HWSProjectPropertiesTool(HadruwavesStudio studio) {
        super(studio);
        tree = new CustomJXTreeTableImpl(this);

//        tree.setTreeCellRenderer(new DefaultAppTreeCellRenderer(application){
//            @Override
//            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
//                Component e = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
//                setIcon(null);
//                return e;
//            }
//        });
        app().activeProperties().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                if (event.getProperty().name().equals("activeProperties")) {
                    updateRoot();
                }
            }
        });
        updateRoot();
        setContent(new JScrollPane(tree));
        SwingAppFactory f = new SwingAppFactory();
        popUpMenu = f.createPopupMenu(studio.app(), "/propertiesMenu");
        AppCompUtils.bind(popUpMenu, tree, this::preparePopupBeforeShowing);
        createPopUpMenu();
    }

    protected void preparePopupBeforeShowing() {
        refreshTools();
    }

    protected void onLookChanged() {
        tree.setTreeCellRenderer(new PropsTreeCellRenderer(this));
        AppCompUtils.updateUI(popUpMenu);
    }

    public AppTools popupTools() {
        return popUpMenu.tools();
    }

    private void createPopUpMenu() {
        popupTools().addAction(new MoMNewFunctionGroupListAction(studio()), "/moMNewFunctionGroupList");
        popupTools().addAction(new MoMNewFunctionGroupSeqAction(studio()), "/moMNewFunctionGroupSeq");
        popupTools().addAction(new MoMNewFunctionGroupMeshAction(studio()), "/moMNewFunctionGroupMesh");
        popupTools().addSeparator("/separator1");
        popupTools().addAction(new RemoveFromPropsAction(studio()), "/removeFromProps");
    }

    public void refreshTools() {
        app().tools().refresh();
    }

    public DefaultMutableTreeTableNode create(AppPropertiesNode o) {
        if (o instanceof AppPropertiesNodeFolder) {
            AppPropertiesNodeFolder t = (AppPropertiesNodeFolder) o;
            return new AppPropertiesNodeFolderTreeTableNode(t, this);
        } else if (o instanceof AppPropertiesNodeItem) {
            AppPropertiesNodeItem item = (AppPropertiesNodeItem) o;
            return new AppPropertiesNodeItemTreeTableNode(item, this);
        } else {
            return new DefaultNonEditableMutableTreeTableNode(o, false);
        }
    }

    public static Object toITem(Object o) {
        AppPropertiesNode t = toITemTT(o);
        if (t == null) {
            return null;
        }
        return t;
    }

    protected static AppPropertiesNode toITemTT(Object o) {
        if (o instanceof DefaultMutableTreeTableNode) {
            o = ((DefaultMutableTreeTableNode) o).getUserObject();
        }
        if (o instanceof AppPropertiesNode) {
            return (AppPropertiesNode) o;
        }
        if (o == null) {
            return null;
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public void updateRoot() {
        AppPropertiesTree p = studio().app().activeProperties().get();
        TreeTableNode root = null;
        if (p == null) {
            p = new EmptyPropertiesTree("None");
        }
        p.refresh();
        root = create(p.root());
        tree.setTreeTableModel(new DefaultTreeTableModel(root) {
            @Override
            public String getColumnName(int column) {
                switch (column) {
                    case 0:
                        return "Name";
                    case 1:
                        return "Value";
//                    case 2:
//                        return "Evaluated Expr.";
                }
                return super.getColumnName(column);
            }

            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return String.class;
                    case 1:
                        return Object.class;
//                    case 2:
//                        return Object.class;
                }
                return super.getColumnClass(column);
            }
        });
        tree.expandAll();
        tree.repaint();
        if (studio() != null && studio().get3DView() != null) {
            studio().get3DView().updateScene();
        }
    }

    public AppPropertiesNode[] getSelectedItems() {
        return getSelectedItems(null);
    }

    public AppPropertiesNode getSelectedItem() {
        return getSelectedItem(null);
    }

    public AppPropertiesNode getSelectedItem(Predicate<AppPropertiesNode> filter) {
        AppPropertiesNode[] f = getSelectedItems(filter);
        if (f.length == 1) {
            return f[0];
        }
        return null;
    }

    public AppPropertiesNode[] getSelectedItems(Predicate<AppPropertiesNode> filter) {
        java.util.List<AppPropertiesNode> list = new ArrayList<>();
        for (TreePath path : tree.getTreeSelectionModel().getSelectionPaths()) {
            Object c = path.getLastPathComponent();
            Object i = toITem(c);
            if (i instanceof AppPropertiesNode) {
                AppPropertiesNode v = (AppPropertiesNode) i;
                if (filter == null || filter.test(v)) {
                    list.add(v);
                }
            }
        }
        return (AppPropertiesNode[]) list.toArray(new AppPropertiesNode[0]);
    }

}
