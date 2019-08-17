package net.vpc.scholar.hadruplot.backends.simple.mesh;


class Mesh3DModel extends DefaultMesh3DModel {

    double rotation;

    Mesh3DModel(int i, int j, Mesh3DObject p1) {
        super(i, j, 1);
        p1.md = this;
        rotation = 1.0D;
        setPersp(false);
        p1.compile();

        p1.setTranslation(-p1.ocenter.x, -p1.ocenter.y, -p1.ocenter.z);
        addObject(p1);
    }

    void setPaintOrder() {
        zSort();
    }
}
