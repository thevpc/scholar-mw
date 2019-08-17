package net.vpc.scholar.hadruplot.backends.simple.curve;


import net.vpc.common.util.MinMax;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.text.DecimalFormat;

public class CurveStat extends Graph {

    //In parameter :
    private Color gridBg = new Color(225, 225, 225);
    private Color gridColor = Color.black;
    private Curve[] curves;
    private double xUserUnit = 100;
    private double yUserUnit = 100;
    private boolean displayGrid = true;        //Display the grid or no

    //Work parameter :
    private int nbrCurves;
    private int totPoints = 0;
    private int legendWidth = 0;            // The legend width

    private int legendHeight = 0;        // The legend height

    private Point[] pointsCoord;
    private double xScale = -1;
    private double xMaxValue = 0;
    private double pxMaxValue = 0;
    private double mxMaxValue = 0;
    private double xUnit = 5;
    private int pxUserNbr;
    private int mxUserNbr;
    private int xLabsMaxLen = 0;
    private double yScale = -1;
    private double yMaxValue = 0;
    private double pyMaxValue = 0;
    private double myMaxValue = 0;
    private double yUnit = 100;
    private int pyUserNbr;
    private int myUserNbr;
    private int yLabsMaxLen = 0;
    private double xxMax = Double.NaN;
    private double yyMax = Double.NaN;
    DecimalFormat df = new DecimalFormat("0.###");

    public CurveStat(Curve[] curves, String graphTitle, double xUsrUnit, double yUsrUnit) {

        //setTextFont(Swings.getDefaultFont());
        //setTitleFont(Swings.getDefaultBigFont());
        setBackground(Color.white);
        setTitle(graphTitle);
        if (xUsrUnit > 0 && yUsrUnit > 0) {
            this.xUserUnit = xUsrUnit;
            this.yUserUnit = yUsrUnit;
        } else {
            throw new IllegalArgumentException("Graphics Graph NullUnit");
        }
        setCurves(curves);
    }

    @Override
    public void paint(Graphics g) {
        if (totPoints > 0) {
            setGraphWidth(this.getWidth());
            setGraphHeight(this.getHeight());
            paintGraph(g);
        } else {
            g.setColor(getPaperColor());
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setFont(getTextFont());
            g.setColor(getTextColor());
            String message = "Graphics.Graph.EmptyTables";
            int cx = Math.max((getWidth() - getMetrics().stringWidth(message)) / 2, 0);
            int cy = Math.max(getHeight() / 2 - getFontMetrics(getTitleFont()).getDescent(), 0);
            g.drawString(message, cx, cy);
        }
    }

