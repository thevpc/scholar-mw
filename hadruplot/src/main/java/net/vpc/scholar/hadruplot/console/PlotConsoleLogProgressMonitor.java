package net.vpc.scholar.hadruplot.console;

import net.vpc.common.mon.AbstractProgressMonitor;
import net.vpc.common.mon.TaskMessage;
import net.vpc.common.util.DoubleFormat;

import java.util.Date;

public class PlotConsoleLogProgressMonitor extends AbstractProgressMonitor {
    private double progress;
    private String messageFormat;
    private ConsoleLogger writer;


    /**
     * %value%
     * %date%
     *
     * @param messageFormat
     */
    public PlotConsoleLogProgressMonitor(String messageFormat, ConsoleLogger writer) {
        super(nextId());
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

    public void setMessageImpl(TaskMessage message) {
        DoubleFormat sdf = PlotConfigManager.Config.dblformat("%");
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

    public void setProgressImpl(double progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "TLog(" +
                "value=" + getProgressValue() +
                "," + writer + '}';
    }
}
