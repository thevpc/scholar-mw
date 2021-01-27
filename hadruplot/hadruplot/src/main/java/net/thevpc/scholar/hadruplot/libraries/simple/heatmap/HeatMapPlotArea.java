package net.thevpc.scholar.hadruplot.libraries.simple.heatmap;

import net.thevpc.common.swing.SerializableActionListener;
import net.thevpc.common.util.ArrayUtils;
import net.thevpc.common.util.MinMax;
import net.thevpc.scholar.hadruplot.HSBColorPalette;
import net.thevpc.scholar.hadruplot.ColorPalette;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruplot.model.ValuesPlotXYDoubleModelFace;
import net.thevpc.scholar.hadruplot.util.PlotUtils;

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
    private ColorPalette colorPaletteContrasted = HSBColorPalette.DEFAULT_PALETTE;
    private ColorPalette colorPalette = HSBColorPalette.DEFAULT_PALETTE;
    int contrast = -1;
    private int current_yi = -1;
    private int current_xj = -1;
    private double yOverXRatio = -1;
    private boolean useTooltips = false;
    private PlotNormalizer normalizer = new DefaultPlotNormalizer();
    private HeatMapRendrer renderer = new HeatMapRendrer() {
        @Override
        public void paintValue(HeatMapPlotArea area, Graphics g, Rectangle bounds, double value, int x, int y, int width, int height, boolean selected) {
        }

        @Override
        public void paintAnnotations(HeatMapPlotArea area, Graphics g) {
        }
    };

