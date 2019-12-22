package net.vpc.scholar.hadrumaths.io;

import java.io.*;
import java.util.Base64;

public class Base64StringSerializer implements StringSerializer {
    public static final Base64StringSerializer INSTANCE = new Base64StringSerializer();
    public static final String PREFIX = "base64://";

//    public static void main(String[] args) {
//        try {
//            String e = HadrumathsIOUtils.serializeObjectToString(19.3);
//            System.out.println(e);
//            System.out.println(HadrumathsIOUtils.deserializeObjectToString(e));
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//    }

    @Override
    public int acceptSerialize(Object value) {
        return (value instanceof Serializable) ? 1 : 0;
    }

    @Override
    public boolean acceptDeserialize(String value) {
        return value.startsWith(PREFIX);
    }

    @Override
    public String serialize(Object value) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bytes);
            oos.writeObject(value);
            oos.close();
        } finally {
            if (oos != null) oos.close();
        }
        return PREFIX + new String(Base64.getEncoder().encode(bytes.toByteArray()));
    }

    @Override
    public Object deserialize(String value) throws IOException {
        if (value == null || !value.startsWith(PREFIX)) {
            throw new IllegalArgumentException("Invalid format");
        }
        Object o = null;
        ByteArrayInputStream bytes = new ByteArrayInputStream(Base64.getDecoder().decode(value.substring(PREFIX.length()).getBytes()));
        ObjectInputStream oos = null;
        try {
            oos = new ObjectInputStream(bytes);
            try {
                o = oos.readObject();
            } catch (ClassNotFoundException e) {
                throw new IOException(e);
            }
            oos.close();
        } finally {
            if (oos != null) oos.close();
        }
        return o;
    }
}
