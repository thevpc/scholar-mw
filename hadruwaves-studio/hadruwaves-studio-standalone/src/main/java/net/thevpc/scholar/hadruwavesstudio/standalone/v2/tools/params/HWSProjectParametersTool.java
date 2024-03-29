package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.params;

import net.thevpc.common.props.Path;
import net.thevpc.echo.Button;
import net.thevpc.echo.api.components.AppComponent;
import net.thevpc.echo.api.AppContainerChildren;
import net.thevpc.echo.ContextMenu;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.params.components.*;
import net.thevpc.common.props.PropertyEvent;
import net.thevpc.common.props.PropertyListener;
import net.thevpc.common.props.WritableIndexedNode;
import net.thevpc.scholar.hadruwaves.project.HWProject;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwaves.project.parameter.HWParameterFolder;
import net.thevpc.scholar.hadruwaves.project.parameter.HWParameterValue;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.params.actions.AddParameterAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.params.actions.RemoveParameterOrFolderAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.AbstractToolWindowPanel;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.HWPropertiesTree;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableNode;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import net.thevpc.scholar.hadrumaths.units.UnitType;
import net.thevpc.scholar.hadruwaves.project.parameter.HWParameterElement;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.params.actions.AddParameterFolderAction;
import net.thevpc.echo.api.AppPropertiesTree;
import net.thevpc.echo.swing.peers.SwingPeer;

public class HWSProjectParametersTool extends AbstractToolWindowPanel {

    private CustomJXTreeTable tree;
    private ContextMenu popUpMenu;
    private HWConfigurationRun configuration;

    public HWSProjectParametersTool(HadruwavesStudio studio) {
        super(studio);
        tree = new CustomJXTreeTable(this);
        tree.setTreeTableModel(new CustomDefaultTreeTableModelImpl(new DefaultMutableTreeTableNode()));
        tree.expandAll();
        add(new JScrollPane(tree));
        studio.proc().onChange(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                updateRoot(null, null);
            }
        });
        app().activeProperties().onChange(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                if (event.property().propertyName().equals("activeProperties")) {
                    AppPropertiesTree t = event.newValue();
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
        popUpMenu = new ContextMenu(app());
        createPopUpMenu();
        tree.setComponentPopupMenu(
                (JPopupMenu) SwingPeer.gcompOf(popUpMenu)
        );
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
        if (o instanceof WritableIndexedNode) {
            WritableIndexedNode u = (WritableIndexedNode) o;
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
            SwingUtilities.updateComponentTreeUI(
                (JPopupMenu) SwingPeer.gcompOf(popUpMenu)
            );
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

        AppContainerChildren<AppComponent> tools = popUpMenu.children();
        for (UnitType value : UnitType.values()) {
            tools.add(new Button("AddParameterAction",
                            new AddParameterAction(this, value),app()
                            ),Path.of("/Add/New " + value.group() + " Parameter/addParameter" + value));
            tools.add(
                    new Button("AddParameterAction",new AddParameterAction(this, value),app())
            ,Path.of("/Add/New " + value.group() + " Parameter/addParameter" + value));
        }
        tools.addSeparator(Path.of("/Add/sepatator1"));
        tools.add(new Button("AddParameterFolderAction",new AddParameterFolderAction(this),app()), Path.of("/Add/addParameterFolder"));
        tools.addSeparator(Path.of("/sepatator1"));
        tools.add(new Button("RemoveParameterOrFolderAction",new RemoveParameterOrFolderAction(this),app()), Path.of("/removeParameter"));
    }

}
