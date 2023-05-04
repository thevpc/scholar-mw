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
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;

import net.thevpc.echo.UserControl;
import net.thevpc.echo.api.components.AppContextMenu;
import net.thevpc.echo.api.components.AppMenu;
import net.thevpc.echo.ContextMenu;
import net.thevpc.echo.swing.peers.SwingPeer;
import org.jdesktop.swingx.JXTreeTable;

/**
 *
 * @author vpc
 */
public class AppCompUtils {

    public static void updateUI(Object any) {
        if (any instanceof AppMenu) {
            AppMenu popUpMenu = (AppMenu) any;
            SwingUtilities.updateComponentTreeUI(
                    (JPopupMenu) SwingPeer.gcompOf(popUpMenu)
            );
        }
    }

    public static void bind(ContextMenu popUpMenu, JComponent source, Runnable preparePopupBeforeShowing) {
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
        private final AppContextMenu popUpMenu;
        private final Runnable preparePopupBeforeShowing;

        public JXTreeTableMouseAdapterImpl(JXTreeTable tree, AppContextMenu popUpMenu, Runnable preparePopupBeforeShowing) {
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
//                    popUpMenu.model().refresh();
                    preparePopupBeforeShowing.run();
                    if (popUpMenu.actionable().get()) {
                        popUpMenu.show(new UserControl("",tree, popUpMenu.app()), e.getX(), e.getY());
                    }
                } catch (Exception ex) {
                    System.err.println(ex);
                }
            }

        }
    }

    private static class JTreeMouseAdapterImpl extends MouseAdapter {

        private final JTree tree;
        private final ContextMenu popUpMenu;
        private final Runnable preparePopupBeforeShowing;

        public JTreeMouseAdapterImpl(JTree tree, ContextMenu popUpMenu, Runnable preparePopupBeforeShowing) {
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
//                    popUpMenu.model().refresh();
                    preparePopupBeforeShowing.run();
                    if (popUpMenu.actionable().get()) {
                        popUpMenu.show(new UserControl("",tree, popUpMenu.app()), e.getX(), e.getY());
                    }
                } catch (Exception ex) {
                    System.err.println(ex);
                }
            }

        }
    }

}
