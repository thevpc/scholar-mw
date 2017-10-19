package net.vpc.scholar.hadrumaths.util;

import java.io.IOException;

public interface StringSerializer {
    int acceptSerialize(Object value);

    boolean acceptDeserialize(String value);

    String serialize(Object value) throws IOException;

    Object deserialize(String value) throws IOException;
}
