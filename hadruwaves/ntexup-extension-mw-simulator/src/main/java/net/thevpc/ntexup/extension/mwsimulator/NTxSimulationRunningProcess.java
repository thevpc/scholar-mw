package net.thevpc.ntexup.extension.mwsimulator;

import java.util.function.Function;

public interface NTxSimulationRunningProcess {

    <T> void putVar(String key, T value);

    <T> T getVar(String key);

    <T> T computeVar(String key, Function<String, T> supplier);

    String id();

    String name();

    boolean isDone();

    boolean isRunning();

    String getError();

    Object getResult();
}
