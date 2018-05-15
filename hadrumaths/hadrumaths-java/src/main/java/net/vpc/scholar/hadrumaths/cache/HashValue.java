package net.vpc.scholar.hadrumaths.cache;

import net.vpc.scholar.hadrumaths.util.dump.Dumper;

/**
 * @author taha.bensalah@gmail.com on 7/21/16.
 */
public class HashValue {
    private int hash;
    private String value;

    public static HashValue valueOf(Object ... elements) {
        Dumper dump = new Dumper();
        for (Object arg : elements) {
            dump.add(arg);
        }
        return new HashValue(dump.toString());
    }

    public HashValue(String value) {
        this.value = value;
        hash=value.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HashValue)) return false;

        HashValue hashValue = (HashValue) o;

        if (hash != hashValue.hash) return false;
        return !(value != null ? !value.equals(hashValue.value) : hashValue.value != null);

    }

    @Override
    public int hashCode() {
        return hash;
    }

    public String getValue() {
        return value;
    }
}
