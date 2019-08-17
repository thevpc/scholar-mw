package net.vpc.scholar.hadruplot.backends.simple.mesh;


import java.awt.*;

class Mesh3DObjectTransformable extends Mesh3DTransformable {

    String name;
    DefaultMesh3DModel md;
    Mesh3DVector[] vert;
    Mesh3DVector[] overt;
    Mesh3DVector[] norm;
    Mesh3DVector[] onorm;
    Mesh3DVector[] faceCenter;
    Mesh3DVector[] ofaceCenter;
    int[] facePaintOrder;
    Mesh3DVector center;
    Mesh3DVector ocenter;
    Mesh3DFace face[];
    int nvert;
    int cvert;
    int nface;
    int cface;

    Mesh3DObjectTransformable(DefaultMesh3DModel model3d, int nbrVertexes, int nbrFaces) {
        name = null;
        md = model3d;
        nvert = nbrVertexes;
        nface = nbrFaces;
        if (nbrVertexes <= 3 || nbrFaces <= 0) {
            System.err.println("(nbrVertexes <= 3 || nbrFaces <= 0)");
        }
        vert = new Mesh3DVector[nbrVertexes];
        overt = new Mesh3DVector[nbrVertexes];
        norm = new Mesh3DVector[nbrFaces];
        onorm = new Mesh3DVector[nbrFaces];
        faceCenter = new Mesh3DVector[nbrFaces];
        ofaceCenter = new Mesh3DVector[nbrFaces];
        facePaintOrder = new int[nbrFaces];
        center = new Mesh3DVector();
        ocenter = new Mesh3DVector();
        face = new Mesh3DFace[nbrFaces];
        cvert = cface = 0;
    }

    Mesh3DObjectTransformable(String s, DefaultMesh3DModel model3d, int i, int j) {
        this(model3d, i, j);
        name = s;
    }

    int addVertex(Mesh3DVector vector3d) {
        return addVertex(vector3d.x, vector3d.y, vector3d.z);
    }

    int addVertex(double d, double d1, double d2) {
        if (cvert >= nvert) {
            return -1;
        } else {
            vert[cvert] = new Mesh3DVector(d, d1, d2);
            overt[cvert] = new Mesh3DVector(d, d1, d2);
            return cvert++;
        }
    }

    void setFaceCenter(int i, Mesh3DVector vector3d) {
        setFaceCenter(i, vector3d.x, vector3d.y, vector3d.z);
    }

    void setFaceCenter(int i, double d, double d1, double d2) {
        faceCenter[i] = new Mesh3DVector(d, d1, d2);
        ofaceCenter[i] = new Mesh3DVector(d, d1, d2);
    }

    int addFace(int nbrVertexes, Color color) {
        return addFace(nbrVertexes, color.getRed(), color.getGreen(), color.getBlue());
    }

    int addFace(int nbrVertexes, int j, int k, int l) {
        if (cface >= nface) {
            return -1;
        } else {
            face[cface] = new Mesh3DFace(this, nbrVertexes, j, k, l);
            return cface++;
        }
    }

    int addFace(int i) {
        int j = addFace(i, 0, 0, 0);
        face[j].dummy = true;
        return j;
    }

    void setName(String s) {
        name = s;
    }

