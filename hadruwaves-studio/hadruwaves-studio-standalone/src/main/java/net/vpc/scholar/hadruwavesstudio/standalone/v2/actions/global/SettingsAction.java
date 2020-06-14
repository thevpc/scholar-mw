/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.actions.global;

import java.awt.event.ActionEvent;
import net.vpc.common.app.AbstractAppAction;
import net.vpc.common.app.Application;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;

/**
 *
 * @author vpc
 */
public class SettingsAction extends AbstractAppAction {

    private final HadruwavesStudio studio;

    public SettingsAction(Application aplctn, final HadruwavesStudio studio) {
        super(aplctn, "Settings");
        this.studio = studio;
    }

    @Override
    public void actionPerformedImpl(ActionEvent e) {
        studio.openSettings();
    }
    @Override
    public void refresh() {

    }

}
