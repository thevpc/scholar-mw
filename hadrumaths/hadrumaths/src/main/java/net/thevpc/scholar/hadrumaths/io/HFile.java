package net.thevpc.scholar.hadrumaths.io;

import net.thevpc.scholar.hadrumaths.concurrent.AppLock;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HFile {
    private final String path;
    private final HFileSystem fs;

    public HFile(HFileSystem fs, String path) {
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("Invalid path " + path);
        }
        this.fs = fs;
        this.path = validatePath(path);
    }

    protected String validatePath(String path) {
        List<String> s = new ArrayList<String>(Arrays.asList(path.split("/")));
        for (int i = s.size() - 1; i >= 0; i--) {
            if (s.get(i).equals(".")) {
                s.remove(i);
            } else if (s.get(i).equals("..")) {
                s.remove(i);
                if (i > 0) {
                    s.remove(i - 1);
                    i--;
                }
            }
        }
        if (s.isEmpty()) {
            return "/";
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.size(); i++) {
                sb.append("/").append(s.get(i));
            }
            return sb.toString();
        }
    }

    public HFile(HFile parent, String path) {
        String spath = validatePath(path);
        if (spath.equals("/")) {
            throw new IllegalArgumentException("Invalid path " + path);
        }
        String ppath = parent.getPath();
        if (ppath.equals("/")) {
            spath = spath.substring(1);
        }
        this.path = ppath + spath;
        this.fs = parent.getFs();
    }

    public String getPath() {
        return path;
    }

    public HFileSystem getFs() {
        return fs;
    }

    public String getLongName() {
        String n = getName();
        int i = n.lastIndexOf('.');
        if (i >= 0) {
            return n.substring(0, i);
        }
        return n;
    }

    public String getName() {
        if (path.equals("/")) {
            return "/";
        }
        int i = path.lastIndexOf("/");
        return path.substring(i + 1);
    }

    public String getSimpleName() {
        String n = getName();
        int i = n.indexOf('.');
        if (i >= 0) {
            return n.substring(0, i);
        }
        return n;
    }

    public String getExt() {
        String n = getName();
        int i = n.lastIndexOf('.');
        if (i >= 0) {
            return n.substring(i + 1);
        }
        return n;
    }

    public String getLongExt() {
        String n = getName();
        int i = n.indexOf('.');
        if (i >= 0) {
            return n.substring(i + 1);
        }
        return n;
    }

    public boolean exists() {
        return fs.exists(this);
    }

    public boolean existsOrWait() {
        return fs.existsOrWait(this);
    }

    public boolean exists(int timeout) {
        return fs.existsOrWait(this, timeout);
    }

    public Reader getReader() {
        return fs.getReader(this);
    }

    public byte[] loadBytes() {
        return fs.loadBytes(this);
    }

    public InputStream getInputStream() {
        return fs.getInputStream(this);
    }

    public OutputStream getOutputStream() {
        return fs.getOutputStream(this);
    }

    public OutputStream getOutputStream(boolean append) {
        return fs.getOutputStream(this, append);
    }

    public long length() {
        return fs.length(this);
    }

    public boolean mkdirParents() {
        HFile p = getParent();
        if (p != null) {
            return fs.mkdirs(p);
        }
        return false;
    }

    public HFile getParent() {
        if (path.equals("/")) {
            return null;
        }
        int i = path.lastIndexOf("/");
        return new HFile(fs, path.substring(0, i));
    }

    public boolean mkdirs() {
        return fs.mkdirs(this);
    }

    @Override
    public String toString() {
        return String.valueOf(path);
    }

    public AppLock lock() {
        return fs.lock(this);
    }

    public boolean isFile() {
        return fs.isFile(this);
    }

    public boolean isDirectory() {
        return fs.isDirectory(this);
    }

    public HFile[] listFiles() {
        return fs.listFiles(this);
    }

    public HFile[] listFiles(HFileFilter filter) {
        return fs.listFiles(this, filter);
    }

    public boolean delete(FailStrategy strategy) {
        return fs.delete(this, strategy);
    }

    public boolean deleteFolderTree(HFileFilter fileFilter, FailStrategy strategy) {
        return fs.deleteFolderTree(this, fileFilter, strategy);
    }

    public boolean isNativeLocalFile() {
        return fs.isNativeLocalFile(this);
    }

    public String getNativeLocalFile() {
        return fs.getNativeLocalFile(this);
    }
}
