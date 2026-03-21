package net.thevpc.ntexup.extension.mwsimulator;

import net.thevpc.nuts.log.NLogger;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.util.NAssert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class NTxSimulationPlanImpl implements NTxSimulationPlan {
    public String hash;
    public String name;
    public NLogger logger;
    public List<NTxSolverRun> items = new ArrayList<>();
    public volatile boolean compiled;

    public NTxSimulationPlanImpl(String name, NLogger logger) {
        this.name = name;
        this.logger = logger;
    }

    public NLogger log() {
        return logger;
    }

    public NTxSolverRun add(String computeName, String solverName) {
        NAssert.requireNamedFalse(compiled, "compiled");
        NTxSolverRun i = createItem(computeName, solverName);
        if (i != null) {
            items.add(i);
        } else {
            log().log(NMsg.ofC("[%s] Unsupported solver %s. ignored", computeName, solverName));
        }
        return i;
    }

    public abstract NTxSolverRun createItem(String computeName, String solverName);

    public abstract String computeHash();

    public void compile() {
        if (!compiled) {
            synchronized (this) {
                if (!compiled) {
                    if (name == null) {
                        name = "Query";
                    }
                    for (NTxSolverRun item : items) {
                        item.compile();
                    }
                    this.hash = computeHash();
                    compiled = true;
                }
            }
        }
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String hash() {
        compile();
        return hash;
    }

    @Override
    public List<NTxSolverRun> runs() {
        return Collections.unmodifiableList(items);
    }
}
