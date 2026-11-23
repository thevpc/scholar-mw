package net.thevpc.ntexup.extension.mwsimulator;

import net.thevpc.ntexup.api.document.node.NTxNode;
import net.thevpc.ntexup.api.document.style.NTxPropName;
import net.thevpc.ntexup.api.engine.NTxNodeBuilderContext;
import net.thevpc.ntexup.api.eval.NTxFunctionArg;
import net.thevpc.ntexup.api.eval.NTxFunctionCallContext;
import net.thevpc.ntexup.api.eval.NTxObjFromMap;
import net.thevpc.ntexup.api.eval.NTxObjs;
import net.thevpc.ntexup.api.eval.NTxValue;
import net.thevpc.ntexup.api.extension.NTxNodeBuilder;
import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.api.util.NTxUtils;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NPairElement;
import net.thevpc.nuts.io.NPath;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.util.NOptional;

import java.util.*;

/**
 * {@code reference-data(...)}: digitised data from a publication, measured with the same code
 * as a solver result ({@link NTxSolverMetrics}) and published under the same names, so a
 * reference row in a table ({@code ${sheen_frGHz}}) and a claim against it
 * ({@code ${within-pct(oems_fr, sheen_fr, 1)}}) are computed, not typed.
 * <pre>
 * reference-data(
 *     name:"sheen"
 *     frequency:sheen_f                      // Hz
 *     zin-re:sheen_zin_re  zin-im:sheen_zin_im  z0:50   // or: s11-db:sheen_fdtd
 *     source:"Sheen et al. 1990, Fig. 6"
 * )
 * reference-data(                           // the same data from a CSV next to the deck
 *     name:"sheen", output:"allS11"
 *     csv:"sheen1990-fig6.csv"
 *     frequency:"frequency_hz", zin-re:"zin_re_ohm", zin-im:"zin_im_ohm"   // column names
 * )
 * reference-data(name:"sheenmeas", fr:7.46GHz, min-db:-15, source:"Sheen et al. 1990, Fig. 5")
 * </pre>
 * With {@code csv:}, the path is relative to the deck, lines starting with {@code #} and blank
 * lines are skipped, the first other line is the comma-separated header, and
 * {@code frequency}, {@code s11-db}, {@code zin-re}, {@code zin-im} name columns instead of
 * holding arrays.
 * <p>
 * The curve is published like a solver output: {@code <name>.<output>.x}, {@code .db} (and
 * {@code .zinRe}, {@code .zinIm} with Zin input) for plot2d, with the metrics next to it
 * ({@code <name>.<output>.fr}); {@code output} defaults to {@code s11}.
 * Publishes {@code <name>_<metric>} and {@code <name>.<metric>} for every metric of
 * {@link NTxSolverMetrics#NAMES}, {@code <name>_source}, {@code <name>_status} (empty when
 * the data is valid) and the object {@code <name>} with the curve ({@code x}, {@code db})
 * so a plot draws the very curve the metrics were measured on. With complex Zin the curve is
 * |S11| = |(Zin - z0)/(Zin + z0)| in dB. Invalid input (unequal lengths, non-numeric or
 * non-increasing frequencies, fewer than 3 points, conflicting inputs) is a render error and
 * publishes every metric as NaN, so claims against it fail visibly.
 * Renders nothing.
 */
public class NTxReferenceDataBuilder implements NTxNodeBuilder {

    public static final String ID = "reference-data";

    @Override
    public void build(NTxNodeBuilderContext builderContext) {
        builderContext.id(ID)
                .alias("referenceData", "reference")
                .initializeNodeAction((node, ctx) -> node.setProperty(NTxPropName.LAYOUT, NElement.ofString("none")))
                .parseParam()
                .matchesAny().end()
                .renderComponent(this::render);
    }

    static final class Input {
        String name;
        String source = "";
        String output = "s11";
        NElement csv;
        String fCol;
        String dbCol;
        String reCol;
        String imCol;
        double[] f;
        double[] db;
        double[] zre;
        double[] zim;
        double z0 = 50;
        Double fr;
        Double minDb;
        Double bw10;
    }

    static final class Invalid extends RuntimeException {
        Invalid(String m) {
            super(m);
        }
    }