    public void paintGraph(Graphics graph) {

        Graphics2D g = (Graphics2D) graph;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(getPaperColor());
        g.fillRect(0, 0, getWidth(), getHeight());

        int nbSupp = Math.max(getWidth() / legendWidth, 1);
        legendHeight = ((nbrCurves / nbSupp) + 1) * 20 + getMetrics().getHeight();
        xScale = (getWidth() - yLabsMaxLen - 20) / xMaxValue;
        yScale = (getHeight() - getFontMetrics(getTitleFont()).getHeight() - legendHeight - xLabsMaxLen * Math.sin(Math.PI / 4) - 20) / yMaxValue;
        int xValXscale = (int) Math.round(xMaxValue * xScale) + 1;
        int yValXscale = (int) Math.round(yMaxValue * yScale) + 1;

        // draw the getTitle() centered at the bottom of the graph
        int titleWidth = getMetrics().stringWidth(getTitle());
        int cx = Math.max((getWidth() - titleWidth) / 2, 0);
        int cy = getHeight() - getMetrics().getDescent();
        g.setFont(getTitleFont());
        g.setColor(getTextColor());
        g.drawString(getTitle(), cx, cy);
        xUserUnit = getWidth() / curves[0].getXValues().length;
        yUserUnit = getHeight() / curves[0].getXValues().length;
        //setXUnit(xUserUnit);
        //setYUnit(yUserUnit);
        if (xValXscale > 10 && yValXscale > 10) {

            cx = Math.max(((getWidth() - xValXscale) / 2), yLabsMaxLen + 5);
            cy = getHeight() - getFontMetrics(getTitleFont()).getHeight() - legendHeight - (int) (xLabsMaxLen * Math.sin(Math.PI / 4)) - 5;
            cy = (int) Math.round(cy - (yMaxValue * yScale));

            g.setFont(getTextFont());

            if (this.displayGrid) {

                int pxNbr = 0, mxNbr = 0;
                if (curves[0].getXInType() == GraphConstants.IN_STRINGS) {
                    pxNbr = curves[0].getXLabels().length;
                    if ((int) (xUserUnit * xScale) < 15) {
                        xUnit = 15 * xScale;
                    } else {
                        xUnit = xUserUnit;
                    }
                } else {
                    if ((int) (xUserUnit * xScale) < 15) {
                        if (pxMaxValue != 0) {
                            pxNbr = (int) (pxMaxValue * xScale) / 15 + 1;
                        }
                        if (mxMaxValue != 0) {
                            mxNbr = (int) (Math.abs(mxMaxValue * xScale) / 15) + 1;
                        }
                        xUnit = xMaxValue / (pxNbr + mxNbr);
                        xValXscale = (int) Math.round(xUnit * (pxNbr + mxNbr + 1) * xScale);

                    } else {
                        xUnit = xUserUnit;
                        pxNbr = pxUserNbr;
                        mxNbr = mxUserNbr;
                    }
                }

                int pyNbr = 0, myNbr = 0;
                if (curves[0].getYInType() == GraphConstants.IN_STRINGS) {
                    pyNbr = curves[0].getYLabels().length;
                    if ((int) (yUserUnit * yScale) < 15) {
                        yUnit = 15 / yScale;
                    } else {
                        yUnit = yUserUnit;
                    }
                } else {
                    if ((int) (yUserUnit * yScale) < 15) {
                        if (pyMaxValue != 0) {
                            pyNbr = (int) (pyMaxValue * yScale) / 15 + 1;
                        }
                        if (myMaxValue != 0) {
                            myNbr = (int) (Math.abs(myMaxValue * yScale) / 15) + 1;
                        }
                        yUnit = yMaxValue / (pyNbr + myNbr);
                        yValXscale = (int) Math.round(yUnit * (pyNbr + myNbr + 1) * yScale);

                    } else {
                        yUnit = yUserUnit;
                        pyNbr = pyUserNbr;
                        myNbr = myUserNbr;
                    }
                }

                // draw the Grid of the graph
                g.setColor(gridBg);
                g.fillRect(cx, cy, xValXscale, yValXscale);
                g.setColor(gridColor);
                int X = cx - (int) Math.round(mxMaxValue * xScale);
                AffineTransform at = new AffineTransform();
                for (int l = (0 - mxNbr); l < pxNbr + 1; l++) {
                    if (l == 0) {
                        g.fillRect(X - 1, cy, 3, yValXscale);
                    } else {
                        g.drawLine(X + (int) Math.round(xUnit * xScale * l), cy, X + (int) Math.round(xUnit * xScale * l), cy + yValXscale);
                        String val = "";
                        if (curves[0].getXInType() == GraphConstants.IN_STRINGS) {
                            val = curves[0].getXLabels()[l - 1];
                        } else {
                            val = df.format(xUnit * l);
                        }
                        int CY = (int) (cy + yValXscale + getMetrics().stringWidth(val) * Math.sin(Math.PI / 4) + 7);
                        int CX = (int) (X + (int) Math.round(xUnit * xScale * l) - getMetrics().stringWidth(val) * Math.cos(Math.PI / 4));
                        at.setToRotation(-Math.PI / 4, CX, CY);
                        g.transform(at);
                        g.drawString(val, CX, CY);
                        at.setToRotation(Math.PI / 4, CX, CY);
                        g.transform(at);
                    }
                }
                yValXscale = (int) Math.round(pyMaxValue * yScale) + 1;
                for (int l = (0 - myNbr); l < pyNbr + 1; l++) {
                    if (l == 0) {
                        g.fillRect(cx, cy + yValXscale - 1, xValXscale, 3);
                        g.drawString("0", cx - getMetrics().stringWidth("0") - 5, cy + yValXscale);
                    } else {
                        g.drawLine(cx, cy + yValXscale - ((int) Math.round(yUnit * yScale * l)), cx + xValXscale, cy + yValXscale - ((int) Math.round(yUnit * yScale * l)));
                        String val = "";
                        if (curves[0].getYInType() == GraphConstants.IN_STRINGS) {
                            val = curves[0].getYLabels()[l - 1];
                        } else {
                            val = df.format(yUnit * l);
                        }
                        X = 2 + yLabsMaxLen - getMetrics().stringWidth(val);
                        g.drawString(val, X, cy + yValXscale - ((int) Math.round(yUnit * yScale * l)));
                    }
                }
            }

            // draw the legends ofcurves
            int fstXLab = 0;
            for (int i = 0; i < nbrCurves; i++) {
                int xlab;
                int ylab = getHeight() - legendHeight - getFontMetrics(getTitleFont()).getHeight() + 20 * ((i / nbSupp) + 1);//pieBorderWidth+pieRadius+pieHight+20*((i/nbSupp)+1);

                if (i % nbSupp == 0) {
                    fstXLab = Math.max((getWidth() - Math.min(nbSupp, nbrCurves - i) * legendWidth) / 2, 0);
                    xlab = fstXLab;
                } else {
                    xlab = fstXLab + ((i % nbSupp) * legendWidth);
                }

                g.setColor(Color.BLACK);
                //g.setColor(curves[i].getLineColor());
                g.drawLine(xlab - 5, ylab + 6, xlab + 17, ylab + 6);
                g.setColor(Color.BLACK);
                //g.setColor(curves[i].getPointsColor());
                g.fillRect(xlab, ylab, 12, 12);
                //g.setColor(gridColor);
                g.setColor(Color.BLACK);
                g.drawRect(xlab, ylab, 12, 12);
                //g.setColor(getTextColor());
                g.setColor(Color.BLACK);
                g.drawString(curves[i].getTitle(), xlab + 20, ylab + 12);
            }

            // draw graph
            int k = 0;
            for (int i = 0; i < nbrCurves; i++) {
                paintCurve(g, i, k);
                k += curves[i].getPointsNumber();
            }

            // draw the coordinate of the Point
            this.addMouseMotionListener(new MouseMotionAdapter() {

                public void mouseMoved(MouseEvent e) {
                    int xMousePos = e.getX();
                    int yMousePos = e.getY();

                    int[] result = getSelectedPoint(xMousePos, yMousePos);
                    if (result[0] != -1) {
                        String valx, valy;
                        if (curves[result[1]].getXInType() == GraphConstants.IN_STRINGS) {
                            valx = curves[result[1]].getXLabels()[result[0]];
                        } else {
                            valx = df.format(curves[result[1]].getXValues()[result[0]]);
                        }
                        if (curves[result[1]].getYInType() == GraphConstants.IN_STRINGS) {
                            valy = curves[result[1]].getYLabels()[result[0]];
                        } else {
                            valy = df.format(curves[result[1]].getYValues()[result[0]]);
                        }
                        String ttText = "<html>" + ("Graphics.Curve.XValue") + " : <font color='red'>" + valx + "</font><br>" +
                                ("Graphics.Curve.YValue") + " : <font color='red'>" + valy + "</font></html>";
                        setToolTipText(ttText);
                    } else {
                        setToolTipText(null);
                    }
                }
            });
        } else {
            String message = ("Graphics.Graph.TooTiny");
            cx = Math.max((getWidth() - getMetrics().stringWidth(message)) / 2, 0);
            cy = Math.max(getHeight() / 2 - getFontMetrics(getTitleFont()).getDescent(), 0);
            g.drawString(message, cx, cy);

        }

    }

