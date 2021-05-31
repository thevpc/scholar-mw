package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer;

import net.thevpc.echo.AppTools;
import net.thevpc.echo.api.components.AppComponent;
import net.thevpc.echo.api.AppContainerChildren;
import net.thevpc.echo.ContextMenu;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions.*;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.components.*;
import net.thevpc.common.props.*;
import net.thevpc.echo.swing.helpers.tree.LazyTree;
import net.thevpc.echo.swing.helpers.tree.LazyTreeNode;
import net.thevpc.scholar.hadruwaves.project.*;
import net.thevpc.scholar.hadruwaves.project.scene.*;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import org.jdesktop.swingx.JXTree;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.function.Predicate;
import net.thevpc.common.props.impl.PropsHelper;
import net.thevpc.echo.swing.peers.SwingPeer;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationFolder;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.AbstractToolWindowPanel;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationElement;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.AppCompUtils;

public class HWSSolutionExplorerTool extends AbstractToolWindowPanel {

    public JXTree tree;
    private ContextMenu popUpMenu;

    public HWSSolutionExplorerTool(HadruwavesStudio studio) {
        super(studio);
        tree = (JXTree) LazyTree.of(new JXTree(), new SolutionLazyTreeBackend(studio));

//        tree.setModel(new DefaultTreeModel(new SolutionLoadableMutableTreeNode(application.proc().solution())));
        tree.setCellRenderer(new SolutionAppTreeCellRendererImpl(studio, this));
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                treeSelectionChanged(e);
            }

        });
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                    if (selPath != null) {
                        HWProjectItem i = resolveValidItem(selPath);
                        if (i != null && i.getItem() instanceof HWProjectSourceFile) {
                            studio().openSourceFile((HWProjectSourceFile) i.getItem());
                        }
                    }
                }
            }
        });
        popUpMenu = new ContextMenu(app());
        AppCompUtils.bind(popUpMenu, tree, this::preparePopupBeforeShowing);

        createPopUpMenu();
//        tree.setComponentPopupMenu(popUpMenu);

        JPanel pane = new JPanel(new BorderLayout());
//        pane.add(toolbar,BorderLayout.PAGE_START);
        tree.setBorder(null);
        tree.expandAll();
        JScrollPane jsp = new JScrollPane(tree);
        jsp.setBorder(null);
        pane.add(jsp, BorderLayout.CENTER);
        setContent(pane);
        studio.proc().onChange(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                tree.repaint();
            }
        });
    }

    public void refreshTools() {
//        popUpMenu.model().refresh();
//        app().model().refresh();
    }

    protected void onLookChanged() {
        tree.setCellRenderer(new SolutionAppTreeCellRendererImpl(studio(), HWSSolutionExplorerTool.this));
        if (popUpMenu != null) {
            SwingUtilities.updateComponentTreeUI(
                    SwingPeer.gcompOf(popUpMenu)
            );
        }
    }