    private void render(NTxRendererContext rendererContext) {
        NTxNode node = rendererContext.node();
        if (node.getUserObject("reference-data-published").isPresent()) {
            return;
        }
        Input in = new Input();
        String error = null;
        Map<String, Double> metrics;
        double[] curveDb = null;
        try {
            read(rendererContext, in);
            curveDb = curve(in);
            metrics = curveDb != null
                    ? NTxSolverMetrics.computeFromSweep(in.f, curveDb)
                    : NTxSolverMetrics.fromScalars(in.fr, in.minDb, in.bw10);
            if (metrics.isEmpty()) {
                throw new Invalid("no finite point in the data");
            }
        } catch (Invalid e) {
            error = e.getMessage();
            metrics = Collections.emptyMap();
            curveDb = null;
        }
        if (in.name == null) {
            rendererContext.log(NMsg.ofC("%s: %s", ID, error == null ? "missing name:\"...\"" : error).asError());
            return;
        }
        if (error != null) {
            rendererContext.log(NMsg.ofC("%s %s: %s", ID, in.name, error).asError());
        }
        NTxSolverMetrics.publishReference(rendererContext, in.name, metrics);
        put(rendererContext, in.name + "_source", NElement.ofString(in.source));
        put(rendererContext, in.name + "_status", NElement.ofString(error == null ? "" : " — invalid reference data: " + error));
        NTxObjFromMap obj = NTxObjs.map();
        NTxObjFromMap out = NTxObjs.map();
        for (NTxObjFromMap m : new NTxObjFromMap[]{obj, out}) {
            if (curveDb != null) {
                m.set("x", NTxObjs.elem(array(in.f)));
                m.set("db", NTxObjs.elem(array(curveDb)));
                if (in.zre != null && in.zim != null) {
                    m.set("zinRe", NTxObjs.elem(array(in.zre)));
                    m.set("zinIm", NTxObjs.elem(array(in.zim)));
                }
            }
            for (Map.Entry<String, Double> e : metrics.entrySet()) {
                m.set(e.getKey(), NTxObjs.elem(NElement.ofDouble(e.getValue())));
            }
            m.set("source", NTxObjs.elem(NElement.ofString(in.source)));
        }
        obj.set(in.output, out);
        for (Map.Entry<String, Double> e : metrics.entrySet()) {
            put(rendererContext, in.name + "." + in.output + "." + e.getKey(), NElement.ofDouble(e.getValue()));
        }
        rendererContext.compiledDocument().setGlobalObject(in.name, obj);
        rendererContext.compiledDocument().dependencyGraph().notifyBindingUpdated(in.name);
        node.setUserObject("reference-data-published", Boolean.TRUE);
    }

    private static void put(NTxRendererContext ctx, String key, NElement value) {
        ctx.compiledDocument().setGlobalObject(key, NTxObjs.elem(value));
        ctx.compiledDocument().dependencyGraph().notifyBindingUpdated(key);
    }

    private static NElement array(double[] v) {
        NElement[] e = new NElement[v.length];
        for (int i = 0; i < v.length; i++) {
            e[i] = NElement.ofDouble(v[i]);
        }
        return NElement.ofArray(e);
    }

    // ---------------------------------------------------------------- input

