package net.vpc.scholar.hadrumaths.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SimpleStringSerializer implements StringSerializer {
    public static final SimpleStringSerializer INSTANCE = new SimpleStringSerializer();
    public final Map<String, TT> baseStr = new HashMap<>();
    public final Map<Class, TT> baseCls = new HashMap<>();

    public SimpleStringSerializer() {
        register(new TT(String.class, "string") {
            @Override
            String serialize(Object value) throws IOException {
                return value.toString();
            }

            @Override
            Object deserialize(String value) throws IOException {
                return value;
            }
        });
        register(new TT(Integer.class, "int") {
            @Override
            String serialize(Object value) throws IOException {
                return value.toString();
            }

            @Override
            Object deserialize(String value) throws IOException {
                return Integer.parseInt(value);
            }
        });
        register(new TT(Long.class, "long") {
            @Override
            String serialize(Object value) throws IOException {
                return value.toString();
            }

            @Override
            Object deserialize(String value) throws IOException {
                return Long.parseLong(value);
            }
        });
        register(new TT(Double.class, "double") {
            @Override
            String serialize(Object value) throws IOException {
                return value.toString();
            }

            @Override
            Object deserialize(String value) throws IOException {
                return Double.parseDouble(value);
            }
        });
        register(new TT(Boolean.class, "boolean") {
            @Override
            String serialize(Object value) throws IOException {
                return value.toString();
            }

            @Override
            Object deserialize(String value) throws IOException {
                return Boolean.parseBoolean(value);
            }
        });
    }

    private void register(TT t) {
        baseCls.put(t.cls, t);
        baseStr.put(t.name, t);
    }

    @Override
    public int acceptSerialize(Object value) {
        if (value == null) {
            return 1;
        }
        if (baseCls.containsKey(value.getClass())) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean acceptDeserialize(String value) {
        if (value.contains("://")) {
            String k = value.substring(0, value.indexOf("://"));
            if (k.equals("null")) {
                return true;
            }
            return baseStr.containsKey(k);
        }
        return false;
    }

    @Override
    public String serialize(Object value) throws IOException {
        if (value == null) {
            return "null://";
        }
        TT m = baseCls.get(value.getClass());
        if (m != null) {
            return m.name + "://" + m.serialize(value);
        }
        throw new IllegalArgumentException("No such Object " + value);
    }

    @Override
    public Object deserialize(String value) throws IOException {
        if (value == null) {
            throw new IllegalArgumentException("Invalid format");
        }
        if ("null://".equals(value)) {
            return null;
        }
        if (value.contains("://")) {
            String k = value.substring(0, value.indexOf("://"));
            TT ii = baseStr.get(k);
            if (ii != null) {
                return ii.deserialize(value.substring(k.length() + 3));
            }
        }
        throw new IllegalArgumentException("No such Object " + value);
    }

    abstract class TT {
        Class cls;
        String name;

        public TT(Class cls, String name) {
            this.cls = cls;
            this.name = name;
        }

        abstract String serialize(Object value) throws IOException;

        abstract Object deserialize(String value) throws IOException;
    }
}
