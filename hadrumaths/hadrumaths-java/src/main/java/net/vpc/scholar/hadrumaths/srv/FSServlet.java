package net.vpc.scholar.hadrumaths.srv;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.MathsBase;
import net.vpc.scholar.hadrumaths.io.HFileSystem;

public class FSServlet implements HadrumathsServlet {
    private String id;
    public FSServlet(String id) {
        this.id=id;
    }

    @Override
    public String getId() {
        return id;
    }

    public HFileSystem getFileSystem() {
        return MathsBase.Config.getCacheFileSystem();
    }

}
