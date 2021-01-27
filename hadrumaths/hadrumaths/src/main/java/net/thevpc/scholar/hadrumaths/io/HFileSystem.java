package net.thevpc.scholar.hadrumaths.io;

import net.thevpc.scholar.hadrumaths.concurrent.AppLock;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

public interface HFileSystem {
    HFile get(String path);

    boolean exists(HFile file);

    boolean isDirectory(HFile file);

    boolean isFile(HFile file);

    boolean existsOrWait(HFile file);

    boolean existsOrWait(HFile file, int secondsTimeout);

    byte[] loadBytes(HFile file);

    InputStream getInputStream(HFile file);

    OutputStream getOutputStream(HFile file);

    OutputStream getOutputStream(HFile file, boolean append);

    long write(HFile file, InputStream is);

    boolean mkdirs(HFile file);

    Reader getReader(HFile file);

    long length(HFile file);

    AppLock lock(HFile file);

    HFile[] listFiles(HFile file);

    HFile[] listFiles(HFile file, HFileFilter fileFilter);

    boolean delete(HFile file, FailStrategy strategy);

    boolean deleteFolderTree(HFile folder, HFileFilter fileFilter, FailStrategy strategy);

    boolean isNativeLocalFile(HFile file);

    String getNativeLocalFile(HFile file);
}
