package net.vpc.scholar.hadruwaves;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadrumaths.util.EnhancedComputationMonitor;
import net.vpc.scholar.hadrumaths.util.VoidMonitoredAction;
import net.vpc.scholar.hadruwaves.mom.BoxSpace;

import java.util.ArrayList;

import static net.vpc.scholar.hadrumaths.Maths.*;

/**
 * Created by vpc on 5/20/17.
 */
public abstract class BoxModes {

    protected Axis axis;
    protected double ma;
    protected double mb;
    protected double na;
    protected double nb;
    protected ModeType[] allowedModes;
    protected WallBorders borders;
    protected Domain domain;

    public BoxModes(WallBorders borders, Domain domain, Axis axis) {
        this.borders = borders;
        this.domain = domain;
        this.axis = axis;
    }

    public Domain getDomain() {
        return domain;
    }

    public WallBorders getBorders() {
        return borders;
    }


    public ModeType[] getAllowedModes() {
        return allowedModes;
    }

    public Complex getAdmittance(ModeIndex i, double freq, BoxSpace space) {
        double cachedOmega = Physics.omega(freq);
        Complex gamma = getGamma(i, freq, space);
        Complex y = Complex.ZERO;
        switch (i.getModeType()) {
            case TM: {
                switch (space.getLimit()) {
                    case OPEN: {
                        y = I(cachedOmega).mul(EPS0 * space.getEpsr()).div(cotanh(gamma.mul(space.getWidth()))).div(gamma);
                        break;
                    }
                    case MATCHED_LOAD: {
                        // i omega
                        //------------ * cotanh( gamma thikness)
                        // eps gamma
                        y = I(cachedOmega).mul(EPS0 * space.getEpsr()).div(gamma);
                        break;
                    }
                    case SHORT: {
                        // i omega   cotanh( gamma thikness)
                        //-------- * -------------------------
                        // eps            gamma
                        y = I.mul(cachedOmega).mul(EPS0 * space.getEpsr()).mul(cotanh(gamma.mul(space.getWidth()))).div(gamma);
                        break;
                    }
                    case NOTHING: {
                        //nothing;
                        break;
                    }
                }
                return y;
            }
            case TE: {

                switch (space.getLimit()) {
                    case OPEN: {
                        y = gamma.div(cotanh(gamma.mul(space.getWidth())).mul(I.mul(cachedOmega).mul(U0)));
                        break;
                    }
                    //         gamma
                    //    --------------
                    //      i omega U0
                    case MATCHED_LOAD: {
                        y = gamma.div(Complex.I.mul(cachedOmega).mul(U0));
                        break;
                    }
                    case SHORT: {
                        //         gamma
                        //    ------------- * cotanh( gamma thikness)
                        //      i omega U0
                        y = gamma.mul(cotanh(gamma.mul(space.getWidth()))).div(I.mul(cachedOmega).mul(U0));
                        break;
                    }
                    case NOTHING: {
                        //nothing;
                        break;
                    }
                }
                return y;

            }
            case TEM: {
                return I(U0).div(EPS0);
            }
            default: {
                throw new IllegalArgumentException("Unknown Mode " + i.getModeType());
            }
        }
    }


    public ModeFct[] getModeFcts(int max) {
        return getModeFcts(max, null);
    }

    public ModeFct[] getModeFcts(int max, ComputationMonitor monitor) {
        Chronometer chrono = new Chronometer();
        chrono.start();
        ArrayList<ModeFct> next = new ArrayList<ModeFct>(max);
        ModeIterator iterator = iterator();
        EnhancedComputationMonitor mon = ComputationMonitorFactory.createIncrementalMonitor(monitor, max);
        String tostr = borders + " modes, enumerate modes";
        String message = tostr + " {0,number,#}/{1,number,#}";
        Maths.invokeMonitoredAction(
                mon, tostr,
                new VoidMonitoredAction() {
                    @Override
                    public void invoke(EnhancedComputationMonitor monitor, String messagePrefix) throws Exception {
                        int index = 0;
                        int bruteMax = max * 100 + 1;
                        int bruteIndex = 0;
                        while (index < max && bruteIndex < bruteMax) {
                            ModeIndex modeIndex = iterator.next();
                            if (accept(modeIndex)) {
                                ModeFct modeInfo = new ModeFct(modeIndex, getFunction(modeIndex));
                                next.add(modeInfo);
                                index++;
                            }
                            bruteIndex++;
                            mon.inc(message, index, max);
                        }
                    }
                }
        );

        //System.out.println("found " + next.size()+" modes in "+chrono);
        return next.toArray(new ModeFct[next.size()]);
    }

    public ModeIndex[] indexes(int max) {
        return getIndexes(max);
    }
    public DoubleToVector[] functions(int max) {
        return getFunctions(max);
    }

    public DoubleToVector[] getFunctions(int max) {
        ModeIndex[] indexes = getIndexes(max);
        DoubleToVector[] fct = new DoubleToVector[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            fct[i]=getFunction(indexes[i]);
        }
        return fct;
    }

    public ModeIndex[] getIndexes(int max) {
        return getIndexes(max, null);
    }

    public ModeIndex[] indexes(int max, ComputationMonitor monitor) {
        return getIndexes(max, monitor);
    }

    public ModeIndex[] getIndexes(int max, ComputationMonitor monitor) {
        Chronometer chrono = new Chronometer();
        chrono.start();
        ArrayList<ModeIndex> next = new ArrayList<ModeIndex>(max);
        ModeIterator iterator = iterator();
        EnhancedComputationMonitor mon = ComputationMonitorFactory.createIncrementalMonitor(monitor, max);
        String tostr = borders + " modes, enumerate modes";
        String message = tostr + ", {0,number,#}/{1,number,#}";
        Maths.invokeMonitoredAction(
                mon, tostr,
                new VoidMonitoredAction() {
                    @Override
                    public void invoke(EnhancedComputationMonitor monitor, String messagePrefix) throws Exception {
                        int index = 0;
                        int bruteMax = max * 100 + 1;
                        int bruteIndex = 0;
                        while (index < max && bruteIndex < bruteMax) {
                            ModeIndex modeIndex = iterator.next();
                            if (accept(modeIndex)) {
                                next.add(modeIndex);
                                index++;
                            }
                            bruteIndex++;
                            mon.inc(message, index, max);
                        }
                    }
                });
        //System.out.println("found " + next.size()+" modes in "+chrono);
        return next.toArray(new ModeIndex[next.size()]);
    }


    public Complex impedance(ModeIndex i, double freq, BoxSpace space) {
        return getAdmittance(i, freq, space).inv();
    }

    public Complex admittance(ModeIndex i, double freq, BoxSpace space) {
        return getAdmittance(i, freq, space);
    }

    public Complex getImpedance(ModeIndex i, double freq, BoxSpace space) {
        return admittance(i, freq, space).inv();
    }

    public DoubleToVector function(ModeIndex i) {
        return getFunction(i);
    }

    public Complex gamma(ModeIndex i, double freq, BoxSpace space) {
        return getGamma(i, freq, space);
    }

    public double cutoffFrequency(ModeIndex i) {
        return getCutoffFrequency(i);
    }


    public abstract DoubleToVector getFunction(ModeIndex i);

    public abstract Complex getGamma(ModeIndex i, double freq, BoxSpace space);

    public abstract double getCutoffFrequency(ModeIndex i);

    public abstract boolean accept(ModeIndex i);

    public abstract ModeIterator iterator();
}
