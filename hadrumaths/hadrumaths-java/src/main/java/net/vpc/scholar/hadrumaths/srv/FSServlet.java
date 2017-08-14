package net.vpc.scholar.hadrumaths.srv;

import net.vpc.scholar.hadrumaths.RuntimeIOException;
import net.vpc.scholar.hadrumaths.util.HFile;
import net.vpc.scholar.hadrumaths.util.HadrumathsFileSystem;
import net.vpc.scholar.hadrumaths.util.IOUtils;

import java.io.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class FSServlet extends AbstractHadrumathsServlet {

    public FSServlet(String id) {
        super(id);
    }

    protected abstract HadrumathsFileSystem getFileSystem();

    @Override
    public void service(DataInputStream in, DataOutputStream out) throws IOException {
        FSConstants.Command call = FSConstants.Command.values()[in.readByte()];
//        System.out.println("CALL "+call);
        switch (call) {
            case STAT: {
                String path = in.readUTF();
                HFile file = getFileSystem().get(path);
                success(out);
                if (file == null) {
                    out.writeByte(FSConstants.TYPE_NOT_FOUND);
                } else {
                    if (file.isFile()) {
                        out.writeByte(FSConstants.TYPE_FILE);
                        out.writeLong(file.length());
                    } else if (file.isDirectory()) {
                        out.writeByte(FSConstants.TYPE_FOLDER);
                        HFile[] files = file.listFiles();
                        out.writeInt(files == null ? 0 : files.length);
                    } else {
                        out.writeByte(FSConstants.TYPE_OTHER);
                    }
                }
                break;
            }
            case GET: {
                String path = in.readUTF();
                HFile file = getFileSystem().get(path);
                if (file == null) {
                    error("File not found", out);
                } else {
                    if (file.isFile()) {
                        success(out);
                        out.writeLong(file.length());
                        try(InputStream is=file.getInputStream()) {
                            IOUtils.copy(is, out, 1024 * 1024);
                        }
                    } else {
                        error("Not a folder", out);
                    }
                }
                break;
            }
            case LIST: {
                String path = in.readUTF();
//                System.out.println("path "+path);
                HFile file = getFileSystem().get(path);
                if (file == null) {
                    error("invalid path : " + path, out);
                } else {
                    if (!file.exists()) {
                        error("invalid path : " + path, out);
                    } else if (file.isDirectory()) {
                        success(out);
                        HFile[] files = file.listFiles();
                        int v = files == null ? 0 : files.length;
//                        System.out.println(v+" files");
                        out.writeInt(v);
                        if (files != null) {
                            for (HFile ff : files) {
//                                System.out.println("\t "+ff.getName());
                                out.writeUTF(ff.getPath());
                            }
                        }
                    } else {
                        error("Not a folder : " + path, out);
                    }
                }
                break;
            }
            default: {
                error("Invalid Command " + call, out);
                break;
            }
        }
    }

    @Override
    public Remote export() {
        try {
            return new FSRemoteImpl();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public interface FSInputStream extends Remote {
        byte[] read(int max) throws RemoteException;
    }

    public interface FSRemote extends Remote {
        FileStat stat(String path) throws RemoteException;

        List<String> list(String path) throws RemoteException;

        FSInputStream get(String path) throws RemoteException;
    }

    public class FSRemoteImpl extends UnicastRemoteObject implements FSRemote {
        public FSRemoteImpl() throws RemoteException {
        }

        @Override
        public FileStat stat(String path) throws RemoteException {
            HFile file = getFileSystem().get(path);
            FileStat fileStat = new FileStat();
            if (file == null) {
                fileStat.type = (FSConstants.TYPE_NOT_FOUND);
            } else {
                if (file.isFile()) {
                    fileStat.type = (FSConstants.TYPE_FILE);
                    fileStat.length = (file.length());
                } else if (file.isDirectory()) {
                    fileStat.type = (FSConstants.TYPE_FOLDER);
                    HFile[] files = file.listFiles();
                    fileStat.length = (files == null ? 0 : files.length);
                } else {
                    fileStat.type = (FSConstants.TYPE_OTHER);
                }
            }
            return fileStat;
        }

        @Override
        public List<String> list(String path) throws RemoteException {
            HFile file = getFileSystem().get(path);
            if (file == null) {
                throw new IllegalArgumentException("invalid path : " + path);
            } else {
                if (!file.exists()) {
                    throw new IllegalArgumentException("invalid path : " + path);
                } else if (file.isDirectory()) {
                    HFile[] files = file.listFiles();
                    List<String> all = new ArrayList<>(files.length);
                    for (HFile ff : files) {
                        all.add(ff.getPath());
                    }
                    return all;
                } else {
                    throw new IllegalArgumentException("Not a folder : " + path);
                }
            }
        }

        @Override
        public FSInputStream get(String path) throws RemoteException {
            HFile file = getFileSystem().get(path);
            if (file == null) {
                throw new IllegalArgumentException("invalid path : " + path);
            } else if (!file.exists()) {
                throw new IllegalArgumentException("invalid path : " + path);
            } else if (file.isDirectory()) {
                throw new IllegalArgumentException("Not a file : " + path);
            } else if (!file.isFile()) {
                throw new IllegalArgumentException("Not a file : " + path);
            }
            return new FSInputStreamImpl(file.getInputStream());
        }
    }

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
                throw new RuntimeIOException(e);
            }
        }
    }
}
