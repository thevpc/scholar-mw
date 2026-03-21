package test;

import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.srv.FileStat;
import net.thevpc.scholar.hadrumaths.srv.HadrumathsClient;
import net.thevpc.scholar.hadrumaths.srv.rmi.HRMIClient;
import net.thevpc.scholar.hadrumaths.srv.socket.HSocketClient;

public abstract class TestCli {

    public static void main(String[] args) {

        try {
            HSocketClient cli1 = new HSocketClient("CacheFS", "localhost", -1);
            HadrumathsClient cli2 = new HRMIClient("CacheFS", "localhost", -1);
            for (HadrumathsClient cli : new HadrumathsClient[]{cli1, cli2}) {
                Maths.chrono(cli.getClass().getSimpleName(), new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 5; i < 50; i++) {
                            for (String s : cli.list("/")) {
                                FileStat stat = cli.stat(s);
                            }
                        }
                    }
                });
                cli.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
