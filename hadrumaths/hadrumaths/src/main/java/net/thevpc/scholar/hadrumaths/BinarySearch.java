/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.thevpc.scholar.hadrumaths;

/**
 *
 * @author vpc
 */
public class BinarySearch {

    public static class DDBinarySearch {

        private double yPrecision = 0.000000001;
        private double xPrecision = 0.000000001;
        private double minX;
        private double maxX;
        private boolean ascendent;
        private DDoublefunction fct;

        public double getyPrecision() {
            return yPrecision;
        }

        public double getxPrecision() {
            return xPrecision;
        }

        public double getMinX() {
            return minX;
        }

        public double getMaxX() {
            return maxX;
        }

        public DDBinarySearch setyPrecision(double yPrecision) {
            this.yPrecision = yPrecision;
            return this;
        }

        public DDBinarySearch setxPrecision(double xPrecision) {
            this.xPrecision = xPrecision;
            return this;
        }

        public DDBinarySearch setMinX(double minX) {
            this.minX = minX;
            return this;
        }

        public DDBinarySearch setMaxX(double maxX) {
            this.maxX = maxX;
            return this;
        }

        public double search(double y) {
            double xLow = minX;
            double xHigh = maxX;
            while (xHigh - xLow > xPrecision) {
                double xMid = xLow + ((xHigh - xLow) / 2);
                double yMid = fct.apply(xMid);
                double diff = y - yMid;
                if (Maths.abs(diff) <= yPrecision) {
                    return xMid;
                }
                if (yMid < y) {
                    xLow = xMid;
                } else if (yMid > y) {
                    xHigh = xMid;
                } else {
                    return xMid;
                }
            }
            return xLow + ((xHigh - xLow) / 2);
        }
    }

    public static DDBinarySearch of(DDoublefunction fct, double minX, double maxX) {
        DDBinarySearch dd = new DDBinarySearch();
        dd.fct = fct;
        dd.minX = minX;
        dd.maxX = maxX;
        dd.ascendent = fct.apply(minX) <= fct.apply(maxX);
        return dd;
    }
}
