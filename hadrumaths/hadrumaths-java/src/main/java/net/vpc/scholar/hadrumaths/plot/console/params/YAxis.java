package net.vpc.scholar.hadrumaths.plot.console.params;

import java.io.Serializable;

public class YAxis implements Serializable {
    public int matrixX;
    public int matrixY;
    public YAxisType[] type;

    public YAxis(YAxisType... type) {
        this.type = type;
        this.matrixX = 0;
        this.matrixY = 0;
    }

    public YAxis(int matrixX, int matrixY, YAxisType... type) {
        this.type = type;
        this.matrixX = matrixX;
        this.matrixY = matrixY;
    }

    public boolean doShow(YAxisType t) {
        for (YAxisType aType : type) {
            if (t == aType) {
                return true;
            }

        }
        return false;
    }
}
