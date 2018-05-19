package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.util.Converter;

import java.util.*;

public class PlotLines {
    private Map<String, Map<Double, Complex>> values = new LinkedHashMap<>();
    private Set<Double> xvalues = new TreeSet<>();

    public void addLine(String name) {
        getLineMap(name);
    }

    private Map<Double, Complex> getLineMap(String name) {
        Map<Double, Complex> doubleComplexMap = values.get(name);
        if (doubleComplexMap == null) {
            doubleComplexMap = new HashMap<>();
            values.put(name, doubleComplexMap);
        }
        return doubleComplexMap;
    }

    public void addValues(String name, double[] x, double[] y) {
        if (x == null) {
            x = Maths.dsteps(0, y.length - 1);
        }
        if (x.length != y.length) {
            throw new IllegalArgumentException("Length des not match");
        }
        for (int i = 0; i < y.length; i++) {
            addValue(name, x[i], Complex.valueOf(y[i]));
        }
    }

    public void addValues(String name, double[] x, Complex[] y) {
        if (x == null) {
            x = Maths.dsteps(0, y.length - 1);
        }
        if (x.length != y.length) {
            throw new IllegalArgumentException("Length des not match");
        }
        for (int i = 0; i < y.length; i++) {
            addValue(name, x[i], y[i]);
        }
    }

    public void addValue(PlotPoint p) {
        addValue(p.getTitle(), p.getX(), p.getY());
    }

    public void addValue(String name, double x, double y) {
        addValue(name, x, Complex.valueOf(y));
    }

    public void addValue(String name, double x, Complex y) {
        if (y == null) {
            throw new NullPointerException();
        }
        xvalues.add(x);
        getLineMap(name).put(x, y);
    }

    public TList<String> titles() {
        TList<String> names = Maths.slist(values.size());
        names.appendAll(values.keySet());
        return names;
    }

    public TList<Double> xsamples() {
        TList<Double> xvals = Maths.dlist(xvalues.size());
        xvals.appendAll(xvalues);
        return xvals;
    }

    public Complex avg(String name) {
        MutableComplex s = MutableComplex.Zero();
        Map<Double, Complex> doubleComplexMap = getDoubleComplexMap(name);
        for (Complex c : doubleComplexMap.values()) {
            s.add(c);
        }
        s.div(doubleComplexMap.size());
        return s.toImmutable();
    }

    public Complex max(String name) {
        Complex s = null;
        Map<Double, Complex> doubleComplexMap = getDoubleComplexMap(name);
        for (Complex c : doubleComplexMap.values()) {
            if (s == null) {
                s = c;
            } else if (s.compareTo(c) > 0) {
                s = c;
            }
        }
        return s;
    }

    public Complex min(String name) {
        Complex s = null;
        Map<Double, Complex> doubleComplexMap = getDoubleComplexMap(name);
        for (Complex c : doubleComplexMap.values()) {
            if (s == null) {
                s = c;
            } else if (s.compareTo(c) > 0) {
                s = c;
            }
        }
        return s;
    }

    public PlotLines accumulateLeft() {
        PlotLines newLines = new PlotLines();
        for (String entry : values.keySet()) {
            PlotPoint[] newLine = getLine(entry).toArray();
            for (int i = 1; i < newLine.length; i++) {
                newLine[i] = new PlotPoint(
                        newLine[i].getTitle(),
                        newLine[i].getX(),
                        newLine[i].getY().add(newLine[i - 1].getY())
                );
            }
            for (PlotPoint plotPoint : newLine) {
                newLines.addValue(plotPoint);
            }
        }
        return newLines;
    }

    public PlotLines accumulateRight() {
        PlotLines newLines = new PlotLines();
        for (String entry : values.keySet()) {
            PlotPoint[] newLine = getLine(entry).toArray();
            for (int i = newLine.length - 2; i >= 0; i--) {
                newLine[i] = new PlotPoint(
                        newLine[i].getTitle(),
                        newLine[i].getX(),
                        newLine[i].getY().add(newLine[i + 1].getY())
                );
            }
            for (PlotPoint plotPoint : newLine) {
                newLines.addValue(plotPoint);
            }
        }
        return newLines;
    }

    public PlotLines countPercentBy(Converter<PlotPoint, Double> group) {
        PlotLines newLines = new PlotLines();
        for (String entry : values.keySet()) {
            Map<Double, Double> counts = new HashMap<>();
            for (PlotPoint p : getLine(entry)) {
                Double g = group.convert(p);
                Double oldv = counts.get(g);
                if (oldv == null) {
                    counts.put(g, 1.0);
                } else {
                    counts.put(g, oldv + 1);
                }
            }
            for (Map.Entry<Double, Double> e : counts.entrySet()) {
                newLines.addValue(entry, e.getKey(), Complex.valueOf(e.getValue() / values.get(entry).size()));
            }
        }
        return newLines;
    }

