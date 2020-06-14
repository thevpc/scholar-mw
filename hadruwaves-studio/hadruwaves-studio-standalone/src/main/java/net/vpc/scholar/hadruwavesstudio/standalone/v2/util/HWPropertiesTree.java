/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.util;

import net.vpc.scholar.hadruwaves.project.HWSolution;
import net.vpc.scholar.hadruwaves.project.HWProject;
import net.vpc.common.app.AppPropertiesTree;

/**
 *
 * @author vpc
 */
public interface HWPropertiesTree extends AppPropertiesTree {

    HWSolution getSolution();

    HWProject getProject();
}
