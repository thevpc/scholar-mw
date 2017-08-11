package net.vpc.scholar.hadrumaths.util;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.MemorySizeFormatter;

import java.util.Date;
import java.util.logging.Logger;

public class LogProgressMonitor extends AbstractEnhancedProgressMonitor {
    private static MemorySizeFormatter MF = new MemorySizeFormatter("0K0TF");
    private static Logger defaultLog = Logger.getLogger(LogProgressMonitor.class.getName());

    static {
        defaultLog.setUseParentHandlers(false);
    }

    private double progress;
    private ProgressMessage message;
    private String messageFormat;
    private Logger logger;

    /**
     * %value%
     * %date%
     *
     * @param messageFormat
     */
    public LogProgressMonitor(String messageFormat, Logger logger) {
        if (messageFormat == null) {
            messageFormat = "%inuse-mem% | %free-mem% : %value%";
        }
        if (messageFormat.indexOf("%value%") < 0) {
            messageFormat = messageFormat + " %value%";
        }
        this.messageFormat = messageFormat;
        if (logger == null) {
            logger = getDefaultLogger();
        }
        this.logger = logger;
    }

    public static Logger getDefaultLogger() {
        return defaultLog;
    }

    public double getProgressValue() {
        return progress;
    }

    public void setProgressImpl(double progress, ProgressMessage message) {
        this.progress = progress;
        this.message = message;
        long newd = System.currentTimeMillis();
        String msg = messageFormat
                .replace("%date%", new Date(newd).toString())
                .replace("%value%", Double.isNaN(progress)?"   ?%":StringUtils.PERCENT_FORMAT.format(progress))
                .replace("%inuse-mem%", MF.format(Maths.inUseMemory()))
                .replace("%free-mem%", MF.format(Maths.maxFreeMemory()))
                + " " + message;
        logger.log(message.getLevel(), msg);
    }

    @Override
    public ProgressMessage getProgressMessage() {
        return message;
    }

    @Override
    public String toString() {
        return logger + "(" + getProgressValue() + ")";
    }

}
