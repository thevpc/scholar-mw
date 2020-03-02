package net.vpc.scholar.hadruplot.backends.simple.mesh;


import java.awt.*;

class DefaultMesh3DModel extends Mesh3DTransformable {

    int width;
    int height;
    Mesh3DMatrix mat;
    Mesh3DObjectTransformable[] obj;
    int nobj;
    int cobj;
    double ambient;
    static final double defaultAmbient = 0.29999999999999999D;
    double minScale;
    static final double defaultMinScale = 0.80000000000000004D;
    double maxScale;
    static final double defaultMaxScale = 1.1000000000000001D;
    BoundingBox bb;
    double zCamera;
    double zTarget;
    boolean persp;
    boolean visibleNodes;
    boolean transparent;
    boolean drawLines = false;
    boolean drawSurfaces = true;

    DefaultMesh3DModel(int i, int j, int k) {
        persp = true;
        visibleNodes = false;
        transparent = false;
        width = i;
        height = j;
        nobj = k;
        if (k <= 0)
            return;
        obj = new Mesh3DObjectTransformable[k];
        cobj = 0;
        ambient = defaultAmbient;
        minScale = defaultMinScale;
        maxScale = defaultMaxScale;
        if (ambient < 0.0D || ambient > 1.0D)
            ambient = 0.29999999999999999D;
        if (minScale > maxScale) {
            minScale = 0.80000000000000004D;
            maxScale = 1.1000000000000001D;
        }
    }


    int addObject(Mesh3DObjectTransformable object3d) {
        if (cobj >= nobj)
            return -1;
        obj[cobj] = object3d;
        if (cobj == nobj - 1)
            evalMatrix();
        return cobj++;
    }

    synchronized void paint(Graphics g) {
        if (cobj != nobj)
            return;
        for (int i = 0; i < nobj; i++)
            obj[i].transformToScreenSpace();

        setPaintOrder();
        for (int j = 0; j < nobj; j++)
            obj[j].paint(g);

    }

    synchronized String inside(int i, int j) {
        if (cobj != nobj)
            return null;
        for (int k = nobj - 1; k >= 0; k--) {
            Mesh3DObjectTransformable object3d = obj[k];
            if (object3d.inside(i, j) >= 0)
                if (object3d.name != null)
                    return object3d.name;
                else
                    return "noname";
        }

        return null;
    }

    void setPaintOrder() {
        zSort();
    }

    void zSort() {
        if (nobj <= 1)
            return;
        boolean flag = true;
        while (flag) {
            flag = false;
            Mesh3DObjectTransformable object3d = obj[0];
            for (int i = 1; i < nobj; i++) {
                Mesh3DObjectTransformable object3d1 = obj[i];
                if (object3d.center.z > object3d1.center.z) {
                    obj[i - 1] = object3d1;
                    obj[i] = object3d;
                    flag = true;
                }
                object3d = obj[i];
            }

        }
    }

    void resize(int i, int j) {
        width = i;
        height = j;
        if (cobj != nobj) {
            return;
        } else {
            evalMatrix();
            return;
        }
    }

    synchronized void evalMatrix() {
        if (bb == null)
            computeBoundingBox();
        Mesh3DVector vector3d = bb.getCenter();
        double d = (double) width / bb.getWidth();
        double d1 = (double) height / bb.getHeight();
        double d2 = 0.70000000000000007D * Math.min(d, d1);
        mat = new Mesh3DMatrix();
        mat.translate(-vector3d.x, -vector3d.y, -vector3d.z);
        mat.scale(d2, d2, d2);
        double d3 = d2 * bb.getDepth();
        double d4 = (d3 * minScale) / (maxScale - minScale);
        zCamera = d4 + d3 / 2D;
        zTarget = -d4 * maxScale;
        if (persp) {
            mat.translate(0.0D, 0.0D, -zCamera);
            return;
        } else {
            mat.scale(1.0D, -1D, 1.0D);
            mat.translate(width / 2, height / 2, 0.0D);
            return;
        }
    }

    void computeBoundingBox() {
        Mesh3DObjectTransformable object3d = obj[0];
        object3d.transformToCameraSpace();
        bb = new BoundingBox(object3d.vert, object3d.nvert);
        for (int i = 1; i < nobj; i++) {
            Mesh3DObjectTransformable object3d1 = obj[i];
            object3d1.transformToCameraSpace();
            bb.combine(new BoundingBox(object3d1.vert, object3d1.nvert));
        }

    }

    void setPersp(boolean flag) {
        if (flag == persp)
            return;
        persp = flag;
        if (mat == null)
            return;
        if (flag) {
            mat.translate(-width / 2, -height / 2, 0.0D);
            mat.scale(1.0D, -1D, 1.0D);
            mat.translate(0.0D, 0.0D, -zCamera);
            return;
        } else {
            mat.translate(0.0D, 0.0D, zCamera);
            mat.scale(1.0D, -1D, 1.0D);
            mat.translate(width / 2, height / 2, 0.0D);
            return;
        }
    }

    void setVisibleNodes(boolean flag) {
        visibleNodes = flag;
    }

    void setTransparent(boolean flag) {
        transparent = flag;
    }

    public boolean isDrawLines() {
        return drawLines;
    }

    public void setDrawLines(boolean drawLines) {
        this.drawLines = drawLines;
    }

    public boolean isDrawSurfaces() {
        return drawSurfaces;
    }

    public void setDrawSurfaces(boolean drawSurfaces) {
        this.drawSurfaces = drawSurfaces;
    }
}
