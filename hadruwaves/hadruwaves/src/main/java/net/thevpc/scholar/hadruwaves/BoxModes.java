package net.thevpc.scholar.hadruwaves;

import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.common.mon.VoidMonitoredAction;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.mom.BoxSpace;

import java.util.ArrayList;
import java.util.Iterator;
import net.thevpc.common.time.Chronometer;

import static net.thevpc.scholar.hadrumaths.Maths.*;

/**
 * Created by vpc on 5/20/17.
 */
public abstract class BoxModes implements Iterable<ModeIndex> {

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
        Complex y = Maths.CZERO;
        switch (i.getModeType()) {
            case TM: {
                switch (space.getLimit()) {
                    case INFINITE: {
                        //   i omega eps
                        //  -------------
                        //      gamma
                        y = I(cachedOmega).mul(space.getEpsrc(freq)).div(gamma);
                        break;
                    }
                    case ELECTRIC: {
                        //   i omega eps            
                        //  -------------  cotanh(gamma thikness)
                        //      gamma                
                        y = I.mul(cachedOmega).mul(space.getEpsrc(freq)).mul(cotanh(gamma.mul(space.getWidth()))).div(gamma);
                        break;
                    }
                    case OPEN: {
                        //  i omega eps          1                 
                        // ------------- ------------------------
                        //      gamma     cotanh(gamma thikness)
                        y = I(cachedOmega).mul(space.getEpsrc(freq)).div(cotanh(gamma.mul(space.getWidth()))).div(gamma);
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
                    //         gamma
                    //    -------------- 
                    //      i omega U0
                    case INFINITE: {
                        y = gamma.div(Complex.I.mul(cachedOmega).mul(U0));
                        break;
                    }
                    //         gamma
                    //    ------------- * cotanh( gamma thikness)
                    //      i omega U0
                    //short
                    case ELECTRIC: {
                        y = gamma.mul(cotanh(gamma.mul(space.getWidth()))).div(I.mul(cachedOmega).mul(U0));
                        break;
                    }
                    //         gamma                 1
                    //    ------------- * -------------------------
                    //      i omega U0     cotanh( gamma thikness)
                    case OPEN: {
                        y = gamma.div(cotanh(gamma.mul(space.getWidth())).mul(I.mul(cachedOmega).mul(U0)));
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

    public ModeFct[] getModeFcts(final int max, ProgressMonitor monitor) {
        Chronometer chrono = Chronometer.start();
        final ArrayList<ModeFct> next = new ArrayList<ModeFct>(max);
        final Iterator<ModeIndex> iterator = iterator();
        final ProgressMonitor mon = ProgressMonitors.incremental(monitor, max);
        String tostr = borders + " modes, enumerate modes";
        final String message = tostr + " {0,number,#}/{1,number,#}";
        Maths.invokeMonitoredAction(
                mon, tostr,
                new VoidMonitoredAction() {
            @Override
            public void invoke(ProgressMonitor monitor, String messagePrefix) throws Exception {
                int index = 0;
                int bruteMax = max * 100 + 1;
                int bruteIndex = 0;
                while (index < max && bruteIndex < bruteMax) {
                    if (iterator.hasNext()) {
                        ModeIndex modeIndex = iterator.next();
                        if (accept(modeIndex)) {
                            ModeFct modeInfo = new ModeFct(modeIndex, getFunction(modeIndex));
                            next.add(modeInfo);
                            index++;
                        }
                        bruteIndex++;
                        mon.inc(message, index, max);
                    } else {
                        break;
                    }
                }
            }
        }
        );

        //System.out.println("found " + next.size()+" modes in "+chrono);
        return next.toArray(new ModeFct[0]);
    }

    public ModeIndex[] indexes(int max) {
        return getIndexes(max);
    }

    public DoubleToVector[] functions(int max) {
        return getFunctions(max);
    }

    public Complex[] impedances(int max, double freq, BoxSpace space) {
        Complex[] z = new Complex[max];
        ModeIndex[] indexes = getIndexes(max);
        for (int i = 0; i < indexes.length; i++) {
            z[i] = getImpedance(indexes[i], freq, space);
        }
        return z;
    }

    public Complex[] admittances(int max, double freq, BoxSpace space) {
        Complex[] z = new Complex[max];
        ModeIndex[] indexes = getIndexes(max);
        for (int i = 0; i < indexes.length; i++) {
            z[i] = getAdmittance(indexes[i], freq, space);
        }
        return z;
    }

    public DoubleToVector[] getFunctions(int max) {
        ModeIndex[] indexes = getIndexes(max);
        DoubleToVector[] fct = new DoubleToVector[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            fct[i] = getFunction(indexes[i]);
        }
        return fct;
    }

    public ModeIndex[] getIndexes(int max) {
        return getIndexes(max, null);
    }

    public ModeIndex[] indexes(int max, ProgressMonitor monitor) {
        return getIndexes(max, monitor);
    }

    public ModeIndex[] getIndexes(final int max, ProgressMonitor monitor) {
        Chronometer chrono = Chronometer.start();
        final ArrayList<ModeIndex> next = new ArrayList<ModeIndex>(max);
        final Iterator<ModeIndex> iterator = iterator();
        final ProgressMonitor mon = ProgressMonitors.incremental(monitor, max);
        String tostr = borders + " modes, enumerate modes";
        final String message = tostr + ", {0,number,#}/{1,number,#}";
        Maths.invokeMonitoredAction(
                mon, tostr,
                new VoidMonitoredAction() {
            @Override
            public void invoke(ProgressMonitor monitor, String messagePrefix) throws Exception {
                int index = 0;
                int bruteMax = max * 100 + 1;
                int bruteIndex = 0;
                while (index < max && bruteIndex < bruteMax) {
                    if (iterator.hasNext()) {
                        ModeIndex modeIndex = iterator.next();
                        if (accept(modeIndex)) {
                            next.add(modeIndex);
                            index++;
                        }
                        bruteIndex++;
                        mon.inc(message, index, max);
                    } else {
                        break;
                    }
                }
            }
        });
        //System.out.println("found " + next.size()+" modes in "+chrono);
        return next.toArray(new ModeIndex[0]);
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

    public abstract Iterator<ModeIndex> iterator();
}
