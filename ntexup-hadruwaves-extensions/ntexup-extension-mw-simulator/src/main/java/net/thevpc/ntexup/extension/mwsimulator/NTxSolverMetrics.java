package net.thevpc.ntexup.extension.mwsimulator;

import net.thevpc.ntexup.api.eval.NTxObjFromMap;
import net.thevpc.ntexup.api.eval.NTxObjs;
import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.nuts.elem.NArrayElement;
import net.thevpc.nuts.elem.NElement;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Turns a solved sweep into the scalar quantities a document actually wants to
 * assert: resonance frequency, null depth, bandwidth and Q.
 * <p>
 * Every metric is published three ways so a document can reach it from
 * whichever namespace it already uses:
 * <ul>
 *   <li>{@code <plan>.<output>.<metric>} — next to the curve arrays,</li>
 *   <li>{@code <plan>.<metric>} — the flat global form the slide writes,</li>
 *   <li>{@code <plan>_<metric>} — a plain variable, usable in string
 *       interpolation where a dotted path would be re-resolved through a
 *       future that is still pending.</li>
 * </ul>
 * A metric that cannot be measured on the sweep that was run is published as
 * {@code NaN} rather than omitted: an unbound name interpolates to its own
 * literal text, which a reader cannot tell apart from a formatting mistake.
 */
public class NTxSolverMetrics {

    public static final String FR = "fr";
    public static final String MIN_DB = "minDb";
    public static final String BW10 = "bw10";
    public static final String Q10 = "q10";
    public static final String BW3 = "bw3";
    public static final String Q3 = "q3";
    public static final String GRID_STEP = "gridStep";
    public static final String POINTS = "points";
    /**
     * Display forms of the SI metrics, rounded and scaled to the units an
     * antenna table is written in. They exist because the document layer has no
     * number formatting: a raw 6.62670159131645E9 interpolates verbatim, and
     * arithmetic on such a literal is not evaluated — it is pasted as text.
     */
    public static final String FR_GHZ = "frGHz";
    public static final String BW10_MHZ = "bw10MHz";
    public static final String BW3_MHZ = "bw3MHz";
    public static final String GRID_STEP_MHZ = "gridStepMHz";

    /**
     * Cost of the solve on this machine (see {@link NTxSolverCost}): wall-clock time as
     * text ("1 h 50 min") and peak container memory in GB ("n/a" for in-process solvers).
     */
    public static final String RUN_TIME = "runTime";
    public static final String PEAK_MEM_GB = "peakMemGB";

    public static final String[] NAMES = {
            FR, MIN_DB, BW10, Q10, BW3, Q3, GRID_STEP, POINTS,
            FR_GHZ, BW10_MHZ, BW3_MHZ, GRID_STEP_MHZ, RUN_TIME, PEAK_MEM_GB
    };

    /** Per plan, the costliest output published so far: later outputs reuse the solved plan. */
    private static final Map<String, NTxSolverCost> PUBLISHED_COST = new java.util.concurrent.ConcurrentHashMap<>();

    /**
     * Suffix appended to a plan name to form the flat global of a metric.
     */
    public static String globalName(String planName, String metric) {
        return planName + "_" + metric;
    }

    /**
     * Declares the metrics of a plan as empty globals. A slide compiled before
     * the solver lands must render nothing, not a literal {@code ${...}}.
     */
    public static void seed(NTxRendererContext rendererContext, String planName) {
        if (planName == null || planName.trim().isEmpty()) {
            return;
        }
        for (String name : NAMES) {
            put(rendererContext, globalName(planName.trim(), name), NElement.ofString(""));
        }
    }

    public static void publish(NTxObjFromMap curveMap,
                               NTxRendererContext rendererContext,
                               String planName,
                               String outputName,
                               Number[] x,
                               NArrayElement dbValues) {
        publishCost(curveMap, rendererContext, planName, outputName);
        Map<String, Double> metrics = compute(toDoubles(x), toDoubles(dbValues));
        if (metrics.isEmpty()) {
            return;
        }
        boolean named = planName != null && !planName.trim().isEmpty()
                && rendererContext != null && rendererContext.compiledDocument() != null;
        String pName = named ? planName.trim() : null;
        for (Map.Entry<String, Double> e : metrics.entrySet()) {
            NElement value = POINTS.equals(e.getKey())
                    ? NElement.ofLong(e.getValue().longValue())
                    : NElement.ofDouble(e.getValue());
            if (curveMap != null) {
                curveMap.set(e.getKey(), NTxObjs.elem(value));
            }
            if (named) {
                put(rendererContext, pName + "." + outputName + "." + e.getKey(), value);
                put(rendererContext, pName + "." + e.getKey(), value);
                put(rendererContext, globalName(pName, e.getKey()), value);
            }
        }
    }

