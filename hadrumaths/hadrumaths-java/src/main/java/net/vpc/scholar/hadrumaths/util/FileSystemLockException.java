package net.vpc.scholar.hadrumaths.util;

import java.io.File;

/**
 * Created by vpc on 11/13/16.
 */
public class FileSystemLockException extends RuntimeException {
    private File file;
    public FileSystemLockException(String message,File file) {
        super(message);
        this.file=file;
    }

    public File getFile() {
        return file;
    }
}
