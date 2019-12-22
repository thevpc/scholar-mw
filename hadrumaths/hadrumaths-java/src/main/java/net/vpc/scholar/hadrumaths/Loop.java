package net.vpc.scholar.hadrumaths;

interface Loop {
    void reset();

    void next();

    boolean hasNext();

    Object get();
}
