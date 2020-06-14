package net.vpc.scholar.hadrumaths.srv.socket;

import net.vpc.scholar.hadrumaths.srv.HadrumathsServices;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class HSocketConnection {
    private Socket socket;
    private final InetAddress address;
    private final int port;
    private DataInputStream in;
    private DataOutputStream out;

    public HSocketConnection(InetAddress address, int port) {
        this.address = address;
        if (port < 0) {
            port = HadrumathsServices.DEFAULT_SOCKET_PORT;
        }
        this.port = port;
    }

    public void reconnectIfNecessary() throws UncheckedIOException {
        if (!isConnected()) {
            reconnect();
        }
    }

    public boolean isConnected() {
        if (socket == null) {
            return false;
        }
        return socket.isConnected();
    }

    public void reconnect() throws UncheckedIOException {
        close();
        connect();
    }

    public void close() throws UncheckedIOException {
        try {
            if (socket != null) {
                if (socket.isConnected()) {
                    socket.close();
                }
            }
            socket = null;
        } catch (Exception ex) {
            //
        }
        try {
            if (in != null) {
                in.close();
            }
            in = null;
        } catch (Exception ex) {
            //
        }
        try {
            if (out != null) {
                out.close();
            }
            out = null;
        } catch (Exception ex) {
            //
        }
    }

    public void connect() throws UncheckedIOException {
        try {
            socket = new Socket(address, port);
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void invalidate() {
        try {
            close();
        } catch (Exception e) {
            //
        }
    }

    public DataOutputStream out() throws UncheckedIOException {
        if (in == null) {
            throw new UncheckedIOException(new IOException("Not Connected"));
        }
        return out;
    }

    public void consumeResponseHeader() throws UncheckedIOException {
        byte b = 0;
        try {
            b = in().readByte();
            if (b == HadrumathsServices.MESSAGE_ERROR) {
                throw new IllegalArgumentException(in().readUTF());
            } else if (b == HadrumathsServices.MESSAGE_SUCCESS) {
                return;
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public DataInputStream in() throws UncheckedIOException {
        if (in == null) {
            throw new UncheckedIOException(new IOException("Not Connected"));
        }
        return in;
    }


}
