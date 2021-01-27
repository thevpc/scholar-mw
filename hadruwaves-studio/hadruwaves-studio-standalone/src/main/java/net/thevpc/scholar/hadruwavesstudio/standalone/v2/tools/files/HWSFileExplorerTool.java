package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.files;

import net.thevpc.common.props.PropertyEvent;
import net.thevpc.common.props.PropertyListener;
import net.thevpc.echo.swing.core.swing.DefaultLazyTreeBackend;
import net.thevpc.echo.swing.core.swing.LazyTree;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import org.jdesktop.swingx.JXTree;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;
import net.thevpc.scholar.hadruwaves.project.HWProject;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.AbstractToolWindowPanel;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.DefaultAppTreeCellRenderer;

public class HWSFileExplorerTool extends AbstractToolWindowPanel {

    private JTree tree;
    private PropertyListener projectChangeListener = new PropertyListener() {
        @Override
        public void propertyUpdated(PropertyEvent event) {
            projectUpdated();
        }
    };

    public HWSFileExplorerTool(HadruwavesStudio studio) {
        super(studio);
        tree = LazyTree.of(new JXTree(), new DefaultLazyTreeBackend(new File(System.getProperty("user.home")), "Home Directory", true));
        proc().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                if (event.getProperty().name().equals("activeProject")) {
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
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);
        tb.add(new JButton(".."));
        tb.add(new JButton("->"));
        pane.add(tb, BorderLayout.PAGE_START);
        pane.add(jsp, BorderLayout.CENTER);
        setContent(pane);
    }

    protected void onLookChanged() {
        tree.setCellRenderer(new DefaultAppTreeCellRenderer(studio()));
    }

    private void projectUpdated() {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        model.reload();
    }
}
