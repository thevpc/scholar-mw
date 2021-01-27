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
public interface HWSolverActionNode {

    String name();

    String path();

    HWSolverActionNode parent();

    String icon();

}
