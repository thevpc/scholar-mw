package net.thevpc.scholar.hadruwavesstudio.standalone.v1;
/*
 * @(#)DefaultTreeModel.java	1.55 04/05/05
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import java.io.Serializable;
import java.util.EventListener;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * A simple tree data model that uses TreeNodes.
 * For further information and examples that use DefaultTreeModel,
 * see <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/tree.html">How to Use Trees</a>
 * in <em>The Java Tutorial.</em>
 * <p/>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running
 * the same version of Swing.  As of 1.4, support for long term storage
 * of all JavaBeans<sup><font size="-2">TM</font></sup>
 * has been added to the <code>java.beans</code> package.
 * Please see {@link java.beans.XMLEncoder}.
 *
 * @author Rob Davis
 * @author Ray Ryan
 * @author Scott Violet
 * @version 1.55 05/05/04
 */
public abstract class AbstractMutableTreeModel implements Serializable, TreeModel {
    /**
     * Listeners.
     */
    protected EventListenerList listenerList = new EventListenerList();


    public AbstractMutableTreeModel() {
        super();
    }


    /**
     * This sets the user object of the Object identified by path
     * and posts a node changed.  If you use custom user objects in
     * the TreeModel you're going to need to subclass this and
     * set the user object of the changed node to something meaningful.
     */
    public void valueForPathChanged(TreePath path, Object newValue) {
        Object aNode = path.getLastPathComponent();

        nodeChanged(aNode);
    }

    /**
     * Invoked this to insert newChild at location index in parents children.
     * This will then message nodesWereInserted to create the appropriate
     * event. This is the preferred way to add children as it will create
     * the appropriate event.
     */
    public void insertNodeInto(Object newChild,
                               Object parent, int index) {
        insertNode(parent, newChild, index);

        int[]           newIndexs = new int[1];

        newIndexs[0] = index;
        nodesWereInserted(parent, newIndexs);
    }

    public abstract void removeNode(Object parent, int index);

    public abstract void insertNode(Object parent, Object child, int index);

    public abstract Object getNodeParent(Object child);

    /**
     * Message this to remove node from its parent. This will message
     * nodesWereRemoved to create the appropriate event. This is the
     * preferred way to remove a node as it handles the event creation
     * for you.
     */
    public void removeNodeFromParent(Object node) {
        Object parent = getNodeParent(node);

        if (parent == null)
            throw new IllegalArgumentException("node does not have a parent.");

        int[]            childIndex = new int[1];
        Object[]         removedArray = new Object[1];

        childIndex[0] = getIndexOfChild(parent, node);
        removeNode(parent, childIndex[0]);
        removedArray[0] = node;
        nodesWereRemoved(parent, childIndex, removedArray);
    }

    /**
     * Invoke this method after you've changed how node is to be
     * represented in the tree.
     */
    public void nodeChanged(Object node) {
        if (listenerList != null && node != null) {
            Object parent = getNodeParent(node);

            if (parent != null) {
                int anIndex = getIndexOfChild(parent, node);
                if (anIndex != -1) {
                    int[]        cIndexs = new int[1];

                    cIndexs[0] = anIndex;
                    nodesChanged(parent, cIndexs);
                }
            } else if (node == getRoot()) {
                nodesChanged(node, null);
            }
        }
    }

    /**
     * Invoke this method if you've modified the Objects upon which this
     * model depends.  The model will notify all of its listeners that the
     * model has changed below the node <code>node</code> (PENDING).
     */
    public void reload() {
        reload(getRoot());
    }

    public void reload(Object node) {
        if (node != null) {
            fireTreeStructureChanged(this, getPathToRoot(node), null, null);
        }
    }

