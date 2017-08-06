package net.vpc.scholar.hadrumaths.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.*;

/**
 * @author Taha BEN SALAH <taha.bensalah@gmail.com>
 * @creationdate 9/16/12 10:00 PM
 */
public class LogUtils {
    private static final Logger log = Logger.getLogger(LogUtils.class.getName());
    private static final String NET_VPC_SCHOLAR = "";
    public static final LogFormatter LOG_FORMATTER_1 = new LogFormatter(true);
    public static final LogFormatter LOG_FORMATTER_2 = new LogFormatter(false);
    private static final Filter DEFAULT_FILTER = new Filter() {
        @Override
        public boolean isLoggable(LogRecord record) {
            return true;
//            String loggerName = record == null ? "" : StringUtils.trim(record.getLoggerName());
//            return loggerName.startsWith("net.vpc.scholar");
        }
    };

    static {
        try {
            Logger rootLogger = Logger.getLogger(NET_VPC_SCHOLAR);
            for (Handler handler : rootLogger.getHandlers()) {
                rootLogger.removeHandler(handler);
            }
            rootLogger.addHandler(new ConsoleHandler());
            setLevel(Level.CONFIG);
            log.log(Level.CONFIG, "Initializing Maths Log sub-system...");
        } catch (IOException e) {
            System.err.println("Unable to configure log");
            e.printStackTrace();
        }
    }

    public static void initialize() {
        //do nothing by
    }

    public static void prepare(Level level, String pattern, int maxSize, int count) throws IOException {
        setLogFile(pattern, maxSize, count);
        setLevel(level);
    }

    public static void setLogFile(String pattern, int maxSize, int count) throws IOException {
        Logger rootLogger = Logger.getLogger(NET_VPC_SCHOLAR);
        int MEGA = 1024 * 1024;
        if (StringUtils.trimToNull(pattern) == null) {
            pattern = "net-vpc-scholar-math-%g.log";
        }
        pattern=IOUtils.expandPath(pattern);
        if (maxSize <= 0) {
            maxSize = 5;
        }
        if (count <= 0) {
            count = 3;
        }
        if (pattern.contains("/")) {
            new File(pattern.substring(0, pattern.lastIndexOf('/'))).mkdirs();
        }
        for (Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }
        rootLogger.addHandler(new ConsoleHandler());
        rootLogger.addHandler(new FileHandler(pattern, maxSize * MEGA, count, true));
        for (Handler handler : rootLogger.getHandlers()) {
            handler.setFormatter(LOG_FORMATTER_1);
            handler.setFilter(DEFAULT_FILTER);
        }
    }

    public static void setLevel(Level level) throws IOException {
        Logger rootLogger = Logger.getLogger(NET_VPC_SCHOLAR);
        if (level == null) {
            level = Level.WARNING;
        }
        rootLogger.setLevel(level);
        rootLogger.setFilter(DEFAULT_FILTER);
        for (Handler handler : rootLogger.getHandlers()) {
            handler.setLevel(level);
            handler.setFormatter(LOG_FORMATTER_1);
            handler.setFilter(DEFAULT_FILTER);
        }
    }

    public static final class LogFormatter extends Formatter {

        private static final String LINE_SEPARATOR = System.getProperty("line.separator");
        private boolean sourceClassName;

        public LogFormatter(boolean sourceClassName) {
            this.sourceClassName = sourceClassName;
        }

        @Override
        public String format(LogRecord record) {
            StringBuilder sb = new StringBuilder(255);

            sb.append(new Date(record.getMillis()))
                    .append(" ")
                    .append(record.getLevel().getLocalizedName());
            if (sourceClassName) {
                sb.append(" ")
                        .append(record.getSourceClassName());
            }
            sb.append(": ")
                    .append(formatMessage(record))
                    .append(LINE_SEPARATOR);

            if (record.getThrown() != null) {
                try {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    record.getThrown().printStackTrace(pw);
                    pw.close();
                    sb.append(sw.toString());
                } catch (Exception ex) {
                    // ignore
                }
            }

            return sb.toString();
        }
    }
}
