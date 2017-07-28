package net.vpc.scholar.hadrumaths.plot.console;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
* @creationtime 6 janv. 2007 12:21:32
*/
public class SerializerThread extends UserStopThread {
    public SerializerThread() {
        super(1000);
        setName("SerializerThread");
    }

    public void serialize() {
        start();
        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void execute() {
    }
}
