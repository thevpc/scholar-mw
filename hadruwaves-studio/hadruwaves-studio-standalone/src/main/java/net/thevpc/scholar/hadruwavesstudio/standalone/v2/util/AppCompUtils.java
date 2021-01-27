/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.util;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;
import net.thevpc.echo.AppPopupMenu;
import net.thevpc.echo.swing.core.swing.JPopupMenuComponentSupplier;
import org.jdesktop.swingx.JXTreeTable;

/**
 *
 * @author vpc
 */
public class AppCompUtils {

    public static void updateUI(Object any) {
        if (any instanceof AppPopupMenu) {
            AppPopupMenu popUpMenu = (AppPopupMenu) any;
            SwingUtilities.updateComponentTreeUI(((JPopupMenuComponentSupplier) popUpMenu).component());
        }
    }

    public static void bind(AppPopupMenu popUpMenu, JComponent source, Runnable preparePopupBeforeShowing) {
        if (source instanceof JXTreeTable) {
            JXTreeTable tree = (JXTreeTable) source;
            MouseListener ml = new JXTreeTableMouseAdapterImpl(tree, popUpMenu, preparePopupBeforeShowing);
            tree.addMouseListener(ml);
        }
        if (source instanceof JTree) {
            JTree tree = (JTree) source;
            MouseListener ml = new JTreeMouseAdapterImpl(tree, popUpMenu, preparePopupBeforeShowing);
            tree.addMouseListener(ml);
        }
    }

    private static class JXTreeTableMouseAdapterImpl extends MouseAdapter {

        private final JXTreeTable tree;
        private final AppPopupMenu popUpMenu;
        private final Runnable preparePopupBeforeShowing;

        public JXTreeTableMouseAdapterImpl(JXTreeTable tree, AppPopupMenu popUpMenu, Runnable preparePopupBeforeShowing) {
            this.tree = tree;
            this.popUpMenu = popUpMenu;
            this.preparePopupBeforeShowing = preparePopupBeforeShowing;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
            if (e.isPopupTrigger()) {
                if (selPath != null) {
                    tree.getTreeSelectionModel().setSelectionPath(selPath);
                } else {
                    tree.getTreeSelectionModel().setSelectionPath(null);
                }
                try {
                    popUpMenu.tools().refresh();
                    preparePopupBeforeShowing.run();
                    if (popUpMenu.isActionable()) {
                        popUpMenu.show(tree, e.getX(), e.getY());
                    }
                } catch (Exception ex) {
                    System.err.println(ex);
                }
            }

        }
    }

    private static class JTreeMouseAdapterImpl extends MouseAdapter {

        private final JTree tree;
        private final AppPopupMenu popUpMenu;
        private final Runnable preparePopupBeforeShowing;

        public JTreeMouseAdapterImpl(JTree tree, AppPopupMenu popUpMenu, Runnable preparePopupBeforeShowing) {
            this.tree = tree;
            this.popUpMenu = popUpMenu;
            this.preparePopupBeforeShowing = preparePopupBeforeShowing;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
            if (e.isPopupTrigger()) {
                if (selPath != null) {
                    tree.getSelectionModel().setSelectionPath(selPath);
                } else {
                    tree.getSelectionModel().setSelectionPath(null);
                }
                try {
                    popUpMenu.tools().refresh();
                    preparePopupBeforeShowing.run();
                    if (popUpMenu.isActionable()) {
                        popUpMenu.show(tree, e.getX(), e.getY());
                    }
                } catch (Exception ex) {
                    System.err.println(ex);
                }
            }

        }
    }

}
