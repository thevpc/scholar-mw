package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.nuts.Nuts;
import net.thevpc.nuts.concurrent.NConcurrent;
import net.thevpc.nuts.time.NDuration;
import net.thevpc.nuts.util.NArrays;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.cache.CacheMode;
import net.thevpc.scholar.hadrumaths.geom.DefaultHGeometryList;
import net.thevpc.scholar.hadrumaths.geom.DefaultHPolygon;
import net.thevpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.symbolic.NumberExpr;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruwaves.Material;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.scholar.hadruwaves.WallBorders;
import net.thevpc.scholar.hadruwaves.mom.sources.planar.CstPlanarSource;
import net.thevpc.scholar.hadruwaves.mom.sources.planar.DefaultPlanarSources;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpAdaptiveMesh;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.CellBoundaries;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.UserSinePattern;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class Antenna1ValidationTest {

    @BeforeAll
    public static void init() {
        Nuts.require();
    }

    @Test
    public void testAntennaPatch() {
        Maths.Config.setCacheEnabled(false);
        Maths.Config.setPersistenceCacheMode(CacheMode.DISABLED);

        MomStructure mom = new MomStructure();
        mom.setProjectType(ProjectType.PLANAR_STRUCTURE);

        // === PHYSICS PARAMETERS ===
        double f = 2.4 * Maths.GHZ;
        double er = 4.4;
        double h = 1 * Maths.MM;
        double W = 3 * Maths.MM;  // Width → sets Z₀ ≈ 50-65Ω (NOT resonant)

        // === GUIDED WAVELENGTH & LENGTH ===
        double eeff = (er + 1) / 2 + (er - 1) / 2 / Math.sqrt(1 + 12 * h / W);
        double lambda_g = Maths.C / f / Math.sqrt(eeff);  // ≈ 65 mm
        double L = lambda_g / 4;                          // ≈ 16.25 mm (resonant dimension)
//        double L = 15.447*Maths.MM;                          // ≈ 16.25 mm (resonant dimension)
//        double L = 38.8*Maths.MM;                          // ≈ 16.25 mm (resonant dimension)

        // === DOMAIN & Maths ===
        double pad = 200 * Maths.MM;  // Explicit padding to avoid boundary effects
        double xmin = -W / 2 - pad;
        double ymin = 0;          // Ground plane is at y=0 (handled by courtCircuit)
        double xmax = W / 2 + pad;
        double ymax = L + pad;
        double V = 1.0;
        double gap = 0.2 * Maths.MM;
        Domain box = Domain.ofPoints(xmin, ymin, xmax, ymax);
//        Domain box = Domain.ofPoints(-5*W, 0, 5*W, 5*L);
        mom.setDomain(box);
        mom.setBorders(WallBorders.EEEE);

        // === GEOMETRY: Centered microstrip trace ===
        Domain trace = Domain.ofPoints(-W / 2, 0, W / 2, L);
        GpAdaptiveMesh testFunctions = new GpAdaptiveMesh(
                new DefaultHGeometryList(trace, new DefaultHPolygon(trace)),  // ✅ Single polygon per your API
                new UserSinePattern(0, 6, CellBoundaries.DDxUDy, CellBoundaries.UUxUDy),
                null, new MeshAlgoRect());
        mom.setTestFunctions(testFunctions);

//        g[10]={0,(199.27688058958154*cos(280.70246303257056*Y)*domain(-0.0015->0.0015,0.0->0.016787843360811194))}
//        g[11]={0,(199.27688058958154*cos(467.8374383876176*Y)*domain(-0.0015->0.0015,0.0->0.016787843360811194))}
//        g[12]={0,(281.82006719719*cos(1047.1975511965977*X+1.5707963267948966)*cos(93.56748767752353*Y)*domain(-0.0015->0.0015,0.0->0.016787843360811194))}
//        g[13]={0,(281.82006719719*cos(1047.1975511965977*X+1.5707963267948966)*cos(280.70246303257056*Y)*domain(-0.0015->0.0015,0.0->0.016787843360811194))}
//        g[14]={0,(281.82006719719*cos(1047.1975511965977*X+1.5707963267948966)*cos(467.8374383876176*Y)*domain(-0.0015->0.0015,0.0->0.016787843360811194))}
//        mom.setTestFunctions(new ListTestFunctions()
//                .add(Maths.vector(Maths.DCZERO,Maths.expr(199.27688058958154).mul(Maths.cos(Maths.Y.mul(280.70246303257056)))))
//                .add(Maths.vector(Maths.DCZERO,Maths.expr(199.27688058958154).mul(Maths.cos(Maths.Y.mul(467.8374383876176)))))
//        );

        DoubleToVector[] gp = mom.testFunctions().toArray();

        // === BOUNDARIES ===
        mom.setFirstBoxSpace(BoxSpace.shortCircuit(Material.substrate("FR4", er,0.02), h));  // PEC ground at y=0, substrate h=1mm
//        mom.setSecondBoxSpace(BoxSpace.matchedLoad(Material.substrate("FR4", er)));  // PEC ground at y=0, substrate h=1mm
        mom.setSecondBoxSpace(BoxSpace.matchedLoad(Material.VACUUM));     // Absorbing top boundary

        // === EXCITATION: 1V delta-gap at y=0 ===
        Domain source = Domain.ofPoints(-W / 2, 0, W / 2, 0.2 * Maths.MM);
        mom.setSources(new DefaultPlanarSources(CstPlanarSource.ofVoltage(1, source, Axis.Y,Complex.of(50))));

        // === SOLVER ===
        mom.setFrequency(f);
        mom.setCircuitType(CircuitType.SERIAL);
        mom.modeFunctions().setSize(2000);

        System.out.println(mom.dump());

        for (int i = 0; i < gp.length; i++) {
            DoubleToVector gi = gp[i];
            System.out.println("g[" + i + "]=" + gi);
        }
        if (Boolean.getBoolean("plot")) {
            Plot.title("gp").domain(box).plot(NArrays.append(gp, Maths.vector(source.toDD(), Maths.exp(source).toDD())));
        }
        ModeInfo[] modes = mom.modes();
        DoubleToVector[] fn = mom.modeFunctions().toArray();
        for (int i = 0; i < 10; i++) {
            System.out.println("f[" + i + "]=f[" + modes[i] + "]=" + fn[i]);
        }

        System.out.println("**********");
        System.out.println(" A MATRIX");
        System.out.println("**********");
        System.out.println(mom.matrixA().evalMatrix());

        System.out.println("**********");
        System.out.println(" B MATRIX");
        System.out.println("**********");
        System.out.println(mom.matrixB().evalMatrix());


        NumberExpr aa = mom.getScalarProductOperator().eval(modes[4].fn, gp[0]);
        System.out.println(aa);

        System.out.println("**********");
        System.out.println(" <fn,gp> MATRIX");
        System.out.println("**********");
        ComplexMatrix gpFnSp = mom.testModeScalarProducts();
        for (int p = 0; p < gp.length; p++) {
            for (int n = 0; n < 10; n++) {
                Complex sp = gpFnSp.get(p, n);
                System.out.println("<f[" + n + "].g[" + p + "]>=" + sp);
            }
        }

        // === COMPUTE ===
        Complex zin = mom.inputImpedance().evalComplex();

        String lastValid = "11.807183603375767i";
        System.out.println("=== λ/4 Open-Ended Microstrip Test ===");
        System.out.printf("Trace: W=%.1fmm, L=%.2fmm (λ_g/4 = %.2fmm)\n", W / Maths.MM, L / Maths.MM, lambda_g / 4 / Maths.MM);
        System.out.println("Expected: Zin ≈ 0-20 Ω (Open end → Short at input)");
        System.out.println("Last Valid: Zin ≈ " + lastValid + "Ω");
        System.out.println("Computed: Zin = " + zin + " Ω");
        System.out.println("          |Zin| = " + zin.abs() + " Ω");
        System.out.println("          Phase = " + Math.toDegrees(zin.argdbl()) + "°");

        boolean pass = zin.absdbl() < 30;
        System.out.println("\n" + (pass ? "✅ PASS" : "❌ FAIL") +
                ": MoM transmission-line physics " + (pass ? "verified" : "broken"));
        org.junit.jupiter.api.Assertions.assertTrue(pass, "MoM transmission-line physics should yield |Zin| < 30");

        if (Boolean.getBoolean("plot")) {
            Plot.title("Jx").domain(box).plot(mom.current().evalMatrix(Axis.X, box.dtimes(100)));
            Plot.title("Jy").domain(box).plot(mom.current().evalMatrix(Axis.Y, box.dtimes(100)));
            NConcurrent.sleep(NDuration.ofSeconds(5));
        }
    }
}
