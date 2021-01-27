package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.library;

import net.thevpc.echo.swing.core.swing.LazyTreeBackend;
import net.thevpc.echo.swing.core.swing.LazyTree;
import net.thevpc.echo.swing.core.swing.LazyTreeNode;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import org.jdesktop.swingx.JXTree;

import javax.swing.*;
import java.awt.*;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.AbstractToolWindowPanel;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.DefaultAppTreeCellRenderer;

public class HWSLibraryExplorerTool extends AbstractToolWindowPanel {

    private JTree tree;

    public HWSLibraryExplorerTool(HadruwavesStudio studio) {
        super(studio);

        tree = LazyTree.of(new JXTree(), new LazyTreeBackend() {
            @Override
            public LazyTreeNode getRoot() {
                return new LazyTreeNode("Library", "/", "/", true);
            }

            @Override
            public LazyTreeNode[] getChildren(LazyTreeNode parent) {
                switch (parent.getPath()) {
                    case "/": {
                        return new LazyTreeNode[]{
                            new LazyTreeNode("Materials", null, "/Material", true),
                            new LazyTreeNode("Demos", null, "/Demos", true)
                        };
                    }
                    case "/Material": {
                        return new LazyTreeNode[]{
                            new LazyTreeNode("Vacuum", null, "/Material/Vacuum", false),
                            new LazyTreeNode("PEC", null, "/Material/PEC", false),
                            new LazyTreeNode("PMC", null, "/Material/PMC", false)
                        };
                    }
                    case "/Demos": {
                        return new LazyTreeNode[]{
                            new LazyTreeNode("MicrostripAntenna1", null, "/Demos/MicrostripAntenna1", false),
                            new LazyTreeNode("MicrostripAntenna2", null, "/Demos/MicrostripAntenna2", false),};
                    }
                }
                return new LazyTreeNode[0];
            }
        });

        JPanel pane = new JPanel(new BorderLayout());
//        pane.add(toolbar,BorderLayout.PAGE_START);
        tree.setBorder(null);
        JScrollPane jsp = new JScrollPane(tree);
        jsp.setBorder(null);
        pane.add(jsp, BorderLayout.CENTER);
        setContent(pane);
    }

    protected void onLookChanged() {
        tree.setCellRenderer(new DefaultAppTreeCellRenderer(studio()));
    }
}
