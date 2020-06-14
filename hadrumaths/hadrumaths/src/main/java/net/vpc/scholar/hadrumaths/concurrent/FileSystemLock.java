package net.vpc.scholar.hadrumaths.concurrent;

import java.io.*;

/**
 * Simple File Lock implementation
 * Created by vpc on 11/13/16.
 */
public class FileSystemLock extends AbstractAppLock {
    private final File file;
    //new RandomAccessFile(file,"rw")
    private RandomAccessFile os;

    /**
     * creates a lock descriptor
     *
     * @param file file to lock
     */
    public FileSystemLock(File file) {
        this.file = file;
    }

    public static FileSystemLock createFileLockCompanion(File file) {
        File companion = new File(file.getPath() + ".lock");
        return new FileSystemLock(companion);
    }

    /**
     * try to release an already locked file
     *
     * @throws AppLockException if the file could not be released or is not yet locked
     */
    public void release() throws AppLockException {
        if (os == null) {
            throw new AppLockException("Unable to release non open Lock on " + file, this);
        }
        synchronized (FileSystemLock.class) {
            try {
                os.close();
                os = null;
            } catch (IOException e) {
                throw new AppLockException("Unable to release Lock on " + file, this);
            }
            if (!file.delete()) {
                throw new AppLockException("Unable to release Lock on " + file, this);
            }
        }
        AppLockManager.getInstance().fireLockReleased(this);
    }

    public void forceRelease() throws AppLockException {
        if (os == null) {
            file.delete();
        } else {
            synchronized (FileSystemLock.class) {
                try {
                    os.close();
                    os = null;
                } catch (IOException e) {
                    throw new AppLockException("Unable to release Lock on " + file, this);
                }
                if (!file.delete()) {
                    throw new AppLockException("Unable to release Lock on " + file, this);
                }
            }
        }
        AppLockManager.getInstance().fireLockReleased(this);
    }

    @Override
    public String getName() {
        return file.getName();
    }

    public boolean isLocked() {
        synchronized (FileSystemLock.class) {
            if (os != null) {
                AppLockManager.getInstance().fireLockDetected(this);
                return true;
            }
            synchronized (FileSystemLock.class) {
                if (file.exists()) {
                    try {
                        FileInputStream is = null;
                        try {
                            is = new FileInputStream(file);
                            DataInputStream dis = new DataInputStream(is);
                            long magicNumber = dis.readLong();
                            if (magicNumber != 1977) {
                                System.err.println("Invalid Lock file " + file + ". invalid magicNumber.");
                            }
                            long releaseTime = dis.readLong();
                            if (System.currentTimeMillis() < releaseTime) {
                                System.err.println("Lock File detected " + file);
                                AppLockManager.getInstance().fireLockDetected(this);
                                return true;
                            }
                        } finally {
                            if (is != null) {
                                is.close();
                            }
                        }
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }
                file.delete();
                AppLockManager.getInstance().fireLockReleased(this);
            }
        }
        return false;
    }

    /**
     * try a acquire the file lock. If the lock is locked false is returned otherwise true is returned.
     *
     * @param lockTimeout if able to acquire the lock, the file is locked for maximum <code>lockTimeout</code> millisecond
     */
    public boolean tryAcquire(long lockTimeout) {
        try {
            synchronized (FileSystemLock.class) {
                if (isLocked()) {
                    //System.err.println("FAILED TO ACQUIRE-----" + Thread.currentThread() + " :: " + toString());
                    AppLockManager.getInstance().fireLockAcquireFailed(this);
//                    new Throwable().printStackTrace();
                    return false;
                }
                //System.err.println("ACQUIRE-----" + Thread.currentThread() + " :: " + toString());
//                new Throwable().printStackTrace();
                File pf = file.getParentFile();
                if (pf != null) {
                    pf.mkdirs();
                }
                os = new RandomAccessFile(file, "rw");
                os.writeLong(1977);
                os.writeLong(System.currentTimeMillis() + lockTimeout);
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
        AppLockManager.getInstance().fireLockAcquired(this);
        return true;
    }

    /**
     * try to release an already locked file
     *
     * @return true if release succeeded
     */
    public boolean tryRelease() {
        if (os == null) {
            return false;
        }
        synchronized (FileSystemLock.class) {
            try {
                os.close();
                os = null;
            } catch (IOException e) {
                return false;
            }
            //System.err.println("RELEASE-----" + toString());
//            new Throwable().printStackTrace();
            if (!file.delete()) {
                AppLockManager.getInstance().fireLockReleaseFailed(this);
                return false;
            }
        }
        AppLockManager.getInstance().fireLockReleased(this);
        return true;
    }

    @Override
    public int hashCode() {
        return file != null ? file.hashCode() : 0;
    }

//    public static void main(String[] args) {
//        final File f=new File("/home/vpc/a.txt");
//        FileSystemLock lock=new FileSystemLock(f);
//        FileSystemLock lock2=new FileSystemLock(f);
//        try {
//            lock.acquire(5000);
//            lock2.acquireOrWait(500,1000 * 3600);
//        }finally {
//            lock.tryRelease();
//            lock2.tryRelease();
//        }
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileSystemLock that = (FileSystemLock) o;

        return file != null ? file.equals(that.file) : that.file == null;
    }

    @Override
    public String toString() {
        return file.toString();
    }
}
