package net.thevpc.scholar.hadruwaves;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruplot.PlotComponent;
import net.thevpc.scholar.hadruwaves.mom.*;
import net.thevpc.scholar.hadruwaves.mom.modes.BoxModeFunctions;
import net.thevpc.scholar.hadruwaves.mom.modes.NonPeriodicBoxModes;
import net.thevpc.scholar.hadruwaves.mom.modes.PeriodicBoxModes;
import net.thevpc.scholar.hadruwaves.mom.sources.modal.CutOffModalSources;
import net.thevpc.scholar.hadruwaves.util.AdmittanceValue;
import net.thevpc.scholar.hadruwaves.util.ImpedanceValue;

import static java.lang.Math.PI;
import static java.lang.Math.sqrt;
import static net.thevpc.scholar.hadrumaths.FunctionFactory.*;
import static net.thevpc.scholar.hadrumaths.Maths.*;

public final class Physics {

    private Physics() {
    }

    public static ModeIndex TE(int m, int n) {
        return ModeIndex.mode(ModeType.TE, m, n);
    }

    public static ModeIndex TM(int m, int n) {
        return ModeIndex.mode(ModeType.TM, m, n);
    }

    public static ModeIndex TEM() {
        return ModeIndex.mode(ModeType.TEM, 0, 0);
    }

    public static double K0(double freq) {
        return omega(freq) / Maths.C;//* Math.sqrt(U0 * EPS0);
    }

    public static double freqByK0(double k0) {
        return (k0 * Maths.C) / (2 * Math.PI);
    }

    public static double waveLength(double freq) {
        return Maths.C / freq;
    }

    public static double omega(double freq) {
        return 2.0 * Math.PI * freq;
    }


    public static BoxModes boxModes(WallBorders _borders, Domain domain) {
        return boxModes(_borders, domain, 0, 0, null);
    }

    public static BoxModes boxModes(WallBorders _borders, Domain domain, Axis axis) {
        return boxModes(_borders, domain, 0, 0, axis);
    }

