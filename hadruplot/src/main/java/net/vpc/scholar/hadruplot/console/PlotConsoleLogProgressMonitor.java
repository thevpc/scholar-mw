package net.vpc.scholar.hadruplot.console;

import net.vpc.common.util.DoubleFormat;
import net.vpc.common.mon.BaseProgressMonitor;
import net.vpc.common.mon.ProgressMessage;

import java.util.Date;

public class PlotConsoleLogProgressMonitor extends BaseProgressMonitor {
    private double progress;
    private ProgressMessage message;
    private String messageFormat;
    private ConsoleLogger writer;


    /**
     * %value%
     * %date%
     *
     * @param messageFormat
     */
    public PlotConsoleLogProgressMonitor(String messageFormat, ConsoleLogger writer) {
        if (messageFormat == null) {
            messageFormat = "%value%";
        }
        if (messageFormat.indexOf("%value%") < 0) {
            messageFormat = messageFormat + " %value%";
        }
        this.messageFormat = messageFormat;
        if (writer == null) {
            writer = NullConsoleLogger.INSTANCE;
        }
        this.writer = writer;
    }

    public double getProgressValue() {
        return progress;
    }

    public void setProgressImpl(double progress, ProgressMessage message) {
        DoubleFormat sdf = PlotConfigManager.Config.dblformat("%");
        this.progress = progress;
        this.message = message;
        long newd = System.currentTimeMillis();
        String formattedMessage = messageFormat
                .replace("%date%", new Date(newd).toString())
                .replace("%value%", Double.isNaN(progress) ? "   ?%" :
                        sdf==null?String.valueOf(progress):sdf.formatDouble(progress)
                )
                + " " + message;
        if (progress <= 0 || progress >= 1) {
            writer.trace(formattedMessage);
        } else {
            writer.debug(formattedMessage);
        }
    }

    @Override
    public ProgressMessage getProgressMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "TLog(" +
                "value=" + getProgressValue() +
                "," + writer + '}';
    }
}
