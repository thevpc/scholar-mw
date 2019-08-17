package net.vpc.scholar.hadruplot.backends.simple.mesh;


class Mesh3DSurface {

    boolean surface;
    Mesh3DData fe;
    int nsur;
    int[] psurcon;
    int[] surcon;
    int nUsedNodes;
    int[] usedNodes;

    Mesh3DSurface(Mesh3DData fedata) {
        surface = true;
        fe = fedata;
        psurcon = new int[fe.nel * 6];
        surcon = new int[fe.nel * 48];
        usedNodes = new int[fe.nbrNodes];
        if (fe.dimension == 2) {
            makeFeSurfaces2d();
            return;
        } else {
            makeFeSurfaces3d();
            return;
        }
    }

    void makeFeSurfaces2d() {
        for (int i = 0; i <= fe.nel; i++)
            psurcon[i] = fe.pelcon[i];

        for (int j = 0; j < psurcon[fe.nel]; j++)
            surcon[j] = fe.elcon[j];

        for (int k = 0; k < fe.nbrNodes; k++)
            usedNodes[k] = k;

        nsur = fe.nel;
        nUsedNodes = fe.nbrNodes;
    }

    void makeFeSurfaces3d() {
        int ai[][] = {
                {
                        1, 8, 7, 6, 5, 4, 3, 2
                }, {
                1, 2, 3, 10, 15, 14, 13, 9
        }, {
                3, 4, 5, 11, 17, 16, 15, 10
        }, {
                13, 14, 15, 16, 17, 18, 19, 20
        }, {
                5, 6, 7, 12, 19, 18, 17, 11
        }, {
                7, 8, 1, 9, 13, 20, 19, 12
        }
        };
        int ai1[] = new int[8];
        nsur = 0;
        for (int i = 0; i < fe.nel; i++) {
            for (int k1 = 0; k1 < 6; k1++) {
                for (int l1 = 0; l1 < 8; l1++)
                    ai1[l1] = fe.elcon[(fe.pelcon[i] + ai[k1][l1]) - 1];

                if ((ai1[0] != ai1[2] || ai1[4] != ai1[6]) && (ai1[0] != ai1[6] || ai1[2] != ai1[4])) {
                    int i2;
                    for (i2 = 0; i2 < nsur; i2++)
                        if (equalSurfaces(psurcon[i2], ai1, 8))
                            break;

                    if (i2 == nsur) {
                        for (int i1 = 0; i1 < 8; i1++)
                            surcon[psurcon[nsur] + i1] = ai1[i1];

                        psurcon[nsur + 1] = psurcon[nsur] + 8;
                        nsur++;
                    } else if (surface) {
                        for (int j1 = psurcon[i2]; j1 < psurcon[nsur]; j1++)
                            surcon[j1] = surcon[j1 + 8];

                        nsur--;
                    }
                }
            }

        }

        for (int j = 0; j < fe.nbrNodes; j++)
            usedNodes[j] = -1;

        for (int k = 0; k < psurcon[nsur]; k++)
            if (surcon[k] > 0)
                usedNodes[surcon[k] - 1] = 1;

        int l = 0;
        nUsedNodes = 0;
        for (; l < fe.nbrNodes; l++)
            if (usedNodes[l] > 0)
                usedNodes[l] = nUsedNodes++;

    }

    boolean equalSurfaces(int i, int ai[], int j) {
        int k;
        for (k = 0; k < j; k += 2) {
            int i1 = surcon[i + k];
            int l;
            for (l = 0; l < j; l += 2)
                if (i1 == ai[l])
                    break;

            if (l == j)
                break;
        }

        return k == j;
    }
}
