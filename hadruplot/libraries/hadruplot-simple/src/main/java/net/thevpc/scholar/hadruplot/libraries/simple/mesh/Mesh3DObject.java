package net.thevpc.scholar.hadruplot.libraries.simple.mesh;


import java.awt.*;

class Mesh3DObject extends Mesh3DObjectTransformable {

    Mesh3DObject(DefaultMesh3DModel model3d, int nbrUsedNodes, int nbrSurfaces) {
        super(model3d, nbrUsedNodes, nbrSurfaces);
    }

    Mesh3DObject(DefaultMesh3DModel model3d, Mesh3DData fedata, Mesh3DSurface fesurfaces) {
        super(model3d, fesurfaces.nUsedNodes, fesurfaces.nsur);
        double d2 = 0.0D;
        for (int i = 0; i < fedata.nbrNodes; i++)
            if (fesurfaces.usedNodes[i] >= 0) {
                double d = fedata.nodeXYZ[fedata.dimension * i];
                double d1 = fedata.nodeXYZ[fedata.dimension * i + 1];
                if (fedata.dimension == 3)
                    d2 = fedata.nodeXYZ[fedata.dimension * i + 2];
                Mesh3DVector vector3d = new Mesh3DVector(d, d1, d2);
                addVertex(vector3d);
            }

        int[] ai = new int[8];
        int r = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);
        for (int l = 0; l < fesurfaces.nsur; l++) {
            int j1 = 0;
            for (int j = fesurfaces.psurcon[l]; j < fesurfaces.psurcon[l + 1]; j++) {
                int i1 = fesurfaces.surcon[j] - 1;
                if (i1 >= 0)
                    ai[j1++] = i1;
            }

            Mesh3DFace face3d = face[addFace(j1, new Color(r, g, b))];//Color.yellow
            for (int k = 0; k < j1; k++)
                face3d.addVertex(super.vert[fesurfaces.usedNodes[ai[k]]]);

        }

        colectNormals();
        computeFaceCenters();
    }

    public void compile() {
        colectNormals();
        computeFaceCenters();
    }
}
