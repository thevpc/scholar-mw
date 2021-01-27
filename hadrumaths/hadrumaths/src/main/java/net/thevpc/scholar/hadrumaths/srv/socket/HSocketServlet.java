package net.thevpc.scholar.hadrumaths.srv.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface HSocketServlet {
    String getId();

    void service(DataInputStream in, DataOutputStream out) throws IOException;
}
