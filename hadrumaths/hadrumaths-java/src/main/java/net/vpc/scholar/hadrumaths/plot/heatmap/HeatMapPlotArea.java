package net.vpc.scholar.hadrumaths.plot.heatmap;

import net.vpc.common.swings.SerializableActionListener;
import net.vpc.scholar.hadrumaths.DMatrix;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.MinMax;
import net.vpc.scholar.hadrumaths.plot.HSBColorPalette;
import net.vpc.scholar.hadrumaths.plot.JColorPalette;
import net.vpc.scholar.hadrumaths.plot.ValuesPlotXYDoubleModelFace;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 16 juin 2004 00:42:43
 */
public class HeatMapPlotArea extends JComponent implements MouseMotionListener, MouseListener {
    private static final long serialVersionUID = 1L;
    int rowsCount = 0;
    int columnsCount = 0;
    double minValue;
    double maxValue;
    double[][] matrix;
    double[][] sourceMatrix;
    private Object[] xAxis;
    private Object[] yAxis;
    private JColorPalette colorPaletteContrasted = HSBColorPalette.DEFAULT_PALETTE;
    private JColorPalette colorPalette = HSBColorPalette.DEFAULT_PALETTE;
    int contrast = -1;
    private int current_yi = -1;
    private int current_xj = -1;
    private boolean useTooltips = false;
    private HeatMapPlotNormalizer normalizer = new DefaultHeatMapPlotNormalizer();

//
//    public HeatMapPlotArea(double[] x, double[] y, double[][] matrix) {
//        this(x, y, matrix, null, null);
//    }
//
//    public HeatMapPlotArea(double[] x, double[] y, double[][] matrix, JColorPalette colorPalette) {
//        dsteps(x, y, matrix, colorPalette, null);
//    }

