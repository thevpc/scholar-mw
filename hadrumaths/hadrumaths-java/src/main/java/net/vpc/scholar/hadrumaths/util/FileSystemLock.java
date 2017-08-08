package net.vpc.scholar.hadrumaths.util;

import java.io.*;
import java.util.concurrent.Callable;

/**
 * Simple File Lock implementation
 * Created by vpc on 11/13/16.
 */
public class FileSystemLock implements AppLock{
    private File file;
    //new RandomAccessFile(file,"rw")
    private RandomAccessFile os;

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public String toString() {
        return file.toString();
    }

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
     * runs the given process if and only if lock could be acquired.
     * Lock is guaranteed to be released at the end of the process execution.
     *
     * @param runnable process to run
     */
    public boolean invokeIfAcquired(long waitTime, Runnable runnable) {
        if (tryAcquire(waitTime)) {
            try {
                runnable.run();
            } finally {
                tryRelease();
            }
            return true;
        }
        return false;
    }

    /**
     * runs the given process if and only if lock could be acquired within the timeout.
     * if not an exception is thrown.
     * Lock is guaranteed to be released at the end of the process execution.
     *
     * @param runnable process to run
     * @throws FileSystemLockException if unable to acquire the lock
     */
    public void invokeOrWait(long waitTime, Runnable runnable) throws FileSystemLockException {
        acquireOrWait(waitTime);
        try {
            runnable.run();
        } finally {
            tryRelease();
        }
    }

    /**
     * runs the given process if and only if lock could be acquired within the timeout.
     * if not an false is returned.
     * Lock is guaranteed to be released at the end of the process execution.
     *
     * @param runnable process to run
     * @return true if was able to acquire the lock
     */
    public boolean tryInvokeOrWait(long waitTime, Runnable runnable) {
        if (tryAcquireOrWait(waitTime)) {
            try {
                runnable.run();
            } finally {
                tryRelease();
            }
            return true;
        }
        return false;
    }

    /**
     * runs the given process if lock if acquired. Lock is guaranteed to be released at the end of the process execution.
     *
     * @param runnable process to run
     */
    public void invoke(long lockTimeout, Runnable runnable) {
        acquire(lockTimeout);
        try {
            runnable.run();
        } finally {
            tryRelease();
        }
    }

    /**
     * runs the given process if lock if acquired. Lock is guaranteed to be released at the end of the process execution.
     *
     * @param runnable process to run
     */
    public <V> V invoke(long lockTimeout, Callable<V> runnable) {
        acquire(lockTimeout);
        try {
            try {
                return runnable.call();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } finally {
            tryRelease();
        }
    }

    /**
     * try a acquire the file lock. If the file is locked will try to wait for maximum  <code>waitTime</code>.
     * If the lock stills locked a FileSystemLockException is thrown.
     *
     * @param waitTime max time (milliseconds) to wait for if the file is already locked
     * @throws FileSystemLockException if unable to acquire the lock
     */
    public void acquireOrWait(long waitTime) throws FileSystemLockException {
        if (!tryAcquireOrWait(waitTime)) {
            throw new FileSystemLockException("Unable to lock " + file, file);
        }
    }

    /**
     * try a acquire the file lock. If the file is locked will try to wait for maximum  <code>waitTime</code>.
     * If the lock stills locked false is returned.
     *
     * @param waitTime max time (milliseconds) to wait for if the file is already locked
     * @return true if file locking succeeded withing the time span defined
     */
    public boolean tryAcquireOrWait(long waitTime) {
        long maxTime = (waitTime == Long.MAX_VALUE ? waitTime : (System.currentTimeMillis() + waitTime));
        long sleepTime = 0;
        if (waitTime < 100) {
            sleepTime = waitTime;
        } else if (waitTime < 1000) {
            sleepTime = 500;
        } else if (waitTime < 60000) {
            sleepTime = 2000;
        } else {
            sleepTime = 5000;
        }
        while (true) {
            if (tryAcquire(waitTime)) {
                return true;
            }
            if (System.currentTimeMillis() > maxTime) {
                break;
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                break;
            }
        }
        return false;
    }

    /**
     * try a acquire the file lock. If the lock is locked a FileSystemLockException is thrown.
     *
     * @param lockTimeout if able to acquire the lock, the file is locked for maximum <code>lockTimeout</code> millisecond
     */
    public void acquire(long lockTimeout) {
        if (!tryAcquireOrWait(lockTimeout)) {
            throw new FileSystemLockException("Unable to lock " + file, file);
        }
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
                        return true;
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }
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
                    return false;
                }
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
            if (!file.delete()) {
                return false;
            }
        }
        AppLockManager.getInstance().fireLockReleased(this);
        return true;
    }

    /**
     * try to release an already locked file
     *
     * @throws FileSystemLockException if the file could not be released or is not yet locked
     */
    public void release() throws FileSystemLockException {
        if (os == null) {
            throw new FileSystemLockException("Unable to release non open Lock on " + file, file);
        }
        synchronized (FileSystemLock.class) {
            try {
                os.close();
                os = null;
            } catch (IOException e) {
                throw new FileSystemLockException("Unable to release Lock on " + file, file);
            }
            if (!file.delete()) {
                throw new FileSystemLockException("Unable to release Lock on " + file, file);
            }
        }
        AppLockManager.getInstance().fireLockReleased(this);
    }

    public void forceRelease() throws FileSystemLockException {
        if (os == null) {
            file.delete();
        }else {
            synchronized (FileSystemLock.class) {
                try {
                    os.close();
                    os = null;
                } catch (IOException e) {
                    throw new FileSystemLockException("Unable to release Lock on " + file, file);
                }
                if (!file.delete()) {
                    throw new FileSystemLockException("Unable to release Lock on " + file, file);
                }
            }
        }
        AppLockManager.getInstance().fireLockReleased(this);
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
    public int hashCode() {
        return file != null ? file.hashCode() : 0;
    }
}
