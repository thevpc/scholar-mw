package net.vpc.scholar.hadrumaths.util;

import net.vpc.scholar.hadrumaths.RuntimeIOException;

import java.io.*;

public class FolderHFileSystem extends AbstractHFileSystem {
    private File root;

    public FolderHFileSystem(File root) {
        this.root = root;
    }

    private File resolveFile(HFile path) {
        return resolveFile(path.getPath());
    }

    private File resolveFile(String path) {
        return new File(root, path);
    }

    @Override
    public boolean exists(HFile file) {
        return resolveFile(file).exists();
    }

    @Override
    public boolean isDirectory(HFile file) {
        return resolveFile(file).isDirectory();
    }

    @Override
    public boolean isFile(HFile file) {
        return resolveFile(file).isFile();
    }

    @Override
    public InputStream getInputStream(HFile file) {
        try {
            return new FileInputStream(resolveFile(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeIOException(e);
        }
    }

    @Override
    public Reader getReader(HFile file) {
        try {
            return new FileReader(resolveFile(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeIOException(e);
        }
    }


    @Override
    public OutputStream getOutputStream(HFile file, boolean append) {
        try {
            if (append) {
                File file1 = resolveFile(file);
                return new FileOutputStream(file1, true);
            } else {
                File file1 = resolveFile(file);
                File temp = new File(file1.getPath() + WRITE_TEMP_EXT);
                return new FileOutputStream(temp, false) {
                    boolean closed = false;

                    @Override
                    public void close() throws IOException {
                        if (!closed) {
                            super.close();
                            closed = true;
                            if (!temp.renameTo(file1)) {
                                throw new IOException("Unable to rename " + temp.getPath() + " to " + file1.getName());
                            }
                        }
                    }
                };
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeIOException(e);
        }
    }

    @Override
    public boolean mkdirs(HFile path) {
        File file = resolveFile(path);
        boolean x = file.mkdirs();
        if (!x && (!file.isDirectory() || !file.exists())) {
            throw new RuntimeIOException("Unable to mkdir " + path.getPath()+" as "+file.getPath());
        }
        return false;
    }

    @Override
    public long length(HFile file) {
        return resolveFile(file).length();
    }

    @Override
    public AppLock lock(HFile file) {
        return FileSystemLock.createFileLockCompanion(resolveFile(file));
    }

    @Override
    public HFile[] listFiles(HFile file) {
        File file1 = resolveFile(file);
        File[] files = file1.listFiles();
        HFile[] hfiles = new HFile[files == null ? 0 : files.length];
        for (int i = 0; i < hfiles.length; i++) {
            hfiles[i] = new HFile(file, files[i].getName());
        }
        return hfiles;
    }

    @Override
    public HFile[] listFiles(HFile file, HFileFilter fileFilter) {
        File file1 = resolveFile(file);
        File[] files = file1.listFiles(fileFilter == null ? null : new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return fileFilter.accept(new HFile(file, pathname.getName()));
            }
        });
        HFile[] hfiles = new HFile[files == null ? 0 : files.length];
        for (int i = 0; i < hfiles.length; i++) {
            hfiles[i] = new HFile(file, files[i].getName());
        }
        return hfiles;
    }

    @Override
    public boolean delete(HFile file, FailStrategy strategy) {
        File d = resolveFile(file);
        if (d.delete()) {
            return true;
        }
        switch (strategy) {
            case FAIL_SAFE:
                return false;
        }
        throw new RuntimeIOException("Unable to delete " + file);
    }

    @Override
    public boolean isNativeLocalFile(HFile file) {
        return true;
    }

    @Override
    public String getNativeLocalFile(HFile file) {
        return resolveFile(file).getPath();
    }
}
