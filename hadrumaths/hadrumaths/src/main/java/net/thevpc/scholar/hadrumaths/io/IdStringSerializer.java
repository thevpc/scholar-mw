package net.thevpc.scholar.hadrumaths.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IdStringSerializer implements StringSerializer {
    public static final IdStringSerializer INSTANCE = new IdStringSerializer();
    public static final String PREFIX = "id://";
    public final Map<String, Object> keyToObject = new HashMap<>();
    public final Map<Object, String> objectToKey = new HashMap<>();

    public void register(String key, Object object) {
        keyToObject.put(key, object);
        objectToKey.put(object, key);
    }

    @Override
    public int acceptSerialize(Object value) {
        if (value == null) {
            return 1;
        }
        String k = objectToKey.get(value);
        return k != null ? 1 : 0;
    }

    @Override
    public boolean acceptDeserialize(String value) {
        return value.startsWith("id://") || value.equals("null://");
    }

    @Override
    public String serialize(Object value) throws IOException {
        if (value == null) {
            return "null://";
        }
        String m = objectToKey.get(value);
        if (m != null) {
            return "id://" + m;
        }
        throw new IllegalArgumentException("No such Object " + value);
    }

    @Override
    public Object deserialize(String value) throws IOException {
        if (value == null) {
            throw new IllegalArgumentException("Invalid format");
        }
        if (!value.startsWith("null://") || !value.startsWith("id://")) {
            throw new IllegalArgumentException("Invalid format");
        }
        if ("null://".equals(value)) {
            return null;
        }
        Object v = keyToObject.get(value.substring(PREFIX.length()));
        if (v == null) {
            throw new IllegalArgumentException("No such Object " + value);
        }
        return v;
    }
}
