/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.actions;

import java.util.logging.Level;
import net.thevpc.echo.api.AppEvent;
import net.thevpc.common.msg.JFormattedMessage;
import net.thevpc.echo.api.UndoableAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.thevpc.echo.api.AppPropertiesNodeFolder;
import net.thevpc.scholar.hadruwaves.mom.solver.HWSolverTemplateFDM;
import net.thevpc.scholar.hadruwaves.mom.solver.HWSolverTemplateFEM;
import net.thevpc.scholar.hadruwaves.mom.solver.HWSolverTemplateMoM;
import net.thevpc.scholar.hadruwaves.mom.solver.MomSolverTestTemplate;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwaves.solvers.HWSolverTemplate;
import net.thevpc.common.msg.Message;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions.HWUnduableAction;

/**
 *
 * @author vpc
 */
public abstract class MoMNewFunctionGroupActionBase extends HWUnduableAction {

    private HadruwavesStudio studio;

    public MoMNewFunctionGroupActionBase(final HadruwavesStudio studio, String id) {
        super(studio.app(), id);
        this.studio = studio;
    }

    @Override
    protected UndoableAction createUndo() {
        return new UndoableAction() {
            MomSolverTestTemplate added;
            HWSolverTemplate msolver;

            @Override
            public Message doAction(AppEvent event) {
                msolver = getSolverMoM();
                if (msolver == null) {
                    return null;
                }
                MomSolverTestTemplate added0 = read(msolver);
                if (added0 != null) {
                    if (msolver instanceof HWSolverTemplateMoM) {
                        this.added = added0;
                        ((HWSolverTemplateMoM) msolver).testFunctions().add(added);
                        studio.app().activeProperties().get().refresh();
                        studio.props().updateRoot();
                        return new JFormattedMessage(Level.INFO, "Add Test JFunction {0}", new Object[]{added});
                    }
                }
                return null;
            }

            @Override
            public void undoAction(AppEvent event) {
                if (added != null) {
                    ((HWSolverTemplateMoM) msolver).testFunctions().remove(added);
                    studio.app().activeProperties().get().refresh();
                    studio.props().updateRoot();
                }
            }

            @Override
            public void redoAction(AppEvent event) {
                ((HWSolverTemplateMoM) msolver).testFunctions().add(added);
                studio.app().activeProperties().get().refresh();
                studio.props().updateRoot();
            }
        };
    }

    protected HWSolverTemplateMoM getSolverMoM() {
        HWConfigurationRun config0 = getCurrentConfig();
        if (config0 != null) {
            HWSolverTemplate solver = config0.solver().get();
            if (solver instanceof HWSolverTemplateMoM) {
                return (HWSolverTemplateMoM) solver;
            }
        }
        return null;
    }

    protected HWSolverTemplateFEM getSolverFEM() {
        HWConfigurationRun config0 = getCurrentConfig();
        if (config0 != null) {
            HWSolverTemplate solver = config0.solver().get();
            if (solver instanceof HWSolverTemplateFEM) {
                return (HWSolverTemplateFEM) solver;
            }
        }
        return null;
    }

    protected HWSolverTemplateFDM getSolverFDM() {
        HWConfigurationRun config0 = getCurrentConfig();
        if (config0 != null) {
            HWSolverTemplate solver = config0.solver().get();
            if (solver instanceof HWSolverTemplateFDM) {
                return (HWSolverTemplateFDM) solver;
            }
        }
        return null;
    }

    protected HWConfigurationRun getCurrentConfig() {
        return (HWConfigurationRun) studio.app().activeProperties().get().root().object();
    }

    protected abstract MomSolverTestTemplate read(HWSolverTemplate r);
//    {
//        //                studio.explorer.refreshTools();
//        String s=JOptionPane.showInputDialog(getMainComponent(), "New Group");
//        return s;
//    }

    @Override
    public void refresh() {
        AppPropertiesNodeFolder oo = getItem();
        setAccessible(oo != null);
    }

    public AppPropertiesNodeFolder getItem() {
        AppPropertiesNodeFolder i = (AppPropertiesNodeFolder) studio.props().getSelectedItem(
                n -> n.type().equals("TestFunctions")
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
