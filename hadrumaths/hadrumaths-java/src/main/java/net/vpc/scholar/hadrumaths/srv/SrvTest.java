package net.vpc.scholar.hadrumaths.srv;

public class SrvTest {
    public static void main(String[] args) {
        new HadrumathsRMIServer().start();
        new HSocketServer().start();
    }


}