    public HeatMapPlotArea(ValuesPlotXYDoubleModelFace model, boolean reverseY, JColorPalette colorPalette, Dimension preferredDimension) {
        init(model, reverseY, colorPalette, preferredDimension);
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public HeatMapPlotArea(boolean horizontal, JColorPalette colorPalette, Dimension preferredDimension) {
        double[] x;
        double[] y;
        double[][] _matrix;
        int max = 100;
        if (horizontal) {
            x = Maths.dsteps(max, 0.0, -1.0);
            y = Maths.dsteps(0.0, 0, 1.0);
            _matrix = new double[][]{x};
        } else {
            x = Maths.dsteps(0.0, 0, 1.0);
            y = Maths.dsteps(max, 0.0, -1.0);
            _matrix = new double[y.length][1];
            for (int i = 0; i < y.length; i++) {
                _matrix[i][0] = y[i];
            }
            //_matrix = new DMatrix(new double[][]{x}).transpose().getDoubleArray();
        }
        init(new ValuesPlotXYDoubleModelFace(x, y, _matrix, null, null), false, colorPalette, preferredDimension);
    }

    private void init(ValuesPlotXYDoubleModelFace model, boolean reverseY, JColorPalette colorPalette, Dimension preferredDimension) {
        addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {

            }

            public void mouseMoved(MouseEvent e) {
                onMouseMove(e);
            }
        });
        addMouseListener(this);
        setModel(model, reverseY, colorPalette, preferredDimension);
    }

    public void setModel(ValuesPlotXYDoubleModelFace model, boolean reverseY, JColorPalette colorPalette, Dimension preferredDimension) {
        if (preferredDimension == null) {
            preferredDimension = new Dimension(400, 400);
        }
        setPreferredDimension(preferredDimension);
        setColorPalette(colorPalette);
        setModel(model, reverseY);
    }

    public void setModel(ValuesPlotXYDoubleModelFace model, boolean reverseY) {
        setxAxis(model.getX());
        double[] y = model.getY();
        double[][] matrix = model.getZ();
        if (reverseY) {
            double[][] matrix2 = new double[matrix.length][];
            for (int i = 0; i < matrix.length; i++) {
                matrix2[i] = matrix[matrix.length - 1 - i];
            }
            if (y != null) {
                double[] y2 = new double[y.length];
                for (int i = 0; i < y.length; i++) {
                    y2[i] = y[y.length - 1 - i];
                }
                setData(matrix2);
                setyAxis(y2);
            } else {
                setData(matrix2);
                setyAxis(y);
            }
        } else {
            setData(matrix);
            setyAxis(y);
        }
    }

    public void mouseExited(MouseEvent e) {
        current_yi = -1;
        current_xj = -1;
        repaint();
    }

    public void mouseDragged(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {
        onMouseMove(e);
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public JPopupMenu getComponentPopupMenu() {
        JPopupMenu popupMenu = super.getComponentPopupMenu();
        if (popupMenu == null) {
            popupMenu = new JPopupMenu();
            JMenuItem item = null;
//        get = new JMenuItem("Couleur");
            ButtonGroup group = new ButtonGroup();
            item = new JCheckBoxMenuItem("Couleur", true);
            group.add(item);
            item.addActionListener(new SerializableActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JColorPalette old = getColorPalette();
                    setColorPalette(HSBColorPalette.DEFAULT_PALETTE.derivePaletteReverse(old.isReverse()).derivePaletteSize(old.getSize()));
                }
            });
            popupMenu.add(item);

//        get = new JMenuItem("Variation de Gris");
            item = new JCheckBoxMenuItem("Variation de Gris", false);
            item.addActionListener(new SerializableActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JColorPalette old = getColorPalette();
                    setColorPalette(HSBColorPalette.GRAY_PALETTE.derivePaletteReverse(old.isReverse()).derivePaletteSize(old.getSize()));
                }
            });
            group.add(item);
            popupMenu.add(item);
            popupMenu.addSeparator();

            item = new JMenuItem("Faible contrast");
            item.addActionListener(new SerializableActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setColorContrast(-1);
                }
            });
            popupMenu.add(item);

            item = new JMenuItem("Haut Contrast 512");
            item.addActionListener(new SerializableActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setColorContrast(512);
                }
            });
            popupMenu.add(item);

            item = new JMenuItem("Haut Contrast 256");
            item.addActionListener(new SerializableActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setColorContrast(256);
                }
            });
            popupMenu.add(item);

            item = new JMenuItem("Haut Contrast 32");
            item.addActionListener(new SerializableActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setColorContrast(32);
                }
            });
            popupMenu.add(item);


            item = new JMenuItem("Haut Contrast 16");
            item.addActionListener(new SerializableActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setColorContrast(16);
                }
            });
            popupMenu.add(item);

            item = new JMenuItem("Haut Contrast 8");
            item.addActionListener(new SerializableActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setColorContrast(8);
                }
            });
            popupMenu.add(item);

            item = new JMenuItem("Haut Contrast 4");
            item.addActionListener(new SerializableActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setColorContrast(4);
                }
            });
            popupMenu.add(item);

            item = new JMenuItem("Monochrome");
            item.addActionListener(new SerializableActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setColorContrast(2);
                }
            });
            popupMenu.add(item);

            popupMenu.addSeparator();
            item = new JCheckBoxMenuItem("Inverser", getColorPalette().isReverse());
            item.addActionListener(new SerializableActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JCheckBoxMenuItem i = (JCheckBoxMenuItem) e.getSource();
                    setColorPalette(getColorPalette().derivePaletteReverse(i.isSelected()));
                }
            });
            popupMenu.add(item);


            popupMenu.addSeparator();
            item = new JMenuItem("Rotation Gauche");
            item.addActionListener(new SerializableActionListener() {
                public void actionPerformed(ActionEvent e) {
                    rotateLeft();
                }
            });
            popupMenu.add(item);

            item = new JMenuItem("Rotation Droite");
            item.addActionListener(new SerializableActionListener() {
                public void actionPerformed(ActionEvent e) {
                    rotateRight();
                }
            });
            popupMenu.add(item);

            item = new JMenuItem("Mirroir Horizontal");
            item.addActionListener(new SerializableActionListener() {
                public void actionPerformed(ActionEvent e) {
                    flipHorizontally();
                }
            });
            popupMenu.add(item);

            item = new JMenuItem("Mirroir Vertical");
            item.addActionListener(new SerializableActionListener() {
                public void actionPerformed(ActionEvent e) {
                    flipVertically();
                }
            });
            popupMenu.add(item);

            setComponentPopupMenu(popupMenu);
        }
        return popupMenu;
    }

    public void setPreferredDimension(Dimension preferredDimension) {
//        float factor = (matrix.length > 0 && matrix[0].length > 0) ? ((float) matrix.length / (float) matrix[0].length) : 1;
        float factor = 1;
        Dimension dim = (factor <= 1) ?
                new Dimension(preferredDimension.width, (int) (preferredDimension.height * factor))
                : new Dimension((int) (preferredDimension.width / factor), preferredDimension.height);
        setMinimumSize(dim);
        setPreferredSize(dim);
        setSize(dim);
    }

    public void setData(double[][] matrix) {
        MinMax oldZMinMax = new MinMax();
        oldZMinMax.registerValue(minValue);
        oldZMinMax.registerValue(maxValue);

        minValue = Double.NaN;
        maxValue = Double.NaN;

        if (matrix != null) {
            for (int i = 0; i < matrix.length; i++) {
                if (matrix[i] != null) {
                    for (int j = 0; j < matrix[i].length; j++) {
                        double vv = matrix[i][j];
                        if (!Double.isNaN(vv)) {
                            if (Double.isNaN(minValue) || vv < minValue) {
                                minValue = vv;
                            }
                            if (Double.isNaN(maxValue) || vv > maxValue) {
                                maxValue = vv;
                            }
                        }
                    }
                }
            }
        }

        this.sourceMatrix = matrix;
        rowsCount = sourceMatrix == null ? 0 : sourceMatrix.length;
        columnsCount = 0;
        if (rowsCount > 0) {
            for (double[] doubles : sourceMatrix) {
                if (columnsCount < doubles.length) {
                    columnsCount = doubles.length;
                }
            }
        }
        for (int i = 0; i < rowsCount; i++) {
            if (sourceMatrix[i].length < columnsCount) {
                double[] newRef = new double[columnsCount];
                System.arraycopy(sourceMatrix[i], 0, newRef, 0, sourceMatrix[i].length);
                for (int j = sourceMatrix[i].length; j < newRef.length; j++) {
                    newRef[j] = Double.NaN;
                }
                sourceMatrix[i] = newRef;
            }
        }
        this.matrix = normalizer.normalize(sourceMatrix);
        MinMax zMinMax = new MinMax();
        zMinMax.registerValue(minValue);
        zMinMax.registerValue(maxValue);
        firePropertyChange("zMinMax", oldZMinMax, zMinMax);
    }

    public HeatMapPlotNormalizer getNormalizer() {
        return normalizer;
    }

    public void setNormalizer(HeatMapPlotNormalizer normalizer) {
        this.normalizer = normalizer;
        if (sourceMatrix != null) {
            this.matrix = normalizer.normalize(sourceMatrix);
        }
    }

    private double getSourceMatrixCell(int r, int c) {
        if (r >= 0 && r < rowsCount && c >= 0 && c <= columnsCount) {
            double[] row = this.sourceMatrix[r];
            if (c < row.length) {
                return row[c];
            }
        }
        return Double.NaN;
    }

    private double getMatrixCell(int r, int c) {
        if (r >= 0 && r < rowsCount && c >= 0 && c <= columnsCount) {
            double[] row = this.matrix[r];
            if (c < row.length) {
                return row[c];
            }
        }
        return Double.NaN;
    }

    private void onMouseMove(MouseEvent e) {
        HeatMapCell currentCell = getCurrentCell(e);
        if (currentCell == null) {
            return;
        }
        int yi = currentCell.getyIndex();
        int xj = currentCell.getxIndex();
        if (isUseTooltips()) {
            StringBuilder sb = new StringBuilder();
            sb.append("<html>");
            sb.append("<B>value=</B>").append(getSourceMatrixCell(yi, xj));
            if (xAxis != null && xAxis.length > yi && xAxis[xj] != null) {
                sb.append(" ;<BR><B>x=</B>").append(xAxis[xj]);
            } else {
                sb.append(" ;<BR><B>x=</B>").append(xj + 1);
            }
            if (yAxis != null && yAxis.length > xj && yAxis[yi] != null) {
                sb.append(" ;<BR><B>y=</B>").append(yAxis[yi]);
            } else {
                sb.append(" ;<BR><B>y=</B>").append(yi + 1);
            }
            sb.append(" ;<BR><B>%</B>=").append(matrix[yi][xj] * 100);
            sb.append("</html>");//.append(matrix[i][j] * 100);
            setToolTipText(sb.toString());
        }
        current_yi = yi;
        current_xj = xj;
        repaint();
    }

    public HeatMapCell getCurrentCell(MouseEvent e) {
        Dimension d = getSize();
        int maxX = matrix.length == 0 ? 0 : matrix[0].length;
        int maxY = matrix.length;
        int y = (int) (maxY * ((double) e.getY() / d.height));
        int x = (int) (maxX * ((double) e.getX() / d.width));
        if (maxX == 0 || maxY == 0) {
            return null;
        }
        double zPercent = matrix[y][x];
        double zVal = getSourceMatrixCell(y, x);
        Object[] a = xAxis;
        Object xVal = (a != null && a.length > x && a[x] != null) ? a[x] : (x + 1);
        ;
        a = yAxis;
        Object yVal = (a != null && a.length > y && a[y] != null) ? a[y] : (y + 1);
        return new HeatMapCell(x, y, xVal, yVal, zVal, zPercent);
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
                double dv = matrix[line][column];
                float f = (float) dv;
                int xx = (int) Maths.round(column * x);
                int yy = (int) Maths.round(line * y);
                int ixx = ix;
                int iyy = iy;
                if (column > 0 && (xx > ((int) Maths.round((column - 1) * x)))) {
                    xx--;
                    ixx += 2;
                }
                if (line > 0 && (yy > ((int) Maths.round((line - 1) * y)))) {
                    yy--;
                    iyy += 2;
                }

                if (Double.isNaN(dv)) {
                    if (current_yi == line && current_xj == column) {
                        g.setColor(Color.LIGHT_GRAY);
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    g.fillRect(xx, yy, ixx, iyy);
                    g.setColor(Color.GREEN);
                    g.drawLine(xx, yy, xx + ixx, yy + iyy);
                    g.drawLine(xx, yy + iyy, xx + ixx, yy);
                } else if (dv == Double.POSITIVE_INFINITY) {
                    if (current_yi == line && current_xj == column) {
                        g.setColor(Color.LIGHT_GRAY);
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    g.fillRect(xx, yy, ixx, iyy);
                    g.setColor(Color.RED);
                    g.drawLine(xx, yy, xx + ixx, yy + iyy);
                    g.drawLine(xx, yy + iyy, xx + ixx, yy);
                } else if (dv == Double.NEGATIVE_INFINITY) {
                    if (current_yi == line && current_xj == column) {
                        g.setColor(Color.LIGHT_GRAY);
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    g.fillRect(xx, yy, ixx, iyy);
                    g.setColor(Color.BLUE);
                    g.drawLine(xx, yy, xx + ixx, yy + iyy);
                    g.drawLine(xx, yy + iyy, xx + ixx, yy);
                } else {
                    Color baseColor = colorPaletteContrasted.getColor(f);
                    g.setColor(baseColor);
                    g.fillRect(xx, yy, ixx, iyy);
                    if (current_yi == line && current_xj == column) {
                        Color brighterColor = colorPaletteContrasted.getColor(f).brighter();
                        Color darkerColor = colorPaletteContrasted.getColor(f).darker();
                        g.setColor(brighterColor);
                        g.fillRect(xx + 1, yy + 1, ixx - 2, iyy - 2);
                    }
                }
            }
        }
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

    public JColorPalette getColorPalette() {
        return colorPalette;
    }

    public void setColorContrast(int contrast) {
        if (contrast <= 1) {
            colorPaletteContrasted = colorPalette;
            this.contrast = -1;
        } else {
            colorPaletteContrasted = this.colorPalette.derivePaletteSize(contrast);
            this.contrast = contrast;
        }
        firePropertyChange("colorPaletteContrasted", null, colorPaletteContrasted);
        if (isShowing()) {
            repaint();
        }
    }

    public JColorPalette getColorPaletteContrasted() {
        return colorPaletteContrasted;
    }

    public void setColorPalette(JColorPalette colorPalette) {
        this.colorPalette = colorPalette != null ? colorPalette : HSBColorPalette.DEFAULT_PALETTE;
        if (contrast <= 1) {
            colorPaletteContrasted = this.colorPalette;
            this.contrast = -1;
        } else {
            colorPaletteContrasted = this.colorPalette.derivePaletteSize(contrast);
        }
        firePropertyChange("colorPalette", null, colorPalette);
        firePropertyChange("colorPaletteContrasted", null, colorPaletteContrasted);
        if (isShowing()) {
            repaint();
        }
    }

    public void rotateRight() {
        sourceMatrix = new DMatrix(sourceMatrix).rotateValuesLeft().getDoubleArray();
        matrix = new DMatrix(matrix).rotateValuesLeft().getDoubleArray();
        if (isShowing()) {
            repaint();
        }
    }

    public void rotateLeft() {
        sourceMatrix = new DMatrix(sourceMatrix).rotateValuesRight().getDoubleArray();
        matrix = new DMatrix(matrix).rotateValuesRight().getDoubleArray();
        if (isShowing()) {
            repaint();
        }
    }

    public void flipHorizontally() {
        sourceMatrix = new DMatrix(sourceMatrix).flipHorizontally().getDoubleArray();
        matrix = new DMatrix(matrix).flipHorizontally().getDoubleArray();
        for (int i = 0; i < xAxis.length / 2; i++) {
            xAxis[i] = xAxis[xAxis.length - 1 - i];
        }
        if (isShowing()) {
            repaint();
        }
    }

    public void flipVertically() {
        sourceMatrix = new DMatrix(sourceMatrix).flipVertically().getDoubleArray();
        matrix = new DMatrix(matrix).flipVertically().getDoubleArray();
        for (int i = 0; i < yAxis.length / 2; i++) {
            yAxis[i] = yAxis[yAxis.length - 1 - i];
        }
        if (isShowing()) {
            repaint();
        }
    }

    public boolean isUseTooltips() {
        return useTooltips;
    }

    public void setUseTooltips(boolean useTooltips) {
        this.useTooltips = useTooltips;
    }

}
