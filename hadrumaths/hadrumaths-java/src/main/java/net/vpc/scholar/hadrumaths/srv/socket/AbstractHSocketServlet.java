package net.vpc.scholar.hadrumaths.srv.socket;

import net.vpc.scholar.hadrumaths.srv.HadrumathsServices;
import net.vpc.scholar.hadrumaths.srv.HadrumathsServlet;

import java.io.DataOutputStream;
import java.io.IOException;

public abstract class AbstractHSocketServlet implements HSocketServlet {
    private final HadrumathsServlet baseServlet;

    public AbstractHSocketServlet(HadrumathsServlet baseServlet) {
        this.baseServlet = baseServlet;
    }

    @Override
    public String getId() {
        return baseServlet.getId();
    }

    public HadrumathsServlet getBaseServlet() {
        return baseServlet;
    }

    protected void success(DataOutputStream out) throws IOException {
        out.writeByte(HadrumathsServices.MESSAGE_SUCCESS);
    }

    protected void error(String error, DataOutputStream out) throws IOException {
        out.writeByte(HadrumathsServices.MESSAGE_ERROR);
        if (error == null) {
            error = "";
        }
        out.writeUTF(error);
    }
}
