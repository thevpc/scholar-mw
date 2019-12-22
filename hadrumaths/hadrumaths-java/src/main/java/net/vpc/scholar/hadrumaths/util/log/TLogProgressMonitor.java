package net.vpc.scholar.hadrumaths.util.log;

import net.vpc.common.util.DoubleFormat;
import net.vpc.common.mon.BaseProgressMonitor;
import net.vpc.common.mon.ProgressMessage;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.MathsBase;

import java.util.Date;

public class TLogProgressMonitor extends BaseProgressMonitor {
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
        DoubleFormat sdf = MathsBase.Config.getPercentFormat();
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
