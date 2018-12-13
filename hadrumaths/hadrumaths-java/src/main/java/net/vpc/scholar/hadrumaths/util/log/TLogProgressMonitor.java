package net.vpc.scholar.hadrumaths.util.log;

import net.vpc.common.util.DoubleFormat;
import net.vpc.common.util.mon.AbstractProgressMonitor;
import net.vpc.common.util.mon.ProgressMessage;
import net.vpc.scholar.hadrumaths.Maths;
import java.util.Date;

public class TLogProgressMonitor extends AbstractProgressMonitor {
    private double progress;
    private ProgressMessage message;
    private String messageFormat;
    private TLog writer;


    /**
     * %value%
     * %date%
     *
     * @param messageFormat
     */
    public TLogProgressMonitor(String messageFormat, TLog writer) {
        if (messageFormat == null) {
            messageFormat = "%value%";
        }
        if (messageFormat.indexOf("%value%") < 0) {
            messageFormat = messageFormat + " %value%";
        }
        this.messageFormat = messageFormat;
        if (writer == null) {
            writer = TLogNull.SILENT;
        }
        this.writer = writer;
    }

    public double getProgressValue() {
        return progress;
    }

    public void setProgressImpl(double progress, ProgressMessage message) {
        DoubleFormat sdf = Maths.Config.getPercentFormat();
        this.progress = progress;
        this.message = message;
        long newd = System.currentTimeMillis();
        String formattedMessage = messageFormat
                .replace("%date%", new Date(newd).toString())
                .replace("%value%", Double.isNaN(progress) ? "   ?%" : sdf.formatDouble(progress))
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
