/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions;

/**
 *
 * @author vpc
 */
public abstract class AbstractHWSolverAction extends AbstractHWSolverActionNode implements HWSolverAction {

    private String id;

    public AbstractHWSolverAction(String id, String name, String path, String icon) {
        super(name, path, icon);
        this.id = id;
    }

    @Override
    public String id() {
        return id;
    }

}
