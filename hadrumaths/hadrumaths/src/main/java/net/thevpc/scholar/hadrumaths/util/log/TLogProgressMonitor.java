package net.thevpc.scholar.hadrumaths.util.log;

import net.thevpc.common.mon.AbstractProgressMonitor;
import net.thevpc.common.util.DoubleFormat;
import net.thevpc.scholar.hadrumaths.Maths;

import java.util.Date;
import net.thevpc.common.msg.Message;

public class TLogProgressMonitor extends AbstractProgressMonitor {
    private final String messageFormat;
    private final TLog writer;
    private double progress=Double.NaN;
    private Message message=EMPTY_MESSAGE;


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

    public void setMessageImpl(Message message) {
        this.message=message;
        DoubleFormat sdf = Maths.Config.getPercentFormat();
        long newd = System.currentTimeMillis();
        String formattedMessage = messageFormat
                .replace("%date%", new Date(newd).toString())
                .replace("%value%", Double.isNaN(getProgress()) ? "   ?%" : sdf.formatDouble(getProgress()))
                + " " + message;
        writer.trace(formattedMessage);
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    protected void setProgressImpl(double progress) {
        this.progress=progress;
    }

    @Override
    public double getProgress() {
        return progress;
    }

    @Override
    public String toString() {
        return "TLog(" +
                "value=" + getProgress() +
                "," + writer + '}';
    }
}