    /**
     * Publishes metrics that do not come from a solve (reference data, see
     * {@link NTxReferenceDataBuilder}) under {@code <name>.<metric>} and
     * {@code <name>_<metric>}, the names a solver of that name would use, so a
     * table row or a claim reads a reference exactly like a result. Cost metrics
     * do not apply and are published empty.
     */
    public static void publishReference(NTxRendererContext rendererContext, String name, Map<String, Double> metrics) {
        if (name == null || name.trim().isEmpty() || rendererContext == null || rendererContext.compiledDocument() == null) {
            return;
        }
        String pName = name.trim();
        for (String metric : NAMES) {
            Double v = metrics.get(metric);
            NElement value;
            if (RUN_TIME.equals(metric) || PEAK_MEM_GB.equals(metric)) {
                value = NElement.ofString("");
            } else if (POINTS.equals(metric) && v != null && Double.isFinite(v)) {
                value = NElement.ofLong(v.longValue());
            } else {
                value = NElement.ofDouble(v == null ? Double.NaN : v);
            }
            put(rendererContext, pName + "." + metric, value);
            put(rendererContext, globalName(pName, metric), value);
        }
    }

    /**
     * The metrics of a sweep given as arrays, {@link #compute} under a public name.
     */
    public static Map<String, Double> computeFromSweep(double[] x, double[] db) {
        return compute(x, db);
    }

    /**
     * Metrics of a reference known only by its scalars (a measurement quoted in a
     * paper's text): the display forms and Q follow from the values given, the rest
     * is NaN. {@code bw10} may be null when the source does not give it.
     */
    public static Map<String, Double> fromScalars(Double fr, Double minDb, Double bw10) {
        Map<String, Double> r = new LinkedHashMap<>();
        double f = fr == null ? Double.NaN : fr;
        double b = bw10 == null ? Double.NaN : bw10;
        r.put(MIN_DB, minDb == null ? Double.NaN : minDb);
        r.put(FR, f);
        r.put(FR_GHZ, round(f / 1e9, 3));
        r.put(BW10, b);
        r.put(BW10_MHZ, round(b / 1e6, 3));
        r.put(Q10, round(f > 0 && b > 0 ? f / b : Double.NaN, 2));
        return r;
    }

    private static void publishCost(NTxObjFromMap curveMap, NTxRendererContext rendererContext,
                                    String planName, String outputName) {
        NTxSolverCost cost = NTxSolverCost.finish(planName);
        if (cost == null) {
            return;
        }
        if (curveMap != null) {
            curveMap.set(RUN_TIME, NTxObjs.elem(NElement.ofString(cost.timeText())));
            curveMap.set(PEAK_MEM_GB, NTxObjs.elem(NElement.ofString(cost.peakGBText())));
        }
        if (planName == null || planName.trim().isEmpty()
                || rendererContext == null || rendererContext.compiledDocument() == null) {
            return;
        }
        String pName = planName.trim();
        NTxSolverCost shown = PUBLISHED_COST.merge(pName, cost,
                (a, b) -> b.seconds() > a.seconds() ? b : a);
        NElement time = NElement.ofString(shown.timeText());
        NElement mem = NElement.ofString(shown.peakGBText());
        put(rendererContext, pName + "." + outputName + "." + RUN_TIME, NElement.ofString(cost.timeText()));
        put(rendererContext, pName + "." + outputName + "." + PEAK_MEM_GB, NElement.ofString(cost.peakGBText()));
        put(rendererContext, pName + "." + RUN_TIME, time);
        put(rendererContext, pName + "." + PEAK_MEM_GB, mem);
        put(rendererContext, globalName(pName, RUN_TIME), time);
        put(rendererContext, globalName(pName, PEAK_MEM_GB), mem);
    }

    private static void put(NTxRendererContext rendererContext, String key, NElement value) {
        rendererContext.compiledDocument().setGlobalObject(key, NTxObjs.elem(value));
        rendererContext.compiledDocument().dependencyGraph().notifyBindingUpdated(key);
    }

