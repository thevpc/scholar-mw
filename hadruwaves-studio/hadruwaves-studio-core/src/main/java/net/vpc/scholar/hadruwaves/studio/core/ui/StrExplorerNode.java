/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.studio.core.ui;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

/**
 *
 * @author vpc
 */
public class StrExplorerNode extends AbstractNode {

    public StrExplorerNode(Object value) {
        super(Children.create(new StrExplorerChildFactory(value), true));
        setName(String.valueOf(value));
    }

}
