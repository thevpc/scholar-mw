package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.util.*;

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 31 juil. 2007 23:57:10
 */
public class ComputationMonitorFactory extends AbstractFactory {
    public static EnhancedComputationMonitor none() {
        return new SilentEnhancedComputationMonitor();
    }


    //    private static final PrintStreamComputationMonitor STDOUT=new PrintStreamComputationMonitor(null,System.out);
//    private static final PrintStreamComputationMonitor STDERR=new PrintStreamComputationMonitor(null,System.err);
//    public static ComputationMonitor makeNotNull(ComputationMonitor c) {
//        return c == null ? none() : c;
//    }

//    public static void main(String[] args) {
//        EnhancedComputationMonitor out = log(TLogNull.SILENT).temporize(100).translate(0.8, 0).translate(0.5,0).translate(0.8,0).translate(0.5,0);
//        EnhancedComputationMonitor a = out.translate(0.8, 0);
//        EnhancedComputationMonitor[] m = a.split(new double[]{50, 50}, new boolean[]{true, false});
//        m[0].terminatem("Bye");
//        System.out.println(a.getProgressValue());
//        System.out.println(out.getProgressValue());
//    }
    public static boolean isSilent(ComputationMonitor monitor){
        return monitor instanceof SilentEnhancedComputationMonitor;
    }
    /**
     * creates Monitors for each enabled Element or null if false
     *
     * @param baseMonitor     translated
     * @param enabledElements translated elements flag
     * @return ComputationMonitor[] array that contains nulls or  translated baseMonitor
     */
    public static EnhancedComputationMonitor[] split(ComputationMonitor baseMonitor, boolean... enabledElements) {
        EnhancedComputationMonitor[] all = new EnhancedComputationMonitor[enabledElements.length];
        int count = 0;
        for (boolean enabledElement : enabledElements) {
            if (enabledElement) {
                count++;
            }
        }
        int index = 0;
        for (int i = 0; i < enabledElements.length; i++) {
            boolean enabledElement = enabledElements[i];
            if (enabledElement) {
                all[i] = translate(baseMonitor, 1.0 / count, ((double) index) / count);
                index++;
            }
        }
        return all;
    }

    /**
     * creates Monitors for each enabled Element or null if false
     *
     * @param baseMonitor translated
     * @return ComputationMonitor[] array that contains nulls or  translated baseMonitor
     */
    public static EnhancedComputationMonitor[] split(ComputationMonitor baseMonitor, int nbrElements) {
        double[] dd = new double[nbrElements];
        boolean[] bb = new boolean[nbrElements];
        for (int i = 0; i < bb.length; i++) {
            dd[i] = 1;
            bb[i] = true;
        }
        return split(baseMonitor, dd, bb);
    }

    public static EnhancedComputationMonitor[] split(ComputationMonitor baseMonitor, double[] weight) {
        boolean[] bb = new boolean[weight.length];
        for (int i = 0; i < bb.length; i++) {
            bb[i] = true;
        }
        return split(baseMonitor, weight, bb);
    }

    public static EnhancedComputationMonitor[] split(ComputationMonitor baseMonitor, double[] weight, boolean[] enabledElements) {
        EnhancedComputationMonitor[] all = new EnhancedComputationMonitor[enabledElements.length];
        double[] coeffsOffsets = new double[enabledElements.length];
        double[] xweight = new double[enabledElements.length];
        double coeffsSum = 0;
        for (int i = 0; i < enabledElements.length; i++) {
            boolean enabledElement = enabledElements[i];
            if (enabledElement) {
                coeffsSum += weight[i];
            }
        }
        double coeffsOffset = 0;
        for (int i = 0; i < enabledElements.length; i++) {
            boolean enabledElement = enabledElements[i];
            if (enabledElement) {
                coeffsOffsets[i] = coeffsOffset;
                xweight[i] = (weight[i] / coeffsSum);
                coeffsOffset += xweight[i];
            }
        }
        for (int i = 0; i < enabledElements.length; i++) {
            boolean enabledElement = enabledElements[i];
            if (enabledElement) {
                all[i] = translate(baseMonitor, xweight[i], coeffsOffsets[i]);
            }else{
                all[i]= none();
            }
        }
        return all;
    }

    public static EnhancedComputationMonitor translate(ComputationMonitor baseMonitor, int index, int max) {
        return new ComputationMonitorTranslator(baseMonitor, 1.0 / max, index * (1.0 / max));
    }

    public static EnhancedComputationMonitor translate(ComputationMonitor baseMonitor, double factor, double start) {
        EnhancedComputationMonitor enhanced = enhance(baseMonitor);
        if(isSilent(enhanced)){
            return enhanced;
        }
        return new ComputationMonitorTranslator(baseMonitor, factor,start);
    }

    public static void setProgress(ComputationMonitor baseMonitor, int i, int max, String message) {
        baseMonitor.setProgress((1.0 * i / max), new StringProgressMessage(Level.FINE, message));
    }

    public static void setProgress(ComputationMonitor baseMonitor, int i, int j, int maxi, int maxj, String message) {
        baseMonitor.setProgress(((1.0 * i * maxi) + j) / (maxi * maxj), new StringProgressMessage(Level.FINE, message));
    }

