package net.thevpc.scholar.hadrumaths.srv.rmi;

import net.thevpc.scholar.hadrumaths.srv.FileStat;

import java.rmi.RemoteException;
import java.util.List;

public interface FSRemote extends HRMIServlet {
    FileStat stat(String path) throws RemoteException;

    List<String> list(String path) throws RemoteException;

    FSInputStream get(String path) throws RemoteException;

    void ping() throws RemoteException;
}
