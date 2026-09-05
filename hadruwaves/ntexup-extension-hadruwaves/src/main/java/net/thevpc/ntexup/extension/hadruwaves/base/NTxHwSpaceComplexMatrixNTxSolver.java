package net.thevpc.ntexup.extension.hadruwaves.base;

import net.thevpc.ntexup.extension.hadruwaves.MoMStrNTxSimulationPlan;
import net.thevpc.ntexup.extension.mwsimulator.*;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

import java.util.Collections;
import java.util.List;

public abstract class NTxHwSpaceComplexMatrixNTxSolver extends NTxHwNTxSolver {
    protected NTxSweep xSweep;
    protected NTxSweep ySweep;
    protected Axis axis = Axis.X;

    public NTxHwSpaceComplexMatrixNTxSolver(String computeName, String solverName, String solverType, MoMStrNTxSimulationPlan moMStrSimulationQuery) {
        super(moMStrSimulationQuery,computeName, solverName, solverType);
    }


    @Override
    public NElement toElement() {
        net.thevpc.nuts.elem.NObjectElementBuilder b = net.thevpc.nuts.elem.NElement.ofObjectBuilder()
                .name(outputName())
                .set("solver", solverType());
        if (xSweep != null) {
            b.set("x-sweep", xSweep.toElement());
        }
        if (ySweep != null) {
            b.set("y-sweep", ySweep.toElement());
        }
        if (axis != null) {
            b.add(NElement.ofPair("axis", axis.name()));
        }
        return b.build();
    }

    @Override
    protected void compileImpl() {
        if (xSweep == null && ySweep == null) {
            Domain d = ((MoMStrNTxSimulationPlan) plan()).str.domain().getDomain();
            xSweep = new NTxSweep();
            xSweep.rangeFrom = d.xmin();
            xSweep.rangeTo = d.xmax();
            xSweep.count = 200;

            ySweep = new NTxSweep();
            ySweep.rangeFrom = d.ymin();
            ySweep.rangeTo = d.ymax();
            ySweep.count = 200;
        } else if (xSweep == null) {
            xSweep = ySweep;
        } else if (ySweep == null) {
            ySweep = xSweep;
        }
        if (axis == null) {
            axis = Axis.X;
        }
    }

    @Override
    public List<NTxSimulationResult> execute() {
        MomStructure str = momStructure();
        NTxChronometer chronometer = NTxChronometer.of();
        log(NMsg.ofC("------------------"));
        log(NMsg.ofC("[%s] %s: ", outputName(), solverName()));
        log(NMsg.ofC("------------------"));
        ComplexMatrix matrix = matrix(str);
        for (int r = 0; r < matrix.getRowCount(); r++) {
            for (int c = 0; c < matrix.getColumnCount(); c++) {
                log(NMsg.ofC(" %s-%s[%s,%s]=%s", solverName(), outputName(), c, r, matrix.get(r, c)));
            }
        }
        log(NMsg.ofC("[%s] %s Finished in %s : ", outputName(), solverName(),chronometer.stop()));
        if (plan().rendererContext().isAnimate()) {
            Plot.cd(fullPath()).title(fullName()).plot(matrix);
        }
        return Collections.singletonList(
                NTxSimulationResultFactory.createPlot2dCurve(outputName(), null, Collections.singletonList(0.0))
        );
    }

    @Override
    public void addImpl(String paramName, NElement paramValue) {
        switch (paramName) {
            case "sweep": {
                if (xSweep == null) {
                    xSweep = NTxSweep.parse(paramValue).orNull();
                } else if (ySweep == null) {
                    ySweep = NTxSweep.parse(paramValue).orNull();
                }
                break;
            }
            case "x":
            case "xsweep":
            case "x-sweep":
            case "sweep-x":
            case "sweepx": {
                xSweep = NTxSweep.parse(paramValue).orNull();
                break;
            }
            case "y":
            case "ysweep":
            case "y-sweep":
            case "sweep-y":
            case "sweepy": {
                ySweep = NTxSweep.parse(paramValue).orNull();
                break;
            }
            case "axis": {
                axis = Axis.parse(paramValue).orNull();
                break;
            }
        }
    }

    protected abstract ComplexMatrix matrix(MomStructure str);

}