    public static EnhancedComputationMonitor[] createSilentMonitors(int count) {
        EnhancedComputationMonitor[] mon = new EnhancedComputationMonitor[count];
        for (int i = 0; i < count; i++) {
            mon[i] = none();
        }
        return mon;
    }

    public static EnhancedComputationMonitor createIncrementalMonitor(ComputationMonitor baseMonitor, int iterations) {
        EnhancedComputationMonitor enhanced = enhance(baseMonitor);
        if(isSilent(enhanced)){
            return enhanced;
        }
        EnhancedComputationMonitor i = enhance(baseMonitor);
        i.setIncrementor(new IntIterationComputationMonitorInc(iterations));
        return i;
    }

    public static EnhancedComputationMonitor createIncrementalMonitor(ComputationMonitor baseMonitor, long iterations) {
        EnhancedComputationMonitor enhanced = enhance(baseMonitor);
        if(isSilent(enhanced)){
            return enhanced;
        }
        EnhancedComputationMonitor i = enhance(baseMonitor);
        i.setIncrementor(new LongIterationComputationMonitorInc(iterations));
        return i;
    }

    public static EnhancedComputationMonitor createIncrementalMonitor(ComputationMonitor baseMonitor, double delta) {
        EnhancedComputationMonitor enhanced = enhance(baseMonitor);
        if(isSilent(enhanced)){
            return enhanced;
        }
        EnhancedComputationMonitor i = enhance(baseMonitor);
        i.setIncrementor(new DeltaComputationMonitorInc(delta));
        return i;
    }

    public static EnhancedComputationMonitor createLogMonitor(long freq) {
        return logger().temporize(freq);
    }

    public static EnhancedComputationMonitor createLogMonitor(String message, long freq) {
        return temporize(new LogComputationMonitor(message, null), freq);
    }

    public static EnhancedComputationMonitor createLogMonitor(String message, long freq, Logger out) {
        return logger(message, out).temporize(freq);
    }

    public static EnhancedComputationMonitor createOutMonitor(long freq) {
        return out().temporize(freq);
    }

    public static EnhancedComputationMonitor createOutMonitor(String message, long freq) {
        return temporize(new PrintStreamComputationMonitor(message, null), freq);
    }

    public static EnhancedComputationMonitor createOutMonitor(String message, long freq, PrintStream out) {
        return printStream(message, out).temporize(freq);
    }

    public static EnhancedComputationMonitor temporize(ComputationMonitor baseMonitor, long freq) {
        EnhancedComputationMonitor enhanced = enhance(baseMonitor);
        if(isSilent(enhanced)){
            return enhanced;
        }
        return new FreqComputationMonitor(baseMonitor, freq);
    }

    public static EnhancedComputationMonitor printStream(String messageFormat,PrintStream printStream) {
        return new PrintStreamComputationMonitor(messageFormat, printStream);
    }

    public static EnhancedComputationMonitor logger(String messageFormat,Logger printStream) {
        return new LogComputationMonitor(messageFormat, printStream);
    }

    public static EnhancedComputationMonitor logger(Logger printStream) {
        return new LogComputationMonitor(null, printStream);
    }

    public static EnhancedComputationMonitor logger(long milliseconds) {
        return logger().temporize(milliseconds);
    }

    public static EnhancedComputationMonitor logger() {
        return new LogComputationMonitor(null, null);
    }

    public static EnhancedComputationMonitor logger(String messageFormat,TLog printStream) {
        return new TLogComputationMonitor(messageFormat, printStream);
    }
    public static EnhancedComputationMonitor logger(TLog printStream) {
        return new TLogComputationMonitor(null, printStream);
    }

    public static EnhancedComputationMonitor out(String messageFormat) {
        return printStream(messageFormat, System.out);
    }

    public static EnhancedComputationMonitor out() {
        return printStream(null, System.out);
    }

    public static EnhancedComputationMonitor err() {
        return printStream(null, System.err);
    }

    public static EnhancedComputationMonitor err(String messageFormat) {
        return printStream(null, System.err);
    }

    public static EnhancedComputationMonitor enhance(ComputationMonitor monitor) {
        if (monitor == null) {
            return none();
        }
        if (monitor instanceof EnhancedComputationMonitor) {
            return (EnhancedComputationMonitor) monitor;
        }
        return new EnhancedComputationMonitorAdapter(monitor);
    }

    public static <T> T invokeMonitoredAction(ComputationMonitor mon, String messagePrefix, MonitoredAction<T> run) {
        EnhancedComputationMonitor monitor = ComputationMonitorFactory.enhance(mon);
        monitor.start(messagePrefix + ", starting...");
        String error = null;
        T val = null;
        Chronometer chronometer = Maths.chrono();
        try {
            val = run.process(monitor, messagePrefix);
        } catch (RuntimeException e) {
            error = e.toString();
            if (StringUtils.isEmpty(error)) {
                error = e.getClass().getName();
            }
            throw e;
        } catch (Throwable e) {
            error = e.toString();
            if (StringUtils.isEmpty(error)) {
                error = e.getClass().getName();
            }
            throw new InvocationException(e);
        } finally {
            Chronometer time = chronometer.stop();
            if (error != null) {
                monitor.terminate(messagePrefix + ", terminated with error after {0}", time);
            } else {
                monitor.terminate(messagePrefix + ", terminated after {0}", time);
            }
        }
        return val;
    }
}
