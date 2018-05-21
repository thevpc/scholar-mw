package net.vpc.scholar.hadrumaths.plot.swings;

import java.io.File;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 9 nov. 2006 11:52:18
 */
public interface RecentFilesModel {
    public File[] getFiles();

    public void setFiles(File[] files);

    void removeFile(File file);

    void addFile(File file);

    int getMaxRecentFiles();

    void setMaxRecentFiles(int maxRecentFiles);
}
