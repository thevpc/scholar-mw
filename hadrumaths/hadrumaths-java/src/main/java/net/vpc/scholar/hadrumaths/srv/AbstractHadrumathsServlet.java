package net.vpc.scholar.hadrumaths.srv;

import java.io.DataOutputStream;
import java.io.IOException;

public abstract class AbstractHadrumathsServlet implements HadrumathsServlet {
    private String id;

    public AbstractHadrumathsServlet(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
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
