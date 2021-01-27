package net.thevpc.scholar.hadrumaths.srv.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FSInputStream extends Remote {
    byte[] read(int max) throws RemoteException;
}
