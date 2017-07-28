package net.vpc.scholar.hadrumaths.plot.mesh;

class Mesh3DTransformable {

    Mesh3DMatrix rot;
    Mesh3DMatrix scale;
    Mesh3DMatrix trans;

    Mesh3DTransformable() {
        rot = new Mesh3DMatrix();
        scale = new Mesh3DMatrix();
        trans = new Mesh3DMatrix();
    }

    Mesh3DTransformable(Mesh3DTransformable position) {
        this();
        setValues(position);
    }

    void setValues(Mesh3DTransformable position) {
        rot.setValues(position.rot);
        scale.setValues(position.scale);
        trans.setValues(position.trans);
    }

    void setRotation(double d, double d1, double d2) {
        rot.setValues(Mesh3DMatrix.M_IDENT);
        addRotation(d, d1, d2);
    }

    void setScale(double d, double d1, double d2) {
        scale.setValues(Mesh3DMatrix.M_IDENT);
        addScale(d, d1, d2);
    }

    void setTranslation(double d, double d1, double d2) {
        trans.setValues(Mesh3DMatrix.M_IDENT);
        addTranslation(d, d1, d2);
    }

    void addRotation(double d, double d1, double d2) {
        if (d != 0.0D)
            rot.rotateX(d);
        if (d1 != 0.0D)
            rot.rotateY(d1);
        if (d2 != 0.0D)
            rot.rotateZ(d2);
    }

    void addScale(double d, double d1, double d2) {
        scale.scale(d, d1, d2);
    }

    void addTranslation(double d, double d1, double d2) {
        trans.translate(d, d1, d2);
    }
}
