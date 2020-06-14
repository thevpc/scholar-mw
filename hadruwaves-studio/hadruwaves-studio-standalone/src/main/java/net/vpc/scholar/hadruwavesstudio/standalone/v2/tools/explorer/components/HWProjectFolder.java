/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.components;

import net.vpc.scholar.hadruwaves.project.HWProject;

/**
 *
 * @author vpc
 */
public class HWProjectFolder {

    public String path;
    public HWProject project;

    public HWProjectFolder(String path, HWProject project) {
        this.path = path;
        this.project = project;
    }

    public HWProject project() {
        return project;
    }

}