//    protected void setVisible(AbstractAction a, boolean visible) {
//        a.putValue("visible", visible);
//    }
    protected void preparePopupBeforeShowing() {
    }

    public Object[] getSelectedItemValues() {
        java.util.List<Object> all = new ArrayList<>();
        for (HWProjectItem item : getSelectedItems()) {
            all.add(item.getItemValue());
        }
        return all.toArray(new Object[0]);
    }

    public HWProjectItem getSelectedItemsOne(Predicate<HWProjectItem> filter) {
        HWProjectItem[] a = getSelectedItems(filter);
        if (a.length == 1) {
            return a[0];
        }
        return null;
    }

    public Object getSelectedItemsOneValue() {
        return getSelectedItemsOneValue(null);
    }

    public Object getSelectedItemsOneValue(Predicate<HWProjectItem> filter) {
        HWProjectItem[] a = getSelectedItems(filter);
        if (a.length == 1) {
            return a[0].getItemValue();
        }
        return null;
    }

    public HWProjectItem[] getSelectedItems() {
        return getSelectedItems(null);
    }

    public HWProjectItem[] getSelectedItems(Predicate<HWProjectItem> filter) {
        java.util.List<HWProjectItem> all = new ArrayList<>();
        TreePath[] paths = tree.getSelectionPaths();
        for (TreePath path : paths) {
            HWProjectItem a = resolveValidItem(path);
            if (a != null) {
                if (filter == null || filter.test(a)) {
                    all.add(a);
                }
            }
        }
        return all.toArray(new HWProjectItem[0]);
    }

    public HWProjectItem resolveValidItem(TreePath path) {
        HWProjectItem item = (HWProjectItem) LazyTree.resolveLazyTreeNodeValue(path.getLastPathComponent());
        if (item == null) {
            return null;
        }
        item.treePath = path;
        Object a = item.getItemValue();
        if (a != null) {
            return item;
        }
        return item;
    }

    private void createPopUpMenu() {
        AppContainerChildren<AppComponent> tools = popUpMenu.children();
        tools.addAction().bindUndo(new OpenSourceAction(this)).path("/open").tool();
        tools.addAction().bindUndo(new ReloadAction(this)).path("/reload").tool();
        tools.addAction().bindUndo(new SelectProjectAction(this)).path("/selectProject").tool();
        tools.addAction().bindUndo(new DeSelectProjectAction(this)).path("/deselectProject").tool();
        tools.addAction().bindUndo(new SelectConfigAction(this)).path("/selectConfig").tool();
        tools.addAction().bindUndo(new SaveNodeAction(this)).path("/save").tool();
        tools.addAction().bindUndo(new SaveAllNodeAction(this)).path("/saveAll").tool();
        tools.addAction().bindUndo(new SaveAsNodeAction(this)).path("/saveAs").tool();
        tools.addAction().bindUndo(new BuildNodeAction(this)).path("/build").tool();
        tools.addAction().bindUndo(new CopyNodeAction(this)).path("/copy").tool();
        tools.addAction().bindUndo(new CutNodeAction(this)).path("/cut").tool();
        tools.addAction().bindUndo(new PasteNodeAction(this)).path("/paste").tool();

        tools.addAction().bindUndo(new NewFolderUndoableAction(this)).path("/newFolder").tool();
        tools.addAction().bindUndo(new NewProjectUndoableAction(this)).path("/newProject").tool();
        tools.addAction().bindUndo(new NewMaterialAction(this)).path("/newMaterial").tool();
        tools.addAction().bindUndo(new NewConfigurationUndoableAction(this)).path("/newConfiguration").tool();
        tools.addAction().bindUndo(new NewMaterial2DPolygonAction(this)).path("/newMaterial2DPolygon").tool();
        tools.addAction().bindUndo(new NewMaterial2DRectangleAction(this)).path("/newMaterial2DRectangle").tool();
        tools.addAction().bindUndo(new NewMaterial3DBoxAction(this)).path("/newMaterial3DBox").tool();
        tools.addAction().bindUndo(new NewModalPortRectangleAction(this)).path("/newModalPortRectangle").tool();
        tools.addAction().bindUndo(new NewPlanarPortPolygonAction(this)).path("/newPlanarPortPolygon").tool();
        tools.addAction().bindUndo(new NewPlanarPortRectangleAction(this)).path("/newPlanarPortRectangle").tool();

        tools.addAction().bindUndo(new EnableUndoableActionImpl(this)).path("/enable").tool();
        tools.addAction().bindUndo(new DisableUndoableAction(this)).path("/disable").tool();
        tools.addAction().bind(new PropertiesSimpleActionImpl(studio().app(), this)).path("/properties").tool();
        tools.addSeparator("/separator1");
        tools.addAction().bindUndo(new RemoveUndoableAction(this)).path("/remove").tool();

    }

    public static LazyTreeNode createLazyTreeNode(Object o, HWSolution solution, HWProject project, HadruwavesStudio application, String parentPath) {
        boolean folder = false;
        String name = null;
        Object ov = o;
        if (o instanceof ObservableValue) {
            ov = ((ObservableValue) o).get();
        }
        if (ov instanceof HWProject) {
            project = (HWProject) ov;
        }
        if (ov instanceof HWSolutionElement) {
            name = ((HWSolutionElement) ov).name().get();
            if (ov instanceof DefaultHWSolutionFolder) {
                folder = true;
            }
            if (ov instanceof HWProject) {
                folder = true;
            }
        }
        if (ov instanceof HWConfigurationElement) {
            name = ((HWConfigurationElement) ov).name().get();
            if (ov instanceof HWConfigurationFolder) {
                folder = true;
            }
        }
        if (ov instanceof HWProjectElement) {
            name = ((HWProjectElement) ov).name().get();
            //TODO : Option
            boolean expandHWProjectElementBrick = false;
            if (ov instanceof HWProjectComponentGroup
                    || (expandHWProjectElementBrick && ov instanceof HWProjectBrick)) {
                folder = true;
            }
        }
        if (ov instanceof HWProjectSource) {
            HWProjectSource h = (HWProjectSource) ov;
            File file = h.file();
            name = file.getName();
            folder = file.isDirectory();
            if (project == null) {
                project = h.project;
            }
            if (solution == null) {
                solution = h.project.solution().get();
            }
        }
        if (name == null) {
            name = ov == null ? "null" : ov.getClass().getSimpleName();
        }
        return new LazyTreeNode(name, new HWProjectItem(application, solution, project, o),
                PropsHelper.buildPath(parentPath, name), folder);
    }

    public void reload() {
        LazyTree.reload(tree);
        tree.expandAll();
    }

    public void reload(LazyTreeNode node) {
        LazyTree.reload(tree, node);
    }

    public void reload(HWProjectItem path) {
        if (path != null) {
            LazyTree.reload(tree, path.getTreePath());
        }
    }

    public void reload(TreePath path) {
        LazyTree.reload(tree, path);
    }

    private void treeSelectionChanged(TreeSelectionEvent e) {
        boolean badSelection = false;
        HWProject currProject = null;
        HWConfigurationRun currConf = null;
        for (TreePath path : e.getPaths()) {
            boolean newSel = e.isAddedPath(path);
            HWProjectItem item = resolveValidItem(path);
            Object itemValue = item == null ? null : item.getItemValue();
            if (newSel) {
                if (itemValue instanceof HWProject) {
                    if (!badSelection) {
                        if (currProject == null) {
                            currProject = (HWProject) itemValue;
                        } else if (itemValue != currProject) {
                            currProject = null;
                            badSelection = true;
                        }
                    }
                } else if (itemValue instanceof HWConfigurationRun) {
                    if (!badSelection) {
                        if (currConf == null) {
                            currConf = (HWConfigurationRun) itemValue;
                            HWProject p = currConf.project().get();
                            if (currProject == null) {
                                currProject = (HWProject) p;
                            } else if (p != currProject) {
                                currProject = null;
                                badSelection = true;
                            }
                        } else if (itemValue != currConf) {
                            currConf = null;
                            badSelection = true;
                        }
                    }
                } else if (itemValue instanceof HWProjectElement) {
                    if (!badSelection) {
                        HWProjectElement pe = (HWProjectElement) itemValue;
                        HWProject p = pe.project().get();
                        if (currProject == null) {
                            currProject = (HWProject) p;
                        } else if (p != currProject) {
                            currProject = null;
                            badSelection = true;
                        }
                    }
                } else if (itemValue instanceof HWProjectFolder) {
                    if (!badSelection) {
                        HWProjectFolder pe = (HWProjectFolder) itemValue;
                        HWProject p = pe.project();
                        if (currProject == null) {
                            currProject = (HWProject) p;
                        } else if (p != currProject) {
                            currProject = null;
                            badSelection = true;
                        }
                    }
                }
            }
            if (itemValue instanceof WritableSelectable) {
                ((WritableSelectable) itemValue).selected().set(newSel);
            }
            if (currConf != null & currProject != null && currConf.project().get() != currProject) {
                badSelection = true;
                currConf = null;
                currProject = null;
            }
            if (!badSelection) {
                if (currProject != null && currConf != null) {
                    proc().selectedProject().set(currProject);
                    proc().selectedConfiguration().set(currConf);
                } else if (currProject != null && currConf == null) {
                    proc().selectedProject().set(currProject);
                    proc().selectedConfiguration().set(currProject.configurations().activeConfiguration().get());
                } else if (currProject == null && currConf != null) {
                    proc().selectedProject().set(currConf.project().get());
                    proc().selectedConfiguration().set(currConf);
                } else if (currProject == null && currConf == null) {
                    HWProject project = proc().solution() == null ? null : proc().solution().get().activeProject().get();
                    proc().selectedProject().set(project);
                    proc().selectedConfiguration().set(project == null ? null : project.configurations().activeConfiguration().get());
                }
            } else {
                HWProject project = proc().solution() == null ? null : proc().solution().get().activeProject().get();
                proc().selectedProject().set(project);
                proc().selectedConfiguration().set(project == null ? null : project.configurations().activeConfiguration().get());
            }
        }
        TreePath path = e.getNewLeadSelectionPath();
        if (path == null) {
            app().activeProperties().set(null);
        } else {
            HWProjectItem item = (HWProjectItem) LazyTree.resolveLazyTreeNodeValue(path.getLastPathComponent());
            app().activeProperties().set(item == null ? null : item.toPropertiesAware());
        }
        refreshTools();
    }

}
