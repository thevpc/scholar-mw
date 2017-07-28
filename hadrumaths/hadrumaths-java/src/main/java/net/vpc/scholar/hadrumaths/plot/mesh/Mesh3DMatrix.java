package net.vpc.scholar.hadrumaths.plot.mesh;


import net.vpc.scholar.hadrumaths.Maths;

class Mesh3DMatrix {

    static Mesh3DMatrix M_IDENT = new Mesh3DMatrix(
            1, 0, 0,
            0, 1, 0,
            0, 0, 1,
            0, 0, 0
    );
    double xx;
    double yx;
    double zx;
    double xy;
    double yy;
    double zy;
    double xz;
    double yz;
    double zz;
    double xo;
    double yo;
    double zo;

    public Mesh3DMatrix(
            double xx, double xy, double xz,
            double yx, double yy, double yz,
            double zx, double zy, double zz,
            double xo, double yo, double zo) {
        this.xx = xx;
        this.yx = yx;
        this.zx = zx;
        this.xy = xy;
        this.yy = yy;
        this.zy = zy;
        this.xz = xz;
        this.yz = yz;
        this.zz = zz;
        this.xo = xo;
        this.yo = yo;
        this.zo = zo;
    }

    Mesh3DMatrix() {
        unit();
    }

    Mesh3DMatrix(Mesh3DMatrix matrix3d) {
        setValues(matrix3d);
    }

    void unit() {
        xx = 1.0D;
        xy = 0.0D;
        xz = 0.0D;
        xo = 0.0D;
        yx = 0.0D;
        yy = 1.0D;
        yz = 0.0D;
        yo = 0.0D;
        zx = 0.0D;
        zy = 0.0D;
        zz = 1.0D;
        zo = 0.0D;
    }

    void setValues(Mesh3DMatrix matrix3d) {
        xx = matrix3d.xx;
        xy = matrix3d.xy;
        xz = matrix3d.xz;
        xo = matrix3d.xo;
        yx = matrix3d.yx;
        yy = matrix3d.yy;
        yz = matrix3d.yz;
        yo = matrix3d.yo;
        zx = matrix3d.zx;
        zy = matrix3d.zy;
        zz = matrix3d.zz;
        zo = matrix3d.zo;
    }

    void rotateX(double value) {
        double d1 = Maths.cos2((value * Math.PI) / 180D);
        double d2 = Maths.sin2((value * Math.PI) / 180D);
        double newyx = yx * d1 + zx * d2;
        double newyy = yy * d1 + zy * d2;
        double newyz = yz * d1 + zz * d2;
        double newy0 = yo * d1 + zo * d2;
        double newzx = zx * d1 - yx * d2;
        double newzy = zy * d1 - yy * d2;
        double newzz = zz * d1 - yz * d2;
        double newz0 = zo * d1 - yo * d2;
        yx = newyx;
        yy = newyy;
        yz = newyz;
        yo = newy0;
        zx = newzx;
        zy = newzy;
        zz = newzz;
        zo = newz0;
    }

    void rotateY(double value) {
        double d1 = Maths.cos2((value * Math.PI) / 180D);
        double d2 = Maths.sin2((value * Math.PI) / 180D);
        double d3 = xx * d1 + zx * d2;
        double d4 = xy * d1 + zy * d2;
        double d5 = xz * d1 + zz * d2;
        double d6 = xo * d1 + zo * d2;
        double d7 = zx * d1 - xx * d2;
        double d8 = zy * d1 - xy * d2;
        double d9 = zz * d1 - xz * d2;
        double d10 = zo * d1 - xo * d2;
        xx = d3;
        xy = d4;
        xz = d5;
        xo = d6;
        zx = d7;
        zy = d8;
        zz = d9;
        zo = d10;
    }

