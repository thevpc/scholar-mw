package net.vpc.scholar.hadrumaths.srv.rmi;

import net.vpc.scholar.hadrumaths.io.HFile;
import net.vpc.scholar.hadrumaths.srv.FSConstants;
import net.vpc.scholar.hadrumaths.srv.FSServlet;
import net.vpc.scholar.hadrumaths.srv.FileStat;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class FSRemoteImpl extends UnicastRemoteObject implements FSRemote {
    private FSServlet fsServlet;

    public FSRemoteImpl(FSServlet fsServlet) throws RemoteException {
        this.fsServlet = fsServlet;
    }

    @Override
    public void ping() {
        //do nothing
    }

    @Override
    public FileStat stat(String path) {
        HFile file = fsServlet.getFileSystem().get(path);
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
    public List<String> list(String path) {
        HFile file = fsServlet.getFileSystem().get(path);
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
        HFile file = fsServlet.getFileSystem().get(path);
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
