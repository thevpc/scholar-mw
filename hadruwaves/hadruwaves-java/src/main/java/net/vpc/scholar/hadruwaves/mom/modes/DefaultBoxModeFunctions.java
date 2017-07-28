package net.vpc.scholar.hadruwaves.mom.modes;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadrumaths.util.EnhancedComputationMonitor;
import net.vpc.scholar.hadrumaths.util.VoidMonitoredAction;
import net.vpc.scholar.hadruwaves.*;
import net.vpc.scholar.hadruwaves.mom.BoxSpace;
import net.vpc.scholar.hadruwaves.mom.ModeFunctions;

import java.util.ArrayList;

/**
 * North Eletrique, East Magnetique, South Eletrique,W Magnetique
 */
public class DefaultBoxModeFunctions extends ModeFunctionsBase {

    private Axis axis;
    private double alphax;
    private double betay;
    //    private double ma = 1;
//    private double mb = 0;
//    private double na = 1;
//    private double nb = 0;
//    private ModeType[] allowedModes;
    private BoxModes modesDesc;

    public DefaultBoxModeFunctions(String pattern) {
        this(WallBorders.valueOf(pattern));
    }

    public DefaultBoxModeFunctions(WallBorders b) {
        this(b.north, b.east, b.south, b.west);
    }

    public DefaultBoxModeFunctions(Wall north, Wall east, Wall south, Wall west) {
        this(north, east, south, west, null, 0, 0);
    }

    public DefaultBoxModeFunctions(Wall north, Wall east, Wall south, Wall west, Axis polarization) {
        this(north, east, south, west, polarization, 0, 0);
    }

    public DefaultBoxModeFunctions(Wall north, Wall east, Wall south, Wall west, Axis polarization, double phasex, double phasey) {
        super(
                north.equals(Wall.PERIODIC)
                        || east.equals(Wall.PERIODIC)
                        || south.equals(Wall.PERIODIC)
                        || west.equals(Wall.PERIODIC),
                WallBorders.valueOf(north, east, south, west));
        this.axis = polarization;
        this.alphax = phasex;
        this.betay = phasey;
        WallBorders bord = getBorders();
        if (bord.toString().toLowerCase().indexOf('p') < 0) {
            if (axis != null || phasex != 0 || phasey != 0) {
                throw new IllegalArgumentException(toString() + " : No periodic with polarization.");
            }
        } else {
            if (axis == null) {
                axis = Axis.X;
            }
        }
    }

