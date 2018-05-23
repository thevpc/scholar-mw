//package net.vpc.scholar.hadrumaths;
//
///**
// * @author Taha BEN SALAH (taha.bensalah@gmail.com)
// * @creationtime 13 juil. 2006 22:14:21
// */
//public class Chronometer {
//    public enum DatePart {
//        h,
//        m,
//        s,
//        ms
//    }
//
////    public static void main(String[] args) {
////        Chronometer c=new Chronometer();
////        try {
////            Thread.sleep(3000);
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
////        c.stop();
////        System.out.println(c.getTime());
////        System.out.println(c.toString());
////    }
//
//    private long startDate;
//    private long endDate;
//    private String name;
//
//    public Chronometer() {
//        start();
//    }
//
//    public Chronometer(boolean start) {
//        if (start) {
//            start();
//        }
//    }
//
//    public Chronometer copy() {
//        Chronometer c = new Chronometer();
//        c.name = name;
//        c.endDate = endDate;
//        c.startDate = startDate;
//        return c;
//    }
//
//    /**
//     * restart chronometer and returns a stopped snapshot/copy of the current
//     *
//     * @return
//     */
//    public Chronometer restart() {
//        stop();
//        Chronometer c = copy();
//        start();
//        return c;
//    }
//
//    /**
//     * restart chronometer with new name and returns a stopped snapshot/copy of the current (with old name)
//     *
//     * @param newName
//     * @return
//     */
//    public Chronometer restart(String newName) {
//        stop();
//        Chronometer c = copy();
//        setName(newName);
//        start();
//        return c;
//    }
//
//    public Chronometer(String name) {
//        this.name = name;
//        start();
//    }
//
//    public Chronometer setName(String desc) {
//        this.name = desc;
//        return this;
//    }
//
//    public Chronometer updateDescription(String desc) {
//        setName(desc);
//        return this;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public boolean isStarted() {
//        return startDate != 0 && endDate == 0;
//    }
//
//    public boolean isStopped() {
//        return endDate == 0;
//    }
//
//    public Chronometer start() {
//        endDate = 0;
//        startDate = System.nanoTime();
//        return this;
//    }
//
//    public Chronometer stop() {
//        endDate = System.nanoTime();
//        return this;
//    }
//
//    public long getStartTime() {
//        return startDate;
//    }
//
//    public long getEndDate() {
//        return endDate;
//    }
//
//    public long getTime() {
//        return startDate == 0 ? 0 : ((endDate <= 0) ? System.nanoTime() : endDate) - startDate;
//    }
//
//    public int getMilliSeconds() {
//        long period = getTime() / 1000000;
//        return (int) (getTime() % 1000L);
//    }
//
//    public int getSeconds() {
//        long period = getTime() / 1000000;
//        return (int) ((period % 60000L) / 1000L);
//    }
//
//    public int getMinutes() {
//        long period = getTime() / 1000000;
//        return (int) ((period % (1000L * 60L * 60L)) / 60000L);
//    }
//
//    public int getHours() {
//        long period = getTime() / 1000000;
//        return (int) (period / (1000L * 60L * 60L));
//    }
//
//
//    public static String formatPeriodMilli(long period) {
//        StringBuilder sb = new StringBuilder();
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
//    public static String formatPeriodNano(long periodNano) {
//        StringBuilder sb = new StringBuilder();
//        int nano = (int) (periodNano % 1000000);
//        long period = periodNano / 1000000;
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
//
//        if (ms < 10) {
//            sb.append(" ").append(nano).append(" nanos");
//        }
//        return sb.toString();
//    }
//
//    public static String formatPeriodNano(long period, DatePart precision) {
//        StringBuilder sb = new StringBuilder();
//        period = period / 1000000;
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
//
//    public static String formatPeriodMilli(long period, DatePart precision) {
//        StringBuilder sb = new StringBuilder();
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
//
//    public String toString() {
//        String s = name == null ? "" : name + "=";
//        return s + formatPeriodNano(getTime());
//    }
//
//    public String toString(DatePart precision) {
//        String s = name == null ? "" : name + "=";
//        return s + formatPeriodNano(getTime(), precision);
//    }
//}
