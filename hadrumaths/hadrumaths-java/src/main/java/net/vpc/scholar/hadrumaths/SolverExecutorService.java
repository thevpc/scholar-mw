package net.vpc.scholar.hadrumaths;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by vpc on 12/12/16.
 */
public class SolverExecutorService implements Closeable {
    //    public static void main(String[] args) {
//        try {
//            MathsBase.solverExecutorService(1)
//                    .submit(new Runnable() {
//                        @Override
//                        public void run() {
//                            System.out.println("task 1");
//                            try {
//                                Thread.sleep(3000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            System.out.println("Okkay");
//                        }
//                    })
//                    .submit(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                System.out.println("task 2");
//                                Thread.sleep(2000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            throw new RuntimeException("Error some where");
//                        }
//                    })
//                    .submit(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                System.out.println("task 3");
//                                Thread.sleep(2000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    })
//            .solveAll()
//            .close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        ;
//    }
    private CompletionService ecs;
    private ExecutorService executor;
    List<Future> futures = new ArrayList<Future>();

    public SolverExecutorService(int threadsCount) {
        this(Executors.newFixedThreadPool(threadsCount));
    }

    public SolverExecutorService(ExecutorService e) {
        this.executor = e;
        ecs = new ExecutorCompletionService(e);
    }

    public SolverExecutorService submit(Collection<Callable> solvers) {
        for (Callable s : solvers) {
            submit(s);
        }
        return this;
    }

    public SolverExecutorService submit(final Callable s) {
        futures.add(ecs.submit(s));
        return this;
    }

    public SolverExecutorService submit(final Runnable s) {
        futures.add(ecs.submit(s, ""));
        return this;
    }

    public SolverExecutorService solveAll()
            throws InterruptedException, ExecutionException {
        int n = futures.size();
        for (int i = 0; i < n; ++i) {
            Object r = ecs.take().get();
            if (r != null) {
                //use(r);
            }
        }
        return this;
    }

    public SolverExecutorService cancel() throws IOException {
        for (Future f : futures) {
            f.cancel(true);
        }
        return this;
    }

    @Override
    public void close() throws IOException {
        executor.shutdown();
        try {
            executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public SolverExecutorService solveFirst() throws InterruptedException {
        int n = futures.size();
        Object result = null;
        try {
            for (int i = 0; i < n; ++i) {
                try {
                    Object r = ecs.take().get();
                    if (r != null) {
                        result = r;
                        break;
                    }
                } catch (ExecutionException ignore) {
                }
            }
        } finally {
            for (Future f : futures)
                f.cancel(true);
        }

        if (result != null) {
            //use(result);
        }
        return this;
    }
}
