package net.thevpc.scholar.hadruplot.console;

import net.thevpc.common.mon.AbstractProgressMonitor;
import net.thevpc.common.util.DoubleFormat;

import java.util.Date;
import net.thevpc.common.msg.Message;

public class PlotConsoleLogProgressMonitor extends AbstractProgressMonitor {
    private double progress;
    private String messageFormat;
    private ConsoleLogger writer;
    private Message message=EMPTY_MESSAGE;


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

    public double getProgress() {
        return progress;
    }

    public void setMessageImpl(Message message) {
        this.message=message;
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

    @Override
    public Message getMessage() {
        return message;
    }

    public void setProgressImpl(double progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "TLog(" +
                "value=" + getProgress() +
                "," + writer + '}';
    }
}
