package net.vpc.scholar.hadruwaves.studio.standalone.v2.win;

import net.vpc.common.swings.app.impl.swing.DefaultLazyTreeBackend;
import net.vpc.common.swings.app.impl.swing.LazyTree;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadruwaves.studio.standalone.v2.HadruwavesStudioV2;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.io.File;

public class HWSCacheExplorerTool extends JPanel {
    private HadruwavesStudioV2 application;

    public HWSCacheExplorerTool(HadruwavesStudioV2 application) {
        super(new BorderLayout());
        this.application = application;
        JTree tree = new LazyTree(new DefaultLazyTreeBackend(new File(Maths.Config.getCacheFolder()),"Cache Root",true));
        tree.setCellRenderer(new DefaultTreeCellRenderer() {
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
        add(pane);

    }

//    private static class SolutionLoadableMutableTreeNode extends LoadableMutableTreeNode {
//        public SolutionLoadableMutableTreeNode(Object o) {
//            setUserObject(o);
//            prepareChildren0();
//        }
//
//        @Override
//        public boolean isLeaf() {
//            if (userObject instanceof PValue) {
//                Object o = ((PValue) userObject).get();
//                if (o instanceof HWSolution) {
//                    return false;
//                }
//            } else if (userObject instanceof HWSolution) {
//                return false;
//            } else if (userObject instanceof WritablePIndexedNode) {
//                HWSolutionElement o = ((WritablePIndexedNode<HWSolutionElement>) userObject).get();
//                if (o instanceof HWSolutionFolder) {
//                    return false;
//                }
//            }
//            return true;
//        }
//
//        @Override
//        protected void prepareChildren() {
//            Object userObject = getUserObject();
//            if (userObject instanceof PValue) {
//                Object o = ((PValue) userObject).get();
//                if (children == null) {
//                    children = new Vector();
//                }
//                for (WritablePIndexedNode<HWSolutionElement> child : ((HWSolution) o).children()) {
//                    children.add(new SolutionLoadableMutableTreeNode(child));
//                }
//            } else if (userObject instanceof HWSolution) {
//                if (children == null) {
//                    children = new Vector();
//                }
//                for (WritablePIndexedNode<HWSolutionElement> child : ((HWSolution) userObject).children()) {
//                    children.add(new SolutionLoadableMutableTreeNode(child));
//                }
//            } else if (userObject instanceof WritablePIndexedNode) {
//                if (children == null) {
//                    children = new Vector();
//                }
//                for (WritablePIndexedNode<HWSolutionElement> child : ((WritablePIndexedNode<HWSolutionElement>) userObject).children()) {
//                    children.add(new SolutionLoadableMutableTreeNode(child));
//                }
//            }
//        }
//    }

}