//
//    public HeatMapPlotArea(double[] x, double[] y, double[][] matrix) {
//        this(x, y, matrix, null, null);
//    }
//
//    public HeatMapPlotArea(double[] x, double[] y, double[][] matrix, JColorPalette colorPalette) {
//        dsteps(x, y, matrix, colorPalette, null);
//    }
    public HeatMapPlotArea(ValuesPlotXYDoubleModelFace model, boolean reverseY, ColorPalette colorPalette, Dimension preferredDimension) {
        double w=-1;
        double h=-1;
        if(model.getX()!=null && model.getX().length>0){
            w=model.getX()[model.getX().length-1]-model.getX()[0];
        }
        if(model.getY()!=null && model.getY().length>0){
            h=model.getY()[model.getY().length-1]-model.getY()[0];
        }
        double yOverXRatio=-1;
        if(w>0 && h>0){
            yOverXRatio=h/w;
        }
        init(model, yOverXRatio,reverseY, colorPalette, preferredDimension);
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public HeatMapPlotArea(boolean horizontal, double yOverXRatio, ColorPalette colorPalette, Dimension preferredDimension) {
        double[] x;
        double[] y;
        double[][] _matrix;
        int max = 100;
        if (horizontal) {
            x = ArrayUtils.dsteps(max, 0.0, -1.0);
            y = ArrayUtils.dsteps(0.0, 0, 1.0);
            _matrix = new double[][]{x};
        } else {
            x = ArrayUtils.dsteps(0.0, 0, 1.0);
            y = ArrayUtils.dsteps(max, 0.0, -1.0);
            _matrix = new double[y.length][1];
            for (int i = 0; i < y.length; i++) {
                _matrix[i][0] = y[i];
            }
            //_matrix = new DMatrix(new double[][]{x}).transpose().getDoubleArray();
        }
        init(new ValuesPlotXYDoubleModelFace(x, y, _matrix, null, null), yOverXRatio,false, colorPalette, preferredDimension);
    }

    private void init(ValuesPlotXYDoubleModelFace model, double yOverXRatio,boolean reverseY, ColorPalette colorPalette, Dimension preferredDimension) {
        this.yOverXRatio= yOverXRatio;
        addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {

            }

            public void mouseMoved(MouseEvent e) {
                onMouseMove(e);
            }
        });
        addMouseListener(this);
        setModel(model, reverseY, colorPalette, preferredDimension);
        JPopupMenu popupMenu = new JPopupMenu();

        popupMenu.add(createColorPaletteMenu());
        popupMenu.add(createColorContrastMenu());
        popupMenu.add(createRotationMenu());
        setComponentPopupMenu(popupMenu);
    }

    public void setModel(ValuesPlotXYDoubleModelFace model, boolean reverseY, ColorPalette colorPalette, Dimension preferredDimension) {
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

    public JMenu createColorContrastMenu() {
        JMenu popupMenu = new JMenu("Color Contrast");
        ButtonGroup contrastGroup = new ButtonGroup();
        JMenuItem item = null;
        item = new JCheckBoxMenuItem("Low contrast", true);
        item.addActionListener(new SerializableActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorContrast(-1);
            }
        });
        contrastGroup.add(item);
        popupMenu.add(item);

        item = new JCheckBoxMenuItem("Hight Contrast 512");
        item.addActionListener(new SerializableActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorContrast(512);
            }
        });
        contrastGroup.add(item);
        popupMenu.add(item);

        item = new JCheckBoxMenuItem("Hight Contrast 256");
        item.addActionListener(new SerializableActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorContrast(256);
            }
        });
        contrastGroup.add(item);
        popupMenu.add(item);

        item = new JCheckBoxMenuItem("Hight Contrast 32");
        item.addActionListener(new SerializableActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorContrast(32);
            }
        });
        contrastGroup.add(item);
        popupMenu.add(item);

        item = new JCheckBoxMenuItem("Hight Contrast 16");
        item.addActionListener(new SerializableActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorContrast(16);
            }
        });
        contrastGroup.add(item);
        popupMenu.add(item);

        item = new JCheckBoxMenuItem("Hight Contrast 8");
        item.addActionListener(new SerializableActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorContrast(8);
            }
        });
        contrastGroup.add(item);
        popupMenu.add(item);

        item = new JCheckBoxMenuItem("Hight Contrast 4");
        item.addActionListener(new SerializableActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorContrast(4);
            }
        });
        contrastGroup.add(item);
        popupMenu.add(item);

        item = new JCheckBoxMenuItem("Monochrome");
        item.addActionListener(new SerializableActionListener() {
            public void actionPerformed(ActionEvent e) {
                setColorContrast(2);
            }
        });
        contrastGroup.add(item);
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
        return popupMenu;
    }

    public JMenu createColorPaletteMenu() {
        JMenu popupMenu = new JMenu("Color Palette");
        JMenuItem item = null;
//        get = new JMenuItem("Couleur");
        ButtonGroup palettesGroup = new ButtonGroup();
        ColorPalette[] availableColorPalettes = Plot.getAvailableColorPalettes();
        for (int i = 0; i < availableColorPalettes.length; i++) {
            final ColorPalette p = availableColorPalettes[i];
            item = new JCheckBoxMenuItem(p.getName(), i == 0);
            palettesGroup.add(item);
            item.addActionListener(new SerializableActionListener() {
                public void actionPerformed(ActionEvent e) {
                    ColorPalette old = getColorPalette();
                    setColorPalette(p.derivePaletteReverse(old.isReverse()).derivePaletteSize(old.getSize()));
                }
            });
            popupMenu.add(item);
        }

        return popupMenu;
    }

    public JMenu createRotationMenu() {
        JMenu popupMenu = new JMenu("Transformations");
        JMenuItem item = null;

        item = new JMenuItem("Rotate Left");
        item.addActionListener(new SerializableActionListener() {
            public void actionPerformed(ActionEvent e) {
                rotateLeft();
            }
        });
        popupMenu.add(item);

        item = new JMenuItem("Rotate Right");
        item.addActionListener(new SerializableActionListener() {
            public void actionPerformed(ActionEvent e) {
                rotateRight();
            }
        });
        popupMenu.add(item);

        item = new JMenuItem("Horizontal Mirror");
        item.addActionListener(new SerializableActionListener() {
            public void actionPerformed(ActionEvent e) {
                flipHorizontally();
            }
        });
        popupMenu.add(item);

        item = new JMenuItem("Vertical Mirror");
        item.addActionListener(new SerializableActionListener() {
            public void actionPerformed(ActionEvent e) {
                flipVertically();
            }
        });
        popupMenu.add(item);
        return popupMenu;
    }

    public void setPreferredDimension(Dimension preferredDimension) {
//        float factor = (matrix.length > 0 && matrix[0].length > 0) ? ((float) matrix.length / (float) matrix[0].length) : 1;
        float factor = 1;
        Dimension dim = (factor <= 1)
                ? new Dimension(preferredDimension.width, (int) (preferredDimension.height * factor))
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

    public PlotNormalizer getNormalizer() {
        return normalizer;
    }

    public void setNormalizer(PlotNormalizer normalizer) {
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
        Rectangle d = getAreaBounds();
        int maxX = matrix.length == 0 ? 0 : matrix[0].length;
        int maxY = matrix.length;
        int y = (int) (maxY * (((double) e.getY()-d.y) / d.height));
        int x = (int) (maxX * (((double) e.getX()-d.x) / d.width));
        if (maxX == 0 || maxY == 0) {
            return null;
        }
        if (y<0 || y>=matrix.length) {
            return null;
        }
        if (x<0 || x>=matrix[y].length) {
            return null;
        }
        double zPercent = matrix[y][x];
        double zVal = getSourceMatrixCell(y, x);
        Object[] a = xAxis;
        Object xVal = (a != null && a.length > x && a[x] != null) ? a[x] : (x + 1);
        a = yAxis;
        Object yVal = (a != null && a.length > y && a[y] != null) ? a[y] : (y + 1);
        return new HeatMapCell(x, y, xVal, yVal, zVal, zPercent);
    }

    public Rectangle getAreaBounds() {
        Dimension size = getSize();
        if(yOverXRatio<=0){
            return new Rectangle(0,0,size.width,size.height);
        }
        double actualRatio=size.getHeight()/size.getWidth();
        if(yOverXRatio<=1){
            if(actualRatio<yOverXRatio){
                int w=(int)(size.height/yOverXRatio);
                int h=size.height;
                int x=(size.width-w)/2;
                int y=0;
                return new Rectangle(x,y,w,h);
            }else {
                int w = size.width;
                int h = (int) (size.width * yOverXRatio);
                int x = 0;
                int y = (size.height - h) / 2;
                return new Rectangle(x, y, w, h);
            }
        }else {
            if(actualRatio>yOverXRatio){
                int w = size.width;
                int h = (int) (size.width * yOverXRatio);
                int x = 0;
                int y = (size.height - h) / 2;
                return new Rectangle(x, y, w, h);
            }else {
                int w = (int) (size.height / yOverXRatio);
                int h = size.height;
                int x = (size.width - w) / 2;
                int y = 0;
                return new Rectangle(x, y, w, h);
            }
        }
    }

    public Dimension getAreaSize() {
        return getSize();
    }
    @Override
    public void paint(Graphics g) {
        Dimension ad = getSize();
        double y = 0;
        double x = 0;
        g.setColor(Color.white);
        g.fillRect(0, 0, ad.width, ad.height);
        Rectangle rb = getAreaBounds();
        int x0=rb.x;
        int y0=rb.y;
        if (matrix.length > 0 && matrix[0].length > 0) {
            x = 1.0 * rb.width / matrix[0].length;
            y = 1.0 * rb.height / matrix.length;
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
                int xx = (int) Math.round(column * x);
                int yy = (int) Math.round(line * y);
                int ixx = ix;
                int iyy = iy;
                if (column > 0 && (xx > ((int) Math.round((column - 1) * x)))) {
                    xx--;
                    ixx += 2;
                }
                if (line > 0 && (yy > ((int) Math.round((line - 1) * y)))) {
                    yy--;
                    iyy += 2;
                }

                if (Double.isNaN(dv)) {
                    if (current_yi == line && current_xj == column) {
                        g.setColor(Color.LIGHT_GRAY);
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    g.fillRect(x0+xx, y0+yy, ixx, iyy);
                    g.setColor(Color.GREEN);
                    g.drawLine(x0+xx, y0+yy, x0+xx + ixx, y0+yy + iyy);
                    g.drawLine(x0+xx, y0+yy + iyy, x0+xx + ixx, y0+yy);
                } else if (dv == Double.POSITIVE_INFINITY) {
                    if (current_yi == line && current_xj == column) {
                        g.setColor(Color.LIGHT_GRAY);
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    g.fillRect(x0+xx, y0+yy, ixx, iyy);
                    g.setColor(Color.RED);
                    g.drawLine(x0+xx, y0+yy, x0+xx + ixx, y0+yy + iyy);
                    g.drawLine(x0+xx, y0+yy + iyy, x0+xx + ixx, y0+yy);
                } else if (dv == Double.NEGATIVE_INFINITY) {
                    if (current_yi == line && current_xj == column) {
                        g.setColor(Color.LIGHT_GRAY);
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    g.fillRect(x0+xx, y0+yy, ixx, iyy);
                    g.setColor(Color.BLUE);
                    g.drawLine(x0+xx, y0+yy, x0+xx + ixx, y0+yy + iyy);
                    g.drawLine(x0+xx, y0+yy + iyy, x0+xx + ixx, y0+yy);
                } else {
                    Color baseColor = colorPaletteContrasted.getColor(f);
                    g.setColor(baseColor);
                    g.fillRect(x0+xx, y0+yy, ixx, iyy);
                    if (current_yi == line && current_xj == column) {
                        Color brighterColor = colorPaletteContrasted.getColor(f).brighter();
//                        Color darkerColor = colorPaletteContrasted.getColor(f).darker();
                        g.setColor(brighterColor);
                        g.fillRect(x0+xx + 1, y0+yy + 1, ixx - 2, iyy - 2);
                    }
                }
                renderer.paintValue(this, g, rb, dv, x0+xx, y0+yy, ixx, iyy, current_yi == line && current_xj == column);
            }
        }
        renderer.paintAnnotations(this, g);
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

    public ColorPalette getColorPalette() {
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

    public ColorPalette getColorPaletteContrasted() {
        return colorPaletteContrasted;
    }

    public void setColorPalette(ColorPalette colorPalette) {
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
        sourceMatrix = PlotUtils.rotateValuesLeft(sourceMatrix);
        matrix = PlotUtils.rotateValuesLeft(matrix);
        if (isShowing()) {
            repaint();
        }
    }

    public void rotateLeft() {
        sourceMatrix = PlotUtils.rotateValuesRight(sourceMatrix);
        matrix = PlotUtils.rotateValuesRight(matrix);
        if (isShowing()) {
            repaint();
        }
    }

    public void flipHorizontally() {
        sourceMatrix = PlotUtils.flipHorizontally(sourceMatrix);
        matrix = PlotUtils.flipHorizontally(matrix);
        for (int i = 0; i < xAxis.length / 2; i++) {
            xAxis[i] = xAxis[xAxis.length - 1 - i];
        }
        if (isShowing()) {
            repaint();
        }
    }

    public void flipVertically() {
        sourceMatrix = PlotUtils.flipVertically(sourceMatrix);
        matrix = PlotUtils.flipVertically(matrix);
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
