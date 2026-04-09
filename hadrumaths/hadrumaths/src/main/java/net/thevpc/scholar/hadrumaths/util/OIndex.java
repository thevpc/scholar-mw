package net.thevpc.scholar.hadrumaths.util;

import java.util.Objects;

public class OIndex {
    private final int value;
    public static final OIndex _MINUS_1 = new OIndex(-1);
    public static final OIndex _1 = new OIndex(1);
    public static final OIndex _2 = new OIndex(2);
    public static final OIndex _3 = new OIndex(3);
    public static final OIndex _4 = new OIndex(4);

    public static OIndex of(int index) {
        switch (index) {
            case -1:
                return _MINUS_1;
            case 1:
                return _1;
            case 2:
                return _2;
            case 3:
                return _3;
            case 4:
                return _4;
        }
        return new OIndex(index);
    }

    private OIndex(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OIndex oIndex = (OIndex) o;
        return value == oIndex.value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    public int zeroIndex() {
        return value - 1;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
