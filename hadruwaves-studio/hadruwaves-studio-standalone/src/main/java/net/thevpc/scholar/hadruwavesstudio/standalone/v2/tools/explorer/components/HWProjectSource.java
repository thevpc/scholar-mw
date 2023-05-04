/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.components;

import net.thevpc.scholar.hadruwaves.project.HWProject;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;

import java.io.File;

/**
 *
 * @author vpc
 */
public class HWProjectSource {

    public String path;
    public File file;
    public HWProject project;

    public static HWProjectSource of(String path, HWProject project, File file, HadruwavesStudio studio){
        if(file.isFile()){
            return new HWProjectSourceFile(path, project, file);
        }
        return new HWProjectSource(path, project, file);
    }

    public HWProjectSource(String path, HWProject project,File file) {
        this.path = path;
        this.project = project;
        this.file = file;
    }

    public String path() {
        return path;
    }

    public File file() {
        return file;
    }

    public HWProject project() {
        return project;
    }

}
