package net.vpc.scholar.hadrumaths;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 13 juil. 2006 22:14:21
 */
public class MemoryMeter {

    private MemoryInfo startMemory;
    private MemoryInfo endMemory;
    private String name;

    public MemoryMeter() {
        start();
    }

    public MemoryMeter(boolean start) {
        if (start) {
            start();
        }
    }

    public MemoryMeter copy() {
        MemoryMeter c = new MemoryMeter();
        c.name = name;
        c.endMemory = endMemory;
        c.startMemory = startMemory;
        return c;
    }

    /**
     * restart chronometer and returns a stopped snapshot/copy of the current
     *
     * @return
     */
    public MemoryMeter restart() {
        stop();
        MemoryMeter c = copy();
        start();
        return c;
    }

    /**
     * restart chronometer with new name and returns a stopped snapshot/copy of the current (with old name)
     *
     * @param newName
     * @return
     */
    public MemoryMeter restart(String newName) {
        stop();
        MemoryMeter c = copy();
        setName(newName);
        start();
        return c;
    }

    public MemoryMeter(String name) {
        this.name = name;
    }

    public MemoryMeter setName(String desc) {
        this.name = desc;
        return this;
    }

    public MemoryMeter updateDescription(String desc) {
        setName(desc);
        return this;
    }

    public String getName() {
        return name;
    }

    public boolean isStarted() {
        return startMemory != null && endMemory == null;
    }

    public boolean isStopped() {
        return endMemory == null;
    }

    public MemoryMeter start() {
        endMemory = null;
        startMemory = new MemoryInfo();
        return this;
    }

    public MemoryMeter stop() {
        endMemory = new MemoryInfo();
        return this;
    }

    public MemoryInfo getStartMemory() {
        return startMemory;
    }

    public MemoryInfo getEndMemory() {
        return endMemory;
    }

    public MemoryUsage getMemoryUsage() {
        return ((endMemory == null) ? new MemoryInfo() : endMemory).diff(startMemory);
    }


//    public static String formatPeriod(long period) {
//        StringBuffer sb = new StringBuffer();
//        boolean started = false;
//        int h = (int) (period / (1000L * 60L * 60L));
//        int mn = (int) ((period % (1000L * 60L * 60L)) / 60000L);
//        int s = (int) ((period % 60000L) / 1000L);
//        int ms = (int) (period % 1000L);
//
//        if (h > 0) {
//            sb.append(h).append(" h ");
//            started = true;
//        }
//        if (mn > 0 || started) {
//            sb.append(mn).append(" mn ");
//            started = true;
//        }
//        if (s > 0 || started) {
//            sb.append(s).append(" s ");
//            //started=true;
//        }
//        sb.append(ms).append(" ms");
//        return sb.toString();
//    }
//
//    public static String formatPeriod(long period, DatePart precision) {
//        StringBuffer sb = new StringBuffer();
//        boolean started = false;
//        int h = (int) (period / (1000L * 60L * 60L));
//        int mn = (int) ((period % (1000L * 60L * 60L)) / 60000L);
//        int s = (int) ((period % 60000L) / 1000L);
//        int ms = (int) (period % 1000L);
//        if (precision.ordinal() >= DatePart.h.ordinal()) {
//            if (h > 0) {
//                sb.append(h).append(" h ");
//                started = true;
//            }
//            if (precision.ordinal() >= DatePart.m.ordinal()) {
//                if (mn > 0 || started) {
//                    sb.append(mn).append(" mn ");
//                    started = true;
//                }
//                if (precision.ordinal() >= DatePart.s.ordinal()) {
//                    if (s > 0 || started) {
//                        sb.append(s).append(" s ");
//                        //started=true;
//                    }
//                    if (precision.ordinal() >= DatePart.ms.ordinal()) {
//                        sb.append(ms).append(" ms");
//                    }
//                }
//            }
//        }
//        return sb.toString();
//    }

    public String toString() {
        String s = name == null ? "" : name + "=";
        return s + getMemoryUsage().toString();
    }
}
