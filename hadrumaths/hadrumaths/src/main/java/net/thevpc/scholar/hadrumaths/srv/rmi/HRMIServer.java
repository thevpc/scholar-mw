package net.thevpc.scholar.hadrumaths.srv.rmi;

import net.thevpc.scholar.hadrumaths.srv.FSServlet;
import net.thevpc.scholar.hadrumaths.srv.HadrumathsAbstractServer;
import net.thevpc.scholar.hadrumaths.srv.HadrumathsServices;
import net.thevpc.scholar.hadrumaths.srv.HadrumathsServlet;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

public class HRMIServer extends HadrumathsAbstractServer {
    private final Map<String, HRMIServlet> servlets = new HashMap<>();
    private int port = HadrumathsServices.DEFAULT_RMI_PORT;
    private boolean started;

    public HRMIServer() {
        register(new FSServlet("CacheFS"));
    }


    private void register(HadrumathsServlet servlet) {
        checkNotStarted();
        String id = servlet.getId();
        if (servlets.containsKey(id)) {
            throw new IllegalArgumentException("Already registered " + id + " to " + servlets.get(id));
        }
        if (servlet instanceof FSServlet) {
            try {
                servlets.put(id, new FSRemoteImpl((FSServlet) servlet));
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        } else {
            throw new IllegalArgumentException("Unsupported " + servlet);
        }
    }

    protected void checkNotStarted() {
        if (started) {
            throw new IllegalArgumentException("Already started");
        }
    }    public int getPort() {
        return port;
    }

    protected HRMIServlet getServlet(int id) {
        HRMIServlet s = servlets.get(id);
        if (s == null) {
            throw new IllegalArgumentException("Not Found");
        }
        return s;
    }    public void setPort(int port) {
        checkNotStarted();
        if (port < 0) {
            port = HadrumathsServices.DEFAULT_SOCKET_PORT;
        }
        this.port = port;
    }

    public boolean isStarted() {
        return started;
    }



    public void start() {
        checkNotStarted();
        try {
            Registry registry = LocateRegistry.createRegistry(getPort());
            for (Map.Entry<String, HRMIServlet> ss : servlets.entrySet()) {
                registry.bind(ss.getKey(), ss.getValue());
            }
            started = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    protected void process(Socket socket) throws IOException {
//        DataInputStream in = new DataInputStream(socket.getInputStream());
//        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//        while (true) {
//            int id = in.readInt();
//            if (id == HadrumathsServices.QUIT) {
//                close();
//                break;
//            }
//            HadrumathsServlet servlet = getServlet(id);
////            System.out.println("Received CMD "+id+" => "+servlet);
//            servlet.service(in, out);
//            out.flush();
//        }
//    }

    public void close() {
        if (isStarted()) {
            stop();
        }
    }



    public void stop() {
    }
}
