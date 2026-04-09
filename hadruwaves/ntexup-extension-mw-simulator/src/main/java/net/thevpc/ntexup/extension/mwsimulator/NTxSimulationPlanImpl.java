package net.thevpc.ntexup.extension.mwsimulator;

import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.nuts.log.NLogger;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.time.NChronometer;
import net.thevpc.nuts.time.NChronometerView;
import net.thevpc.nuts.util.NAssert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class NTxSimulationPlanImpl implements NTxSimulationPlan {

    public String hash;
    public String id;
    public String name;
    public NTxRendererContext rendererContext;
    public List<NTxSolverRun> items = new ArrayList<>();
    public List<NTxSolverListener> listeners = new ArrayList<>();
    public NChronometer chronometer = NChronometer.ofUnstarted();
    public NChronometerView nChronometerView = chronometer.asReadOnly();
    public volatile boolean compiled;

    public NTxSimulationPlanImpl(String id, String name,NTxRendererContext rendererContext) {
        this.id = id;
        this.name = name;
        this.rendererContext = rendererContext;
        addSolverListener(new NTxSolverListener() {
            @Override
            public void onStart(NTxRendererContext rendererContext, NTxSimulationPlan plan) {
                chronometer.start();
            }

            @Override
            public void onFinish(NTxRendererContext rendererContext, NTxSimulationPlan plan, boolean error, Throwable throwable) {
                chronometer.stop();
            }
        });

    }

    public NChronometer chronometer() {
        return chronometer;
    }

    public NChronometerView chronometerView() {
        return nChronometerView;
    }
    public String name() {
        return name;
    }

    public void addSolverListener(NTxSolverListener listener) {
        if (listener != null) {
            this.listeners.add(listener);
        }
    }

    @Override
    public List<NTxSolverListener> solverListeners() {
        return Collections.unmodifiableList(new ArrayList<>(listeners));
    }

    public NTxRendererContext rendererContext() {
        return rendererContext;
    }

    public NLogger log() {
        return rendererContext.log();
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
                    if (id == null) {
                        id = "Query";
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
    public String id() {
        return id;
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
