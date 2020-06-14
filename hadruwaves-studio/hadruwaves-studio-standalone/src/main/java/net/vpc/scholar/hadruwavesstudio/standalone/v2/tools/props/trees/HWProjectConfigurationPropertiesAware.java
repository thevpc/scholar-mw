/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.props.trees;

import java.util.ArrayList;
import java.util.List;
import net.vpc.common.props.PMapEntry;
import net.vpc.common.app.Application;
import net.vpc.common.app.swing.core.DefaultPropertiesNodeFolder;
import net.vpc.common.app.swing.core.PValueViewProperty;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;
import net.vpc.common.app.swing.core.PValueViewPropertyReadOnly;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadruwaves.mom.solver.HWSolverTemplateFDM;
import net.vpc.scholar.hadruwaves.mom.solver.HWSolverTemplateFEM;
import net.vpc.scholar.hadruwaves.mom.solver.HWSolverTemplateMoM;
import net.vpc.scholar.hadruwaves.mom.solver.MomSolverTestTemplate;
import net.vpc.scholar.hadruwaves.mom.solver.test.MomSolverTestTemplateList;
import net.vpc.scholar.hadruwaves.mom.solver.test.MomSolverTestTemplateMesh;
import net.vpc.scholar.hadruwaves.mom.solver.test.MomSolverTestTemplateSeq;
import net.vpc.scholar.hadruwaves.mom.solver.test.ParamSeqTemplate;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationFolder;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwaves.props.WritablePExpression;
import net.vpc.scholar.hadruwaves.solvers.HWSolverTemplate;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.util.DefaultHWPropertiesTree;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.util.PValueViewPropertyE;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationElement;

/**
 *
 * @author vpc
 */
public class HWProjectConfigurationPropertiesAware extends DefaultHWPropertiesTree {

    private final HWConfigurationElement wp;
    private final HWProjectItem projectItem;
    private DefaultPropertiesNodeFolder root;

    public HWProjectConfigurationPropertiesAware(HWConfigurationElement wp, final HWProjectItem outer) {
        super(outer);
        this.projectItem = outer;
        this.wp = wp;

        refresh();
    }

