/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.actions;

import java.util.logging.Level;
import javax.swing.JOptionPane;
import net.thevpc.echo.api.AppEvent;
import net.thevpc.echo.api.AppPropertiesNode;
import net.thevpc.common.msg.JFormattedMessage;
import net.thevpc.echo.api.UndoableAction;
import net.thevpc.echo.swing.helpers.actions.SwingAppUndoableAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadruwaves.mom.solver.HWSolverTemplateFDM;
import net.thevpc.scholar.hadruwaves.mom.solver.HWSolverTemplateFEM;
import net.thevpc.scholar.hadruwaves.mom.solver.HWSolverTemplateMoM;
import net.thevpc.scholar.hadruwaves.mom.solver.MomSolverTestTemplate;
import net.thevpc.scholar.hadruwaves.mom.solver.test.MomSolverTestTemplateList;
import net.thevpc.scholar.hadruwaves.mom.solver.test.MomSolverTestTemplateSeq;
import net.thevpc.scholar.hadruwaves.mom.solver.test.ParamSeqTemplate;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwaves.props.WritablePExpression;
import net.thevpc.scholar.hadruwaves.solvers.HWSolverTemplate;
import net.thevpc.common.msg.Message;

/**
 *
 * @author vpc
 */
public class RemoveFromPropsAction extends SwingAppUndoableAction {

    private HadruwavesStudio studio;

    public RemoveFromPropsAction(final HadruwavesStudio studio) {
        super(studio.app(), "RemoveFromProps");
        this.studio = studio;
    }

    @Override
    protected UndoableAction createUndo() {
        return new UndoableAction() {
            Object removed;
            MomSolverTestTemplateList TestFunctionListItem_list;
            MomSolverTestTemplateSeq MomSolverTestTemplateSeqParam_seq;
            HWSolverTemplate msolver;
            String removeType;
            int index;
            String indexName;

            @Override
            public Message doAction(AppEvent event) {
                msolver = getSolverMoM();
                if (msolver == null) {
                    return null;
                }
                if (JOptionPane.showConfirmDialog(getMainComponent(), "Are you sure?", "Attention", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    AppPropertiesNode i = getItem();
                    removeType = i.type();
                    if ("TestFunctionListItem".equals(removeType)) {
                        index = (Integer) i.getUserObject("index");
                        if (msolver instanceof HWSolverTemplateMoM) {
                            TestFunctionListItem_list = (MomSolverTestTemplateList) i.parent().object();
                            this.removed = TestFunctionListItem_list.expressions().removeAt(index);
                            studio.app().activeProperties().get().refresh();
                            studio.props().updateRoot();
                            return new JFormattedMessage(Level.INFO, "Remove Test JFunction {0}", new Object[]{index});
                        }
                    }
                    if ("TestFunctionGroup".equals(removeType)) {
                        index = (Integer) i.getUserObject("index");
                        if (msolver instanceof HWSolverTemplateMoM) {
                            this.removed = ((HWSolverTemplateMoM) msolver).testFunctions().removeAt(index);
                            studio.app().activeProperties().get().refresh();
                            studio.props().updateRoot();
                            return new JFormattedMessage(Level.INFO, "Remove Test JFunction {0}", new Object[]{index});
                        }
                    }
                    if ("MomSolverTestTemplateSeqParam".equals(removeType)) {
                        indexName = (String) i.getUserObject("name");
                        MomSolverTestTemplateSeqParam_seq = (MomSolverTestTemplateSeq) i.parent().getUserObject("item");
                        if (msolver instanceof HWSolverTemplateMoM) {
                            this.removed = MomSolverTestTemplateSeqParam_seq.params().remove(indexName);
                            studio.app().activeProperties().get().refresh();
                            studio.props().updateRoot();
                            return new JFormattedMessage(Level.INFO, "Remove Seq Param {0}", new Object[]{indexName});
                        }
                    }

                }
                return null;
            }

            @Override
            public void undoAction(AppEvent event) {
                if (removed != null) {
                    if ("TestFunctionListItem".equals(removeType)) {
                        TestFunctionListItem_list.expressions().add(index, (WritablePExpression<Expr>) removed);
                        studio.app().activeProperties().get().refresh();
                        studio.props().updateRoot();
                    }
                    if ("TestFunctionGroup".equals(removeType)) {
                        ((HWSolverTemplateMoM) msolver).testFunctions().add(index, (MomSolverTestTemplate) removed);
                        studio.app().activeProperties().get().refresh();
                        studio.props().updateRoot();
                    }
                    if ("MomSolverTestTemplateSeqParam".equals(removeType)) {
                        MomSolverTestTemplateSeqParam_seq.params().remove(indexName);
                        studio.app().activeProperties().get().refresh();
                        studio.props().updateRoot();
                    }
                }
            }

            @Override
            public void redoAction(AppEvent event) {
                if ("TestFunctionListItem".equals(removeType)) {
                    TestFunctionListItem_list.expressions().removeAt(index);
                    studio.app().activeProperties().get().refresh();
                    studio.props().updateRoot();
                }
                if ("TestFunctionGroup".equals(removeType)) {
                    this.removed = ((HWSolverTemplateMoM) msolver).testFunctions().removeAt(index);
                    studio.app().activeProperties().get().refresh();
                    studio.props().updateRoot();
                }
                if ("MomSolverTestTemplateSeqParam".equals(removeType)) {
                    MomSolverTestTemplateSeqParam_seq.params().add((ParamSeqTemplate) removed);
                    studio.app().activeProperties().get().refresh();
                    studio.props().updateRoot();
                }
            }
        };
    }

    protected HWSolverTemplateMoM getSolverMoM() {
        HWConfigurationRun config0 = getCurrentConfig();
        if (config0 != null) {
            return null;
        }
        HWSolverTemplate solver = config0.solver().get();
        if (solver instanceof HWSolverTemplateMoM) {
            return (HWSolverTemplateMoM) solver;
        }
        return null;
    }

    protected HWSolverTemplateFEM getSolverFEM() {
        HWConfigurationRun config0 = getCurrentConfig();
        if (config0 != null) {
            return null;
        }
        HWSolverTemplate solver = config0.solver().get();
        if (solver instanceof HWSolverTemplateFEM) {
            return (HWSolverTemplateFEM) solver;
        }
        return null;
    }

    protected HWSolverTemplateFDM getSolverFDM() {
        HWConfigurationRun config0 = getCurrentConfig();
        if (config0 != null) {
            return null;
        }
        HWSolverTemplate solver = config0.solver().get();
        if (solver instanceof HWSolverTemplateFDM) {
            return (HWSolverTemplateFDM) solver;
        }
        return null;
    }

    protected HWConfigurationRun getCurrentConfig() {
        return (HWConfigurationRun) studio.app().activeProperties().get().root().object();
    }

    @Override
    public void refresh() {
        AppPropertiesNode oo = getItem();
        setAccessible(oo != null);
    }

    public AppPropertiesNode getItem() {
        AppPropertiesNode i = (AppPropertiesNode) studio.props().getSelectedItem(
                n
                -> n.type().equals("TestFunctionListItem")
                || n.type().equals("TestFunctionGroup")
                || n.type().equals("MomSolverTestTemplateSeqParam")
        );
        if (i == null) {
            return null;
        }
        HWConfigurationRun r = getCurrentConfig();
        if (r == null) {
            return null;
        }
        HWSolverTemplate solver = r.solver().get();
        if (solver == null) {
            return null;
        }
        return i;
    }
}
