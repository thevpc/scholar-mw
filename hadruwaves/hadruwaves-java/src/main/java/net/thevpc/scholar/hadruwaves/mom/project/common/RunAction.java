package net.thevpc.scholar.hadruwaves.mom.project.common;

import net.thevpc.common.mon.AbstractProgressMonitor;

/**
 * Created by IntelliJ IDEA. User: taha Date: 3 juil. 2004 Time: 15:15:13 To
 * change this template use File | Settings | File Templates.
 */
public abstract class RunAction extends AbstractProgressMonitor {

    public RunAction() {
        super(nextId());
    }

    protected abstract Object run();

    public Object go() {
        setProgress(0);
        try {
            return run();
        } finally {
            setProgress(1);
        }
    }

//    public long getRemainingTimeEstimation() {
//        double _progress = getProgressValue();
//        if (_progress == 0) {
//            return -1;
//        }
//        return (long) (getDuration() * (1 - _progress) / _progress);
//    }

}
