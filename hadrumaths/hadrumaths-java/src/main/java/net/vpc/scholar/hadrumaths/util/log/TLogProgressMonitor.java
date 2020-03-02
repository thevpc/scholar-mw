package net.vpc.scholar.hadrumaths.util.log;

import net.vpc.common.mon.AbstractProgressMonitor;
import net.vpc.common.mon.TaskMessage;
import net.vpc.common.util.DoubleFormat;
import net.vpc.scholar.hadrumaths.Maths;

import java.util.Date;

public class TLogProgressMonitor extends AbstractProgressMonitor {
    private final String messageFormat;
    private final TLog writer;


    /**
     * %value%
     * %date%
     *
     * @param messageFormat
     */
    public TLogProgressMonitor(String messageFormat, TLog writer) {
        super(nextId());
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

    public void setMessageImpl(TaskMessage message) {
        DoubleFormat sdf = Maths.Config.getPercentFormat();
        long newd = System.currentTimeMillis();
        String formattedMessage = messageFormat
                .replace("%date%", new Date(newd).toString())
                .replace("%value%", Double.isNaN(getProgressValue()) ? "   ?%" : sdf.formatDouble(getProgressValue()))
                + " " + message;
        writer.trace(formattedMessage);
    }

    @Override
    public String toString() {
        return "TLog(" +
                "value=" + getProgressValue() +
                "," + writer + '}';
    }
}