    public PlotLines countBy(Converter<PlotPoint, Double> group) {
        PlotLines newLines = new PlotLines();
        for (String entry : values.keySet()) {
            Map<Double, Double> counts = new HashMap<>();
            for (PlotPoint p : getLine(entry)) {
                Double g = group.convert(p);
                Double oldv = counts.get(g);
                if (oldv == null) {
                    counts.put(g, 1.0);
                } else {
                    counts.put(g, oldv + 1);
                }
            }
            for (Map.Entry<Double, Double> e : counts.entrySet()) {
                newLines.addValue(entry, e.getKey(), Complex.valueOf(e.getValue()));
            }
        }
        return newLines;
    }

    public PlotLines minBy(Converter<PlotPoint, Double> group) {
        PlotLines newLines = new PlotLines();
        for (String entry : values.keySet()) {
            Map<Double, Complex> counts = new HashMap<>();
            for (PlotPoint p : getLine(entry)) {
                Double g = group.convert(p);
                Complex oldv = counts.get(g);
                if (oldv == null) {
                    counts.put(g, p.getY());
                } else {
                    counts.put(g, Maths.min(oldv, p.getY()));
                }
            }
        }
        return newLines;
    }

    public PlotLines maxBy(Converter<PlotPoint, Double> group) {
        PlotLines newLines = new PlotLines();
        for (String entry : values.keySet()) {
            Map<Double, Complex> counts = new HashMap<>();
            for (PlotPoint p : getLine(entry)) {
                Double g = group.convert(p);
                Complex oldv = counts.get(g);
                if (oldv == null) {
                    counts.put(g, p.getY());
                } else {
                    counts.put(g, Maths.max(oldv, p.getY()));
                }
            }
        }
        return newLines;
    }

    public PlotLines avgBy(Converter<PlotPoint, Double> group) {
        PlotLines newLines = new PlotLines();
        for (String entry : values.keySet()) {
            Map<Double, Complex> vals = new HashMap<>();
            Map<Double, Double> counts = new HashMap<>();
            for (PlotPoint p : getLine(entry)) {
                Double g = group.convert(p);
                Complex oldv = vals.get(g);
                Double oldc = counts.get(g);
                if (oldv == null) {
                    counts.put(g, 1.0);
                    vals.put(g, p.getY());
                } else {
                    counts.put(g, oldc + 1);
                    vals.put(g, oldv.add(p.getY()));
                }
            }
            for (Map.Entry<Double, Double> e : counts.entrySet()) {
                Double count = e.getValue();
                Complex value = vals.get(e.getKey());
                newLines.addValue(entry, e.getKey(), value.div(count));
            }
        }
        return newLines;
    }

    public TList<PlotPoint> getLine(String name) {
        TList<PlotPoint> all = new ArrayTList<PlotPoint>(PlotPoint.$TYPE, true, values.size());
        Map<Double, Complex> doubleComplexMap = getDoubleComplexMap(name);
        TreeSet<Double> xx = new TreeSet<>(doubleComplexMap.keySet());
        for (Double x : xx) {
            all.append(new PlotPoint(name, x, doubleComplexMap.get(x)));
        }
        return all;
    }

    private Map<Double, Complex> getDoubleComplexMap(String name) {
        Map<Double, Complex> doubleComplexMap = values.get(name);
        if (doubleComplexMap == null) {
            throw new NoSuchElementException(name);
        }
        return doubleComplexMap;
    }

    public PlotLines stretchDomain() {
        double max = 0;
        for (String t : this.titles()) {
            max = Math.max(max, this.getLine(t).length());
        }

        PlotLines newLines = new PlotLines();
        for (String t : this.titles()) {
            TList<PlotPoint> line = this.getLine(t);
            double coeff = max / line.length();
            for (PlotPoint p : line) {
                newLines.addValue(t, p.getX() * coeff, p.getY());
            }
        }
        return newLines;
    }

    public PlotLines transformPoints(Converter<PlotPoint, PlotPoint[]> valConverter) {
        PlotLines newLines = new PlotLines();
        for (String t : titles()) {
            for (PlotPoint plotPoint : getLine(t)) {
                PlotPoint[] convert = valConverter.convert(plotPoint);
                for (PlotPoint point : convert) {
                    newLines.addValue(point);
                }
            }
        }
        return newLines;
    }

