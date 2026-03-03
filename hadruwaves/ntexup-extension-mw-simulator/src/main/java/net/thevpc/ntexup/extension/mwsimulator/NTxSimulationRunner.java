package net.thevpc.ntexup.extension.mwsimulator;

import net.thevpc.nuts.concurrent.NCachedValue;
import net.thevpc.nuts.concurrent.NCallable;
import net.thevpc.nuts.time.NDuration;
import net.thevpc.nuts.util.NExceptions;
import net.thevpc.nuts.util.NStringUtils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.Function;

public class NTxSimulationRunner {
    private ExecutorService executorService;
    private Map<String, NCachedValue<NTxSimulationRunningProcess>> cachedResults = new ConcurrentHashMap<>();

    public NTxSimulationRunner() {
        executorService = Executors.newFixedThreadPool(3);
    }


    public <T> NTxSimulationRunningProcess add(NCallable<T> def, NTxStrSimulationQuery query) {
        NCachedValue<NTxSimulationRunningProcess> cv = cachedResults.computeIfAbsent(query.hash, t -> NCachedValue.of(() ->
                {
                    synchronized (NTxSimulationRunner.this) {
                        String taskId = UUID.randomUUID().toString();
                        Future<T> f = executorService.submit(def);
                        return new MyNTxSimulationRunningProcess<T>(taskId, NStringUtils.firstNonBlank(
                                query.compute==null?null:query.compute.name(),
                                query.computeName,
                                "Result"
                        ), f);
                    }
                }
        )).setExpiry(NDuration.ofHours(1));
        return cv.get();
    }

    private static class MyNTxSimulationRunningProcess<T> implements NTxSimulationRunningProcess {
        private final String taskId;
        private final String name;
        private final Future<T> f;
        private final Map<String, Object> vars = new ConcurrentHashMap<>();

        public MyNTxSimulationRunningProcess(String taskId, String name, Future<T> f) {
            this.taskId = taskId;
            this.name = name;
            this.f = f;
        }

        @Override
        public <T> void putVar(String key, T value) {
            if (value == null) {
                vars.remove(key);
            } else {
                vars.put(key, value);
            }
        }

        @Override
        public <T> T getVar(String key) {
            return (T) vars.get(key);
        }

        @Override
        public <T> T computeVar(String key, Function<String, T> supplier) {
            if (vars.containsKey(key)) {
                return (T) vars.get(key);
            }
            T n = supplier.apply(name);
            if (n != null) {
                vars.put(key, n);
            }
            return n;
        }

        @Override
        public String id() {
            return taskId;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public boolean isDone() {
            return f.isDone();
        }

        @Override
        public boolean isRunning() {
            return !f.isDone() && !f.isCancelled();
        }

        @Override
        public String getError() {
            if (f.isDone()) {
                try {
                    T t = f.get();
                    return null;
                } catch (Exception ex) {
                    return NExceptions.getErrorMessage(ex);
                }
            }
            return null;
        }

        @Override
        public Object getResult() {
            try {
                return f.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
