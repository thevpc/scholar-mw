package net.thevpc.ntexup.extension.mwsimulator;

public class NTxSweep {
    public Number rangeFrom;
    public Number rangeTo;
    public Number step;
    public Integer times;
    public NTxSweepTarget target;

    public NTxSweep() {
        target = NTxSweepTarget.FREQ;
        rangeFrom = 0;
        rangeTo = 1;
        times = 1;
    }
}
