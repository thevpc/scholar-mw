package net.thevpc.scholar.hadrumaths.srv;

import net.thevpc.scholar.hadrumaths.srv.rmi.HRMIServer;
import net.thevpc.scholar.hadrumaths.srv.socket.HSocketServer;

public class TestSrv {
    public static void main(String[] args) {
        new HRMIServer().start();
        new HSocketServer().start();
    }


}
