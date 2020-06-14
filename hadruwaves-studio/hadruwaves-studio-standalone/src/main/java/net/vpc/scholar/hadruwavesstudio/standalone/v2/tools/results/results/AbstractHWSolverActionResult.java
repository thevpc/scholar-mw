/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results;

import java.io.File;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author vpc
 */
public abstract class AbstractHWSolverActionResult implements HWSolverResult {

    protected String actionId;
    protected String id;
    protected String name;
    protected String icon;
    protected String path;
    protected String defaultName;
    protected String defaultPath;
    protected String savedPath;
    protected HWSolverResultLocationType locationType = HWSolverResultLocationType.ACTION;

    public AbstractHWSolverActionResult() {

    }

    public AbstractHWSolverActionResult(String actionId, String id, String name, String path, String defaultName, String defaultPath,String icon) {
        this.id = id;
        this.actionId = actionId;
        this.name = name;
        this.path = path;
        this.defaultName = defaultName;
        this.defaultPath = defaultPath;
        this.icon = icon;
    }

    @Override
    public String path() {
        return path;
    }

    @Override
    public String icon() {
        return icon;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public void setDefaultPath(String defaultPath) {
        this.defaultPath = defaultPath;
    }

    @Override
    public HWSolverResultLocationType locationType() {
        return locationType;
    }

    public void setSavedPath(String savedPath) {
        this.savedPath = savedPath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocationType(HWSolverResultLocationType locationType) {
        this.locationType = locationType;
    }

    @Override
    public boolean isSaved() {
        return savedPath != null;
    }

    @Override
    public boolean deleteFile() {
        if (savedPath != null) {
            if (new File(savedPath).delete()) {
                savedPath = null;
                return true;
            }
        }
        return false;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String actionId() {
        return actionId;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String defaultName() {
        return defaultName;
    }

    @Override
    public String defaultPath() {
        return defaultPath;
    }

    @Override
    public void saveDefault(HWSolverActionContext context) {
        saveFile(context, nativeDefaultPath(context, defaultName));
    }

    @Override
    public void saveCustom(HWSolverActionContext context, String path) {
        saveFile(context, nativeCustomPath(context, path));
    }

    @Override
    public void loadDefault(HWSolverActionContext context) {
        loadFile(context, nativeDefaultPath(context, defaultName));
    }

    @Override
    public void loadCustom(HWSolverActionContext context, String path) {
        loadFile(context, nativeCustomPath(context, path));
    }

    @Override
    public String saveFile(HWSolverActionContext context, String path) {
        File f = new File(path);
        File p = f.getParentFile();
        String n = f.getName();
        String extension = extension();
        if (!n.endsWith(extension)) {
            f = new File(p, n + extension);
        }
        saveFileImpl(context, f.getPath());
        if (savedPath == null) {
            savedPath = f.getPath();
        }
        return f.getPath();
    }

    protected abstract void saveFileImpl(HWSolverActionContext context, String path);

    protected String extension() {
        return "." + id + ".hwr";
    }

    private String prefix(HWSolverActionContext context) {
        context.project().requirePersistent();
        String projectFilePath = context.project().filePath().get();
        switch (locationType) {
            case ACTION: {
                return new File(projectFilePath, "results/default").getPath();
            }
            case SAVED: {
                return new File(projectFilePath, "results/saved").getPath();
            }
            default:{
                return "";
            }
        }
    }

    private String nativeDefaultPath(HWSolverActionContext context, String name) {
        context.project().requirePersistent();
        String projectFilePath = context.project().filePath().get();
        if (!StringUtils.isEmpty(name)) {
            name = defaultName;
        }
        return new File(projectFilePath, "results/default/" + defaultPath + "/" + defaultName + extension()).getPath();
    }

    private String nativeCustomPath(HWSolverActionContext context, String path) {
        context.project().requirePersistent();
        String projectFilePath = context.project().filePath().get();
        if (path.endsWith("/")) {
            path += "result";
        }
        if (!path.endsWith(extension())) {
            path += extension();
        }
        return new File(projectFilePath, "results/default/" + defaultPath + "/" + path).getPath();
    }

    @Override
    public void loadFile(HWSolverActionContext context, String path) {
        loadFileImpl(context, path);
        String prefix=prefix(context);
        if(path.startsWith(prefix)){
            String p=path.substring(prefix.length());
            this.name=new File(p).getName();
            this.path=new File(p).getParentFile().getPath();
        }else{
            this.name=new File(path).getName();
            this.path=new File(path).getParentFile().getPath();
        }
        savedPath = path;
        if (id == null) {
            throw new IllegalArgumentException("Missing resultId");
        }
        if (actionId == null) {
            throw new IllegalArgumentException("Missing actionId");
        }
        if (defaultName == null) {
            throw new IllegalArgumentException("Missing defaultName");
        }
        if (defaultPath == null) {
            throw new IllegalArgumentException("Missing defaultPath");
        }
    }

    protected abstract void loadFileImpl(HWSolverActionContext context, String path);

}