    public static BoxModes boxModes(WallBorders _borders, Domain domain, double alphax, double betay, Axis axis) {
        switch (_borders) {
            case EMEM: {
                BoxModes r = new NonPeriodicBoxModes(_borders, domain, axis) {

                    @Override
                    protected Expr getFctX(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return sinXsinY(nx, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }

                    @Override
                    protected Expr getFctY(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return cosXcosY(ny, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }

                    protected Expr getFctYTEM(Domain domain) {
                        double a = domain.getXwidth();
                        double b = domain.getYwidth();
                        return (expr(1.0 / sqrt(a * b), domain));
                    }

                    @Override
                    public boolean accept(ModeIndex i) {
                        switch (i.getModeType()) {
                            case TM: {
                                return i.n() != 0;//i.n() != 0;
                            }
                            case TE: {
                                return i.m() != 0;
                            }
                            case TEM: {
                                return i.m() == 0 && i.n() == 0;
                            }
                        }
                        return false;
                    }
                };
                r.ma = 1;
                r.mb = 0;
                r.na = 1;
                r.nb = 0;
                r.allowedModes = new ModeType[]{ModeType.TEM, ModeType.TM, ModeType.TE};
                return r;
            }
            case MEME: {
                BoxModes r = new NonPeriodicBoxModes(_borders, domain, axis) {

                    @Override
                    protected Expr getFctX(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return cosXcosY(nx, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }

                    @Override
                    protected Expr getFctY(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return sinXsinY(ny, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }

                    @Override
                    protected Expr getFctXTEM(Domain domain) {
                        double a = domain.getXwidth();
                        double b = domain.getYwidth();
                        return expr(1.0 / sqrt(a * b), domain);
                    }

                    @Override
                    public boolean accept(ModeIndex i) {
                        switch (i.type()) {
                            case TM: {
                                return i.m() != 0;
                            }
                            case TE: {
                                return i.n() != 0;
                            }
                            case TEM: {
                                return i.m() == 0 && i.n() == 0;
                            }
                        }
                        return false;
                    }
                };
                r.ma = 1;
                r.mb = 0;
                r.na = 1;
                r.nb = 0;
                r.allowedModes = new ModeType[]{ModeType.TEM, ModeType.TE, ModeType.TM};
                return r;
            }
            case MMMM: {
                BoxModes r = new NonPeriodicBoxModes(_borders, domain, axis) {

                    @Override
                    protected Expr getFctX(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return sinXcosY(nx, m * PI / a, -(m * PI / a) * domain.xmin(), n * PI / b, -(n * PI / b) * domain.ymin(), domain);
                    }

                    @Override
                    protected Expr getFctY(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return cosXsinY(ny, m * PI / a, -(m * PI / a) * domain.xmin(), n * PI / b, -(n * PI / b) * domain.ymin(), domain);
                    }

                    @Override
                    public boolean accept(ModeIndex i) {
                        switch (i.type()) {
                            case TM: {
                                return (i.m() != 0 || i.n() != 0);
                            }
                            case TE: {
                                return (i.m() != 0 && i.n() != 0);
                            }
                        }
                        return false;
                    }
                };
                r.ma = 1;
                r.mb = 0;
                r.na = 1;
                r.nb = 0;
                r.allowedModes = new ModeType[]{ModeType.TM, ModeType.TE};
                return r;
            }
            case EEEE: {
                BoxModes r = new NonPeriodicBoxModes(_borders, domain, axis) {

                    @Override
                    protected Expr getFctX(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return cosXsinY(nx, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }

                    @Override
                    protected Expr getFctY(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return sinXcosY(ny, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }

                    @Override
                    public boolean accept(ModeIndex i) {
                        if (i.type() == ModeType.TM) {
                            return (i.m() != 0 && i.n() != 0);
                        } else if (i.type() == ModeType.TE) {
                            return (i.m() != 0 || i.n() != 0);
                        } else {
                            return false;
                        }
                    }
                };
                r.ma = 1;
                r.mb = 0;
                r.na = 1;
                r.nb = 0;
                r.allowedModes = new ModeType[]{ModeType.TE, ModeType.TM};
                return r;
            }
            case MEEE: {
                BoxModes r = new NonPeriodicBoxModes(_borders, domain, axis) {

                    @Override
                    protected Expr getFctX(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return cosXsinY(nx, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }

                    @Override
                    protected Expr getFctY(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return sinXcosY(ny, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }


                    @Override
                    public boolean accept(ModeIndex i) {
                        //OK
                        switch (i.type()) {
                            case TM: {
                                return (i.m() != 0 && i.n() != 0);
                            }
                            case TE: {
                                return (i.m() != 0 || i.n() != 0);
                            }
                            case TEM: {
                                return false;
                            }
                        }
                        return false;
                    }
                };
                r.ma = 1;
                r.mb = 0;
                r.na = 1;
                r.nb = 0;
                r.allowedModes = new ModeType[]{ModeType.TE, ModeType.TM};
                return r;
            }
            case EMMM: {
                BoxModes r = new NonPeriodicBoxModes(_borders, domain, axis) {

                    @Override
                    protected Expr getFctX(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return sinXcosY(nx, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }

                    @Override
                    protected Expr getFctY(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return cosXsinY(ny, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }


                    @Override
                    public boolean accept(ModeIndex i) {
                        switch (i.type()) {
                            case TM: {
                                return i.n() != 0;//(i.n() != 0);
                            }
                            case TE: {
                                return i.n() != 0 && i.m() != 0;
                            }
                        }
                        return false;
                    }
                };
                r.ma = 1;
                r.mb = 0;
                r.na = 1;
                r.nb = -0.5;
                r.allowedModes = new ModeType[]{ModeType.TM, ModeType.TE};
                return r;
            }
            case EMME: {
                BoxModes r = new NonPeriodicBoxModes(_borders, domain, axis) {

                    @Override
                    protected Expr getFctX(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return cosXcosY(nx, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }

                    @Override
                    protected Expr getFctY(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return sinXsinY(ny, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }


                    @Override
                    public boolean accept(ModeIndex i) {
                        //TODO check ME
                        switch (i.type()) {
                            case TE: {
                                return i.m() != 0 && i.n() != 0;//(i.m() != 0 && i.n() != 0);
                            }
                            case TM: {
                                return i.m() != 0 && i.n() != 0;//( && );
                            }
                        }
                        return false;
                    }
                };
                r.ma = 1;
                r.mb = -0.5;
                r.na = 1;
                r.nb = -0.5;
                r.allowedModes = new ModeType[]{ModeType.TM, ModeType.TE};
                return r;
            }
            case MMME: {
                BoxModes r = new NonPeriodicBoxModes(_borders, domain, axis) {

                    @Override
                    protected Expr getFctX(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return cosXcosY(nx, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }

                    @Override
                    protected Expr getFctY(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return sinXsinY(ny, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }


                    @Override
                    public boolean accept(ModeIndex i) {
                        switch (i.type()) {
                            case TM: {
                                return i.m() != 0;
                            }
                            case TE: {
                                return i.m() != 0 && i.n() != 0;
                            }
                        }
                        return false;
                    }
                };
                r.ma = 1;
                r.mb = -0.5;
                r.na = 1;
                r.nb = 0;
                r.allowedModes = new ModeType[]{ModeType.TM, ModeType.TE};
                return r;
            }
            case MMEE: {
                BoxModes r = new NonPeriodicBoxModes(_borders, domain, axis) {

                    @Override
                    protected Expr getFctX(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return cosXsinY(nx, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }

                    @Override
                    protected Expr getFctY(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return sinXcosY(ny, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }


                    @Override
                    public boolean accept(ModeIndex i) {
                        //TODO check ME
                        switch (i.type()) {
                            case TE:
                            case TM: {

                                return (i.m() != 0 && i.n() != 0);
                            }
                        }
                        return false;
                    }
                };
                r.ma = 1;
                r.mb = -0.5;
                r.na = 1;
                r.nb = -0.5;
                r.allowedModes = new ModeType[]{ModeType.TM, ModeType.TE};
                return r;
            }
            case EEEM: {
                BoxModes r = new NonPeriodicBoxModes(_borders, domain, axis) {

                    @Override
                    protected Expr getFctX(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return sinXsinY(nx, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }

                    @Override
                    protected Expr getFctY(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return cosXcosY(ny, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }


                    @Override
                    public boolean accept(ModeIndex i) {
                        //TODO check ME
                        switch (i.type()) {
                            case TE: {
                                return (i.m() != 0);
                            }
                            case TM: {
                                return (i.m() != 0 && i.n() != 0);
                            }
                        }
                        return false;
                    }
                };
                r.ma = 1;
                r.mb = -0.5;
                r.na = 1;
                r.nb = 0;
                r.allowedModes = new ModeType[]{ModeType.TM, ModeType.TE};
                return r;
            }
            case EEMM: {
                BoxModes r = new NonPeriodicBoxModes(_borders, domain, axis) {

                    @Override
                    protected Expr getFctX(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return sinXcosY(nx, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }

                    @Override
                    protected Expr getFctY(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return cosXsinY(ny, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }


                    @Override
                    public boolean accept(ModeIndex i) {
                        switch (i.type()) {
                            case TE: {
                                return (i.m() != 0) && (i.n() != 0);
                            }
                            case TM: {
                                return (i.m() != 0) && (i.n() != 0);
                            }
                        }
                        return false;
                    }
                };
                r.ma = 1;
                r.mb = -0.5;
                r.na = 1;
                r.nb = -0.5;
                r.allowedModes = new ModeType[]{ModeType.TM, ModeType.TE};
                return r;
            }
            case MEEM: {
                BoxModes r = new NonPeriodicBoxModes(_borders, domain, axis) {

                    @Override
                    protected Expr getFctX(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return sinXsinY(nx, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }

                    @Override
                    protected Expr getFctY(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return cosXcosY(ny, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }


                    @Override
                    public boolean accept(ModeIndex i) {
                        switch (i.type()) {
                            case TE: {
                                return (i.m() != 0 && i.n() != 0);
                            }
                            case TM: {
                                return (i.m() != 0 && i.n() != 0);
                            }
                        }
                        return false;
                    }
                };
                r.ma = 1;
                r.mb = -0.5;
                r.na = 1;
                r.nb = -0.5;
                r.allowedModes = new ModeType[]{ModeType.TM, ModeType.TE};
                return r;
            }
            case EMEE: {
                BoxModes r = new NonPeriodicBoxModes(_borders, domain, axis) {

                    @Override
                    protected Expr getFctX(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return cosXsinY(nx, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }

                    @Override
                    protected Expr getFctY(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return sinXcosY(ny, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }


                    @Override
                    public boolean accept(ModeIndex i) {
                        //TODO check ME
                        switch (i.type()) {
                            case TE: {
                                return (i.m() != 0);
                            }
                            case TM: {
                                return i.m() != 0 && i.n() != 0;
                            }
                        }
                        return false;
                    }
                };
                r.ma = 1;
                r.mb = -0.5;
                r.na = 1;
                r.nb = 0;
                r.allowedModes = new ModeType[]{ModeType.TM, ModeType.TE};
                return r;
            }
            case MMEM: {
                BoxModes r = new NonPeriodicBoxModes(_borders, domain, axis) {

                    @Override
                    protected Expr getFctX(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return sinXsinY(nx, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }

                    @Override
                    protected Expr getFctY(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return cosXcosY(ny, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }


                    @Override
                    public boolean accept(ModeIndex i) {
                        switch (i.type()) {
                            case TM: {
                                return i.n() != 0;
                            }
                            case TE: {
                                return i.m() != 0 && i.n() != 0;
                            }
                        }
                        return false;
                    }
                };
                r.ma = 1;
                r.mb = 0;
                r.na = 1;
                r.nb = -0.5;
                r.allowedModes = new ModeType[]{ModeType.TM, ModeType.TE};
                return r;
            }
            case EEME: {
                BoxModes r = new NonPeriodicBoxModes(_borders, domain, axis) {

                    @Override
                    protected Expr getFctX(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return cosXcosY(nx, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }

                    @Override
                    protected Expr getFctY(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return sinXsinY(ny, (m * PI / a), -(m * PI / a) * domain.xmin(), (n * PI / b), -(n * PI / b) * domain.ymin(), domain);
                    }


                    @Override
                    public boolean accept(ModeIndex i) {
                        //OK
                        switch (i.type()) {
                            case TM: {
                                return (i.m() != 0 && i.n() != 0);
                            }
                            case TE: {
                                return i.n() != 0;//(i.m() != 0 && i.n() != 0);
                            }
                            case TEM: {
                                return false;
                            }
                        }
                        return false;
                    }
                };
//                r.ma = 1;
//                r.mb = 0;
//                r.na = 1;
//                r.nb = 0.5;
                r.ma = 1;
                r.mb = 0;
                r.na = 1;
                r.nb = -0.5;
                r.allowedModes = new ModeType[]{ModeType.TM, ModeType.TE};
                return r;
            }
            case MEMM: {
                BoxModes r = new NonPeriodicBoxModes(_borders, domain, axis) {

                    @Override
                    protected Expr getFctX(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return sinXcosY(nx, m * PI / a, -(m * PI / a) * domain.xmin(), n * PI / b, -(n * PI / b) * domain.ymin(), domain);
                    }

                    @Override
                    protected Expr getFctY(Domain domain, double m, double n, double a, double b, double nx, double ny) {
                        return cosXsinY(ny, m * PI / a, -(m * PI / a) * domain.xmin(), n * PI / b, -(n * PI / b) * domain.ymin(), domain);
                    }


                    @Override
                    public boolean accept(ModeIndex i) {
                        //TODO check ME
                        switch (i.type()) {
                            case TM: {
                                return (i.m() != 0);
                            }
                            case TE: {
                                return (i.m() != 0 && i.n() != 0);
                            }
                            case TEM: {
                                return false;
                            }
                        }
                        return false;
                    }
                };
                r.ma = 1;
                r.mb = -0.5;
                r.na = 1;
                r.nb = 0;
                r.allowedModes = new ModeType[]{ModeType.TM, ModeType.TE};
                return r;
            }
            case PPPP: {
                return new PeriodicBoxModes(_borders, domain, axis, alphax, betay);
            }
            default: {
                throw new UnsupportedOperationException(_borders + " : Not supported yet.");
            }
        }
//        throw new UnsupportedOperationException(borders + " : Not supported yet.");
    }


    public static BoxSpace openCircuitBoxSpace(Material material, double width) {
        return BoxSpace.openCircuit(material, width);
    }

    public static BoxSpace matchedLoadBoxSpace(Material material) {
        return BoxSpace.matchedLoad(material);
    }

    public static BoxSpace shortCircuitBoxSpace(Material material, double width) {
        return BoxSpace.shortCircuit(material, width);
    }

    public static BoxSpace nothingBoxSpace() {
        return BoxSpace.nothing();
    }


    public static void plotWallBorders(WallBorders type) {
        plotWallBorders(type, -1, 0, 0, 0.012, 0.004, 4.79 * Maths.GHZ);
    }

    public static void plotWallBorders(WallBorders borders, int count, double x, double y, double w, double h, double f) {
        ModeFunctions fnModeFunctions = new BoxModeFunctions();
        fnModeFunctions.setSize(count < 1 ? 30 : count);

        DefaultModeFunctionsEnv env=new DefaultModeFunctionsEnv();
        //FnBaseFunctions fnBaseFunctions = new Fn("EMMM");
        //FnBaseFunctions fnBaseFunctions = new Fn("MEME");
        env.setBorders(borders);
        env.setDomain(Domain.ofWidth(x, w, y, h));
        env.setHintFnModes(ModeType.TEM, ModeType.TM, ModeType.TE);
        //fnBaseFunctions.setHintFnModeTypes(Mode.TEM, ModeType.TM);
        env.setFirstBoxSpace(BoxSpace.shortCircuit(Material.substrate(2.2), 1.59 * 1E-3));
        env.setSecondBoxSpace(BoxSpace.matchedLoad(Material.VACUUM));
        env.setFrequency(f);
//        fnBaseFunctions.setProjectType(ProjectType.PLANAR_STRUCTURE);
        env.setHintAxisType(HintAxisType.XY_SEPARATED);
        env.setSources(new CutOffModalSources(1));
        fnModeFunctions.setEnv(env);
//        fnModeFunctions.a();
//        ModeInfo ii = new ModeInfo(ModeType.TM, 2, 1);
//        fnModeFunctions.fillIndex(ii, 0);
//        System.out.println(DefaultBoxModeFunctions.descMode(ii));
//        ModeInfo fte10 = fnBaseFunctions.get(new ModeInfo(Mode.TE, 1, 0));
//        for (ModeInfo m : fnModeFunctions.getModes()) {
//            System.out.println(DefaultBoxModeFunctions.descMode(m));
//            //System.out.println("fx=" + fte10.fn.fx);
//        }
        WallBorders b = env.getBorders();
        final String ttl = fnModeFunctions.toString() + "=>E(x=" + b.getExDescription() + ",y=" + b.getEyDescription() + ") ; J(x=" + b.getJxDescription() + ",y=" + b.getJyDescription() + ")";
        PlotComponent jFrame = Plot.title(ttl).plot(fnModeFunctions.arr());
//        jFrame.display();
        //jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Complex[][] gfps = new Complex[fnModeFunctions.count()][fnModeFunctions.count()];
        ModeInfo[] indexes = fnModeFunctions.getModes();
        int max = fnModeFunctions.count();
//        int progress=0;
        for (int q = 0; q < gfps.length; q++) {
            for (int n = 0; n < max; n++) {
                gfps[q][n] = Maths.scalarProduct(indexes[n].fn, indexes[q].fn).toComplex();
            }
        }
        Plot.title("<fn,fn>").asMatrix().plot(matrix(gfps));//.display();

    }

    public static AdmittanceValue evalLayersAdmittance(StrLayer[] layers, Complex gamma1, Complex gamma2, Complex z0) {
        AdmittanceValue yl = new AdmittanceValue(Complex.ZERO);
        double l = 0;
        for (StrLayer layer : layers) {
            l+=layer.getWidth();
            Complex gamma=l<0?gamma1:gamma2;
            Complex zt = layer.getImpedance().impedanceValue();//
            Complex z=z0.mul(
                    zt.plus(z0.mul(tanh(gamma.mul(l)))).div(
                            z0.plus(zt.mul(tanh(gamma.mul(l))))
                    )
            );
            yl=yl.parallel(impedance(z));
        }
        return yl;
    }

    public static Complex addSerialImpedance(Complex impedance1, Complex impedance2) {
        return impedance1.plus(impedance2);
    }

    public static Complex addParallelImpedance(Complex impedance1, Complex impedance2) {
        return (impedance1.inv().plus(impedance2.inv())).inv();
    }

    public static Complex addParallelAdmittance(Complex admittance1, Complex admittance2) {
        return admittance1.plus(admittance2);
    }

    public static Complex addSerialAdmittance(Complex admittance1, Complex admittance2) {
        return (admittance1.inv().plus(admittance2.inv())).inv();
    }

    public static ImpedanceValue impedance(Complex c) {
        return c == null ? null : new ImpedanceValue(c);
    }

    public static AdmittanceValue admittance(Complex c) {
        return c == null ? null : new AdmittanceValue(c);
    }

    public static class Config {
        public static void setModeIndexCacheCount(int count) {
            ModeIndex.setCachedModeIndexSize(count);
        }

        public static void setModeIndexCacheEnabled(boolean enabled) {
            ModeIndex.setCachedEnabled(enabled);
        }
    }

    public static String getHadruwavesVersion() {
        return HadruwavesService.getVersion();
    }

//    public static void main(String[] args) {
//        CstPlotDoubleRow[] cstPlotDoubleRows = loadCSTLinearPlot(new File("/run/media/vpc/KHADHRAOUII/s11 results/s11Boundaries.txt"));
//        CstPlotDoubleRow[] cstPlotDoubleUnboxedRows = loadCSTLinearPlot(new File("/run/media/vpc/KHADHRAOUII/s11 results/s11Unboxed.txt"));
//        ValuesPlotModel plotModel = (ValuesPlotModel) Plot.loadPlotModel(new File("/run/media/vpc/KHADHRAOUII/s11 results/s11BoxVariationSave.jfig"));
//        PlotLines pl=new PlotLines();
//        pl.addValues(cstPlotDoubleRows[1].getTitle(), ArrayUtils.mul(cstPlotDoubleRows[0].getValues(),1*Maths.GHZ),cstPlotDoubleRows[1].getValues());
//        pl.addValues(cstPlotDoubleUnboxedRows[1].getTitle(), ArrayUtils.mul(cstPlotDoubleUnboxedRows[0].getValues(),1*Maths.GHZ),cstPlotDoubleUnboxedRows[1].getValues());
//        pl.addValues(plotModel.getYtitle(0),plotModel.getX(0),plotModel.getZ(0));
//        pl.addValues(plotModel.getYtitle(2),plotModel.getX(0),plotModel.getZ(2));
//        Plot.asCurve().plot(pl.interpolate(InterpolationStrategy.SMOOTH));
//
//        System.out.println(cstPlotDoubleRows);
//    }

}
