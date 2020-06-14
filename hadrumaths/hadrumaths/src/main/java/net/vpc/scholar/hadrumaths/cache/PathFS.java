package net.vpc.scholar.hadrumaths.cache;

import java.io.PrintStream;

/**
 * Created by vpc on 5/30/14.
 */
public class PathFS {
    private final PathItem root = new PathItem();

    public PathItem getRoot() {
        return root;
    }

    public boolean contains(String path) {
        return get(path) != null;
    }

    public PathItem get(String path) {
        return root.getPath(path);
    }

    public PathItem mkdirs(String path) {
        return root.addPath(path, true, true, true);
    }

    public PathItem touch(String path) {
        return root.addPath(path, false, true, true);
    }

    public void print(PrintStream out) {
        for (PathItem t : root.get()) {
            print(t, out);
        }
    }

    private void print(PathItem t, PrintStream out) {
        out.print(t.getPath());
        for (PathItem tt : t.get()) {
            print(tt, out);
        }
    }

}