    @Override
    public ModeFunctions setDomain(Domain domain) {
        ModeFunctions d = super.setDomain(domain);
        modesDesc = Physics.boxModes(getBorders(), getDomain(), this.alphax, this.betay, axis);
        return d;
    }


//    private static String descMode(ModeInfo m) {
//        return m.mode.type + "(" + m.mode.m + "," + m.mode.n + ") "
//                + "; fc = " + (m.cutOffFrequency / 1E9) + "GHz "
//                + "; |f| = " + Maths.scalarProduct(m.fn, m.fn) + " "
//                + "; zmn=" + m.impedance + " "
//                + "; gmn_down=" + m.firstBoxSpaceGamma + " "
//                + "; gmn_up=" + m.secondBoxSpaceGamma + " "
//                + "; fx=" + m.fn.getComponent(Axis.X) + " "
//                + "; fy=" + m.fn.getComponent(Axis.Y) + " ";
//    }

//    public ModeInfo[] getIndexesImpl_old(ComputationMonitor par0) {
//        int max = getFnMax();
//        System.out.println("lookup for " + max + " fn modes for " + this);
//        WallBorders bord = getBorders();
//        Chronometer chrono = new Chronometer();
//        chrono.start();
//        ArrayList<ModeInfo> next = new ArrayList<ModeInfo>();
//        switch (bord) {
//            case PPPP: {
//                for (int n = 0; n < max && next.size() < max; n++) {
//                    ModeType[] modes = axis == Axis.X ? new ModeType[]{ModeType.TM, ModeType.TE} : new ModeType[]{ModeType.TE, ModeType.TM};// TE prioritaire
//
//                    for (int h = 0; h < 2; h++) {
//                        for (int i = 0; i < n; i++) {
//                            for (int s1 = -1; s1 <= 1; s1 += 2) {
//                                if (i == 0 && s1 == -1) {
//                                    continue;
//                                }
//                                for (int s2 = -1; s2 <= 1; s2 += 2) {
//                                    if (n == 0 && s2 == -1) {
//                                        continue;
//                                    }
//                                    for (int k = 0; k < modes.length && next.size() < max; k++) {
//                                        ModeInfo o = h == 0 ? new ModeInfo(modes[k], s1 * i, n * s2, next.size())
//                                                : new ModeInfo(modes[k], n * s2, s1 * i, next.size());
//                                        if (modes[k] == ModeType.TM) {
//                                            fillIndex(o, next.size());
//                                            next.add(o);
//                                        } else if (modes[k] == ModeType.TE) {
//                                            fillIndex(o, next.size());
//                                            next.add(o);
//                                        } else {
//                                            throw new RuntimeException("impossible");
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    for (int s1 = -1; s1 <= 1; s1 += 2) {
//                        if (n == 0 && s1 == -1) {
//                            continue;
//                        }
//                        for (int s2 = -1; s2 <= 1; s2 += 2) {
//                            if (n == 0 && s2 == -1) {
//                                continue;
//                            }
//                            for (int k = 0; k < modes.length && next.size() < max; k++) {
//                                ModeInfo o = new ModeInfo(modes[k], s1 * n, s2 * n, next.size());
//
//                                if (modes[k] == ModeType.TM) {
//                                    fillIndex(o, next.size());
//                                    next.add(o);
//                                } else if (modes[k] == ModeType.TE) {
//                                    fillIndex(o, next.size());
//                                    next.add(o);
//                                } else {
//                                    throw new RuntimeException("impossible");
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            default: {
//                String message = toString() + ", enumerate modes";
//                EnhancedComputationMonitor mon = ComputationMonitorFactory.createIncrementalMonitor(par0, max);
//                List<ModeType> theSystemAllowed = new ArrayList<ModeType>(Arrays.asList(modesDesc.getAllowedModes()));
//                ModeType[] hintFnModes = getHintFnModes();
//                if ((hintFnModes != null && hintFnModes.length > 0)) {
//                    theSystemAllowed.retainAll(new TreeSet<ModeType>(Arrays.asList(hintFnModes)));
//                }
//                ModeType[] goodModes = theSystemAllowed.toArray(new ModeType[theSystemAllowed.size()]);
//                Maths.invokeMonitoredAction(mon, message, new VoidMonitoredAction() {
//                    @Override
//                    public void invoke(EnhancedComputationMonitor monitor, String messagePrefix) throws Exception {
//                        int count = 0;
//                        out:
//                        for (int i = 0; ; i++) {
//                            Collection<ModeInfo> modeInfos = nextModeInfos(i, goodModes, true);
//                            for (ModeInfo modeInfo : modeInfos) {
//                                next.add(modeInfo);
//                                //if((next.size()%100)==0){
//                                //    System.out.println("... = " + next.size());
//                                //}
//                                count++;
//                                monitor.inc(message);
//                                if (count >= max) {
//                                    break out;
//                                }
//                            }
//                        }
//                    }
//                });
//            }
//        }
//        System.out.println("found " + next.size() + " modes in " + chrono);
//        return next.toArray(new ModeInfo[next.size()]);
//    }

    @Override
    public ModeInfo[] getIndexesImpl(ComputationMonitor par0) {
        int max = getSize();
        //System.out.println("lookup for "+max+" fn modes for "+this);
        Chronometer chrono = new Chronometer();
        chrono.start();
        ArrayList<ModeInfo> next = new ArrayList<ModeInfo>(max);
        ModeIterator iterator = getModeIteratorFactory().iterator(this);
        EnhancedComputationMonitor monitor = ComputationMonitorFactory.createIncrementalMonitor(par0, max);
        String str = toString() + ", enumerate modes";
        String message = str + " {0,number,#}/{1,number,#}";
        Maths.invokeMonitoredAction(monitor, str, new VoidMonitoredAction() {
            @Override
            public void invoke(EnhancedComputationMonitor monitor, String messagePrefix) throws Exception {
                int index = 0;
                int bruteMax = max * 100 + 1;
                int bruteIndex = 0;
                while (index < max && bruteIndex < bruteMax) {
                    ModeIndex modeIndex = iterator.next();
                    if (modeIndex == null) {
                        break;
                    }
                    if (doAcceptModeIndex(modeIndex)) {
                        ModeInfo modeInfo = new ModeInfo(modeIndex);
                        fillIndex(modeInfo, 0);
                        if (doAcceptModeInfo(modeInfo)) {
                            next.add(modeInfo);
                            index++;
                        }
                    }
                    bruteIndex++;
                    monitor.inc(message, index, max);

                }
            }
        });
        //System.out.println("found " + next.size()+" modes in "+chrono);
        return next.toArray(new ModeInfo[next.size()]);
    }

    protected boolean doAcceptModeIndex(ModeIndex o) {
        if (!super.doAcceptModeIndex(o)) {
            return false;
        }
        return modesDesc.accept(o);
    }

    public double getCutoffFrequency(ModeIndex i) {
        return modesDesc.getCutoffFrequency(i);
    }

    @Override
    public Complex getGammaImpl(ModeIndex i, BoxSpace bs) {
        return modesDesc.getGamma(i, getFrequency(), bs);
    }

    @Override
    public DoubleToVector getFnImpl(ModeIndex i) {
        return modesDesc.getFunction(i);
    }

    @Override
    public String toString() {
        String p = getBorders().toString();
        String sfx = (p.indexOf('p') >= 0) ? ("[Polarization" + axis + "]" + (alphax != 0 ? ("[alphax=" + String.valueOf(alphax) + "]") : "") + (betay != 0 ? ("[alphax=" + String.valueOf(betay) + "]") : "")) : "";
        return "BoxModes[" + p + "]" + sfx;
    }

    public ModeType[] getAllowedModes() {
        return modesDesc.getAllowedModes();
    }

    public Axis getAxis() {
        return axis;
    }

    public double getAlphax() {
        return alphax;
    }

    public double getBetay() {
        return betay;
    }
}
