package net.thevpc.scholar.hadrumaths;

class MathsAlgebra {
    public static long pgcd(long a, long b) {
        long r, i;
        while (b != 0) {
            r = a % b;
            a = b;
            b = r;
        }
        return a;
    }

    public static int pgcd(int a, int b) {
        int r, i;
        while (b != 0) {
            r = a % b;
            a = b;
            b = r;
        }
        return a;
    }
}