    /**
     * Invoke this method after you've inserted some Objects into
     * node.  childIndices should be the index of the new primitiveElement3DS and
     * must be sorted in ascending order.
     */
    public void nodesWereInserted(Object node, int[] childIndices) {
        if (listenerList != null && node != null && childIndices != null
                && childIndices.length > 0) {
            int cCount = childIndices.length;
            Object[]          newChildren = new Object[cCount];

            for (int counter = 0; counter < cCount; counter++)
                newChildren[counter] = getChild(node, childIndices[counter]);
            fireObjectsInserted(this, getPathToRoot(node), childIndices,
                    newChildren);
        }
    }

    /**
     * Invoke this method after you've removed some Objects from
     * node.  childIndices should be the index of the removed primitiveElement3DS and
     * must be sorted in ascending order. And removedChildren should be
     * the array of the children objects that were removed.
     */
    public void nodesWereRemoved(Object node, int[] childIndices,
                                 Object[] removedChildren) {
        if (node != null && childIndices != null) {
            fireObjectsRemoved(this, getPathToRoot(node), childIndices,
                    removedChildren);
        }
    }

    /**
     * Invoke this method after you've changed how the children identified by
     * childIndicies are to be represented in the tree.
     */
    public void nodesChanged(Object node, int[] childIndices) {
        if (node != null) {
            if (childIndices != null) {
                int cCount = childIndices.length;

                if (cCount > 0) {
                    Object[]       cChildren = new Object[cCount];

                    for (int counter = 0; counter < cCount; counter++)
                        cChildren[counter] = getChild(node, childIndices[counter]);
                    fireObjectsChanged(this, getPathToRoot(node),
                            childIndices, cChildren);
                }
            } else if (node == getRoot()) {
                fireObjectsChanged(this, getPathToRoot(node), null, null);
            }
        }
    }

    /**
     * Invoke this method if you've totally changed the children of
     * node and its childrens children...  This will post a
     * treeStructureChanged event.
     */
    public void nodeStructureChanged(Object node) {
        if (node != null) {
            fireTreeStructureChanged(this, getPathToRoot(node), null, null);
        }
    }

    /**
     * Builds the parents of node up to and including the root node,
     * where the original node is the last primitiveElement3D in the returned array.
     * The length of the returned array gives the node's depth in the
     * tree.
     *
     * @param aNode the Object to get the path for
     */
    public Object[] getPathToRoot(Object aNode) {
        return getPathToRoot(aNode, 0);
    }

    /**
     * Builds the parents of node up to and including the root node,
     * where the original node is the last primitiveElement3D in the returned array.
     * The length of the returned array gives the node's depth in the
     * tree.
     *
     * @param aNode the Object to get the path for
     * @param depth an int giving the number of steps already taken towards
     *              the root (on recursive calls), used to size the returned array
     * @return an array of Objects giving the path from the root to the
     *         specified node
     */
    protected Object[] getPathToRoot(Object aNode, int depth) {
        Object[]              retNodes;
        // This method recurses, traversing towards the root in order
        // size the array. On the way back, it fills in the nodes,
        // starting from the root and working back to the original node.

        /* Check for null, in case someone passed in a null node, or
           they passed in an primitiveElement3D that isn't rooted at root. */
        if (aNode == null) {
            if (depth == 0)
                return null;
            else
                retNodes = new Object[depth];
        } else {
            depth++;
            if (getRoot().equals(aNode))
                retNodes = new Object[depth];
            else
                retNodes = getPathToRoot(getNodeParent(aNode), depth);
            retNodes[retNodes.length - depth] = aNode;
        }
        return retNodes;
    }

    //
    //  Events
    //

    /**
     * Adds a listener for the TreeModelEvent posted after the tree changes.
     *
     * @param l the listener to add
     * @see #removeTreeModelListener
     */
    public void addTreeModelListener(TreeModelListener l) {
        listenerList.add(TreeModelListener.class, l);
    }

    /**
     * Removes a listener previously added with <B>addTreeModelListener()</B>.
     *
     * @param l the listener to remove
     * @see #addTreeModelListener
     */
    public void removeTreeModelListener(TreeModelListener l) {
        listenerList.remove(TreeModelListener.class, l);
    }

