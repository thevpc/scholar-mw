package net.vpc.scholar.hadrumaths.util;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.MemorySizeFormatter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class LogProgressMonitor extends AbstractEnhancedProgressMonitor {
    private static MemorySizeFormatter MF = new MemorySizeFormatter("0K0TF");
    private static Logger defaultLog = Logger.getLogger(LogProgressMonitor.class.getName());
    private static FastMessageFormat defaultFastMessageFormat = createFastMessageFormat("used=${inuse-mem} | free=${free-mem} : ${value}");

    static {
        defaultLog.setUseParentHandlers(false);
    }

    private double progress;
    private ProgressMessage message;
    private FastMessageFormat fastMessageFormat;
    private Logger logger;

    /**
     * %value%
     * %date%
     *
     * @param messageFormat
     */
    public LogProgressMonitor(String messageFormat, Logger logger) {
        if (messageFormat == null) {
            fastMessageFormat = defaultFastMessageFormat;
        } else {
            if (!messageFormat.contains("${value}")) {
                messageFormat = messageFormat + " ${value}";
            }
            fastMessageFormat = createFastMessageFormat(messageFormat);
        }


        if (logger == null) {
            logger = getDefaultLogger();
        }
        this.logger = logger;
    }

    private static FastMessageFormat createFastMessageFormat(String format) {
        FastMessageFormat fastMessageFormat = new FastMessageFormat();
        fastMessageFormat.addVar("inuse-mem", new FastMessageFormat.Evaluator() {
            @Override
            public String eval(Map<String, Object> context) {
                return MF.format(Maths.inUseMemory());
            }
        });
        fastMessageFormat.addVar("free-mem", new FastMessageFormat.Evaluator() {
            @Override
            public String eval(Map<String, Object> context) {
                return MF.format(Maths.maxFreeMemory());
            }
        });
        fastMessageFormat.addVar("date", new FastMessageFormat.Evaluator() {
            @Override
            public String eval(Map<String, Object> context) {
                return new Date().toString();
            }
        });
        fastMessageFormat.addVar("value", new FastMessageFormat.Evaluator() {
            @Override
            public String eval(Map<String, Object> context) {
                double progress = (double) context.get("progress");
                return Double.isNaN(progress) ? "   ?%" : StringUtils.PERCENT_FORMAT.format(progress);
            }
        });
        fastMessageFormat.parse(format);
        return fastMessageFormat;
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
        Map<String, Object> context = new HashMap<>();
        context.put("progress", progress);
        String msg = fastMessageFormat.format(context) + " " + message;
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
