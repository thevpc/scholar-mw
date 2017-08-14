package net.vpc.scholar.hadrumaths.util;

import net.vpc.scholar.hadrumaths.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.Maths;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by vpc on 5/14/17.
 */
public class ProgressMonitorOutputStream extends OutputStream {
    private OutputStream target;
    private long written;
    private EnhancedProgressMonitor monitor;
    private String message;

    public ProgressMonitorOutputStream(OutputStream target, ProgressMonitor monitor, String messagePrefix) {
        this.target = target;
        this.monitor = ProgressMonitorFactory.enhance(monitor);
        message=StringUtils.trim(messagePrefix);
        if(message.length()>0){
            message+=" ";
        }
        message+=", written {0}";
    }

    @Override
    public void write(int b) throws IOException {
        target.write(b);
        written++;
        monitor.setProgress(-1,message, Maths.formatMemory(written));
    }

    @Override
    public void write(byte[] b) throws IOException {
        target.write(b);
        written+=b.length;
        monitor.setProgress(-1,message, Maths.formatMemory(written));
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        target.write(b, off, len);
        written+=len;
        monitor.setProgress(-1,message, Maths.formatMemory(written));
    }

    @Override
    public void close() throws IOException {
        super.close();
        target.close();
    }
}
