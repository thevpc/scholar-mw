/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.studio.core.ui;

import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author vpc
 */
public class StrExplorerChildFactory extends ChildFactory<Object>{
    private Object baseObject;

    public StrExplorerChildFactory(Object baseObject) {
        this.baseObject = baseObject;
    }
    
    @Override
    protected boolean createKeys(List<Object> toPopulate) {
        if(baseObject==null){
            toPopulate.add("A");
            toPopulate.add("B");
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(Object key) {
        return new StrExplorerNode(key);
    }
    
}
