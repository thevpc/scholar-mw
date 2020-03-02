package net.vpc.scholar.hadrumaths.srv.socket;

import net.vpc.common.util.Chronometer;
import net.vpc.scholar.hadrumaths.srv.FSConstants;
import net.vpc.scholar.hadrumaths.srv.FileStat;
import net.vpc.scholar.hadrumaths.srv.HadrumathsClient;
import net.vpc.scholar.hadrumaths.srv.HadrumathsServices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class HSocketClient implements HadrumathsClient {
    private final String fsId;
    private final String address;
    private final int port;
    private HSocketConnection connection;

    public HSocketClient(String fsId, String address, int port) throws UnknownHostException {
        this.fsId = fsId;
        if (port < 0) {
            port = HadrumathsServices.DEFAULT_SOCKET_PORT;
        }
        this.port = port;
        this.address = address;
        this.connection = new HSocketConnection(InetAddress.getByName(address), port);
    }

    public HSocketConnection getConnection() {
        return connection;
    }

    public void setConnection(HSocketConnection connection) {
        this.connection = connection;
    }

    public List<String> list(String path) {
        connection.reconnectIfNecessary();
        try {
            connection.out().writeUTF(fsId);
            connection.out().writeByte(FSConstants.Command.LIST.ordinal());
            connection.out().writeUTF(path);
            connection.consumeResponseHeader();
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
            connection.consumeResponseHeader();
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
            connection.consumeResponseHeader();
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

    @Override
    public long ping() {
        long ret = -1;
        Chronometer c = Chronometer.start();
        try {
            connection.reconnectIfNecessary();
            connection.out().writeUTF(fsId);
            connection.out().writeByte(FSConstants.Command.PING.ordinal());
            connection.consumeResponseHeader();
            ret = c.stop().getTime();
        } catch (IOException e) {
            connection.invalidate();
        }
        return ret;
    }

    @Override
    public void close() {
        connection.close();
    }
}