    void rotateZ(double value) {
        double d1 = Maths.cos2((value * Math.PI) / 180D);
        double d2 = Maths.sin2((value * Math.PI) / 180D);
        double d3 = yx * d1 + xx * d2;
        double d4 = yy * d1 + xy * d2;
        double d5 = yz * d1 + xz * d2;
        double d6 = yo * d1 + xo * d2;
        double d7 = xx * d1 - yx * d2;
        double d8 = xy * d1 - yy * d2;
        double d9 = xz * d1 - yz * d2;
        double d10 = xo * d1 - yo * d2;
        yx = d3;
        yy = d4;
        yz = d5;
        yo = d6;
        xx = d7;
        xy = d8;
        xz = d9;
        xo = d10;
    }

    void scale(double value) {
        xx *= value;
        xy *= value;
        xz *= value;
        xo *= value;
        yx *= value;
        yy *= value;
        yz *= value;
        yo *= value;
        zx *= value;
        zy *= value;
        zz *= value;
        zo *= value;
    }

    void scale(double vx, double vy, double vz) {
        xx *= vx;
        xy *= vx;
        xz *= vx;
        xo *= vx;
        yx *= vy;
        yy *= vy;
        yz *= vy;
        yo *= vy;
        zx *= vz;
        zy *= vz;
        zz *= vz;
        zo *= vz;
    }

    void translate(double vx, double vy, double vz) {
        xo += vx;
        yo += vy;
        zo += vz;
    }

    void mul(Mesh3DMatrix matrix3d) {
        double d = xx * matrix3d.xx + yx * matrix3d.xy + zx * matrix3d.xz;
        double d1 = xy * matrix3d.xx + yy * matrix3d.xy + zy * matrix3d.xz;
        double d2 = xz * matrix3d.xx + yz * matrix3d.xy + zz * matrix3d.xz;
        double d3 = xo * matrix3d.xx + yo * matrix3d.xy + zo * matrix3d.xz + matrix3d.xo;
        double d4 = xx * matrix3d.yx + yx * matrix3d.yy + zx * matrix3d.yz;
        double d5 = xy * matrix3d.yx + yy * matrix3d.yy + zy * matrix3d.yz;
        double d6 = xz * matrix3d.yx + yz * matrix3d.yy + zz * matrix3d.yz;
        double d7 = xo * matrix3d.yx + yo * matrix3d.yy + zo * matrix3d.yz + matrix3d.yo;
        double d8 = xx * matrix3d.zx + yx * matrix3d.zy + zx * matrix3d.zz;
        double d9 = xy * matrix3d.zx + yy * matrix3d.zy + zy * matrix3d.zz;
        double d10 = xz * matrix3d.zx + yz * matrix3d.zy + zz * matrix3d.zz;
        double d11 = xo * matrix3d.zx + yo * matrix3d.zy + zo * matrix3d.zz + matrix3d.zo;
        xx = d;
        xy = d1;
        xz = d2;
        xo = d3;
        yx = d4;
        yy = d5;
        yz = d6;
        yo = d7;
        zx = d8;
        zy = d9;
        zz = d10;
        zo = d11;
    }

    void transform(Mesh3DVector vect1, Mesh3DVector vect2) {
        double v1x = vect1.x;
        double v1y = vect1.y;
        double v1z = vect1.z;
        vect2.x = v1x * xx + v1y * xy + v1z * xz + xo;
        vect2.y = v1x * yx + v1y * yy + v1z * yz + yo;
        vect2.z = v1x * zx + v1y * zy + v1z * zz + zo;
    }

    void transform(Mesh3DVector[] avector3d, Mesh3DVector[] avector3d1, int i) {
        for (int j = 0; j < i; j++) {
            Mesh3DVector vector3d = avector3d[j];
            double d = vector3d.x;
            double d1 = vector3d.y;
            double d2 = vector3d.z;
            vector3d = avector3d1[j];
            vector3d.x = d * xx + d1 * xy + d2 * xz + xo;
            vector3d.y = d * yx + d1 * yy + d2 * yz + yo;
            vector3d.z = d * zx + d1 * zy + d2 * zz + zo;
        }

    }

    public String toString() {
        return "[" + xx + ", " + yx + ", " + zx + "]\n" + "[" + xy + ", " + yy + ", " + zy + "]\n" + "[" + xz + ", " + yz + ", " + zz + "]\n" + "[" + xo + ", " + yo + ", " + zo + "]\n";
    }
}
