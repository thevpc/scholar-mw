package net.vpc.scholar.hadruplot.libraries.simple.mesh;


class Mesh3DVector {

    double x;
    double y;
    double z;

    Mesh3DVector() {
    }

    Mesh3DVector(Mesh3DVector vector3d) {
        copy(vector3d);
    }

    Mesh3DVector(double d, double d1, double d2) {
        set(d, d1, d2);
    }

    void copy(Mesh3DVector vector3d) {
        x = vector3d.x;
        y = vector3d.y;
        z = vector3d.z;
    }

    void set(double d, double d1, double d2) {
        x = d;
        y = d1;
        z = d2;
    }

    void add(Mesh3DVector vector3d) {
        x += vector3d.x;
        y += vector3d.y;
        z += vector3d.z;
    }

    void sub(Mesh3DVector vector3d) {
        x -= vector3d.x;
        y -= vector3d.y;
        z -= vector3d.z;
    }

    void cmul(Mesh3DVector vector3d) {
        x *= vector3d.x;
        y *= vector3d.y;
        z *= vector3d.z;
    }

    double dot(Mesh3DVector vector3d) {
        return x * vector3d.x + y * vector3d.y + z * vector3d.z;
    }

    void mul(Mesh3DVector vector3d) {
        double d = x;
        double d1 = y;
        double d2 = z;
        x = d1 * vector3d.z - d2 * vector3d.y;
        y = d2 * vector3d.x - d * vector3d.z;
        z = d * vector3d.y - d1 * vector3d.x;
    }

    void mul(double d) {
        x *= d;
        y *= d;
        z *= d;
    }

    void div(double d) {
        x /= d;
        y /= d;
        z /= d;
    }

    boolean equals(Mesh3DVector vector3d) {
        if (vector3d == null)
            return false;
        return x == vector3d.x && y == vector3d.y && z == vector3d.z;
    }

    double vabs() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    void normalize() {
        double d = vabs();
        x /= d;
        y /= d;
        z /= d;
    }

    double cos(Mesh3DVector vector3d) {
        return dot(vector3d) / (vabs() * vector3d.vabs());
    }

    double sin(Mesh3DVector vector3d) {
        Mesh3DVector vector3d1 = new Mesh3DVector(this);
        vector3d1.mul(vector3d);
        return vector3d1.vabs() / (vabs() * vector3d.vabs());
    }

    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }
}
