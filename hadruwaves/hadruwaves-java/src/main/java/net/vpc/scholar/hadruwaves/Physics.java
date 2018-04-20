package net.vpc.scholar.hadruwaves;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.plot.PlotComponent;
import net.vpc.scholar.hadruwaves.mom.BoxSpaceFactory;
import net.vpc.scholar.hadruwaves.mom.HintAxisType;
import net.vpc.scholar.hadruwaves.mom.BoxSpace;
import net.vpc.scholar.hadruwaves.mom.modes.DefaultBoxModeFunctions;
import net.vpc.scholar.hadruwaves.mom.modes.ModeFunctionsBase;
import net.vpc.scholar.hadruwaves.mom.modes.NonPeriodicBoxModes;
import net.vpc.scholar.hadruwaves.mom.modes.PeriodicBoxModes;
import net.vpc.scholar.hadruwaves.mom.sources.modal.CutOffModalSources;

import static java.lang.Math.PI;
import static java.lang.Math.sqrt;
import static net.vpc.scholar.hadrumaths.FunctionFactory.*;
import static net.vpc.scholar.hadrumaths.Maths.*;

public final class Physics {

    private Physics() {
    }

    public static ModeIndex TE(int m,int n){
        return ModeIndex.mode(ModeType.TE, m,n);
    }

    public static ModeIndex TM(int m,int n){
        return ModeIndex.mode(ModeType.TM, m,n);
    }

    public static ModeIndex TEM(){
        return ModeIndex.mode(ModeType.TEM, 0,0);
    }

    public static double K0(double freq) {
        return omega(freq) / Maths.C;//* Math.sqrt(U0 * EPS0);
    }

    public static double freqByK0(double k0) {
        return (k0 * Maths.C) / (2 * Math.PI);
    }

    public static double lambda(double freq) {
//        return freq / Physics.C;
        return Maths.C / freq;
    }

    public static double waveLength(double freq) {
//        return freq / Physics.C;
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
                                return i.n() != 0 ;//i.n() != 0;
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
                            throw new RuntimeException("impossible");
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
                            case TM:{
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
                            case TE:{
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
                                return (i.m() != 0 );
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
                        switch (i.type()){
                            case TE:{
                                return (i.m() != 0 ) && (i.n() != 0 );
                            }
                            case TM:{
                                return (i.m() != 0 ) && (i.n() != 0 );
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
                        switch (i.type()){
                            case TE:{
                                return (i.m() != 0 && i.n() != 0);
                            }
                            case TM:{
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
                                return (i.m() != 0 );
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


    public static BoxSpace openCircuitBoxSpace(double epsr, double width) {
        return BoxSpaceFactory.openCircuit(epsr, width);
    }

    public static BoxSpace matchedLoadBoxSpace(double epsr) {
        return BoxSpaceFactory.matchedLoad(epsr);
    }

    public static BoxSpace shortCircuitBoxSpace(double epsr, double width) {
        return BoxSpaceFactory.shortCircuit(epsr, width);
    }

    public static BoxSpace nothingBoxSpace() {
        return BoxSpaceFactory.nothing();
    }


    public static void plotWallBorders(WallBorders type) {
        plotWallBorders(type, -1, 0, 0, 0.012, 0.004, 4.79 * Maths.GHZ);
    }

    public static void plotWallBorders(WallBorders borders, int count, double x, double y, double w, double h, double f) {
        ModeFunctionsBase fnModeFunctions = new DefaultBoxModeFunctions(borders);
        //FnBaseFunctions fnBaseFunctions = new Fn("EMMM");
        //FnBaseFunctions fnBaseFunctions = new Fn("MEME");
        fnModeFunctions.setDomain(Domain.forWidth(x, w, y, h));
        fnModeFunctions.setMaxSize(count < 1 ? 30 : count);
        fnModeFunctions.setHintFnModes(ModeType.TEM, ModeType.TM, ModeType.TE);
        //fnBaseFunctions.setHintFnModeTypes(Mode.TEM, ModeType.TM);
        fnModeFunctions.setFirstBoxSpace(BoxSpaceFactory.shortCircuit(2.2, 1.59 * 1E-3));
        fnModeFunctions.setSecondBoxSpace(BoxSpaceFactory.matchedLoad(1));
        fnModeFunctions.setFrequency(f);
//        fnBaseFunctions.setProjectType(ProjectType.PLANAR_STRUCTURE);
        fnModeFunctions.setSources(null);
        fnModeFunctions.setHintAxisType(HintAxisType.XY_SEPARATED);
        fnModeFunctions.setSources(new CutOffModalSources(1));
//        fnModeFunctions.a();
//        ModeInfo ii = new ModeInfo(ModeType.TM, 2, 1);
//        fnModeFunctions.fillIndex(ii, 0);
//        System.out.println(DefaultBoxModeFunctions.descMode(ii));
//        ModeInfo fte10 = fnBaseFunctions.get(new ModeInfo(Mode.TE, 1, 0));
//        for (ModeInfo m : fnModeFunctions.getModes()) {
//            System.out.println(DefaultBoxModeFunctions.descMode(m));
//            //System.out.println("fx=" + fte10.fn.fx);
//        }
        WallBorders b = fnModeFunctions.getBorders();
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
                gfps[q][n] = Maths.scalarProduct(true, indexes[n].fn, indexes[q].fn);
            }
        }
        Plot.title("<fn,fn>").asMatrix().plot(matrix(gfps));//.display();

    }

    public static class Config {
        public static void setModeIndexCacheCount(int count) {
            ModeIndex.setCachedModeIndexSize(count);
        }

        public static void setModeIndexCacheEnabled(boolean enabled) {
            ModeIndex.setCachedEnabled(enabled);
        }
    }

}
