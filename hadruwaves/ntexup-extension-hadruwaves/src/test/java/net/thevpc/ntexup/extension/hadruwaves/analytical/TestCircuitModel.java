package net.thevpc.ntexup.extension.hadruwaves.analytical;

import net.thevpc.ntexup.extension.mwsimulator.MicrostripCircuitModel;

public class TestCircuitModel {

    public static void main(String[] args) {
        MicrostripCircuitModel model = new MicrostripCircuitModel();
        model.substrateH = 1.6e-3;
        model.epsilonR = 4.4;
        model.lossTangent = 0.02;
        model.z0Ref = 50.0;
        model.portLocation = new MicrostripCircuitModel.Point2D(0, -28e-3);
        model.portWidth = 3.1e-3;

        double[] patchX = {-33e-3, -11e-3, 11e-3, 33e-3};
        for (int i = 0; i < 4; i++) {
            MicrostripCircuitModel.PatchElement p = new MicrostripCircuitModel.PatchElement();
            p.id = i + 1;
            p.width = 13.5e-3;
            p.length = 9.4e-3;
            p.insetDepth = 3.4e-3;
            p.feedX = patchX[i];
            p.feedY = 0;
            p.nodeName = "_p" + p.id;

            double u = p.width / model.substrateH;
            double epsEff = (model.epsilonR + 1.0) / 2.0 + ((model.epsilonR - 1.0) / 2.0) / Math.sqrt(1.0 + 12.0 / u);
            double dL = 0.412 * model.substrateH * ((epsEff + 0.3) / (epsEff - 0.258)) * ((u + 0.264) / (u + 0.8));
            double notchFraction = p.length > 0 ? (p.insetDepth / p.length) : 0.0;
            double notchFactor = notchFraction * (1.0 - notchFraction);
            double leff = p.length + 2.0 * dL + p.insetDepth * notchFactor;
            p.fr = MicrostripCircuitModel.C_LIGHT / (2.0 * leff * Math.sqrt(epsEff));

            double k0 = 2.0 * Math.PI * p.fr / MicrostripCircuitModel.C_LIGHT;
            double lam0 = MicrostripCircuitModel.C_LIGHT / p.fr;
            double grad = (p.width / (120.0 * lam0)) * (1.0 - Math.pow(k0 * model.substrateH, 2.0) / 24.0);
            double redge = 1.0 / (2.0 * Math.max(1e-6, grad));
            double cosVal = Math.cos(Math.PI * p.insetDepth / p.length);
            p.rin = redge * Math.pow(cosVal, 2.0);

            double qDiel = 1.0 / Math.max(1e-6, model.lossTangent);
            double qCond = model.substrateH * Math.sqrt(Math.PI * p.fr * 4.0 * Math.PI * 1e-7 * 5.8e7);
            double cPatch = 8.854187817e-12 * model.epsilonR * p.width * p.length / (2.0 * model.substrateH);
            double qRad = 2.0 * Math.PI * p.fr * cPatch * redge;
            p.q = 1.0 / ((1.0 / qRad) + (1.0 / qDiel) + (1.0 / qCond));

            double w0 = 2.0 * Math.PI * p.fr;
            p.cEq = p.q / (w0 * p.rin);
            p.lEq = 1.0 / (w0 * w0 * p.cEq);

            model.patches.add(p);
            model.nodeToPatch.put(p.nodeName, p);
        }

        double wf = 3.1e-3;
        double w70 = 1.55e-3;
        int lid = 1;

        model.lines.add(createLine(model, lid++, wf, 6e-3, false, new MicrostripCircuitModel.Point2D(0, -28e-3), new MicrostripCircuitModel.Point2D(0, -22e-3), "_net0", "_net1"));
        model.lines.add(createLine(model, lid++, w70, 22e-3, true, new MicrostripCircuitModel.Point2D(0, -22e-3), new MicrostripCircuitModel.Point2D(-22e-3, -22e-3), "_net1", "_s1L"));
        model.lines.add(createLine(model, lid++, w70, 22e-3, true, new MicrostripCircuitModel.Point2D(0, -22e-3), new MicrostripCircuitModel.Point2D(22e-3, -22e-3), "_net1", "_s1R"));

        model.lines.add(createLine(model, lid++, wf, 11e-3, false, new MicrostripCircuitModel.Point2D(-22e-3, -22e-3), new MicrostripCircuitModel.Point2D(-22e-3, -11e-3), "_s1L", "_s2L"));
        model.lines.add(createLine(model, lid++, wf, 11e-3, false, new MicrostripCircuitModel.Point2D(22e-3, -22e-3), new MicrostripCircuitModel.Point2D(22e-3, -11e-3), "_s1R", "_s2R"));

        model.lines.add(createLine(model, lid++, w70, 11e-3, true, new MicrostripCircuitModel.Point2D(-22e-3, -11e-3), new MicrostripCircuitModel.Point2D(-33e-3, -11e-3), "_s2L", "_p1feed"));
        model.lines.add(createLine(model, lid++, w70, 11e-3, true, new MicrostripCircuitModel.Point2D(-22e-3, -11e-3), new MicrostripCircuitModel.Point2D(-11e-3, -11e-3), "_s2L", "_p2feed"));
        model.lines.add(createLine(model, lid++, w70, 11e-3, true, new MicrostripCircuitModel.Point2D(22e-3, -11e-3), new MicrostripCircuitModel.Point2D(11e-3, -11e-3), "_s2R", "_p3feed"));
        model.lines.add(createLine(model, lid++, w70, 11e-3, true, new MicrostripCircuitModel.Point2D(22e-3, -11e-3), new MicrostripCircuitModel.Point2D(33e-3, -11e-3), "_s2R", "_p4feed"));

        model.lines.add(createLine(model, lid++, wf, 11e-3, false, new MicrostripCircuitModel.Point2D(-33e-3, -11e-3), new MicrostripCircuitModel.Point2D(-33e-3, 0), "_p1feed", "_p1"));
        model.lines.add(createLine(model, lid++, wf, 11e-3, false, new MicrostripCircuitModel.Point2D(-11e-3, -11e-3), new MicrostripCircuitModel.Point2D(-11e-3, 0), "_p2feed", "_p2"));
        model.lines.add(createLine(model, lid++, wf, 11e-3, false, new MicrostripCircuitModel.Point2D(11e-3, -11e-3), new MicrostripCircuitModel.Point2D(11e-3, 0), "_p3feed", "_p3"));
        model.lines.add(createLine(model, lid++, wf, 11e-3, false, new MicrostripCircuitModel.Point2D(33e-3, -11e-3), new MicrostripCircuitModel.Point2D(33e-3, 0), "_p4feed", "_p4"));

        double f = 6.71e9;
        java.util.Set<MicrostripCircuitModel.LineSegment> visited = new java.util.HashSet<>();
        // compute Zin at _net1 (without the 6mm input line)
        visited.add(model.lines.get(0)); // skip line 0
        // Find Zin looking into _net1
        MicrostripCircuitModel.ComplexNum zNet1 = computeNode(model, "_net1", f, visited);
        MicrostripCircuitModel.ComplexNum zNet0 = model.computeZin(f);
        System.out.printf("At 6.71 GHz: Zin at _net1 (y = -22 mm) = %.2f + %.2fj Ohm%n", zNet1.re, zNet1.im);
        for (double d = 0; d <= 6.0; d += 0.5) {
            MicrostripCircuitModel.ComplexNum zPos = MicrostripCircuitModel.tline(wf, d * 1e-3, f, model.substrateH, model.epsilonR, zNet1);
            MicrostripCircuitModel.ComplexNum gam = zPos.minus(MicrostripCircuitModel.ComplexNum.of(50, 0)).div(zPos.plus(MicrostripCircuitModel.ComplexNum.of(50, 0)));
            double s11db = 20 * Math.log10(gam.abs());
            System.out.printf("  dist from split = %.1f mm (y = %.1f mm): Zin = %6.2f + %6.2fj Ohm | S11 = %6.2f dB%n",
                    d, -22.0 - d, zPos.re, zPos.im, s11db);
        }
    }

