/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results;

/**
 *
 * @author vpc
 */
public interface HWSolverResult {

    HWSolverResultLocationType locationType();

    boolean isSaved();

    String actionId();

    String id();

    String icon();
    
    String defaultName();

    String defaultPath();

    void saveDefault(HWSolverActionContext context);

    void saveCustom(HWSolverActionContext context, String path);

    String saveFile(HWSolverActionContext context, String path);

    void loadDefault(HWSolverActionContext context);

    void loadCustom(HWSolverActionContext context, String path);

    void loadFile(HWSolverActionContext context, String path);

    void show(HWSolverActionContext context);

    String name();

    String path();

    boolean deleteFile();
}
