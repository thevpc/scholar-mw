package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer;

import net.vpc.scholar.hadruwaves.project.HWProjectComponent;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions.*;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.components.*;
import net.vpc.common.props.*;
import net.vpc.common.app.swing.core.swing.LazyTree;
import net.vpc.common.app.swing.core.swing.LazyTreeNode;
import net.vpc.scholar.hadruwaves.project.*;
import net.vpc.scholar.hadruwaves.project.scene.*;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
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
import net.vpc.common.props.impl.PropsHelper;
import net.vpc.common.app.AppPopupMenu;
import net.vpc.common.app.swing.core.swing.JPopupMenuComponentSupplier;
import net.vpc.common.app.swing.core.swing.SwingAppFactory;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationFolder;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.AbstractToolWindowPanel;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationElement;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.util.AppCompUtils;

public class HWSSolutionExplorerTool extends AbstractToolWindowPanel {

    public JXTree tree;
    private AppPopupMenu popUpMenu;

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
                if(e.getClickCount()==2){
                    TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                    if (selPath != null) {
                        HWProjectItem i = resolveValidItem(selPath);
                        if(i!=null && i.getItem() instanceof HWProjectSourceFile){
                            studio().openSourceFile((HWProjectSourceFile) i.getItem());
                        }
                    }
                }
            }
        });
        SwingAppFactory f = new SwingAppFactory();
        popUpMenu = f.createPopupMenu(studio.app(), "/solutionMenu");
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
        studio.proc().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                tree.repaint();
            }
        });
    }

    public void refreshTools() {
        popUpMenu.tools().refresh();
        app().tools().refresh();
    }

    protected void onLookChanged() {
        tree.setCellRenderer(new SolutionAppTreeCellRendererImpl(studio(), HWSSolutionExplorerTool.this));
        if (popUpMenu != null) {
            SwingUtilities.updateComponentTreeUI(((JPopupMenuComponentSupplier) popUpMenu).component());
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
        item.treePath=path;
        Object a = item.getItemValue();
        if (a != null) {
            return item;
        }
        return item;
    }

    private void createPopUpMenu() {
        popUpMenu.tools().addAction(new OpenSourceAction(this), "/open");
        popUpMenu.tools().addAction(new ReloadAction(this), "/reload");
        popUpMenu.tools().addAction(new SelectProjectAction(this), "/selectProject");
        popUpMenu.tools().addAction(new DeSelectProjectAction(this), "/deselectProject");
        popUpMenu.tools().addAction(new SelectConfigAction(this), "/selectConfig");
        popUpMenu.tools().addAction(new SaveNodeAction(this), "/save");
        popUpMenu.tools().addAction(new SaveAllNodeAction(this), "/saveAll");
        popUpMenu.tools().addAction(new SaveAsNodeAction(this), "/saveAs");
        popUpMenu.tools().addAction(new BuildNodeAction(this), "/build");
        popUpMenu.tools().addAction(new CopyNodeAction(this), "/copy");
        popUpMenu.tools().addAction(new CutNodeAction(this), "/cut");
        popUpMenu.tools().addAction(new PasteNodeAction(this), "/paste");

        popUpMenu.tools().addAction(new NewFolderUndoableAction(this), "/newFolder");
        popUpMenu.tools().addAction(new NewProjectUndoableAction(this), "/newProject");
        popUpMenu.tools().addAction(new NewMaterialAction(this), "/newMaterial");
        popUpMenu.tools().addAction(new NewConfigurationUndoableAction(this), "/newConfiguration");
        popUpMenu.tools().addAction(new NewMaterial2DPolygonAction(this), "/newMaterial2DPolygon");
        popUpMenu.tools().addAction(new NewMaterial2DRectangleAction(this), "/newMaterial2DRectangle");
        popUpMenu.tools().addAction(new NewMaterial3DBoxAction(this), "/newMaterial3DBox");
        popUpMenu.tools().addAction(new NewModalPortRectangleAction(this), "/newModalPortRectangle");
        popUpMenu.tools().addAction(new NewPlanarPortPolygonAction(this), "/newPlanarPortPolygon");
        popUpMenu.tools().addAction(new NewPlanarPortRectangleAction(this), "/newPlanarPortRectangle");

        popUpMenu.tools().addAction(new EnableUndoableActionImpl(this), "/enable");
        popUpMenu.tools().addAction(new DisableUndoableAction(this), "/disable");
        popUpMenu.tools().addAction(new PropertiesAppSimpleActionImpl(studio().app(), this), "/properties");
        popUpMenu.tools().addSeparator("/separator1");
        popUpMenu.tools().addAction(new RemoveUndoableAction(this), "/remove");

    }

    public static LazyTreeNode createLazyTreeNode(Object o, HWSolution solution, HWProject project, HadruwavesStudio application, String parentPath) {
        boolean folder = false;
        String name = null;
        Object ov = o;
        if (o instanceof PValue) {
            ov = ((PValue) o).get();
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
            folder=file.isDirectory();
            if(project==null) {
                project = h.project;
            }
            if(solution==null){
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