    private static void read(NTxRendererContext rendererContext, Input in) {
        NElement raw = rendererContext.node().getRaw();
        List<NElement> params = raw == null ? Collections.emptyList() : NTxMwSimulationUtils.getTupleChildren(raw);
        NTxFunctionCallContext args = rendererContext.engine().createFunctionArgs(
                rendererContext.node().name(), params.toArray(new NElement[0]), rendererContext);
        for (NTxFunctionArg arg : args.args()) {
            NElement a = arg.eval();
            if (!a.isNamedPair()) {
                throw new Invalid("unexpected argument " + NTxUtils.snippet(a) + ", every argument is key:value");
            }
            NPairElement p = a.asNamedPair().get();
            String key = NTxUtils.uid(p.key().asStringValue().orElse(""));
            NElement v = args.scopedContext().evalExpression(p.value()).orElse(p.value());
            switch (key) {
                case "name":
                    in.name = v.asStringValue().map(String::trim).filter(s -> !s.isEmpty())
                            .orElseThrow(() -> new Invalid("name must be a non-empty string"));
                    break;
                case "source":
                    in.source = v.asStringValue().orElse(String.valueOf(v));
                    break;
                case "frequency":
                case "x":
                    if (v.isAnyString() && !v.isName()) {
                        in.fCol = v.asStringValue().get().trim();
                    } else {
                        in.f = doubles(key, v);
                    }
                    break;
                case "s11-db":
                case "db":
                    if (v.isAnyString() && !v.isName()) {
                        in.dbCol = v.asStringValue().get().trim();
                    } else {
                        in.db = doubles(key, v);
                    }
                    break;
                case "zin-re":
                    if (v.isAnyString() && !v.isName()) {
                        in.reCol = v.asStringValue().get().trim();
                    } else {
                        in.zre = doubles(key, v);
                    }
                    break;
                case "zin-im":
                    if (v.isAnyString() && !v.isName()) {
                        in.imCol = v.asStringValue().get().trim();
                    } else {
                        in.zim = doubles(key, v);
                    }
                    break;
                case "csv":
                    in.csv = v;
                    break;
                case "output":
                    in.output = v.asStringValue().map(String::trim).filter(x -> !x.isEmpty())
                            .orElseThrow(() -> new Invalid("output must be a non-empty name"));
                    break;
                case "z0":
                    in.z0 = scalar(key, v);
                    if (!(in.z0 > 0)) {
                        throw new Invalid("z0 must be positive, got " + in.z0);
                    }
                    break;
                case "fr":
                    in.fr = scalar(key, v);
                    break;
                case "min-db":
                    in.minDb = scalar(key, v);
                    break;
                case "bw10":
                    in.bw10 = scalar(key, v);
                    break;
                default:
                    throw new Invalid("unknown option '" + key + "', expected name, source, output, csv, frequency, s11-db, zin-re, zin-im, z0, fr, min-db, bw10");
            }
        }
        boolean anyCol = in.fCol != null || in.dbCol != null || in.reCol != null || in.imCol != null;
        if (in.csv == null) {
            if (anyCol) {
                throw new Invalid("column names (frequency/s11-db/zin-re/zin-im as strings) need csv:\"file.csv\"");
            }
            return;
        }
        if (in.f != null || in.db != null || in.zre != null || in.zim != null) {
            throw new Invalid("with csv:, frequency/s11-db/zin-re/zin-im are column names, not arrays");
        }
        if (in.fCol == null) {
            throw new Invalid("csv: needs frequency:\"<column>\"");
        }
        NPath path;
        try {
            path = args.scopedContext().resolvePath(in.csv);
        } catch (RuntimeException e) {
            throw new Invalid("cannot resolve csv path " + NTxUtils.snippet(in.csv) + ": " + e.getMessage());
        }
        if (path == null || !path.isRegularFile()) {
            throw new Invalid("csv file not found: " + (path == null ? NTxUtils.snippet(in.csv) : path.toString()));
        }
        Map<String, double[]> cols = readCsv(path);
        in.f = column(cols, in.fCol, path);
        in.db = in.dbCol == null ? null : column(cols, in.dbCol, path);
        in.zre = in.reCol == null ? null : column(cols, in.reCol, path);
        in.zim = in.imCol == null ? null : column(cols, in.imCol, path);
        if (in.source.isEmpty()) {
            in.source = path.name();
        }
    }

    private static double[] column(Map<String, double[]> cols, String name, NPath path) {
        double[] c = cols.get(name);
        if (c == null) {
            throw new Invalid("column '" + name + "' not in " + path.name() + ", columns are " + cols.keySet());
        }
        return c;
    }

    /**
     * Comma-separated numbers under one header line; {@code #} lines and blank lines skipped.
     */
    static Map<String, double[]> readCsv(NPath path) {
        List<String> lines;
        try {
            lines = Arrays.asList(path.readString().split("\\R"));
        } catch (RuntimeException e) {
            throw new Invalid("cannot read " + path + ": " + e.getMessage());
        }
        String[] header = null;
        List<double[]> rows = new ArrayList<>();
        for (int ln = 0; ln < lines.size(); ln++) {
            String line = lines.get(ln).trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            String[] cells = line.split(",", -1);
            for (int i = 0; i < cells.length; i++) {
                cells[i] = cells[i].trim().replaceAll("^\"(.*)\"$", "$1");
            }
            if (header == null) {
                header = cells;
                continue;
            }
            if (cells.length != header.length) {
                throw new Invalid(path.name() + " line " + (ln + 1) + ": " + cells.length + " cells for " + header.length + " columns");
            }
            double[] r = new double[cells.length];
            for (int i = 0; i < cells.length; i++) {
                try {
                    r[i] = Double.parseDouble(cells[i]);
                } catch (NumberFormatException e) {
                    throw new Invalid(path.name() + " line " + (ln + 1) + ", column '" + header[i] + "': '" + cells[i] + "' is not a number");
                }
            }
            rows.add(r);
        }
        if (header == null) {
            throw new Invalid(path.name() + " has no header line");
        }
        Map<String, double[]> cols = new LinkedHashMap<>();
        for (int i = 0; i < header.length; i++) {
            double[] c = new double[rows.size()];
            for (int k = 0; k < c.length; k++) {
                c[k] = rows.get(k)[i];
            }
            cols.put(header[i], c);
        }
        return cols;
    }

