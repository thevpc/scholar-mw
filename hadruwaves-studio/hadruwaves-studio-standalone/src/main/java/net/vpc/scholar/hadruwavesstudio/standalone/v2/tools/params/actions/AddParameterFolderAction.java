/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.params.actions;

import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.params.HWSProjectParametersTool;

import javax.swing.*;
import net.vpc.scholar.hadruwaves.project.parameter.HWParameterElement;
import net.vpc.scholar.hadruwaves.project.parameter.HWParameterFolder;

/**
 * @author vpc
 */
public class AddParameterFolderAction extends AddParameterActionBase {

    public AddParameterFolderAction(HWSProjectParametersTool outer) {
        super(outer, "AddParameterFolder");
    }

    protected HWParameterElement read() {
        String newName = "newFolder";
        newName = JOptionPane.showInputDialog(getMainComponent(), "New Folder", newName);
        if (newName != null) {
            return new HWParameterFolder(newName);
        }
        return null;
    }

}