    static Map<String, Double> compute(double[] x, double[] db) {
        Map<String, Double> r = new LinkedHashMap<>();
        int n = Math.min(x == null ? 0 : x.length, db == null ? 0 : db.length);
        if (n == 0) {
            return r;
        }
        int i = -1;
        for (int k = 0; k < n; k++) {
            if (Double.isFinite(x[k]) && Double.isFinite(db[k]) && (i < 0 || db[k] < db[i])) {
                i = k;
            }
        }
        if (i < 0) {
            return r;
        }
        double minDb = db[i];
        r.put(MIN_DB, round(minDb, 3));
        r.put(POINTS, (double) n);
        double gridStep = n > 1 && Double.isFinite(x[0]) && Double.isFinite(x[n - 1]) ? (x[n - 1] - x[0]) / (n - 1) : Double.NaN;
        r.put(GRID_STEP, gridStep);
        r.put(GRID_STEP_MHZ, round(gridStep / 1e6, 3));
        double fr = refineMinimum(x, db, i, n);
        r.put(FR, fr);
        r.put(FR_GHZ, round(fr / 1e9, 3));
        putBand(r, x, db, i, n, FR, -10.0, BW10, Q10, BW10_MHZ);
        putBand(r, x, db, i, n, FR, minDb + 3.0, BW3, Q3, BW3_MHZ);
        return r;
    }

    private static double round(double v, int digits) {
        if (!Double.isFinite(v)) {
            return v;
        }
        double s = Math.pow(10, digits);
        return Math.round(v * s) / s;
    }

    /**
     * The grid minimum is quantised by the sweep step, which on a 16-point
     * sweep over 750 MHz is 50 MHz — coarser than the discrepancies this
     * benchmark exists to measure. A parabola through the three points around
     * the minimum recovers sub-grid resolution; it is only trusted when the
     * minimum is interior and bracketed by higher values on both sides.
     */
    private static double refineMinimum(double[] x, double[] db, int i, int n) {
        if (i == 0 || i == n - 1) {
            return x[i];
        }
        double den = db[i - 1] - 2 * db[i] + db[i + 1];
        if (!(Math.abs(den) > 1e-12)) {
            return x[i];
        }
        double p = 0.5 * (db[i - 1] - db[i + 1]) / den;
        if (!(p >= -1.0 && p <= 1.0)) {
            return x[i];
        }
        double fr = x[i] + 0.5 * p * (x[i + 1] - x[i - 1]);
        double lo = Math.min(x[i - 1], x[i + 1]);
        double hi = Math.max(x[i - 1], x[i + 1]);
        return (fr >= lo && fr <= hi) ? fr : x[i];
    }

    private static void putBand(Map<String, Double> r, double[] x, double[] db, int i, int n,
                                String frKey, double threshold, String bwKey, String qKey, String bwMHzKey) {
        double bw = Double.NaN;
        int lo = i;
        while (lo > 0 && db[lo - 1] <= threshold) {
            lo--;
        }
        int hi = i;
        while (hi < n - 1 && db[hi + 1] <= threshold) {
            hi++;
        }
        boolean closedLeft = lo > 0;
        boolean closedRight = hi < n - 1;
        if (db[i] <= threshold && closedLeft && closedRight) {
            double left = cross(x[lo - 1], db[lo - 1], x[lo], db[lo], threshold);
            double right = cross(x[hi], db[hi], x[hi + 1], db[hi + 1], threshold);
            bw = right - left;
        }
        r.put(bwKey, bw);
        r.put(bwMHzKey, round(bw / 1e6, 3));
        double fr = r.get(frKey);
        r.put(qKey, round(fr > 0 && bw > 0 ? fr / bw : Double.NaN, 2));
    }

    private static double cross(double x0, double y0, double x1, double y1, double threshold) {
        double d = y1 - y0;
        if (Math.abs(d) < 1e-12) {
            return x1;
        }
        return x0 + (threshold - y0) / d * (x1 - x0);
    }

    private static double[] toDoubles(Number[] e) {
        if (e == null) {
            return new double[0];
        }
        double[] r = new double[e.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = e[i] == null ? Double.NaN : e[i].doubleValue();
        }
        return r;
    }

    private static double[] toDoubles(NArrayElement e) {
        if (e == null) {
            return new double[0];
        }
        double[] r = new double[e.children().size()];
        for (int i = 0; i < r.length; i++) {
            r[i] = e.children().get(i).asDoubleValue().orElse(Double.NaN);
        }
        return r;
    }

    private NTxSolverMetrics() {
    }
}
