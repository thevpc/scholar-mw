package net.thevpc.scholar.hadruplot;

import net.thevpc.common.util.ArrayUtils;

public class PlotCube {
    private double[] x;
    private double[] y;
    private double[] z;
    private Object[][][] values;

    public PlotCube(double[] x, double[] y, double[] z, Object[][][] values) {
        this.values = values;
        if (z == null) {
            z = ArrayUtils.dsteps(0, values.length, 1);
        }
        if (y == null) {
            if (values.length == 0) {
                y = new double[0];
            } else {
                y = ArrayUtils.dsteps(0, values[0].length, 1);
            }
        }
        if (x == null) {
            if (values[0].length == 0) {
                x = new double[0];
            } else {
                x = ArrayUtils.dsteps(0, values[0][0].length, 1);
            }
        }
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double[] getX() {
        return x;
    }

    public double[] getY() {
        return y;
    }

    public double[] getZ() {
        return z;
    }

    public int getCount(int index) {
        switch (index) {
            case 0: {
                if (this.values.length == 0) {
                    return 0;
                }
                if (this.values[0].length == 0) {
                    return 0;
                }
                return this.values[0][0].length;
            }
            case 1: {
                if (this.values.length == 0) {
                    return 0;
                }
                return this.values[0].length;
            }
            case 2: {
                return this.values.length;
            }
        }
        return 0;
    }

    public Object[][] getArray(int fixedAxis, int index) {
        switch (fixedAxis) {
            case 2: {
                int xcount = getCount(0);
                int ycount = getCount(1);
                Object[][] d = new Object[ycount][xcount];
                for (int j = 0; j < ycount; j++) {
                    System.arraycopy(this.values[index][j], 0, d[j], 0, xcount);
                }
                return d;
            }
            case 1: {
                int xcount = getCount(0);
                int zcount = getCount(2);
                Object[][] d = new Object[zcount][xcount];
                for (int i = 0; i < zcount; i++) {
                    System.arraycopy(this.values[i][index], 0, d[i], 0, xcount);
                }
                return d;
            }
            case 0: {
                int zcount = getCount(2);
                int ycount = getCount(1);
                Object[][] d = new Object[zcount][ycount];
                for (int i = 0; i < zcount; i++) {
                    for (int j = 0; j < ycount; j++) {
                        d[i][j] = this.values[i][j][index];
                    }
                }
                return d;
            }
        }
        throw new IllegalArgumentException("Impossible");
    }

    public Object[][][] getValues() {
        return values;
    }
}