    private static MicrostripCircuitModel.ComplexNum computeNode(MicrostripCircuitModel model, String node, double freq, java.util.Set<MicrostripCircuitModel.LineSegment> visited) {
        if (model.nodeToPatch.containsKey(node)) {
            return model.nodeToPatch.get(node).computeZ(freq);
        }
        java.util.List<MicrostripCircuitModel.ComplexNum> branchLoads = new java.util.ArrayList<>();
        for (MicrostripCircuitModel.LineSegment line : model.nodeToLines.get(node)) {
            if (visited.contains(line)) continue;
            String next = line.nodeA.equals(node) ? line.nodeB : line.nodeA;
            visited.add(line);
            MicrostripCircuitModel.ComplexNum zNext = computeNode(model, next, freq, visited);
            branchLoads.add(MicrostripCircuitModel.tline(line.width, line.length, freq, model.substrateH, model.epsilonR, zNext));
        }
        MicrostripCircuitModel.ComplexNum yTot = MicrostripCircuitModel.ComplexNum.of(0, 0);
        for (MicrostripCircuitModel.ComplexNum z : branchLoads) {
            yTot = yTot.plus(MicrostripCircuitModel.ComplexNum.of(1, 0).div(z));
        }
        return MicrostripCircuitModel.ComplexNum.of(1, 0).div(yTot);
    }

    private static MicrostripCircuitModel.LineSegment createLine(
            MicrostripCircuitModel model, int id, double w, double len, boolean isHoriz,
            MicrostripCircuitModel.Point2D pA, MicrostripCircuitModel.Point2D pB, String nA, String nB) {
        MicrostripCircuitModel.LineSegment l = new MicrostripCircuitModel.LineSegment(id, w, len, isHoriz, pA, pB);
        l.nodeA = nA;
        l.nodeB = nB;
        model.nodeToLines.computeIfAbsent(nA, k -> new java.util.ArrayList<>()).add(l);
        model.nodeToLines.computeIfAbsent(nB, k -> new java.util.ArrayList<>()).add(l);
        return l;
    }
}
