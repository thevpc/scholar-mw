package net.vpc.scholar.hadrumaths.monitors;

import net.vpc.scholar.hadrumaths.util.StringUtils;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 19 juil. 2007 00:27:15
 */
public class ProgressMonitorTranslator extends AbstractEnhancedProgressMonitor {

    private double start;
    private ProgressMessage message;
    private double factor;
    private ProgressMonitor baseMonitor;

    public ProgressMonitorTranslator(ProgressMonitor baseMonitor, double factor, double start) {
        if (baseMonitor == null) {
            throw new NullPointerException("baseMonitor could not be null");
        }
        this.baseMonitor = baseMonitor;
        this.factor = factor;
        this.start = start;
    }

    @Override
    public double getProgressValue() {
        double d = (baseMonitor.getProgressValue() - start) / factor;
        return d < 0 ? 0 : d > 1 ? 1 : d;
    }

    @Override
    public void setProgressImpl(double progress, ProgressMessage message) {
        this.message = message;
        double translatedProgress = Double.isNaN(progress) ? progress : (progress * factor + start);
//        double translatedProgress = (progress-start)/factor;
        if (!Double.isNaN(progress) && (translatedProgress < 0 || translatedProgress > 1)) {
            if (translatedProgress > 1 && translatedProgress < 1.1) {
                translatedProgress = 1;
            } else {
                System.err.println("ProgressMonitorTranslator : %= " + translatedProgress + "????????????");
            }
        }
        if (message != null && message instanceof StringPrefixProgressMessage) {
            message = new StringPrefixProgressMessage(
                    StringUtils.PERCENT_FORMAT.format(progress) + " " +
                            ((StringPrefixProgressMessage) message).getPrefix(),
                    ((StringPrefixProgressMessage) message).getMessage()
            );
        } else {
            message = new StringPrefixProgressMessage(
                    StringUtils.PERCENT_FORMAT.format(progress) + " ",
                    message
            );
        }
        baseMonitor.setProgress(translatedProgress, message);
//        baseMonitor.setProgress((progress-start)/factor);
    }

    @Override
    public ProgressMessage getProgressMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Translator(" +
                "value=" + getProgressValue() +
                ",start=" + start +
                ", factor=" + factor +
                ", " + baseMonitor +
                ')';
    }
}
