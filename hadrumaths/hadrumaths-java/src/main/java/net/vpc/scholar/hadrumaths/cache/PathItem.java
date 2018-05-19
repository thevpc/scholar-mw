package net.vpc.scholar.hadrumaths.cache;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by vpc on 5/30/14.
 */
public class PathItem {
    private String name;
    private boolean folder;
    private PathItem parent;
    private LinkedHashMap<String, PathItem> children;

    public PathItem() {

    }

    public PathItem(String name, boolean folder, PathItem parent) {
        this.name = name;
        this.folder = folder;
        this.parent = parent;
        checkName(name);
        if (parent == null) {
            throw new IllegalArgumentException("Invalid parent null");
        }
    }

    public String getName() {
        return name;
    }

    public boolean isFolder() {
        return folder;
    }

    public PathItem getParent() {
        return parent;
    }

    public PathItem[] get() {
        return children == null ? new PathItem[0] : children.values().toArray(new PathItem[children.size()]);
    }

    public PathItem get(String name) {
        return children == null ? null : children.get(name);
    }

    private String[] splitPath(String path) {
        StringTokenizer st = new StringTokenizer(path, "/\\");
        List<String> all = new ArrayList<String>();
        while (st.hasMoreElements()) {
            String n = st.nextToken();
            all.add(n);
        }
        return all.toArray(new String[all.size()]);
    }

    public PathItem getPath(String path) {
        String[] all = splitPath(path);
        PathItem cur = this;
        for (int i = 0; i < all.length; i++) {
            String pi = all[i];
            if (pi.equals(".")) {
                //do nothing;
            } else if (pi.equals("..")) {
                if (parent == null) {
                    throw new IllegalArgumentException("Invalid path " + getPath() + "/" + name);
                }
                cur = cur.getParent();
            } else {
                cur = cur.get(all[i]);
                if (cur == null) {
                    return null;
                }
            }
        }
        return cur;
    }

    public PathItem addPath(String name, boolean folder, boolean ignoreExisting, boolean mkdirs) {
        StringTokenizer st = new StringTokenizer(name, "/\\");
        String[] all = splitPath(name);
        for (String n : all) {
            checkName(n);
        }
        if (all.length == 0) {
            throw new IllegalArgumentException("Invalid path");
        }
        PathItem cur = this;
        for (int i = 0; i < all.length - 1; i++) {
            String pi = all[i];
            if (pi.equals(".")) {
                //do nothing;
            } else if (pi.equals("..")) {
                if (parent == null) {
                    throw new IllegalArgumentException("Invalid path " + getPath() + "/" + name);
                }
                cur = cur.getParent();
            } else {
                cur = cur.add(all[i], true, mkdirs);
            }
        }
        return cur.add(all[all.length - 1], folder, ignoreExisting);
    }

    public PathItem add(String name, boolean folder, boolean ignoreExisting) {
        PathItem old = null;
        if (children == null) {
            children = new LinkedHashMap<String, PathItem>();
        } else {
            old = children.get(name);
            if (old != null) {
                if (!ignoreExisting) {
                    throw new RuntimeException("Path already exists " + getPath());
                } else {
                    return old;
                }
            }
        }
        PathItem c = new PathItem(name, folder, this);
        children.put(name, c);
        return c;
    }

    public String getPath() {
        if (parent == null) {
            //root
            return "/";
        }
        return parent.getPath() + "/" + name;
    }

    private static void checkName(String name) {
        if (name == null || name.length() == 0 || name.contains("/") || name.contains("\\")) {
            throw new IllegalArgumentException("Invalid " + name);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PathItem)) return false;

        PathItem pathItem = (PathItem) o;

        if (folder != pathItem.folder) return false;
        if (name != null ? !name.equals(pathItem.name) : pathItem.name != null) return false;
        if (parent != null ? !parent.equals(pathItem.parent) : pathItem.parent != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (folder ? 1 : 0);
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }
}
