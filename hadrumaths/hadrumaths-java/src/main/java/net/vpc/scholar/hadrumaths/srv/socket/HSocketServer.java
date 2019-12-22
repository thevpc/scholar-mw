package net.vpc.scholar.hadrumaths.srv.socket;

import net.vpc.scholar.hadrumaths.srv.FSServlet;
import net.vpc.scholar.hadrumaths.srv.HadrumathsAbstractServer;
import net.vpc.scholar.hadrumaths.srv.HadrumathsServices;
import net.vpc.scholar.hadrumaths.srv.HadrumathsServlet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class HSocketServer extends HadrumathsAbstractServer {

    private Map<String, HSocketServlet> servlets = new HashMap<>();
    private int port = HadrumathsServices.DEFAULT_SOCKET_PORT;
    private boolean started;
    private ServerSocket serverSocket;

    public HSocketServer() {
        register(new FSServlet("CacheFS"));
    }


    private void register(HadrumathsServlet servlet) {
        String id = servlet.getId();
        if (servlets.containsKey(id)) {
            throw new IllegalArgumentException("Already registered " + id + " to " + servlets.get(id));
        }
        if(servlet instanceof FSServlet){
            servlets.put(id, new FSSocketServlet((FSServlet) servlet));
        }else{
            throw new IllegalArgumentException("Unsupported "+servlet.getClass().getName());
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        checkNotStarted();
        if (port < 0) {
            port = HadrumathsServices.DEFAULT_SOCKET_PORT;
        }
        this.port = port;
    }

    public boolean isStarted() {
        return started;
    }

    protected void checkNotStarted() {
        if (started) {
            throw new IllegalArgumentException("Already started");
        }
    }

    public void start() {
        checkNotStarted();
        try {
            serverSocket = new ServerSocket(port);
            started = true;
            while (true) {
                Socket accept = serverSocket.accept();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            process(accept);
                        } catch (EOFException e) {
                            //disconnected
                        } catch (SocketException e) {
                            //disconnected
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void process(Socket socket) throws IOException {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        while (true) {
            String id = in.readUTF();
            if ("quit".equals(id)) {
                close();
                break;
            }
            HSocketServlet servlet = getServlet(id);
//            System.out.println("Received CMD "+id+" => "+servlet);
            servlet.service(in, out);
            out.flush();
        }
    }

    public void close() {
        if (isStarted()) {
            stop();
        }
    }

    protected HSocketServlet getServlet(String id) {
        HSocketServlet s = servlets.get(id);
        if (s == null) {
            throw new IllegalArgumentException("Not Found");
        }
        return s;
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
