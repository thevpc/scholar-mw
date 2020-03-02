package net.vpc.scholar.hadruwaves.studio.standalone.v2.win;

import net.vpc.common.prpbind.PropertyEvent;
import net.vpc.common.prpbind.PropertyListener;
import net.vpc.common.swings.app.impl.swing.DefaultLazyTreeBackend;
import net.vpc.common.swings.app.impl.swing.LazyTree;
import net.vpc.scholar.hadruwaves.project.HWProject;
import net.vpc.scholar.hadruwaves.studio.standalone.v2.HadruwavesStudioV2;
import org.jdesktop.swingx.JXTree;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;

public class HWSFileExplorerTool extends JPanel {
    private HadruwavesStudioV2 application;
    private JTree tree;
    private PropertyListener projectChangeListener = new PropertyListener() {
        @Override
        public void propertyUpdated(PropertyEvent event) {
            projectUpdated();
        }
    };
    public HWSFileExplorerTool(HadruwavesStudioV2 application) {
        super(new BorderLayout());
        this.application = application;
        tree = LazyTree.of(new JXTree(),new DefaultLazyTreeBackend(new File(System.getProperty("user.home")),"Home Directory",true));
        this.application.proc().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                if (event.getProperty().getPropertyName().equals("activeProject")) {
                    HWProject oldValue = event.getOldValue();
                    if (oldValue != null) {
                        oldValue.listeners().remove(projectChangeListener);
                    }
                    HWProject newValue = event.getNewValue();
                    if (newValue != null) {
                        newValue.listeners().add(projectChangeListener);
                    }
                    projectUpdated();
                }
            }
        });
//        tree.setCellRenderer(new DefaultTreeCellRenderer() {
//            @Override
//            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
//                Component u = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
//                //
//                return u;
//            }
//        });
        JPanel pane = new JPanel(new BorderLayout());
//        pane.add(toolbar,BorderLayout.PAGE_START);
        tree.setBorder(null);
        JScrollPane jsp = new JScrollPane(tree);
        jsp.setBorder(null);
        JToolBar tb=new JToolBar();
        tb.setFloatable(false);
        tb.add(new JButton(".."));
        tb.add(new JButton("->"));
        pane.add(tb, BorderLayout.PAGE_START);
        pane.add(jsp, BorderLayout.CENTER);
        add(pane);
    }
    private void projectUpdated() {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        model.reload();
    }
}
