package net.thevpc.scholar.hadrumaths.concurrent;

import java.util.concurrent.Callable;

public interface AppLock {
    void release();

    void forceRelease();

    String getName();

    boolean isLocked();

    boolean invokeIfAcquired(long waitTime, Runnable runnable);

    void invokeOrWait(long waitTime, Runnable runnable) throws AppLockException;

    boolean tryInvokeOrWait(long waitTime, Runnable runnable);

    void invoke(long lockTimeout, Runnable runnable);

    <V> V invoke(long lockTimeout, Callable<V> runnable);

    void acquireOrWait(long waitTime) throws AppLockException;

    boolean tryAcquireOrWait(long waitTime);

    /**
     * try a acquire the file lock. If the lock is locked false is returned otherwise true is returned.
     *
     * @param lockTimeout if able to acquire the lock, the file is locked for maximum <code>lockTimeout</code> millisecond
     */
    boolean tryAcquire(long lockTimeout);

    /**
     * try to release an already locked file
     *
     * @return true if release succeeded
     */
    boolean tryRelease();

    void acquire(long lockTimeout);
}
