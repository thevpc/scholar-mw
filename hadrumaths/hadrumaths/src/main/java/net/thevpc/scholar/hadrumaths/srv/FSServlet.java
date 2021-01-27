package net.thevpc.scholar.hadrumaths.srv;

import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.io.HFileSystem;

public class FSServlet implements HadrumathsServlet {
    private final String id;

    public FSServlet(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public HFileSystem getFileSystem() {
        return Maths.Config.getCacheFileSystem();
    }

}
