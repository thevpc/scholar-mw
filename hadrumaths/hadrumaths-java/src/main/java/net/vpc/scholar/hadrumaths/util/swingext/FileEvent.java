package net.vpc.scholar.hadrumaths.util.swingext;

import java.util.EventObject;
import java.io.File;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 9 nov. 2006 15:25:46
 */
public class FileEvent extends EventObject {
    private File file;

    public FileEvent(Object source, File file) {
        super(source);
        this.file = file;
    }


    public File getFile() {
        return file;
    }
}
