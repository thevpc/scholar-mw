package net.vpc.scholar.hadruwaves.studio.standalone.v2.win;

import net.vpc.common.swings.app.impl.swing.LazyTreeBackend;
import net.vpc.common.swings.app.impl.swing.LazyTree;
import net.vpc.common.swings.app.impl.swing.LazyTreeNode;
import net.vpc.scholar.hadruwaves.studio.standalone.v2.HadruwavesStudioV2;
import org.jdesktop.swingx.JXTree;

import javax.swing.*;
import java.awt.*;

public class HWSLibraryExplorerTool extends JPanel {
    private HadruwavesStudioV2 application;

    public HWSLibraryExplorerTool(HadruwavesStudioV2 application) {
        super(new BorderLayout());
        this.application = application;

        JTree tree = LazyTree.of(new JXTree(), new LazyTreeBackend() {
            @Override
            public LazyTreeNode getRoot() {
                return  new LazyTreeNode("Library", "/", "/", true);
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
                                new LazyTreeNode("MicrostripAntenna2", null, "/Demos/MicrostripAntenna2", false),
                        };
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
        add(pane);
    }
}
