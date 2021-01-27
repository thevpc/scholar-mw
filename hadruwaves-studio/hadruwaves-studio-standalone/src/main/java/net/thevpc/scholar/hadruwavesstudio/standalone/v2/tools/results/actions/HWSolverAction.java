/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions;

import java.io.File;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.HWSolverActionContext;

/**
 *
 * @author vpc
 */
public interface HWSolverAction extends HWSolverActionNode {

    String id();

    void run(HWSolverActionContext context);

    boolean acceptResultFile(File file);

}
