package net.thevpc.scholar.hadrumaths.util;

public interface UnsafeHandler {
    static UnsafeHandler get(){
        return SafeUnsafe.INSTANCE;
    }

    int sizeOf(Class src);
}
