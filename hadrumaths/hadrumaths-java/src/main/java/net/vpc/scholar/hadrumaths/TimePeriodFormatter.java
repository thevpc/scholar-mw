//package net.vpc.scholar.hadrumaths;
//
//import net.vpc.scholar.hadrumaths.util.StringUtils;
//
//import java.text.DecimalFormat;
//import java.util.Calendar;
//
///**
// * Created by vpc on 3/20/17.
// */
//public class TimePeriodFormatter {
//    private static final DecimalFormat SECONDS_FORMAT = new DecimalFormat("00.000");
//    private int precision;
//
//    public TimePeriodFormatter() {
//        this(Calendar.MILLISECOND);
//    }
//
//    public TimePeriodFormatter(int precision) {
//        this.precision = precision;
//    }
//
//
//    public String format(long period) {
//        int max;
//        boolean empty = true;
//        switch (precision) {
//            case Calendar.DATE: {
//                max = 1;
//                break;
//            }
//            case Calendar.HOUR: {
//                max = 2;
//                break;
//            }
//            case Calendar.MINUTE: {
//                max = 3;
//                break;
//            }
//            case Calendar.SECOND: {
//                max = 4;
//                break;
//            }
//            case Calendar.MILLISECOND: {
//                max = 5;
//                break;
//            }
//            default: {
//                throw new IllegalArgumentException("Unsupported precision use Calendar.DATE,Calendar.HOUR, ...Calendar.MILLISECOND");
//            }
//        }
////        int milliSeconds=(int) (period % 1000L);
//
//        double seconds = ((period % (60L * 1000L)) / 1000.0);
//
//        int minutes = (int) ((period % (60L * 60L * 1000L)) / (60L * 1000L));
//
//        int hours = (int) ((period % (24L * 60L * 60L * 1000L)) / (60L * 60L * 1000L));
//
//        int days = (int) (period / (24L * 60L * 60L * 1000L));
//        boolean skipZeros = false;
//        StringBuilder sb = new StringBuilder();
//
//        if (max > 0) {
//            if (days != 0 || (empty && max == 1)) {
//                if (!empty) {
//                    sb.append(' ');
//                }
//                sb.append(StringUtils.formatLeft(days, 2)).append('d');
//                empty = false;
//            }
//            max--;
//        }
//
//        if (max > 0) {
//            if (hours != 0 || (empty && max == 1) || (!empty && !skipZeros)) {
//                if (!empty) {
//                    sb.append(' ');
//                }
//                sb.append(StringUtils.formatLeft(hours, 2)).append('h');
//                empty = false;
//            }
//            max--;
//        }
//
//        if (max > 0) {
//            if (minutes != 0 || (empty && max == 1) || (!empty && !skipZeros)) {
//                if (!empty) {
//                    sb.append(' ');
//                }
//                sb.append(StringUtils.formatLeft(minutes, 2)).append("mn");
//                empty = false;
//            }
//            max--;
//        }
//
//        if (max > 1) {
//            if (seconds != 0 || empty) {
//                if (!empty) {
//                    sb.append(' ');
//                }
//                if (seconds == (int) seconds) {
//                    sb.append(StringUtils.formatLeft((int) seconds, 2)).append('s').append("    ");
//                } else {
//                    sb.append(SECONDS_FORMAT.format(seconds)).append('s');
//                }
//
////                empty = false;
//            }
////            max--;
////            max--;
//
//        } else if (max > 0) {
//            if (seconds != 0 || empty) {
//                if (!empty) {
//                    sb.append(' ');
//                }
//                sb.append((int) seconds);
//                sb.append('s');
////                empty = false;
//            }
////            max--;
//        }
//        return sb.toString();
//    }
//
//}
