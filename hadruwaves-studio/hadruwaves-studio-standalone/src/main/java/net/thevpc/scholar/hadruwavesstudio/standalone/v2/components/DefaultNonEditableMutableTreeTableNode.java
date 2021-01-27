/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.components;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

/**
 *
 * @author vpc
 */
public class DefaultNonEditableMutableTreeTableNode extends DefaultMutableTreeTableNode {
    
    public DefaultNonEditableMutableTreeTableNode(Object userObject, boolean allowsChildren) {
        super(userObject, allowsChildren);
    }

    @Override
    public boolean isEditable(int column) {
        return false;
    }
    
}
