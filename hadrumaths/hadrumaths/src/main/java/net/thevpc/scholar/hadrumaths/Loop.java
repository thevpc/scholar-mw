package net.thevpc.scholar.hadrumaths;

interface Loop {
    void reset();

    void next();

    boolean hasNext();

    Object get();
}
