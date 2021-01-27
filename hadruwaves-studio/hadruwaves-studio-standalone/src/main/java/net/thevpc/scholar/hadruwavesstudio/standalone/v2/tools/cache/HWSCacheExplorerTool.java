package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.cache;

import net.thevpc.echo.swing.core.swing.DefaultLazyTreeBackend;
import net.thevpc.echo.swing.core.swing.LazyTree;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.AbstractToolWindowPanel;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.DefaultAppTreeCellRenderer;
import org.jdesktop.swingx.JXTree;

public class HWSCacheExplorerTool extends AbstractToolWindowPanel {

    private JTree tree;

    public HWSCacheExplorerTool(HadruwavesStudio application) {
        super(application);
        tree = LazyTree.of(new JXTree(), new DefaultLazyTreeBackend(new File(Maths.Config.getCacheFolder()), "Cache Root", true));
        tree.setCellRenderer(new DefaultAppTreeCellRenderer(studio()) {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                Component u = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                //
                return u;
            }
        });
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
        tree.setCellRenderer(new DefaultAppTreeCellRenderer(studio()) {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                Component u = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                //
                return u;
            }
        });
    }
}
