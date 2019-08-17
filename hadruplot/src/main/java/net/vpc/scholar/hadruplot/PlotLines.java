package net.vpc.scholar.hadruplot;

import net.vpc.common.util.ArrayUtils;
import net.vpc.common.util.Converter;
import net.vpc.common.util.DoubleArrayList;
import net.vpc.common.util.TypeReference;
import net.vpc.scholar.hadruplot.console.PlotConfigManager;
import net.vpc.scholar.hadruplot.util.PlotUtils;

import java.util.*;

public class PlotLines {
    private Map<String, Map<Double, Object>> values = new LinkedHashMap<>();
    private Set<Double> xvalues = new TreeSet<>();

    public void addLine(String name) {
        getLineMap(name);
    }

    private Map<Double, Object> getLineMap(String name) {
        Map<Double, Object> doubleComplexMap = values.get(name);
        if (doubleComplexMap == null) {
            doubleComplexMap = new HashMap<>();
            values.put(name, doubleComplexMap);
        }
        return doubleComplexMap;
    }

    public void addValues(String name, Object anyXvalues, Object anyYvalues) {
        Object[] xx = PlotUtils.toObjectArrayOrNull(anyXvalues);
        Object[] yy = PlotUtils.toObjectArrayOrNull(anyYvalues);

        if (yy == null) {
            yy = new Object[]{anyYvalues};
        }
        if (xx == null) {
            xx = ArrayUtils.box(ArrayUtils.dsteps(0, yy.length - 1));
        }
        if (xx.length != yy.length) {
            throw new IllegalArgumentException("Length des not match");
        }
        for (int i = 0; i < yy.length; i++) {
            addValue(name, PlotConfigManager.Numbers.toDouble(xx[i]), yy[i]);
        }
    }

    public void addValue(PlotPoint p) {
        addValue(p.getTitle(), p.getX(), p.getY());
    }


    public void addValue(String name, double x, Object y) {
        if (y == null) {
            throw new NullPointerException();
        }
        xvalues.add(x);
        getLineMap(name).put(x, y);
    }

    public String[] titles() {
        return values.keySet().toArray(new String[0]);
    }

    public double[] xsamples() {
        DoubleArrayList d = new DoubleArrayList(xvalues.size());
        for (Double xvalue : xvalues) {
            d.add(xvalue == null ? Double.NaN : xvalue);
        }
        return d.toArray();
    }

    public Object avg(String name) {
        Object s = 0.0;
        Map<Double, Object> doubleComplexMap = getDoubleComplexMap(name);
        for (Object c : doubleComplexMap.values()) {
            s = PlotConfigManager.Numbers.plus(s, c);
        }
        s = PlotConfigManager.Numbers.mul(s, doubleComplexMap.size() == 0 ? 0 : (1.0 / doubleComplexMap.size()));
        return s;
    }

    public Object max(String name) {
        Object s = null;
        Map<Double, Object> doubleComplexMap = getDoubleComplexMap(name);
        for (Object c : doubleComplexMap.values()) {
            if (s == null) {
                s = c;
            } else if (PlotConfigManager.Numbers.compare(s, c) > 0) {
                s = c;
            }
        }
        return s;
    }

    public Object min(String name) {
        Object s = null;
        Map<Double, Object> doubleComplexMap = getDoubleComplexMap(name);
        for (Object c : doubleComplexMap.values()) {
            if (s == null) {
                s = c;
            } else if (PlotConfigManager.Numbers.compare(s, c) > 0) {
                s = c;
            }
        }
        return s;
    }

