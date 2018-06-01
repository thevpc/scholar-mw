package net.vpc.scholar.hadrumaths.srv;

import net.vpc.scholar.hadrumaths.srv.rmi.HRMIServer;
import net.vpc.scholar.hadrumaths.srv.socket.HSocketServer;

public class TestSrv {
    public static void main(String[] args) {
        new HRMIServer().start();
        new HSocketServer().start();
    }


}