    private static double scalar(String key, NElement v) {
        NOptional<Double> d = NTxValue.of(v).asDouble();
        if (!d.isPresent() || !Double.isFinite(d.get())) {
            throw new Invalid(key + " must be a finite number, got " + NTxUtils.snippet(v));
        }
        return d.get();
    }

    private static double[] doubles(String key, NElement v) {
        if (v == null || !v.isListContainer() || v.isAnyString()) {
            throw new Invalid(key + " must be an array of numbers, got " + NTxUtils.snippet(v));
        }
        List<NElement> c = v.asListContainer().get().children();
        double[] r = new double[c.size()];
        for (int i = 0; i < r.length; i++) {
            NOptional<Double> d = NTxValue.of(c.get(i)).asDouble();
            if (!d.isPresent() || !Double.isFinite(d.get())) {
                throw new Invalid(key + "[" + i + "] is not a finite number: " + NTxUtils.snippet(c.get(i)));
            }
            r[i] = d.get();
        }
        return r;
    }

    /**
     * The |S11| dB curve the metrics are measured on, or null for scalar-only data.
     */
    static double[] curve(Input in) {
        boolean hasZ = in.zre != null || in.zim != null;
        boolean hasDb = in.db != null;
        boolean hasScalar = in.fr != null || in.minDb != null || in.bw10 != null;
        if (hasZ && hasDb) {
            throw new Invalid("give either s11-db or zin-re/zin-im, not both (use two reference-data nodes to compare them)");
        }
        if (!hasZ && !hasDb) {
            if (in.f != null) {
                throw new Invalid("frequency given without s11-db or zin-re/zin-im");
            }
            if (!hasScalar) {
                throw new Invalid("no data: give frequency with s11-db or zin-re/zin-im, or the scalars fr / min-db / bw10");
            }
            if (in.fr == null) {
                throw new Invalid("scalar reference data needs at least fr");
            }
            return null;
        }
        if (hasScalar) {
            throw new Invalid("fr / min-db / bw10 are computed from the curve; give either the curve or the scalars");
        }
        if (in.f == null) {
            throw new Invalid("frequency is required with " + (hasDb ? "s11-db" : "zin-re/zin-im"));
        }
        int n = in.f.length;
        if (n < 3) {
            throw new Invalid("at least 3 points are needed, got " + n);
        }
        for (int i = 1; i < n; i++) {
            if (!(in.f[i] > in.f[i - 1])) {
                throw new Invalid("frequency must be strictly increasing, frequency[" + i + "] = " + in.f[i] + " <= " + in.f[i - 1]);
            }
        }
        if (hasDb) {
            if (in.db.length != n) {
                throw new Invalid("s11-db has " + in.db.length + " values for " + n + " frequencies");
            }
            return in.db.clone();
        }
        if (in.zre == null || in.zim == null) {
            throw new Invalid("complex Zin needs both zin-re and zin-im");
        }
        if (in.zre.length != n || in.zim.length != n) {
            throw new Invalid("zin-re/zin-im have " + in.zre.length + "/" + in.zim.length + " values for " + n + " frequencies");
        }
        double[] db = new double[n];
        for (int i = 0; i < n; i++) {
            double ar = in.zre[i] - in.z0, ai = in.zim[i];
            double br = in.zre[i] + in.z0, bi = in.zim[i];
            double mag = Math.hypot(ar, ai) / Math.hypot(br, bi);
            db[i] = 20 * Math.log10(Math.max(mag, 1e-12));
        }
        return db;
    }
}
