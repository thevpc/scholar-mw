package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.MathsBase;
import net.vpc.scholar.hadrumaths.ComplexMatrix;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Created by IntelliJ IDEA.
 * User: taha
 * Date: 16 juin 2004
 * Time: 00:42:43
 * To change this template use File | Settings | File Templates.
 */
class _old_HeatMapPlot extends JComponent {
    private static final long serialVersionUID = 1L;
    double[][] matrix;
    double[][] sourceMatrix;
    private Object[] xAxis;
    private Object[] yAxis;
    private float H = 180f / 240f;
    //    private float H = 134f / 240f;
    private float S = 210f / 240f;
    //    private float S = 203f / 240f;
    private float B = (125f + 40) / 240f;
    private float D = 40f / 240f;

    private int current_i = -1;
    private int current_j = -1;


    public _old_HeatMapPlot(ComplexMatrix matrix, int preferredDimension) {
        this(toAbs(matrix), preferredDimension);
    }

    public _old_HeatMapPlot(double[][] matrix, int preferredDimension) {
        super();
        setData(matrix);
        setPreferredDimension(preferredDimension);
        addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {

            }

            public void mouseMoved(MouseEvent e) {
                onMouseMove(e);
            }
        });
        addMouseListener(new MouseAdapter() {
            public void mouseExited(MouseEvent e) {
                current_i = -1;
                current_j = -1;
                repaint();
            }
        });
    }

    public void setPreferredDimension(int preferredDimension) {
        float factor = (matrix.length > 0 && matrix[0].length > 0) ? ((float) matrix.length / (float) matrix[0].length) : 1;
        Dimension dim = (factor <= 1) ?
                new Dimension(preferredDimension, (int) (preferredDimension * factor))
                : new Dimension((int) (preferredDimension / factor), preferredDimension);
        setMinimumSize(dim);
        setPreferredSize(dim);
        setSize(dim);
    }

    public void setData(double[][] matrix) {
        this.matrix = new double[matrix.length][matrix.length == 0 ? 0 : matrix[0].length];
        this.sourceMatrix = new double[matrix.length][matrix.length == 0 ? 0 : matrix[0].length];
        double min;
        double max;
        max = Double.NaN;
        min = Double.NaN;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                double d = matrix[i][j];
                sourceMatrix[i][j] = d;
                if (!Double.isInfinite(d) || !Double.isNaN(d)) {
                    if (Double.isNaN(max) || d > max) {
                        max = d;
                    }
                    if (d < min || Double.isNaN(min)) {
                        min = d;
                    }
                }
            }
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                double d = matrix[i][j];
                if (d == Double.POSITIVE_INFINITY) {
                    this.matrix[i][j] = 1;
                } else if (d == Double.NEGATIVE_INFINITY) {
                    this.matrix[i][j] = 0;
                } else if (Double.isNaN(d)) {
                    this.matrix[i][j] = 1;
                } else {
                    this.matrix[i][j] = (d - min) / (max - min);
                }
            }
        }
    }

    private void onMouseMove(MouseEvent e) {
        Dimension d = getSize();
        int x = matrix.length == 0 ? 0 : matrix[0].length;
        int y = matrix.length;
        int i = (int) (y * ((double) e.getY() / d.height));
        int j = (int) (x * ((double) e.getX() / d.width));
        StringBuilder sb = new StringBuilder();
        sb.append("value=").append(sourceMatrix[i][j]);
        if (xAxis != null && xAxis.length > i && xAxis[i] != null) {
            sb.append(" ; x=").append(xAxis[i]);
        } else {
            sb.append(" ; x=").append(i + 1);
        }
        if (yAxis != null && yAxis.length > j && yAxis[j] != null) {
            sb.append(" ; y=").append(yAxis[j]);
        } else {
            sb.append(" ; y=").append(j + 1);
        }
        sb.append(" ; %=").append(matrix[i][j] * 100);
        current_i = i;
        current_j = j;
        setToolTipText(sb.toString());
        repaint();
    }

    public void paint(Graphics g) {
        Dimension d = getSize();
        double y = 0;
        double x = 0;
        g.setColor(Color.white);
        g.fillRect(0, 0, d.width, d.height);
        if (matrix.length > 0 && matrix[0].length > 0) {
            x = 1.0 * d.width / matrix[0].length;
            y = 1.0 * d.height / matrix.length;
        }
        int ix = (int) x;
        int iy = (int) y;
        if (ix == 0) {
            ix = 1;
        }
        if (iy == 0) {
            iy = 1;
        }
        for (int line = 0; line < matrix.length; line++) {
            for (int column = 0; column < matrix[line].length; column++) {
                float f = 1 - (float) matrix[line][column];
                Color baseColor = new Color(Color.HSBtoRGB(H * (f), S, B));
                g.setColor(baseColor);
                int xx = (int) MathsBase.round(column * x);
                int yy = (int) MathsBase.round(line * y);
                int ixx = ix;
                int iyy = iy;
                if (column > 0 && (xx > ((int) MathsBase.round((column - 1) * x)))) {
                    xx--;
                    ixx += 2;
                }
                if (line > 0 && (yy > ((int) MathsBase.round((line - 1) * y)))) {
                    yy--;
                    iyy += 2;
                }

                g.fillRect(xx, yy, ixx, iyy);
                if (current_i == line && current_j == column) {
                    Color brighterColor = new Color(Color.HSBtoRGB(H * (f), S, (B + D)));
//                    Color darkerColor = new Color(Color.HSBtoRGB(H * (f), S, (B - D)));
                    g.setColor(brighterColor);
                    g.fillRect(xx, yy, ixx, iyy);
//                    if(ix>8 && iy>8){
//                        g.setColor(darkerColor);
//                        g.drawRect((int) (column*x)+1,(int) (line*y)+1,ix-2,iy-2);
//                    }
                }
            }
        }
    }

    private static double[][] toAbs(ComplexMatrix m) {
        double[][] d = new double[m.getRowCount()][m.getColumnCount()];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[i].length; j++) {
                d[i][j] = m.get(i, j).absdbl();
            }
        }
        return d;
    }

    public Object[] getxAxis() {
        return xAxis;
    }

    public void setxAxis(Object[] xAxis) {
        this.xAxis = xAxis;
    }

    public Object[] getyAxis() {
        return yAxis;
    }

    public void setyAxis(Object[] yAxis) {
        this.yAxis = yAxis;
    }

    public void setyAxis(double[] yAxis) {
        if (yAxis == null) {
            this.yAxis = null;
        } else {
            this.yAxis = new Double[yAxis.length];
            for (int i = 0; i < yAxis.length; i++) {
                this.yAxis[i] = new Double(yAxis[i]);
            }
        }
    }

    public void setxAxis(double[] xAxis) {
        if (xAxis == null) {
            this.xAxis = null;
        } else {
            this.xAxis = new Double[xAxis.length];
            for (int i = 0; i < xAxis.length; i++) {
                this.xAxis[i] = new Double(xAxis[i]);
            }
        }
    }
}