    /**
     * Returns an array of all the tree model listeners
     * registered on this model.
     *
     * @return all of this model's <code>TreeModelListener</code>s
     *         or an empty
     *         array if no tree model listeners are currently registered
     * @see #addTreeModelListener
     * @see #removeTreeModelListener
     * @since 1.4
     */
    public TreeModelListener[] getTreeModelListeners() {
        return (TreeModelListener[]) listenerList.getListeners(
                TreeModelListener.class);
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     *
     * @param source       the node being changed
     * @param path         the path to the root node
     * @param childIndices the indices of the changed primitiveElement3DS
     * @param children     the changed primitiveElement3DS
     * @see EventListenerList
     */
    protected void fireObjectsChanged(Object source, Object[] path,
                                      int[] childIndices,
                                      Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path,
                            childIndices, children);
                ((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     *
     * @param source       the node where new primitiveElement3DS are being inserted
     * @param path         the path to the root node
     * @param childIndices the indices of the new primitiveElement3DS
     * @param children     the new primitiveElement3DS
     * @see EventListenerList
     */
    protected void fireObjectsInserted(Object source, Object[] path,
                                       int[] childIndices,
                                       Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path,
                            childIndices, children);
                ((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     *
     * @param source       the node where primitiveElement3DS are being removed
     * @param path         the path to the root node
     * @param childIndices the indices of the removed primitiveElement3DS
     * @param children     the removed primitiveElement3DS
     * @see EventListenerList
     */
    protected void fireObjectsRemoved(Object source, Object[] path,
                                      int[] childIndices,
                                      Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path,
                            childIndices, children);
                ((TreeModelListener) listeners[i + 1]).treeNodesRemoved(e);
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     *
     * @param source       the node where the tree model has changed
     * @param path         the path to the root node
     * @param childIndices the indices of the affected primitiveElement3DS
     * @param children     the affected primitiveElement3DS
     * @see EventListenerList
     */
    protected void fireTreeStructureChanged(Object source, Object[] path,
                                            int[] childIndices,
                                            Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path,
                            childIndices, children);
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
            }
        }
    }

    /*
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     *
     * @param source the node where the tree model has changed
     * @param path the path to the root node
     * @see EventListenerList
     */
    private void fireTreeStructureChanged(Object source, TreePath path) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path);
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
            }
        }
    }

    /**
     * Returns an array of all the objects currently registered
     * as <code><em>Foo</em>Listener</code>s
     * upon this model.
     * <code><em>Foo</em>Listener</code>s are registered using the
     * <code>add<em>Foo</em>Listener</code> method.
     * <p/>
     * <p/>
     * <p/>
     * You can specify the <code>listenerType</code> argument
     * with a class literal,
     * such as
     * <code><em>Foo</em>Listener.class</code>.
     * For example, you can query a
     * <code>DefaultTreeModel</code> <code>m</code>
     * for its tree model listeners with the following code:
     * <p/>
     * <pre>TreeModelListener[] tmls = (TreeModelListener[])(m.getListeners(TreeModelListener.class));</pre>
     * <p/>
     * If no such listeners exist, this method returns an empty array.
     *
     * @param listenerType the type of listeners requested; this parameter
     *                     should specify an interface that descends from
     *                     <code>java.util.EventListener</code>
     * @return an array of all objects registered as
     *         <code><em>Foo</em>Listener</code>s on this component,
     *         or an empty array if no such
     *         listeners have been added
     * @throws ClassCastException if <code>listenerType</code>
     *                            doesn't specify a class or interface that implements
     *                            <code>java.util.EventListener</code>
     * @see #getTreeModelListeners
     * @since 1.3
     */
    public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
        return listenerList.getListeners(listenerType);
    }

} // End of class DefaultTreeModel


