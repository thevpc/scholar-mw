package net.thevpc.scholar.hadruplot.libraries.simple.mesh;


import java.awt.*;

class Mesh3DFace {

    Mesh3DObjectTransformable obj;
    Mesh3DVector[] vert;
    Mesh3DVector center;
    Mesh3DVector normal;
    int nvert;
    int cvert;
    int red;
    int green;
    int blue;
    boolean dummy;

    Mesh3DFace(Mesh3DObjectTransformable object3d, int nbrVertexes, Color color) {
        dummy = false;
        obj = object3d;
        nvert = nbrVertexes;
        red = color.getRed();
        green = color.getGreen();
        blue = color.getBlue();
        if (nbrVertexes < 3) {
            return;
        } else {
            vert = new Mesh3DVector[nbrVertexes];
            center = new Mesh3DVector();
            cvert = 0;
            return;
        }
    }

    Mesh3DFace(Mesh3DObjectTransformable object3d, int nbrVertexes, int j, int k, int l) {
        this(object3d, nbrVertexes, new Color(j, k, l));
    }

    Mesh3DFace(Mesh3DObjectTransformable object3d, int i) {
        this(object3d, i, 0, 0, 0);
        dummy = true;
    }

    int addVertex(Mesh3DVector vector3d) {
        if (cvert >= nvert)
            return -1;
        vert[cvert] = vector3d;
        if (cvert == nvert - 1)
            computeNormal();
        return cvert++;
    }

    void setColor(Color color) {
        setColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    void setColor(int i, int j, int k) {
        red = i;
        green = j;
        blue = k;
    }

    void setDummy(boolean flag) {
        dummy = flag;
    }

    boolean visible() {
        if (normal == null)
            return false;
        if (obj.md.persp)
            return center.dot(normal) < 0.0D;
        return normal.z > 0.0D;
    }

    void paint(Graphics g) {
        if (dummy)
            return;
        if (cvert != nvert)
            return;
        double d1 = obj.md.ambient;
        double d;
        if (obj.md.persp)
            d = -center.dot(normal);
        else
            d = normal.z;
        d *= 1.0D - d1;
        d1 *= 255D;
        int i = (int) (d1 + d * (double) red);
        int j = (int) (d1 + d * (double) green);
        int k = (int) (d1 + d * (double) blue);
        if (i < 0 || i > 255) {
            i = 0;
        }
        if (j < 0 || j > 255) {
            j = 0;
        }
        if (k < 0 || k > 255) {
            k = 0;
        }
        Color color = new Color(i, j, k);
        g.setColor(color);
        int[] ai = new int[nvert];
        int[] ai1 = new int[nvert];
        for (int l = 0; l < nvert; l++) {
            Mesh3DVector vector3d = vert[l];
            ai[l] = (int) vector3d.x;
            ai1[l] = (int) vector3d.y;
        }
        if (obj.md.drawSurfaces) {
            g.fillPolygon(ai, ai1, nvert);
        }
        g.setColor(Color.black);
        if (obj.md.drawLines) {
            g.drawPolygon(ai, ai1, nvert);
        }
        if (obj.md.visibleNodes) {
            for (int i1 = 0; i1 < nvert; i1++) {
                g.fillRect(ai[i1] - 1, ai1[i1] - 1, 3, 3);
            }

        }
    }

    boolean inside(int i, int j) {
        if (dummy)
            return false;
        if (cvert != nvert)
            return false;
        int[] ai = new int[nvert];
        int[] ai1 = new int[nvert];
        for (int k = 0; k < nvert; k++) {
            Mesh3DVector vector3d = vert[k];
            ai[k] = (int) vector3d.x;
            ai1[k] = (int) vector3d.y;
        }

        Polygon polygon = new Polygon(ai, ai1, nvert);
        return polygon.contains(i, j);
    }

    void computeCenter() {
        Mesh3DVector vector3d = center;
        vector3d.set(0.0D, 0.0D, 0.0D);
        for (int i = 0; i < nvert; i++)
            vector3d.add(vert[i]);

        vector3d.div(nvert);
        vector3d.normalize();
    }

    Mesh3DVector getRealCenter() {
        Mesh3DVector vector3d = new Mesh3DVector(0.0D, 0.0D, 0.0D);
        for (int i = 0; i < nvert; i++)
            vector3d.add(vert[i]);

        vector3d.div(nvert);
        return vector3d;
    }

    void computeNormal() {
        normal = new Mesh3DVector(0.0D, 0.0D, 0.0D);
        Mesh3DVector vector3d = new Mesh3DVector(vert[0]);
        vector3d.sub(vert[nvert - 1]);
        for (int i = 0; i < nvert - 1; i++) {
            Mesh3DVector vector3d1 = new Mesh3DVector(vert[i + 1]);
            vector3d1.sub(vert[i]);
            vector3d.mul(vector3d1);
            normal.add(vector3d);
            vector3d = vector3d1;
        }

        Mesh3DVector vector3d2 = new Mesh3DVector(vert[0]);
        vector3d2.sub(vert[nvert - 1]);
        vector3d.mul(vector3d2);
        normal.add(vector3d);
        normal.normalize();
    }
}
