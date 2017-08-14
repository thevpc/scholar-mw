package net.vpc.scholar.hadrumaths.srv;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.rmi.Remote;

public interface HadrumathsServlet {
    String getId();
    void service(DataInputStream in, DataOutputStream out) throws IOException;
    Remote export();
}
