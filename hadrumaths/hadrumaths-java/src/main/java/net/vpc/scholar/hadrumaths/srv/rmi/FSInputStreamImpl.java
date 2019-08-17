package net.vpc.scholar.hadrumaths.srv.rmi;

import java.io.UncheckedIOException;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

public class FSInputStreamImpl extends UnicastRemoteObject implements FSInputStream {
    private InputStream in;
    private byte[] buffer;

    public FSInputStreamImpl(InputStream in) throws RemoteException {
        this.in = in;
    }

    public FSInputStreamImpl() throws RemoteException {
    }

    @Override
    public byte[] read(int max) {
        if (max < 0 || max > 100 * 1024 * 1024) {
            max = 10 * 1024 * 1024;
        }
        if (buffer == null || buffer.length < max) {
            buffer = new byte[max];
        }
        int count = 0;
        try {
            count = in.read(buffer, 0, max);
            return Arrays.copyOf(buffer, count);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
