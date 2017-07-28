package net.vpc.scholar.hadrumaths.convergence;

import java.io.PrintStream;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime Oct 25, 2007 11:52:55 PM
 */
public class ConvergenceLogListener implements ConvergenceListener{
    private PrintStream out;
    public ConvergenceLogListener(PrintStream out) {
        this.out = out;
    }

    @Override
    public void progress(ConvergenceResult result) {
        if (out!=null) {
            out.println(result);
        }
    }
}
