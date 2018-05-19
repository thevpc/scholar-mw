package net.vpc.scholar.hadrumaths.plot.curve;

import java.awt.*;

public class Curve {

    private String title;
    private CurveData xValues;
    private CurveData yValues;
    private Color lineColor = Color.blue;
    private Color pointsColor = Color.red;

    public Curve(String title, double[] xValues, String[] yValues) {
        this.title = title;
        this.xValues = new CurveData(xValues);
        this.yValues = new CurveData(yValues);
    }

    public Curve(String title, String[] xValues, double[] yValues) {
        this.title = title;
        this.xValues = new CurveData(xValues);
        this.yValues = new CurveData(yValues);
    }

    public Curve(String title, String[] xValues, String[] yValues) {
        this.title = title;
        this.xValues = new CurveData(xValues);
        this.yValues = new CurveData(yValues);
    }

    public Curve(String title, double[] xValues, double[] yValues) {
        this.title = title;
        this.xValues = new CurveData(xValues);
        this.yValues = new CurveData(yValues);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String val) {
        if (val != null) title = val;
    }

    public double[] getXValues() {
        return xValues.getValues();
    }

    public void setXValues(double[] val) {
        if (val != null) xValues.setValues(val);
    }

    public String[] getXLabels() {
        return xValues.getLabels();
    }

    public void setXLabelss(String[] val) {
        if (val != null) xValues.setLabels(val);
    }

    public double[] getYValues() {
        return yValues.getValues();
    }

    public void setYValues(double[] val) {
        if (val != null) yValues.setValues(val);
    }

    public String[] getYLabels() {
        return yValues.getLabels();
    }

    public void setYLabelss(String[] val) {
        if (val != null) yValues.setLabels(val);
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color color) {
        if (color != null) lineColor = color;
    }

    public Color getPointsColor() {
        return pointsColor;
    }

    public void setPointsColor(Color color) {
        if (color != null) pointsColor = color;
    }

    public int getPointsNumber() {
        return xValues.length;
    }

    public double getXMax() {
        return xValues.getMax();
    }

    public double getXMin() {
        return xValues.getMin();
    }

    public double getYMax() {
        return yValues.getMax();
    }

    public double getYMin() {
        return yValues.getMin();
    }

    public int getXInType() {
        return xValues.getInType();
    }

    public int getYInType() {
        return yValues.getInType();
    }

    public int getXLabelsUnit() {
        return xValues.getLabUnit();
    }

    public void setXLabelsUnit(int val) {
        xValues.setLabUnit(val);
    }

    public int getYLabelsUnit() {
        return yValues.getLabUnit();
    }

    public void setYLabelsUnit(int val) {
        yValues.setLabUnit(val);
    }
}

class CurveData {
    public int length;
    private int labUnit = 50;
    private double[] values;
    private String[] labels;
    private int inType;

    public CurveData(String[] labs, double[] vals) {
        values = vals;
        labels = labs;
        length = (values.length == labels.length) ? values.length : -1;
        inType = GraphConstants.IN_BOTH;
    }

    public CurveData(double[] vals) {
        values = vals;
        length = values.length;
        labels = new String[length];
        for (int j = 0; j < length; j++) labels[j] = String.valueOf(values[j]);
        inType = GraphConstants.IN_DOUBLES;
    }

    public CurveData(String[] labs) {
        labels = labs;
        length = labels.length;
        values = new double[length];
        for (int j = 0; j < length; j++) values[j] = (j + 1) * labUnit;
        inType = GraphConstants.IN_STRINGS;
    }

    public double[] getValues() {
        return values;
    }

    public String[] getLabels() {
        return labels;
    }

    public void setValues(double[] vals) {
        values = vals;
    }

    public void setLabels(String[] labs) {
        labels = labs;
    }

    public int getLabUnit() {
        return labUnit;
    }

    public int getInType() {
        return inType;
    }

    public void setLabUnit(int val) {
        labUnit = val;
        if (inType == GraphConstants.IN_STRINGS) {
            for (int j = 0; j < length; j++) values[j] = (j + 1) * labUnit;
        }
    }

    public double getMax() {
        if (values != null && length > 0) {
            double max = values[0];
            for (int i = 1; i < length; i++) {
                if (values[i] > max) {
                    max = values[i];
                }
            }
            return max;
        } else {
            return 0;
        }
    }

    public double getMin() {
        if (values != null && length > 0) {
            double min = values[0];
            for (int i = 1; i < length; i++) {
                if (values[i] < min) {
                    min = values[i];
                }
            }
            return min;
        } else {
            return 0;
        }
    }
}

