package net.vpc.scholar.hadrumaths.concurrent;

import java.util.concurrent.Callable;

public abstract class AbstractAppLock implements AppLock {
    /**
     * runs the given process if and only if lock could be acquired.
     * Lock is guaranteed to be released at the end of the process execution.
     *
     * @param runnable process to run
     */
    @Override
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
     * @throws AppLockException if unable to acquire the lock
     */
    @Override
    public void invokeOrWait(long waitTime, Runnable runnable) throws AppLockException {
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
    @Override
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
    @Override
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
    @Override
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
     * If the lock stills locked a AppLockException is thrown.
     *
     * @param waitTime max time (milliseconds) to wait for if the file is already locked
     * @throws AppLockException if unable to acquire the lock
     */
    @Override
    public void acquireOrWait(long waitTime) throws AppLockException {
        if (!tryAcquireOrWait(waitTime)) {
            throw new AppLockException("Unable to lock " + this, this);
        }
    }

    /**
     * try a acquire the file lock. If the file is locked will try to wait for maximum  <code>waitTime</code>.
     * If the lock stills locked false is returned.
     *
     * @param waitTime max time (milliseconds) to wait for if the file is already locked
     * @return true if file locking succeeded withing the time span defined
     */
    @Override
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
     * try a acquire the file lock. If the lock is locked a AppLockException is thrown.
     *
     * @param lockTimeout if able to acquire the lock, the file is locked for maximum <code>lockTimeout</code> millisecond
     */
    @Override
    public void acquire(long lockTimeout) {
        if (!tryAcquireOrWait(lockTimeout)) {
            throw new AppLockException("Unable to lock " + toString(), this);
        }
    }

}