    public PlotLines interpolate(InterpolationStrategy strategy) {
        Double[] xvalues = this.xvalues.toArray(new Double[this.xvalues.size()]);
        String[] xtitles = this.titles().toArray();
        TList<TList<Complex>> values = getValues(strategy);
        PlotLines lines = new PlotLines();
        for (int i = 0; i < values.length(); i++) {
            TList<Complex> complexes = values.get(i);
            for (int j = 0; j < complexes.length(); j++) {
                lines.addValue(xtitles[i], xvalues[j], complexes.get(j));
            }
        }
        return lines;
    }

    public TList<TList<Complex>> getValues() {
        return getValues(InterpolationStrategy.DEFAULT);
    }

    public TList<TList<Complex>> getValues(InterpolationStrategy strategy) {
        if (strategy == null) {
            strategy = InterpolationStrategy.DEFAULT;
        }
        TList<TList<Complex>> all = new ArrayTList<TList<Complex>>(Maths.$CLIST, true, values.size());
        Map<String, Set<Integer>> undefinedValuesMap = new LinkedHashMap<>();
        for (Map.Entry<String, Map<Double, Complex>> e : values.entrySet()) {
            TList<Complex> t = new ArrayTList<Complex>(Maths.$COMPLEX, false, xvalues.size());
//            Complex lastYvalue=Complex.ZERO;
            int index = 0;
            Set<Integer> indefinedValues = new HashSet<>();
            for (Double xvalue : xvalues) {
                Map<Double, Complex> value = e.getValue();
                Complex yvalue = value.get(xvalue);
                if (yvalue == null) {
                    yvalue = Complex.ZERO;
                    indefinedValues.add(index);
                }
                t.append(yvalue);
//                lastYvalue=yvalue;
                index++;
            }
            if (!indefinedValues.isEmpty()) {
                switch (strategy) {
                    case SMOOTH: {
                        for (Integer zeroIndex : indefinedValues) {
                            int predecessor = zeroIndex - 1;
                            int successor = zeroIndex + 1;
                            while (predecessor >= 0 && indefinedValues.contains(predecessor)) {
                                predecessor--;
                            }
                            while (successor < t.length() && indefinedValues.contains(successor)) {
                                successor++;
                            }
                            if (predecessor >= 0 && successor < t.length()) {
                                t.set(zeroIndex, t.get(predecessor).add(t.get(successor)).div(2));
                            } else if (predecessor >= 0) {
                                t.set(zeroIndex, t.get(predecessor));
                            } else if (successor >= 0) {
                                t.set(zeroIndex, t.get(successor));
                            }
                        }
                        break;
                    }
                    case PREDECESSOR: {
                        for (Integer zeroIndex : indefinedValues) {
                            int predecessor = zeroIndex - 1;
                            while (predecessor >= 0 && indefinedValues.contains(predecessor)) {
                                predecessor--;
                            }
                            if (predecessor >= 0) {
                                t.set(zeroIndex, t.get(predecessor));
                            }
                        }
                        break;
                    }
                    case ZERO: {
                        //do nothing let it zero
                        break;
                    }
                    case NAN: {
                        //do nothing let it NAN
                        for (Integer zeroIndex : indefinedValues) {
                            if (zeroIndex > 0 && zeroIndex + 1 < t.size() && !indefinedValues.contains(zeroIndex - 1) && !indefinedValues.contains(zeroIndex + 1)) {
                                t.set(zeroIndex, Complex.NaN);
//                        t.set(zeroIndex, Complex.valueOf(3000));
                            }
                        }
                        break;
                    }
                    default: {
                        for (Integer zeroIndex : indefinedValues) {
                            if (zeroIndex > 0 && zeroIndex + 1 < t.size() && !indefinedValues.contains(zeroIndex - 1) && !indefinedValues.contains(zeroIndex + 1)) {
                                t.set(zeroIndex, t.get(zeroIndex - 1).add(t.get(zeroIndex + 1)).div(2));
//                        t.set(zeroIndex, Complex.valueOf(3000));
                            }
                        }
                        undefinedValuesMap.put(e.getKey(), indefinedValues);
                        break;
                    }
                }
            }
            all.append(t);
        }
        return all;
    }

    public static class PlotPoint {
        public static final TypeReference<PlotPoint> $TYPE = new TypeReference<PlotPoint>() {
        };
        private String title;
        private double x;
        private Complex y;

        public PlotPoint(String title, double x, Complex y) {
            this.title = title;
            this.x = x;
            this.y = y;
        }

        public String getTitle() {
            return title;
        }

        public double getX() {
            return x;
        }

        public Complex getY() {
            return y;
        }
    }
}
