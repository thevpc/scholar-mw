package net.vpc.scholar.hadrumaths.srv;

import java.io.File;
import java.util.List;

public interface HadrumathsClient extends AutoCloseable {

    List<String> list(String path);

    FileStat stat(String path);

    void get(String path, File writeTo);

    /**
     *
     * @return -1 if no route, else time for ping
     */
    long ping();

    void close();
}
