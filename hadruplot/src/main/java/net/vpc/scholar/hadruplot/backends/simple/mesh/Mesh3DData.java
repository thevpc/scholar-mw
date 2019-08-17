package net.vpc.scholar.hadruplot.backends.simple.mesh;


class Mesh3DData {

    int dimension;
    int nbrNodes;
    int nel;
    int[] pelcon;
    int[] elcon;
    int[] elmat;
    double[] nodeXYZ;

    Mesh3DData() {
    }

    Mesh3DData(
            int dimension,
            int[] pelcon,
            int[] elcon,
            double[] nodeXYZ
    ) {
        this.dimension = dimension;
//        this.lelcon=lelcon;

        this.pelcon = pelcon;
        this.elcon = elcon;
        this.nodeXYZ = nodeXYZ;

        nbrNodes = nodeXYZ.length;
        nel = elcon.length;
        pelcon[nel] = elcon.length;
    }
}