    void setColor(Color color) {
        setColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    void setColor(int i, int j, int k) {
        if (cvert != nvert || cface != nface)
            return;
        for (int l = 0; l < nface; l++)
            face[l].setColor(i, j, k);

    }

    void setDummy(boolean flag) {
        for (int i = 0; i < nface; i++)
            face[i].setDummy(flag);

    }

    void transformToModelSpace() {
        if (cvert != nvert || cface != nface) {
            return;
        } else {
            Mesh3DMatrix matrix3d = new Mesh3DMatrix();
            matrix3d.mul(super.rot);
            matrix3d.mul(super.scale);
            matrix3d.mul(super.trans);
            matrix3d.transform(ocenter, center);
            matrix3d.transform(overt, vert, nvert);
            matrix3d.setValues(Mesh3DMatrix.M_IDENT);
            matrix3d.mul(super.rot);
            matrix3d.transform(onorm, norm, nface);
            return;
        }
    }

    void transformToCameraSpace() {
        if (cvert != nvert || cface != nface) {
            return;
        } else {
            Mesh3DMatrix matrix3d = new Mesh3DMatrix();
            matrix3d.mul(super.rot);
            matrix3d.mul(super.scale);
            matrix3d.mul(super.trans);
            matrix3d.mul(((Mesh3DTransformable) (md)).rot);
            matrix3d.mul(((Mesh3DTransformable) (md)).scale);
            matrix3d.mul(((Mesh3DTransformable) (md)).trans);
            matrix3d.transform(ocenter, center);
            matrix3d.transform(overt, vert, nvert);
            matrix3d.unit();
            matrix3d.mul(super.rot);
            matrix3d.mul(((Mesh3DTransformable) (md)).rot);
            matrix3d.transform(onorm, norm, nface);
            return;
        }
    }

    void transformToScreenSpace() {
        if (cvert != nvert || cface != nface)
            return;
        Mesh3DMatrix matrix3d = new Mesh3DMatrix();
        matrix3d.mul(super.rot);
        matrix3d.mul(super.scale);
        matrix3d.mul(super.trans);
        matrix3d.mul(((Mesh3DTransformable) (md)).rot);
        matrix3d.mul(((Mesh3DTransformable) (md)).scale);
        matrix3d.mul(((Mesh3DTransformable) (md)).trans);
        matrix3d.transform(ocenter, center);
        matrix3d.mul(md.mat);
        matrix3d.transform(overt, vert, nvert);
        matrix3d.transform(ofaceCenter, faceCenter, nface);
        zSortFaceCenters();
        matrix3d.setValues(Mesh3DMatrix.M_IDENT);
        matrix3d.mul(super.rot);
        matrix3d.mul(((Mesh3DTransformable) (md)).rot);
        matrix3d.transform(onorm, norm, nface);
        if (md.persp) {
            for (int i = 0; i < nface; i++)
                face[i].computeCenter();

            double d = md.width / 2;
            double d1 = md.height / 2;
            double d2 = md.zTarget;
            for (int j = 0; j < nvert; j++) {
                Mesh3DVector vector3d = vert[j];
                if (vector3d.z < 0.0D) {
                    double d3 = d2 / vector3d.z;
                    vector3d.x = d + vector3d.x * d3;
                    vector3d.y = d1 - vector3d.y * d3;
                } else {
                    setDummy(true);
                }
            }

        }
    }

    void paint(Graphics g) {
        if (cvert != nvert || cface != nface)
            return;
        for (int i = 0; i < nface; i++) {
            Mesh3DFace face3d;
            if (md.transparent)
                face3d = face[i];
            else
                face3d = face[facePaintOrder[i]];
            if (face3d.visible() || !md.transparent)
                face3d.paint(g);
        }

    }

    int inside(int i, int j) {
        if (cvert != nvert || cface != nface)
            return -1;
        for (int k = 0; k < nface; k++) {
            Mesh3DFace face3d = face[k];
            if (face3d.visible() && face3d.inside(i, j))
                return k;
        }

        return -1;
    }

    void computeCenter() {
        Mesh3DVector vector3d = ocenter;
        vector3d.set(0.0D, 0.0D, 0.0D);
        for (int i = 0; i < nvert; i++)
            vector3d.add(overt[i]);

        vector3d.div(nvert);
    }

    void colectNormals() {
        if (cvert != nvert || cface != nface)
            return;
        computeCenter();
        for (int i = 0; i < nface; i++) {
            Mesh3DFace face3d = face[i];
            Mesh3DVector vector3d = new Mesh3DVector(ocenter);
            vector3d.sub(face3d.vert[0]);
            onorm[i] = face3d.normal;
            face3d.normal = norm[i] = new Mesh3DVector();
        }

    }

    void computeFaceCenters() {
        if (cvert != nvert || cface != nface)
            return;
        for (int i = 0; i < nface; i++) {
            Mesh3DFace face3d = face[i];
            Mesh3DVector vector3d = ocenter;
            vector3d.set(0.0D, 0.0D, 0.0D);
            for (int j = 0; j < face3d.nvert; j++)
                vector3d.add(face3d.vert[j]);

            vector3d.div(face3d.nvert);
            setFaceCenter(i, vector3d);
        }

    }

    void zSortFaceCenters() {
        double ad[] = new double[nface];
        for (int i = 0; i < nface; i++) {
            facePaintOrder[i] = i;
            Mesh3DVector vector3d = faceCenter[i];
            ad[i] = vector3d.z;
        }

        qSort(ad, facePaintOrder, 0, nface - 1);
    }

    void qSort(double ad[], int ai[], int i, int j) {
        if (i < j) {
            double d = ad[j];
            int k = i;
            int l = j - 1;
            do {
                do {
                    while (ad[k] < d)
                        k++;
                    for (; l >= 1 && d < ad[l]; l--) ;
                    if (k >= l)
                        break;
                    double d1 = ad[k];
                    int i1 = ai[k];
                    ad[k] = ad[l];
                    ai[k] = ai[l];
                    ad[l] = d1;
                    ai[l] = i1;
                    k++;
                    l--;
                } while (true);
            } while (k < l);
            double d2 = ad[k];
            int j1 = ai[k];
            ad[k] = ad[j];
            ai[k] = ai[j];
            ad[j] = d2;
            ai[j] = j1;
            qSort(ad, ai, i, k - 1);
            qSort(ad, ai, k + 1, j);
        }
    }
}
