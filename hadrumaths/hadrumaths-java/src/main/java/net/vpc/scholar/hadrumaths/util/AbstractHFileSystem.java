package net.vpc.scholar.hadrumaths.util;

import net.vpc.scholar.hadrumaths.RuntimeIOException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractHFileSystem implements HFileSystem {
    public static final String WRITE_TEMP_EXT = ".writing";

    public boolean deleteFolderTree(HFile folder, HFileFilter fileFilter, FailStrategy strategy) {
        if (strategy == null) {
            strategy = FailStrategy.FAIL_FAST;
        }
        if (!folder.exists()) {
            if (strategy == FailStrategy.FAIL_SAFE) {
                return false;
            }
            throw new RuntimeIOException("Folder not found " + folder);
        }
        HFile[] files = folder.listFiles(fileFilter);
        boolean ok = true;
        if (files != null) {
            for (HFile file : files) {
                if (file.isFile() && (fileFilter == null || fileFilter.accept(file))) {
                    boolean deleted = false;
                    try {
                        deleted = file.delete(strategy);
                    } catch (Exception ex) {
                        switch (strategy) {
                            case FAIL: {
                                //ignore
                                break;
                            }
                            case FAIL_FAST: {
                                //ignore
                                if (ex instanceof RuntimeException) {
                                    throw (RuntimeException) ex;
                                }
                                throw new RuntimeException(ex);
                            }
                            case FAIL_SAFE: {
                                //ignore
                            }
                        }
                    }
                    if (!deleted) {
                        ok = false;
                    }
                } else if (file.isDirectory()) {
                    ok = deleteFolderTree(file, fileFilter, strategy);
                } else {
                    ok = false;
                }
            }
        }
        if (ok) {
            try {
                if (folder.delete(strategy)) {
                    return true;
                }
                if (strategy == FailStrategy.FAIL_SAFE) {
                    return false;
                }
            } catch (Exception ex) {
                if (ex instanceof RuntimeException) {
                    throw (RuntimeException) ex;
                }
                throw new RuntimeException(ex);
            }
        }
        throw new RuntimeIOException("Unable to Delete Folder " + folder);
    }

    @Override
    public HFile get(String path) {
        return new HFile(this, path);
    }

    @Override
    public boolean existsOrWait(HFile file) {
        return existsOrWait(file, 300);
    }

    @Override
    public OutputStream getOutputStream(HFile file) {
        return getOutputStream(file, false);
    }

    @Override
    public boolean existsOrWait(HFile file, int secondsTimeout) {
        if (exists(file)) {
            return true;
        }
        File file1 = new File(file.getPath() + WRITE_TEMP_EXT);
        if (file1.exists()) {
            for (int i = 0; i < secondsTimeout * 2; i++) {
                if (!file1.exists()) {
                    return exists(file);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            throw new RuntimeIOException("File does not exist but an associated temp file failed to finish writing to " + file);
        }
        return false;
    }

    @Override
    public long write(HFile file, InputStream is) {
        OutputStream out = getOutputStream(file);
        byte[] buffer = new byte[4096 * 4];
        long x = 0;
        try {
            while (true) {
                int count = is.read(buffer);
                if (count <= 0) {
                    break;
                }
                x += count;
                out.write(buffer, 0, count);
            }
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
        return x;
    }

    @Override
    public boolean isNativeLocalFile(HFile file) {
        return false;
    }

    @Override
    public String getNativeLocalFile(HFile file) {
        throw new IllegalArgumentException("Not a Native File " + file);
    }
}
