package net.thevpc.scholar.hadrumaths.srv;

public interface HadrumathsServer {
    boolean isStarted();

    void stop();

    void start();

    void close();
}
