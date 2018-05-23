//package net.vpc.scholar.hadrumaths;
//
//import net.vpc.scholar.hadrumaths.util.*;
//import net.vpc.scholar.hadrumaths.monitors.*;
//
//import java.io.PrintStream;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///**
// * @author Taha Ben Salah (taha.bensalah@gmail.com)
// * @creationtime 31 juil. 2007 23:57:10
// */
//public class ProgressMonitorFactory extends AbstractFactory {
//    public static EnhancedProgressMonitor none() {
//        return new SilentEnhancedProgressMonitor();
//    }
//
//
//    //    private static final PrintStreamProgressMonitor STDOUT=new PrintStreamProgressMonitor(null,System.out);
////    private static final PrintStreamProgressMonitor STDERR=new PrintStreamProgressMonitor(null,System.err);
////    public static ProgressMonitor makeNotNull(ProgressMonitor c) {
////        return c == null ? none() : c;
////    }
//
//    //    public static void main(String[] args) {
////        EnhancedProgressMonitor out = log(TLogNull.SILENT).temporize(100).translate(0.8, 0).translate(0.5,0).translate(0.8,0).translate(0.5,0);
////        EnhancedProgressMonitor a = out.translate(0.8, 0);
////        EnhancedProgressMonitor[] m = a.split(new double[]{50, 50}, new boolean[]{true, false});
////        m[0].terminatem("Bye");
////        System.out.println(a.getProgressValue());
////        System.out.println(out.getProgressValue());
////    }
//    public static boolean isSilent(ProgressMonitor monitor) {
//        return monitor instanceof SilentEnhancedProgressMonitor;
//    }
//
//    /**
//     * creates Monitors for each enabled Element or null if false
//     *
//     * @param baseMonitor     translated
//     * @param enabledElements translated elements flag
//     * @return ProgressMonitor[] array that contains nulls or  translated baseMonitor
//     */
//    public static EnhancedProgressMonitor[] split(ProgressMonitor baseMonitor, boolean... enabledElements) {
//        EnhancedProgressMonitor[] all = new EnhancedProgressMonitor[enabledElements.length];
//        int count = 0;
//        for (boolean enabledElement : enabledElements) {
//            if (enabledElement) {
//                count++;
//            }
//        }
//        int index = 0;
//        for (int i = 0; i < enabledElements.length; i++) {
//            boolean enabledElement = enabledElements[i];
//            if (enabledElement) {
//                all[i] = translate(baseMonitor, 1.0 / count, ((double) index) / count);
//                index++;
//            }
//        }
//        return all;
//    }
//
//    /**
//     * creates Monitors for each enabled Element or null if false
//     *
//     * @param baseMonitor translated
//     * @return ProgressMonitor[] array that contains nulls or  translated baseMonitor
//     */
//    public static EnhancedProgressMonitor[] split(ProgressMonitor baseMonitor, int nbrElements) {
//        double[] dd = new double[nbrElements];
//        boolean[] bb = new boolean[nbrElements];
//        for (int i = 0; i < bb.length; i++) {
//            dd[i] = 1;
//            bb[i] = true;
//        }
//        return split(baseMonitor, dd, bb);
//    }
//
//    public static EnhancedProgressMonitor[] split(ProgressMonitor baseMonitor, double[] weight) {
//        boolean[] bb = new boolean[weight.length];
//        for (int i = 0; i < bb.length; i++) {
//            bb[i] = true;
//        }
//        return split(baseMonitor, weight, bb);
//    }
//
//    public static EnhancedProgressMonitor[] split(ProgressMonitor baseMonitor, double[] weight, boolean[] enabledElements) {
//        EnhancedProgressMonitor[] all = new EnhancedProgressMonitor[enabledElements.length];
//        double[] coeffsOffsets = new double[enabledElements.length];
//        double[] xweight = new double[enabledElements.length];
//        double coeffsSum = 0;
//        for (int i = 0; i < enabledElements.length; i++) {
//            boolean enabledElement = enabledElements[i];
//            if (enabledElement) {
//                coeffsSum += weight[i];
//            }
//        }
//        double coeffsOffset = 0;
//        for (int i = 0; i < enabledElements.length; i++) {
//            boolean enabledElement = enabledElements[i];
//            if (enabledElement) {
//                coeffsOffsets[i] = coeffsOffset;
//                xweight[i] = (weight[i] / coeffsSum);
//                coeffsOffset += xweight[i];
//            }
//        }
//        for (int i = 0; i < enabledElements.length; i++) {
//            boolean enabledElement = enabledElements[i];
//            if (enabledElement) {
//                all[i] = translate(baseMonitor, xweight[i], coeffsOffsets[i]);
//            } else {
//                all[i] = none();
//            }
//        }
//        return all;
//    }
//
//    public static EnhancedProgressMonitor translate(ProgressMonitor baseMonitor, int index, int max) {
//        return new ProgressMonitorTranslator(baseMonitor, 1.0 / max, index * (1.0 / max));
//    }
//
//    public static EnhancedProgressMonitor translate(ProgressMonitor baseMonitor, double factor, double start) {
//        EnhancedProgressMonitor enhanced = enhance(baseMonitor);
//        if (isSilent(enhanced)) {
//            return enhanced;
//        }
//        return new ProgressMonitorTranslator(baseMonitor, factor, start);
//    }
//
//    public static void setProgress(ProgressMonitor baseMonitor, int i, int max, String message) {
//        baseMonitor.setProgress((1.0 * i / max), new StringProgressMessage(Level.FINE, message));
//    }
//
//    public static void setProgress(ProgressMonitor baseMonitor, int i, int j, int maxi, int maxj, String message) {
//        baseMonitor.setProgress(((1.0 * i * maxi) + j) / (maxi * maxj), new StringProgressMessage(Level.FINE, message));
//    }
//
//    public static EnhancedProgressMonitor[] createSilentMonitors(int count) {
//        EnhancedProgressMonitor[] mon = new EnhancedProgressMonitor[count];
//        for (int i = 0; i < count; i++) {
//            mon[i] = none();
//        }
//        return mon;
//    }
//
//    public static EnhancedProgressMonitor createIncrementalMonitor(ProgressMonitor baseMonitor, int iterations) {
//        EnhancedProgressMonitor enhanced = enhance(baseMonitor);
//        if (isSilent(enhanced)) {
//            return enhanced;
//        }
//        EnhancedProgressMonitor i = enhance(baseMonitor);
//        i.setIncrementor(new IntIterationProgressMonitorInc(iterations));
//        return i;
//    }
//
//    public static EnhancedProgressMonitor createIncrementalMonitor(ProgressMonitor baseMonitor, long iterations) {
//        EnhancedProgressMonitor enhanced = enhance(baseMonitor);
//        if (isSilent(enhanced)) {
//            return enhanced;
//        }
//        EnhancedProgressMonitor i = enhance(baseMonitor);
//        i.setIncrementor(new LongIterationProgressMonitorInc(iterations));
//        return i;
//    }
//
//    public static EnhancedProgressMonitor createIncrementalMonitor(ProgressMonitor baseMonitor, double delta) {
//        EnhancedProgressMonitor enhanced = enhance(baseMonitor);
//        if (isSilent(enhanced)) {
//            return enhanced;
//        }
//        EnhancedProgressMonitor i = enhance(baseMonitor);
//        i.setIncrementor(new DeltaProgressMonitorInc(delta));
//        return i;
//    }
//
//    public static EnhancedProgressMonitor createLogMonitor(long freq) {
//        return logger().temporize(freq);
//    }
//
//    public static EnhancedProgressMonitor createLogMonitor(String message, long freq) {
//        return temporize(new LongIterationProgressMonitorInc.LogProgressMonitor(message, null), freq);
//    }
//
//    public static EnhancedProgressMonitor createLogMonitor(String message, long freq, Logger out) {
//        return logger(message, out).temporize(freq);
//    }
//
//    public static EnhancedProgressMonitor createOutMonitor(long freq) {
//        return out().temporize(freq);
//    }
//
//    public static EnhancedProgressMonitor createOutMonitor(String message, long freq) {
//        return temporize(new PrintStreamProgressMonitor(message, null), freq);
//    }
//
//    public static EnhancedProgressMonitor createOutMonitor(String message, long freq, PrintStream out) {
//        return printStream(message, out).temporize(freq);
//    }
//
//    public static EnhancedProgressMonitor temporize(ProgressMonitor baseMonitor, long freq) {
//        EnhancedProgressMonitor enhanced = enhance(baseMonitor);
//        if (isSilent(enhanced)) {
//            return enhanced;
//        }
//        return new FreqProgressMonitor(baseMonitor, freq);
//    }
//
//    public static EnhancedProgressMonitor printStream(String messageFormat, PrintStream printStream) {
//        return new PrintStreamProgressMonitor(messageFormat, printStream);
//    }
//
//    public static EnhancedProgressMonitor logger(String messageFormat, Logger printStream) {
//        return new LongIterationProgressMonitorInc.LogProgressMonitor(messageFormat, printStream);
//    }
//
//    public static EnhancedProgressMonitor logger(Logger printStream) {
//        return new LongIterationProgressMonitorInc.LogProgressMonitor(null, printStream);
//    }
//
//    public static EnhancedProgressMonitor logger(long milliseconds) {
//        return logger().temporize(milliseconds);
//    }
//
//    public static EnhancedProgressMonitor logger() {
//        return new LongIterationProgressMonitorInc.LogProgressMonitor(null, null);
//    }
//
//    public static EnhancedProgressMonitor logger(String messageFormat, TLog printStream) {
//        return new TLogProgressMonitor(messageFormat, printStream);
//    }
//
//    public static EnhancedProgressMonitor logger(TLog printStream) {
//        return new TLogProgressMonitor(null, printStream);
//    }
//
//    public static EnhancedProgressMonitor out(String messageFormat) {
//        return printStream(messageFormat, System.out);
//    }
//
//    public static EnhancedProgressMonitor out() {
//        return printStream(null, System.out);
//    }
//
//    public static EnhancedProgressMonitor err() {
//        return printStream(null, System.err);
//    }
//
//    public static EnhancedProgressMonitor err(String messageFormat) {
//        return printStream(null, System.err);
//    }
//
//    public static EnhancedProgressMonitor enhance(ProgressMonitor monitor) {
//        if (monitor == null) {
//            return none();
//        }
//        if (monitor instanceof EnhancedProgressMonitor) {
//            return (EnhancedProgressMonitor) monitor;
//        }
//        return new ProgressMonitorAdapter(monitor);
//    }
//
//    public static <T> T invokeMonitoredAction(ProgressMonitor mon, String messagePrefix, MonitoredAction<T> run) {
//        EnhancedProgressMonitor monitor = ProgressMonitorFactory.enhance(mon);
//        monitor.start(messagePrefix + ", starting...");
//        String error = null;
//        T val = null;
//        Chronometer chronometer = Maths.chrono();
//        try {
//            val = run.process(monitor, messagePrefix);
//        } catch (RuntimeException e) {
//            error = e.toString();
//            if (StringUtils.isEmpty(error)) {
//                error = e.getClass().getName();
//            }
//            throw e;
//        } catch (Throwable e) {
//            error = e.toString();
//            if (StringUtils.isEmpty(error)) {
//                error = e.getClass().getName();
//            }
//            throw new InvocationException(e);
//        } finally {
//            Chronometer time = chronometer.stop();
//            if (error != null) {
//                monitor.terminate(messagePrefix + ", terminated with error after {0}", time);
//            } else {
//                monitor.terminate(messagePrefix + ", terminated after {0}", time);
//            }
//        }
//        return val;
//    }
//}