    private void paintCurve(Graphics2D g, int rngGraph, int pointsDrawed) {

        int oldCx = -1;
        int oldCy = -1;
        int nbrPoints = curves[rngGraph].getPointsNumber();
        double[] xValues = curves[rngGraph].getXValues();
        double[] yValues = curves[rngGraph].getYValues();
        Color ptColor = curves[rngGraph].getPointsColor();
        Color lnColor = curves[rngGraph].getLineColor();
        ptColor = Color.BLACK;
        lnColor = Color.BLACK;

        for (int i = 0; i < nbrPoints; i++) {

            int widthOfItems = (int) Math.round((xMaxValue * xScale) + 1);

            // Coordinates for this Point
            int cx = Math.max(((getWidth() - widthOfItems) / 2), yLabsMaxLen + 5);
            cx = cx - (int) Math.round(mxMaxValue * xScale);
            cx = (int) Math.round(cx + (xValues[i] * xScale));
            int cy = getHeight() - getFontMetrics(getTitleFont()).getHeight() - legendHeight - (int) (xLabsMaxLen * Math.sin(Math.PI / 4)) - 5;
            cy = cy + (int) Math.round(myMaxValue * yScale);
            cy = cy - Math.round((float) (yValues[i] * yScale)) + 2;

            // draw the Point
            pointsCoord[pointsDrawed + i] = new Point(cx - 3, cy - 3);
            g.setColor(ptColor);
            try {
                g.fillRect(cx - 3, cy - 3, 7, 7);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            //draw the Line between two points
            g.setColor(lnColor);
            if (oldCx != -1 && oldCy != -1 && !Double.isInfinite(cy) && !Double.isNaN(cy) && !Double.isInfinite(cx) && !Double.isNaN(cx)) {
                try {
                    g.drawLine(oldCx, oldCy, cx, cy);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
            oldCx = cx;
            oldCy = cy;
        }
    }

    public int[] getSelectedPoint(int x, int y) {

        int k = 0;
        int[] result = new int[2];
        for (int i = 0; i < nbrCurves; i++) {
            for (int n = 0; n < curves[i].getPointsNumber(); n++) {
                int X1 = pointsCoord[k].x;
                int X2 = X1 + 7;
                int Y1 = pointsCoord[k].y;
                int Y2 = Y1 + 7;
                if (x >= X1 && x <= X2 && y >= Y1 && y <= Y2) {
                    result[0] = n;
                    result[1] = i;
                    return result;
                }
                k++;
            }
        }
        result[0] = -1;
        result[1] = -1;
        return result;
    }

    private boolean coherentLabels(String[] labs1, String[] labs2) {
        if (labs1.length != labs2.length) {
            return false;
        } else {
            for (int i = 0; i < labs1.length; i++) {
                if (labs1[i] == null) {
                    if (labs2[i] != null) {
                        return false;
                    }
                } else if (!labs1[i].equals(labs2[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    public Curve[] getCurves() {
        return curves;
    }

    public void setCurves(Curve[] crv) {
        this.curves = crv;
        nbrCurves = curves.length;
        int xIntype = GraphConstants.IN_DOUBLES, yIntype = GraphConstants.IN_DOUBLES;
        String[] xLabs = new String[]{}, yLabs = new String[]{};
        if (nbrCurves > 0) {
            xIntype = curves[0].getXInType();
            yIntype = curves[0].getYInType();
            xLabs = curves[0].getXLabels();
            yLabs = curves[0].getYLabels();
        }
        for (int i = 0; i < nbrCurves; i++) {
            MinMax xmm = new MinMax();
            MinMax ymm = new MinMax();
            for (int j = 0; j < curves[i].getXLabels().length; j++) {
                double[] xv = curves[i].getXValues();
                double[] yv = curves[i].getYValues();
                for (int ii = 0; ii < xv.length; ii++) {
                    xmm.registerValue(xv[i]);
                    ymm.registerValue(yv[i]);
                }
            }
            xxMax = xmm.getMax();
            yyMax = ymm.getMax();
            if (curves[i].getXInType() == xIntype) {
                if (xIntype == GraphConstants.IN_STRINGS) {
                    if (!coherentLabels(xLabs, curves[i].getXLabels())) {
                        throw new IllegalArgumentException("Graphics.Curve.ErrData");
                    }
                } else {
                    double tempXMax = curves[i].getXMax();
                    double tempXMin = curves[i].getXMin();
                    if (tempXMax > pxMaxValue) {
                        pxMaxValue = tempXMax;
                    }
                    if (tempXMin < mxMaxValue) {
                        mxMaxValue = tempXMin;
                    }
                }
            } else {
                throw new IllegalArgumentException("Graphics.Curve.ErrData");
            }

            if (curves[i].getYInType() == yIntype) {
                if (yIntype == GraphConstants.IN_STRINGS) {
                    if (!coherentLabels(yLabs, curves[i].getYLabels())) {
                        throw new IllegalArgumentException(("Graphics.Curve.ErrData"));
                    }
                } else {
                    double tempYMax = curves[i].getYMax();
                    double tempYMin = curves[i].getYMin();
                    if (tempYMax > pyMaxValue) {
                        pyMaxValue = tempYMax;
                    }
                    if (tempYMin < myMaxValue) {
                        myMaxValue = tempYMin;
                    }
                }
            } else {
                throw new IllegalArgumentException(("Graphics.Curve.ErrData"));
            }
            for (int j = 0; j < curves[i].getXLabels().length; j++) {
                if (getMetrics().stringWidth(curves[i].getXLabels()[j]) > xLabsMaxLen) {
                    xLabsMaxLen = getMetrics().stringWidth(curves[i].getXLabels()[j]);
                }
            }
            for (int j = 0; j < curves[i].getYLabels().length; j++) {
                if (getMetrics().stringWidth(curves[i].getYLabels()[j]) > yLabsMaxLen) {
                    yLabsMaxLen = getMetrics().stringWidth(curves[i].getYLabels()[j]);
                }
            }

            totPoints += curves[i].getPointsNumber();
            int temp = 20 + getMetrics().stringWidth(curves[i].getTitle());
            if (legendWidth < temp) {
                legendWidth = temp;
            }
        }
        pointsCoord = new Point[totPoints];
        setXUnit(xUserUnit);
        setYUnit(yUserUnit);
        legendHeight = ((nbrCurves / 2) + 1) * 20;
    }

    public double getXUnit() {
        return xUnit;
    }

    public void setXUnit(double xUserUnit) {
        if (xUserUnit > 0) {
            this.xUserUnit = xUserUnit;
            for (int n = 0; n < nbrCurves; n++) {
                curves[n].setXLabelsUnit((int) xUserUnit);
            }
        } else {
            throw new IllegalArgumentException(("Graphics.Graph.NullUnit"));
        }
        if (curves[0].getXInType() == GraphConstants.IN_STRINGS) {
            pxUserNbr = (curves[0].getXLabels().length + 1);
            pxMaxValue = xUserUnit * pxUserNbr;
            mxUserNbr = 0;
            mxMaxValue = 0;
            xMaxValue = pxMaxValue;
        } else {
            if (pxMaxValue != 0) {
                pxUserNbr = (int) (pxMaxValue / xUserUnit) + 1;
                pxMaxValue = pxUserNbr * xUserUnit;
            } else {
                pxUserNbr = 0;
            }
            if (mxMaxValue != 0) {
                mxUserNbr = (int) Math.abs(mxMaxValue / xUserUnit) + 1;
                mxMaxValue = 0 - (mxUserNbr * xUserUnit);
            } else {
                mxUserNbr = 0;
            }
            xMaxValue = pxMaxValue - mxMaxValue;
        }
    }

    public double getYUnit() {
        return yUnit;
    }

    public void setYUnit(double yUserUnit) {
        if (yUserUnit > 0) {
            this.yUserUnit = yUserUnit;
            for (int n = 0; n < nbrCurves; n++) {
                curves[n].setYLabelsUnit((int) yUserUnit);
            }
        } else {
            throw new IllegalArgumentException(("Graphics.Graph.NullUnit"));
        }
        if (curves[0].getYInType() == GraphConstants.IN_STRINGS) {
            pyUserNbr = (curves[0].getYLabels().length + 1);
            pyMaxValue = yUserUnit * pyUserNbr;
            myUserNbr = 0;
            myMaxValue = 0;
            yMaxValue = pyMaxValue;
        } else {
            if (pyMaxValue != 0) {
                pyUserNbr = (int) (pyMaxValue / yUserUnit) + 1;
                pyMaxValue = pyUserNbr * yUserUnit;
            } else {
                pyUserNbr = 0;
            }
            if (myMaxValue != 0) {
                myUserNbr = (int) Math.abs(myMaxValue / yUserUnit) + 1;
                myMaxValue = 0 - (myUserNbr * yUserUnit);
            } else {
                myUserNbr = 0;
            }
            yMaxValue = pyMaxValue - myMaxValue;
        }
    }

    public Color getGridBackground() {
        return gridBg;
    }

    public void setGridBackground(Color color) {
        if (color != null) {
            gridBg = color;
        }
    }

    public Color getGridColor() {
        return gridColor;
    }

    public void setGridColor(Color color) {
        if (color != null) {
            gridColor = color;
        }
    }

    public boolean isGridDisplayed() {
        return displayGrid;
    }

    public void setDisplayGrid(boolean value) {
        displayGrid = value;
    }
}