    public PlotLines accumulateLeft() {
        PlotLines newLines = new PlotLines();
        for (String entry : values.keySet()) {
            PlotPoint[] newLine = getLine(entry);
            for (int i = 1; i < newLine.length; i++) {
                newLine[i] = new PlotPoint(
                        newLine[i].getTitle(),
                        newLine[i].getX(),
                        PlotConfigManager.Numbers.plus(newLine[i].getY(), newLine[i - 1].getY())
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
            PlotPoint[] newLine = getLine(entry);
            for (int i = newLine.length - 2; i >= 0; i--) {
                newLine[i] = new PlotPoint(
                        newLine[i].getTitle(),
                        newLine[i].getX(),
                        PlotConfigManager.Numbers.plus(
                                newLine[i].getY(), newLine[i + 1].getY())
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
                newLines.addValue(entry, e.getKey(), (e.getValue() / values.get(entry).size()));
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
                newLines.addValue(entry, e.getKey(), e.getValue());
            }
        }
        return newLines;
    }

    public PlotLines minBy(Converter<PlotPoint, Double> group) {
        PlotLines newLines = new PlotLines();
        for (String entry : values.keySet()) {
            Map<Double, Object> counts = new HashMap<>();
            for (PlotPoint p : getLine(entry)) {
                Double g = group.convert(p);
                Object oldv = counts.get(g);
                if (oldv == null) {
                    counts.put(g, p.getY());
                } else {
                    counts.put(g, PlotUtils.numbersMin(oldv, p.getY()));
                }
            }
        }
        return newLines;
    }

    public PlotLines maxBy(Converter<PlotPoint, Double> group) {
        PlotLines newLines = new PlotLines();
        for (String entry : values.keySet()) {
            Map<Double, Object> counts = new HashMap<>();
            for (PlotPoint p : getLine(entry)) {
                Double g = group.convert(p);
                Object oldv = counts.get(g);
                if (oldv == null) {
                    counts.put(g, p.getY());
                } else {
                    counts.put(g, PlotUtils.numbersMax(oldv, p.getY()));
                }
            }
        }
        return newLines;
    }

    public PlotLines avgBy(Converter<PlotPoint, Double> group) {
        PlotLines newLines = new PlotLines();
        for (String entry : values.keySet()) {
            Map<Double, Object> vals = new HashMap<>();
            Map<Double, Double> counts = new HashMap<>();
            for (PlotPoint p : getLine(entry)) {
                Double g = group.convert(p);
                Object oldv = vals.get(g);
                Double oldc = counts.get(g);
                if (oldv == null) {
                    counts.put(g, 1.0);
                    vals.put(g, p.getY());
                } else {
                    counts.put(g, oldc + 1);
                    vals.put(g, PlotConfigManager.Numbers.plus(oldv, p.getY()));
                }
            }
            for (Map.Entry<Double, Double> e : counts.entrySet()) {
                Double count = e.getValue();
                Object value = vals.get(e.getKey());
                newLines.addValue(entry, e.getKey(),
                        PlotConfigManager.Numbers.mul(
                                value, (count == null || count == 0) ? 0 : 1.0 / count
                        )
                );
            }
        }
        return newLines;
    }

    public PlotPoint[] getLine(String name) {
        List<PlotPoint> all = new ArrayList<>(values.size());
        Map<Double, Object> doubleComplexMap = getDoubleComplexMap(name);
        TreeSet<Double> xx = new TreeSet<>(doubleComplexMap.keySet());
        for (Double x : xx) {
            all.add(new PlotPoint(name, x, doubleComplexMap.get(x)));
        }
        return all.toArray(new PlotPoint[0]);
    }

    private Map<Double, Object> getDoubleComplexMap(String name) {
        Map<Double, Object> doubleComplexMap = values.get(name);
        if (doubleComplexMap == null) {
            throw new NoSuchElementException(name);
        }
        return doubleComplexMap;
    }

    public PlotLines stretchDomain() {
        double max = 0;
        for (String t : this.titles()) {
            max = Math.max(max, this.getLine(t).length);
        }

        PlotLines newLines = new PlotLines();
        for (String t : this.titles()) {
            PlotPoint[] line = this.getLine(t);
            double coeff = max / line.length;
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
        Double[] xvalues = this.xvalues.toArray(new Double[0]);
        String[] xtitles = this.titles();
        List<List<Object>> values = getValues(strategy);
        PlotLines lines = new PlotLines();
        for (int i = 0; i < values.size(); i++) {
            List<Object> complexes = values.get(i);
            for (int j = 0; j < complexes.size(); j++) {
                lines.addValue(xtitles[i], xvalues[j], complexes.get(j));
            }
        }
        return lines;
    }

    public List<List<Object>> getValues() {
        return getValues(InterpolationStrategy.DEFAULT);
    }

    public List<List<Object>> getValues(InterpolationStrategy strategy) {
        if (strategy == null) {
            strategy = InterpolationStrategy.DEFAULT;
        }
        List<List<Object>> all = new ArrayList<List<Object>>(values.size());
        Map<String, Set<Integer>> undefinedValuesMap = new LinkedHashMap<>();
        for (Map.Entry<String, Map<Double, Object>> e : values.entrySet()) {
            List<Object> t = new ArrayList<Object>(xvalues.size());
//            Complex lastYvalue=Complex.ZERO;
            int index = 0;
            Set<Integer> indefinedValues = new HashSet<>();
            for (Double xvalue : xvalues) {
                Map<Double, Object> value = e.getValue();
                Object yvalue = value.get(xvalue);
                if (yvalue == null) {
                    yvalue = 0.0;
                    indefinedValues.add(index);
                }
                t.add(yvalue);
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
                            while (successor < t.size() && indefinedValues.contains(successor)) {
                                successor++;
                            }
                            if (predecessor >= 0 && successor < t.size()) {
                                t.set(zeroIndex,
                                        PlotConfigManager.Numbers.mul(
                                                PlotConfigManager.Numbers.plus(
                                                        t.get(predecessor),
                                                        t.get(successor)
                                                ), 0.5)
                                );
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
                                t.set(zeroIndex, Double.NaN);
//                        t.set(zeroIndex, Complex.valueOf(3000));
                            }
                        }
                        break;
                    }
                    default: {
                        for (Integer zeroIndex : indefinedValues) {
                            if (zeroIndex > 0 && zeroIndex + 1 < t.size() && !indefinedValues.contains(zeroIndex - 1) && !indefinedValues.contains(zeroIndex + 1)) {
                                t.set(zeroIndex,
                                        PlotConfigManager.Numbers.mul(
                                                PlotConfigManager.Numbers.plus(
                                                        t.get(zeroIndex - 1),
                                                        t.get(zeroIndex + 1)
                                                ), 0.5)
                                );
//                        t.set(zeroIndex, Complex.valueOf(3000));
                            }
                        }
                        undefinedValuesMap.put(e.getKey(), indefinedValues);
                        break;
                    }
                }
            }
            all.add(t);
        }
        return all;
    }

    public static class PlotPoint {
        public static final TypeReference<PlotPoint> $TYPE = new TypeReference<PlotPoint>() {
        };
        private String title;
        private double x;
        private Object y;

        public PlotPoint(String title, double x, Object y) {
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

        public Object getY() {
            return y;
        }
    }
}
