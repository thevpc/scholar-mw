package net.vpc.scholar.hadruwavesstudio.standalone.v2.util;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.Enumeration;

public abstract class LoadableMutableTreeNode extends DefaultMutableTreeNode {
    @Override
    public TreeNode getChildAt(int index) {
        prepareChildren0();
        return super.getChildAt(index);
    }

    @Override
    public int getIndex(TreeNode aChild) {
        prepareChildren0();
        return super.getIndex(aChild);
    }

    @Override
    public Enumeration children() {
        prepareChildren0();
        return super.children();
    }

    @Override
    public void remove(MutableTreeNode aChild) {
        prepareChildren0();
        super.remove(aChild);
    }

    @Override
    public void removeAllChildren() {
        prepareChildren0();
        super.removeAllChildren();
    }

    @Override
    public void add(MutableTreeNode newChild) {
        prepareChildren0();
        super.add(newChild);
    }

    @Override
    public void insert(MutableTreeNode newChild, int childIndex) {
        prepareChildren0();
        super.insert(newChild, childIndex);
    }

    @Override
    public void remove(int childIndex) {
        prepareChildren0();
        super.remove(childIndex);
    }

    @Override
    public TreeNode getFirstChild() {
        prepareChildren0();
        return super.getFirstChild();
    }

    @Override
    public TreeNode getLastChild() {
        prepareChildren0();
        return super.getLastChild();
    }

    @Override
    public TreeNode getChildAfter(TreeNode aChild) {
        prepareChildren0();
        return super.getChildAfter(aChild);
    }

    @Override
    public TreeNode getChildBefore(TreeNode aChild) {
        prepareChildren0();
        return super.getChildBefore(aChild);
    }

    @Override
    public int getSiblingCount() {
        prepareChildren0();
        return super.getSiblingCount();
    }

    @Override
    public DefaultMutableTreeNode getFirstLeaf() {
        prepareChildren0();
        return super.getFirstLeaf();
    }

    @Override
    public DefaultMutableTreeNode getLastLeaf() {
        prepareChildren0();
        return super.getLastLeaf();
    }

    @Override
    public DefaultMutableTreeNode getNextLeaf() {
        prepareChildren0();
        return super.getNextLeaf();
    }

    @Override
    public DefaultMutableTreeNode getPreviousLeaf() {
        prepareChildren0();
        return super.getPreviousLeaf();
    }

    protected void prepareChildren0() {
        if (children == null) {
            prepareChildren();
        }
    }

    protected abstract void prepareChildren();
}
