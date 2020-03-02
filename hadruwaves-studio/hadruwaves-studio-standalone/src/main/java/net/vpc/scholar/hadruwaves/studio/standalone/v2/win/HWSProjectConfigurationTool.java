package net.vpc.scholar.hadruwaves.studio.standalone.v2.win;

import net.vpc.common.prpbind.PropertyEvent;
import net.vpc.common.prpbind.PropertyListener;
import net.vpc.common.prpbind.WritablePIndexedNode;
import net.vpc.common.prpbind.WritablePNamedNode;
import net.vpc.scholar.hadruwaves.project.HWProject;
import net.vpc.scholar.hadruwaves.project.configuration.HWSConfigurationElement;
import net.vpc.scholar.hadruwaves.project.configuration.HWSConfigurationFolder;
import net.vpc.scholar.hadruwaves.project.configuration.HWSConfigurationRun;
import net.vpc.scholar.hadruwaves.project.configuration.HWSConfigurations;
import net.vpc.scholar.hadruwaves.studio.standalone.v2.HadruwavesStudioV2;
import org.jdesktop.swingx.JXTree;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Vector;

public class HWSProjectConfigurationTool extends JPanel {
    private HadruwavesStudioV2 application;
    private JXTree tree;
    private PropertyListener configurationChangedListener = new PropertyListener() {
        @Override
        public void propertyUpdated(PropertyEvent event) {
            configurationChanged();
        }
    };

    public HWSProjectConfigurationTool(HadruwavesStudioV2 application) {
        super(new BorderLayout());
        this.application = application;
        JToolBar toolbar = new JToolBar();
        toolbar.add(new AbstractAction() {
            {
                putValue(NAME, "add");
            }

            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        toolbar.add(new AbstractAction() {
            {
                putValue(NAME, "remove");
            }

            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        toolbar.add(new AbstractAction() {
            {
                putValue(NAME, "copy");
            }

            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        toolbar.setFloatable(false);
        toolbar.add(new AbstractAction() {
            {
                putValue(NAME, "run");
            }

            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        tree = new JXTree();
        application.proc().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                if (event.getProperty().getPropertyName().equals("activeProject")) {
                    HWProject oldProject = event.getOldValue();
                    HWProject newProject = event.getNewValue();
                    if (oldProject != null) {
                        oldProject.configurations().listeners().remove(configurationChangedListener);
                    }
                    if (newProject != null) {
                        newProject.configurations().listeners().add(configurationChangedListener);
                    }
                    configurationChanged();
                }
            }
        });
        tree.setCellRenderer(new DefaultAppTreeCellRenderer(application) {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                Component u = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                if (value instanceof ConfLoadableMutableTreeNode) {
                    value = ((ConfLoadableMutableTreeNode) value).getUserObject();
                    if (value instanceof HWSConfigurations) {
                        setText("Configurations");
                    } else if (value instanceof WritablePIndexedNode) {
                        HWSConfigurationElement cn = ((WritablePIndexedNode<HWSConfigurationElement>) value).get();
                        setText(cn.name().get());
                        if (cn instanceof HWSConfigurationRun) {
                            if (application.activeProjectConfiguration() == cn) {
                                super.setForeground(getActivatedColor(sel));
                            }
                        }
                    }
                }
                return u;
            }
        });
        configurationChanged();
        JPanel pane = new JPanel(new BorderLayout());
        pane.add(toolbar, BorderLayout.PAGE_START);
        tree.setBorder(null);
        JScrollPane jsp = new JScrollPane(tree);
        jsp.setBorder(null);
        pane.add(jsp, BorderLayout.CENTER);
        add(pane);
    }

    private void configurationChanged() {
        HWProject project = application.activeProject();
        tree.setModel(new DefaultTreeModel(new ConfLoadableMutableTreeNode(project == null ? null : project.configurations())));
    }


    private static class ConfLoadableMutableTreeNode extends LoadableMutableTreeNode {
        public ConfLoadableMutableTreeNode(Object o) {
            setUserObject(o);
            prepareChildren0();
        }

        @Override
        public boolean isLeaf() {
            if (userObject instanceof HWSConfigurations) {
                return false;
            } else if (userObject instanceof WritablePIndexedNode) {
                HWSConfigurationElement o = ((WritablePIndexedNode<HWSConfigurationElement>) userObject).get();
                if (o instanceof HWSConfigurationFolder) {
                    return false;
                }
            }
            return true;
        }

        @Override
        protected void prepareChildren() {
            Object userObject = getUserObject();
            if (userObject instanceof HWSConfigurations) {
                if (children == null) {
                    children = new Vector();
                }
                for (WritablePNamedNode<HWSConfigurationElement> child : ((HWSConfigurations) userObject).children().values()) {
                    children.add(new ConfLoadableMutableTreeNode(child));
                }
            } else if (userObject instanceof WritablePIndexedNode) {
                if (children == null) {
                    children = new Vector();
                }
                for (WritablePIndexedNode<HWSConfigurationElement> child : ((WritablePIndexedNode<HWSConfigurationElement>) userObject).children()) {
                    children.add(new ConfLoadableMutableTreeNode(child));
                }
            }
        }
    }
}
