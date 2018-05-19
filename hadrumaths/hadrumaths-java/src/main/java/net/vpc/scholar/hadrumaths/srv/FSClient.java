package net.vpc.scholar.hadrumaths.srv;

import net.vpc.scholar.hadrumaths.Maths;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;

public abstract class FSClient {

    public static void main(String[] args) {
        HadrumathsConnection connection = null;
        try {
            FSClient cli = new FSSocketClient("CacheFS", "localhost", -1);
//            FSClient cli = new FSRMIClient("CacheFS", "localhost", -1);
            Maths.chrono("srv", new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 100; i++) {
                        for (String s : cli.list("/structure.dump/1k/te/5l")) {
                            FileStat stat = cli.stat(s);
                            System.out.println(s + " : " + stat);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract List<String> list(String path);

    public abstract FileStat stat(String path);

    public abstract void get(String path, File writeTo);

    private static class FSRMIClient extends FSClient {
        private String fsId;
        private String address;
        private int port;
        private FSServlet.FSRemote remote;

        public FSRMIClient(String fsId, String address, int port) throws RemoteException, NotBoundException {
            this.fsId = fsId;
            this.address = address;
            if (port < 0) {
                port = HadrumathsServices.DEFAULT_RMI_PORT;
            }
            this.port = port;
            remote = (FSServlet.FSRemote) LocateRegistry.getRegistry(address, port).lookup(fsId);
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
                FSServlet.FSInputStream in = remote.get(path);
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
    }

    private static class FSSocketClient extends FSClient {
        private String fsId;
        private String address;
        private int port;
        private HadrumathsConnection connection;

        public FSSocketClient(String fsId, String address, int port) throws UnknownHostException {
            this.fsId = fsId;
            if (port < 0) {
                port = HadrumathsServices.DEFAULT_SOCKET_PORT;
            }
            this.port = port;
            this.address = address;
            this.connection = new HadrumathsConnection(InetAddress.getByName(address), port);
        }

        public HadrumathsConnection getConnection() {
            return connection;
        }

        public void setConnection(HadrumathsConnection connection) {
            this.connection = connection;
        }


        public List<String> list(String path) {
            connection.reconnectIfNecessary();
            try {
                connection.out().writeUTF(fsId);
                connection.out().writeByte(FSConstants.Command.LIST.ordinal());
                connection.out().writeUTF(path);
                connection.consumeResponse();
                int count = connection.in().readInt();
                if (count < 0) {
                    throw new IllegalArgumentException("Not a valid folder " + path);
                }
//            System.out.println("found "+count);
                List<String> all = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    String e = connection.in().readUTF();
                    System.out.println(e);
                    all.add(e);
                }
                return all;
            } catch (IOException e) {
                connection.invalidate();
                throw new IllegalArgumentException(e);
            }
        }

        public FileStat stat(String path) {
            connection.reconnectIfNecessary();
            try {
                connection.out().writeUTF(fsId);
                connection.out().writeByte(FSConstants.Command.STAT.ordinal());
                connection.out().writeUTF(path);
                connection.consumeResponse();
                FileStat fileStat = new FileStat();
                byte type = connection.in().readByte();
                fileStat.type = type;
                switch (type) {
                    case FSConstants.TYPE_FILE: {
                        fileStat.length = connection.in().readLong();
                        break;
                    }
                    case FSConstants.TYPE_FOLDER: {
                        fileStat.length = connection.in().readInt();
                        break;
                    }
                    case FSConstants.TYPE_NOT_FOUND: {
                        break;
                    }
                    case FSConstants.TYPE_OTHER: {
                        break;
                    }
                }
                return fileStat;
            } catch (IOException e) {
                connection.invalidate();
                throw new IllegalArgumentException(e);
            }
        }

        public void get(String path, File writeTo) {
            connection.reconnectIfNecessary();
            try {
                connection.out().writeUTF(fsId);
                connection.out().writeByte(FSConstants.Command.GET.ordinal());
                connection.out().writeUTF(path);
                connection.consumeResponse();
                long count = connection.in().readLong();
                if (count < 0) {
                    throw new IllegalArgumentException("File not found " + path);
                }
                byte[] buffer = new byte[4096];
                FileOutputStream fos = new FileOutputStream(writeTo);
                while (count > 0) {
                    int len = 0;
                    if (count > buffer.length) {
                        len = buffer.length;
                    } else {
                        len = (int) count;
                    }
                    int read = connection.in().read(buffer, 0, len);
                    if (read > 0) {
                        fos.write(buffer, 0, read);
                        count -= read;
                    } else {
                        throw new IOException("Unexpected");
                    }
                }
                fos.close();
            } catch (IOException e) {
                connection.invalidate();
                throw new IllegalArgumentException(e);
            }
        }

    }


}
