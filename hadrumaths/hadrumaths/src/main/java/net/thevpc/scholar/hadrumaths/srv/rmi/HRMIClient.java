package net.thevpc.scholar.hadrumaths.srv.rmi;

import net.thevpc.common.util.Chronometer;
import net.thevpc.scholar.hadrumaths.srv.FileStat;
import net.thevpc.scholar.hadrumaths.srv.HadrumathsClient;
import net.thevpc.scholar.hadrumaths.srv.HadrumathsServices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.List;

public class HRMIClient implements HadrumathsClient {
    private final String fsId;
    private final String address;
    private final int port;
    private final FSRemote remote;

    public HRMIClient(String fsId, String address, int port) throws RemoteException, NotBoundException {
        this.fsId = fsId;
        this.address = address;
        if (port < 0) {
            port = HadrumathsServices.DEFAULT_RMI_PORT;
        }
        this.port = port;
        remote = (FSRemote) LocateRegistry.getRegistry(address, port).lookup(fsId);
    }

    @Override
    public List<String> list(String path) {
        try {
            return remote.list(path);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileStat stat(String path) {
        try {
            return remote.stat(path);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void get(String path, File writeTo) {
        try {
            FSInputStream in = remote.get(path);
            FileOutputStream fos = new FileOutputStream(writeTo);
            while (true) {
                byte[] rr = in.read(4096);
                if (rr == null || rr.length == 0) {
                    break;
                }
                fos.write(rr, 0, rr.length);
            }
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long ping() {
        long ret = -1;
        Chronometer c = Chronometer.start();
        try {
            remote.ping();
            ret = c.stop().getTime();
        } catch (IOException e) {
            //
        }
        return ret;
    }

    @Override
    public void close() {

    }
}
