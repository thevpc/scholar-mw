/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions;

/**
 *
 * @author vpc
 */
public abstract class AbstractHWSolverActionNode implements HWSolverActionNode {

    private String name;

    private String path;

    private HWSolverActionNode parent;

    private String icon;
    
    public AbstractHWSolverActionNode(String name, String path, String icon) {
        this.name = name;
        this.path = path;
        this.icon = icon;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String path() {
        return path;
    }

    @Override
    public HWSolverActionNode parent() {
        return parent;
    }

    @Override
    public String icon() {
        return icon;
    }
    
    
    

}
