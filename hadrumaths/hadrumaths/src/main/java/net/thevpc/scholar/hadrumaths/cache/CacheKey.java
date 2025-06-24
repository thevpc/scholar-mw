package net.thevpc.scholar.hadrumaths.cache;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElementBase;
import net.thevpc.tson.TsonSerializer;
import net.thevpc.scholar.hadrumaths.Maths;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by vpc on 5/30/14.
 */
public class CacheKey {
    private final String dump;
    private final String path;
    private final int hashCode;


    private CacheKey(String dump) {
        this.dump = dump;
        this.path = toPath(dump);
        this.hashCode = this.path.hashCode();
    }

    private static String toPath(String stringToHash) {
        String hh = toHashString(stringToHash);
        int qq = 8;
        StringBuilder sb = new StringBuilder();
        int j = 0;
        int hhLength = hh.length();
        for (int i = 0; i < hhLength; i++) {
            if (j == qq) {
                sb.append("/");
                j = 0;
            }
            sb.append(hh.charAt(i));
            j++;
        }
        return sb.toString();
    }

    public static String toHashString(String stringToHash) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return toPath0(stringToHash);
        }
        messageDigest.update(stringToHash.getBytes());
        byte[] digiest = messageDigest.digest();
        String hh = convertByteToHex(digiest);
        return hh;
    }

    public static String toPath0(String dump) {
        String hh = Integer.toString(dump.hashCode(), 36).toLowerCase();
        if (hh.startsWith("-")) {
            hh = hh.substring(1);
        }
        StringBuilder sb = new StringBuilder();
        int j = 0;
        for (int i = 0; i < hh.length(); i++) {
            if (j == 2) {
                sb.append("/");
                j = 0;
            }
            sb.append(hh.charAt(i));
            j++;
        }
        return sb.toString();
    }

    private static String convertByteToHex(byte[] byteData) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static CacheKey fct(String name, Object... elements) {
        return of(Tson.ofUplet(name, Tson.serializer().serializeArray(elements)));
    }

    public static CacheKey of(TsonElementBase element) {
        return new CacheKey(Maths.Config.getTsonSerializer().serialize(element).toString(false));
    }

    public static CacheKey obj(String name, String a1, Object v1) {
        TsonSerializer s = Tson.serializer();
        return of(Tson.ofObjectBuilder(name).add(a1, s.serialize(v1)));
    }

    public static CacheKey obj(String name, String a1, Object v1, String a2, Object v2) {
        TsonSerializer s = Tson.serializer();
        return of(Tson.ofObjectBuilder(name)
                .add(a1, s.serialize(v1))
                .add(a2, s.serialize(v2))
        );
    }

    public static CacheKey obj(String name, String a1, Object v1, String a2, Object v2, String a3, Object v3) {
        TsonSerializer s = Tson.serializer();
        return of(Tson.ofObjectBuilder(name)
                .add(a1, s.serialize(v1))
                .add(a2, s.serialize(v2))
                .add(a3, s.serialize(v3))
        );
    }

    public static CacheKey obj(String name, String a1, Object v1, String a2, Object v2, String a3, Object v3, String a4, Object v4) {
        TsonSerializer s = Tson.serializer();
        return of(Tson.ofObjectBuilder(name)
                .add(a1, s.serialize(v1))
                .add(a2, s.serialize(v2))
                .add(a3, s.serialize(v3))
                .add(a4, s.serialize(v4))
        );
    }

    public static CacheKey obj(String name, String a1, Object v1, String a2, Object v2, String a3, Object v3, String a4, Object v4, String a5, Object v5) {
        TsonSerializer s = Tson.serializer();
        return of(Tson.ofObjectBuilder(name)
                .add(a1, s.serialize(v1))
                .add(a2, s.serialize(v2))
                .add(a3, s.serialize(v3))
                .add(a4, s.serialize(v4))
                .add(a5, s.serialize(v5))
        );
    }

    public static CacheKey obj(String name, String a1, Object v1, String a2, Object v2, String a3, Object v3, String a4, Object v4, String a5, Object v5, String a6, Object v6) {
        TsonSerializer s = Tson.serializer();
        return of(Tson.ofObjectBuilder(name)
                .add(a1, s.serialize(v1))
                .add(a2, s.serialize(v2))
                .add(a3, s.serialize(v3))
                .add(a4, s.serialize(v4))
                .add(a5, s.serialize(v5))
                .add(a6, s.serialize(v6))
        );
    }

    public static CacheKey obj(String name, String a1, Object v1, String a2, Object v2, String a3, Object v3, String a4, Object v4, String a5, Object v5, String a6, Object v6, String a7, Object v7) {
        TsonSerializer s = Tson.serializer();
        return of(Tson.ofObjectBuilder(name)
                .add(a1, s.serialize(v1))
                .add(a2, s.serialize(v2))
                .add(a3, s.serialize(v3))
                .add(a4, s.serialize(v4))
                .add(a5, s.serialize(v5))
                .add(a6, s.serialize(v6))
                .add(a7, s.serialize(v7))
        );
    }

    public static CacheKey obj(String name, String a1, Object v1, String a2, Object v2, String a3, Object v3, String a4, Object v4, String a5, Object v5
            , String a6, Object v6, String a7, Object v7, String a8, Object v8) {
        TsonSerializer s = Tson.serializer();
        return of(Tson.ofObjectBuilder(name)
                .add(a1, s.serialize(v1))
                .add(a2, s.serialize(v2))
                .add(a3, s.serialize(v3))
                .add(a4, s.serialize(v4))
                .add(a5, s.serialize(v5))
                .add(a6, s.serialize(v6))
                .add(a7, s.serialize(v7))
                .add(a8, s.serialize(v8))
        );
    }

    public static CacheKey obj(String name, String a1, Object v1, String a2, Object v2, String a3, Object v3, String a4, Object v4, String a5, Object v5
            , String a6, Object v6, String a7, Object v7, String a8, Object v8, String a9, Object v9) {
        TsonSerializer s = Tson.serializer();
        return of(Tson.ofObjectBuilder(name)
                .add(a1, s.serialize(v1))
                .add(a2, s.serialize(v2))
                .add(a3, s.serialize(v3))
                .add(a4, s.serialize(v4))
                .add(a5, s.serialize(v5))
                .add(a6, s.serialize(v6))
                .add(a7, s.serialize(v7))
                .add(a8, s.serialize(v8))
                .add(a9, s.serialize(v9))
        );
    }

    public static CacheKey of(Object... elements) {
        if (elements == null) {
            throw new IllegalArgumentException("Empty Key");
        }
        switch (elements.length) {
            case 0: {
                throw new IllegalArgumentException("Empty Key");
            }
            case 1: {
                return new CacheKey(Maths.Config.getTsonSerializer().serialize(elements[0]).toString(false));
            }
        }
        return new CacheKey(Maths.Config.getTsonSerializer().serialize(elements).toString(false));
    }

    public static CacheKey load(String string) {
        return new CacheKey(string);
    }

    public static String toHashString(Object stringToHash) {
        if (stringToHash instanceof String) {
            return toHashString((String) stringToHash);
        }
        return toHashString(Tson.serializer().serialize(stringToHash).toString());
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheKey cacheKey = (CacheKey) o;
        return hashCode == cacheKey.hashCode &&
                path.equals(cacheKey.path) &&
                dump.equals(cacheKey.dump);
    }

    @Override
    public String toString() {
        return "DumpPath{" +
                "dump='" + dump + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    public String getDump() {
        return dump;
    }

    public String getPath() {
        return path;
    }

}
