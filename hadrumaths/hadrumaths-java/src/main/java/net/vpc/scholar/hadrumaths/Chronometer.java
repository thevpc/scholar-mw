package net.vpc.scholar.hadrumaths;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 13 juil. 2006 22:14:21
 */
public class Chronometer {
    public enum DatePart {
        h,
        m,
        s,
        ms
    }

    private long startDate;
    private long endDate;
    private String name;

    public Chronometer() {
        start();
    }

    public Chronometer(boolean start) {
        if(start){
            start();
        }
    }

    public Chronometer copy(){
        Chronometer c=new Chronometer();
        c.name = name;
        c.endDate = endDate;
        c.startDate = startDate;
        return c;
    }

    /**
     * restart chronometer and returns a stopped snapshot/copy of the current
     * @return
     */
    public Chronometer restart(){
        stop();
        Chronometer c = copy();
        start();
        return c;
    }

    /**
     * restart chronometer with new name and returns a stopped snapshot/copy of the current (with old name)
     * @param newName
     * @return
     */
    public Chronometer restart(String newName){
        stop();
        Chronometer c = copy();
        setName(newName);
        start();
        return c;
    }

    public Chronometer(String name) {
        this.name = name;
        start();
    }

    public Chronometer setName(String desc) {
        this.name = desc;
        return this;
    }
    public Chronometer updateDescription(String desc) {
        setName(desc);
        return this;
    }

    public String getName() {
        return name;
    }

    public boolean isStarted() {
        return startDate != 0 && endDate == 0;
    }

    public boolean isStopped() {
        return endDate == 0;
    }

    public Chronometer start() {
        endDate = 0;
        startDate = System.currentTimeMillis();
        return this;
    }

    public Chronometer stop() {
        endDate = System.currentTimeMillis();
        return this;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public long getTime() {
        return ((endDate <= 0) ? System.currentTimeMillis() : endDate) - startDate;
    }

    public int getMilliSeconds() {
        return (int) (getTime() % 1000L);
    }

    public int getSeconds() {
        return (int) ((getTime() % 60000L) / 1000L);
    }

    public int getMinutes() {
        return (int) ((getTime() % (1000L * 60L * 60L)) / 60000L);
    }

    public int getHours() {
        //old 0x36ee80L
        return (int) (getTime() / (1000L * 60L * 60L));
    }


    public static String formatPeriod(long period) {
        StringBuilder sb = new StringBuilder();
        boolean started = false;
        int h = (int) (period / (1000L * 60L * 60L));
        int mn = (int) ((period % (1000L * 60L * 60L)) / 60000L);
        int s = (int) ((period % 60000L) / 1000L);
        int ms = (int) (period % 1000L);

        if (h > 0) {
            sb.append(h).append(" h ");
            started = true;
        }
        if (mn > 0 || started) {
            sb.append(mn).append(" mn ");
            started = true;
        }
        if (s > 0 || started) {
            sb.append(s).append(" s ");
            //started=true;
        }
        sb.append(ms).append(" ms");
        return sb.toString();
    }

    public static String formatPeriod(long period, DatePart precision) {
        StringBuilder sb = new StringBuilder();
        boolean started = false;
        int h = (int) (period / (1000L * 60L * 60L));
        int mn = (int) ((period % (1000L * 60L * 60L)) / 60000L);
        int s = (int) ((period % 60000L) / 1000L);
        int ms = (int) (period % 1000L);
        if (precision.ordinal() >= DatePart.h.ordinal()) {
            if (h > 0) {
                sb.append(h).append(" h ");
                started = true;
            }
            if (precision.ordinal() >= DatePart.m.ordinal()) {
                if (mn > 0 || started) {
                    sb.append(mn).append(" mn ");
                    started = true;
                }
                if (precision.ordinal() >= DatePart.s.ordinal()) {
                    if (s > 0 || started) {
                        sb.append(s).append(" s ");
                        //started=true;
                    }
                    if (precision.ordinal() >= DatePart.ms.ordinal()) {
                        sb.append(ms).append(" ms");
                    }
                }
            }
        }
        return sb.toString();
    }

    public String toString() {
        String s= name ==null?"": name +"=";
        return s+formatPeriod(getTime());
    }

    public String toString(DatePart precision) {
        String s= name ==null?"": name +"=";
        return s+formatPeriod(getTime(),precision);
    }
}
