/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.util;

import net.thevpc.echo.swing.helpers.tree.LazyTreeBackend;
import net.thevpc.echo.swing.helpers.tree.LazyTreeNode;

/**
 *
 * @author vpc
 */
public class LazyTreeBackendImpl implements LazyTreeBackend {
    
    private final CustomLazyNode r;

    public LazyTreeBackendImpl(CustomLazyNode r) {
        this.r = r;
    }

    @Override
    public LazyTreeNode getRoot() {
        return new LazyTreeNode(r.getName(), r, r.getPath(), r.isFodler());
    }

    @Override
    public LazyTreeNode[] getChildren(LazyTreeNode parent) {
        CustomLazyNode r = (CustomLazyNode) parent.getValue();
        return r.getList().stream().map((x) -> new LazyTreeNode(x.getName(), x, x.getPath(), x.isFodler())).toArray(LazyTreeNode[]::new);
    }
    
}
