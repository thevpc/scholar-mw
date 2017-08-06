package net.vpc.scholar.hadrumaths.util;

public final class ClassPair {
    private Class first;
    private Class second;

    public ClassPair(Class first, Class second) {
        this.first = first;
        this.second = second;
    }

    public Class getFirst() {
        return first;
    }

    public Class getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassPair)) return false;

        ClassPair classPair = (ClassPair) o;

        if (first != null ? !first.equals(classPair.first) : classPair.first != null) return false;
        if (second != null ? !second.equals(classPair.second) : classPair.second != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = first != null ? first.getName().hashCode() : 0;
        result = 31 * result + (second != null ? second.getName().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ClassPair{" +
                "" + first +
                ", " + second +
                '}';
    }
}
