package net.thevpc.ntexup.extension.openems.solvers;

import net.thevpc.ntexup.extension.mwsimulator.NTxStrSimulationQuery;
import net.thevpc.ntexup.extension.mwsimulator.NTxTargetSolver;
import net.thevpc.nuts.command.NExec;
import net.thevpc.nuts.io.NPath;

public class NTxHwS11Solver implements NTxTargetSolver {
    @Override
    public Object solve(NTxStrSimulationQuery query) {
        NPath path = NPath.ofTempFile("openems.xml");
        NExec cmd = NExec.ofSystem("openEMS", "-f", path.toString());
        if (false) {
            cmd.run();
        }
        return 1;
    }
}
