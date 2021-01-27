package net.thevpc.scholar.hadruplot.libraries.simple.mesh;


class BoundingBox {

    Mesh3DVector min;
    Mesh3DVector max;

    BoundingBox(Mesh3DVector[] avector3d, int i) {
        if (i <= 0)
            return;
        Mesh3DVector vector3d = avector3d[0];
        double d = vector3d.x;
        double d1 = d;
        double d2 = vector3d.y;
        double d3 = d2;
        double d4 = vector3d.z;
        double d5 = d4;
        for (int j = 1; j < i; j++) {
            Mesh3DVector vector3d1 = avector3d[j];
            double d6 = vector3d1.x;
            if (d6 < d)
                d = d6;
            if (d6 > d1)
                d1 = d6;
            double d7 = vector3d1.y;
            if (d7 < d2)
                d2 = d7;
            if (d7 > d3)
                d3 = d7;
            double d8 = vector3d1.x;
            if (d8 < d4)
                d4 = d8;
            if (d8 > d5)
                d5 = d8;
        }

        min = new Mesh3DVector(d, d2, d4);
        max = new Mesh3DVector(d1, d3, d5);
    }

    void combine(BoundingBox boundingbox) {
        Mesh3DVector vector3d = boundingbox.min;
        if (vector3d.x < min.x)
            min.x = vector3d.x;
        if (vector3d.y < min.y)
            min.y = vector3d.y;
        if (vector3d.z < min.z)
            min.z = vector3d.z;
        vector3d = boundingbox.max;
        if (vector3d.x > max.x)
            max.x = vector3d.x;
        if (vector3d.y > max.y)
            max.y = vector3d.y;
        if (vector3d.z > max.z)
            max.z = vector3d.z;
    }

    Mesh3DVector getCenter() {
        double d = (min.x + max.x) / 2D;
        double d1 = (min.y + max.y) / 2D;
        double d2 = (min.z + max.z) / 2D;
        return new Mesh3DVector(d, d1, d2);
    }

    double getWidth() {
        return max.x - min.x;
    }

    double getHeight() {
        return max.y - min.y;
    }

    double getDepth() {
        return max.z - min.z;
    }

    public double getMinSize() {
        double d = Math.min(getWidth(), getHeight());
        d = Math.min(d, getDepth());
        return d;
    }

    public double getMaxSize() {
        double d = Math.max(getWidth(), getHeight());
        d = Math.max(d, getDepth());
        return d;
    }
}
