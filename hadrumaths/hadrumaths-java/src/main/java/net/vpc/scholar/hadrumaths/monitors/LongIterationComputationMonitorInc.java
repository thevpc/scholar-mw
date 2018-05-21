package net.vpc.scholar.hadrumaths.monitors;

import net.vpc.scholar.hadrumaths.DoubleFormatter;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.MemorySizeFormatter;
import net.vpc.scholar.hadrumaths.util.*;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author taha.bensalah@gmail.com on 7/22/16.
 */
public class LongIterationComputationMonitorInc implements ComputationMonitorInc {
    private double max;
    private long index;

    public LongIterationComputationMonitorInc(long max) {
        this.max = max;
    }

    @Override
    public double inc(double last) {
        index++;
        return index / max;
    }

    public static class LogProgressMonitor extends AbstractEnhancedProgressMonitor {
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

    public static class PercentDoubleFormatter implements DoubleFormatter {
        public static DoubleFormatter INSTANCE = new PercentDoubleFormatter();

        DecimalFormat format;
        DecimalFormat simpleFormat;

        public PercentDoubleFormatter() {
            format = new DecimalFormat("00.00%");
            format.setMaximumIntegerDigits(1);
            simpleFormat = new DecimalFormat("00.00%");
        }

        @Override
        public String formatDouble(double value) {
            if (Double.isNaN(value)) {
                return ("NaN");
            } else {
                DecimalFormat f = format;
                if ((value >= 1E-3 && value <= 1E4) || (value <= -1E-3 && value >= -1E4)) {
                    f = simpleFormat;
                }
                String v = f == null ? String.valueOf(value) : f.format(value);
                if (v.endsWith("E0")) {
                    v = v.substring(0, v.length() - 2);
                }
                return v;
            }
        }
    }
}
