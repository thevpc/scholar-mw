/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.util;

import net.thevpc.scholar.hadruwaves.project.HWSolution;
import net.thevpc.scholar.hadruwaves.project.HWProject;
import net.thevpc.echo.AppPropertiesTree;

/**
 *
 * @author vpc
 */
public interface HWPropertiesTree extends AppPropertiesTree {

    HWSolution getSolution();

    HWProject getProject();
}
