package net.thevpc.ntexup.extension.mwsimulator;

public class NTxStrSimulationQuery {
    public String hash;
    public NTxSweep sweep;
    public NTxMwResultType compute;
    public String computeName;
    public NTxTargetSolver computer = null;
    public NTxTargetConfigurer applier = null;
}
