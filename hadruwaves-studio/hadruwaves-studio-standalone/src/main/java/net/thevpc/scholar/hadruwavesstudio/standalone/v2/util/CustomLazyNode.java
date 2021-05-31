/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.util;

import java.util.ArrayList;
import net.thevpc.common.props.Path;

/**
 *
 * @author vpc
 */
public class CustomLazyNode {
    
    private String path;
    private String type;
    private String name;
    private Object value;
    private java.util.List<CustomLazyNode> list = new ArrayList<>();
    private boolean fodler;

    public CustomLazyNode(String path, String name, String type, Object value, boolean fodler) {
        this.type = type;
        this.path = path;
        this.name = name;
        this.value = value;
        this.fodler = fodler;
    }

    public CustomLazyNode find(String n) {
        for (CustomLazyNode r : getList()) {
            if (r.getPath().equals(Path.of(this.getPath(), n).toString())) {
                return r;
            }
        }
        return null;
    }

    public void add(CustomLazyNode n) {
        getList().add(n);
    }

    public void add(String path, String name, String type, Object value, boolean folder) {
        Path ipath = Path.of(path);
        if (ipath.isEmpty()) {
            throw new IllegalArgumentException("Error");
        } else if (ipath.size() == 1) {
            getList().add(new CustomLazyNode(Path.of(this.getPath()).append(ipath.first()).toString(), name, type, value, folder));
        } else {
            CustomLazyNode c = find(ipath.first());
            if (c == null) {
                c = new CustomLazyNode(Path.of(this.getPath() + "/" + ipath.first()).toString(), ipath.first(), "folder", null, true);
                getList().add(c);
            }
            c.add(ipath.skipFirst().toString(), name, type, value, folder);
        }
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * @return the list
     */
    public java.util.List<CustomLazyNode> getList() {
        return list;
    }

    /**
     * @return the fodler
     */
    public boolean isFodler() {
        return fodler;
    }
    
}
