package net.vpc.scholar.hadrumaths.srv.socket;

import net.vpc.scholar.hadrumaths.RuntimeIOException;
import net.vpc.scholar.hadrumaths.srv.HadrumathsServices;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class HSocketConnection {
    private Socket socket;
    private InetAddress address;
    private int port;
    private DataInputStream in;
    private DataOutputStream out;

    public HSocketConnection(InetAddress address, int port) {
        this.address = address;
        if (port < 0) {
            port = HadrumathsServices.DEFAULT_SOCKET_PORT;
        }
        this.port = port;
    }

    public boolean isConnected() {
        if (socket == null) {
            return false;
        }
        return socket.isConnected();
    }

    public void reconnectIfNecessary() throws RuntimeIOException {
        if (!isConnected()) {
            reconnect();
        }
    }

    public void reconnect() throws RuntimeIOException {
        close();
        connect();
    }

    public void connect() throws RuntimeIOException {
        try {
            socket = new Socket(address, port);
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    public void invalidate() {
        try {
            close();
        } catch (Exception e) {
            //
        }
    }

    public void close() throws RuntimeIOException {
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

    public DataInputStream in() throws RuntimeIOException {
        if (in == null) {
            throw new RuntimeIOException("Not Connected");
        }
        return in;
    }

    public DataOutputStream out() throws RuntimeIOException {
        if (in == null) {
            throw new RuntimeIOException("Not Connected");
        }
        return out;
    }

    public void consumeResponseHeader() throws RuntimeIOException {
        byte b = 0;
        try {
            b = in().readByte();
            if (b == HadrumathsServices.MESSAGE_ERROR) {
                throw new IllegalArgumentException(in().readUTF());
            } else if (b == HadrumathsServices.MESSAGE_SUCCESS) {
                return;
            }
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }


}
