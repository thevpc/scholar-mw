package net.thevpc.scholar.hadrumaths.cache;




import net.thevpc.nuts.elem.NElement;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;

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
        return of(NElement.ofUplet(name, NElementHelper.elem(elements)));
    }

    public static CacheKey of(NElement element) {
        return new CacheKey(NElementHelper.elem(element).toString(false));
    }

    public static CacheKey obj(String name, String a1, Object v1) {
        return of(NElement.ofObjectBuilder(name).add(a1, NElementHelper.elem(v1)));
    }

    public static CacheKey obj(String name, String a1, Object v1, String a2, Object v2) {

        return of(NElement.ofObjectBuilder(name)
                .add(a1, NElementHelper.elem(v1))
                .add(a2, NElementHelper.elem(v2))
        );
    }

    public static CacheKey obj(String name, String a1, Object v1, String a2, Object v2, String a3, Object v3) {

        return of(NElement.ofObjectBuilder(name)
                .add(a1, NElementHelper.elem(v1))
                .add(a2, NElementHelper.elem(v2))
                .add(a3, NElementHelper.elem(v3))
        );
    }

    public static CacheKey obj(String name, String a1, Object v1, String a2, Object v2, String a3, Object v3, String a4, Object v4) {

        return of(NElement.ofObjectBuilder(name)
                .add(a1, NElementHelper.elem(v1))
                .add(a2, NElementHelper.elem(v2))
                .add(a3, NElementHelper.elem(v3))
                .add(a4, NElementHelper.elem(v4))
        );
    }

    public static CacheKey obj(String name, String a1, Object v1, String a2, Object v2, String a3, Object v3, String a4, Object v4, String a5, Object v5) {

        return of(NElement.ofObjectBuilder(name)
                .add(a1, NElementHelper.elem(v1))
                .add(a2, NElementHelper.elem(v2))
                .add(a3, NElementHelper.elem(v3))
                .add(a4, NElementHelper.elem(v4))
                .add(a5, NElementHelper.elem(v5))
        );
    }

    public static CacheKey obj(String name, String a1, Object v1, String a2, Object v2, String a3, Object v3, String a4, Object v4, String a5, Object v5, String a6, Object v6) {

        return of(NElement.ofObjectBuilder(name)
                .add(a1, NElementHelper.elem(v1))
                .add(a2, NElementHelper.elem(v2))
                .add(a3, NElementHelper.elem(v3))
                .add(a4, NElementHelper.elem(v4))
                .add(a5, NElementHelper.elem(v5))
                .add(a6, NElementHelper.elem(v6))
        );
    }

    public static CacheKey obj(String name, String a1, Object v1, String a2, Object v2, String a3, Object v3, String a4, Object v4, String a5, Object v5, String a6, Object v6, String a7, Object v7) {

        return of(NElement.ofObjectBuilder(name)
                .add(a1, NElementHelper.elem(v1))
                .add(a2, NElementHelper.elem(v2))
                .add(a3, NElementHelper.elem(v3))
                .add(a4, NElementHelper.elem(v4))
                .add(a5, NElementHelper.elem(v5))
                .add(a6, NElementHelper.elem(v6))
                .add(a7, NElementHelper.elem(v7))
        );
    }

    public static CacheKey obj(String name, String a1, Object v1, String a2, Object v2, String a3, Object v3, String a4, Object v4, String a5, Object v5
            , String a6, Object v6, String a7, Object v7, String a8, Object v8) {

        return of(NElement.ofObjectBuilder(name)
                .add(a1, NElementHelper.elem(v1))
                .add(a2, NElementHelper.elem(v2))
                .add(a3, NElementHelper.elem(v3))
                .add(a4, NElementHelper.elem(v4))
                .add(a5, NElementHelper.elem(v5))
                .add(a6, NElementHelper.elem(v6))
                .add(a7, NElementHelper.elem(v7))
                .add(a8, NElementHelper.elem(v8))
        );
    }

    public static CacheKey obj(String name, String a1, Object v1, String a2, Object v2, String a3, Object v3, String a4, Object v4, String a5, Object v5
            , String a6, Object v6, String a7, Object v7, String a8, Object v8, String a9, Object v9) {

        return of(NElement.ofObjectBuilder(name)
                .add(a1, NElementHelper.elem(v1))
                .add(a2, NElementHelper.elem(v2))
                .add(a3, NElementHelper.elem(v3))
                .add(a4, NElementHelper.elem(v4))
                .add(a5, NElementHelper.elem(v5))
                .add(a6, NElementHelper.elem(v6))
                .add(a7, NElementHelper.elem(v7))
                .add(a8, NElementHelper.elem(v8))
                .add(a9, NElementHelper.elem(v9))
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
                return new CacheKey(NElementHelper.elem(elements[0]).toString(false));
            }
        }
        return new CacheKey(NElementHelper.elem(elements).toString(false));
    }

    public static CacheKey load(String string) {
        return new CacheKey(string);
    }

    public static String toHashString(Object stringToHash) {
        if (stringToHash instanceof String) {
            return toHashString((String) stringToHash);
        }
        return toHashString(NElementHelper.elem(stringToHash).toString());
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