    public void refresh() {
        root = new DefaultPropertiesNodeFolder("folder",
                (wp instanceof HWConfigurationFolder)
                        ? "Configuration Folder" : "Configuration Run",
                wp);
        setRoot(root);
        DefaultPropertiesNodeFolder folder = root.addFolder("folder", "General");
        Application app = projectItem.getStudio().app();
        if ("default".equals(wp.name().get())) {
            folder.add(new PValueViewProperty("property", app, wp.name().readOnly()));
        } else {
            folder.add(new PValueViewProperty("property", app, wp.name()));
        }
        folder.add(new PValueViewProperty("property", app, wp.description()));
        if (wp instanceof HWConfigurationRun) {
            HWConfigurationRun r = (HWConfigurationRun) wp;
            folder.add(new PValueViewPropertyReadOnly("property", app, "active", wp == projectItem.getProject().configurations().activeConfiguration().get()));
            List<HWSolverTemplate> possibleSolvers = new ArrayList<HWSolverTemplate>();
            HWSolverTemplate currentSolver = r.solver().get();
            if (currentSolver instanceof HWSolverTemplateMoM) {
                possibleSolvers.add(currentSolver);
            } else {
                HWSolverTemplateMoM solver = new HWSolverTemplateMoM();
                solver.name().set("Method of Moments Solver");
                possibleSolvers.add(solver);
            }
            if (currentSolver instanceof HWSolverTemplateFDM) {
                possibleSolvers.add(currentSolver);
            } else {
                HWSolverTemplateFDM solver = new HWSolverTemplateFDM();
                solver.name().set("Finite Difference Solver");
                possibleSolvers.add(solver);
            }
            if (currentSolver instanceof HWSolverTemplateFEM) {
                possibleSolvers.add(currentSolver);
            } else {
                HWSolverTemplateFEM solver = new HWSolverTemplateFEM();
                solver.name().set("Finite Elements Solver");
                possibleSolvers.add(solver);
            }
            DefaultPropertiesNodeFolder solverFolder = root.addFolder("folder", "Solver Properties");
            solverFolder.add(new PValueViewProperty("property", app, r.solver(), possibleSolvers.toArray(new HWSolverTemplate[0])));
            if (currentSolver instanceof HWSolverTemplateMoM) {
                HWSolverTemplateMoM s = (HWSolverTemplateMoM) currentSolver;
                solverFolder.add(new PValueViewPropertyE("property", s.frequency()));
                solverFolder.add(new PValueViewPropertyE("property", s.modesCount()));
                solverFolder.add(new PValueViewPropertyE("property", s.circuitType()));
                DefaultPropertiesNodeFolder testFunctionsFolder = solverFolder.addFolder("TestFunctions", "Test Functions");
                int testFunctinoGroupIndex = 1;
                for (MomSolverTestTemplate testFunction : s.testFunctions()) {
                    DefaultPropertiesNodeFolder tf = testFunctionsFolder.addFolder("TestFunctionGroup", "Group " + testFunctinoGroupIndex);
                    tf.putUserObject("index", testFunctinoGroupIndex - 1);
                    tf.putUserObject("item", testFunction);
                    testFunctinoGroupIndex++;
                    tf.add(new PValueViewProperty("property", app, testFunction.name()));
                    tf.add(new PValueViewProperty("property", app, testFunction.description()));
                    if (testFunction instanceof MomSolverTestTemplateSeq) {
                        MomSolverTestTemplateSeq tf2 = (MomSolverTestTemplateSeq) testFunction;
                        tf.add(new PValueViewPropertyE("property", tf2.expr()));
                        tf.add(new PValueViewPropertyE("property", tf2.condition()));
                        DefaultPropertiesNodeFolder psfolder = tf.addFolder("folder", "Params");
                        int index = 0;
                        for (PMapEntry<String, ParamSeqTemplate> param : tf2.params()) {
                            DefaultPropertiesNodeFolder pfolder = psfolder.addFolder("MomSolverTestTemplateSeqParam", param.getValue().name().get());
                            pfolder.putUserObject("index", index);
                            pfolder.putUserObject("name", param.getValue().name().get());
                            pfolder.add(new PValueViewPropertyE("property", param.getValue().from()));
                            pfolder.add(new PValueViewPropertyE("property", param.getValue().to()));
                            pfolder.add(new PValueViewPropertyE("property", param.getValue().condition()));
                            index++;
                        }
                    } else if (testFunction instanceof MomSolverTestTemplateMesh) {
                        MomSolverTestTemplateMesh tf2 = (MomSolverTestTemplateMesh) testFunction;
                        tf.add(new PValueViewPropertyE("property", tf2.complexity()));
                        tf.add(new PValueViewPropertyE("property", tf2.invariance()));
                        tf.add(new PValueViewPropertyE("property", tf2.mesh()));
                        tf.add(new PValueViewPropertyE("property", tf2.meshMinPrecision()));
                        tf.add(new PValueViewPropertyE("property", tf2.meshMaxPrecision()));
                        tf.add(new PValueViewPropertyE("property", tf2.pattern()));

                    } else if (testFunction instanceof MomSolverTestTemplateList) {
                        MomSolverTestTemplateList tf2 = (MomSolverTestTemplateList) testFunction;
                        tf.add(new PValueViewPropertyE("property", tf2.complexity()));
                        int i = 0;
                        for (WritablePExpression<Expr> expression : tf2.expressions()) {
                            PValueViewPropertyE e = new PValueViewPropertyE("TestFunctionListItem", expression);
                            e.putUserObject("index", i);
                            tf.add(e);
                            i++;
                        }
                    } else {
                        throw new IllegalArgumentException("Unsupported");
                    }
                }
            } else if (currentSolver instanceof HWSolverTemplateFDM) {
                HWSolverTemplateFDM s = (HWSolverTemplateFDM) currentSolver;
            } else if (currentSolver instanceof HWSolverTemplateFEM) {
                HWSolverTemplateFEM s = (HWSolverTemplateFEM) currentSolver;
            }
        }
    }

}
