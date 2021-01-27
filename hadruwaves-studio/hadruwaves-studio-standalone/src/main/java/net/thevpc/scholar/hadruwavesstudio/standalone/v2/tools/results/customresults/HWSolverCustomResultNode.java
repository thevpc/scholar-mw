/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.customresults;

import javax.swing.ImageIcon;

/**
 *
 * @author vpc
 */
public interface HWSolverCustomResultNode {

    String name();

    String path();

    HWSolverCustomResultFolder parent();

    ImageIcon icon();

}
