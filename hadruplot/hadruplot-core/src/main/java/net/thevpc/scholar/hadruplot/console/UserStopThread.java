package net.thevpc.scholar.hadruplot.console;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 6 janv. 2007 12:12:53
 */
abstract class UserStopThread extends Thread {
    boolean stopped;
    long sleep;

    protected UserStopThread(long sleep) {
        super("UserStopThread");
        setDaemon(true);
        this.sleep = sleep;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void softStop() {
        this.stopped = true;
    }

    public void run() {
        while (!stopped) {
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            execute();
        }
    }

    public abstract void execute();
}
