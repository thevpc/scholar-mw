package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.params;

import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.params.components.*;
import net.vpc.common.props.PropertyEvent;
import net.vpc.common.props.PropertyListener;
import net.vpc.common.props.WritablePIndexedNode;
import net.vpc.common.app.AppPopupMenu;
import net.vpc.common.app.swing.core.swing.JPopupMenuComponentSupplier;
import net.vpc.common.app.swing.core.swing.SwingAppFactory;
import net.vpc.scholar.hadruwaves.project.HWProject;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwaves.project.parameter.HWParameterFolder;
import net.vpc.scholar.hadruwaves.project.parameter.HWParameterValue;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.params.actions.AddParameterAction;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.params.actions.RemoveParameterOrFolderAction;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.AbstractToolWindowPanel;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.util.HWPropertiesTree;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableNode;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import net.vpc.scholar.hadrumaths.units.UnitType;
import net.vpc.scholar.hadruwaves.project.parameter.HWParameterElement;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.params.actions.AddParameterFolderAction;
import net.vpc.common.app.AppPropertiesTree;

public class HWSProjectParametersTool extends AbstractToolWindowPanel {

    private CustomJXTreeTable tree;
    private AppPopupMenu popUpMenu;
    private HWConfigurationRun configuration;

    public HWSProjectParametersTool(HadruwavesStudio studio) {
        super(studio);
        tree = new CustomJXTreeTable(this);
        tree.setTreeTableModel(new CustomDefaultTreeTableModelImpl(new DefaultMutableTreeTableNode()));
        tree.expandAll();
        add(new JScrollPane(tree));
        studio.proc().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                updateRoot(null, null);
            }
        });
        app().activeProperties().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                if (event.getProperty().name().equals("activeProperties")) {
                    AppPropertiesTree t = event.getNewValue();
                    HWProject prj = null;
                    HWConfigurationRun conf = null;
                    if (t instanceof HWPropertiesTree) {
                        prj = ((HWPropertiesTree) t).getProject();
                    }
                    if (t != null) {
                        Object a = t.root().object();
                        if (a instanceof HWConfigurationRun) {
                            conf = (HWConfigurationRun) a;
                        }
                    }
                    updateRoot(prj, conf);
                }
            }
        });
        SwingAppFactory f = new SwingAppFactory();
        popUpMenu = f.createPopupMenu(studio.app(), "/parametersMenu");
        createPopUpMenu();
        tree.setComponentPopupMenu(((JPopupMenuComponentSupplier) popUpMenu).component());
        onLookChanged();
    }

    public HWConfigurationRun configuration() {
        return configuration;
    }

    public static Object toItem(Object o) {
        if (o instanceof DefaultMutableTreeTableNode) {
            DefaultMutableTreeTableNode u = (DefaultMutableTreeTableNode) o;
            return toItem(u.getUserObject());
        }
        if (o instanceof WritablePIndexedNode) {
            WritablePIndexedNode u = (WritablePIndexedNode) o;
            return toItem(u.get());
        }
        return o;
    }

    public JXTreeTable tree() {
        return tree;
    }

    public Object[] getSelectedElements() {
        TreePath[] selectionPaths = tree.getTreeSelectionModel().getSelectionPaths();
        java.util.List<Object> all = new ArrayList<Object>();
        for (TreePath p : selectionPaths) {
            Object lastPathComponent = toItem(p.getLastPathComponent());
            if (lastPathComponent instanceof HWParameterElement) {
                all.add(lastPathComponent);
            } else if (lastPathComponent instanceof HWConfigurationRun) {
                all.add(lastPathComponent);
            }
        }
        return all.toArray(new Object[0]);
    }

    @Override
    protected void onLookChanged() {
//        tree.setCellRenderer(new SolutiionAppTreeCellRendererImpl(studio, HWSSolutionExplorerTool.this));
        if (popUpMenu != null) {
            SwingUtilities.updateComponentTreeUI(((JPopupMenuComponentSupplier) popUpMenu).component());
        }
    }

    protected void updateRoot(HWProject prj, HWConfigurationRun conf) {
        if (prj != null && conf != null) {
            updateRoot(conf);
        } else if (prj != null) {
            conf = prj.configurations().activeConfiguration().get();
            updateRoot(conf);
        } else {
            prj = proc().activeProjectValue();
            if (prj != null) {
                conf = prj.configurations().activeConfiguration().get();
                updateRoot(conf);
            } else {
                updateRoot(null);
            }
        }
    }

    public void updateRoot() {
        if (configuration != null) {
            configuration.reset();
        }
        CustomDefaultTreeTableModelImpl model = (CustomDefaultTreeTableModelImpl) tree.getTreeTableModel();
        TreeTableNode root = create(configuration, configuration, tree);
        model.setRoot(root);
        tree.setRootVisible(true);
        tree.expandAll();
        tree.repaint();
    }

    protected void updateRoot(HWConfigurationRun configuration) {
        this.configuration = configuration;
        updateRoot();
    }

    public static DefaultMutableTreeTableNode create(Object o, HWConfigurationRun configuration, CustomJXTreeTable tree) {
        if (o instanceof HWConfigurationRun) {
            HWConfigurationRun t = (HWConfigurationRun) o;
            return new HWParametersTreeTableNodeImpl(configuration, tree);
        } else if (o instanceof HWParameterElement) {
            HWParameterElement pe = (HWParameterElement) o;
            if (pe instanceof HWParameterFolder) {
                return new HWParameterFolderTreeTableNode((HWParameterFolder) pe, configuration, tree);
            } else if (pe instanceof HWParameterValue) {
                return new HWParameterValueTreeTableNode((HWParameterValue) pe, configuration, tree);
            }
        }
        DefaultMutableTreeTableNode d = new HWParameterTreeTableNodeBase("Please Select Project First...", false,configuration,tree) {
            @Override
            public boolean isEditable(int column) {
                return false;
            }
            @Override
            public Object getValueAt(int column) {
                return "";
            }
        };
        return d;
    }

    private void createPopUpMenu() {

        for (UnitType value : UnitType.values()) {
            popUpMenu.tools().addAction(new AddParameterAction(this, value), "/Add/New " + value.group() + " Parameter/addParameter" + value);
        }
        popUpMenu.tools().addSeparator("/Add/sepatator1");
        popUpMenu.tools().addAction(new AddParameterFolderAction(this), "/Add/addParameterFolder");
        popUpMenu.tools().addSeparator("/sepatator1");
        popUpMenu.tools().addAction(new RemoveParameterOrFolderAction(this), "/removeParameter");
    }

}
